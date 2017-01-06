package com.example.appzaorro.myapplication.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.appzaorro.myapplication.R;

import java.util.ArrayList;

public class Notification extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    ArrayList<String>notification,datetime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Notifcation_getter");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        listView =(ListView)findViewById(R.id.notificationlist);
        notification = new ArrayList<>();
        datetime = new ArrayList<>();
        notification.add("Rates have dropped in Sector 22 Chandigarh. Request a ride as soon as possible to avoid surge pricing.");
        notification.add("Rates have dropped in Sector 22 Chandigarh.Request a ride as soon as possible to avoid surge pricing.");
        notification.add("Rates have dropped in Sector 22 Chandigrh.Request a ride as soon as possible to avoid surge pricing.");
        datetime.add("Nov-15-2016 : 3:40 pm");
        datetime.add("Nov-15-2016 : 3:40 pm");
        datetime.add("Nov-15-2016 : 3:40 pm");
        Log.e("notification",notification.toString());
        Log.e("date",datetime.toString());


        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),notification,datetime);
        listView.setAdapter(customAdapter);



    }

    @Override
    protected void onResume() {
        super.onResume();

        /*CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),notification,datetime);
        listView.setAdapter(customAdapter);*/
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(Notification.this,HomePage_Activity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class CustomAdapter extends BaseAdapter {
        Context context;
        ArrayList<String> notification,date;
        private LayoutInflater inflater=null;
        public CustomAdapter(Context context,ArrayList<String>notification,ArrayList<String>date) {

            this.notification =notification;
            this.date = date;
            this.context = context;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            Log.e("lenth of arraylist", String.valueOf(notification.size()));
            return notification.size();
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

            rowView = inflater.inflate(R.layout.notificationadapter, null);
            holder.txtdate_time =(TextView)rowView.findViewById(R.id.txtdatetime);
            holder.txtnotification =(TextView)rowView.findViewById(R.id.txtnotification);
            String notify = notification.get(position);
            String datewithtime = date.get(position);

            holder.txtdate_time.setText(""+datewithtime);
            holder.txtnotification.setText(""+notify);
            return rowView;
        }

    }
    public class Holder
    {
        TextView txtnotification,txtdate_time;


    }
}
