package com.example.appzaorro.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appzaorro.myapplication.com.getter.Create_EVENTBUS;
import com.example.appzaorro.myapplication.com.getter.DriverDetail;
import com.example.appzaorro.myapplication.com.getter.DriverResposeAdapter;
import com.example.appzaorro.myapplication.com.getter.PendingRequest;
import com.example.appzaorro.myapplication.com.task.excute.GetPendingRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Choose_A_Driver extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    private EventBus bus = EventBus.getDefault();
    ArrayList<DriverDetail>list;
    TextView Notaccepted;
    public  static String requestId ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__a__driver);
        listView = (ListView) findViewById(R.id.listview);
        Notaccepted =(TextView)findViewById(R.id.txtnotaccepted) ;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Choose A Driver");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        list = new ArrayList<>();
        Intent intent =getIntent();
        int postion= Integer.parseInt(intent.getStringExtra("postion"));
        requestId= intent.getStringExtra("requestid");
        Log.e("postion of arraylist",String.valueOf(postion));
        PendingRequest pendingRequest =  GetPendingRequest.arrayList.get(postion);
         list = pendingRequest.getList();
        if (list.size()>0){
            listView.setVisibility(View.VISIBLE);
            Notaccepted.setVisibility(View.GONE);
            DriverResposeAdapter driverResposeAdapter = new DriverResposeAdapter(Choose_A_Driver.this,list);
            listView.setAdapter(driverResposeAdapter);
        }
        else {
            Notaccepted.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }






    }

    @Subscribe
    public void onEvent(Create_EVENTBUS event) {
        Log.e("call", "eventbus");

        switch (event.getKey()) {
            case "DRIVERRESPONSE":

                break;

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home){
           Intent intent = new Intent(Choose_A_Driver.this,Pending_Trips.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
