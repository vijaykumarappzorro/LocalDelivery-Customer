package com.example.appzaorro.myapplication.controller;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.appzaorro.myapplication.model.Config;
import com.example.appzaorro.myapplication.model.Constants;
import com.example.appzaorro.myapplication.model.Event;
import com.example.appzaorro.myapplication.model.HttpHandler;
import com.example.appzaorro.myapplication.model.Ldshareadprefernce;
import com.example.appzaorro.myapplication.model.Operations;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;



public class FacebookLoginManager {

    private static final String TAG = FacebookLoginManager.class.getSimpleName();

    public void doFacebookLogin(final Activity context, CallbackManager callbackManager) {

        com.facebook.login.LoginManager.getInstance().logInWithReadPermissions(context,
                Arrays.asList("email", "user_friends", "public_profile")
        );

        com.facebook.login.LoginManager.getInstance().registerCallback(
                callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        String user_id = loginResult.getAccessToken().getUserId();
                        Log.e(TAG, "Id: " + user_id);
                        Log.e(TAG, "Token: " + loginResult.getAccessToken().getToken());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        exception.printStackTrace();
                    }
                }
        );

    }

    public void getFacebookData(final Activity context) {
        Bundle params = new Bundle();
        params.putString("fields", "id,name,email,picture.type(large)");
        new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if(response != null) {
                            try {
                                JSONObject data = response.getJSONObject();
                                Log.e(TAG, "Data: " + data);
                                String id = data.getString("id");
                                String name = data.getString("name");
                                String email = "";

                                try {
                                    email = data.getString("email");
                                }catch (JSONException ex) {
                                    ex.printStackTrace();
                                }

                                String profilePicUrl = "";
                                Log.e(TAG, "user_id-- " + id);
                                if (data.has("picture")) {
                                    profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");

                                    Log.e(TAG, "Image: " + profilePicUrl);
                                }
                                Ldshareadprefernce.putString(context, "user_name", name);
                                Ldshareadprefernce.putString(context, "facebook_id", id);
                                Ldshareadprefernce.putString(context, "user_email", email);
                                Ldshareadprefernce.putString(context, "user_image", profilePicUrl);
                                Ldshareadprefernce.putString(context,"facebook_login","true");

                                new ExecuteApi(context).execute(Operations.facebookLogintask(context, id, Config.user_type));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).executeAsync();
    }

    class ExecuteApi extends AsyncTask<String, String, String> {
        private Context context;

        public ExecuteApi(Context context) {
            this.context = context;
            Log.e("facebook id","check");
        }

        @Override
        protected String doInBackground(String... params) {

            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(params[0]);
            Log.e(TAG, "facebook_login response-- " + response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject response = jsonObject.getJSONObject("response");
                    int id = Integer.parseInt(response.getString("id"));

                    if (id>0) {
                        Log.e("fb id recived","");
                        Ldshareadprefernce.putString(context, "user_id", String.valueOf(id));

                        new ExecuteApiUserDetails(context).execute(Operations.userDeatil(context, String.valueOf(id)));

                    }else {

                        Log.e("fb id recived","NOT");

                        EventBus.getDefault().post(new Event(Constants.FACEBOOK_LOGINEMPTY, ""));
                    }

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            } else {
                EventBus.getDefault().post(new Event(Constants.SERVER_ERROR, ""));
            }
        }
    }

    public void registerUser(Context context, String url, String params) {

        new ExecuteApiRegisterUser(context).execute(url, params);
    }

    private class ExecuteApiRegisterUser extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiRegisterUser(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.getResponse(strings[0],strings[1]);
            //   String response = httpHandler.makeServiceCall(strings[0]);
            Log.e(TAG, "login response--" +response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject response = jsonObject.getJSONObject("response");
                    int id = response.getInt("id");
                    Log.e("customerid",String.valueOf(id));
                    String message = response.getString("message");
                    if (id >= 1) {

                        Ldshareadprefernce.putString(mContext, "customer_id", String.valueOf(id));

                        new ExecuteApiUserDetails(mContext).execute(Operations.userDeatil(mContext, String.valueOf(id)));

                    } else {

                        EventBus.getDefault().post(new Event(Constants.SERVER_ERROR, message));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                EventBus.getDefault().post(new Event(Constants.SERVER_ERROR, ""));
            }

        }
    }


    private class ExecuteApiUserDetails extends AsyncTask<String, String, String> {
        private Context mContext;

        ExecuteApiUserDetails(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(strings[0]);

            Log.e(TAG, "user_details response--" +response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject response = jsonObject.getJSONObject("response");
                    String id = response.getString("id");
                    String email = response.getString("email");
                   // String name = response.getString("name").replace("+", " ");
                    String mobile = response.getString("mobile");
                    String profile_pic = response.getString("profile_pic");
                    Ldshareadprefernce.putString(mContext, "user_email", email);
                 //   Ldshareadprefernce.putString(mContext, "user_name", name);
                    Ldshareadprefernce.putString(mContext, "user_mobile", mobile);
                    Ldshareadprefernce.putString(mContext, "user_image", profile_pic);
                    Log.e("image url",Config.imagebaseurl+profile_pic);
                    EventBus.getDefault().post(new Event(Constants.FACEBOOK_LOGIN_SUCCESS, ""));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                EventBus.getDefault().post(new Event(Constants.SERVER_ERROR, ""));
            }

        }
    }

}