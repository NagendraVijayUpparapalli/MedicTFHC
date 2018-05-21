package com.example.cool.patient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.jsibbold.zoomage.ZoomageView;

import java.util.ArrayList;

public class ViewPatientHistoryInDoctor extends AppCompatActivity {

    TextView Doctorname,speciality,patientname,aadharnumber,mobilenumber,appointmentdate,reason,doctorcomment;
    ZoomageView zoomageView;
    Button prescription;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_history_in_doctor);
        Doctorname=(TextView) findViewById(R.id.Doctor_name);
        speciality=(TextView) findViewById(R.id.speciality);
        patientname=(TextView) findViewById(R.id.Patient_name);
        aadharnumber=(TextView) findViewById(R.id.Aadharnumber);
        mobilenumber=(TextView)findViewById(R.id.mobileNumber);
        appointmentdate=(TextView)findViewById(R.id.appointmentdate);
        reason=(TextView) findViewById(R.id.Reason);
        doctorcomment=(TextView)findViewById(R.id.Doctor_comments);

        prescription=(Button)findViewById(R.id.prescription);

        zoomageView=(ZoomageView)findViewById(R.id.myZoomageView);

        date=getIntent().getStringExtra("date");

        ArrayList<String> aadharnumbers = getIntent().getStringArrayListExtra("aadharnumber");
        ArrayList<String> patientnames = getIntent().getStringArrayListExtra("patientname");
        ArrayList<String> mobilenumbers = getIntent().getStringArrayListExtra("mobilenumbers");
        ArrayList<String> reasonlist = getIntent().getStringArrayListExtra("reason");
        ArrayList<String> doctorname = getIntent().getStringArrayListExtra("doctorname");
        ArrayList<String> specialitylist = getIntent().getStringArrayListExtra("speciality");
        ArrayList<String> dates = getIntent().getStringArrayListExtra("dates");
        ArrayList<String> comment = getIntent().getStringArrayListExtra("comment");

        int pos = dates.indexOf(date);
        System.out.println("position"+pos);
        Doctorname.setText(doctorname.get(pos));
        speciality.setText(specialitylist.get(pos));
        patientname.setText(patientnames.get(pos));
        aadharnumber.setText(aadharnumbers.get(pos));
        mobilenumber.setText(mobilenumbers.get(pos));
        appointmentdate.setText(dates.get(pos));
        reason.setText(reasonlist.get(pos));
        doctorcomment.setText(comment.get(pos));


    }
}
