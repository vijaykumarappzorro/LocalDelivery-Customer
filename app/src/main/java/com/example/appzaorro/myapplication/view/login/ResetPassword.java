package com.example.appzaorro.myapplication.view.login;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.appzaorro.myapplication.R;

public class ResetPassword extends AppCompatActivity {
    EditText newpassword,confirmPassword;
    TextView instruction;
    Button save,cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        newpassword =(EditText)findViewById(R.id.edtnewpassword);
        confirmPassword =(EditText)findViewById(R.id.edtconfirmpassword);
        instruction =(TextView)findViewById(R.id.txtinstruction);
        save =(Button) findViewById(R.id.btnsave);
        cancel =(Button)findViewById(R.id.btncancel);
        instruction.setText("What would yu like your\n" +"new password to be ?");

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("onclick","on cancel");
                save.setBackgroundResource(R.drawable.border_color_blue);
                save.setTextColor(Color.parseColor("#40bfff"));
                cancel.setTextColor(Color.parseColor("#ffffff"));
                cancel.setBackgroundResource(R.drawable.rounded_shape);

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onclick","on Save");
                cancel.setBackgroundResource(R.drawable.border_color_blue);
                save.setBackgroundResource(R.drawable.rounded_shape);
                cancel.setTextColor(Color.parseColor("#40bfff"));
                save.setTextColor(Color.parseColor("#ffffff"));
                Intent intent = new Intent(ResetPassword.this,Checked_email.class);
                startActivity(intent);
            }
        });


    }
}
