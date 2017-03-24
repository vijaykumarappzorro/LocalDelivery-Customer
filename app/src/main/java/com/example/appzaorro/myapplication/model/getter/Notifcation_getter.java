package com.example.appzaorro.myapplication.model.getter;

/**
 * Created by vijay on 25/11/16.
 */

public class Notifcation_getter {
    private String notification;
    private  String time;

    public Notifcation_getter(String notification, String time) {
        this.notification = notification;
        this.time = time;

    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
