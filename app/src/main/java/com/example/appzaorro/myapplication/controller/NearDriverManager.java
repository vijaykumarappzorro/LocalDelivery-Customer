package com.example.appzaorro.myapplication.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.appzaorro.myapplication.model.Constants;
import com.example.appzaorro.myapplication.model.Event;
import com.example.appzaorro.myapplication.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by vijay on 9/1/17.
 */

public class NearDriverManager {


    private static final String TAG = NearDriverManager.class.getSimpleName();


    public void NearDriverManager(Context context, String params) {

        new NearDriverManager.ExecuteApi(context).execute(params);
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

            Log.e(TAG, "near by driver --"+response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("near by driver response"," "+s);
            try {
                EventBus.getDefault().post(new Event(Constants.nearbydriver,s));

            }catch (Exception ex){
                ex.printStackTrace();
            }




        }
    }

}

