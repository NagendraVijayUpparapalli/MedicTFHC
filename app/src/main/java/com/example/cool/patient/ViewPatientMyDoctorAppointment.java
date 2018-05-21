package com.example.cool.patient;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsibbold.zoomage.ZoomageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ViewPatientMyDoctorAppointment  extends AppCompatActivity {

    TextView appointmentdate, doctorname, Timeslot, patientname, status, reason, amount, comment, payment, phonenumber;
    CheckBox cashonhand, paytm, netbanking, creditcard;
    String date, appdate, dtname, tmslot, paname, statuss, reson, amnt, coment, prescript, paymentmde;
//    PatientAppointmentDetails patient;
    Bitmap mIcon11;
    Button prescription, paymode;
    ImageView imageView;
    ProgressDialog mProgressDialog;
    ZoomageView zoomageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_my_doctor_appointment);

        imageView = (ImageView) findViewById(R.id.doctor_image);
//        patient = new PatientAppointmentDetails();
        doctorname = (TextView) findViewById(R.id.Doctor_name);
        //appointmentdate=(TextView)findViewById(R.id.appointment_date);
        patientname = (TextView) findViewById(R.id.Patient_name);
        Timeslot = (TextView) findViewById(R.id.Time_slot);
        status = (TextView) findViewById(R.id.status);
        reason = (TextView) findViewById(R.id.Reason);
        amount = (TextView) findViewById(R.id.amount);
        comment = (TextView) findViewById(R.id.Doctor_comments);
        payment = (TextView) findViewById(R.id.payment);

        phonenumber = (TextView) findViewById(R.id.phonenum);
        zoomageView=(ZoomageView) findViewById(R.id.myZoomageView);

        prescription = (Button) findViewById(R.id.prescription);
        paymode = (Button) findViewById(R.id.pay_button);

        date = getIntent().getStringExtra("date");

        System.out.println("date in view"+date);

        phonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_no = phonenumber.getText().toString();
                System.out.println("phone number" + phone_no);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:8465887420"));
                if (ActivityCompat.checkSelfPermission(ViewPatientMyDoctorAppointment.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }
        });


        ArrayList<String> doctornames = getIntent().getStringArrayListExtra("doctorname");
        ArrayList<String> dates = getIntent().getStringArrayListExtra("dates");
        ArrayList<String> patientnames = getIntent().getStringArrayListExtra("patientname");
        ArrayList<String> timeslot = getIntent().getStringArrayListExtra("timeslot");
        ArrayList<String> statuslist = getIntent().getStringArrayListExtra("status");
        ArrayList<String> reasonlist = getIntent().getStringArrayListExtra("reason");
        ArrayList<String> commentlist = getIntent().getStringArrayListExtra("comment");
        ArrayList<String> amountlist = getIntent().getStringArrayListExtra("amount");
        ArrayList<String> prescriptions = getIntent().getStringArrayListExtra("prescription");
        ArrayList<String> pamentmode = getIntent().getStringArrayListExtra("payment");



        int pos = dates.indexOf(date);
        doctorname.setText(doctornames.get(pos));
        Timeslot.setText(timeslot.get(pos));
        patientname.setText(patientnames.get(pos));

        if(statuslist.get(pos).equals("Accept"))
        {
            paymode.setBackgroundColor(getResources().getColor(R.color.accept));
        }

        if(statuslist.get(pos).equals("Reject"))
        {
            paymode.setBackgroundColor(getResources().getColor(R.color.reject));
        }

        if(statuslist.get(pos).equals("Initiation Pending"))
        {
            paymode.setBackgroundColor(getResources().getColor(R.color.initiation));
        }

//        if(statuslist.get(pos).equals("Reschedule"))
//        {
//
//        }

        status.setText(statuslist.get(pos));
        reason.setText(reasonlist.get(pos));
        comment.setText(commentlist.get(pos));
        amount.setText(amountlist.get(pos));


        System.out.println("position of date.."+pos);


        if (dates.contains(date)) {

//            for (int i = 0; i < doctornames.size(); i++) {
//                dtname = doctornames.get(i);
//                System.out.println("Doctor Name view" + dtname);
//                doctorname.setText(dtname);
//            }
//            for (int i = 0; i < timeslot.size(); i++) {
//                tmslot = timeslot.get(i);
//                Timeslot.setText(tmslot);
//            }
            //doctorname.setText(dtname);
//            patientname.setText(patientnames.get(pos));
//            Timeslot.setText(tmslot);
//            status.setText(statuss);
//            reason.setText(reson);
//            comment.setText(coment);
//            amount.setText(amnt);

            //payment.setText(paymentmde);

            //new DownloadImage().execute("https://meditfhc.com//dprescription/131674854318175623prescription.jpg");

            //new DownloadImage().execute("http://www.meditfhc.com//dprescription/131698197247397713download (2).jpg");



            prescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    zoomageView.setVisibility(View.VISIBLE);
                    // showimage();
                }
            });
        }
    }

    private void showimage() {


//        Dialog settingsDialog = new Dialog(this);
//        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.image_popup, null));
//        settingsDialog.show();
//        settingsDialog.setCanceledOnTouchOutside(true);
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ViewPatientMyDoctorAppointment.this);
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
            zoomageView.setImageBitmap(result);
            // Close progressdialog
            mProgressDialog.dismiss();
        }

        //////// view
//        private class GetImageTask extends AsyncTask<String, Void, Bitmap> {
//            ImageView bmImage;
//
//            public GetImageTask(ImageView bmImage) {
//                this.bmImage = bmImage;
//            }
//
//            protected Bitmap doInBackground(String... urls) {
//                String urldisplay = urls[0];
//                mIcon11 = null;
//                try {
//                    InputStream in = new java.net.URL(urldisplay).openStream();
//                    mIcon11 = BitmapFactory.decodeStream(in);
//                } catch (Exception e) {
//                    Log.e("Error", e.getMessage());
//                    e.printStackTrace();
//                }
//                return mIcon11;
//            }
//
//            protected void onPostExecute(Bitmap result) {
//                // prescription.setImageBitmap(result);
//            }
//
//        }
    }
}
