package com.example.appzaorro.myapplication.model;

/**
 * Created by vijay on 16/11/16.
 */

public class Event {
    private String key,value;

    public Event(String key, String value) {
        this.key = key;
        this.value = value;
    }



    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
