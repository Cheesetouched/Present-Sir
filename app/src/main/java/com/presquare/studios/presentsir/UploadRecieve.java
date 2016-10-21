package com.presquare.studios.presentsir;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class UploadRecieve extends AppCompatActivity implements View.OnClickListener {


    SessionManagement session;

    private static final int CAMERA_REQUEST = 1888;

    public static final String UPLOAD_URL = "http://presentsir.esy.es/upload.php";

    public static final String UPLOAD_KEY = "image";

    public static final String UPLOAD_NUMBER = "mobile";

    private TextView header;

    private String mobile;

    private String random;

    private String capturePath;

    private int PICK_IMAGE_REQUEST = 1;

    private Button buttonCapture;

    private Button buttonUpload;

    private ImageView imageView;

    private Bitmap select;

    private Bitmap capture;

    private Uri filePath;

    private File captureFile;

    private File captureFileTrue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Session Management

        session = new SessionManagement(getApplicationContext());


        // Getting Mobile Number Stored In Session

        HashMap<String, String> user = session.getUserDetails();


        // Storing Mobile Number In A Variable

        mobile = user.get(session.KEY_NAME);

        // Setting Content View

        setContentView(R.layout.activity_upload);


        // Declaring All Variables

        buttonCapture = (Button) findViewById(R.id.buttonCapture);

        buttonUpload = (Button) findViewById(R.id.buttonUpload);

        imageView = (ImageView) findViewById(R.id.student);

        buttonCapture.setOnClickListener(this);

        buttonUpload.setOnClickListener(this);


        // Internet Connection Check

        initialCheck();

    }



    private void initialCheck() {

        if (!isOnline()) {

            Toast.makeText(UploadRecieve.this, "No Internet Connection!", Toast.LENGTH_LONG).show();


        }

    }



    public boolean isOnline() {

        final ConnectivityManager connectivityManager = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));

        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

    }



    private void check() {

        if (select == null && capture==null) {

            showFileChooser();

            Toast.makeText(UploadRecieve.this, "Please Select A Photo", Toast.LENGTH_SHORT).show();

        }

        if (select != null) {

            uploadImage();

        }

        if (capture != null) {

            uploadImageCaptured();

        }

    }



    private void showFileChooser() {

        Intent intent = new Intent();

        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }



    private void capture() {

        captureFile = new File(Environment.getExternalStorageDirectory(), "/Present Sir/");

        captureFileTrue = new File(Environment.getExternalStorageDirectory(), "/Present Sir/dp.jpg");

        if (captureFile.exists()) {

            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(captureFileTrue));

            startActivityForResult(intent, CAMERA_REQUEST);

            buttonCapture.setVisibility(View.GONE);

            buttonUpload.setText("Upload");

        }

        else {

            captureFile.mkdir();

            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(captureFileTrue));

            startActivityForResult(intent, CAMERA_REQUEST);

            buttonCapture.setVisibility(View.GONE);

            buttonUpload.setText("Upload");

        }



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();

            try {

                select = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                buttonCapture.setVisibility(View.GONE);

                imageView.setImageBitmap(select);

            }

            catch (IOException e) {

                e.printStackTrace();

            }

        }


        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            capturePath = Environment.getExternalStorageDirectory() + "/Present Sir/" + "dp.jpg";

            File file = new File(capturePath);

            if (file.exists()) {

                capture = BitmapFactory.decodeFile(capturePath);

                imageView.setImageBitmap(capture);

            }

            else {

                Toast.makeText(UploadRecieve.this, "Try Again!", Toast.LENGTH_LONG).show();

            }

        }

    }



    public String getStringImage(Bitmap bmp){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] imageBytes = baos.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;

    }



    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();

        File myDir = new File(root + "/Present Sir");

        myDir.mkdirs();

        Random generator = new Random();

        int n = 10000;

        n = generator.nextInt(n);

        random = "Image-"+ n +".jpg";

        File file = new File (myDir, random);

        if (file.exists ()) file.delete ();

        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }




    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UploadRecieve.this, "Uploading Image", "Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                if(s.equalsIgnoreCase("Image Uploaded Successfully!")){

                    SaveImage(select);

                    session.UploadSession(random);

                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("First", true).commit();

                    Intent intent = new Intent(UploadRecieve.this, Dashboard.class);

                    finish();

                    startActivity(intent);

                }

                if(s.equalsIgnoreCase("")){

                    Toast.makeText(UploadRecieve.this, "Loading... Try Again!", Toast.LENGTH_LONG);

                }

            }

            @Override
            protected String doInBackground(Bitmap... params) {

                Bitmap bitmap = params[0];

                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);
                data.put(UPLOAD_NUMBER, mobile);

                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }


        }

        UploadImage ui = new UploadImage();
        ui.execute(select);

    }



    private void uploadImageCaptured(){
        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UploadRecieve.this, "Uploading Image", "Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                if(s.equalsIgnoreCase("Image Uploaded Successfully!")){

                    SaveImage(select);

                    session.UploadSession(random);

                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("First", true).commit();

                    Intent intent = new Intent(UploadRecieve.this, Dashboard.class);

                    finish();

                    startActivity(intent);

                }

                if(s.equalsIgnoreCase("")){

                    Toast.makeText(UploadRecieve.this, "Loading... Try Again!", Toast.LENGTH_LONG);

                }

            }

            @Override
            protected String doInBackground(Bitmap... params) {

                Bitmap bitmap = params[0];

                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);
                data.put(UPLOAD_NUMBER, mobile);

                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }


        }

        UploadImage ui = new UploadImage();
        ui.execute(capture);

    }

    @Override
    public void onClick(View v) {

        if(v == buttonUpload){

            Intent go = new Intent(UploadRecieve.this, Dashboard.class);

            finish();

            startActivity(go);

        }

        if(v == buttonCapture){

            capture();

        }

    }

}