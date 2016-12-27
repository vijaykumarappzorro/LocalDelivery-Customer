package com.example.appzaorro.myapplication.com.login.com;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appzaorro.myapplication.Config;
import com.example.appzaorro.myapplication.HomePage_Activity;
import com.example.appzaorro.myapplication.R;

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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Forgot_Password extends AppCompatActivity {
TextView instruction;
    EditText email_id;
    ProgressDialog progressDialog;
    Button reset_password;
    String Email_id;
    String response;
    String action="forgot_password";
    String user_type = "customer";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);
        instruction =(TextView)findViewById(R.id.txtprotocol);
        email_id =(EditText)findViewById(R.id.edtemail);
        reset_password =(Button)findViewById(R.id.btnreset);
        progressDialog = new ProgressDialog(Forgot_Password.this);
        instruction.setText("Enter the email address you register with and \n" +"we will send a link to rest your\n" +"password");

        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Email_id = email_id.getText().toString();
                if (Email_id.isEmpty()){
                    email_id.setError("Required");
                }
                else if (!emailValidator(Email_id)){

                    email_id.setError("Please fill the valid email id");
                }
                else{

                    new ForgotPassowrd().execute(action,Email_id,user_type);
                }
            }
        });
    }
    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    class ForgotPassowrd extends AsyncTask<String, String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Loading..");
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            DefaultHttpClient hc=new DefaultHttpClient();
            ResponseHandler<String> res=new BasicResponseHandler();
            HttpPost postMethod=new HttpPost(Config.forgot_password);
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("action", params[0]));
            nameValuePairs.add(new BasicNameValuePair("email", params[1]));
            nameValuePairs.add(new BasicNameValuePair("user_type", params[2]));
          try {
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response=hc.execute(postMethod,res);
            /*  String new_url = Config.forgot_password+"" +nameValuePairs;
              String str= new_url.replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\\,","&").replaceAll(" ","");
              Log.e("url",str);*/
                Log.e("Login Response: ", ""+response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                String result = jsonObject1.getString("message");
                String id= jsonObject1.getString("id");
                int result_id = Integer.parseInt(id);
                if(result_id>=1){
                    Toast.makeText(Forgot_Password.this," "+result,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Forgot_Password.this,Login_activity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(Forgot_Password.this," "+result+ "  Please fill registerd email id",Toast.LENGTH_LONG).show();
                }



            }catch (JSONException ex) {

                ex.printStackTrace();
            }
        }
    }
}
