package com.example.appzaorro.myapplication.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.appzaorro.myapplication.model.Constants;
import com.example.appzaorro.myapplication.model.Event;
import com.example.appzaorro.myapplication.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vijay on 1/2/17.
 */

public class AcceptDriverManager {

    private static final String TAG = AcceptDriverManager.class.getSimpleName();

    public void AcceptDriverManager(Context context, String params) {

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

            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(params[0]);

            Log.e(TAG, "update-setting of user"+response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("update-setting of user",s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                String id = jsonObject1.getString("id");
                String message = jsonObject1.getString("message");
                int idd = Integer.parseInt(id);

                EventBus.getDefault().post(new Event(Constants.driverAccept,id+","+message));
             /*   if (idd>0){
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText(""+message)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Intent intent = new Intent(context, Pending_Trips.class);
                                    intent.putExtra("STATUS","Ongoing");
                                    context.startActivity(intent);
                                    ((Activity)context).finish();
                                }
                            })
                            .show();
                }
                else {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Warning")
                            .setContentText(""+message)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Intent intent = new Intent(context, Pending_Trips.class);
                                    context.startActivity(intent);
                                    ((Activity)context).finish();
                                }
                            })
                            .show();*/

            } catch (JSONException ex) {

                ex.printStackTrace();
            }
        }
    }
}
