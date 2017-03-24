package com.example.appzaorro.myapplication.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.appzaorro.myapplication.model.Event;
import com.example.appzaorro.myapplication.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vijay on 25/1/17.
 */

public class RequesDriverManager {

    private static final String TAG = RequesDriverManager.class.getSimpleName();

    public void RequesDriverManager(Context context,String params) {

        new RequesDriverManager.ExecuteApi(context).execute(params);
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
                String result = jsonObject1.getString("message");
                String id=  jsonObject1.getString("id");
                int responseid= Integer.parseInt(id);

                if (responseid>0) {

                    EventBus.getDefault().post(new Event("SUCCESS",result));


                } else {
                    EventBus.getDefault().post(new Event("FAILED",result));


                }

            } catch (JSONException ex) {

                ex.printStackTrace();
            }
        }
    }

}
