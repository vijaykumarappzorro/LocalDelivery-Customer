package com.example.appzaorro.myapplication.view.login;

/**
 * Created by vijay on 5/10/16.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
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
import com.example.appzaorro.myapplication.model.Config;
import com.example.appzaorro.myapplication.model.Ldshareadprefernce;
import com.example.appzaorro.myapplication.view.HomePage_Activity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Login_activity extends AppCompatActivity {
    Toolbar toolbar;
    EditText user_email, user_password;
    TextInputLayout user_email1, user_password1;
    TextView forgot_password, newuser_signup;
    String strPassword, strMbile;
    String email, password, Register_id;
    String response, device_token, device_type = "A";
    String Register = "register";
    Button login;
    ImageView facebook_login;
    LoginButton loginButton;
    String user_type = "customer";
    String action1 = "user_detail", Loginstatus = "", loginstatus = "0";
    CallbackManager callbackManager;
    Context mContext;

    SharedPreferences sharedPreferences, userDetail, Customerdetail;

    SharedPreferences.Editor editor1, customeditor;

    SharedPreferences.Editor editor;
    String userid;


    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login_activity);
        mContext = this;
        if (Ldshareadprefernce.readString(mContext, "Logged").equals("Logged in")) {
            Intent i = new Intent(Login_activity.this, HomePage_Activity.class);
            startActivity(i);
            return;
        }
        Toast.makeText(Login_activity.this, "if you have not Register with us please signup first", Toast.LENGTH_LONG).show();



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
        Customerdetail = getSharedPreferences("CUSTOMER_DETAIL", Context.MODE_PRIVATE);
        customeditor = Customerdetail.edit();
        Loginstatus = Ldshareadprefernce.readString(mContext, "LOGINSTATUS");


        userid = Ldshareadprefernce.readString(mContext, "USERID");
        Log.e("useridffndnd", userid);

        if (Loginstatus.equals("1")) {

            new UserDeatilUpdate().execute(action1, userid, user_type);
            return;


        }

        intilizingWidght();
        userid = Ldshareadprefernce.readString(mContext, "USERID");
        Log.e("useridffndnd", userid);

        if (Loginstatus.equals("1")) {

            new UserDeatilUpdate().execute(action1, userid, user_type);
            return;


        }


        user_email1 = (TextInputLayout) findViewById(R.id.edtusername);
        user_password1 = (TextInputLayout) findViewById(R.id.edtuserpassword);
        user_email = (EditText) findViewById(R.id.username);
        user_password = (EditText) findViewById(R.id.userpassword);
        user_email.addTextChangedListener(new MyTextWatcher(user_email));
        user_password.addTextChangedListener(new MyTextWatcher(user_password));
        facebook_login = (ImageView) findViewById(R.id.imgfacebook);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.appzaorro.myapplication",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }


        progressDialog = new ProgressDialog(Login_activity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        // getting the deatil of user and user Type
        final Intent intent = getIntent();
        String usertype = intent.getStringExtra("custom_type");
        Log.e("user_type", "" + usertype);
        device_token = FirebaseInstanceId.getInstance().getToken();
        Log.e("tokenid", "" + device_token);
        //sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);


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
        facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onclick on", "on facebook");
                //loginWithFacebook();
                //loginButton.performClick();
                LoginManager.getInstance().logInWithReadPermissions(Login_activity.this,
                        Arrays.asList("user_friends", "email", "public_profile", "user_birthday"));

                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                setFacebookData(loginResult);
                                String facebook = loginResult.getAccessToken().getToken();
                                Log.e("facebooktoken", facebook);
                            }

                            @Override
                            public void onCancel() {
                                    

                            }

                            @Override
                            public void onError(FacebookException exception) {

                                Log.e("exception", "message");
                                exception.printStackTrace();

                            }
                        });


            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginWithUser();
            }
        });

    }


    class LoginTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            Log.e("simple url", Config.login_url);
        }

        @Override
        protected String doInBackground(String... params) {
            DefaultHttpClient hc = new DefaultHttpClient();
            ResponseHandler<String> res = new BasicResponseHandler();
            HttpPost postMethod = new HttpPost(Config.login_url);
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("action", params[0]));
            nameValuePairs.add(new BasicNameValuePair("email", params[1]));
            nameValuePairs.add(new BasicNameValuePair("password", params[2]));
            nameValuePairs.add(new BasicNameValuePair("device_token", params[3]));
            nameValuePairs.add(new BasicNameValuePair("device_type", params[4]));
            nameValuePairs.add(new BasicNameValuePair("user_type", params[5]));
            nameValuePairs.add(new BasicNameValuePair("latitude", params[6]));
            nameValuePairs.add(new BasicNameValuePair("longitude", params[7]));
            try {
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = hc.execute(postMethod, res);
                Log.e("Login Response: ", "" + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject("" + s);
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                String result = jsonObject1.getString("complete_status");
                Register_id = jsonObject1.getString("id");
                Log.e("user id of user", Register_id);
                Log.e("getting response", "" + result);
                if (result.equals("true")) {
                    Ldshareadprefernce.putString(mContext, "USERID", Register_id);
                    Ldshareadprefernce.putString(mContext, "email_id", email);
                    Ldshareadprefernce.putString(mContext, "Logged", "Logged in");
                   /* customeditor.putString("USERID",Register_id).apply();
                    editor = sharedPreferences.edit();
                    editor.putString("email_id", email);
                    editor.putString("Logged", "Logged in");
                    editor.apply();*/
                    new UserDeatilUpdate().execute(action1, Register_id, user_type);
                } else {

                    Toast.makeText(Login_activity.this, "result  " + result, Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }

            } catch (JSONException ex) {

                ex.printStackTrace();
            }

        }
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
    }

    private boolean validateEmail() {
        if (user_email.getText().toString().trim().isEmpty()) {
            user_email1.setError("Required");
            requestFocus(user_email);
            return false;

        } else if (!emailValidator(user_email.getText().toString().toString())) {
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
        String device_token = "67f234f22k";
        String action = "login";
        String device_type = "A";
        String latitude = "32.112342";
        String longititude = "76.4523442";
        email = user_email1.getEditText().getText().toString().trim();
        password = user_password1.getEditText().getText().toString().trim();

        new LoginTask().execute(action, email, password, device_token, device_type, user_type, latitude, longititude);

    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
           /* Intent intent = new Intent(Login_activity.this,HomePage_Activity.class);
            startActivity(intent);*/
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
    public void intilizingWidght() {

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
        Customerdetail = getSharedPreferences("CUSTOMER_DETAIL", Context.MODE_PRIVATE);
        customeditor = Customerdetail.edit();
        Loginstatus = Ldshareadprefernce.readString(mContext, "LOGINSTATUS");




    }

    public class Update_Facebook_Login extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("clal", "updatefacebook");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            DefaultHttpClient hc = new DefaultHttpClient();
            ResponseHandler<String> res = new BasicResponseHandler();
            HttpPost postMethod = new HttpPost(Config.custom_register_url);
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("action", params[0]));
            nameValuePairs.add(new BasicNameValuePair("email", params[1]));
            nameValuePairs.add(new BasicNameValuePair("firstname", params[2]));
            nameValuePairs.add(new BasicNameValuePair("lastname", params[3]));
            nameValuePairs.add(new BasicNameValuePair("password", params[4]));
            nameValuePairs.add(new BasicNameValuePair("mobile", params[5]));
            nameValuePairs.add(new BasicNameValuePair("device_token", params[6]));
            nameValuePairs.add(new BasicNameValuePair("device_type", params[7]));
            nameValuePairs.add(new BasicNameValuePair("user_type", params[8]));
            nameValuePairs.add(new BasicNameValuePair("latitude", params[9]));
            nameValuePairs.add(new BasicNameValuePair("longitude", params[10]));
            nameValuePairs.add(new BasicNameValuePair("profile_pic", params[11]));
            try {
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = hc.execute(postMethod, res);
                Log.e("Registration Response: ", "" + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("getting", "response");

            try {
                if (s!=null) {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                    String result = jsonObject1.getString("message");
                    Register_id = jsonObject1.getString("id");
                    int user_id = Integer.parseInt(Register_id);
                    Log.e("userid at time facebook", Register_id);

                    Log.e("getting response", "" + result);
                    if (user_id >= 1) {
                        Log.e("enter in user id", "");

                        loginstatus = "1";

                        new UserDeatilUpdate().execute(action1, Register_id, user_type);


                    } else {
                        String message = jsonObject.getString("message");
                        Toast.makeText(Login_activity.this, "result " + message, Toast.LENGTH_SHORT).show();

                    }
                }
                else {

                    Toast.makeText(mContext,"please try again",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }

    /* getting the all detail from the facebook*/
    private void setFacebookData(final LoginResult loginResult) {
        Log.e("call", "setfacebookdtat");
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {

                            Log.e("Response", response.toString());
                            String facebookid = response.getJSONObject().getString("id");
                            String email = response.getJSONObject().getString("email");
                            String firstName = response.getJSONObject().getString("first_name");
                            String lastName = response.getJSONObject().getString("last_name");
                            String gender = response.getJSONObject().getString("gender");
                            String bday = response.getJSONObject().getString("birthday");

                            Profile profile = Profile.getCurrentProfile();
                            Uri profilePictureUri = ImageRequest.getProfilePictureUri(facebookid, 200, 250);
                            String link = profile.getLinkUri().toString();
                            Log.e("Link", link);
                            if (Profile.getCurrentProfile() != null) {
                                // Register using with user Facebook Login
                                new Update_Facebook_Login().execute(Register, email, firstName, lastName, strPassword, strMbile, device_token, device_type, user_type, "45.112342", "90.4523442", String.valueOf(profilePictureUri));

                            }
                            Log.e("Login" + "Email", email);
                            Log.e("Login" + "FirstName", firstName);
                            Log.e("Login" + "LastName", lastName);
                            Log.e("Login" + "Gender", gender);
                            Log.e("Login" + "Bday", bday);
                            Log.e("facebookprofile pic", String.valueOf(profilePictureUri));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender,picture, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }
/*--------------------- Register User Detail----------------------------------------------------------------------------------------------*/


    public class UserDeatilUpdate extends AsyncTask<String, String, String> {
        String response;
        String firstname, lastname, profilepic, mobileNumber, emailid;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (Loginstatus.equals("1")) {
                progressDialog = new ProgressDialog(Login_activity.this);
                progressDialog.setTitle("Loading..");
                progressDialog.setMessage("Please wait..");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            DefaultHttpClient hc = new DefaultHttpClient();
            ResponseHandler<String> res = new BasicResponseHandler();
            HttpPost postMethod = new HttpPost(Config.user_detail_url);
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("action", params[0]));
            nameValuePairs.add(new BasicNameValuePair("user_id", params[1]));
            nameValuePairs.add(new BasicNameValuePair("user_type", params[2]));
            try {
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = hc.execute(postMethod, res);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject("" + s);
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                String id = jsonObject1.getString("id");
                int user_id = Integer.parseInt(id);
                emailid = jsonObject1.getString("email");
                firstname = jsonObject1.getString("firstname");
                lastname = jsonObject1.getString("lastname");
                profilepic = jsonObject1.getString("profile_pic");
                mobileNumber = jsonObject1.getString("mobile");
                Log.e("response of userdetail", s);
                if (user_id >= 1) {

                    String image_base_url = "http://traala.com.md-in-45.webhostbox.net/deliver/ws/profile_pics/";
                    Ldshareadprefernce.putString(mContext, "EMAILID", emailid);
                    // editor1.putString("EMAILID",emailid).apply();
                    if (Loginstatus.equals("1") || loginstatus.equals("1")) {
                        Ldshareadprefernce.putString(mContext, "PROFILEPIC", profilepic);
                        //editor1.putString("PROFILEPIC", profilepic).apply();
                    } else {
                        Ldshareadprefernce.putString(mContext, "PROFILEPIC", image_base_url + "" + profilepic);
                        //editor1.putString("PROFILEPIC", image_base_url+""+profilepic).apply();
                    }
                    Ldshareadprefernce.putString(mContext, "FULLNAME", firstname + " " + lastname);
                    Ldshareadprefernce.putString(mContext, "MOBILENUMBER", mobileNumber);
                    Ldshareadprefernce.putString(mContext, "USERID", Register_id);
                    Ldshareadprefernce.putString(mContext, "LOGINSTATUS", loginstatus);

                    /*editor1.putString("FULLNAME",firstname+" "+lastname).apply();
                    editor1.putString("MOBILENUMBER",mobileNumber).apply();
                    editor1.putString("USERID",Register_id).apply();
                    customeditor.putString("USERID",Register_id);
                    customeditor.putString("LOGINSTATUS",loginstatus).apply();
*/
                    Intent intent = new Intent(Login_activity.this, HomePage_Activity.class);
                    startActivity(intent);

                } else {
                    String message = jsonObject1.getString("message");
                    Toast.makeText(Login_activity.this, "result " + message, Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
}





