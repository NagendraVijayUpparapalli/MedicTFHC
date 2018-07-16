package com.example.cool.patient.patient.MyDoctorAppointments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.patient.PatientDashBoard;
import com.example.cool.patient.patient.ViewDoctorsList.GetCurrentDoctorsList;
import com.example.cool.patient.R;
import com.jsibbold.zoomage.ZoomageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ViewPatientMyDoctorAppointment  extends AppCompatActivity {

    TextView navigation,doctorspeciality,appointmentdate, doctorname, Timeslot, patientname, status,
            reason, amount, comment, payment, phonenumber,cancel,reschedule,close;

    Bitmap mIcon11;
    Button prescription, paymode,backButton;
    ImageView imageView;

    ApiBaseUrl baseUrl;

    String  myAppointmentdate,appointmentId,userId,mobileNumber,myappointmentId,mydoctorID,mydoctorname, myappointmentdate, mypatientname, mytimeslot, mystatus, myreason, mycomment,
            myamount, myprescription, mypaymentmode;

    ProgressDialog mProgressDialog;
    String doctorLongitude,doctorLatitude,doctorAddress,doctorHospitalName;
    ZoomageView zoomageView;
    String mydoctorImage,mydoctormobile,mydctorspeciality,mydoctorqualification;
    LinearLayout layout;
    ImageView imageView2;
    Button ok;
    TextView close1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_my_doctor_appointment);

        baseUrl = new ApiBaseUrl();

        imageView = (ImageView) findViewById(R.id.doctor_image);
//        patient = new PatientAppointmentDetails();
        doctorname = (TextView) findViewById(R.id.Doctor_name);
        navigation = (TextView) findViewById(R.id.navigation);
        doctorspeciality=(TextView)findViewById(R.id.speciality);
        //appointmentdate=(TextView)findViewById(R.id.appointment_date);
        patientname = (TextView) findViewById(R.id.Patient_name);
        Timeslot = (TextView) findViewById(R.id.Time_slot);
        status = (TextView) findViewById(R.id.status);
        reason = (TextView) findViewById(R.id.Reason);
        amount = (TextView) findViewById(R.id.amount);
        comment = (TextView) findViewById(R.id.Doctor_comments);
        payment = (TextView) findViewById(R.id.payment);
        cancel = (TextView) findViewById(R.id.cancel);
        reschedule = (TextView) findViewById(R.id.Reschedule);


        phonenumber = (TextView) findViewById(R.id.phonenum);
        imageView2=(ImageView) findViewById(R.id.myZoomageView);


        prescription = (Button) findViewById(R.id.prescription);
        paymode = (Button) findViewById(R.id.pay_button);

        backButton = (Button) findViewById(R.id.okbutton);

        userId = getIntent().getStringExtra("userId");
        mobileNumber = getIntent().getStringExtra("mobile");

        close=(TextView) findViewById(R.id.close);

        ok = (Button) findViewById(R.id.ok);
        layout=(LinearLayout) findViewById(R.id.layout1);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.GONE);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("close");
                Intent intent=new Intent(ViewPatientMyDoctorAppointment.this,PatientMyDoctorAppointments.class);
                intent.putExtra("id",getIntent().getStringExtra("userId"));
                intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                startActivity(intent);
            }
        });

        close1 = (TextView) findViewById(R.id.close1);
        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.GONE);
            }
        });


        final Toolbar toolbar=(Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        //  collapsingToolbarLayout.setTitle("My Toolbar");
        collapsingToolbarLayout.setTitleEnabled(false);

        System.out.println("user id my doc.."+getIntent().getStringExtra("userId")+"...."+getIntent().getStringExtra("mobile"));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPatientMyDoctorAppointment.this,PatientMyDoctorAppointments.class);
                intent.putExtra("id",getIntent().getStringExtra("userId"));
                intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                startActivity(intent);
            }
        });

        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object latitude = doctorLatitude;
                Object longitude = doctorLongitude;
                String uri = String.format(Locale.ENGLISH, "geo:0,0?q="+doctorLatitude+","+doctorLongitude+"("+doctorHospitalName+")", latitude, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        //phone call
        phonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phn = phonenumber.getText().toString();

                System.out.println("phone no in my diag..."+phn);

                Intent callintent = new Intent(Intent.ACTION_CALL);
                callintent.setData(Uri.parse("tel:"+phn));
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
                startActivity(callintent);
            }
        });

        myappointmentId = getIntent().getStringExtra("appointmentId");
        myAppointmentdate = getIntent().getStringExtra("appointmentdate");
        mydoctorID = getIntent().getStringExtra("doctorId");
        mydoctorname = getIntent().getStringExtra("doctorname");
        mypatientname = getIntent().getStringExtra("patientname");
        mytimeslot = getIntent().getStringExtra("timeslot");
        mystatus = getIntent().getStringExtra("status");
        myreason = getIntent().getStringExtra("reason");
        mycomment = getIntent().getStringExtra("comments");
        myamount = getIntent().getStringExtra("amount");
        myprescription = getIntent().getStringExtra("prescription");
        mypaymentmode = getIntent().getStringExtra("paymentmode");

        System.out.println("doctorid...."+mydoctorID);
        System.out.println("appid...."+myappointmentId);

        System.out.println("mypayment...."+mypaymentmode+"..comments.."+mycomment);

        System.out.println("prescript url...."+myprescription);

        // get current date and time to disable cancel and reschedule links
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("M/dd/yyyy");
        String formattedDate = df.format(c);

