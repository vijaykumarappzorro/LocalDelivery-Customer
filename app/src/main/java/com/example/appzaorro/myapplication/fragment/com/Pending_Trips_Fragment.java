package com.example.appzaorro.myapplication.fragment.com;

/**
 * Created by vijay on 12/10/16.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appzaorro.myapplication.Choose_A_Driver;
import com.example.appzaorro.myapplication.Config;
import com.example.appzaorro.myapplication.HomePage_Activity;
import com.example.appzaorro.myapplication.Pending_Trips;
import com.example.appzaorro.myapplication.R;
import com.example.appzaorro.myapplication.ServiceHandler;
import com.example.appzaorro.myapplication.com.getter.Create_EVENTBUS;
import com.example.appzaorro.myapplication.com.getter.Operations;
import com.example.appzaorro.myapplication.com.getter.PendingRequest;
import com.example.appzaorro.myapplication.com.task.excute.GetPendingRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Pending_Trips_Fragment extends Fragment {
    ListView listView;
    String downstatus="CLOSE";
    SharedPreferences sharedPreferences;
    String status="pending";





    public Pending_Trips_Fragment() {

        }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pendingrequestlist, container, false);
        listView = (ListView)view.findViewById(R.id.requestlist);
        Intent intent = getActivity().getIntent();
        String status = intent.getStringExtra("STATUS");
        Log.e("statisss",""+status);
        ImageView imageView =(ImageView)view.findViewById(R.id.imageview);
        TextView textView =(TextView)view.findViewById(R.id.txtview);
        sharedPreferences = getActivity().getSharedPreferences("CUSTOMER_DETAIL", Context.MODE_PRIVATE);
        Config.customer__id =sharedPreferences.getString("USERID","");


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PendingRequest pendingRequest =  GetPendingRequest.arrayList.get(position);
                String requestID= pendingRequest.getRequestid();
                Log.e("Requestid",requestID);

                    Intent intent = new Intent(getContext(), Choose_A_Driver.class);
                    intent.putExtra("postion", String.valueOf(position));
                    intent.putExtra("requestid", requestID);
                    Log.e("sending postion", String.valueOf(position));
                    startActivity(intent);
                    getActivity().finish();
            }
        });

        GetPendingRequest getPendingRequest = new GetPendingRequest();
        getPendingRequest.GetPendingRequest(getActivity(), Operations.getPendingRequest(getActivity(),Config.customer__id));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onEvent(Create_EVENTBUS event){
        ArrayList<PendingRequest>arrayList = new ArrayList<>();
        PendingRequest request = new PendingRequest();


        switch (event.getKey()) {

            case "Response":
                CustomAdapter customAdapter = new CustomAdapter(getActivity(),GetPendingRequest.arrayList);
                listView.setAdapter(customAdapter);
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        /*Intent intent = new Intent(getActivity(),Pending_Trips.class);
        getActivity().startActivity(intent);*/
        Log.e("onresusmee","");

    }

    public class CustomAdapter extends BaseAdapter{
       Context context;
        ArrayList<PendingRequest>list;
        private LayoutInflater inflater=null;
        public CustomAdapter(Context context,ArrayList<PendingRequest>list) {

          this.list =list;
          this.context = context;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            Log.e("lenth of arraylist", String.valueOf(list.size()));
            return list.size();
        }
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Holder holder=new Holder();
            View rowView = convertView;
            final PendingRequest users = list.get(position);
            rowView = inflater.inflate(R.layout.pending_fragment, null);
            holder.relativeLayout =(RelativeLayout)rowView.findViewById(R.id.layout2);
            holder.txtrequestid=(TextView) rowView.findViewById(R.id.productid);
            holder.txtdescripton=(TextView) rowView.findViewById(R.id.productdescription);
            holder.txtstatusreport=(TextView) rowView.findViewById(R.id.statusReport);
            holder.txtdatetime=(TextView) rowView.findViewById(R.id.txtorderdate);
            holder.txtprice=(TextView) rowView.findViewById(R.id.txtcash);
            holder.txtpayment=(TextView) rowView.findViewById(R.id.txtpaymentshow);
            holder.txtpick=(TextView) rowView.findViewById(R.id.txtpick_address);
            holder.txtdrop=(TextView) rowView.findViewById(R.id.txtdeliver_address);
            holder.imgscroll=(ImageView) rowView.findViewById(R.id.scrollimage);
            final String staus= users.getStatusreport();
            holder.relativeLayout.setVisibility(View.GONE);
            holder.txtrequestid.setText(users.getRequestid());
            holder.txtdescripton.setText(users.getDescrption());
            if (staus.equals("1")){

                holder.txtstatusreport.setText("Ongoing");
                holder.txtstatusreport.setTextColor(Color.BLUE);
            }
            else  if (staus.equals("2")){
                holder.txtstatusreport.setText("Completed");
                holder.txtstatusreport.setTextColor(Color.BLUE);
            }
            else if (staus.equals("0")){
                holder.txtstatusreport.setText("Pending");
                holder.txtstatusreport.setTextColor(Color.RED);
            }
            holder.txtdatetime.setText(users.getDatetime());
            holder.txtpick.setText(users.getPickaddress());
            holder.txtdrop.setText(users.getDropaddress());
            if (staus.equals(null)){

                holder.txtprice.setVisibility(View.GONE);
                holder.txtpayment.setVisibility(View.GONE);
            }
            holder.imgscroll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(downstatus.equals("OPEN")){
                        holder.relativeLayout.setVisibility(View.GONE);
                        holder.imgscroll.setImageResource(R.drawable.ic_icon_down_arrow);
                        downstatus="CLOSE";
                    }
                    else if (downstatus.equals("CLOSE")) {
                        holder.relativeLayout.setVisibility(View.VISIBLE);
                        holder.imgscroll.setImageResource(R.drawable.ic_icon_up_arrow);
                        downstatus = "OPEN";
                    }

                }
            });



            return rowView;
        }

    }
    public class Holder
    {
        TextView txtrequestid,txtdescripton,txtstatusreport,txtdatetime,txtprice,txtpick,txtdrop,txtpayment;
        ImageView imgscroll;
        RelativeLayout relativeLayout;

    }

}
