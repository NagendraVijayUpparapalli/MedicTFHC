package com.example.cool.patient;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.bloder.magic.view.MagicButton;

public class DiagnosticAddAddress extends AppCompatActivity {

    EditText diagnosticName,address,pincode,contactPerson,mobile,landlineMobileNumber,comments,lat,lng;
    Spinner city,state,district;
    CheckBox availableService;
    ImageView centerImage;
    FloatingActionButton addCenterIcon;
    MagicButton btn_AddAddress;


    static String uploadServerUrl = null,addressId ;
    static String myHospitalName,myAddress,myPincode,myContactPerson,myMobile,myLandlineMobileNumber,myComments,myLati,myLngi,myCity,myState,myDistrict;
    boolean myAvailableService;

    static int getUserId;
    TextView speciality;

    String[] ListItems;

    boolean[] checkedItems;
    List<String> getmUserItems = new ArrayList<>();
    List<String> getmUserItems_Value = new ArrayList<String>();
    Map<String, List<String>> map = new HashMap<String, List<String>>();

    //get specialities fields
    HashMap<Long, String> mySpecialityList = new HashMap<Long, String>();
    List<String> specialitiesList;

    List<String> districtsList,citiesList,statesList,amTimeSlotsList,pmTimeSlotsList;
    String myQrArrayList;
    String[] mydistrictlist;
    ArrayAdapter<String> adapter2,adapter3,adapter4;
    HashMap<Long, String> myCitiesList = new HashMap<Long, String>();
    HashMap<Long, String> myStatesList = new HashMap<Long, String>();
    HashMap<Long, String> myAmTimeSlotsList = new HashMap<Long, String>();
    HashMap<Long, String> myPmTimeSlotsList = new HashMap<Long, String>();
    List<String> myDistrictsList = new ArrayList<String>();

    final int REQUEST_CODE_GALLERY1 = 999;

    ApiBaseUrl baseUrl;

    //timings variables
    EditText chooseTime,ToTime;
    TimePickerDialog timePickerDialog;
    TimePickerDialog timePickerDialog1;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;

    //get lat,lng on touch map
    String myLatitude,myLongitude;
    TextView getLatLong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic_add_address);

        baseUrl = new ApiBaseUrl();

        new GetAllCities().execute(baseUrl.getUrl()+"GetAllCity");

        new GetAllStates().execute(baseUrl.getUrl()+"GetAllState");

        new GetAllDistricts().execute(baseUrl.getUrl()+"GetAllDistrict");


        new GetAllDiagSpecialities().execute(baseUrl.getUrl()+"GetDiagSpeciality");

        getUserId = getIntent().getIntExtra("id",getUserId);
        System.out.print("diagid in add address....."+getUserId);

        diagnosticName = (EditText) findViewById(R.id.Diagnostic_Name);
        address = (EditText) findViewById(R.id.Address);
        city = (Spinner) findViewById(R.id.cityId);
        state = (Spinner) findViewById(R.id.stateId);
        district = (Spinner) findViewById(R.id.districtId);
        mobile = (EditText) findViewById(R.id.Mobile_Number);
        pincode = (EditText) findViewById(R.id.pincode);
        contactPerson = (EditText) findViewById(R.id.Frontoffice);
        landlineMobileNumber = (EditText) findViewById(R.id.landMobileNumber);
        comments = (EditText) findViewById(R.id.Comments_Others);
        getLatLong = findViewById(R.id.getlatlng);
        lat = (EditText) findViewById(R.id.Latitude);
        lng = (EditText) findViewById(R.id.Longitude);
        availableService = (CheckBox) findViewById(R.id.serviceAvailable);
        speciality = (TextView) findViewById(R.id.Select_Speciality);
        chooseTime = findViewById(R.id.From);
        ToTime = findViewById(R.id.To_Timing);

        centerImage = (ImageView) findViewById(R.id.diag_center_image);
        addCenterIcon = (FloatingActionButton) findViewById(R.id.addDiagCenterIcon);
        btn_AddAddress = (MagicButton)findViewById(R.id.gen_btn);

        getLatLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiagnosticAddAddress.this,MapsActivity.class);
                intent.putExtra("diag","diagAdd");
                intent.putExtra("id",getUserId);
                intent.putExtra("diagName",diagnosticName.getText().toString());
                intent.putExtra("address",address.getText().toString());
                intent.putExtra("city",city.getSelectedItem().toString());
                intent.putExtra("state",state.getSelectedItem().toString());
                intent.putExtra("district",district.getSelectedItem().toString());
                intent.putExtra("mobile",mobile.getText().toString());
                intent.putExtra("pincode",pincode.getText().toString());
                intent.putExtra("person",contactPerson.getText().toString());
                intent.putExtra("landmobile",landlineMobileNumber.getText().toString());
                intent.putExtra("comments",comments.getText().toString());
                startActivity(intent);
            }
        });

        System.out.print("diagnos in add address comments....."+comments.getText().toString());
