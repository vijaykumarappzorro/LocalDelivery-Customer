package com.example.appzaorro.myapplication.model;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.appzaorro.myapplication.R;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vijay on 25/1/17.
 */

public class Util {




    // get the address of the pickup locaion

    public  static  String getCompleteAddressString(Context context,double LATITUDE, double LONGITUDE) {


        String strAdd ="";

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            if(addresses != null) {

                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

             /*   for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");

                }*/
                strReturnedAddress.append(returnedAddress.getFeatureName());
                        if(returnedAddress.getSubAdminArea()!=null){

                            strReturnedAddress.append(returnedAddress.getSubAdminArea());
                        }
                strAdd=strReturnedAddress.toString();

                Log.e("addresssss",returnedAddress.getExtras()+"\n"+returnedAddress.getThoroughfare()+"\n"+
                returnedAddress.getLocale()+"\n"+returnedAddress.getPremises()+"\n"+returnedAddress.getFeatureName());


                //strAdd = strReturnedAddress.toString();


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

    public static String getDestinationAddress(Context  context ,double LATITUDE, double LONGITUDE) {
        String strDestAdd="";

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
              //  Log.e("address",returnedAddress.getLocality()+"\n"+returnedAddress.getAdminArea()+"\n"+returnedAddress.getSubAdminArea());

                StringBuilder strReturnedAddress = new StringBuilder("");

               /* for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getLocality()).append("\n");
                    Log.e("dfdff",returnedAddress.getFeatureName()+"  "+returnedAddress.getAddressLine(i));


                }*/
                strReturnedAddress.append(returnedAddress.getAddressLine(0));
                if (returnedAddress.getSubLocality()!=null)
                    strReturnedAddress.append(", ").append(returnedAddress.getSubLocality());




                strDestAdd = strReturnedAddress.toString();


                Log.e("Current Address of user",strDestAdd);
            } else {
                Log.e("", "No Address returned!");


            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", "Canont get Address!");
        }
        return strDestAdd;
    }

    public static float getZoomValue(Context context,float currentSpan, float lastSpan)
    {
        double value = (Math.log(currentSpan / lastSpan) / Math.log(1.55d));
        return (float) value;
    }

    public  static  boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static Dialog driverAcceptDialog(Context context){

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.driver_bidform);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        return dialog;
    }

    public static void makeSnackBar(Context context, View view, String message) {
        Snackbar snackbar;
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.RED);
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
    public static String getPostDataString(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while (itr.hasNext()) {
            String key = itr.next();
            Object value = params.get(key);
            if (first)
                first = false;
            else
            result.append("&");
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }




    public  static void showGPSDisabledAlertToUser(final Context context)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                       context.startActivity(callGPSSettingIntent);

                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }



}
