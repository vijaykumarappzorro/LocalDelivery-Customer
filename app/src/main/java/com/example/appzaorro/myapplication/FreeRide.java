package com.example.appzaorro.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class FreeRide extends AppCompatActivity {
    Toolbar toolbar;
    Button invite;
    TextView txtfreeride;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_ride);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Free Rides");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        invite =(Button)findViewById(R.id.btninvite);

        txtfreeride =(TextView)findViewById(R.id.txtfreeride);
        txtfreeride.setText("Invite your friends to try Local\n" +"  Delivery and get Rs 50% off on\n" + "   each of your next rides");

  // here  share the local delivery link with other persons by using  share meduim
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT,"Invite your friends to try Local  Delivery and get Rs 50% off on each of your next rides");
                startActivity(Intent.createChooser(sharingIntent, ""));
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home)

        {
            Intent intent = new Intent(FreeRide.this,HomePage_Activity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
