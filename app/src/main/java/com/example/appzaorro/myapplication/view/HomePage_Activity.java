package com.example.appzaorro.myapplication.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appzaorro.myapplication.R;
import com.example.appzaorro.myapplication.controller.ModelManager;
import com.example.appzaorro.myapplication.controller.SearchAddressManager;
import com.example.appzaorro.myapplication.model.Config;
import com.example.appzaorro.myapplication.model.Constants;
import com.example.appzaorro.myapplication.model.Event;
import com.example.appzaorro.myapplication.model.Ldshareadprefernce;
import com.example.appzaorro.myapplication.model.Operations;
import com.example.appzaorro.myapplication.model.Util;
import com.example.appzaorro.myapplication.model.database.LocationDatabase;
import com.example.appzaorro.myapplication.model.getter.LocationBean;
import com.example.appzaorro.myapplication.model.getter.SearchAddressBean;
import com.example.appzaorro.myapplication.view.login.Login_activity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.GONE;
import static com.example.appzaorro.myapplication.R.id.map;


public class HomePage_Activity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleMap googleMap;
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    DrawerLayout drawerLayout;
    ArrayList<SearchAddressBean> address;
    LocationRequest locationRequest;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationDatabase locationDatabase;
    List<LocationBean> previouslocation;
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    LocationManager locationManager;
    private int fingers = 0;
    private long lastZoomTime = 0;
    private float lastSpan = -1;
    private int STORAGE_PERMISSION_CODE = 23;
    long startTime;

    final int MAX_DURATION = 200;
    private ScaleGestureDetector gestureDetector;
    private Handler handler = new Handler();
    private LocationRequest mLcationrequest = null;

    ProgressDialog progressdialog;
    EditText edtdrop_loc;
    CardView cardView;
    ArrayList<String> driver_name, latititude, longititude, profilepic;
    RelativeLayout picklayout, locationlayout, timelayout;
    Toolbar toolbar;
    String place_id = "", carStatus = "", deststatus = "required", usermobile, completeAddress, droplat, droplng;

    double onmaplat, onmaplng, currentlat, currentlng;
    TextView txtdate, txttime, setAddress, txtdestinationaddress;
    Marker marker;
    String pathstatus = "";
    private EventBus bus = EventBus.getDefault();
    Activity activity;
    float currentsZoomlevel = 16f;
    SupportMapFragment mapFragment;
    ImageView sourcesearch_img, destsearch_img, sourceview, destview, circleimageview, pinimage1;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_);
        initviews();



        picklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage_Activity.this, Search_Activity.class);
                Config.SearchStatus = "PICK";
                Log.e("click on ", "picklayout");
                startActivity(intent);
                return;
            }
        });

        txtdestinationaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String filled_addressed = txtdestinationaddress.getText().toString();
                Intent intent = new Intent(HomePage_Activity.this, Search_Activity.class);
                Config.SearchStatus = "DEST";
                Log.e("click on ", "destination");
                startActivity(intent);
            }
        });


    }


    public void initviews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        activity = this;
        locationDatabase = new LocationDatabase(activity);

        progressdialog = new ProgressDialog(HomePage_Activity.this);
        locationlayout = (RelativeLayout) findViewById(R.id.fullpinlayout);
        circleimageview = (ImageView) findViewById(R.id.circleimage);
        pinimage1 = (ImageView) findViewById(R.id.pin);
        txttime = (TextView) findViewById(R.id.minutevalue);
        timelayout = (RelativeLayout) findViewById(R.id.timelayout);

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(7000);
        locationRequest.setSmallestDisplacement(1);

        Log.e("user id", Ldshareadprefernce.readString(activity, "USERID"));

        picklayout = (RelativeLayout) findViewById(R.id.picklayout);
        txtdestinationaddress = (TextView) findViewById(R.id.txtdestaddress);
        destsearch_img = (ImageView) findViewById(R.id.destinsearch);
        cardView = (CardView) findViewById(R.id.cardview);
        setAddress = (TextView) findViewById(R.id.txtaddress);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        initNavigationDrawer();
        getLastBestStaleLocation();


    }


    @Override
    public void onLocationChanged(Location location) {

        initCamera(location);

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    // customer request  dialong box
    public class ViewDialog {

        public void showDialog(final Activity activity, String msg) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.cutomer_request_pop);
            final EditText priceedt = (EditText) dialog.findViewById(R.id.edtprice);
            Button request = (Button) dialog.findViewById(R.id.btnrequest);
            Button cancel = (Button) dialog.findViewById(R.id.btncancel);
            final EditText edtpickup_loc, edtprice, edtdescription;
            edtpickup_loc = (EditText) dialog.findViewById(R.id.picked_location);
            edtdrop_loc = (EditText) dialog.findViewById(R.id.destination);
            //  edtprice = (EditText) dialog.findViewById(R.id.price);
            edtdescription = (EditText) dialog.findViewById(R.id.description);
            txtdate = (TextView) dialog.findViewById(R.id.datepicker);
            txttime = (TextView) dialog.findViewById(R.id.timepicker);
            edtpickup_loc.setText(completeAddress);
            edtdrop_loc.setText(Config.destinationAddress);
            edtdrop_loc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Search_Activity.class);
                    intent.putExtra("SEARCHSTATUS", "DROP");
                    startActivity(intent);
                }
            });
            txtdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date_Time date_time = new Date_Time(HomePage_Activity.this);
                    date_time.dateDialog();


                }
            });
            txttime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date_Time date_time = new Date_Time(HomePage_Activity.this);
                    date_time.timedialog();

                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    // destinationlayout.setVisibility(GONE);
                    txtdestinationaddress.setText("");
                    txtdestinationaddress.setHint("Destination Required");

                }
            });


            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (edtdrop_loc.getText().toString().isEmpty()) {
                        edtdrop_loc.setError("enter drop location");
                    } else if (edtdescription.getText().toString().trim().isEmpty()) {

                        edtdescription.setError("set prouct discription");
                    } else if (priceedt.getText().toString().isEmpty()) {
                        priceedt.setError("please fill the price");

                    } else {
                        progressdialog = new ProgressDialog(activity);
                        progressdialog.show();
                        Config.startpoint = new LatLng(onmaplat, onmaplng);
                        Config.endpoint = new LatLng(Double.parseDouble(droplat), Double.parseDouble(droplng));

                        ModelManager.getInstance().getRequesDriverManager().RequesDriverManager(activity, Operations.getCustomerRequest(activity,
                                Ldshareadprefernce.readString(activity, "USERID"), edtpickup_loc.getText().toString(), edtdrop_loc.getText().toString(),
                                String.valueOf(onmaplat), String.valueOf(onmaplng), String.valueOf(droplat), String.valueOf(droplng), txtdate.getText().toString(),
                                txttime.getText().toString(), edtdescription.getText().toString(), priceedt.getText().toString()));
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        }

    }





    @Subscribe
    public void onEvent(Event event) {
        Log.e("call", "eventbus");

        switch (event.getKey()) {

            case "DATE":
                txtdate.setText(event.getValue());
                break;
            case "TIME":
                txttime.setText(event.getValue());
                break;
            case "SUCCESS":
                progressdialog.dismiss();
                Toast.makeText(HomePage_Activity.this, "" + event.getValue(), Toast.LENGTH_LONG).show();

                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(event.getValue())
                        .setContentText("Please wait for some time until you dont any response from the driver")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            /*    progressdialog = ProgressDialog.show(activity, "Please wait.",
                                        "Fetching route information.", true);*/
                               /* Routing routing = new Routing.Builder()
                                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                                        .withListener((RoutingListener) activity)
                                        .alternativeRoutes(true)
                                        .waypoints(Config.startpoint, Config.endpoint)
                                        .build();
                                routing.execute();*/
                                txtdestinationaddress.setText("Destination  required");

                            }
                        })
                        .show();
                break;
            case "FAILED":
                progressdialog.dismiss();
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Request to Driver")
                        .setContentText(event.getValue())
                        .show();

                sourcesearch_img.setVisibility(GONE);
                destsearch_img.setVisibility(GONE);
                sourceview.setVisibility(View.VISIBLE);
                destview.setVisibility(View.VISIBLE);
                break;
            case Constants.nearbydriver:
                driver_name = new ArrayList<>();
                latititude = new ArrayList<>();
                longititude = new ArrayList<>();
                profilepic = new ArrayList<>();
                String response = event.getValue();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        if (Integer.parseInt(id) > 0) {
                            String firstname = jsonObject1.getString("firstname");
                            String lastname = jsonObject1.getString("lastname");
                            String fullname = firstname + " " + lastname;
                            driver_name.add(fullname);
                            latititude.add(jsonObject1.getString("latitude"));
                            longititude.add(jsonObject1.getString("longitude"));
                            profilepic.add(jsonObject1.getString("profile_pic"));
                            double drivetlat = Double.parseDouble(jsonObject1.getString("latitude"));
                            double drivetlng = Double.parseDouble(jsonObject1.getString("longitude"));
                            LatLng latLng = new LatLng(drivetlat, drivetlng);
                            Marker melbourne = googleMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_hatchback)));


                        } else {
                            carStatus = jsonObject1.getString("message");
                            Toast.makeText(activity, "No driver found  nearby you", Toast.LENGTH_LONG).show();
                        }
                    }


                } catch (Exception ex) {
                }
                break;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        if (googleMap != null) {

            if (Config.SearchStatus.equals("PICK")) {

                address = new ArrayList<>();
                address = SearchAddressManager.addressList;


                LatLng latLng = new LatLng(Double.parseDouble(Ldshareadprefernce.readString(activity, "source_latitude")), Double.parseDouble(Ldshareadprefernce.readString(activity, "source_longitude")));

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, currentsZoomlevel);
                googleMap.animateCamera(cameraUpdate);
                // location_layout.setVisibility(View.VISIBLE);
                // pinimage.setVisibility(View.VISIBLE);
                locationlayout.setVisibility(View.VISIBLE);
                onmaplat = latLng.latitude;
                onmaplng = latLng.longitude;
                Log.e("onnnn", String.valueOf(onmaplat) + " off\n" + String.valueOf(onmaplng));

                Log.e("current lat long", String.valueOf(onmaplat + "   " + String.valueOf(onmaplng)));

                // here get the address from the Searchbean classs
                SearchAddressBean searchAddressBean = address.get(0);

                previouslocation = locationDatabase.getAllFriends();

                if (previouslocation.size() < 5) {
                    LocationBean bean = new LocationBean(activity, String.valueOf(onmaplat), String.valueOf(onmaplng), searchAddressBean.getAddress(), searchAddressBean.getArea());
                    boolean flag = locationDatabase.insertvalue(bean);
                    previouslocation.add(bean);
                    if (flag) {
                        Toast.makeText(activity, "data are inserted", Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(activity, "not inserted", Toast.LENGTH_LONG).show();
                    }
                } else {

                    Toast.makeText(activity, "data excced insert limit", Toast.LENGTH_LONG).show();
                }


                completeAddress = Util.getCompleteAddressString(activity, onmaplat, onmaplng);

                Config.SearchStatus = "";

                return;


            } else if (Config.SearchStatus.equals("DEST")) {
                if (!Ldshareadprefernce.readString(activity, "dest_longitude").isEmpty()) {

                    Config.destinationAddress = Util.getDestinationAddress(activity, Double.parseDouble(Ldshareadprefernce.readString(activity, "dest_latitude")),
                            Double.parseDouble(Ldshareadprefernce.readString(activity, "dest_longitude")));
                    // txtdestinationaddress.setText(""+Config.destinationAddress);
                    droplat = String.valueOf(Ldshareadprefernce.readString(activity, "dest_latitude"));
                    droplng = String.valueOf(Ldshareadprefernce.readString(activity, "dest_longitude"));
                    txtdestinationaddress.setText("" + Config.destinationAddress);

                    ViewDialog alert = new ViewDialog();
                    deststatus = "required";
                    alert.showDialog(activity, "Error de conexión al servidor");
                    Ldshareadprefernce.putString(activity, "dest_latitude", "");
                    Ldshareadprefernce.putString(activity, "dest_longitude", "");

                    return;

                }
            } else if (Config.SearchStatus.equals("DESTCHOOSE")) {
                if (!Ldshareadprefernce.readString(activity, "dest_longitude").isEmpty()) {


                    Config.destinationAddress = Util.getDestinationAddress(activity, Double.parseDouble(Ldshareadprefernce.readString(activity, "dest_latitude")), Double.parseDouble(Ldshareadprefernce.readString(
                            activity, "dest_longitude"
                    )));
                    txtdestinationaddress.setText("" + Config.destinationAddress);
                    droplat = Ldshareadprefernce.readString(activity, "dest_latitude");
                    droplng = Ldshareadprefernce.readString(activity, "dest_longitude");

                    ViewDialog alert = new ViewDialog();
                    deststatus = "required";
                    alert.showDialog(HomePage_Activity.this, "Error de conexión al servidor");
                    Ldshareadprefernce.putString(activity, "dest_latitude", "");
                    Ldshareadprefernce.putString(activity, "dest_longitude", "");

                    return;
                }

            } else if (Config.SearchStatus.equals("PICKCHOOSE")) {
                LatLng latLng = new LatLng(Double.parseDouble(Ldshareadprefernce.readString(activity, "source_latitude")), Double.parseDouble(Ldshareadprefernce.readString(activity, "source_longitude")));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, currentsZoomlevel);
                googleMap.animateCamera(cameraUpdate);
                //  location_layout.setVisibility(View.VISIBLE);
                // pinimage.setVisibility(View.VISIBLE);
                locationlayout.setVisibility(View.VISIBLE);
                onmaplat = latLng.latitude;
                onmaplng = latLng.longitude;

            } else {


                txtdestinationaddress.setText("");
                txtdestinationaddress.setHint("Destination Required");
            }
        }

        String devicetoken = FirebaseInstanceId.getInstance().getToken();
        Log.e("device token ", "" + devicetoken);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;


        View view = mapFragment.getView();
        // googleMap.setMyLocationEnabled(true);
        View locationButton = ((View) view.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        // and next place it, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                locationButton.getLayoutParams();
        // position on right bottom
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.setMargins(50, 50, 50, 50);
        locationButton.setLayoutParams(layoutParams);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);

        initCamera(mLastLocation);



        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                // googleMap.clear();
                if (marker != null) {
                    marker.remove();
                    marker = null;
                }

                LatLng corner;
                Log.e("camera moved", "idle");

                locationlayout.setVisibility(View.VISIBLE);
                circleimageview.setVisibility(View.VISIBLE);
                timelayout.setVisibility(View.VISIBLE);

                txtdestinationaddress.setVisibility(View.VISIBLE);

                if (pathstatus.equals("")) {


                    corner = googleMap.getCameraPosition().target;
                    Log.e("latlng", String.valueOf(corner));

                    onmaplat = corner.latitude;
                    onmaplng = corner.longitude;
                    LatLng latLng = new LatLng(onmaplat, onmaplng);

                    // googleMap.getUiSettings().setZoomControlsEnabled(true);
                    googleMap.getUiSettings().setScrollGesturesEnabled(true);
                    currentsZoomlevel = googleMap.getCameraPosition().zoom;
                    Log.e("zoomlevel", String.valueOf(currentsZoomlevel));
                    if (currentsZoomlevel >= 11f) {

                        pinimage1.setVisibility(View.VISIBLE);
                        Log.e("status of cureent", Config.currentlocation);


                        if (Config.currentlocation.equals("1")) {

                            Log.e("current", "if");


                            completeAddress = Util.getCompleteAddressString(activity, currentlat, currentlng);
                            setAddress.setText("" + completeAddress);
                            //completeAddress = getCompleteAddressString(currentlat, currentlng);
                            onmaplat = currentlat;
                            onmaplng = currentlng;
                            Config.currentlocation = "";
                        } else {

                            Log.e("current", "else");
                            //  completeAddress = getCompleteAddressString(onmaplat, onmaplng);

                            completeAddress = Util.getCompleteAddressString(activity, onmaplat, onmaplng);
                            setAddress.setText(completeAddress);


                        }


                        ModelManager.getInstance().getNearDriverManager().NearDriverManager(activity, Operations.nearByDriverUrl(activity, String.valueOf(onmaplat), String.valueOf(onmaplng)));

                    }
                } else {

                    //  location_layout.setVisibility(View.GONE);
                    //   pinimage.setVisibility(View.GONE);

                }

            }
        });

        googleMap.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
            @Override
            public void onCameraMoveCanceled() {

                Log.e("cameramove cancle", "canclnhhhhhhheddd");


            }
        });
        Log.e("onstop", "method");
        //Config.place_id="";
        Ldshareadprefernce.putString(activity, "dest_latitude", "");
        Ldshareadprefernce.putString(activity, "dest_longitude", "");


        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {

                Log.e("camera moved", "started");

                Log.e("latlongof move", String.valueOf(onmaplat));

                //  pinimage.setVisibility(View.VISIBLE);
                pinimage1.setVisibility(GONE);
                //   location_layout.setVisibility(View.GONE);

                //place_id =null;
                googleMap.getUiSettings().setScrollGesturesEnabled(false);

            }
        });
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                Log.e("On camera", "Move");
                LatLng latLng = new LatLng(onmaplat, onmaplng);
                //  location_layout.setVisibility(View.GONE);
                circleimageview.setVisibility(View.GONE);
                timelayout.setVisibility(View.GONE);
                pinimage1.setVisibility(View.VISIBLE);
                //  pinimage.setVisibility(View.GONE);
                //  pinimage.setVisibility(View.VISIBLE);
                googleMap.getUiSettings().setScrollGesturesEnabled(false);
                googleMap.getUiSettings().setScrollGesturesEnabled(false);
                googleMap.getUiSettings().setRotateGesturesEnabled(true);
                googleMap.getUiSettings().setZoomGesturesEnabled(true);

                if (currentsZoomlevel != googleMap.getCameraPosition().zoom) {
                    googleMap.getUiSettings().setScrollGesturesEnabled(false);
                    // pinimage.setVisibility(View.VISIBLE);
                } else {

                    googleMap.getUiSettings().setScrollGesturesEnabled(true);
                }


            }
        });


        //here click on driver  vichle icon and accept the drivr bidd

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                dialog = Util.driverAcceptDialog(activity);
                dialog.show();
                ImageView imagedriver = (ImageView) dialog.findViewById(R.id.imagevieww);
                TextView drivername = (TextView) dialog.findViewById(R.id.txtdrivername);
                TextView requestid = (TextView) dialog.findViewById(R.id.txtrequestid);
                TextView price = (TextView) dialog.findViewById(R.id.txtpricevalue);
                TextView date = (TextView) dialog.findViewById(R.id.txtdatevalue);
                TextView reject = (TextView) dialog.findViewById(R.id.btReject);
                TextView accept = (TextView) dialog.findViewById(R.id.btnaccept);
                return false;
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Toast.makeText(activity, "onconnected", Toast.LENGTH_SHORT).show();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation==null){

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,locationRequest, (com.google.android.gms.location.LocationListener) this);
        }


    //    initCamera(mLastLocation);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e("connection failed", "check it again");

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        mGoogleApiClient.connect();

        bus.register(HomePage_Activity.this);
    }

    @Override
    protected void onStop() {

        bus.unregister(HomePage_Activity.this);
        super.onStop();


        if (mGoogleApiClient!=null && mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }


    }
  public void initNavigationDrawer() {


        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.payment:
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(HomePage_Activity.this, Payment_Activity_Method.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.trip:
                        Toast.makeText(getApplicationContext(),"Trip",Toast.LENGTH_LONG).show();
                        Intent intent11 = new Intent(HomePage_Activity.this, Pending_Trips.class);
                        startActivity(intent11);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.notification:
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        Intent intente = new Intent(HomePage_Activity.this, Notification.class);
                        startActivity(intente);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.free_rides:
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        Intent intentt = new Intent(HomePage_Activity.this, FreeRide.class);
                        startActivity(intentt);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.setting:
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(HomePage_Activity.this, Update_Profile_Activity.class);
                        startActivity(intent2);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.help:
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        Intent intent2t = new Intent(HomePage_Activity.this, HelpActivity.class);
                        startActivity(intent2t);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.feedback:
                        Intent intent1 = new Intent(HomePage_Activity.this,FeedBack.class);
                        startActivity(intent1);
                     drawerLayout.closeDrawers();
                        break;

                    case R.id.logout:
                       // SharedPreferences.Editor editor = logoutsharead.edit();
                        Ldshareadprefernce.putString(activity,"Logged","");
                        Intent intent3=new Intent(HomePage_Activity.this,Login_activity.class);
                        startActivity(intent3);
                        finish();
                        drawerLayout.closeDrawers();
                        break;

                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.txtphonenumber);
        Log.e("UserDeatail of name",""+Ldshareadprefernce.readString(activity,"FULLNAME"));

        if (usermobile == null) {

            name.setText(Ldshareadprefernce.readString(activity,"FULLNAME"));
        }
       else {

            name.setText(Ldshareadprefernce.readString(activity,"FULLNAME") + "\n" + Ldshareadprefernce.readString(activity,"MOBILENUMBER"));
        }


        ImageView imageView = (ImageView) header.findViewById(R.id.nav_image);
      Picasso.with(this)
         .load("https://www.simplifiedcoding.net/wp-content/uploads/2015/10/advertise.png")
         .into(imageView);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_home_page_);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }


    private void initCamera(Location location) {

        if (place_id.equals("")) {

            if (Config.SearchStatus.equals("PICKCHOOSE")) {

                CameraPosition position = CameraPosition.builder()
                        .target(new LatLng(Double.parseDouble(Config.recentlat),
                                Double.parseDouble(Config.recentlng)))
                        .zoom(14f)
                        .bearing(0.0f)
                        .tilt(0.0f)
                        .build();
                googleMap.moveCamera(CameraUpdateFactory
                        .newCameraPosition(position));


            } else {

                if (location != null) {
                    CameraPosition position = CameraPosition.builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))
                            .zoom(14f)
                            .bearing(0.0f)
                            .tilt(0.0f)
                            .build();
                    currentlat = location.getLatitude();
                    currentlng = location.getLongitude();
                    Config.currentlocation = "1";
                    // pinimage.setVisibility(GONE);
                    completeAddress = Util.getCompleteAddressString(activity, currentlat, currentlng);
                    setAddress.setText("" + completeAddress);
                    //completeAddress = getCompleteAddressString(currentlat, currentlng);

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));

                    locationlayout.setVisibility(View.VISIBLE);
                     locationManager.removeUpdates(this);

                } else {


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getLastBestStaleLocation();
                        }
                    }, 1000);


                }
            }
        }

        gestureDetector = new ScaleGestureDetector(activity, new ScaleGestureDetector.OnScaleGestureListener()
        {
            @Override
            public boolean onScale(ScaleGestureDetector detector)
            {
                if (lastSpan == -1)
                {
                    lastSpan = detector.getCurrentSpan();
                }
                else if (detector.getEventTime() - lastZoomTime >= 50)
                {
                    lastZoomTime = detector.getEventTime();
                    googleMap.animateCamera(CameraUpdateFactory.zoomBy(Util.getZoomValue(activity,detector.getCurrentSpan(), lastSpan)), 50, null);
                    lastSpan = detector.getCurrentSpan();
                }
                return false;
            }
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector)
            {
                lastSpan = -1;
                return true;
            }
            @Override
            public void onScaleEnd(ScaleGestureDetector detector)
            {
                lastSpan = -1;
            }
        });
    }

    public Location getLastBestStaleLocation() {
        Location bestresult = null;

        Log.e("call","getbestlocation");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return null;
        }
        Location gpslocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location networklocation= locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (gpslocation != null && networklocation != null)
        {
            if (gpslocation.getTime() > networklocation.getTime())
            {
                bestresult = gpslocation;
                Log.e("result", "both location are found---- "+ bestresult.getLatitude());

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
            }
            else {
                bestresult = networklocation;
                Log.e("result", "network location ---- "+ bestresult.getLatitude());

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
            }
        }
        else if (gpslocation != null)
        {
            bestresult = gpslocation;
            Log.e("result", "gps location only found---- "+ bestresult.getLatitude());

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        } else if (networklocation != null)
        {
            bestresult = networklocation;
            Log.e("result", "network location only found--- "+ bestresult.getLatitude());
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
            Log.e("",bestresult.toString());
        }
        return bestresult;
    }

 @Override
 public boolean dispatchTouchEvent(MotionEvent ev)
 {
     switch (ev.getAction() & MotionEvent.ACTION_MASK)
     {
         case MotionEvent.ACTION_POINTER_DOWN:
             fingers = fingers + 1;
             break;
         case MotionEvent.ACTION_POINTER_UP:
         fingers = fingers - 1;
             break;
         case MotionEvent.ACTION_UP:
             startTime = System.currentTimeMillis();fingers = 0;
             break;
         case MotionEvent.ACTION_DOWN:
             fingers = 1;
             if(System.currentTimeMillis() - startTime <= MAX_DURATION)
             {
                 Log.e("double", "Double tapped");
                 googleMap.getUiSettings().setScrollGesturesEnabled(false);
             }
             break;
       }
     if (fingers > 1)
     {
         disableScrolling();
     }
     else if (fingers < 1)
     {
         enableScrolling();
     }
     if (fingers > 1)
     {
         return gestureDetector.onTouchEvent(ev);
     }
     else
     {
         return super.dispatchTouchEvent(ev);
     }
 }
    private void enableScrolling()
    {
        if (googleMap != null && !googleMap.getUiSettings().isScrollGesturesEnabled())
        {
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run() {
                    googleMap.getUiSettings().setAllGesturesEnabled(true);}}, 50);}
    }
    private void disableScrolling()
    {
        handler.removeCallbacksAndMessages(null);
        if (googleMap != null && googleMap.getUiSettings().isScrollGesturesEnabled())
        {
            googleMap.getUiSettings().setAllGesturesEnabled(false);}
    }

    public boolean checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            else
            {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        }
        else
        {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                Util.showGPSDisabledAlertToUser(activity);
            }

            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Util.showGPSDisabledAlertToUser(activity);
                        }
                    } else {
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                        Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                    }
                    return;
                }
                // other 'case' lines to check for other
                // permissions this app might request
            }
        }
    }


}
