package com.example.cool.patient.diagnostic.TodaysAppointments;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.diagnostic.DashBoardCalendar.DiagnosticDashboard;
import com.example.cool.patient.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DiagnosticsTodaysAppointments extends AppCompatActivity {


    String date,geturl;

    String Status,PatientName,CenterName,EmailID,MobileNo,Aadharnumber;
    int Dstatus,RDTestID,AppointmentID;

    private RecyclerView recyclerView;

    LinearLayoutManager layoutManager;
    private DiagnosticsTodaysAppointmentsAdapter adapter;

    private List<PatientDatainDiagnosticsTodaysAppointmentsClass> data_list;

    String diagId,diagMobile;
    ApiBaseUrl baseUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostics_todays_appointments);

        baseUrl = new ApiBaseUrl();

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        data_list=new ArrayList<>();

        diagId = getIntent().getStringExtra("userId");
        diagMobile = getIntent().getStringExtra("mobile");

        new GetPatientDetails().execute(baseUrl.getUrl()+"DiagnosticGetTodayAppointments"+"?id="+diagId);


        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DiagnosticsTodaysAppointmentsAdapter(this,data_list);
        recyclerView.setAdapter(adapter);


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
                        Intent intent = new Intent(DiagnosticsTodaysAppointments.this,DiagnosticDashboard.class);
                        intent.putExtra("mobile",diagMobile);
                        intent.putExtra("id",diagId);
                        startActivity(intent);

                    }
                }

        );



    }

    //Get patient details  based on doctor id and appointment date
    private class GetPatientDetails extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result    ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getDetails(result);
            adapter.notifyDataSetChanged();

        }
    }

    private void getDetails(String result) {
        try {

            JSONArray jsonArray=new JSONArray(result);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject js = jsonArray.getJSONObject(i);

                Dstatus = js.getInt("DStatus");
                RDTestID=js.getInt("RDID");
                AppointmentID=js.getInt("AppointmentID");
                PatientName=(String)js.get("PatientName");
                CenterName=(String)js.get("CenterName");
                EmailID = (String) js.get("EmailID");
                MobileNo=(String) js.get("MobileNo");
                Aadharnumber=(String)js.get("Aadharnumber");

                PatientDatainDiagnosticsTodaysAppointmentsClass myPatientData=new PatientDatainDiagnosticsTodaysAppointmentsClass(diagId,diagMobile,Dstatus,RDTestID,AppointmentID,PatientName,CenterName,EmailID,MobileNo,Aadharnumber);

                data_list.add(myPatientData);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
