package com.example.appzaorro.myapplication.view.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.appzaorro.myapplication.R;
import com.example.appzaorro.myapplication.controller.ModelManager;
import com.example.appzaorro.myapplication.model.Constants;
import com.example.appzaorro.myapplication.model.Event;
import com.example.appzaorro.myapplication.model.Operations;
import com.example.appzaorro.myapplication.model.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cc.cloudist.acplibrary.ACProgressFlower;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Forgot_Password extends AppCompatActivity {
    TextView instruction;
    EditText email_id;
    ProgressDialog progressDialog;
    Button reset_password;
    String Email_id;
    String response;
    String action = "forgot_password";
    String user_type = "customer";
    private EventBus bus = EventBus.getDefault();
    Context context;
    ACProgressFlower dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);
        instruction = (TextView) findViewById(R.id.txtprotocol);
        email_id = (EditText) findViewById(R.id.edtemail);
        reset_password = (Button) findViewById(R.id.btnreset);
        context = this;


        instruction.setText("Enter the email address you register with and \n" + "we will send a link to rest your\n" + "password");

        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email_id = email_id.getText().toString();
                if (Email_id.isEmpty()) {
                    email_id.setError("Required");
                } else if (!Util.emailValidator(Email_id)) {

                    email_id.setError("Please fill the valid email id");
                }else {
                    dialog = new ACProgressFlower.Builder(context).build();
                    dialog.show();
                    ModelManager.getInstance().getForgotPasswordmanager().ForgotPasswordmanager(context, Operations.forgotPassword(context,
                            Email_id, user_type));
                    //new ForgotPassowrd().execute(action,Email_id,user_type);
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        bus.getDefault().register(context);
    }

    @Override
    public void onStop() {
        super.onStop();
        bus.getDefault().unregister(context);
    }

    @Subscribe
    public void onEvent(Event event) {

        switch (event.getKey()) {

            case Constants.forgotpassword:
                dialog.dismiss();

                String response = event.getValue();
                String[] responsesplit = response.split(",");
                int id = Integer.parseInt(responsesplit[responsesplit.length - 2]);
                String message = responsesplit[responsesplit.length - 1];
                if (id > 0) {
                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText(message).show();
                    Intent intent = new Intent(Forgot_Password.this, Login_activity.class);
                    startActivity(intent);
                    finish();

                } else {
                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Failed")
                            .setContentText(message).show();
                }
                break;
        }
    }


}


