package com.example.appzaorro.myapplication.model.getter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.appzaorro.myapplication.R;

import java.util.ArrayList;

/**
 * Created by vijay on 24/1/17.
 */

public class RecentLocationdapter  extends BaseAdapter {
    Context context;
    ArrayList<RecentlocationBean> addressBeen;
    private LayoutInflater inflater=null;

    public RecentLocationdapter(Context context,ArrayList<RecentlocationBean>addressBeen){

        this.context=context;
        this.addressBeen=addressBeen;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return addressBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder=new Holder();
        View rowView = convertView;
        final RecentlocationBean searchaddress = addressBeen.get(position);
        rowView = inflater.inflate(R.layout.searchaddressadapter, null);
        holder.txtheading=(TextView) rowView.findViewById(R.id.heading);
        holder.txtaddres=(TextView) rowView.findViewById(R.id.address);
        holder.txtheading.setText(searchaddress.getAddress());

        holder.txtaddres.setText(searchaddress.getAddressarea());

        return rowView;
    }

    public class Holder
    {
        TextView txtheading,txtaddres;


    }
}
