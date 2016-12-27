package com.example.appzaorro.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appzaorro.myapplication.com.getter.Create_EVENTBUS;
import com.example.appzaorro.myapplication.com.getter.Operations;
import com.example.appzaorro.myapplication.com.login.com.Login_activity;
import com.example.appzaorro.myapplication.com.task.excute.RequestToDriver;
import com.example.appzaorro.myapplication.com.update.com.Update_Profile_Activity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.appzaorro.myapplication.R.id.map;

public class HomePage_Activity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    GoogleMap googleMap;
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    DrawerLayout drawerLayout;
    LinearLayout linearLayout;
    ProgressDialog progressdialog;
    TextView updateminute;
    EditText edtdrop_loc;
    Intent intent;
    ImageView pinimage;
    CardView cardView;
    SharedPreferences userDeatil,logoutsharead,customedertail;
    ArrayList<String>driver_name,latititude,longititude,profilepic;
    RelativeLayout location_layout;
    String strAdd = "",searchlocationurl="https://maps.googleapis.com/maps/api/place/details/json?placeid=";
    Toolbar toolbar;
    String place_id="";
    String carStatus="",user_id,SearchStatus;
    String userfirstname,userEmailid,userimage,usermobile,completeAddress;
    double onmaplat,onmaplng;
    TextView txtdate, txttime;
    Marker marker;
    private EventBus bus = EventBus.getDefault();
    SharedPreferences sharedPreferences;
    Activity activity;
    TextView setAddress;
    float currentsZoomlevel =16f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home Page");
        setSupportActionBar(toolbar);
        activity = this;
        progressdialog = new ProgressDialog(HomePage_Activity.this);
        location_layout = (RelativeLayout) findViewById(R.id.locationgetting);
        linearLayout = (LinearLayout) findViewById(R.id.location_layout);
        cardView = (CardView) findViewById(R.id.cardview);
        updateminute =(TextView)findViewById(R.id.txtminute);
        setAddress= (TextView)findViewById(R.id.txtaddress);
        pinimage =(ImageView)findViewById(R.id.pin);
        linearLayout.setVisibility(View.GONE);
        userDeatil = getSharedPreferences("USERDETAIL", Context.MODE_PRIVATE);
        customedertail = getSharedPreferences("CUSTOMER_DETAIL", Context.MODE_PRIVATE);
        user_id =customedertail.getString("USERID","");
        userfirstname = userDeatil.getString("FULLNAME","");
        userEmailid =userDeatil.getString("EMAILID","");
        userimage = userDeatil.getString("PROFILEPIC","");
        usermobile = userDeatil.getString("MOBILENUMBER","");


        logoutsharead = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);

        updateminute.setText("  1"+"\n"+"Min");
        initNavigationDrawer();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage_Activity.this, Search_Activity.class);
                intent.putExtra("SEARCHSTATUS","PICK");
                Config.SearchStatus ="PICK";
                startActivity(intent);

            }
        });
        location_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("layout ","click");
                ViewDialog alert = new ViewDialog();
                alert.showDialog(HomePage_Activity.this, "Error de conexión al servidor");

            }
        });


    }
    // customer request  dialong box
            public class ViewDialog {

                public void showDialog(final Activity activity, String msg) {
                    final Dialog dialog = new Dialog(activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.cutomer_request_pop);
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
                    edtdrop_loc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(),Search_Activity.class);
                            intent.putExtra("SEARCHSTATUS","DROP");
                            startActivity(intent);
                        }
                    });
                    txtdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Date_Time  date_time = new Date_Time(HomePage_Activity.this);
                           date_time.dateDialog();




                        }
                    });
                    txttime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Date_Time  date_time = new Date_Time(HomePage_Activity.this);
                            date_time.timedialog();

                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            dialog.dismiss();
                        }
                    });


                    request.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (edtdrop_loc.getText().toString().isEmpty()) {
                                edtdrop_loc.setError("enter drop location");
                            } /*else if (edtprice.getText().toString().trim().isEmpty()) {

                                edtprice.setError("enter your price");
                            } */
                            else if (edtdescription.getText().toString().trim().isEmpty()) {

                                edtdescription.setError("set prouct discription");
                            } else {

                                RequestToDriver driver = new RequestToDriver();
                               /* driver.requestDriver(activity, Operations.getCustomerRequest(activity,user_id,
                                        edtpickup_loc.getText().toString(),edtdrop_loc.getText().toString()
                                ,txtdate.getText().toString(),txttime.getText().toString(),edtdescription.getText().toString(),
                                        String.valueOf(onmaplat),String.valueOf(onmaplng)));*/
                                Log.e("send detail",edtpickup_loc.getText().toString()+"\n"+edtdrop_loc.getText().toString());

                                driver.requestDriver(activity, Operations.getCustomerRequest(activity,user_id,
                                        edtpickup_loc.getText().toString(),edtdrop_loc.getText().toString()
                                        ,txtdate.getText().toString(),txttime.getText().toString(),edtdescription.getText().toString(),
                                        "32.11","72.11"));
                               /* new Request_To_Driver().execute("request_driver_for_delivery", user_id, edtpickup_loc.getText().toString().trim(), edtdrop_loc.getText().toString().trim()
                                        , edtprice.getText().toString().trim(), String.valueOf(onmaplat), String.valueOf(onmaplng));*/
                                dialog.dismiss();
                            }


                        }
                    });
                    dialog.show();
                }

                }

    @Subscribe
    public void onEvent(Create_EVENTBUS event){
        Log.e("call","eventbus");

        switch (event.getKey()) {

            case "DATE":
                txtdate.setText(event.getValue());
                break;
            case "TIME":
                txttime.setText(event.getValue());
                break;
            case "SUCCESS":
                Toast.makeText(HomePage_Activity.this,""+event.getValue(),Toast.LENGTH_LONG).show();
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(event.getValue())
                        .setContentText("Please wait for some time until you dont any response from the driver")
                        .show();
                break;
            case "FAILED":
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Request to Driver")
                        .setContentText(event.getValue())
                        .show();
                break;


        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        if (Config.SearchStatus.equals("DROP")){
            Seraclocation(Config.place_id);
            edtdrop_loc.setText(Config.destinationAddress);

        }

    }




    private void Seraclocation( String placeId){


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {

            StringBuilder sb = new StringBuilder(searchlocationurl +placeId);
            sb.append("&key=" + Search_Activity.API_KEY);


            URL url = new URL(sb.toString());
            Log.e("new url", String.valueOf(url));

            conn = (HttpURLConnection) url.openConnection();

            InputStreamReader in = new InputStreamReader(conn.getInputStream());


            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];

            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);

            }

        } catch (MalformedURLException e) {

            Log.e("", "Error processing Places API URL", e);


        } catch (IOException e) {

            Log.e("", "Error connecting to Places API", e);



        } finally {

            if (conn != null) {

                conn.disconnect();

            }

        }
        try {

            // Create a JSON object hierarchy from the results

            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONObject jsonObject = jsonObj.getJSONObject("result");
            JSONObject jsonObject1 = jsonObject.getJSONObject("geometry");
            JSONObject jsonObject2 = jsonObject1.getJSONObject("location");
            String lati= jsonObject2.getString("lat");
            String lngi = jsonObject2.getString("lng");
            Log.e("latng of home ",lati +" " +lngi);
            double latilong = Double.parseDouble(lati);
            double longititude = Double.parseDouble(lngi);
            // Extract the Place descriptions from the results
         //   LatLng cur_Latlng=new LatLng(latilong,longititude); // giving your marker to zoom to your location area.
            if (Config.SearchStatus.equals("DROP")){

                Config.destinationAddress=    getCompleteAddressString(latilong,longititude);


            }
            CameraPosition position = CameraPosition.builder()
                    .target( new LatLng(latilong,
                            longititude ) )
                    .zoom( currentsZoomlevel )
                    .bearing( 0.0f )
                    .tilt( 0.0f )
                    .build();

            googleMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(position));
        completeAddress=    getCompleteAddressString(latilong,longititude);



        } catch (JSONException e) {

            Log.e("", "Cannot process JSON results", e);

        }



    }
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

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


        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                location_layout.setVisibility(View.VISIBLE);
                return null;
            }
            @Override
            public View getInfoContents(Marker marker) {
                Log.e("marker null", "");
                return null;
            }
        });

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng corner;
                Log.e("camera moved","idle");
                location_layout.setVisibility(View.VISIBLE);
                TextView textView = (TextView) findViewById(R.id.txtarrow);
                final TextView txtminute = (TextView) findViewById(R.id.txtminute);
                Log.e("windowinfo", "cvbvcbnncvnv");
                ImageView imageView = (ImageView) findViewById(R.id.image);
                // progressBar.setSecondaryProgress(100);
                Animation startRotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                imageView.startAnimation(startRotateAnimation);

                if (place_id==null) {
                    corner = googleMap.getProjection().getVisibleRegion().latLngBounds.northeast;
                    Log.e("latlng", String.valueOf(corner));
                  /*  double lat = corner.latitude;
                    double lng = corner.longitude;*/
                    onmaplat = corner.latitude;
                    onmaplng = corner.longitude;
                    LatLng latLng = new LatLng(onmaplat,onmaplng);
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                    googleMap.getUiSettings().setScrollGesturesEnabled(true);
                    currentsZoomlevel = googleMap.getCameraPosition().zoom;
                    Log.e("zoomlevel", String.valueOf(currentsZoomlevel));

                    if (currentsZoomlevel>=16f) {

                        completeAddress = getCompleteAddressString(onmaplat, onmaplng);
                        String action = "return_driver_list";
                        new NearByClass().execute(action, "32.11", "72.11");
                    }
                }

            }
        });
        googleMap.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
            @Override
            public void onCameraMoveCanceled() {
                Log.e("camera moved","cancleddd");

            }
        });

        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {

                Log.e("camera moved","started");

                    pinimage.setVisibility(View.VISIBLE);
                    location_layout.setVisibility(View.GONE);
                    place_id =null;
                googleMap.getUiSettings().setScrollGesturesEnabled(false);

            }
        });
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                Log.e("On camera","Move");
                location_layout.setVisibility(View.GONE);
                pinimage.setVisibility(View.VISIBLE);
                if (currentsZoomlevel!=googleMap.getCameraPosition().zoom){

                    googleMap.getUiSettings().setScrollGesturesEnabled(false);

               }
                else{

                    googleMap.getUiSettings().setScrollGesturesEnabled(true);
                }


            }});



    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
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
       Config.Camerastatus ="1";
        Log.e("camera","update");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        initCamera(mLastLocation);
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
                    case R.id.logout:
                        SharedPreferences.Editor editor = logoutsharead.edit();
                        editor.clear();
                        editor.commit();
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
        Log.e("UserDeatail of name",""+userDeatil.getString("FULLNAME",""));
        if (usermobile.equals(null)) {

            name.setText(userDeatil.getString("FULLNAME", ""));
        }
       else {

            name.setText(userDeatil.getString("FULLNAME", "") + "\n" + userDeatil.getString("MOBILENUMBER", ""));
        }
        ImageView imageView = (ImageView) header.findViewById(R.id.nav_image);
        Picasso.with(this)
         .load(userimage)
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

            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            //   googleMap.setTrafficEnabled(true);
            googleMap.setMyLocationEnabled(true);


            CameraPosition position = CameraPosition.builder()
                    .target( new LatLng( location.getLatitude(),
                            location.getLongitude() ) )
                    .zoom( 16f )
                    .bearing( 0.0f )
                    .tilt( 0.0f )
                    .build();
            double lat = location.getLatitude();
            double longi= location.getLongitude();
            LatLng latLng = new LatLng(lat,longi);
            pinimage.setVisibility(View.GONE);
      /* marker= googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_pin)));
        Log.e("curren_location data",lat+"" +longi);*/
            completeAddress=   getCompleteAddressString(lat,longi);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }


            googleMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(position));
            linearLayout.setVisibility(View.VISIBLE);

            intent = getIntent();
            if (intent != null) {
                place_id = intent.getStringExtra("placeid");

                Log.e("placeId", "" + place_id);
                if (place_id!=null){

                    Seraclocation(place_id);
                }
        }
    }

 // getting the full address from Latitude and logitude
 private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {


     Geocoder geocoder = new Geocoder(this, Locale.getDefault());
     try {
         List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
         if (addresses != null) {
             Address returnedAddress = addresses.get(0);
             StringBuilder strReturnedAddress = new StringBuilder("");

             for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                 strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
             }
             strAdd = strReturnedAddress.toString();
             setAddress.setText(strAdd);
             Log.e("Current Address of user",strAdd);
         } else {
             Log.e("", "No Address returned!");
         }
     } catch (Exception e) {
         e.printStackTrace();
         Log.e("", "Canont get Address!");
     }
     return strAdd;
 }

 private class NearByClass extends AsyncTask<String, String, String> {

                public String response;


                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    driver_name = new ArrayList<>();
                    latititude = new ArrayList<>();
                    longititude = new ArrayList<>();
                    profilepic = new ArrayList<>();
                }
                @Override
                protected String doInBackground(String... params) {
                    DefaultHttpClient hc = new DefaultHttpClient();
                    ResponseHandler<String> res = new BasicResponseHandler();
                    HttpPost postMethod = new HttpPost(Config.nearbyDriver_url);
                    List<NameValuePair> nameValuePairs = new ArrayList<>();
                    nameValuePairs.add(new BasicNameValuePair("action", params[0]));
                    nameValuePairs.add(new BasicNameValuePair("latitude", params[1]));
                    nameValuePairs.add(new BasicNameValuePair("longitude", params[2]));
                    try {
                        postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        response = hc.execute(postMethod, res);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray =  jsonObject.getJSONArray("response");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String id = jsonObject1.getString("id");
                            if (Integer.parseInt(id)>0) {
                                String firstname = jsonObject1.getString("firstname");
                                String lastname = jsonObject1.getString("lastname");
                                String fullname = firstname + " " + lastname;
                                driver_name.add(fullname);
                                //latititude.add(jsonObject1.getString("latitude"));
                                //longititude.add(jsonObject1.getString("longitude"));
                               // profilepic.add(jsonObject1.getString("profile_pic"));
                            }
                            else{
                                carStatus= jsonObject1.getString("message");
                            }
                        }
                    }catch (Exception ex){

                    }

                    return null;
                }
                protected void onProgressUpdate(String... progress) {

                }
                @Override
                protected void onPostExecute(String unused) {
                   if (carStatus!=null){
                       latititude.add("30.713363325429583");
                       longititude.add("76.69824287295341");
                       latititude.add("30.715097986342318");
                       longititude.add("76.69059824198484");
                       latititude.add("30.719090355048895");
                       longititude.add("76.69328145682812");
                       latititude.add("30.711387076839184");
                       longititude.add("76.69589091092348");
                       latititude.add("30.712376647450817");
                       longititude.add("76.69129930436611");
                       latititude.add("30.71789908942077");
                       longititude.add("76.69396139681339");
                       latititude.add("30.71411680277952");
                       longititude.add("76.69950250536203");
                       latititude.add("30.741930235267073");
                       longititude.add("76.70203149318695");

                       for (int i=0;i<driver_name.size();i++){
                           double drivetlat= Double.parseDouble(latititude.get(i));
                           double drivetlng= Double.parseDouble(longititude.get(i));
                           LatLng latLng = new LatLng(drivetlat,drivetlng);
                           Marker melbourne = googleMap.addMarker(new MarkerOptions()
                                   .position(latLng)
                                   .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_hatchback)));
                       }

                   }

                }
            }


}
