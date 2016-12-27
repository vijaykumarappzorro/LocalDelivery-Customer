package com.example.appzaorro.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.CardException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.appzaorro.myapplication.Date_Time.day;



public class Payment_Activity_Method extends AppCompatActivity {
    Toolbar toolbar;
    TextView paytmpayment, creditcardpayment;
    String orderId;
    ProgressDialog progressdialog;
    String tokenid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.method_payment);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Payment method");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        initOrderId();
        progressdialog = new ProgressDialog(Payment_Activity_Method.this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        paytmpayment = (TextView) findViewById(R.id.txtpaytam);
        creditcardpayment = (TextView) findViewById(R.id.txtcredit);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();StrictMode.setThreadPolicy(policy);
        creditcardpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             ViewDialog alert = new ViewDialog();
                alert.showDialog(Payment_Activity_Method.this, "Error de conexión al servidor");
             /*   Intent intent = new Intent(Payment_Activity_Method.this,CardPayment.class);
                startActivity(intent);*/
            }
        });


        paytmpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                PaytmPGService Service = PaytmPGService.getStagingService();
                Map<String, String> paramMap = new HashMap<String, String>();
                //  paramMap.put("REQUEST_TYPE", "DEFAULT");
                paramMap.put("REQUEST_TYPE", "DEFAULT");
                paramMap.put("ORDER_ID", "ORDER12345");
                paramMap.put("MID", "klbGlV59135347348753");
                paramMap.put("CUST_ID", "CUST110");
                paramMap.put("CHANNEL_ID", "WAP");
                paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                paramMap.put("WEBSITE", "paytm");
                paramMap.put("TXN_AMOUNT", "1.0");
                paramMap.put("THEME ", "merchant");

                PaytmOrder Order = new PaytmOrder(paramMap);
//Create new Merchant Object having all merchant configuration.
                PaytmMerchant Merchant = new PaytmMerchant(
                        "http://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
                        "http://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");
//Set PaytmOrder and PaytmMerchant objects. Call this method and set both objects before starting transaction.
                Service.initialize(Order, Merchant, null);

