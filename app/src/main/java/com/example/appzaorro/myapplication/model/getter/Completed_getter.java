package com.example.appzaorro.myapplication.model.getter;

/**
 * Created by vijay on 30/11/16.
 */

public class Completed_getter {

    private  String mapScreen;
    private   String requestid;
    private   String cahs;
    private  String driverid;
    private    String drivername;
    private   String driverimage;
    private   String drivermobile;
    private   String pickupaddress;
    private  String dropaddress;
    private  String distance;
    private  String distancefare;
    private String time;
    private  String subtotal;
    private   String promotion;

    public Completed_getter(String mapScreen,String requestid,String cahs,String driverid, String drivername,String driverimage
            ,String  drivermobile,String pickupaddress,String dropaddress,String distance,String distancefare,String time
            ,String subtotal,String promotion) {


        this.requestid = requestid;
        this.mapScreen = mapScreen;
        this.cahs =cahs;
        this.driverid = driverid;
        this.drivername = drivername;
        this.driverimage = driverimage;
        this.drivermobile = drivermobile;
        this.pickupaddress = pickupaddress;
        this.dropaddress = dropaddress;
        this.distance = distance;
        this.distancefare = distancefare;
        this.time = time;
        this.subtotal = subtotal;
        this.promotion = promotion;
    }


   /* public  void Completed_getter(Context context,String mapScreen,String requestid,String cahs,String driverid,String driverimage
    ,String  drivermobile,String pickupaddress,String dropaddress,String distance,String distancefare,String time
    ,String subtotal,String promotion){
       this.context = context;
        this.requestid = requestid;
        this.mapScreen = mapScreen;
        this.cahs =cahs;
        this.driverid = driverid;
        this.driverimage = driverimage;
        this.drivermobile = drivermobile;
        this.pickupaddress = pickupaddress;
        this.dropaddress = dropaddress;
        this.distance = distance;
        this.distancefare = distancefare;
        this.time = time;
        this.subtotal = subtotal;
        this.promotion = promotion;
    }*/



    public String getMapScreen() {
        return mapScreen;
    }

    public String getRequestid() {
        return requestid;
    }

    public String getCahs() {
        return cahs;
    }

    public String getDriverid() {
        return driverid;
    }

    public String getDrivername() {
        return drivername;
    }

    public String getDriverimage() {
        return driverimage;
    }

    public String getDrivermobile() {
        return drivermobile;
    }

    public String getPickupaddress() {
        return pickupaddress;
    }

    public String getDropaddress() {
        return dropaddress;
    }

    public String getDistance() {
        return distance;
    }

    public String getDistancefare() {
        return distancefare;
    }

    public String getTime() {
        return time;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public String getPromotion() {
        return promotion;
    }
}
