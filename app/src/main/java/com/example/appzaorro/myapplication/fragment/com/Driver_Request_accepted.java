package com.example.appzaorro.myapplication.fragment.com;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appzaorro.myapplication.R;

/**
 * Created by vijay on 14/10/16.
 */

public class Driver_Request_accepted extends Fragment {


    public Driver_Request_accepted() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_driver__request__accepted, container, false);
        return view;

    }
}