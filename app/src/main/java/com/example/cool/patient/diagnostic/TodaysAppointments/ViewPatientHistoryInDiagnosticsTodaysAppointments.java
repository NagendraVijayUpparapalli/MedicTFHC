package com.example.cool.patient.diagnostic.TodaysAppointments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.example.cool.patient.R;

public class ViewPatientHistoryInDiagnosticsTodaysAppointments extends AppCompatActivity {

    TextView centername,patientname,aadharnumber,mobilenumber,comments,testname;
//    Button button;

    String centname,patname,aadharnum,mobilenum,coments,tstname,patientid;
    String diagId,diagMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_history_in_diagnostics_todays_appointments);

        centername=(TextView)findViewById(R.id.Center_name);
        patientname=(TextView)findViewById(R.id.Patient_name);
        aadharnumber=(TextView)findViewById(R.id.Aadharnumber);
        mobilenumber=(TextView)findViewById(R.id.phonenum);
        comments=(TextView)findViewById(R.id.comments);
        testname=(TextView)findViewById(R.id.testname);
//        button = (Button)findViewById(R.id.okbutton);


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

        final RippleView rippleView = (RippleView) findViewById(R.id.rippleView);

        rippleView.setOnClickListener(new View.OnClickListener() {
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


        //phone call
        mobilenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phn = mobilenumber.getText().toString();

                System.out.println("phone number in view patient history in diag..."+phn);

                Intent callintent = new Intent(Intent.ACTION_CALL);
                callintent.setData(Uri.parse("tel:"+phn));
                if (ActivityCompat.checkSelfPermission(ViewPatientHistoryInDiagnosticsTodaysAppointments.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callintent);
            }
        });


    }
}
