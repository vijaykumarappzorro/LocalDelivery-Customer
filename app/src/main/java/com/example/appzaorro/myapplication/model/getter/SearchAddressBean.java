package com.example.appzaorro.myapplication.model.getter;

/**
 * Created by vijay on 23/1/17.
 */

public class SearchAddressBean {

    private String address,area;

    public SearchAddressBean(String address,String area){

        this.address= address;
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public String getArea() {
        return area;
    }
}
