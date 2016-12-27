package com.example.appzaorro.myapplication.com.task.excute;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.appzaorro.myapplication.ServiceHandler;
import com.example.appzaorro.myapplication.com.getter.Create_EVENTBUS;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vijay on 16/11/16.
 */

public class RequestToDriver {
    Context context;
    ProgressDialog progressDialog;
    String params;

    private EventBus bus = EventBus.getDefault();

    public void requestDriver(Context context, String params){

        this.context = context;
        this.params =params;

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
            String result = sh.makeServiceCall(params[0],sh.GET);
            Log.e("url",params[0].toString());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            Log.e("response: ", ""+s);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                String result = jsonObject1.getString("message");

                String id=  jsonObject1.getString("id");
                int responseid= Integer.parseInt(id);

                if (responseid>0) {

                    EventBus.getDefault().post(new Create_EVENTBUS("SUCCESS",result));


                } else {
                    EventBus.getDefault().post(new Create_EVENTBUS("FAILED",result));


                }

            } catch (JSONException ex) {

                ex.printStackTrace();
            }

        }
    }

}
