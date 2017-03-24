package com.example.appzaorro.myapplication.model.getter;

/**
 * Created by vijay on 18/11/16.
 */

public class DriverDetail {
    String drivername;
    String price;
    String date;
    String time;
    String image;
    String driverid;
    String driverlat;
    String driverlng;

    public DriverDetail(String drivername, String price, String date, String time, String image,
    String driverid,String driverlat,String driverlng) {
        this.drivername = drivername;
        this.price = price;
        this.date = date;
        this.time= time;
        this.image =image;
        this.driverid =driverid;
        this.driverlat = driverlat;
        this.driverlng = driverlng;

    }



    public void  DriverDetail(){


    }

    public String getDriverid() {
        return driverid;
    }

    public String getDriverlat() {
        return driverlat;
    }

    public void setDriverlat(String driverlat) {
        this.driverlat = driverlat;
    }

    public String getDriverlng() {
        return driverlng;
    }

    public void setDriverlng(String driverlng) {
        this.driverlng = driverlng;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