//        myLatitude = getIntent().getStringExtra("lat");
//        myLongitude = getIntent().getStringExtra("lng");

        btn_AddAddress.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String js = formatDataAsJson();
//                new sendEditProfileDetails().execute(baseUrl.getUrl()+"DoctorAddAddress",js.toString());
            }

        });

        addCenterIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(
                                DiagnosticAddAddress.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_GALLERY1
                        );

                    }
                });

        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showalert();
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(DiagnosticAddAddress.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        chooseTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();



            }
        });


        ToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(DiagnosticAddAddress.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        ToTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });


        speciality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DiagnosticAddAddress.this,
                        "Speciality", Toast.LENGTH_LONG).show();
                AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DiagnosticAddAddress.this);
                mBuilder2.setTitle("Your Specialities");
                mBuilder2.setMultiChoiceItems(ListItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked){
                            String i = Integer.toString(position);
                            getmUserItems_Value.add(i);
                        }else{
                            getmUserItems_Value.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder2.setCancelable(false);
                mBuilder2.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";

                        for (int i = 0; i <  getmUserItems_Value.size(); i++) {
                            item = item + ListItems[Integer.parseInt(getmUserItems_Value.get(i))];
                            if (i != getmUserItems_Value.size() - 1) {
                                item = item + ", ";

                            }
                        }


                        getmUserItems.add(item);
                        map.put("Speciaity",getmUserItems);


                        // mItemSelectedFriday.setText("Friday=>"+item);




                    }
                });

                mBuilder2.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder2.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i <   checkedItems.length; i++) {
                            checkedItems[i] = false;
                            getmUserItems_Value.clear();
                            //   mItemSelectedFriday.setText("");
                        }

                    }

                });

                AlertDialog mDialog1 = mBuilder2.create();
                mDialog1.show();
            }
        });




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.qrcode);

//        toolbar.setNavigationIcon(R.drawable.qrcode);

//        getSupportActionBar().setCustomView();

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("Add Address");
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DiagnosticAddAddress.this,DiagnosticDashboard.class);
                        intent.putExtra("id",getUserId);
                        startActivity(intent);

                    }
                }

        );

    }

