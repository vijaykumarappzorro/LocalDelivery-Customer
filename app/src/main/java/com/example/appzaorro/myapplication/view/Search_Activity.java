package com.example.appzaorro.myapplication.view;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.appzaorro.myapplication.R;
import com.example.appzaorro.myapplication.model.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Search_Activity extends AppCompatActivity {
    SearchView search;
    ListView listView;
    String status= "YES";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    ArrayList arrayList;
    static String API_KEY ="AIzaSyD_ZxOh6Iyhld4l1q9nswiPYHZrpIvdC1E";
    ArrayList<String>placeid;
    ArrayList<String> rsultlist;
    String searchstatus,filledaddress;
    ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_);
        search=(SearchView) findViewById(R.id.searchView1);
        listView =(ListView)findViewById(R.id.list_item);
        Intent intent = getIntent();
        searchstatus = intent.getStringExtra("SEARCHSTATUS");
        filledaddress = intent.getStringExtra("filledtext");
        Log.e("serachstatus",""+searchstatus);
        placeid = new ArrayList<>();
        search.setIconifiedByDefault(true);
        search.setIconified(false);
        if (filledaddress!=null){

            search.setQueryHint(filledaddress);
        }

        arrayList = autocompletedText("");
        arrayAdapter   = new ArrayAdapter(this,android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                status ="NO";
                if(Config.SearchStatus.equals("PICK")){
                   // Intent intent = new Intent(Search_Activity.this, HomePage_Activity.class);
                  //  intent.putExtra("placeid", placeid.get(position));
                    Config.SearchStatus ="PICK";
                    Config.place_id = placeid.get(position);
                   // startActivity(intent);
                    finish();
                }
              else if (Config.SearchStatus.equals("DEST")){

                    //Intent intent = new Intent(Search_Activity.this, HomePage_Activity.class);
                    //intent.putExtra("placeid", placeid.get(position));
                    Config.SearchStatus ="DEST";
                    Config.place_id = placeid.get(position);
                  //  startActivity(intent);
                    finish();
                }
                else {
                    Config.place_id = placeid.get(position);
                    Config.SearchStatus="DROP";
                    finish();
                }
            }
        });


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

                  autocompletedText(newText);




                return false;
            }

        });

    }
    public  ArrayList autocompletedText(String input){


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {

            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            Log.e("new url", String.valueOf(url));

            conn = (HttpURLConnection) url.openConnection();

            InputStreamReader in = new InputStreamReader(conn.getInputStream());


            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];

            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);

            }

        } catch (MalformedURLException e) {

            Log.e("", "Error processing Places API URL", e);

            return rsultlist;

        } catch (IOException e) {

            Log.e("", "Error connecting to Places API", e);

            return rsultlist;

        } finally {

            if (conn != null) {

                conn.disconnect();

            }

        }



        try {

            // Create a JSON object hierarchy from the results

            JSONObject jsonObj = new JSONObject(jsonResults.toString());

            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");



            // Extract the Place descriptions from the results

            rsultlist = new ArrayList(predsJsonArray.length());
            placeid=new ArrayList<>();

            for (int i = 0; i < predsJsonArray.length(); i++) {
                Log.e("description",predsJsonArray.getJSONObject(i).getString("description"));
                Log.e("placeId",predsJsonArray.getJSONObject(i).getString("place_id"));
                String descripton =predsJsonArray.getJSONObject(i).getString("description");
                rsultlist.add(predsJsonArray.getJSONObject(i).getString("description"));
                placeid.add( predsJsonArray.getJSONObject(i).getString("place_id"));
            }
            Log.e("SizeArray",""+rsultlist.size());
            Log.e("SizePlace",""+placeid.size());

            arrayAdapter   = new ArrayAdapter(this,android.R.layout.simple_list_item_1, rsultlist);
            listView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
        } catch (JSONException e) {


            Log.e("", "Cannot process JSON results", e);

        }



            return rsultlist;
    }




}
