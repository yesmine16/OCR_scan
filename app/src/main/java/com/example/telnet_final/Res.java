package com.example.telnet_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Res extends AppCompatActivity {

    TextView text4, text3, text1,text5;
    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);
        text1 = (TextView) Res.this.findViewById(R.id.text1);
        text4 = (TextView) Res.this.findViewById(R.id.text3);
        text3 = (TextView) Res.this.findViewById(R.id.text4);
        text5 = (TextView) Res.this.findViewById(R.id.text5);
        table =(TableLayout) Res.this.findViewById(R.id.table);

        Intent intent = getIntent();
        ActivityCompat.requestPermissions(Res.this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Db con = new Db();
        try {

            PreparedStatement ps = con.ConnectionDb().prepareStatement("select * from ressources where UPPER(name)  LIKE '%' || ? || '%'");
            ps.setString(1, intent.getStringExtra("id"));
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {
                table.setVisibility(View.VISIBLE);
                text1.setText(rs.getString(1));
                text3.setText(rs.getString(2));
                text4.setText(rs.getString(3));
                text5.setText(rs.getString(9));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void start(View view) throws SQLException, ClassNotFoundException {
        ActivityCompat.requestPermissions(Res.this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Db con = new Db();
        PreparedStatement ps = con.ConnectionDb().prepareStatement("select * from ressources where internal_pn='10'");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            //txt1.setText(rs.getString(2));

        }

    }

    public void img(ImageView imgView, ResultSet rs) throws Exception {
//
//        InputStream is = rs.getBinaryStream("image");
//        OutputStream os = new FileOutputStream(new File("photo.jpg"));
//        byte[] content = new byte[2048];
//        int size = 0;
//        while ((size = is.read(content)) != -1) {
//            os.write(content, 0, size);
//        }
        InputStream is = rs.getBinaryStream("image");
        OutputStream os = new FileOutputStream(new File("@drawable/photo.jpg"));
        byte[] content = new byte[2048];
        int size = 0;
        while ((size = is.read(content)) != -1) {
            os.write(content, 0, size);
        }
        //byte[] blob=rs.getBytes("image");
        // Bitmap bmp=BitmapFactory.decodeByteArray(blob,0,blob.length);
        //imgView.setImageResource(R.drawable.photo.jpg);
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();

    }


}