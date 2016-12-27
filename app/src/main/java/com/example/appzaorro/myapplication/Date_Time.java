package com.example.appzaorro.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.appzaorro.myapplication.com.getter.Create_EVENTBUS;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

/**
 * Created by vijay on 16/11/16.
 */

public class Date_Time {
  private   Context context;
    static int year,month, day,hour,minute,second;
    private EventBus bus = EventBus.getDefault();


    public Date_Time(Context applicationContext) {

        this.context = applicationContext;
    }




    public void dateDialog(){

         Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {



                Config.date= dayOfMonth +" -" +month+1 + "-" +year;
                EventBus.getDefault().post(new Create_EVENTBUS("DATE", dayOfMonth +" -" +(month+1) + "-" +year));


            }
        },year,month,day);
        datePickerDialog.show();


    }
    public  void timedialog(){
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute =c.get(Calendar.MINUTE);
        second =c.get(Calendar.SECOND);
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String AM_PM ;
                if(hourOfDay < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }

                Config.time = hourOfDay + ":" + minute;
                EventBus.getDefault().post(new Create_EVENTBUS("TIME",hourOfDay+":" +minute +" "+AM_PM));

            }



        },hour,minute,false);
        timePickerDialog.show();
    }




}
