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
 * Created by vijay on 3/2/17.
 */

public class ForgotPasswordmanager {

    private static final String TAG = ForgotPasswordmanager.class.getSimpleName();

    public void ForgotPasswordmanager(Context context, String params) {

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
                String result = jsonObject1.getString("message");
                int id= jsonObject1.getInt("id");
                EventBus.getDefault().post(new Event(Constants.forgotpassword,String.valueOf(id)+","+result));
            }catch (JSONException ex) {

                ex.printStackTrace();
            }
        }
    }


}
