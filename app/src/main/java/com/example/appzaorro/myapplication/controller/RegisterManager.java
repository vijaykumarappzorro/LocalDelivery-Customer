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
 * Created by vijay on 20/3/17.
 */

public class RegisterManager {

    private static final String TAG = RegisterManager.class.getSimpleName();


    public void RegisterManager(Context context,String url, String params) {

        new ExecuteApi(context).execute(url,params);
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
        protected String doInBackground(String... strings) {

            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.getResponse(strings[0],strings[1]);
            Log.e(TAG, "simple register--"+response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String result = "";

            if (s!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                  result = jsonObject1.getString("message");
                    int id = Integer.parseInt(jsonObject1.getString("id"));
                    if (id > 0) {

                        EventBus.getDefault().post(new Event(Constants.REGISTER, result));
                    } else {

                        EventBus.getDefault().post(new Event(Constants.NOT_REGISTER, result));

                    }


                    Log.e("getting response", "" + result);

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }else {


                EventBus.getDefault().post(new Event(Constants.SERVER_ERROR, result));
            }
        }
    }

}
