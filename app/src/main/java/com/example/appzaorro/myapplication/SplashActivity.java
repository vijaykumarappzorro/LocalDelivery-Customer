package com.example.appzaorro.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.TextView;

import com.example.appzaorro.myapplication.com.login.com.Custom_Registration;
import com.example.appzaorro.myapplication.com.login.com.Login_activity;
import com.example.appzaorro.myapplication.com.update.com.Update_Profile_Activity;
import com.wang.avi.AVLoadingIndicatorView;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {
    private static int SPLASH_TIME_OUT = 3000;
    Animation animation;
    TextView app_name;
    AVLoadingIndicatorView avLoadingIndicatorDialog;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        app_name =(TextView)findViewById(R.id.textview);
        avLoadingIndicatorDialog =(AVLoadingIndicatorView)findViewById(R.id.avloadingIndicatorView);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, Login_activity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    void startAnim(){

        findViewById(R.id.avloadingIndicatorView).setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationStart(Animation animation) {


    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

}