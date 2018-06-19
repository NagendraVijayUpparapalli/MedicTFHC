package com.example.cool.patient.common.aboutUs;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.cool.patient.R;

public class AboutUs extends AppCompatActivity {

    ViewPager viewPager;
    AboutUsSwipeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        viewPager=(ViewPager) findViewById(R.id.view_pager);
        adapter=new AboutUsSwipeAdapter(this);
        viewPager.setAdapter(adapter);
    }
}
