package com.example.appzaorro.myapplication.com.login.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.appzaorro.myapplication.R;

public class Checked_email extends AppCompatActivity {
    TextView message,instruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked_mail);
        message =(TextView)findViewById(R.id.txtview1);
        instruction =(TextView)findViewById(R.id.txtinstruction);
        instruction.setText("We just emailed you with the instruction to\n" +"reset your password");
    }
}
