package com.example.appzaorro.myapplication.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.appzaorro.myapplication.R;
import com.example.appzaorro.myapplication.model.Config;
import com.example.appzaorro.myapplication.view.fragment.Completed_Trips_Fragment;
import com.example.appzaorro.myapplication.view.fragment.Pending_Trips_Fragment;

import java.util.ArrayList;
import java.util.List;

public class Pending_Trips extends AppCompatActivity {

     Toolbar toolbar;
     TabLayout tabLayout;
     ViewPager viewPager;
     public static String pending_status ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending__trips);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Pending Trips");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pending_status= Config.pending_status;
        Log.e("log.e",pending_status);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Pending_Trips_Fragment(), "pending");
        adapter.addFragment(new Completed_Trips_Fragment(), "Completed");
       /* adapter.addFragment(new Customer_Request_pop(), "customer_Request");
        adapter.addFragment(new Driver_Request_accepted(), "Driver_Request");
        adapter.addFragment(new Choose_a_driver(), "choose a Driver");*/
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(Pending_Trips.this,HomePage_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
