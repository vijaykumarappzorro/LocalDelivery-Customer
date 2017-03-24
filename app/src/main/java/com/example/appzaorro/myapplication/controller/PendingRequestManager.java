package com.example.appzaorro.myapplication.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.appzaorro.myapplication.model.Constants;
import com.example.appzaorro.myapplication.model.Event;
import com.example.appzaorro.myapplication.model.HttpHandler;
import com.example.appzaorro.myapplication.model.getter.DriverDetail;
import com.example.appzaorro.myapplication.model.getter.PendingRequest;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vijay on 9/3/17.
 */

public class PendingRequestManager {

    public static String RequestSataus="";
    public static ArrayList<PendingRequest>arrayList ;
    public  static ArrayList<DriverDetail>driverdata ;
    ArrayList<DriverDetail>list;


    private static final String TAG = RequesDriverManager.class.getSimpleName();

    public void PendingRequestManager(Context context, String params) {

        new ExecuteApi(context).execute(params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {

        Context mContext;

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            arrayList = new ArrayList<>();
            driverdata = new ArrayList<>();
            list = new ArrayList<>();

            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(params[0]);

            Log.e(TAG, "getpending request"+response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("pending response ",s);
            if (s!=null) {
                try {

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String requestid = jsonObject1.getString("request_id");
                        int id = Integer.parseInt(requestid);
                        if (id > 0) {
                            String time = jsonObject1.getString("datetime");
                            String pick = jsonObject1.getString("pickup_location");
                            String drop = jsonObject1.getString("drop_location");
                            String status = jsonObject1.getString("request_status");

                            String description = jsonObject1.getString("product_desc");
                            String customerprice = jsonObject1.getString("price");

                            Log.e("Description: ", "" + description);

                            JSONArray jsonArray1 = jsonObject1.getJSONArray("pending_requests");

                            if (jsonArray1.length() > 0) {
                                list = new ArrayList<>();

                                EventBus.getDefault().post(new Event("DRIVERRESPONSE", "true"));

                                for (int j = 0; j < jsonArray1.length(); j++) {

                                    try {
                                        JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                                        String drivername = jsonObject2.getString("driver_firstname") + " " + jsonObject2.getString("driver_lastname");
                                        String driverid = jsonObject2.getString("driver_id");
                                        String price = jsonObject2.getString("estimated_cost");
                                        String date = jsonObject2.getString("estimated_date");
                                        String time1 = jsonObject2.getString("estimated_time");
                                        String image = jsonObject2.getString("driver_profile_pic");
                                        String driverlat = jsonObject2.getString("driver_latitude");
                                        String driverlng = jsonObject2.getString("driver_longitude");
                                        DriverDetail driverDetail = new DriverDetail(drivername, price, date, time1, image, driverid, driverlat, driverlng);
                                        list.add(driverDetail);
                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }

                                }
                            } else {

                                RequestSataus = "pending";
                              //  list = new ArrayList<>();

                            }

                            PendingRequest pendingRequest = new PendingRequest(requestid, description, time, pick, drop, status,customerprice, list);
                            arrayList.add(pendingRequest);


                        } else {

                            String errorMessage = jsonObject1.getString("message");
                            Log.e("error message", errorMessage);
                            EventBus.getDefault().post(new Event(Constants.NOREQUEST, errorMessage));
                        }
                    }

                } catch (JSONException ex) {

                    ex.printStackTrace();
                }
                EventBus.getDefault().post(new Event("Response",""));
                EventBus.getDefault().post(new Event(Constants.RESPONSE,""));
            }
            else {


                EventBus.getDefault().post(new Event(Constants.SERVER_ERROR,""));

            }

        }
    }

}
