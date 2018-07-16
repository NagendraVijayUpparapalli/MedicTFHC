package com.example.cool.patient.doctor.TodaysAppointments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.R;
import com.jsibbold.zoomage.ZoomageView;

import java.io.InputStream;
import java.util.ArrayList;

public class ViewPatientHistoryInDoctor extends AppCompatActivity {

    TextView Doctorname,speciality,patientname,aadharnumber,mobilenumber,appointmentdate,reason,close;
    EditText doctorcomment;
    ZoomageView zoomageView;
    Button prescription,ok;
    String date;
    ProgressDialog mProgressDialog;

    Bitmap mIcon11;
    ApiBaseUrl baseUrl;

    ImageView doctorImage;

    String myId,docMobile,docId,myDoctorname,docspeciality,myname,myaadharnumber,mymobilenumber,myreason,mydoctorcomment,
            myPrescriptUrl;

    LinearLayout layout;
    ImageView imageView2;
    Button ok1;
    TextView close1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_history_in_doctor);

        baseUrl = new ApiBaseUrl();

        Doctorname=(TextView) findViewById(R.id.Doctor_name);
        speciality=(TextView) findViewById(R.id.speciality);
        patientname=(TextView) findViewById(R.id.Patient_name);
        aadharnumber=(TextView) findViewById(R.id.Aadharnumber);
        mobilenumber=(TextView)findViewById(R.id.mobileNumber);
        appointmentdate=(TextView)findViewById(R.id.appointmentdate);
        reason=(TextView) findViewById(R.id.Reason);
        doctorcomment = (EditText) findViewById(R.id.Doctor_comments);

        prescription=(Button)findViewById(R.id.prescription);
        ok=(Button)findViewById(R.id.ok);

        zoomageView=(ZoomageView)findViewById(R.id.myZoomageView);

        doctorImage = (ImageView) findViewById(R.id.doctor_image);

        close=(TextView) findViewById(R.id.close);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewPatientHistoryInDoctor.this, PatientHistoryInDoctor.class);
                i.putExtra("patientId", getIntent().getStringExtra("patientId"));
                i.putExtra("doctorImageUrl",getIntent().getStringExtra("doctorImageUrl"));
                i.putExtra("doctorMobile",getIntent().getStringExtra("doctorMobile"));
                i.putExtra("id",getIntent().getStringExtra("id"));
                startActivity(i);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("close");
                Intent intent=new Intent(ViewPatientHistoryInDoctor.this,PatientHistoryInDoctor.class);
                intent.putExtra("patientId", getIntent().getStringExtra("patientId"));
                intent.putExtra("doctorImageUrl",getIntent().getStringExtra("doctorImageUrl"));
                intent.putExtra("doctorMobile",getIntent().getStringExtra("doctorMobile"));
                intent.putExtra("id",getIntent().getStringExtra("id"));
                startActivity(intent);
            }
        });


        final Toolbar toolbar=(Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        //  collapsingToolbarLayout.setTitle("My Toolbar");
        collapsingToolbarLayout.setTitleEnabled(false);

//        prescription.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(myprescription.equals(""))
//                {
//                    showalert();
//                }
//                else
//                {
//                    zoomageView.setVisibility(View.VISIBLE);
//                    new DownloadPrescription().execute(prescript);
//                }
//
//                // showimage();
//            }
//        });


        new GetProfileImageTask(doctorImage).execute(baseUrl.getImageUrl()+getIntent().getStringExtra("doctorImageUrl"));

        date=getIntent().getStringExtra("date");

        myaadharnumber=getIntent().getStringExtra("aadharnumber");
        myname=getIntent().getStringExtra("patientname");
        mymobilenumber=getIntent().getStringExtra("mobilenum");
        myreason=getIntent().getStringExtra("reason");
        myDoctorname=getIntent().getStringExtra("doctorName");
        docspeciality=getIntent().getStringExtra("speciality");
        mydoctorcomment = getIntent().getStringExtra("doctorcomment");
        myPrescriptUrl = getIntent().getStringExtra("prescriptUrl");

        if(myDoctorname.equals(""))
        {
            Doctorname.setText("Not Available");
        }
        else
        {
            Doctorname.setText(myDoctorname);
        }

        if(myreason.equals(""))
        {
            reason.setText("Not Available");
        }
        else
        {
            reason.setText(myreason);
        }

        if(myaadharnumber.equals(""))
        {
            aadharnumber.setText("Not Available");
        }
        else
        {

            aadharnumber.setText(myaadharnumber);
        }

        if(mymobilenumber.equals(""))
        {
            mobilenumber.setText("Not Available");
        }
        else
        {
            mobilenumber.setText(mymobilenumber);

        }

        if(mydoctorcomment.equals(""))
        {
            doctorcomment.setText("Not Available");
        }
        else
        {

            doctorcomment.setText(mydoctorcomment);
        }

        if(myname.equals(""))
        {
            patientname.setText("Not Available");
        }

        else
        {
            patientname.setText(myname);
        }

        if(docspeciality.equals(""))
        {
            speciality.setText("Not Available");
        }
        else
        {
            speciality.setText(docspeciality);
        }

        close1 = (TextView) findViewById(R.id.close1);
        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.GONE);
            }
        });

        ok1 = (Button) findViewById(R.id.ok1);
        layout=(LinearLayout) findViewById(R.id.layout1);
        ok1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.GONE);
            }
        });

        prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myPrescriptUrl.equals(""))
                {
                    showalert();
                }
                else
                {
                    layout.setVisibility(View.VISIBLE);
                    new DownloadPrescription().execute(baseUrl.getImageUrl()+myPrescriptUrl);
                }

            }
        });


//        if(dates.get(pos).equals(""))
//        {
//            appointmentdate.setText("Not Available");
//        }
//        else
//        {
//            appointmentdate.setText(dates.get(pos));
//        }

    }

    private class GetProfileImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public GetProfileImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            doctorImage.setImageBitmap(result);
        }

    }


    private void showalert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewPatientHistoryInDoctor.this);
        builder.setMessage("You don't have prescription");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }





    private class DownloadPrescription extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ViewPatientHistoryInDoctor.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Download Image Tutorial");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");

            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            // zoomageView.setImageBitmap(result);
            // Close progressdialog
            mProgressDialog.dismiss();
        }
    }


}
