package com.example.appzaorro.myapplication.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appzaorro.myapplication.R;
import com.example.appzaorro.myapplication.controller.ModelManager;
import com.example.appzaorro.myapplication.model.Config;
import com.example.appzaorro.myapplication.model.Ldshareadprefernce;
import com.example.appzaorro.myapplication.model.Operations;
import com.example.appzaorro.myapplication.model.Util;
import com.facebook.CallbackManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cc.cloudist.acplibrary.ACProgressFlower;

import static com.example.appzaorro.myapplication.R.mipmap.profilepic;

public class Update_Profile_Activity extends AppCompatActivity {
    ProgressDialog progressDialog;
    ImageView imageView;
    Toolbar toolbar;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    CallbackManager callbackManager;
    MenuItem shareditem;

    Context context;
    String userChoosenTask,covertedImage;
    String firstname,lastname,usertype="customer",user_id,emailid,mobilenumber,placeid="",homeaddress,workaddress;
    EditText edtfirstname,edtlastname,edtemailid,edtmobilenumber;
    TextView txthomelocation,txtworklocation,txtsearchhomelocation;
    Double lat,lng;

    ImageView crossimage,edtimage,delimage;


    ACProgressFlower dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__profile_);
        callbackManager = CallbackManager.Factory.create();
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Setting");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        context = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        initlize();
        Log.e("user detail",firstname +"\n" +lastname + "\n" + usertype +"\n" + user_id);

//  here we can change the profile pic of the customer

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            selectImage();

            }
        });

    }
    public void initlize(){

        imageView = (ImageView)findViewById(R.id.image);


        edtfirstname =(EditText)findViewById(R.id.edtfirstname);
        edtlastname=(EditText)findViewById(R.id.username);
        edtemailid =(EditText)findViewById(R.id.edtemailid);
        edtmobilenumber =(EditText)findViewById(R.id.edtmobilenumber);
        txthomelocation =(TextView)findViewById(R.id.address);
        txtsearchhomelocation=(TextView)findViewById(R.id.txtaddhome);
        txtworklocation=(TextView)findViewById(R.id.txtworkname);
        crossimage =(ImageView)findViewById(R.id.cross);
        edtimage=(ImageView)findViewById(R.id.imgedit);
        delimage =(ImageView)findViewById(R.id.imgdel);
         String fullname = Ldshareadprefernce.readString(context,"FULLNAME");
        if (!fullname.isEmpty()){

            String[] split = fullname.split(" ");
            firstname = split[split.length-2];
            lastname = split[split.length-1];
            edtfirstname.setText(firstname);
            edtlastname.setText(lastname);

        }

        emailid = Ldshareadprefernce.readString(context,"EMAILID");
        mobilenumber =Ldshareadprefernce.readString(context,"MOBILENUMBER");
        edtemailid.setText(emailid);
        edtmobilenumber.setText(mobilenumber);

        disableEdittext();




    }
    public void disableEdittext(){
        edtfirstname.setEnabled(false);
        edtfirstname.setClickable(false);
        edtlastname.setClickable(false);
        edtlastname.setEnabled(false);
        edtmobilenumber.setClickable(false);
        edtmobilenumber.setEnabled(false);
        edtemailid.setClickable(false);
        edtemailid.setEnabled(false);
    }
    public void homelocationClick(View view){

                Intent intent = new Intent(context,Search_Activity.class);
                Config.SearchStatus ="HOMELOCATION";
                startActivity(intent);
    }
    public  void worklocationclick(View view){

        Intent intent = new Intent(context,Search_Activity.class);
        Config.SearchStatus ="WORKLOCATION";
        startActivity(intent);
    }
    public void delhomeAddress(View view){

        txthomelocation.setText("");
        txthomelocation.setVisibility(View.GONE);
        crossimage.setVisibility(View.GONE);

    }
    public void delWorkAddress(View view){

        txtworklocation.setText(" Add Work address");
      ;



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        shareditem = menu.findItem(R.id.tick);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.tick:
                shareditem.setVisible(false);
                dialog = new ACProgressFlower.Builder(context).build();
                dialog.show();
                Ldshareadprefernce.putString(context, "FULLNAME", edtfirstname.getText().toString() + " " + edtlastname.getText().toString());
                Ldshareadprefernce.putString(context, "PROFILEPIC", Config.imagebaseurl + "" + profilepic);

                ModelManager.getInstance().getUpdateProfileManager().UpdateProfileManager(context, Operations.updateprofile(context,
                        Ldshareadprefernce.readString(context,"USERID"),covertedImage, firstname,lastname,usertype));

                break;
            case R.id.edit:
                shareditem.setVisible(true);
                edtfirstname.setEnabled(true);
                edtfirstname.setClickable(true);
                edtlastname.setClickable(true);
                edtlastname.setEnabled(true);
                edtmobilenumber.setClickable(true);
                edtmobilenumber.setEnabled(true);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        placeid = Config.place_id;

            if (Config.SearchStatus.equals("HOMELOCATION")){

                lat = Double.parseDouble(Ldshareadprefernce.readString(context,"home_latitude"));
                lng = Double.parseDouble(Ldshareadprefernce.readString(context,"home_longitude"));

                txthomelocation.setText(Util.getCompleteAddressString(context,lat,lng));
                txthomelocation.setVisibility(View.VISIBLE);
                crossimage.setVisibility(View.VISIBLE);

            }
        else if (Config.SearchStatus.equals("WORKLOCATION")){

                lat = Double.parseDouble(Ldshareadprefernce.readString(context,"work_latitude"));
                lng = Double.parseDouble(Ldshareadprefernce.readString(context,"work_longitude"));
                txtworklocation.setText(Util.getCompleteAddressString(context,lat,lng));
                delimage.setVisibility(View.VISIBLE);

            }


    }




