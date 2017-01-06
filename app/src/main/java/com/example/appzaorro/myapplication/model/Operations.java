package com.example.appzaorro.myapplication.model;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by vijay on 16/11/16.
 */

public class Operations {

    public static String getCustomerRequest(Context context,String userid,String pick,String drop,String date,String time,String description,

                                            String lat, String lng) {
        String params = null;


        try {
            params = Config.send_request_to_Driver+userid+"&pickup_location="+URLEncoder.encode(pick,"UTF-8")+"&drop_location="+URLEncoder.encode(drop,"UTF-8")+"&date="+URLEncoder.encode(date,"UTF-8")+"&time="+URLEncoder.encode(time,"UTF-8")+"&product_desc="+URLEncoder.encode(description,"UTF-8")+"&latitude="+URLEncoder.encode(lat,"UTF-8")+"&longitude="+URLEncoder.encode(lng,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return params.replaceAll(" ,", ",").replaceAll("\n","");

    }
    public static String getPendingRequest(Context context,String userid) {

        String params = Config.get_pending_request+"customer_id="+userid;

        return params.replaceAll(" ,", ",").replaceAll(" ", "").replaceAll("\n","");
    }
    public static  String acceptEstimatedDeliveryRequest(Context context,String userId,String rquestId,String driverId){

        String params =Config.accept_estimated_deliveryRequest+userId+"&request_id="+rquestId+"&driver_id="+driverId;


        return params;

    }
    public static String completeRquestbyDriver(Context context,String userId){

        String parms = Config.completed_trip+userId;
        return  parms;
    }
}
