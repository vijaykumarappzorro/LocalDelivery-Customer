package com.example.appzaorro.myapplication.com.task.excute;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.appzaorro.myapplication.model.ServiceHandler;
import com.example.appzaorro.myapplication.model.Event;
import com.example.appzaorro.myapplication.com.getter.DriverDetail;
import com.example.appzaorro.myapplication.com.getter.PendingRequest;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vijay on 17/11/16.
 */

public class GetPendingRequest {
    Context context;
    ProgressDialog progressDialog;
    String params;
    public static String RequestSataus="";
     public   static ArrayList<PendingRequest>arrayList ;
    public  static ArrayList<DriverDetail>driverdata ;
    ArrayList<DriverDetail> list;

    private EventBus bus = EventBus.getDefault();

    public void GetPendingRequest(Context context, String params ) {

        this.context = context;
        this.params = params;
        Log.e("url",params);

        new ExecuteApi(context).execute(params);


    }

    class ExecuteApi extends AsyncTask<String, String, String> {
        String response;
        Context mContext;

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("wait for some time your request sending to the  drivers");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            arrayList = new ArrayList<>();
            driverdata = new ArrayList<>();
          list = new ArrayList<>();
            ServiceHandler sh = new ServiceHandler();
            String result = sh.makeServiceCall(params[0], sh.GET);
            Log.e("url", params[0].toString());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            Log.e("response: ", "" + s);
            progressDialog.dismiss();
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
                                list = new ArrayList<>();

                            }
                            PendingRequest pendingRequest = new PendingRequest(requestid, description, time, pick, drop, status, list);
                            arrayList.add(pendingRequest);


                        } else {

                            String errorMessage = jsonObject1.getString("message");
                            Log.e("error message", errorMessage);
                            EventBus.getDefault().post(new Event("NOREQUEST", errorMessage));

                        }


                    }


                } catch (JSONException ex) {

                    ex.printStackTrace();
                }
                EventBus.getDefault().post(new Event("Response",""));
            }
            else {

                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
