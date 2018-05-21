package com.example.cool.patient;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DoctorTodaysAppointment extends AppCompatActivity {

    EditText name;
    TextView age,reason,viewhistory;
    Spinner diagnostic_centers;
    Button submitBtn;

    FloatingActionButton camaraicon;
    CheckBox refer;
    RelativeLayout relativeLayout;
    String patientage,patientname,appointmentreason;

    ProgressDialog progressDialog;

    Dialog MyDialog;
    Spinner spinner1;

    HashMap<Long, String> myDiagSpecialityList = new HashMap<Long, String>();
    List<String> specialityList;
    ArrayAdapter<String> adapter;

    ApiBaseUrl baseUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_todays_appointment);

        baseUrl = new ApiBaseUrl();

        name=(EditText)findViewById(R.id.name);
        age=(TextView) findViewById(R.id.age);
        reason=(TextView) findViewById(R.id.reason);
        viewhistory=(TextView) findViewById(R.id.click);
        submitBtn = (Button) findViewById(R.id.gen_btn);



        new GetAllDiagSpeciality().execute(baseUrl.getUrl()+"GetDiagSpeciality");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage();
            }
        });

//        diagnostic_centers = (Spinner)findViewById(R.id.diagnostic_centers);

        camaraicon=(FloatingActionButton) findViewById(R.id.camera_icon);
//        refer=(CheckBox) findViewById(R.id.refer_diagnostics);
//        relativeLayout=(RelativeLayout) findViewById(R.id.relative1);

        patientname=getIntent().getStringExtra("patientname").toString();
        patientage=getIntent().getStringExtra("age").toString();
        appointmentreason=getIntent().getStringExtra("reason").toString();

        age.setText(patientage);
        name.setText(patientname);
        reason.setText(appointmentreason);

        viewhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(DoctorTodaysAppointment.this,PatientHistoryInDoctor.class);
                startActivity(i);
            }
        });

//        refer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (refer.isChecked())
//                {
//                    relativeLayout.setVisibility(View.VISIBLE);
//                }
//            }
//        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("Todays Appointments");
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DoctorTodaysAppointment.this,DoctorTodaysAppointmentsForPatient.class);
//                        intent.putExtra("id",getUserId);
                        startActivity(intent);

                    }
                }

        );

    }

    // alert to show suucessfull

    //this is alert for refer diagnostic
    public void showMessage(){

        AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage("Do you want refer as diagnostic")
                .setCancelable(false)
                .setNegativeButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
                        showReferDiag();
                        Toast.makeText(getApplicationContext(),"Yes",Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(),"No",Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Refer as Diagnostic");
        alert.show();

    }

    public void showReferDiag()
    {
        MyDialog =  new Dialog(DoctorTodaysAppointment.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.doctor_refer_diagnostic);

        spinner1 = (Spinner)MyDialog.findViewById(R.id.speciality);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, specialityList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        spinner1.setAdapter(adapter); // Apply the adapter to the spinner
        spinner1.setVisibility(View.VISIBLE);

    }

//    public void onClick(View view)
//    {
//        if(refer.isChecked())
//        {
//            System.out.println("hiii");
//        }
//    }


    //Get all diagnostic specialities from api call
    public class GetAllDiagSpeciality extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(DoctorTodaysAppointment.this);
            // Set progressdialog title
            progressDialog.setTitle("Your searching process is");
            // Set progressdialog message
            progressDialog.setMessage("Loading...");

            progressDialog.setIndeterminate(false);
            // Show progressdialog
            progressDialog.show();
        }


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

            Log.e("TAG result prev timings", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getPreviousTiming(result);
            progressDialog.dismiss();

        }
    }

    private void getPreviousTiming(String result) {
        try
        {
            JSONArray jsonArr = new JSONArray(result);
            specialityList = new ArrayList<>();


            for (int i = 0; i < jsonArr.length(); i++) {

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                Long cityKey = jsonObj.getLong("Key");
                String cityValue = jsonObj.getString("Value");
                myDiagSpecialityList.put(cityKey,cityValue);
                specialityList.add(jsonObj.getString("Value"));



            }

//            System.out.println("sun prev.."+prevSunTimeSlotsList);

        }
        catch (JSONException e)
        {}
    }


}
