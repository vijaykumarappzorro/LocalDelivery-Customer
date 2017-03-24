package com.example.appzaorro.myapplication.model.getter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.appzaorro.myapplication.R;

import java.util.ArrayList;

/**
 * Created by vijay on 13/1/17.
 */

public class Search_adapter extends BaseAdapter {


    Context context;
    ArrayList<String>list;
    private LayoutInflater inflater=null;
    public Search_adapter(Context context, ArrayList<String> list) {

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

        rowView = inflater.inflate(R.layout.searchaddressadapter, null);

        holder.txtsearch=(TextView) rowView.findViewById(R.id.heading);

        holder.txtsearch.setText(list.get(position));


        return rowView;
    }


    public class Holder
    {
        TextView txtsearch;



    }

}
