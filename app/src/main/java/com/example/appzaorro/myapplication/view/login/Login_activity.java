package com.example.appzaorro.myapplication.view.login;

/**
 * Created by vijay on 5/10/16.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.appzaorro.myapplication.view.HomePage_Activity;
import com.example.appzaorro.myapplication.view.LoginFacebookActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

import static com.example.appzaorro.myapplication.model.Config.device_token;
import static com.example.appzaorro.myapplication.model.Constants.USERDETAIL;


public class Login_activity extends AppCompatActivity {
    Toolbar toolbar;
    EditText user_email, user_password;
    TextInputLayout user_email1, user_password1;
    TextView forgot_password, newuser_signup;
    String strPassword, strMbile;
    String email, password, Register_id;
    String response;

    Button login;
    ImageView facebook_login;
    LoginButton loginButton;

    CallbackManager callbackManager;
    Context mContext;

    AlertDialog dialog;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login_activity);
        mContext = this;

        initviews();


        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Login_activity.this, Forgot_Password.class);
                startActivity(intent1);
                finish();
            }
        });

        newuser_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Login_activity.this, Custom_Registration.class);
                startActivity(intent1);


            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginWithUser();
            }
        });

    }

// here we imlement the facebook login

    public void faceBookiLogin(View view){

        ModelManager.getInstance().getFacebookLoginManager().doFacebookLogin(this,callbackManager);

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){

            dialog = new SpotsDialog(mContext);
            dialog.show();
            ModelManager.getInstance().getFacebookLoginManager().getFacebookData(Login_activity.this);
        }


    }

    private boolean validateEmail() {
        if (user_email.getText().toString().trim().isEmpty()) {
            user_email1.setError("Required");
            requestFocus(user_email);
            return false;

        } else if (!Util.emailValidator(user_email.getText().toString().toString())) {
            user_email1.setError("Please fill the Registerd Email id");
            requestFocus(user_email);
            return false;
        } else {
            user_email1.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (user_password.getText().toString().trim().isEmpty()) {
            user_password1.setError("Required");
            requestFocus(user_email);
            return false;
        } else {
            user_password1.setErrorEnabled(false);
        }
        return true;
    }

    public void loginWithUser() {


        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }



        email = user_email1.getEditText().getText().toString().trim();
        password = user_password1.getEditText().getText().toString().trim();


        dialog = new SpotsDialog(mContext);
        dialog.show();
        ModelManager.getInstance().getLoginManager().LoginManager(mContext,Operations.loginUser(mContext,email,password,device_token));


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
         
        }

        return super.onOptionsItemSelected(item);
    }

    //  to check the value in edittext
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.username:

                    break;
                case R.id.userpassword:
                    break;
            }
        }
    }

    /* intialize all widgt here ...*/
    public void initviews() {

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Login Here");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        forgot_password = (TextView) findViewById(R.id.txtforgot);
        newuser_signup = (TextView) findViewById(R.id.txtnewuser);
        login = (Button) findViewById(R.id.btnlogin);
        user_email1 = (TextInputLayout) findViewById(R.id.edtusername);
        user_password1 = (TextInputLayout) findViewById(R.id.edtuserpassword);
        user_email = (EditText) findViewById(R.id.username);

        user_password = (EditText) findViewById(R.id.userpassword);
        user_email.addTextChangedListener(new MyTextWatcher(user_email));
        user_password.addTextChangedListener(new MyTextWatcher(user_password));
        facebook_login = (ImageView) findViewById(R.id.imgfacebook);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        Config.device_token = FirebaseInstanceId.getInstance().getToken();
        Log.e("tokenid", "" + Config.device_token);






      //  Toast.makeText(Login_activity.this, "if you have not Register with us please signup first", Toast.LENGTH_LONG).show();


    }



    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void onEvent(Event event){
        Log.e("calling to eventbus","");
        switch (event.getKey()) {
            case USERDETAIL:
                dialog.dismiss();
                Ldshareadprefernce.putString(mContext,"login_status","true");
                Intent intent = new Intent(mContext,HomePage_Activity.class);
                startActivity(intent);
                finish();
                break;
            case Constants.LOGIN:
                dialog.dismiss();
                Ldshareadprefernce.putString(mContext,"facebook_login","false");
                dialog = new SpotsDialog(mContext);
                dialog.show();
                ModelManager.getInstance().getUserDeatilManager().UserDeatilManager(mContext, Operations.userDeatil(mContext,
                        Ldshareadprefernce.readString(mContext, "user_id")));
                break;
            case Constants.SERVER_ERROR:
                dialog.dismiss();
                Toast.makeText(mContext, "Your internet connection is too slow please try again", Toast.LENGTH_SHORT).show();
                break;

            case  Constants.FACEBOOK_LOGINEMPTY:
                dialog.dismiss();
                Intent intent1 = new Intent(mContext, LoginFacebookActivity.class);
                startActivity(intent1);
                break;

            case  Constants.FACEBOOK_LOGIN_SUCCESS:
                dialog.dismiss();
                Intent intent2 = new Intent(mContext,HomePage_Activity.class);
                startActivity(intent2);
                finish();
                break;
            case  Constants.LOGIN_ERROR:
                    dialog.dismiss();
                    Config.event_meesage =event.getValue();
                    new SweetAlertDialog(mContext,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText(Config.event_meesage)
                            .show();
                break;



        }

    }
}







