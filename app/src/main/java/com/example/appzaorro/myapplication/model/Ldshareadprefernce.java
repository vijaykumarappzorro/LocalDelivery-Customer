package com.example.appzaorro.myapplication.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vijay on 27/12/16.
 */

public class Ldshareadprefernce {
    private static SharedPreferences getPreference(Context context){
        return context.getSharedPreferences("LD_PREF", Context.MODE_PRIVATE);
    }
    public static String readString(Context context, String key) {
        return getPreference(context).getString(key, "");
    }

    public static void putString(Context context, String key, String value){
        getPreference(context).edit().putString(key,value).apply();
    }

    public static void getString(Context context , String key , String value) {
        getPreference(context).getString(key,value);
    }
}

