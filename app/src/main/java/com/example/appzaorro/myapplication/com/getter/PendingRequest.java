package com.example.appzaorro.myapplication.com.getter;

import java.util.ArrayList;

/**
 * Created by vijay on 17/11/16.
 */

public class PendingRequest {
    String requestid,descrption,datetime,pickaddress,dropaddress, statusreport;
    ArrayList<DriverDetail>list;

    public PendingRequest(String requestid, String descrption,String datetime,String pickaddress,String dropaddress, String statusreport,ArrayList<DriverDetail> list) {

        this.requestid=requestid;
        this.descrption=descrption;
        this.datetime = datetime;
        this.pickaddress = pickaddress;
        this.dropaddress = dropaddress;
        this.statusreport =statusreport;
        this.list =list;
    }
    public  PendingRequest(){


    }

    public String getStatusreport() {
        return statusreport;
    }

    public void setStatusreport(String statusreport) {
        this.statusreport = statusreport;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public ArrayList<DriverDetail> getList() {
        return list;
    }

    public void setList(ArrayList<DriverDetail> list) {
        this.list = list;
    }

    public String getPickaddress() {
        return pickaddress;
    }

    public void setPickaddress(String pickaddress) {
        this.pickaddress = pickaddress;
    }

    public String getDropaddress() {
        return dropaddress;
    }

    public void setDropaddress(String dropaddress) {
        this.dropaddress = dropaddress;
    }
}
