package com.example.appzaorro.myapplication.model;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by vijay on 4/11/16.
 */

public class FireIDService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {

        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("onTokenRefresh:",token);
    }
}
