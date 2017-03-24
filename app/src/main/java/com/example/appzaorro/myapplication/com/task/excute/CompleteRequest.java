package com.example.appzaorro.myapplication.com.task.excute;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.appzaorro.myapplication.model.ServiceHandler;
import com.example.appzaorro.myapplication.model.getter.Completed_getter;
import com.example.appzaorro.myapplication.model.Event;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vijay on 30/11/16.
 */

public class CompleteRequest {

    Context context;
    String params;
    ProgressDialog progressDialog;
    public static ArrayList<Completed_getter>arrayList;

    public void CompleteRequest(Context context, String params) {

        this.context = context;
        this.params = params;
        Log.e("url", params);
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
            ServiceHandler sh = new ServiceHandler();
            String result = sh.makeServiceCall(params[0], sh.GET);
            Log.e("url", params[0].toString());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
           // Log.e("response of","completed trip \n"+ s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 =jsonArray.getJSONObject(i);

                    String  requestId= jsonObject1.getString("deliver_request_id");
                    String cash= jsonObject1.getString("cash");
                    String pickupaddrss= jsonObject1.getString("pickup_location");
                    String dropaddress = jsonObject1.getString("drop_location");
                    String distance = jsonObject1.getString("Distance");
                    JSONObject jsonObject2= jsonObject1.getJSONObject("Driver Detail");
                    String driverid= jsonObject2.getString("id");
                    String firstname = jsonObject2.getString("firstname");
                    String lastname = jsonObject2.getString("lastname");
                    String fullname = firstname +" "+lastname;
                    String  profilepic= jsonObject2.getString("profile_pic");
                    String mobile = jsonObject2.getString("mobile");
                    Log.e("detaillll",requestId +"\n" +fullname);

                    Completed_getter completed_getter = new Completed_getter("map image",requestId,cash,driverid,fullname,profilepic,mobile,pickupaddrss
                    ,dropaddress,distance,"20","22 minute","50 rs","-30");
                    arrayList.add(completed_getter);
                }
                EventBus.getDefault().post(new Event("COMPLETE","TRUE"));

            }catch (Exception ex){

                ex.printStackTrace();
            }




        }
    }
}