/*

    private void Seraclocation( String placeId){


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {

            StringBuilder sb = new StringBuilder(Config.searchUrl +placeId);
            sb.append("&key=" + Search_Activity.API_KEY);


            URL url = new URL(sb.toString());
            Log.e("new url", String.valueOf(url));

            conn = (HttpURLConnection) url.openConnection();

            InputStreamReader in = new InputStreamReader(conn.getInputStream());


            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];

            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);

            }

        } catch (MalformedURLException e) {

            Log.e("", "Error processing Places API URL", e);


        } catch (IOException e) {

            Log.e("", "Error connecting to Places API", e);



        }finally {

            if (conn != null) {

                conn.disconnect();
            }

        }
        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONObject jsonObject = jsonObj.getJSONObject("result");
            JSONObject jsonObject1 = jsonObject.getJSONObject("geometry");
            JSONObject jsonObject2 = jsonObject1.getJSONObject("location");
            String lati= jsonObject2.getString("lat");
            String lngi = jsonObject2.getString("lng");
            Log.e("latng of home ",lati +" " +lngi);
            double latilong = Double.parseDouble(lati);
            double longititude = Double.parseDouble(lngi);
            // Extract the Place descriptions from the results
            //   LatLng cur_Latlng=new LatLng(latilong,longititude); // giving your marker to zoom to your location area.
            if (Config.SearchStatus.equals("HOMELOCATION")){

                homeaddress = Util.getCompleteAddressString(context,latilong, longititude);
                txthomelocation.setText(homeaddress);
                return;


            }
            else if (Config.SearchStatus.equals("WORKLOCATION")){

                 workaddress=Util.getCompleteAddressString(context,latilong,longititude);
                  txtworklocation.setText(workaddress);

            }

        }catch (JSONException e) {

            Log.e("", "Cannot process JSON results", e);

        }


    }
*/


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
       // new UpdateImage().execute(action,firstname,lastname,covertedImage,usertype);
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
