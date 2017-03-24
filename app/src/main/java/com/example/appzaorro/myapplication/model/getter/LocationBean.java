package com.example.appzaorro.myapplication.model.getter;

import android.content.Context;

/**
 * Created by vijay on 11/1/17.
 */

public class LocationBean {

    Context context;
    String sourcelat;
    String sourcelng;
    String sourceadd;
    String sourcearea;


    public LocationBean(Context context, String sourcelat, String sourcelng, String sourceadd,String sourcearea) {

        this.context = context;
        this.sourcelat = sourcelat;
        this.sourcelng = sourcelng;
        this.sourceadd = sourceadd;
        this.sourcearea= sourcearea;


    }

    public LocationBean() {

    }

    public void setSourcearea(String sourcearea) {
        this.sourcearea = sourcearea;
    }

    public void setSourcelat(String sourcelat) {
        this.sourcelat = sourcelat;
    }

    public void setSourcelng(String sourcelng) {
        this.sourcelng = sourcelng;
    }

    public String getSourcearea() {
        return sourcearea;
    }

    public void setSourceadd(String sourceadd) {
        this.sourceadd = sourceadd;
    }



    public Context getContext() {
        return context;
    }



    public String getSourceadd() {
        return sourceadd;
    }



    public String getSourcelng() {
        return sourcelng;
    }

    public String getSourcelat() {
        return sourcelat;
    }
}


