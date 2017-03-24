package com.example.appzaorro.myapplication.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.appzaorro.myapplication.model.getter.LocationBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vijay on 11/1/17.
 */

public class LocationDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "locationdata";

    // Friend table name
    private static final String TABLE_NAME = "student";
   // public static final String KEY_ROWID = "_id";

    private  static final String TC_SOURCELAT ="sourcelat";
    //private static final String KEY_ROLLNO = "id";
    private static final String TC_SOURCELNG = "sourcelng";
    private static final String TC_SOURCEADD = "sourceadd";
    private static final String TC_SOURCEAREA="sourcearea";

    public LocationDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FRIEND_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                 + TC_SOURCELAT + " TEXT,"
                + TC_SOURCELNG + " TEXT, " + TC_SOURCEADD +" TEXT, " + TC_SOURCEAREA +" TEXT"+ ")";
        db.execSQL(CREATE_FRIEND_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Adding a new record (friend) to table
    public Boolean insertvalue(LocationBean bean) {
        boolean flag = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(TC_SOURCELAT, bean.getSourcelat());
            values.put(TC_SOURCELNG, bean.getSourcelng());
            values.put(TC_SOURCEADD, bean.getSourceadd());
            values.put(TC_SOURCEAREA,bean.getSourcearea());
            long row = db.insert(LocationDatabase.TABLE_NAME, null, values);
            if (row > 0) {

                flag = true;

            }
        }catch (Exception ex){

            Log.e("Insert Error", ex.toString());
        }
        // inserting this record
        return flag;
    }

    // Getting All Friends in Table of Database
    public List<LocationBean> getAllFriends() {
        List<LocationBean> friendList = new ArrayList<>();

        // select query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all table records and adding to list
        if (cursor.moveToFirst()) {
            do {
                LocationBean friend = new LocationBean();
                friend.setSourcelat(cursor.getString(0));
                //friend.setId(Integer.parseInt(cursor.getString(0)));
                friend.setSourcelng(cursor.getString(1));
                friend.setSourceadd(cursor.getString(2));
                friend.setSourcearea(cursor.getString(3));
                // Adding friend to list
                friendList.add(friend);
            } while (cursor.moveToNext());
        }
        return friendList;
    }
    public ArrayList<Integer> printAutoIncrements(){
        ArrayList<Integer>list= new ArrayList<>() ;
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do{

                list.add(cursor.getInt(0));


            }while (cursor.moveToNext());
        }

        cursor.close();
return list;
    }

}