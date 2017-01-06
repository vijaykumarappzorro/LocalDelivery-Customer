package com.example.appzaorro.myapplication.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.appzaorro.myapplication.R;

/**
 * Created by vijay on 1/11/16.
 */

public class History_Demo extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completedtrip_dialog);
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Thu,6 oct 2016");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(History_Demo.this,HomePage_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}