package com.example.appzaorro.myapplication.com.task.excute;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.appzaorro.myapplication.Config;
import com.example.appzaorro.myapplication.Pending_Trips;
import com.example.appzaorro.myapplication.ServiceHandler;
import com.example.appzaorro.myapplication.com.getter.Create_EVENTBUS;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by vijay on 22/11/16.
 */

public class Accepted_EstimatedRequest {
Context context;
    String params;
    ProgressDialog progressDialog;

    public void Accepted_EstimatedRequest(Context context, String params ) {

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
            ServiceHandler sh = new ServiceHandler();
            String result = sh.makeServiceCall(params[0], sh.GET);
            Log.e("url", params[0].toString());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Log.e("response",s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                String id = jsonObject1.getString("id");
                String message = jsonObject1.getString("message");
                int idd = Integer.parseInt(id);
                if (idd>0){
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
                            .show();
                }
            } catch (JSONException ex) {

                ex.printStackTrace();
            }

        }
    }
}
