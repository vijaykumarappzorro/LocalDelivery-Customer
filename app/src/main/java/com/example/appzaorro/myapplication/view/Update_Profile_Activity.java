package com.example.appzaorro.myapplication.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appzaorro.myapplication.R;
import com.example.appzaorro.myapplication.model.Config;
import com.facebook.CallbackManager;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Update_Profile_Activity extends AppCompatActivity {
    ProgressDialog progressDialog;
    ImageView imageView;
    Toolbar toolbar;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    CallbackManager callbackManager;
    SharedPreferences sharedPreferences;
    String userChoosenTask,covertedImage,response;
    String firstname,lastname,usertype="customer",action="update_profile",user_id;
    Button update_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__profile_);
        callbackManager = CallbackManager.Factory.create();
        progressDialog = new ProgressDialog(Update_Profile_Activity.this);
        imageView = (ImageView)findViewById(R.id.image);
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Setting");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        update_profile = (Button)findViewById(R.id.btnupdateprofile);
        sharedPreferences = getSharedPreferences("CUSTOMER_DETAIL", Context.MODE_PRIVATE);

        Log.e("user detail",firstname +"\n" +lastname + "\n" + usertype +"\n" + user_id);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            selectImage();

            }
        });
        Spinner s = ( Spinner )findViewById( R.id.sppiner );  // id of your spinner
        Countr_code cc = new Countr_code( this );
        s.setAdapter( cc );

        TelephonyManager man = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        int index = Countr_code.getIndex( man.getSimCountryIso() );
        if( index > -1 )
        {
            s.setSelection( index );
        }

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new UpdateImage().execute(action,user_id,firstname,lastname,covertedImage,usertype);
            }
        });
    }


 /*.....................................update the profile pic of customer ..........................*/
  public class UpdateImage extends AsyncTask<String,String,String>{

      @Override
      protected void onPreExecute() {
          super.onPreExecute();
          progressDialog.setTitle("Please wait..");
          progressDialog.setMessage("Upload photo");
          progressDialog.setCancelable(false);
          progressDialog.show();
      }
      @Override
      protected String doInBackground(String... params) {
          DefaultHttpClient hc = new DefaultHttpClient();
          ResponseHandler<String> res = new BasicResponseHandler();
          HttpPost postMethod = new HttpPost(Config.update_profile_url);
          List<NameValuePair> nameValuePairs = new ArrayList<>();
          nameValuePairs.add(new BasicNameValuePair("action", params[0]));
          nameValuePairs.add(new BasicNameValuePair("user_id", params[1]));
          nameValuePairs.add(new BasicNameValuePair("firstname", params[2]));
          nameValuePairs.add(new BasicNameValuePair("lastname", params[3]));
          nameValuePairs.add(new BasicNameValuePair("profile_pic", params[4]));
          nameValuePairs.add(new BasicNameValuePair("user_type", params[5]));
          try {
              postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
              response = hc.execute(postMethod, res);
              Log.e("Update response: ", "" + response);
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
              int user_id =Integer.parseInt(id);
              Log.e("user id update ",id);
              Log.e("getting response", "" + result);
              if (user_id>=1) {
                  Toast.makeText(Update_Profile_Activity.this,"Message "+result,Toast.LENGTH_LONG).show();
              } else {
                  String message = jsonObject.getString("message");
                  Toast.makeText(Update_Profile_Activity.this, "result "+message, Toast.LENGTH_SHORT).show();
              }

          } catch (JSONException ex) {
              ex.printStackTrace();
          }
      }
  }
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(Update_Profile_Activity.this,HomePage_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
 //   create a circle image

    public Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        bitmap.recycle();
        return output;
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

        AlertDialog.Builder builder = new AlertDialog.Builder(Update_Profile_Activity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(Update_Profile_Activity.this);

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
        thumbnail.compress(Bitmap.CompressFormat.PNG, 90, bytes);

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
        imageView.setImageBitmap(getCircleBitmap(thumbnail));
        byte[] byteArray = bytes.toByteArray();

        covertedImage = Base64.encodeToString(byteArray, 0);
        new UpdateImage().execute(action,firstname,lastname,covertedImage,usertype);
        Log.e("camera images",covertedImage);


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 90, bytes);
                byte[] byteArray = bytes.toByteArray();
                covertedImage = Base64.encodeToString(byteArray, 0);
                imageView.setImageBitmap(getCircleBitmap(bm));
                Log.e("From gallery",covertedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {

        }

    }

}
