package com.example.appzaorro.myapplication.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.appzaorro.myapplication.model.Constants;
import com.example.appzaorro.myapplication.model.Event;
import com.example.appzaorro.myapplication.model.HttpHandler;
import com.example.appzaorro.myapplication.model.getter.Completed_getter;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vijay on 24/1/17.
 */

public class CompleteRequestManager {

    public static ArrayList<Completed_getter> arrayList;


    private static final String TAG = CompleteRequestManager.class.getSimpleName();


    public void CompleteRequestManager(Context context, String params) {

        new CompleteRequestManager.ExecuteApi(context).execute(params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {

        Context mContext;

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {
            arrayList = new ArrayList<>();

            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(params[0]);

            Log.e(TAG, "near by driver --"+response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String requestId = jsonObject1.getString("deliver_request_id");
                    int id = Integer.parseInt(requestId);
                    if (id > 0) {
                        String cash = jsonObject1.getString("cash");
                        String pickupaddrss = jsonObject1.getString("pickup_location");
                        String dropaddress = jsonObject1.getString("drop_location");
                        String distance = jsonObject1.getString("Distance");
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("Driver Detail");
                        String driverid = jsonObject2.getString("id");
                        String firstname = jsonObject2.getString("firstname");
                        String lastname = jsonObject2.getString("lastname");
                        String fullname = firstname + " " + lastname;
                        String profilepic = jsonObject2.getString("profile_pic");
                        String mobile = jsonObject2.getString("mobile");
                        Log.e("detaillll", requestId + "\n" + fullname);

                        Completed_getter completed_getter = new Completed_getter("map image", requestId, cash, driverid, fullname, profilepic, mobile, pickupaddrss
                                , dropaddress, distance, "20", "22 minute", "50 rs", "-30");
                        arrayList.add(completed_getter);

                        EventBus.getDefault().post(new Event(Constants.completedtripstatus, requestId));
                    }
                    else{

                        EventBus.getDefault().post(new Event(Constants.completedtripstatus, requestId));
                         }
                }

            }catch (Exception ex){

                ex.printStackTrace();
            }




        }
    }

}

