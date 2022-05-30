package com.example.telnet_final;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MainActivity extends AppCompatActivity {
    Button capture;
    Bitmap bitmap;
    ImageView img;
    private static final int REQUEST_CAMERA_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        capture = (Button) MainActivity.this.findViewById(R.id.capture);
        img = (ImageView) MainActivity.this.findViewById(R.id.img);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(MainActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult res = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri res_uri = res.getUri();

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Db con = new Db();

                try {

                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), res_uri);
                    try {

                        PreparedStatement ps = con.ConnectionDb().prepareStatement("select * from ressources where UPPER(name) LIKE '%' || ? || '%'");
                        ps.setString(1,get_text(bitmap).toUpperCase());

                        ResultSet rs = ps.executeQuery();
                        if(rs.next()){
                            Toast.makeText(MainActivity.this, get_text(bitmap), Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(MainActivity.this, Res.class);
                            in.putExtra("id", get_text(bitmap).toUpperCase());
                            startActivity(in);
                        }else    Toast.makeText(MainActivity.this,"Not Found", Toast.LENGTH_SHORT).show();



                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    String id = null;

    private String get_text(Bitmap bitmap) {
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if (!recognizer.isOperational()) {
            Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < textBlockSparseArray.size(); i++) {
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            id = stringBuilder.toString().trim();
        }
        return id;
    }












}