//    private String formatDataAsJson()
//    {
//
//        JSONObject data = new JSONObject();
//
////        System.out.println("emergency contact..."+Emergency_mobile);
//
//        myHospitalName = hospitalName.getText().toString().trim();
//        myAddress = address.getText().toString().trim();
//        myPincode = pincode.getText().toString().trim();
//        myContactPerson = contactPerson.getText().toString();
//        myFee = fee.getText().toString();
//        myLandlineMobileNumber = landlineMobileNumber.getText().toString().trim();
//        myComments = comments.getText().toString().trim();
//        myLati = lat.getText().toString().trim();
//        myLngi = lng.getText().toString().trim();
//        myCity= city.getSelectedItem().toString();
//        myState= state.getSelectedItem().toString();
//        myDistrict= district.getSelectedItem().toString();
//
//        if(availableService.isChecked()){
//            myAvailableService = true;
//        }
//        else if(!availableService.isChecked())
//        {
//            myAvailableService = false;
//        }
//
//        try{
//            data.put("DoctorID",getUserId);
//            data.put("Address1",myAddress);
//            data.put("HospitalName",myHospitalName);
//
//            data.put("ZipCode",myPincode);
//            data.put("LandlineNo",myLandlineMobileNumber);
//
////            data.put("EmergencyContact",myExperience);///
//
//            data.put("District",myDistrict);
//            data.put("FrontofficeContactPerson",myContactPerson);
//
//            data.put("iConsultationFee",myFee);
//            data.put("EmergencyService", myAvailableService);
//            data.put("Latitude",myLati);
//            data.put("Longitude", myLngi);
//            data.put("PromotionalOffer", myComments);///
//
//            return data.toString();
//
//        }
//        catch (Exception e)
//        {
//            Log.d("JSON","Can't format JSON");
//        }
//
//        return null;
//    }


    //send diagnostic edit profile details
    private class sendEditProfileDetails extends AsyncTask<String, Void, String> {

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
                System.out.println("params diag add....."+params[1]);
                wr.writeBytes(params[1]);
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
            Log.e("TAG result diag add   ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            JSONObject js;

//            try {
//                js= new JSONObject(result);
//                int s = js.getInt("Code");
//                if(s == 200)
//                {
//                    addressId = js.getString("DataValue");
////                    showSuccessMessage(js.getString("Message"));
//                }
////                else
////                {
////                    showErrorMessage(js.getString("Message"));
////                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }


        }
    }

    public void showalert() {

        timePickerDialog1 = new TimePickerDialog(DiagnosticAddAddress.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                if (hourOfDay >= 12) {
                    amPm = "PM";
                } else {
                    amPm = "AM";
                }
                ToTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
            }
        }, currentHour, currentMinute, false);

        timePickerDialog1.show();
    }

    //Get DiagSpecialities list from api call
    private class GetAllDiagSpecialities extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result specialities", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getSpecialities(result);

        }
    }

    private void getSpecialities(String result) {
        try
        {
            JSONArray jsonArr = new JSONArray(result);
            specialitiesList = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++) {

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);
                Long specialityKey = jsonObj.getLong("Key");
                String specialityValue = jsonObj.getString("Value");
                mySpecialityList.put(specialityKey,specialityValue);
                specialitiesList.add(jsonObj.getString("Value"));
                System.out.print("myspeciality list.."+mySpecialityList);
                System.out.print("speciality list.."+specialitiesList);
                String[] stockArr = new String[specialitiesList.size()];
                ListItems = specialitiesList.toArray(stockArr);
                checkedItems = new boolean[ListItems.length];
            }

        }
        catch (JSONException e)
        {}
    }

    //Get cities list from api call
    public class GetAllCities extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result  cities ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getCities(result);

        }
    }

    private void getCities(String result) {
        try
        {

            JSONArray jsonArr = new JSONArray(result);
            citiesList = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++) {

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                Long cityKey = jsonObj.getLong("Key");
                String cityValue = jsonObj.getString("Value");
                myCitiesList.put(cityKey,cityValue);
                citiesList.add(jsonObj.getString("Value"));
                System.out.print("mycity list.."+myCitiesList);
                System.out.print("city list.."+citiesList);
            }

            adapter3 = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, citiesList);
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
            city.setAdapter(adapter3); // Apply the adapter to the spinner



        }
        catch (JSONException e)
        {}
    }

    //Get states list from api call
    private class GetAllStates extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result  states  ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getStates(result);
        }
    }

    private void getStates(String result) {
        try
        {
            JSONArray jsonArr = new JSONArray(result);
            statesList = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++) {

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                Long stateKey = jsonObj.getLong("Key");
                String stateValue = jsonObj.getString("Value");
                myStatesList.put(stateKey,stateValue);
                statesList.add(jsonObj.getString("Value"));
            }

            adapter2 = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, statesList);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
            state.setAdapter(adapter2); // Apply the adapter to the spinner

        }
        catch (JSONException e)
        {}
    }

    //Get districts list from api call
    private class GetAllDistricts extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result  districts  ", result); // this is expecting a response code to be sent from your server upon receiving the POST data

            getDistricts(result);

        }
    }

    private void getDistricts(String result) {
        mydistrictlist = result.split(",");
        JSONArray myjson;
        try {
            myjson = new JSONArray(result);
            int len = myjson.length();
            districtsList = new ArrayList<String>();
            for (int i = 0; i < len; i++) {

                districtsList.add(myjson.getString(i));
                myDistrictsList.add(i,myjson.getString(i));
//            districtsList.add(0,myDistrict);
            }

            adapter4 = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, districtsList);
            adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
            district.setAdapter(adapter4); // Apply the adapter to the spinner
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
