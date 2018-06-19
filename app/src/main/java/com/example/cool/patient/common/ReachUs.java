package com.example.cool.patient.common;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;

import com.example.cool.patient.R;

import java.util.Locale;

public class ReachUs extends AppCompatActivity {

    ImageView img1,img2,img3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reach_us);
        img1=(ImageView) findViewById(R.id.image1);
        img2=(ImageView) findViewById(R.id.image2);
        img3=(ImageView) findViewById(R.id.image3);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Object longitude=82.2093830;
                Object latitude=17.0011020;
                String uri = String.format(Locale.ENGLISH, "geo:0,0?q=17.0011020,82.2093830(Medic TFHC, Private Limited)", latitude, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
//                Intent intent=new Intent(ReachUs.this,Location1Activity.class);
//                startActivity(intent);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object longitude=83.3072587;
                Object latitude=17.7165192;
                String uri = String.format(Locale.ENGLISH, "geo:0,0?q=17.7165192,83.3072587(Medic TFHC, Private Limited)", latitude, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);

//                Intent intent=new Intent(ReachUs.this,Location2Activity.class);
//                startActivity(intent);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Object longitude=78.4028431;
                Object latitude=17.5002275;
                String uri = String.format(Locale.ENGLISH, "geo:0,0?q=17.5002275,78.4028431(Medic TFHC, Private Limited)", latitude, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);

//                Intent intent=new Intent(ReachUs.this,Location3Activity.class);
//                startActivity(intent);
            }
        });
    }

}