package com.example.appzaorro.myapplication.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.appzaorro.myapplication.R;
import com.example.appzaorro.myapplication.controller.ModelManager;
import com.example.appzaorro.myapplication.model.Constants;
import com.example.appzaorro.myapplication.model.Event;
import com.example.appzaorro.myapplication.model.Operations;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cc.cloudist.acplibrary.ACProgressFlower;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by vijay on 9/1/17.
 */

public class FeedBack extends AppCompatActivity {
    RatingBar ratingBar;
    EditText edtdescription;
    Button submit;
    float ratingnumber;
    int rartingnumbertotal;
    private EventBus bus = EventBus.getDefault();
    String status = "";
    Context context;
    ACProgressFlower dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_layout);
        intilize();
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                ratingnumber = rating;
                rartingnumbertotal = ratingBar.getNumStars();

            }
        });
        if (ratingnumber < 2.0) {

            status = "Poor";
        } else if (ratingnumber < 3.5 && ratingnumber > 2.0) {

            status = "Good experinnce";
        } else if (ratingnumber < 4.5 && ratingnumber > 3.5) {

            status = "Very Goood Experince";

        } else if (ratingnumber <= 5 && ratingnumber > 4.5) {

            status = "Excellent experince";
        }


    }

    public void intilize() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
        edtdescription = (EditText) findViewById(R.id.edtdescription);
        submit = (Button) findViewById(R.id.btnsubmit);
        context = this;


    }
    public void submitclick(View view) {

        if (edtdescription.getText().toString().isEmpty()) {

            edtdescription.setError("Please give the description");
        } else if (!edtdescription.getText().toString().isEmpty()) {
            Log.e("feedback detail", String.valueOf(ratingnumber) + "\n" + status);

            dialog = new ACProgressFlower.Builder(this).build();

            dialog.show();

            ModelManager.getInstance().getFeedbackManager().FeedbackManager(context, Operations.feedback(context, "51", "32", String.valueOf(ratingnumber), status, "customer"));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(context);
    }

    @Override
    protected void onStart() {
        bus.register(context);
        super.onStart();
    }

    @Subscribe
    public void onEvent(Event event) {
        Log.e("call", "eventbus");

        switch (event.getKey()) {
            case Constants.feedbackstatus:

                dialog.dismiss();
                String message  = event.getValue();
                String[] split = message.split(",");
                int id = Integer.parseInt(split[split.length-2]);
                String messagestatus= split[split.length-1];
                Log.e("feedback",String.valueOf(id)+"\n"+messagestatus);
                if (id>0){
                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Feedback Status")
                            .setContentText(messagestatus)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                    finish();
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();


                }
                else {
                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Feedback Status")
                            .setContentText(message)
                            .show();
                }

        }
    }
}
