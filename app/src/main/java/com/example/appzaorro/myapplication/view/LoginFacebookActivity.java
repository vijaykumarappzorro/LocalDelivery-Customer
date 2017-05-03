package com.example.appzaorro.myapplication.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appzaorro.myapplication.R;
import com.example.appzaorro.myapplication.controller.ModelManager;
import com.example.appzaorro.myapplication.model.Config;
import com.example.appzaorro.myapplication.model.Constants;
import com.example.appzaorro.myapplication.model.Event;
import com.example.appzaorro.myapplication.model.Ldshareadprefernce;
import com.example.appzaorro.myapplication.model.Operations;
import com.example.appzaorro.myapplication.model.Util;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import dmax.dialog.SpotsDialog;

import static com.example.appzaorro.myapplication.model.Config.device_token;


public class LoginFacebookActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = LoginFacebookActivity.class.getSimpleName();
    Activity activity = this;
    EditText  editName, editEmail, editPhone;
    String name, email, phone;
    TextView textRegister;
    RelativeLayout linearLayout;
    android.app.AlertDialog dialog;
    Toolbar toolbar;
    Context context;

    String firstname ,lastname,profile_pic,fb_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        initViews();
    }

    public void initViews() {
        context =this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Facebook Login");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        device_token = FirebaseInstanceId.getInstance().getToken();





        editName = (EditText) findViewById(R.id.edit_name);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPhone = (EditText) findViewById(R.id.edit_mobile);
        textRegister = (TextView) findViewById(R.id.text_register);

        textRegister.setOnClickListener(this);
        name = Ldshareadprefernce.readString(this, "user_name");
        Log.e("user name ",""+name);
        String split[]= name.split(" ");
        firstname = split[split.length-2];
        lastname = split[split.length-1];


        email = Ldshareadprefernce.readString(this, "user_email");

        editName.setText(name);
        editEmail.setText(email);

        if (!email.isEmpty())
            editEmail.setText(email);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.text_register:

                name = editName.getText().toString().trim();
                email = editEmail.getText().toString().trim();
                phone = editPhone.getText().toString().trim();

                profile_pic = Ldshareadprefernce.readString(activity, "user_image");
                Log.e("profile pic ",profile_pic);

                fb_id = Ldshareadprefernce.readString(activity, "facebook_id");
                Log.e("facebook id ",""+fb_id);

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                    Util.makeSnackBar(activity, linearLayout, "Please fill all the details");
                    return;
                } else if (!Util.emailValidator(email)) {

                    Util.makeSnackBar(activity, linearLayout, "Please enter the valid email address");
                }
                else if (phone.isEmpty()){

                    Util.makeSnackBar(activity, linearLayout, "Please fill the mobile number");
                }

                else {

                    dialog = new SpotsDialog(context);
                    dialog.show();
                    ModelManager.getInstance().getFacebookLoginManager().registerUser(context, Config.fb_login_url,Operations.facebook_login(context,email,firstname,lastname,
                            editPhone.getText().toString(),fb_id,device_token,Config.device_type,Config.user_type,profile_pic));

                }

                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.FACEBOOK_LOGIN_SUCCESS:
                dialog.dismiss();
                Toast.makeText(activity, "Logged-in successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(activity, HomePage_Activity.class);
                Ldshareadprefernce.putString(activity, "login_status", "true");
                startActivity(i);
                finish();
                break;
            case Constants.SERVER_ERROR:
                dialog.dismiss();
                Util.makeSnackBar(activity, linearLayout, "Sorry, server error occurred. Please try after sometime.");
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setMessage("Are you sure you want to cancel? \nYour information will not be saved.");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

                break;
        }
        return true;
    }
}