//Start the Payment Transaction. Before starting the transaction ensure that initialize method is called.
                Service.startPaymentTransaction(Payment_Activity_Method.this, true, true, new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        Toast.makeText(Payment_Activity_Method.this, inErrorMessage, Toast.LENGTH_SHORT).show();

                        Log.e("error1",inErrorMessage);
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // This may be due to initialization of views in Payment Gateway Activity or may be due to initialization of webview.
                        // Error Message details the error occurred.
                    }

                    @Override
                    public void onTransactionSuccess(Bundle inResponse) {
                        // After successful transaction this method gets called.
                        // Response bundle contains the merchant response parameters.
                    }

                    @Override
                    public void onTransactionFailure(String inErrorMessage, Bundle inResponse) {
                        Toast.makeText(Payment_Activity_Method.this, inErrorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("error2",inErrorMessage);
                        // This method gets called if transaction failed.
                        // Here in this case transaction is completed, but with a failure.
                        // Error Message describes the reason for failure.
                        // Response bundle contains the merchant response parameters.
                    }

                    @Override
                    public void networkNotAvailable() {
                        // If network is not available, then this method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        Toast.makeText(Payment_Activity_Method.this, inErrorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("error3",inErrorMessage);
                        // This method gets called if client authentication failed.
                        // Failure may be due to following reasons
                        //      1. Server error or downtime.
                        //      2. Server unable to generate checksum or checksum response is
                        //         not in proper format.
                        //      3. Server failed to authenticate that client. That is value of
                        //         payt_STATUS is 2.
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingURL) {
                        Toast.makeText(Payment_Activity_Method.this, inErrorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("error4",inErrorMessage);
                        // This page gets called if some error occurred while loading some URL in Webview.
                        // Error Code and Error Message describes the error.
                        // Failing URL is the URL that failed to load.
                    }

                    @Override
                    public void onBackPressedCancelTransaction() {

                    }
                });
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        orderId = "ORDER" + (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);

    }

    // customer request  dialong box
    public class ViewDialog {

        public void showDialog(final Activity activity, String msg) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.debitcreditlayout);
            Button add = (Button) dialog.findViewById(R.id.pay);
            final EditText edtcard, edtcardname, edtexpirymonth, edtexpiryyear,edtccv;
            edtcard = (EditText) dialog.findViewById(R.id.creditnumber);
            final int month = 0,year=0;


            edtcardname = (EditText) dialog.findViewById(R.id.creditholdername);
            edtexpirymonth = (EditText) dialog.findViewById(R.id.creditexpirydate);
            edtccv =(EditText)dialog.findViewById(R.id.edtccv);


            edtcard.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    Log.e("ontextchange",s.toString());
                    //edtcard.setText(new String(s.toString()));
                    if (count<4){

                        String carnumber= edtcard.getText().toString();
                        if (carnumber.startsWith("4")){
                            Drawable img = getApplicationContext().getResources().getDrawable( R.drawable.ic_icon_visa );
                            edtcard.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);
                            return;
                        }
                        for (int i=50; i<=55; i++) {
                             if (carnumber.startsWith(String.valueOf(i))) {
                                 Drawable img = getApplicationContext().getResources().getDrawable( R.drawable.ic_icon_mastercard );
                                 edtcard.setCompoundDrawablesWithIntrinsicBounds( null,null,img,null );
                                 return;
                             }
                        }
                    }


                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                    //edtcard.setText(new String(s.toString()));
                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.e("afterchange",s.toString());
                    //edtcard.setText(new String(s.toString()));
                }
            });
            edtexpirymonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  Calendar c = Calendar.getInstance();
                     int month = c.get(Calendar.MONTH);
                         int year = c.get(Calendar.YEAR);
                    final DatePickerDialog datePickerDialog = new DatePickerDialog(Payment_Activity_Method.this, new DatePickerDialog.OnDateSetListener(){


                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                            edtexpirymonth.setText(month+1 + "/" +year);
                            month = month+1;
                            year= year;
                        }
                    },year,month,day);
                    datePickerDialog .getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();

                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Card card = new Card(edtcard.getText().toString(), month, year, edtccv.getText().toString());

                    Stripe stripe = null;
                    try {
                       stripe = new Stripe("pk_test_IQeg0qvPdsgedCxSar97p7xR")
                        boolean validation = card.validateCard();
                        if (validation) {

                            stripe.createToken(
                                    card,
                                    new TokenCallback() {
                                        public void onSuccess(Token token) {
                                            // Send token to your server
                                            Log.e("token", token.getId());
                                            tokenid =token.getId();
                                            if (tokenid!=null){
                                                Log.e("tokenid","generated");
                                                dialog.dismiss();
                                                new CreateCharge().execute();
                                            }
                                        }
                                        public void onError(Exception error) {

                                            Log.e("error", String.valueOf(error));
                                        }
                                    }
                            );
                        }
                        else if (!card.validateCard()){

                            edtcardname.setError("Please fill correct card number");

                            Toast.makeText(Payment_Activity_Method.this, "The card number is invalid", Toast.LENGTH_SHORT).show();
                            
                        }
                        else if (!card.validateExpiryDate()){

                         edtexpirymonth.setError("please fill the correct expirymonth");
                            Toast.makeText(Payment_Activity_Method.this, "The Expiry date of card is invalid", Toast.LENGTH_SHORT).show();
                        }
                        else if (!card.validateCVC()){
                            edtccv.setError("Incoorect CCV Code");
                            Toast.makeText(Payment_Activity_Method.this, "The card cvc number is incorrect", Toast.LENGTH_SHORT).show();
                        }

                    } catch (AuthenticationException e) {
                        e.printStackTrace();
                    }

                }

            });

            dialog.show();


        }
    }

    public class CreateCharge extends AsyncTask<String,String,String>{
        Charge charge;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressdialog.setTitle("Loading..");
            progressdialog.setMessage("Please wait..");
            progressdialog.setCancelable(false);
            progressdialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            com.stripe.Stripe.apiKey = "sk_test_7T68y1h1K5wRgCkE90WSyBrR";
            try {
                Map<String, Object> chargeParams = new HashMap<String, Object>();
                chargeParams.put("amount", 1000); // Amount in cents
                chargeParams.put("currency", "usd");
                chargeParams.put("source", tokenid);
                chargeParams.put("description", "Example charge");

              charge = Charge.create(chargeParams);
                Log.e("cahrge",charge.getInvoice().toString());


            } catch (CardException e) {
                // The card has been declined
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("exception",""+e.getMessage());
            }



            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressdialog.dismiss();
            if (charge!=null){
                new SweetAlertDialog(Payment_Activity_Method.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Payment Succeeded")
                        .setContentText("Your Payment has been succeeded")
                        .show();
            }
            else {

                new SweetAlertDialog(Payment_Activity_Method.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Payment Failed")
                        .setContentText("Please try again")
                        .show();
            }
        }
    }


}
