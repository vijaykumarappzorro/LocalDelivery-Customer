package com.example.appzaorro.myapplication.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.appzaorro.myapplication.R;
import com.example.appzaorro.myapplication.controller.CompleteRequestManager;
import com.example.appzaorro.myapplication.controller.ModelManager;
import com.example.appzaorro.myapplication.model.Constants;
import com.example.appzaorro.myapplication.model.Event;
import com.example.appzaorro.myapplication.model.Ldshareadprefernce;
import com.example.appzaorro.myapplication.model.Operations;
import com.example.appzaorro.myapplication.model.getter.Completed_getter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by vijay on 12/10/16.
 */

public class Completed_Trips_Fragment extends Fragment {
    ListView listView1;
    ImageView imageView;
    TextView txtinstruction;
    ACProgressFlower dialog;


    public Completed_Trips_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

          View view =inflater.inflate(R.layout.completedlist, container, false);
          listView1 = (ListView)view.findViewById(R.id.listview);
          imageView =(ImageView)view.findViewById(R.id.imageview);
          txtinstruction=(TextView)view.findViewById(R.id.txtview);


        //CompleteRequest completeRequest = new CompleteRequest();
       /* dialog = new ACProgressFlower.Builder(getActivity()).build();
        dialog.show();*/
        ModelManager.getInstance().getCompleteRequestManager().CompleteRequestManager(getActivity(), Operations.completeRquestbyDriver(
                getActivity(), Ldshareadprefernce.readString(getActivity(),"USERID")
        ));


      //  completeRequest.CompleteRequest(getActivity(), Operations.completeRquestbyDriver(getActivity(), Ldshareadprefernce.readString(getActivity(),"USERID")));

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
    public void onEvent(Event event){

        Log.e("calling to eventbus","");


        switch (event.getKey()) {

            case "COMPLETE":
             /*  CompleteAdtapter completeAdtapter = new CompleteAdtapter(getActivity(),CompleteRequest.arrayList);
                listView1.setAdapter(completeAdtapter);*/
                break;
            case Constants.completedtripstatus:
                int id = Integer.parseInt(event.getValue());
                if (id>0){
                    Log.e("data are ","avilable");
                    listView1.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    txtinstruction.setVisibility(View.GONE);
                    CompleteAdtapter completeAdtapter1 = new CompleteAdtapter(getActivity(), CompleteRequestManager.arrayList);
                    listView1.setAdapter(completeAdtapter1);

                }
                else {
                    Log.e("data are ","avilable");
                    listView1.setVisibility(View.GONE);
                    txtinstruction.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);

                }
        }
    }

    public class CompleteAdtapter extends BaseAdapter {
        Context context;
        String staus ="gone";
        ArrayList<Completed_getter>list;
        private LayoutInflater inflater=null;
        public CompleteAdtapter(Context context, ArrayList<Completed_getter> list) {
            this.list = list;
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

            rowView = inflater.inflate(R.layout.completedtrip_dialog, null);

            final Completed_getter completeTripDeatil = list.get(position);
            holder.addresslayout =(RelativeLayout) rowView.findViewById(R.id.pickuplayout);
            holder.relativeLayout=(RelativeLayout)rowView.findViewById(R.id.farelayout);
            holder.linearLayout=(LinearLayout)rowView.findViewById(R.id.addressLayout);
            holder.txtrequestid=(TextView) rowView.findViewById(R.id.txtorderid);
            holder.txtcash=(TextView) rowView.findViewById(R.id.txtcash);
            holder.txtdetail=(TextView) rowView.findViewById(R.id.txtdetail);
            holder.txtview=(TextView) rowView.findViewById(R.id.txtviewdetail);
            holder.txtpick=(TextView) rowView.findViewById(R.id.txtpikupAddress);
            holder.txtdrop=(TextView) rowView.findViewById(R.id.txtdropaddress);
            holder.txtmiledistnce=(TextView) rowView.findViewById(R.id.txtmiles);
            holder.txtminute=(TextView) rowView.findViewById(R.id.txttime);
            holder.txtmiledistancefare =(TextView)rowView.findViewById(R.id.txtmilesfare) ;
            holder.txttimefare=(TextView) rowView.findViewById(R.id.txttimefare);
            holder.txtsubtotal=(TextView) rowView.findViewById(R.id.txtsutoatlamount);
            holder.txtpromotion=(TextView) rowView.findViewById(R.id.txtpromotionamount);
            holder.txttoatlcash=(TextView) rowView.findViewById(R.id.txttotalpayble);

           /* holder.addresslayout.setVisibility(View.GONE);
            holder.relativeLayout.setVisibility(View.GONE);
            holder.linearLayout.setVisibility(View.GONE);*/

            holder.mapview=(ImageView) rowView.findViewById(R.id.mapimage);
            holder.driverimage=(ImageView) rowView.findViewById(R.id.imageview);

            Log.e("pickup address",completeTripDeatil.getPickupaddress());

            Log.e("drop address",completeTripDeatil.getDropaddress());

            holder.txtrequestid.setText(completeTripDeatil.getRequestid());
            holder.txtcash.setText(completeTripDeatil.getCahs());
            holder.txtdetail.setText(" Driver Id: "+completeTripDeatil.getDriverid()+"\n\n Name: "+completeTripDeatil.getDrivername()+"\n\n Mobile: "+completeTripDeatil.getDrivermobile());
            holder.txtpick.setText(completeTripDeatil.getPickupaddress());
            holder.txtdrop.setText(completeTripDeatil.getDropaddress());
            holder.txtmiledistnce.setText(completeTripDeatil.getDistance());
            holder.txttoatlcash.setText(completeTripDeatil.getCahs());
            Log.e("Status",staus);

            holder.txtview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(staus.equals("gone")){


                        Log.e("click on ","view detail0");
                        holder.addresslayout.setVisibility(View.VISIBLE);
                        holder.txtview.setText("Hide deatil");
                        holder.relativeLayout.setVisibility(View.VISIBLE);
                        holder.linearLayout.setVisibility(View.GONE);
                        staus ="visible";

                    }
                    else if (staus.equals("visible")){
                        Log.e("click on ","on hide");

                        holder.addresslayout.setVisibility(View.GONE);
                        holder.relativeLayout.setVisibility(View.GONE);
                        holder.txtview.setText("View detail");
                        staus ="gone";
                    }


                }
            });
            return rowView;
        }

    }
    public class Holder
    {
        TextView txtrequestid,txtcash,txtdetail,txtview,txtpick,txtdrop,txtmiledistnce,txtminute,txtmiledistancefare,
                txttimefare,txtsubtotal,txtpromotion,txttoatlcash;
        ImageView mapview,driverimage;

        RelativeLayout relativeLayout ,addresslayout;
        LinearLayout linearLayout;


    }
}