//        SimpleDateFormat df1 = new SimpleDateFormat("M/dd/yyyy");
//        String formattedDate1 = df1.format(c);

//        System.out.println("appoint date in my doc.."+myAppointmentdate+"....current date1..."+formattedDate1);

        System.out.println("appoint date in my doc.."+myAppointmentdate+"....current date..."+formattedDate);

        if(myAppointmentdate.equals(formattedDate))
        {
            cancel.setClickable(true);
            reschedule.setClickable(true);
            paymode.setClickable(true);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // https://meditfhc.com/mapi/CancelAppointment?AppointmentID=1865

                    Toast.makeText(getApplicationContext(),"You cancelling this appointment",Toast.LENGTH_SHORT).show();

                new sendAppointmentCancellationDetails().execute(baseUrl.getUrl()+"CancelAppointment?AppointmentID="+appointmentId);

                }
            });

            reschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // https://meditfhc.com/mapi/CancelAppointment?AppointmentID=1865

                new sendAppointmentRescheduleDetails().execute(baseUrl.getUrl()+"CancelAppointment?AppointmentID="+appointmentId);

                }
            });

        }
        else
        {
            cancel.setClickable(false);
            reschedule.setClickable(false);
            paymode.setClickable(false);
        }

        new GetDoctorDetails().execute(baseUrl.getUrl()+"GetDoctorByID"+"?id="+mydoctorID);

        new GetDoctorAllAddressDetails().execute(baseUrl.getUrl()+"DoctorGetAllAddress?ID="+mydoctorID);

        String docName = "Dr. "+mydoctorname;
        int docNameLength = docName.length();

        String name_qualification = "Dr. "+mydoctorname+" "+mydoctorqualification;
