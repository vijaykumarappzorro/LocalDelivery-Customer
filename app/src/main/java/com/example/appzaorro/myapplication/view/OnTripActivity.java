package com.example.appzaorro.myapplication.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.RoutingListener;
import com.example.appzaorro.myapplication.R;
import com.example.appzaorro.myapplication.model.Config;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class OnTripActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener ,
        OnMapReadyCallback,RoutingListener{
    Toolbar toolbar;
    GoogleMap googleMap;
    GoogleApiClient googleApiClient;
    private List<Polyline> polylines;
    ArrayList<String>reason;
    ImageView driver_pic;
    TextView driver_name,veichle_type,number,call,cancel,txt_support;

    private static final int[] COLORS = new int[]{R.color.pathcolor, R.color.colorPrimaryDark, R.color.colorAccent, R.color.colorAccent, R.color.primary_dark_material_light};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_trip);
        initviews();
    }

    public void initviews(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("On Trip");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        reason = new ArrayList<>();
        driver_pic =(ImageView)findViewById(R.id.driver_image);
        driver_name=(TextView)findViewById(R.id.text_driver_name);
        veichle_type =(TextView)findViewById(R.id.vehicle_type);
        number =(TextView)findViewById(R.id.vehicle_number);
        call =(TextView)findViewById(R.id.call_to_driver);
        cancel =(TextView)findViewById(R.id.cancel_ride);
        txt_support =(TextView)findViewById(R.id.support);
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {


        polylines = new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(Config.startpoint);
        builder.include(Config.endpoint);
        LatLngBounds bounds = builder.build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30));
        PolylineOptions polyOptions = null;
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }
        int min = 0;
        int indexvalue = 0;

        for (int j = 0; j < arrayList.size(); j++) {
            int colorIndex = j % COLORS.length;
            polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(7 + j * 3);

            if (j > 0) {
                if (min > arrayList.get(j).getDurationValue()) {

                    indexvalue = j;
                    min = arrayList.get(j).getDistanceValue();

                }
            } else {


                min = arrayList.get(j).getDistanceValue();

            }
            Log.e("route", String.valueOf(j + 1) + "     distance--" + arrayList.get(j).getDistanceValue() + "  duration---" + arrayList.get(j).getDurationValue());
            Toast.makeText(getApplicationContext(), "Route " + (j + 1) + ": distance - " + arrayList.get(j).getDistanceValue() + ": duration - " + arrayList.get(j).getDurationValue(), Toast.LENGTH_SHORT).show();
        }
        polyOptions.addAll(arrayList.get(indexvalue).getPoints());
        Polyline polyline = googleMap.addPolyline(polyOptions);


        Log.e("shortest distance", String.valueOf(arrayList.get(indexvalue).getDistanceValue()));
        Log.e("shortest duration", String.valueOf(arrayList.get(indexvalue).getDurationValue()));
        polylines.add(polyline);

        MarkerOptions options = new MarkerOptions();
        options.position(Config.startpoint);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_pin));
        googleMap.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(Config.endpoint);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon_pin));
        googleMap.addMarker(options);

    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
