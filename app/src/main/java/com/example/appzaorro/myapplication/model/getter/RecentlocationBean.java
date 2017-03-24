package com.example.appzaorro.myapplication.model.getter;

/**
 * Created by vijay on 24/1/17.
 */

public class RecentlocationBean {
    String address;
    String addressarea;

    public RecentlocationBean(String address,String area){

        this.address= address;
        this.addressarea = area;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressarea() {
        return addressarea;
    }
}
