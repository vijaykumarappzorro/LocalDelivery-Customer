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

public class Choose_a_driver extends Fragment {


    public Choose_a_driver() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.choose_a_driver, container, false);
    }
}

