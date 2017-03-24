package com.example.appzaorro.myapplication.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.appzaorro.myapplication.model.Constants;
import com.example.appzaorro.myapplication.model.Event;
import com.example.appzaorro.myapplication.model.HttpHandler;
import com.example.appzaorro.myapplication.model.Ldshareadprefernce;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vijay on 3/2/17.
 */

public class LoginManager {

    private static final String TAG = LoginManager.class.getSimpleName();


    public void LoginManager(Context context, String params) {

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
            String result="";
            if (s!=null) {
                try {
                    JSONObject jsonObject = new JSONObject("" + s);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                    result  = jsonObject1.getString("complete_status");
                    int id = Integer.parseInt(jsonObject1.getString("id"));
                    if (id > 0) {

                        Ldshareadprefernce.putString(mContext, "user_id",String.valueOf(id));
                        Ldshareadprefernce.putString(mContext, "login_status", "true");
                        EventBus.getDefault().post(new Event(Constants.LOGIN,"true"));


                    } else {

                        EventBus.getDefault().post(new Event(Constants.LOGIN_ERROR, result));


                    }


                } catch (JSONException ex) {

                    ex.printStackTrace();
                }
            }else {

                EventBus.getDefault().post(new Event(Constants.SERVER_ERROR, result));
            }
        }
    }
}
