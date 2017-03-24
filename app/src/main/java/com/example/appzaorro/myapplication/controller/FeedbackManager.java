package com.example.appzaorro.myapplication.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.appzaorro.myapplication.model.Constants;
import com.example.appzaorro.myapplication.model.Event;
import com.example.appzaorro.myapplication.model.HttpHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * Created by vijay on 9/1/17.
 */

public class FeedbackManager {
    private static final String TAG = FeedbackManager.class.getSimpleName();


    public void FeedbackManager(Context context, String params) {

        new FeedbackManager.ExecuteApi(context).execute(params);
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
            Log.e("near by driver response",s);

            try {
                JSONObject jsonObject1 = new JSONObject(s);
                JSONObject jsonObject11= jsonObject1.getJSONObject("response");
                String id = jsonObject11.getString("id");
                String message = jsonObject11.getString("message");
                EventBus.getDefault().post(new Event(Constants.feedbackstatus,id+","+message));

            }catch (Exception ex){
                ex.printStackTrace();


            }
        }
    }

}

