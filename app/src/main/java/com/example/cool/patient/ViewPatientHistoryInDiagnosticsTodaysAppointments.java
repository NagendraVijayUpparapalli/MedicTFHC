package com.example.cool.patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewPatientHistoryInDiagnosticsTodaysAppointments extends AppCompatActivity {

    TextView centername,patientname,aadharnumber,mobilenumber,comments,testname;
    Button button;

    String centname,patname,aadharnum,mobilenum,coments,tstname,patientid;
    String diagId,diagMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_history_in_diagnostics_todays_appointments);

        centername=(TextView)findViewById(R.id.Center_name);
        patientname=(TextView)findViewById(R.id.Patient_name);
        aadharnumber=(TextView)findViewById(R.id.Aadharnumber);
        mobilenumber=(TextView)findViewById(R.id.mobilenumber);
        comments=(TextView)findViewById(R.id.comments);
        testname=(TextView)findViewById(R.id.testname);
        button = (Button)findViewById(R.id.okbutton);

        diagId = getIntent().getStringExtra("diagId");
        diagMobile = getIntent().getStringExtra("diagMobile");
        patientid = getIntent().getStringExtra("patientid");
        centname=getIntent().getStringExtra("centername");
        patname=getIntent().getStringExtra("patientname");
        aadharnum=getIntent().getStringExtra("aadharnumber");
        mobilenum=getIntent().getStringExtra("mobilenum");
        coments=getIntent().getStringExtra("comments");
        tstname=getIntent().getStringExtra("testname");

        System.out.println("patient id in view history.."+patientid);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewPatientHistoryInDiagnosticsTodaysAppointments.this,MainPatientHistoryInDiagnostics.class);
                intent.putExtra("patientid",patientid);
                intent.putExtra("diagId",diagId);
                intent.putExtra("diagMobile",diagMobile);
                startActivity(intent);
            }
        });

        centername.setText(centname);
        patientname.setText(patname);
        aadharnumber.setText(aadharnum);
        mobilenumber.setText(mobilenum);
        comments.setText(coments);
        testname.setText(tstname);
    }
}
