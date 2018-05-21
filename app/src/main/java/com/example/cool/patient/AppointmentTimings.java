package com.example.cool.patient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ListView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class AppointmentTimings extends AppCompatActivity {

    ListView listView;
    ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_timings);
        listView = (ListView) findViewById(R.id.listview);
        viewGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.timings_header,listView,false);
        listView.addHeaderView(viewGroup);
        String[] items = getResources().getStringArray(R.array.list);
        TimingAdapter listAdapter = new TimingAdapter(this,R.layout.test,R.id.earlymorning,items);
        listView.setAdapter(listAdapter);
    }
}

