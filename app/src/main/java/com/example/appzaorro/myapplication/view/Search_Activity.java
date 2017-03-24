package com.example.appzaorro.myapplication.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.appzaorro.myapplication.R;
import com.example.appzaorro.myapplication.controller.ModelManager;
import com.example.appzaorro.myapplication.controller.SearchAddressManager;
import com.example.appzaorro.myapplication.model.Config;
import com.example.appzaorro.myapplication.model.Constants;
import com.example.appzaorro.myapplication.model.Event;
import com.example.appzaorro.myapplication.model.Ldshareadprefernce;
import com.example.appzaorro.myapplication.model.database.LocationDatabase;
import com.example.appzaorro.myapplication.model.getter.LocationBean;
import com.example.appzaorro.myapplication.model.getter.RecentLocationdapter;
import com.example.appzaorro.myapplication.model.getter.RecentlocationBean;
import com.example.appzaorro.myapplication.model.getter.SearchAddressAdapter;
import com.example.appzaorro.myapplication.model.getter.SearchAddressBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class Search_Activity extends AppCompatActivity {
  //  SearchView search;
    ListView listView,recentlist;
    private EventBus bus = EventBus.getDefault();
    String status= "YES";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    ArrayList arrayList;
    static String API_KEY ="AIzaSyD_ZxOh6Iyhld4l1q9nswiPYHZrpIvdC1E";
    ArrayList<String>placeid;
    ArrayList<String> rsultlist;
    ArrayList<String>listrecent;
    String searchstatus,filledaddress;
    List<LocationBean>priviouslist;
    ArrayList<SearchAddressBean>address;
    ArrayList<RecentlocationBean>recentloction;
    LocationDatabase locationDatabase;
    SearchAddressAdapter searchAddressAdapter;
    Context context;
    ArrayAdapter arrayAdapter;
    EditText serach;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_);
        context=this;
        initview();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                status ="NO";
                if(Config.SearchStatus.equals("PICK")){
                   // Intent intent = new Intent(Search_Activity.this, HomePage_Activity.class);
                  //  intent.putExtra("placeid", placeid.get(position));
                    Config.SearchStatus ="PICK";
                    Config.place_id = placeid.get(position);
                    ModelManager.getInstance().getPlaceParser().getAddress(context,placeid.get(position),"PICK");

                }
              else if (Config.SearchStatus.equals("DEST")){

                    //Intent intent = new Intent(Search_Activity.this, HomePage_Activity.class);
                    //intent.putExtra("placeid", placeid.get(position));
                    Config.SearchStatus ="DEST";
                    Config.place_id = placeid.get(position);
                    ModelManager.getInstance().getPlaceParser().getAddress(context,placeid.get(position),"DEST");
                }
                else if (Config.SearchStatus.equals("HOMELOCATION")){

                    Config.place_id =placeid.get(position);
                    ModelManager.getInstance().getPlaceParser().getAddress(context,placeid.get(position),"HOMELOCATION");


                }
                else if (Config.SearchStatus.equals("WORKLOCATION")){

                    Config.place_id =placeid.get(position);
                    ModelManager.getInstance().getPlaceParser().getAddress(context,placeid.get(position),"WORKLOCATION");


                }
                else {
                    Config.place_id = placeid.get(position);
                    Config.SearchStatus="DROP";
                    finish();
                }
            }
        });
       recentlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               LocationBean bean = priviouslist.get(position);
               if (Config.SearchStatus.equals("DEST")){
               /*    Config.recentlat= bean.getSourcelat();
                   Config.recentlng= bean.getSourcelng();*/
                   Ldshareadprefernce.putString(context,"dest_latitude",bean.getSourcelat());
                   Ldshareadprefernce.putString(context,"dest_longitude",bean.getSourcelng());
                   Config.SearchStatus="DESTCHOOSE";
                   finish();
               }
               else if(Config.SearchStatus.equals("PICK")){
                   Ldshareadprefernce.putString(context,"source_latitude",bean.getSourcelat());
                   Ldshareadprefernce.putString(context,"source_longitude",bean.getSourcelng());
                   Config.recentlat= bean.getSourcelat();
                   Config.recentlng= bean.getSourcelng();
                   Config.SearchStatus="PICKCHOOSE";
                   finish();
               }
               else if (Config.SearchStatus.equals("WORKLOCATION")){

                  Ldshareadprefernce.putString(context,"work_latitude",bean.getSourcelat());
                   Ldshareadprefernce.putString(context,"work_longitude",bean.getSourcelng());
                   Config.SearchStatus="WORKLOCATION";
                   finish();

               }
               else if (Config.SearchStatus.equals("HOMELOCATION")){

                   Ldshareadprefernce.putString(context,"home_latitude",bean.getSourcelat());
                   Ldshareadprefernce.putString(context,"home",bean.getSourcelng());
                   Config.SearchStatus="HOMELOCATION";
                   finish();
               }


           }
       });

            serach.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    ModelManager.getInstance().getSearchAddressManager().getAddress(context,s.toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
      /*  search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.e("onText","textsubmit");

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                placeid.clear();

                String location_name = String.valueOf(search.getQuery());
                Log.e("serch location",newText);

                if (newText.isEmpty()){

                    listView.setAdapter(null);
                }

                ModelManager.getInstance().getSearchAddressManager().getAddress(context,newText);
                //  autocompletedText(newText);




                return false;
            }

        });*/

    }

    public void initview(){

        locationDatabase = new LocationDatabase(this);
        serach =(EditText)findViewById(R.id.searchView1);

        //search=(SearchView) findViewById(R.id.searchView1);
        listView =(ListView)findViewById(R.id.list_item);
        recentlist =(ListView)findViewById(R.id.listrecentlocation);
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent = getIntent();
        searchstatus = intent.getStringExtra("SEARCHSTATUS");
        filledaddress = intent.getStringExtra("filledtext");
        listrecent = new ArrayList<>();
        priviouslist = locationDatabase.getAllFriends();
        Log.e("serachstatus",""+searchstatus);
        // previouslocation= myConnection.selectAllData();
        placeid = new ArrayList<>();
        //search.setIconifiedByDefault(true);
        //search.setIconified(false);

        if (priviouslist.size()>0){

            recentloction = new ArrayList<>();

            for (int i=0; i<priviouslist.size();i++){
                LocationBean locationBean = priviouslist.get(i);
                //    RecentlocationBean recentlocationBean = recentloction.add(locationBean.getSourceadd(),locationBean.getSourcearea());
                RecentlocationBean recentlocationBean = new RecentlocationBean(locationBean.getSourceadd(),locationBean.getSourcearea());
                recentloction.add(recentlocationBean);
            }
            RecentLocationdapter recentLocationdapter = new RecentLocationdapter(context,recentloction);
            recentlist.setAdapter(recentLocationdapter);
            recentLocationdapter.notifyDataSetChanged();

        }


    }

    @Subscribe
    public void onEvent(Event event){
        Log.e("call","eventbus");

        switch (event.getKey()) {

            case Constants.picksearch:
                finish();
                break;
            case Constants.dropsearch:
                finish();
                break;
            case Constants.homelocation:
                finish();
                break;
            case Constants.worksearch:
                finish();
                break;
            case Constants.address_success:
                Log.e("on call","0");
                placeid = SearchAddressManager.placeIdList;
                address = SearchAddressManager.addressList;
                Log.e("addresslist",address.toString());
                searchAddressAdapter = new SearchAddressAdapter(context,address);
                listView.setAdapter(searchAddressAdapter);
                searchAddressAdapter.notifyDataSetChanged();
                break;
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(context);

    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(context);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
