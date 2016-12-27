package com.example.appzaorro.myapplication.fragment.com;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appzaorro.myapplication.R;

/**
 * Created by vijay on 14/10/16.
 */

public class Method_payment extends Fragment {


    public Method_payment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.method_payment, container, false);
    }
}

