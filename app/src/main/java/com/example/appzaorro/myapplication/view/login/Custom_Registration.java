package com.example.appzaorro.myapplication.view.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appzaorro.myapplication.R;
import com.example.appzaorro.myapplication.controller.ModelManager;
import com.example.appzaorro.myapplication.model.Config;
import com.example.appzaorro.myapplication.model.Constants;
import com.example.appzaorro.myapplication.model.Event;
import com.example.appzaorro.myapplication.model.Operations;
import com.example.appzaorro.myapplication.view.Utility;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

import static com.example.appzaorro.myapplication.model.Config.device_token;
import static com.example.appzaorro.myapplication.model.Config.device_type;
import static com.example.appzaorro.myapplication.model.Config.user_type;


public class Custom_Registration extends AppCompatActivity {
    Toolbar toolbar;
    TextInputLayout edtFirstname, edtLastname, edtMail, edtPassword, edtMobile;
    EditText frstname,lastname,emailid, password,mobile;
    String strFirstname, strLastname, strEmail, strPassword, strMbile, covertedImage;
    TextView register;
    ImageView facebook_login;
    Context context;

    //loginstatus=1 for facbooklogin and  loginstatus = 0 for simple login
    String LoginStatus="0";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ProgressDialog progressDialog;
    String response,loginstatus;
    String userChoosenTask;
    ImageView mImage;
    Bitmap bitmapimage;
    CallbackManager callbackManager;
    android.app.AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_custom__registration);
        context= this;

        Intilizingwidght();


        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                hideKeybord(v);
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeybord(v);
                submitForm();

            }
        });

    }



    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private boolean validateFirstname() {
        if (frstname.getText().toString().trim().isEmpty()) {
            edtFirstname.setError("Required");
            requestFocus(frstname);
            return false;
        } else {
            edtFirstname.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateLastname() {
        if (lastname.getText().toString().trim().isEmpty()) {
            edtLastname.setError("Required");
            requestFocus(lastname);
            return false;
        } else {
            edtLastname.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatEmailid() {
        if (emailid.getText().toString().trim().isEmpty()) {
            edtMail.setError("Required");
            requestFocus(frstname);
            return false;
        }
        else if (!emailValidator(emailid.getText().toString().trim())){

            edtMail.setError("Please fill the valid email id");
            requestFocus(emailid);
            return false;

        }
        else {

            edtMail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateMobile() {
        if (mobile.getText().toString().trim().isEmpty()) {
            edtMobile.setError("Required");
            requestFocus(mobile);
            return false;
        } else {
            edtMobile.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (password.getText().toString().trim().isEmpty()) {
            edtPassword.setError("Required");
            requestFocus(password);
            return false;
        } else {
            edtPassword.setErrorEnabled(false);
        }

        return true;
    }
    private void submitForm() {
        if (!validateFirstname()) {
            return;
        }

        if (!validateLastname()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        if (!validateMobile()) {
            return;
        }
        if (!validatEmailid()) {
            return;
        }

        strFirstname = edtFirstname.getEditText().getText().toString().trim();
        strLastname = edtLastname.getEditText().getText().toString();
        strEmail = edtMail.getEditText().getText().toString().trim();
        strPassword = edtPassword.getEditText().getText().toString();
        strMbile = edtMobile.getEditText().getText().toString();

        dialog = new SpotsDialog(context);
        dialog.show();

        ModelManager.getInstance().getRegisterManager().RegisterManager(context,Config.custom_register_url, Operations.registerUser(context,strEmail,strFirstname,
                strLastname,strPassword,strMbile, device_token, device_type, user_type,covertedImage));

      //  new RegistrationTask().execute(Register, strEmail, strFirstname, strLastname, strPassword, strMbile, device_token, device_type, user_type, "32.112342", "76.4523442", covertedImage);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);

            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    // validate the emial id  te email id should be fully domain
    public static boolean emailValidator(final String mailAddress) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mailAddress);
        return matcher.matches();
    }

    // covert the image into base64 for post the to server

    public  void convertImageToBase64() {
        Bitmap bit = BitmapFactory.decodeResource(getResources(),
                R.mipmap.custom);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte[] ba = bao.toByteArray();
        covertedImage = Base64.encodeToString(ba, 0);
        Log.e("converted Image", "" + covertedImage);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
/* select image from gallery and capture from camera..*/
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Custom_Registration.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(Custom_Registration.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mImage.setImageBitmap(thumbnail);
        byte[] byteArray = bytes.toByteArray();
        covertedImage = Base64.encodeToString(byteArray, 0);
        Log.e("camera images",covertedImage);


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byte[] byteArray = bytes.toByteArray();
                covertedImage = Base64.encodeToString(byteArray, 0);
                mImage.setImageBitmap(bm);
                Log.e("From gallery",covertedImage);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {


        }
        //convertImageToBase64(bm);
    }
/* hide the keyboard from the UI ..*/
    public void hideKeybord(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }


    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(context);

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(context);

    }

    @Subscribe
    public void onEvent(Event event) {

        switch (event.getKey()) {

            case Constants.REGISTER:
                dialog.dismiss();
                Config.event_meesage =event.getValue();
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(Config.event_meesage)
                        .setContentText("Please verify your email id")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent intent = new Intent(context,Login_activity.class);
                                startActivity(intent);
                                finish();
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

                break;

            case Constants.NOT_REGISTER:
                dialog.dismiss();
                Config.event_meesage = event.getValue();
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Failed")
                        .setContentText(Config.event_meesage).show();
                break;

            case Constants.SERVER_ERROR:
                dialog.dismiss();
                Toast.makeText(context, "Please check your intenet cconnection", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // create Aysntask class for Registration of the User

  /* public  class RegistrationTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Loading..");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
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
                String id = jsonObject1.getString("id");
                Ldshareadprefernce.putString(Custom_Registration.this,"USERID",id);
                //editor.putString("USERID",id).apply();
                int user_id =Integer.parseInt(id);
                Log.e("getting response", "" + result);
                if (user_id>=1) {

                    Ldshareadprefernce.putString(Custom_Registration.this,"LOGINSTATUS",LoginStatus);
                    Toast.makeText(Custom_Registration.this,"Message "+result,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Custom_Registration.this, Login_activity.class);
                    startActivity(intent);
                } else {
                    String message = jsonObject.getString("message");
                    Toast.makeText(Custom_Registration.this, "result "+message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(Custom_Registration.this, Driver_Registration_Form.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


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
              case R.id.edt_firstname:
                  validateFirstname();
                  break;
              case R.id.edt_lastname:
                  validateLastname();
                  break;

              case R.id.edt_mail:
                  validatEmailid();
                  break;
              case R.id.edt_mobile:
                  validateMobile();
                  break;
              case R.id.edt_password:
                  validatePassword();
                  break;
          }
      }
  }
/* all the widght are define here */
    public void Intilizingwidght(){

        callbackManager = CallbackManager.Factory.create();

        register = (TextView) findViewById(R.id.btnregister);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Customer Register Form");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        edtFirstname = (TextInputLayout) findViewById(R.id.firstname);
        edtLastname = (TextInputLayout) findViewById(R.id.lastname);
        edtMail = (TextInputLayout) findViewById(R.id.email);

        edtPassword = (TextInputLayout) findViewById(R.id.password);
        edtMobile = (TextInputLayout) findViewById(R.id.mobile);
        frstname =(EditText)findViewById(R.id.edt_firstname);
        lastname =(EditText)findViewById(R.id.edt_lastname);
        emailid =(EditText)findViewById(R.id.edt_mail);
        mobile=(EditText)findViewById(R.id.edt_mobile);
        password =(EditText)findViewById(R.id.edt_password);
        frstname.addTextChangedListener(new MyTextWatcher(frstname));
        lastname.addTextChangedListener(new MyTextWatcher(lastname));
        emailid.addTextChangedListener(new MyTextWatcher(emailid));
        mobile.addTextChangedListener(new MyTextWatcher(mobile));
        password.addTextChangedListener(new MyTextWatcher(password));

        bitmapimage = BitmapFactory.decodeResource(getResources(),
                R.mipmap.custom);
        convertImageToBase64();

        // set circle bitmap
        mImage = (ImageView) findViewById(R.id.customer_image);
        mImage.setImageBitmap(bitmapimage);
        device_token = FirebaseInstanceId.getInstance().getToken();
        Log.e("tokenid",""+ device_token);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
