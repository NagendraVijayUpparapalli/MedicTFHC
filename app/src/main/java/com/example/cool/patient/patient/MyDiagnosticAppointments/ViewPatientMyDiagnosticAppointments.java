package com.example.cool.patient.patient.MyDiagnosticAppointments;


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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.patient.BookAppointment.PatientBookAppointmentToDiagnostics;
import com.example.cool.patient.patient.PatientDashBoard;
import com.example.cool.patient.patient.ViewDiagnosticsList.GetCurrentDiagnosticsList;
import com.example.cool.patient.R;
import com.jsibbold.zoomage.ZoomageView;

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

public class ViewPatientMyDiagnosticAppointments extends AppCompatActivity {

    String DiagAddressId,diagLat,diagLongi,diagCenterImage,diagMobile,userId,mobileNumber,appointmentId,Patientname,
            centername, testname, diagnoticstatus, diagnosticreport, paymentmode, amount, comment,appointmentdate;

    TextView paname, centename, ttname, diastatus, paymode, amnt, cmment,cancel,reschedule,phonenumber,navigation,close;
    ImageView centerImage;
    ZoomageView diagreport;


    ProgressDialog mProgressDialog;
    Button prescription, backButton;

    ApiBaseUrl baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_my_diagnostic_appointments);

        baseUrl = new ApiBaseUrl();

        paname = (TextView) findViewById(R.id.Patient_name);
        centename = (TextView) findViewById(R.id.Center_name);
        ttname = (TextView) findViewById(R.id.Test_name);
        diastatus = (TextView) findViewById(R.id.status);
        paymode = (TextView) findViewById(R.id.payment);
        amnt = (TextView) findViewById(R.id.amount);
        cmment = (TextView) findViewById(R.id.Diagnostic_comments);
        navigation = (TextView) findViewById(R.id.navigation);
        diagreport = (ZoomageView) findViewById(R.id.myZoomageView);
        prescription = (Button) findViewById(R.id.prescription);
        backButton = (Button) findViewById(R.id.okbutton);

        centerImage = (ImageView) findViewById(R.id.center_image);
        phonenumber = (TextView) findViewById(R.id.phonenum);

        cancel = (TextView) findViewById(R.id.cancel);
        reschedule = (TextView) findViewById(R.id.Reschedule);

        DiagAddressId = getIntent().getStringExtra("DiagAddressId");
        userId = getIntent().getStringExtra("userId");
        appointmentdate = getIntent().getStringExtra("appointmentdate");
        mobileNumber = getIntent().getStringExtra("mobile");
        appointmentId = getIntent().getStringExtra("appointmentId");
        Patientname = getIntent().getStringExtra("patientname");
        centername = getIntent().getStringExtra("centername");
        testname = getIntent().getStringExtra("testname");
        diagnoticstatus = getIntent().getStringExtra("status");
        diagnosticreport = getIntent().getStringExtra("diagnostcreport");
        paymentmode = getIntent().getStringExtra("paymentmode");
        amount = getIntent().getStringExtra("amount");
        comment = getIntent().getStringExtra("comment");

        new GetDiagnosticCenterbyAddressIDDetails().execute(baseUrl.getUrl()+"DiagnosticCenterbyAdressByID?AddressID="+DiagAddressId);

        System.out.println("user id my doc.."+userId+"...."+mobileNumber);

        // get current date and time to disable cancel and reschedule links
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        System.out.println("appoint date in my diag.."+appointmentdate+"....current date..."+formattedDate);

        if(appointmentdate.equals(formattedDate))
        {
            cancel.setClickable(true);
            reschedule.setClickable(true);
            paymode.setClickable(true);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new sendAppointmentCancellationDetails().execute(baseUrl.getUrl()+"CancelDiagAppointment?AppointmentID="+appointmentId);

                }
            });

            reschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new sendAppointmentRescheduleDetails().execute(baseUrl.getUrl()+"CancelDiagAppointment?AppointmentID="+appointmentId);

                }
            });
        }
        else
        {
            cancel.setClickable(false);
            reschedule.setClickable(false);
            paymode.setClickable(false);
        }

        prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(diagnosticreport.equals(""))
                {
                    showalert();
                }
                else
                {
                    diagreport.setVisibility(View.VISIBLE);
                    new DownloadPrescription().execute(baseUrl.getImageUrl()+diagnosticreport);
                }
            }
        });

        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object latitude = diagLat;
                Object longitude = diagLongi;
                String uri = String.format(Locale.ENGLISH, "geo:0,0?q="+diagLat+","+diagLongi+"("+centername+")", latitude, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPatientMyDiagnosticAppointments.this,PatientMyDiagnosticAppointments.class);
                intent.putExtra("id",getIntent().getStringExtra("userId"));
                intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                startActivity(intent);
            }
        });

        close=(TextView)findViewById(R.id.close) ;

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("close");
                Intent intent=new Intent(ViewPatientMyDiagnosticAppointments.this,PatientMyDiagnosticAppointments.class);
                intent.putExtra("id",getIntent().getStringExtra("userId"));
                intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                startActivity(intent);
            }
        });

        final Toolbar toolbar=(Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        //  collapsingToolbarLayout.setTitle("My Toolbar");
        collapsingToolbarLayout.setTitleEnabled(false);


        System.out.println("diagnosticreport....."+diagnosticreport);

        paname.setText(Patientname);
        centename.setText(centername);
//        phonenumber.setText(diagMobile);
        ttname.setText(testname);
        diastatus.setText(diagnoticstatus);
        paymode.setText(paymentmode);
        amnt.setText(amount);
        cmment.setText(comment);

        if(diagnoticstatus.equals("Initiation Pending"))
        {
            paymode.setBackgroundColor(getResources().getColor(R.color.initiation));
        }

        if(diagnoticstatus.equals("In Progress"))
        {
            paymode.setBackgroundColor(getResources().getColor(R.color.reject));
            paymode.setVisibility(View.VISIBLE);
        }

        if(diagnoticstatus.equals("Finished"))
        {
            paymode.setBackgroundColor(getResources().getColor(R.color.initiation));
            paymode.setVisibility(View.VISIBLE);
        }


        //phone call
        phonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phn = phonenumber.getText().toString();

                System.out.println("phone no in my diag..."+phn);

                Intent callintent = new Intent(Intent.ACTION_CALL);
                callintent.setData(Uri.parse("tel:"+phn));
                if (ActivityCompat.checkSelfPermission(ViewPatientMyDiagnosticAppointments.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

    //Get diagnostic center by addressID details from api call
    private class GetDiagnosticCenterbyAddressIDDetails extends AsyncTask<String, Void, String> {

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


                JSONObject object = new JSONObject(result);

                diagLat = object.getString("Latitude");
                diagLongi = object.getString("Longitude");
                diagCenterImage = object.getString("CenterImage");
                diagMobile = object.getString("MobileNumber");

                phonenumber.setText(diagMobile);
                System.out.println("image...."+diagCenterImage);
                new DownloadImage().execute(baseUrl.getImageUrl()+diagCenterImage);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ViewPatientMyDiagnosticAppointments.this);
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
            centerImage.setImageBitmap(result);
            // Close progressdialog
            mProgressDialog.dismiss();
        }
    }

    private class DownloadPrescription extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ViewPatientMyDiagnosticAppointments.this);
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
            diagreport.setImageBitmap(result);
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
                if(s == 500)
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
                        Intent intent = new Intent(ViewPatientMyDiagnosticAppointments.this,PatientDashBoard.class);
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
                System.out.println("params doc add....."+params[1]);
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
            Log.e("TAG result resched app", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            JSONObject js;

            try {
                js= new JSONObject(result);
                int s = js.getInt("Code");
                if(s == 500)
                {

                    showRescheduleSuccessMessage(js.getString("Message"));
                }
                else
                {
                    showRescheduleErrorMessage(js.getString("Message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public void showRescheduleSuccessMessage(String message){

        android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();

//                        new Mytask().execute();
                        Intent intent = new Intent(ViewPatientMyDiagnosticAppointments.this,GetCurrentDiagnosticsList.class);
                        intent.putExtra("userId",userId);
                        intent.putExtra("mobile",mobileNumber);
                        startActivity(intent);
                    }
                });
        android.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Your Appointment");
        alert.show();

    }

    public void showRescheduleErrorMessage(String message){

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

    private void showalert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewPatientMyDiagnosticAppointments.this);
        builder.setMessage("You don't have any prescription");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

}

