package com.example.appzaorro.myapplication.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.appzaorro.myapplication.R;
import com.example.appzaorro.myapplication.controller.PendingRequestManager;
import com.example.appzaorro.myapplication.model.Constants;
import com.example.appzaorro.myapplication.model.Event;
import com.example.appzaorro.myapplication.model.getter.DriverDetail;
import com.example.appzaorro.myapplication.model.getter.DriverResposeAdapter;
import com.example.appzaorro.myapplication.model.getter.PendingRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Choose_A_Driver extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    ArrayList<DriverDetail>list;
    TextView Notaccepted;
    public  static String requestId ="";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__a__driver);
        initView();

    }
    public void initView(){

        listView = (ListView) findViewById(R.id.listview);
        Notaccepted =(TextView)findViewById(R.id.txtnotaccepted) ;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Choose A Driver");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        context=this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        list = new ArrayList<>();
        Intent intent =getIntent();
        int postion= Integer.parseInt(intent.getStringExtra("postion"));
        requestId= intent.getStringExtra("requestid");
        Log.e("postion of arraylist",String.valueOf(postion));
        PendingRequest pendingRequest =  PendingRequestManager.arrayList.get(postion);
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
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.driverAccept:
                String message = event.getValue();
                String[] message_array = message.split(",");
                int result_id = Integer.parseInt(message_array[message_array.length-2]);
                if (result_id>0) {
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText(""+message)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Intent intent = new Intent(context, Pending_Trips.class);
                                    intent.putExtra("STATUS","Ongoing");
                                    context.startActivity(intent);
                                    finish();
                                }
                            })
                            .show();
                }
                else {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Warning")
                            .setContentText(""+message)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Intent intent = new Intent(context, Pending_Trips.class);
                                    context.startActivity(intent);
                                  finish();
                                }
                            })
                            .show();
                }
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
