package com.example.cool.patient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

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

public class GetPatientDetailsListInDiagnostics extends AppCompatActivity {

    String date,diagID,diagmobile;

    String addressId,Payment,PatientName,CenterName,EmailID,MobileNo,Comments,Prescription,Amount,Aadharnumber,Specialitylist,Speciality;
    int Dstatus,RDTestID,SpecialityID;

    String status;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    LinearLayoutManager layoutManager;
    private MyPatientDataAdapterInDiagnostics adapter;

    private List<MyPatientDataClassInDiagnostics> data_list;
    List<String> speciality;

    ApiBaseUrl baseUrl;

    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_patient_details_list_in_diagnostics);

        baseUrl =  new ApiBaseUrl();

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        data_list=new ArrayList<>();
        speciality=new ArrayList<>();

        date = getIntent().getStringExtra("date");
        diagID = getIntent().getStringExtra("id");
        diagmobile = getIntent().getStringExtra("mobile");

        new GetPatientDetails().execute(baseUrl.getUrl()+"APIGetDiagnosticAppointToAccept"+"?id="+diagID+"&AppointmentDate="+date);

        gridLayoutManager = new GridLayoutManager(this,2);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyPatientDataAdapterInDiagnostics(this,data_list);
        recyclerView.setAdapter(adapter);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("Patients List");
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(GetPatientDetailsListInDiagnostics.this,DiagnosticDashboard.class);
                        intent.putExtra("id",diagID);
                        intent.putExtra("mobile",diagmobile);
                        startActivity(intent);

                    }
                }

        );

    }



    //Get patient details  based on doctor id and appointment date
    private class GetPatientDetails extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(GetPatientDetailsListInDiagnostics.this);
            // Set progressdialog title
//            mProgressDialog.setTitle("Image");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");

            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
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
            mProgressDialog.dismiss();
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
                addressId = js.getString("AddressID");
                Payment = (String) js.get("Payment");
                RDTestID=js.getInt("RDID");
                PatientName=(String)js.get("PatientName");
                Comments=(String) js.get("DComment");
                CenterName=(String)js.get("CenterName");
                EmailID = (String) js.get("EmailID");
                MobileNo=(String) js.get("MobileNo");
                Prescription=(String) js.get("Prescription");
                Amount=(String)js.get("Amount");
                Aadharnumber=(String) js.get("Aadharnumber");

                if(Dstatus==1)
                {
                    status="Initiated";
                }
                else if(Dstatus==2){

                    status="In Progress";
                }
                else if(Dstatus==3){

                    status="Finished";
                }
                else {

                    status="Pending";
                }

                JSONArray jsonArray1=new JSONArray((js.getString("SpecialityLst")));

                for(int j=0;j<jsonArray1.length();j++)
                {
                    JSONObject jsonObject=jsonArray1.getJSONObject(j);
                    SpecialityID=jsonObject.getInt("SpecialityID");
                    Speciality=jsonObject.getString("Speciality");
                    speciality.add(Speciality);
                }

                MyPatientDataClassInDiagnostics myPatientData=new MyPatientDataClassInDiagnostics(diagID,diagmobile,addressId,Dstatus,Payment,
                        RDTestID,PatientName,Comments,CenterName,EmailID,MobileNo,Prescription,Amount,Aadharnumber,speciality,
                        status,date);

                data_list.add(myPatientData);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
