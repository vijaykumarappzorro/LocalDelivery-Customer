package com.example.appzaorro.myapplication.com.getter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appzaorro.myapplication.Choose_A_Driver;
import com.example.appzaorro.myapplication.R;
import com.example.appzaorro.myapplication.com.task.excute.Accepted_EstimatedRequest;
import com.example.appzaorro.myapplication.com.task.excute.GetPendingRequest;
import com.example.appzaorro.myapplication.fragment.com.Pending_Trips_Fragment;

import java.util.ArrayList;

/**
 * Created by vijay on 18/11/16.
 */

public class DriverResposeAdapter  extends BaseAdapter{


        Context context;
        ArrayList<DriverDetail> list;
        private LayoutInflater inflater=null;
      SharedPreferences sharedPreferences;
        public DriverResposeAdapter(Context context, ArrayList<DriverDetail> list) {

            this.list =list;
            this.context = context;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            sharedPreferences = context.getSharedPreferences("CUSTOMER_DETAIL", Context.MODE_PRIVATE);
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
            final DriverDetail users = list.get(position);
            rowView = inflater.inflate(R.layout.choose_a_driver, null);

            holder.txtdrivername=(TextView) rowView.findViewById(R.id.drivername);
            holder.txtprice=(TextView) rowView.findViewById(R.id.price);
            holder.txtdate=(TextView) rowView.findViewById(R.id.txtdate);
            holder.txttime=(TextView) rowView.findViewById(R.id.txttime);
            holder.approve=(Button)rowView.findViewById(R.id.btnapprove) ;
            holder.driverimage=(ImageView) rowView.findViewById(R.id.profile_image);
            holder.txtdrivername.setText(users.getDrivername());
            holder.txtprice.setText(""+users.getPrice());
            holder.txtdate.setText(users.getDate());
            holder.txttime.setText(users.getTime());

            holder.approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = users.getDriverid();
                    String usaerid=sharedPreferences.getString("USERID","");
                    Log.e("Driver id",id);
                    Toast.makeText(context,"Driver id  " +id,Toast.LENGTH_LONG ).show();
                    Accepted_EstimatedRequest accepted_estimatedRequest = new Accepted_EstimatedRequest();
                    accepted_estimatedRequest.Accepted_EstimatedRequest(context, Operations.acceptEstimatedDeliveryRequest(context,usaerid,Choose_A_Driver.requestId,id));

                }


            });

            return rowView;
        }


    public class Holder
    {
        TextView txtdrivername,txtprice,txtdate,txttime;
        ImageView driverimage;
        Button approve;


    }

}
