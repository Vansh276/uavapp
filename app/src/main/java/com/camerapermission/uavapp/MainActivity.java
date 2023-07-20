package com.camerapermission.uavapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.uavapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION_CODE = 1;

    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);
    }

    public void captureImage(View view) {
        //Check for permission at runtime

        if (ContextCompat.checkselfPermission(context:this, Mainfest.permission.CAMERA)
        !=PackageManager.PERMISSION_GRANTED)
        {
            //request camera permission if not granted
            ActivityCompat.requestPermissions(activity:this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CAMERA_PERMISSION_CODE);
            return;


        }

        //open the camera app
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE && requestCode == RESULT_OK){
            //GET the capture image bitmap
            Bundle extras = data.getExtras();
            Bitmap imagebitmap = (Bitmap) extras.get("data");

            imageView.setImageBitmap(imageBitmap);
            //save image to gallery

            saveImageToGallery(imagebitmap);

        }

    }



    private void saveImageToGallery(Bitmap imageBitmap){
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);



        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm ss",Local.getDefault())
                .format(new Date());
        String fileName ="IMG_" + timeStamp + ".jpg";
        File imageFile = new File(storageDir,fileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,quality: 100, outputStream);
            outputStream.flush();
            outputStream.close();

            Intent mediaScanIntent = new  Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(Uri.fromFile(imageFile));
            sendBroadcast(mediaScanIntent);
            Toast .makeText(context:this,resid:"Image Saved Successfully",Toast.LENGTH_LONG).show();


        }catch(Exception e){

        }
    }
}

