//        String arr[] = name_qualification.split(" ");
        SpannableString spannableString = new SpannableString(name_qualification);
        spannableString.setSpan(new RelativeSizeSpan(1.35f),0,docNameLength,0);
        spannableString.setSpan(new ForegroundColorSpan(getApplicationContext().getResources().getColor(R.color.docName)),0,docNameLength,0);

        doctorname.setText(spannableString);

        Timeslot.setText(mytimeslot);
        patientname.setText(mypatientname);
        status.setText(mystatus);
        reason.setText(myreason);
        comment.setText(mycomment);
        amount.setText(myamount);
        payment.setText(mypaymentmode);


        if(mystatus.equals("Accept"))
        {
            paymode.setBackgroundColor(getResources().getColor(R.color.accept));
        }

        if(mystatus.equals("Reject"))
        {
            paymode.setBackgroundColor(getResources().getColor(R.color.reject));
            paymode.setVisibility(View.INVISIBLE);
        }

        if(mystatus.equals("Initiation Pending"))
        {
            paymode.setBackgroundColor(getResources().getColor(R.color.initiation));
            paymode.setVisibility(View.INVISIBLE);
        }

        if(mystatus.equals("Cancel") || mystatus.equals("Reschedule"))
        {
            paymode.setBackgroundColor(getResources().getColor(R.color.reject));
            cancel.setEnabled(false);
            reschedule.setEnabled(false);
            cancel.setFocusable(false);
            reschedule.setFocusable(false);
            cancel.setTextColor(getResources().getColor(R.color.cancel_or_reschedule));
            reschedule.setTextColor(getResources().getColor(R.color.cancel_or_reschedule));
        }

        if(mystatus.equals("Reschedule"))
        {
            paymode.setBackgroundColor(getResources().getColor(R.color.reschedule));
            paymode.setVisibility(View.INVISIBLE);
        }

        prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myprescription.equals(""))
                {
                    showalert();
                }
                else
                {
                    layout.setVisibility(View.VISIBLE);
                    new DownloadPrescription().execute(baseUrl.getImageUrl()+myprescription);
                }

            }
        });


    }


    //Get all addresses for doctor list from api call
    private class GetDoctorAllAddressDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";
            HttpURLConnection httpURLConnection = null;
            try {
                System.out.println("dsfafssss....");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                Log.d("Service", "Started");
                httpURLConnection.setRequestMethod("GET");
                System.out.println("u...."+params[0]);
                System.out.println("dsfafssss....");
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("Api response.....", result);
            getData(result);
        }
    }

    private void getData(String result) {
        try {

            JSONArray jarray = new JSONArray(result);

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);

                doctorLatitude = object.getString("Latitude");
                doctorLongitude = object.getString("Longitude");
                doctorHospitalName = object.getString("HospitalName");
//                doctorAddress = object.getString("Address1");


            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showimage() {


//        Dialog settingsDialog = new Dialog(this);
//        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.image_popup, null));
//        settingsDialog.show();
//        settingsDialog.setCanceledOnTouchOutside(true);
    }


    private void showalert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewPatientMyDoctorAppointment.this);
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
            mProgressDialog = new ProgressDialog(ViewPatientMyDoctorAppointment.this);
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
             imageView2.setImageBitmap(result);
            // Close progressdialog
            mProgressDialog.dismiss();
        }
    }



    //get doctor details based on id from api call
    private class GetDoctorDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";
            HttpURLConnection httpURLConnection = null;
            try {
                System.out.println("dsfafssss....");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                Log.d("Service", "Started");
                httpURLConnection.setRequestMethod("GET");

//                httpURLConnection.setDoOutput(true);
                System.out.println("u...." + params[0]);
                System.out.println("dsfafssss....");
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e("TAG result docprofile", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getProfileDetails(result);
        }

    }

    private void getProfileDetails(String result) {
        try
        {
            JSONObject js = new JSONObject(result);

            mydoctormobile = (String) js.get("MobileNumber");
            mydctorspeciality = js.getString("SpecialityName");
            mydoctorImage = (String) js.getString("DoctorImage");
            mydoctorqualification=js.getString("Qualification");
            System.out.println("image"+mydoctorImage);
            new DownloadImage().execute(baseUrl.getImageUrl()+mydoctorImage);
            phonenumber.setText(mydoctormobile);
            doctorspeciality.setText(mydctorspeciality);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ViewPatientMyDoctorAppointment.this);
            // Set progressdialog title
//            mProgressDialog.setTitle("Download Image Tutorial");
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
            imageView.setImageBitmap(result);
            // Close progressdialog
            mProgressDialog.dismiss();
        }

    }

    //send appointment cancellation details
    private class sendAppointmentCancellationDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

