package com.example.appzaorro.myapplication.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.appzaorro.myapplication.model.Config;
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

public class UserDeatilManager {

    private static final String TAG = UserDeatilManager.class.getSimpleName();


    public void UserDeatilManager(Context context, String params) {

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
            if (s!=null) {

                try {
                    JSONObject jsonObject = new JSONObject("" + s);

                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                    int id = Integer.parseInt(jsonObject1.getString("id"));
                    String emailid = jsonObject1.getString("email");
                    String firstname = jsonObject1.getString("firstname");
                    String lastname = jsonObject1.getString("lastname");
                    String profilepic = jsonObject1.getString("profile_pic");
                    String mobileNumber = jsonObject1.getString("mobile");
                    if (id >= 1) {

                        Ldshareadprefernce.putString(mContext, "user_email", emailid);
                        Ldshareadprefernce.putString(mContext, "user_id", String.valueOf(id));
                        Ldshareadprefernce.putString(mContext, "user_mobile", mobileNumber);
                        Ldshareadprefernce.putString(mContext, "user_name", firstname + " " + lastname);


                        if (Ldshareadprefernce.readString(mContext, "facebook_login").equals("true")) {

                            Ldshareadprefernce.putString(mContext, "user_image", profilepic);

                        } else {
                            Ldshareadprefernce.putString(mContext, "user_image", Config.imagebaseurl + "" + profilepic);

                        }

                        EventBus.getDefault().post(new Event(Constants.USERDETAIL, ""));


                    } else {


                    }



                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }else {

                EventBus.getDefault().post(new Event(Constants.SERVER_ERROR, ""));
            }
        }
    }
}
