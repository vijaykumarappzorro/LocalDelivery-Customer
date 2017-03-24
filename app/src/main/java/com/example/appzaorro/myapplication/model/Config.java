package com.example.appzaorro.myapplication.model;

import com.google.android.gms.maps.model.LatLng;

public class Config {

    public static final String login_url = "http://traala.com/deliver/ws/api.php?action=login&user_type=customer&device_type=A";
    public static final String custom_register_url="http://traala.com/deliver/ws/api.php?action=register";
   // public  static  final String custom_register_url ="http://www.traala.com.md-in-45.webhostbox.net/deliver/ws/api.php?action=register&email=neeraj@gmail.com&firstname=neeraj&lastname=sharma&password=123&mobile=9988776655&device_token=asdasd&device_type=";
    public static  final String forgot_password = "http://www.traala.com.md-in-45.webhostbox.net/deliver/ws/api.php?action=forgot_password&email=neeraj@gmail.com&user_type=";
    public  static  final  String update_profile_url="http://www.traala.com.md-in-45.webhostbox.net/deliver/ws/api.php?action=update_profile";
    public  static  final  String nearbyDriver_url="http://traala.com/deliver/ws/api.php?action=return_driver_list";
    public  static  final  String user_detail_url ="http://www.traala.com/deliver/ws/api.php?action=user_detail&user_type=customer";
   // public  static  final  String send_request_to_Driver="http://www.traala.com/deliver/ws/api.php?action=request_driver_for_delivery&customer_id=";
    public  static String place_id ="";
    public  static  final  String send_request_to_Driver="http://traala.com/deliver/ws/api.php?action=request_driver_for_delivery&customer_id=";
    public  static String SearchStatus ="";
    public  static  String currentlocation="";
    public static String recentlat="";
    public static  String recentlng="";
    public static  String destinationAddress="";
    public  static  String Camerastatus="";
    public static String date ="";
    public  static  String time ="";
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public  static  String pending_status ="";
    public static LatLng startpoint;
    public  static LatLng endpoint;
    public static String device_type = "A";
    public static String user_type ="customer";
    public  static  String device_token;
    public static String event_meesage="";
    public  static  final  String get_pending_request ="http://traala.com/deliver/ws/api.php?action=my_pending_requests&";
    public  static final  String accept_estimated_deliveryRequest="http://traala.com/deliver/ws/api.php?action=acceptDeliveryEstimatedRequest&customer_id=";
    public  static  final String completed_trip="http://traala.com/deliver/ws/api.php?action=customer_history&customer_id=";
    public  static  final  String feedback ="http://traala.com.md-in-45.webhostbox.net/deliver/ws/api.php?action=feedback";
    public  static  final  String searchUrl="https://maps.googleapis.com/maps/api/place/details/json?placeid=";
    public static final  String imagebaseurl ="http://traala.com.md-in-45.webhostbox.net/deliver/ws/profile_pics/";
    public static final String checked_Fbid_url ="http://traala.com/deliver/ws/api.php?action=customerDetailFacebookId";
    public static final String fb_login_url="http://traala.com/deliver/ws/api.php?action=register";

}