//            HttpURLConnection connection=null;
            HttpURLConnection httpURLConnection = null;
            try {
                System.out.println("dsfafssss....");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                Log.d("Service","Started");
                httpURLConnection.connect();

                //write
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                System.out.println("params doc add....."+params[0]);
                wr.writeBytes(params[0]);
                wr.flush();
                wr.close();

                int statuscode = httpURLConnection.getResponseCode();

                System.out.println("status code....."+statuscode);

                InputStream in = null;
                if (statuscode == 200) {

                    in = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(in);

                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data += current;
                    }

                }
                else if(statuscode == 404){
                    in = httpURLConnection.getErrorStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(in);

                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data += current;
                    }
                }
                else if(statuscode == 500){
                    in = httpURLConnection.getErrorStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(in);

                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data += current;
                    }
                }
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//
            Log.e("TAG result cancel app", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            JSONObject js;

            try {
                js= new JSONObject(result);
                int s = js.getInt("Code");
                if(s == 200)
                {

                    showCancelSuccessMessage(js.getString("Message"));
                }
                else
                {
                    showCancelErrorMessage(js.getString("Message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void showCancelSuccessMessage(String message){

        android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();

//                        new Mytask().execute();
                        Intent intent = new Intent(ViewPatientMyDoctorAppointment.this,PatientDashBoard.class);
                        intent.putExtra("id",userId);
                        intent.putExtra("mobile",mobileNumber);
                        startActivity(intent);
                    }
                });
        android.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Your Appointment");
        alert.show();

    }

    public void showCancelErrorMessage(String message){

        android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Your Appointment");
        alert.show();

    }


    //send appointment reschedule details
    private class sendAppointmentRescheduleDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

//            HttpURLConnection connection=null;
            HttpURLConnection httpURLConnection = null;
            try {
                System.out.println("dsfafssss....");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setUseCaches(false);
//                httpURLConnection.setChunkedStreamingMode(1024);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
//                httpURLConnection.setRequestProperty("Accept", "application/json");
                Log.d("Service","Started");
//                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                //write
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                System.out.println("params doc add....."+params[0]);
                wr.writeBytes(params[0]);
                wr.flush();
                wr.close();

                int statuscode = httpURLConnection.getResponseCode();

                System.out.println("status code....."+statuscode);

                InputStream in = null;
                if (statuscode == 200) {

                    in = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(in);

                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data += current;
                    }

                }
                else if(statuscode == 404){
                    in = httpURLConnection.getErrorStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(in);

                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data += current;
                    }
                }
                else if(statuscode == 500){
                    in = httpURLConnection.getErrorStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(in);

                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data += current;
                    }
                }
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//
            Log.e("TAG result cancel app", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            JSONObject js;

            try {
                js= new JSONObject(result);
                int s = js.getInt("Code");
                if(s == 200)
                {

                    showRescheduleSuccessMessage1(js.getString("Message"));
                }
                else
                {
                    showRescheduleErrorMessage1(js.getString("Message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public void showRescheduleSuccessMessage1(String message){

        android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();

//                        new Mytask().execute();
                        Intent intent = new Intent(ViewPatientMyDoctorAppointment.this,GetCurrentDoctorsList.class);
                        intent.putExtra("userId",userId);
                        intent.putExtra("mobile",mobileNumber);
                        startActivity(intent);
                    }
                });
        android.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Your Appointment");
        alert.show();

    }

    public void showRescheduleErrorMessage1(String message){

        android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Your Appointment");
        alert.show();

    }

//    // Doctor after reject the patient appointment:
//
//    private class Mytask extends AsyncTask<Void, Void,Void>
//    {
//
//        URL myURL=null;
//        HttpURLConnection myURLConnection=null;
//        BufferedReader reader=null;
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try {
//
//                String username = mypatientname;
//                String doctorname = mydoctorname;
//                String doctormobilenum = "";
//                String Status="Rejected";
//                String address="Dr.No-123,jntuk,kakinda";
//                String link="https://www.medictfhc.com/";
//
//                String message="Hi "+username+", Sorry for your in convince your appointment has been "+Status+" by Dr."+doctorname+" on "+date+" at "+address+". Please call "+doctormobilenum+" for any assistance/re-schedule. "+" Thank You."+" Click here to navigate:"+link;
//                smsUrl = baseUrl.getSmsUrl();
//                String uname="MedicTr";
//                String password="X!g@c$R2";
//                String sender="MEDICC";
//                String destination = "";
//
//                String encode_message= URLEncoder.encode(message, "UTF-8");
//                StringBuilder stringBuilder=new StringBuilder(smsUrl);
//                stringBuilder.append("uname="+URLEncoder.encode(uname, "UTF-8"));
//                stringBuilder.append("&pass="+URLEncoder.encode(password, "UTF-8"));
//                stringBuilder.append("&send="+URLEncoder.encode(sender, "UTF-8"));
//                stringBuilder.append("&dest="+URLEncoder.encode(destination, "UTF-8"));
//
//                stringBuilder.append("&msg="+encode_message);
//
//                smsUrl=stringBuilder.toString();
//                System.out.println("smsUrl.. "+smsUrl);
//                myURL=new URL(smsUrl);
//
//                myURLConnection=(HttpURLConnection) myURL.openConnection();
//                myURLConnection.connect();
//                reader=new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
//                String response;
//                while ((response = reader.readLine()) != null) {
//                    Log.d("RESPONSE", "" + response);
//                }
//                reader.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
////            Toast.makeText(getApplicationContext(), "the message", Toast.LENGTH_LONG).show();
//            super.onPreExecute();
//        }
//    }

}