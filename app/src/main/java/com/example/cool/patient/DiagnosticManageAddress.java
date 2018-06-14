package com.example.cool.patient;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.MultiAutoCompleteTextView;

import org.json.JSONArray;
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

public class DiagnosticManageAddress extends AppCompatActivity {

    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<DiagnosticsManageAddressClass> myList;
    android.widget.ListView listview;
    DiagnosticsManageAddressAdapter adapter;

    //api url
    static String getUserId,regMobile;
    static String uploadServerUrl = null,myProfileImage;
    ApiBaseUrl baseUrl;

    ProgressDialog progressDialog;

    MultiAutoCompleteTextView reason_Todelete;
    String reasonToDelete = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic_manage_address);

        baseUrl = new ApiBaseUrl();

        regMobile = getIntent().getStringExtra("mobile");
        getUserId = getIntent().getStringExtra("id");
        System.out.print("doctorid in manage address....."+getUserId);
//
        uploadServerUrl = baseUrl.getUrl()+"DiagnosticGetAllAddress?ID="+getUserId;
//
//        new GetDoctorDetails().execute(baseUrl.getUrl()+"GetDoctorByID"+"?id="+getUserId);
//
        new GetDiagnosticsAllAddressDetails().execute(uploadServerUrl);
//
        myList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
//
//
        adapter = new DiagnosticsManageAddressAdapter(this, myList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
//
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        toolbar.setTitle("Manage Address");

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(BloodBank.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DiagnosticManageAddress.this,DiagnosticDashboard.class);
                        intent.putExtra("id",getUserId);
                        intent.putExtra("mobile",regMobile);
                        startActivity(intent);

                    }
                }

        );


    }


    //Get all addresses for diagnostics list from api call
    private class GetDiagnosticsAllAddressDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(DiagnosticManageAddress.this);
            // Set progressdialog title
//            progressDialog.setTitle("You are logging");
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
            progressDialog.dismiss();
            Log.e("Api response.....", result);
            try
            {
                JSONObject jsono = new JSONObject(result);
                String ss = (String) jsono.get("Message");
                if(ss.equals("No data found."))
                {
                    showMessage();
                    Log.e("Api response if.....", result);
                }
                else
                {
                    getData(result);
                    adapter.notifyDataSetChanged();
                    Log.e("Api response else.....", result);
                }
            }
            catch (Exception e)
            {}
            getData(result);
            adapter.notifyDataSetChanged();
//            Log.e("Api response.....", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }

    private void getData(String result) {
        try {

            JSONArray jarray = new JSONArray(result);

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);
                DiagnosticsManageAddressClass bb = new DiagnosticsManageAddressClass();

                bb.setMobileNumeber(object.getString("MobileNumber"));
                bb.setDiagnosticId(object.getString("DiagnosticsID"));
                bb.setCityName(object.getString("CityName"));
                bb.setStateName(object.getString("StateName"));
                bb.setCenterName(object.getString("CenterName"));
                bb.setAddress1(object.getString("Address1"));
                bb.setComment(object.getString("Comment"));
                bb.setEmergencyContactNumber(object.getString("EmergencyContact"));
                bb.setStateId(object.getString("StateID"));
                bb.setCityId(object.getString("CityID"));
                bb.setPincode(object.getString("PinCode"));
                bb.setDistrict(object.getString("District"));
                bb.setLandLineNo(object.getString("LandlineNo"));
                bb.setContactPerson(object.getString("ContactPerson"));
                bb.setLatitude((object.getString("Latitude")));
                bb.setLongitude((object.getString("Longitude")));
                bb.setAddressId(object.getString("AddressID"));
                bb.setFromTime(object.getString("FromTime"));
                bb.setToTime(object.getString("ToTime"));
                bb.setDeleteReason("Test");
                bb.setEmergencyservice(true);/////
                bb.setRegisterdMobileNumber(regMobile);

                myList.add(bb);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void showMessage(){

        android.support.v7.app.AlertDialog.Builder a_builder = new android.support.v7.app.AlertDialog.Builder(DiagnosticManageAddress.this);
        a_builder.setMessage("Addresses are not available for ur profile")
                .setCancelable(false)
                .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Address list");
        alert.show();

    }



}
