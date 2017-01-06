package com.example.appzaorro.myapplication.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.appzaorro.myapplication.R;

public class HelpActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txthelp,txtquestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        txthelp =(TextView)findViewById(R.id.txthelp);
        txtquestion=(TextView)findViewById(R.id.txtquestion);
        toolbar.setTitle("Help");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        txthelp.setText("Local Delivery is a technology platform connecting " +
                "drivers and riders. Use this app to request a delivery " +
                "and when nearby driver accepts your request, you will " +
                "be notified. Local Delivery is a technology platform " +
                "connecting drivers and riders. Use this app to request " +
                "a delivery and when nearby driver accepts your " +
                "request, you will be notified. Local Delivery is a " +
                "technology platform connecting drivers and riders. " +
                "Use this app to request a delivery and when nearby " +
                "driver accepts your request, you will be notified.");
    }
    public boolean onOptionsItemSelected(MenuItem item){
        // handle arrow click here
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(HelpActivity.this,HomePage_Activity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
