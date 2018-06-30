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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.R;
import com.jsibbold.zoomage.ZoomageView;

import java.io.InputStream;
import java.util.ArrayList;

public class ViewPatientHistoryInDoctor extends AppCompatActivity {

    TextView Doctorname,speciality,patientname,aadharnumber,mobilenumber,appointmentdate,reason,doctorcomment,close;
    ZoomageView zoomageView;
    Button prescription,ok;
    String date;
    ProgressDialog mProgressDialog;

    Bitmap mIcon11;
    ApiBaseUrl baseUrl;

    ImageView doctorImage;


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
        doctorcomment=(TextView)findViewById(R.id.Doctor_comments);

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
