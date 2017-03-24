package com.example.appzaorro.myapplication.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by vijay on 16/11/16.
 */

public class Operations {

    public static String getCustomerRequest(Context context,String userid,String pick,String drop,String source_lat, String souurce_lng,String dest_lat,
            String dest_lng,String date,String time,String description,String price) {
        String params = null;

        try {
            params = Config.send_request_to_Driver+userid+"&pickup_location="+URLEncoder.encode(pick,"UTF-8")+"&drop_location="+URLEncoder.encode(drop,"UTF-8")+"&source_latitude="+URLEncoder.encode(source_lat)
            +"&source_longitude="+URLEncoder.encode(souurce_lng)+"&destination_latitude="+URLEncoder.encode(dest_lat)
            +"&destination_longitude="+URLEncoder.encode(dest_lng)+"&date="+URLEncoder.encode(date,"UTF-8")+"&time="+URLEncoder.encode(time,"UTF-8")+"&product_desc="+URLEncoder.encode(description,"UTF-8")+"&price="
            +URLEncoder.encode(price);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e("getcustomer  request","url"+params);
        return params.replaceAll(" ,", ",").replaceAll("\n","");

    }
    public static String getPendingRequest(Context context,String userid) {

        String params = Config.get_pending_request+"customer_id="+userid;
        Log.e("getpendingrequest url",params);

        return params.replaceAll(" ,", ",").replaceAll(" ", "").replaceAll("\n","");

    }
    public static  String driverAccept(Context context,String userId,String rquestId,String driverId){

        String params =Config.accept_estimated_deliveryRequest+userId+"&request_id="+rquestId+"&driver_id="+driverId;
        Log.e("driver accept","url"+params);

        return params;

    }
    public static String completeRquestbyDriver(Context context,String userId){

        String parms = Config.completed_trip+userId;
        Log.e("completed trip",parms);
        return  parms;
    }

    public static String nearByDriverUrl(Context context, String lati, String longi) {
        String parms = Config.nearbyDriver_url+"&latitude="+lati+"&longitude="+longi;
        Log.e("nearbydriver url",parms);
        return parms;

    }
    public static String feedback(Context context,String customerid,String driverid,String rating,String feedback,String feedfrom){

        String parms = Config.feedback+"&feedback_from_id="+customerid+"&feedback_to_id="+driverid+"&rating="+rating+"&feedback="
                +feedback+"&feedback_from=customer";
        return parms;

    }

    public  static String updateprofile(Context context,String userid,String profilepic,String firstname,String lastname,String usertype){
        String parms = Config.update_profile_url+"&user_id="+userid+"&profile_pic="+profilepic+"&firstname="+firstname+"&lastname="+lastname
                +"&user_type="+usertype;

        Log.e("Setting url",parms);

        return parms;

    }
    public static String forgotPassword(Context context,String email,String usertype){


        String parms = Config.forgot_password+"&email="+email+"&user_type="+usertype;

        Log.e("forgot password url",parms);
        return parms;
    }
     public  static String userDeatil(Context context,String userid){

         String parms = Config.user_detail_url+"&user_id="+userid;
         Log.e("userdetail url",parms);
         return parms;

     }

    public static String loginUser(Context context,String emailid,String password,String devicetoken){

        String parms=Config.login_url+"&email="+emailid+"&password="+password+"&device_token="+devicetoken;

        Log.e("Login url",parms);


        return parms;

    }


    public static String registerUser(Context context,String email,String firstname,String lastname,String password,
                                      String mobile,String device_token,String device_type,String user_type,String profle) {

        try {
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("email", email);
            postDataParams.put("firstname", firstname);
            postDataParams.put("lastname", lastname);
            postDataParams.put("password", password);
            postDataParams.put("mobile", mobile);
            postDataParams.put("device_token", device_token);
            postDataParams.put("device_type", device_type);
            postDataParams.put("user_type", user_type);
            postDataParams.put("profile_pic", profle);

            String params = null;
            try {
                params = Util.getPostDataString(postDataParams);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Log.e("register url",Config.custom_register_url+params);

            return params;

        } catch (Exception e) {

            e.printStackTrace();
        }
        //     Log.e(TAG, "fb_login params-- "+params);return params;} catch (JSONException e) {e.printStackTrace();}return null;}
        return null;
    }

    public static String facebookLogintask(Context context, String facebookid,String usertype){


        String parms=Config.checked_Fbid_url+"&facebook_id="+facebookid+"&user_type="+usertype;
        Log.e("facebook id ",parms);
        return parms;

    }
    public static String facebook_login(Context context,String email,String firstname,String lastname,
                                      String mobile,String fb_id,String device_token,String device_type,String user_type,String profle) {

        try {
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("email", email);
            postDataParams.put("firstname", firstname);
            postDataParams.put("lastname", lastname);
            postDataParams.put("password", "");
            postDataParams.put("facebook_id", fb_id);
            postDataParams.put("mobile", mobile);
            postDataParams.put("device_token", device_token);
            postDataParams.put("device_type", device_type);
            postDataParams.put("user_type", user_type);
            postDataParams.put("profile_pic", profle);
            String params = null;
            try {
                params = Util.getPostDataString(postDataParams);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Log.e("facebooklogin url",Config.custom_register_url+"&"+params);

            return params;

        } catch (Exception e) {

            e.printStackTrace();
        }
        //     Log.e(TAG, "fb_login params-- "+params);return params;} catch (JSONException e) {e.printStackTrace();}return null;}
        return null;
    }


}
