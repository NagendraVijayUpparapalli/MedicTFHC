package com.example.cool.patient;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.ListView;

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

public class DoctorManageAddress extends AppCompatActivity {

    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<DoctorManageAddressClass> myList;
    ListView listview;
    DoctorManageAddressAdapter adapter;

    //api url
    static int getUserId;
    static String uploadServerUrl = null,myProfileImage;
    ApiBaseUrl baseUrl;

    MultiAutoCompleteTextView reason_Todelete;
    String reasonToDelete = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_manage_address);

        baseUrl = new ApiBaseUrl();

        getUserId = getIntent().getIntExtra("id",getUserId);
        System.out.print("doctorid in manage address....."+getUserId);

        uploadServerUrl = baseUrl.getUrl()+"DoctorGetAllAddress?ID="+getUserId;

        new GetDoctorDetails().execute(baseUrl.getUrl()+"GetDoctorByID"+"?id="+getUserId);

        new GetDoctorAllAddressDetails().execute(uploadServerUrl);

//        arrayList = new ArrayList<DoctorManageAddressClass>();
//        listview = (android.widget.ListView)findViewById(R.id.mylist);

//        reason_Todelete = (MultiAutoCompleteTextView)findViewById(R.id.reason_delete);
//        reasonToDelete = reason_Todelete.getText().toString().trim();
//
//        System.out.println("comm.."+reasonToDelete);

        myList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);


        adapter = new DoctorManageAddressAdapter(this, myList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

//        adapter.notifyDataSetChanged();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        toolbar.setTitle("Manage Address");

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(BloodBank.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DoctorManageAddress.this,DoctorDashboard.class);
                        startActivity(intent);

                    }
                }

        );
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

//            (String) js.get("DoctorID");
            if(js.has("DoctorImage")) {
                myProfileImage = (String) js.get("DoctorImage");

                System.out.println("doc profile image url.." + myProfileImage);

            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

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
                DoctorManageAddressClass bb = new DoctorManageAddressClass();

                bb.setDoctorId(object.getString("DoctorID"));
                bb.setAddressId(object.getString("AddressID"));
                bb.setAddress1(object.getString("Address1"));
                bb.setHospitalName(object.getString("HospitalName"));
                bb.setStateId(object.getString("StateID"));
                bb.setCityId(object.getString("CityID"));
                bb.setStateName(object.getString("StateName"));
                bb.setConsultationFee(object.getString("ConsultationPrice"));
                bb.setEmergencyContactNumber(object.getString("EmergencyContact"));
                bb.setProfileImage(myProfileImage);
                String cityy = object.getString("CityName");

                bb.setCityName(object.getString("CityName"));

                bb.setZipcode(object.getString("ZipCode"));
                bb.setLandLineNo(object.getString("LandlineNo"));
                bb.setContactPerson(object.getString("FrontofficeContactPerson"));
                bb.setLatitude((object.getString("Latitude")));
                bb.setLongitude((object.getString("Longitude")));
                bb.setEmergencyservice(true);
                bb.setComment(object.getString("Comment"));
//                bb.setDeleteReason();
                bb.setDistrict(object.getString("District"));

                myList.add(bb);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void showMessage(){

        android.support.v7.app.AlertDialog.Builder a_builder = new android.support.v7.app.AlertDialog.Builder(DoctorManageAddress.this);
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
