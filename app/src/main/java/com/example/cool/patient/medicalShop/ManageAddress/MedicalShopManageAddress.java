package com.example.cool.patient.medicalShop.ManageAddress;

import android.app.ProgressDialog;
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

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.medicalShop.MedicalShopDashboard;
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

public class MedicalShopManageAddress extends AppCompatActivity {

    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<MedicalShopManageAddressClass> myList;
    ListView listview;
    MedicalShopManageAddressAdapter adapter;

    //api url
    static String getUserId,regMobile;
    static String uploadServerUrl = null,myProfileImage;
    ApiBaseUrl baseUrl;

    MultiAutoCompleteTextView reason_Todelete;
    String reasonToDelete = null;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_shop_manage_address);

        baseUrl = new ApiBaseUrl();

        getUserId = getIntent().getStringExtra("id");
        regMobile = getIntent().getStringExtra("mobile");
        System.out.print("medicalid in manage address....."+getUserId);

        uploadServerUrl = baseUrl.getUrl()+"MSGetAddress?ID="+getUserId;

        new GetMedicalDetails().execute(baseUrl.getUrl()+"MedicalShopByID"+"?id="+getUserId);

        new GetMedicalAllAddressDetails().execute(uploadServerUrl);

        myList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        adapter = new MedicalShopManageAddressAdapter(this, myList);
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
                        Intent intent = new Intent(MedicalShopManageAddress.this,MedicalShopDashboard.class);
                        intent.putExtra("id",getUserId);
                        intent.putExtra("mobile",regMobile);
                        startActivity(intent);

                    }
                }

        );
    }

    //get medical details based on id from api call
    private class GetMedicalDetails extends AsyncTask<String, Void, String> {

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
            if(js.has("ShopImage")) {
                myProfileImage = (String) js.get("ShopImage");

                System.out.println("doc profile image url.." + myProfileImage);

            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    //Get all addresses for doctor list from api call
    private class GetMedicalAllAddressDetails extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(MedicalShopManageAddress.this);
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
            progressDialog.dismiss();
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
                MedicalShopManageAddressClass bb = new MedicalShopManageAddressClass();
                bb.setMedicalshopID(getUserId);
                bb.setAddressId(object.getString("AddressID"));
                bb.setAddress1(object.getString("Address1"));
                bb.setFrom(object.getString("FromTime"));
                bb.setTo(object.getString("ToTime"));
                bb.setHospitalName(object.getString("ShopName"));
                bb.setStateId(object.getString("StateID"));
                bb.setCityId(object.getString("CityID"));
                bb.setPharmacyKey(object.getString("PharmacyType"));
                bb.setExperience(object.getString("Experience"));
                bb.setStateName(object.getString("StateName"));
                bb.setMobileNumber(object.getString("MobileNumber"));
                // bb.setConsultationFee(object.getString("ConsultationPrice"));

                bb.setEmergencyContactNumber(object.getString("EmergencyContact"));
                bb.setProfileImage(myProfileImage);
                bb.setRegisteredMobileNumber(regMobile);
                String cityy = object.getString("CityName");
                bb.setCityName(object.getString("CityName"));
                bb.setZipcode(object.getString("PinCode"));
                bb.setLandLineNo(object.getString("LandlineNo"));
                bb.setContactPerson(object.getString("ContactPerson"));
                bb.setLatitude((object.getString("Latitude")));
                bb.setLongitude((object.getString("Longitude")));
                bb.setEmergencyservice(true);////
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

        android.support.v7.app.AlertDialog.Builder a_builder = new android.support.v7.app.AlertDialog.Builder(MedicalShopManageAddress.this);
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
