package com.example.appzaorro.myapplication;

public class Config {

    public static final String login_url = "http://www.traala.com.md-in-45.webhostbox.net/deliver/ws/api.php?action=login&email=neeraj@gmail.com&password=123&device_token=asdasd&device_type=";
    public  static  final String custom_register_url ="http://www.traala.com.md-in-45.webhostbox.net/deliver/ws/api.php?action=register&email=neeraj@gmail.com&firstname=neeraj&lastname=sharma&password=123&mobile=9988776655&device_token=asdasd&device_type=";
    public static  final String forgot_password = "http://www.traala.com.md-in-45.webhostbox.net/deliver/ws/api.php?action=forgot_password&email=neeraj@gmail.com&user_type=";
    public  static  final  String update_profile_url="http://www.traala.com.md-in-45.webhostbox.net/deliver/ws/api.php?action=update_profile&user_id=1&profile_pic=base64image&firstname=neerajk&lastname=sharma&user_type=";
    public  static  final  String nearbyDriver_url="http://traala.com/deliver/ws/api.php?action=return_driver_list&latitude=12.222&longitude=12.222";
    public  static  final  String user_detail_url ="http://www.traala.com/deliver/ws/api.php?action=user_detail&user_id=1&user_type=customer";
    public  static  final  String send_request_to_Driver="http://www.traala.com/deliver/ws/api.php?action=request_driver_for_delivery&customer_id=";
    public  static String place_id ="";
    public  static String SearchStatus ="";
    public static  String destinationAddress="";
    public  static  String Camerastatus="";
    public static String date ="";
    public  static  String time ="";
    public  static  String customer__id;
    public  static  String pending_status ="";
    public  static  final  String get_pending_request ="http://traala.com/deliver/ws/api.php?action=my_pending_requests&";
    public  static final  String accept_estimated_deliveryRequest="http://traala.com/deliver/ws/api.php?action=acceptDeliveryEstimatedRequest&customer_id=";
    public  static  final String completed_trip="http://traala.com/deliver/ws/api.php?action=completed_delivery&customer_id=";
}


