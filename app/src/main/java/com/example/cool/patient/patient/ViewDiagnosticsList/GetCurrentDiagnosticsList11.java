package com.example.cool.patient.patient.ViewDiagnosticsList;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.patient.PatientDashBoard;
import com.example.cool.patient.R;
import com.example.cool.patient.common.SelectCity;
import com.google.android.gms.maps.model.LatLng;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
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
import android.widget.ListView;

public class GetCurrentDiagnosticsList11 extends AppCompatActivity {
    public static final CharSequence[] states = {"---Speciality---", "Head", "nose", "eyes"};
//    Dialog MyDialog;
//    Dialog MyDialoganother;
//    Button okBtn,cancelBtn;
    AlertDialog alertDialog1;

    private static SeekBar seek_bar;
    private static TextView distance,availability;
    static int progress_value,dis = 20,availabilityCount;

    ProgressDialog progressDialog;
    //lat,long
    static String uploadServerUrl = null;
    static String getcity=null;
    LocationManager locationManager;
    String lattitude,longitude;
    static double lat,lng;
    Geocoder geocoder;
    List<Address> addresses;
    List<Address> fulladdress;
    private static final int REQUEST_LOCATION = 1;


    static double selectedCitylat;
    static double selectedCitylong;
    static String myDistance = null;

    static String selectedlocation=null;

    String addressline,mobile,email,pincode,city,state;
    TextView current_city;

    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    ArrayList<DiagnosticsClass> myList;
    DiagnosticsListAdapter adapter;


    ListView listview;

    ApiBaseUrl baseUrl;
    String getUserId;
    TextView userId;

    //get specialities fields
    HashMap<Long, String> mySpecialityList = new HashMap<Long, String>();
    List<String> specialitiesList;
    String[] ListItems;
    boolean[] checkedItems;
    SearchableSpinner Speciality;
    ArrayAdapter<String > specialityAdapter;


    String cur_addressId,mydoctorId,myaddressId,mydocName,myhospitalName,myaddress,mycity,mystate,myfee,mypaymentMode,myphone,myLati,myLongi,myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_current_diagnostics_list11);

        baseUrl = new ApiBaseUrl();

        getUserId = getIntent().getStringExtra("userId");
        getcity = getIntent().getStringExtra("city");

        System.out.print("userid in getdiag list....."+getUserId);

        current_city = (TextView) findViewById(R.id.select_city);
        current_city.setText(getcity);

        new GetAllDiagSpecialities().execute(baseUrl.getUrl()+"GetDiagSpeciality");

        myList = new ArrayList<DiagnosticsClass>();


        current_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GetCurrentDiagnosticsList11.this,SelectCity.class);
                i.putExtra("module","diagList");
                i.putExtra("userId",getUserId);
                i.putExtra("mobile",getIntent().getStringExtra("mobile"));
                startActivity(i);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        toolbar.setTitle("Diagnostics List");

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(BloodBank.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(GetCurrentDiagnosticsList11.this,PatientDashBoard.class);
                        intent.putExtra("id",getUserId);
                        intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                        startActivity(intent);

                    }
                }

        );

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        else {
            getlatlng();
        }

        Speciality = (SearchableSpinner) findViewById(R.id.speciality);
        distance = (TextView) findViewById(R.id.DistanceRange);
        seek_bar = (SeekBar) findViewById(R.id.seekbar);
        availability = (TextView) findViewById(R.id.availability);
        seek_bar.setProgress(dis);

        rangeBar();


        adapter = new DiagnosticsListAdapter(this, myList);
        layoutManager = new LinearLayoutManager(this);

        Speciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String js = specialityBasedFormatDataAsJson();
                uploadServerUrl = baseUrl.getUrl()+"GetDiagnosticsInRange";

                new GetDiagnostics_N_List().execute(uploadServerUrl,js.toString());

                myList = new ArrayList<DiagnosticsClass>();
//
                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);

                adapter = new DiagnosticsListAdapter(GetCurrentDiagnosticsList11.this, myList);
                layoutManager = new LinearLayoutManager(GetCurrentDiagnosticsList11.this);

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        MyDialog =  new Dialog(GetCurrentDiagnosticsList11.this);
//        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        MyDialog.setContentView(R.layout.speciality_based_layout);
//
//        Speciality = (SearchableSpinner)MyDialog.findViewById(R.id.speciality);
//
//        okBtn = (Button)MyDialog.findViewById(R.id.ok_btn);
//        cancelBtn = (Button)MyDialog.findViewById(R.id.cancel_btn);
//        okBtn.setEnabled(true);
//        cancelBtn.setEnabled(true);
//        okBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_LONG).show();
//                anotheralert();
//
//            }
//        });
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                anotheralert();
//            }
//        });
//        MyDialog.setCancelable(false);
//        MyDialog.setCanceledOnTouchOutside(false);
//        MyDialog.show();

    }

    //Get DiagSpecialities list from api call
    private class GetAllDiagSpecialities extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(GetCurrentDiagnosticsList11.this);
            // Set progressdialog title
//            progressDialog.setTitle("Your searching process is");
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

                specialityAdapter = new ArrayAdapter<String> (this, R.layout.custom_spinner, specialitiesList);
                specialityAdapter.setDropDownViewResource(R.layout.custom_spinner); // Specify the layout to use when the list of choices appears
                Speciality.setAdapter(specialityAdapter); // Apply the adapter to the spinner
            }

        }
        catch (JSONException e)
        {}
    }


    protected void buildAlertMessageNoGps() {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final android.app.AlertDialog alert = builder.create();
        alert.show();
    }


    private String formatDataAsJson()
    {
        JSONObject data = new JSONObject();

        try{
            data.put("Latitude",selectedCitylat);
            data.put("Longitude",selectedCitylong);
            data.put("Distance", dis);
            return data.toString();
//            }
        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }
    //Get diagnostics list from api call

    private class GetDiagnostics_N_List extends AsyncTask<String, Void, String> {

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Create a progressdialog
//            progressDialog = new ProgressDialog(GetCurrentDiagnosticsList11.this);
//            // Set progressdialog title
////            progressDialog.setTitle("Your searching process is");
//            // Set progressdialog message
//            progressDialog.setMessage("Loading...");
//
//            progressDialog.setIndeterminate(false);
//            // Show progressdialog
//            progressDialog.show();
//        }

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {
                System.out.println("dsfafssss....");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();

                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                Log.d("Service","Started");
                httpURLConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                System.out.println("params....."+params[1]);
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

            Log.e("TAG result current   ", result); // this is expecting a response code to be sent from your server upon receiving the POST data

            progressDialog.dismiss();

            try
            {
                JSONObject jsono = new JSONObject(result);
                String ss = (String) jsono.get("Message");
                if(ss.equals("No data found."))
                {
                    showMessage();
                    availabilityCount = 0;
                    System.out.println("medical availabilityCount...."+availabilityCount);

                    availability.setText(Integer.toString(availabilityCount));
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

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

        }
    }

    private void getData(String result) {
        try {

            JSONArray jarray = new JSONArray(result);

            availabilityCount = jarray.length();
            System.out.println("diag availabilityCount...."+availabilityCount);

            availability.setText(Integer.toString(availabilityCount));

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);

                String mobile = object.getString("MobileNumber");
                String diagId = object.getString("DiagnosticsID");

                System.out.println("d idds..."+diagId);

                String centerName = object.getString("CenterName");

                String cashOnHand = object.getString("CashOnHand");
                String creditDebit = object.getString("CreditDebit");
                String netBanking = object.getString("Netbanking");
                String paytm = object.getString("Paytm");

                String landLineNumber = object.getString("LandlineNo");
                String contactPerson = object.getString("ContactPerson");

                String mylatii= object.getString("Latitude");
                String mylongii = object.getString("Longitude");

                String emergencyService = "";

                if(object.has("EmergencyService"))
                {
                    emergencyService = object.getString("EmergencyService");
                }

                else
                {
                    emergencyService = "";
                }


                double myDistances  = distance(Double.parseDouble(mylatii),Double.parseDouble(mylongii),selectedCitylat,selectedCitylong);

                System.out.println("distance from current to ur selected location.in doc..."+myDistances);

                double dis = Math.round(myDistances*1000)/1000.000;
                myDistance = String.format("%.1f", dis)+" Km";
                System.out.println("dist decimal round...."+myDistance);

                String addressId = object.getString("AddressID");
                String centerImage = object.getString("CenterImage");

                DiagnosticsClass diagnosticsClass = new DiagnosticsClass(mobile,diagId,getUserId,centerName,cashOnHand,
                        creditDebit,paytm,netBanking,landLineNumber,contactPerson,mylatii,mylongii,myDistance,emergencyService,
                        addressId,centerImage);

                myList.add(diagnosticsClass);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        double mydist = dist/0.62137;//in kms
        return (mydist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public void showMessage(){

        android.support.v7.app.AlertDialog.Builder a_builder = new android.support.v7.app.AlertDialog.Builder(GetCurrentDiagnosticsList11.this);
        a_builder.setMessage("Not available for selected city")
                .setCancelable(false)
                .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Diagnostic Records");
        alert.show();
    }


    public void showalert()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(GetCurrentDiagnosticsList11.this);
        alert.setTitle("Do you want to take Appointment for Register user?");
        //  alert.show();
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(GetCurrentDiagnosticsList11.this, "YES", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(GetCurrentDiagnosticsList11.this, "NO", Toast.LENGTH_SHORT).show();
                alertDialog1.cancel();
            }
        });
        alert.setCancelable(false);
        alertDialog1 = alert.create();
        alertDialog1.setCanceledOnTouchOutside(false);
        alert.show();
    }

//    public void anotheralert()
//    {
//
//        MyDialoganother =  new Dialog(GetCurrentDiagnosticsList11.this);
//        MyDialoganother.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        MyDialoganother.setContentView(R.layout.range_layout);
//
//        seek_bar = (SeekBar) MyDialoganother.findViewById(R.id.seekbar);
//        seek_bar.setProgress(dis);
//
//        rangeBar();
//
//        distance = (TextView) MyDialoganother.findViewById(R.id.DistanceRange);
//
//        okBtn = (Button)MyDialoganother.findViewById(R.id.ok_btn);
//        cancelBtn = (Button)MyDialoganother.findViewById(R.id.cancel_btn);
//        okBtn.setEnabled(true);
//        cancelBtn.setEnabled(true);
//        okBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_LONG).show();
//
//                String js = specialityBasedFormatDataAsJson();
//                uploadServerUrl = baseUrl.getUrl()+"GetDiagnosticsInRange";
//
//                new GetDiagnostics_N_List().execute(uploadServerUrl,js.toString());
//
//                myList = new ArrayList<DiagnosticsClass>();
////
//                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//                recyclerView.setHasFixedSize(true);
//
//
//                recyclerView.setLayoutManager(layoutManager);
//                recyclerView.setAdapter(adapter);
//
//                MyDialog.dismiss();
//                MyDialoganother.dismiss();
//            }
//        });
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyDialog.dismiss();
//
//            }
//        });
//        MyDialoganother.setCancelable(false);
//        MyDialoganother.setCanceledOnTouchOutside(false);
//        MyDialoganother.show();
//    }

    public void rangeBar()
    {

        seek_bar = (SeekBar) findViewById(R.id.seekbar);
        distance = (TextView) findViewById(R.id.DistanceRange);

        seek_bar.setProgress(20);
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value = progress;
                System.out.println("progress...."+progress);
                distance.setText(progress+"Km") ;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(BloodBank.this,"SeekBar is in StartTracking",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                distance.setText(progress_value+"Km");
//                bw_dist.setText("Distance stop value :"+progress_value+"Km");
                dis = progress_value;
                System.out.println("dis.."+dis);
//                getlatlng();

                String js = specialityBasedFormatDataAsJson();
                uploadServerUrl = baseUrl.getUrl()+"GetDiagnosticsInRange";

                new GetDiagnostics_N_List().execute(uploadServerUrl,js.toString());

                myList = new ArrayList<DiagnosticsClass>();

                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);

                adapter = new DiagnosticsListAdapter(GetCurrentDiagnosticsList11.this, myList);
                layoutManager = new LinearLayoutManager(GetCurrentDiagnosticsList11.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }
        });

        distance.setText(seek_bar.getProgress()+"Km");


    }


    private void getlatlng()
    {
        if(Geocoder.isPresent())
        {
            try {
                selectedlocation = getcity;
                System.out.println("getlatlong method city....."+selectedlocation);
                Geocoder gc=new Geocoder(this);
                List<Address> addresses1=gc.getFromLocationName(selectedlocation,5);
                List<LatLng> l1=new ArrayList<>(addresses1.size());
                System.out.println("adresses1"+addresses1);
                for(Address a:addresses1){
                    if(a.hasLatitude() && a.hasLongitude()){
                        l1.add(new LatLng(a.getLatitude(),a.getLongitude()));
                    }
                }
                selectedCitylat = l1.get(0).latitude;
                selectedCitylong = l1.get(0).longitude;

//                adapter = new DiagnosticsListAdapter(this, myList);
//                layoutManager = new LinearLayoutManager(this);
//
//                String js = formatDataAsJson();
//                uploadServerUrl = baseUrl.getUrl()+"GetDiagnosticsInRange";
//
//                new GetDiagnostics_N_List().execute(uploadServerUrl,js.toString());
//
//                myList = new ArrayList<DiagnosticsClass>();
////
//                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//                recyclerView.setHasFixedSize(true);
//
//                recyclerView.setLayoutManager(layoutManager);
//                recyclerView.setAdapter(adapter);

//                getaddress(selectedCitylat,selectedCitylong);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String specialityBasedFormatDataAsJson()
    {
        JSONObject data = new JSONObject();

        try{
            data.put("Latitude",selectedCitylat);
            data.put("Longitude",selectedCitylong);
            data.put("Distance", dis);
            data.put("Speciality", Speciality.getSelectedItem().toString());
            return data.toString();
//            }
        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }


    public void getaddress(double lat,double lng)
    {
        geocoder=new Geocoder(getApplicationContext());

        try {
            fulladdress = geocoder.getFromLocation(lat, lng,1);
            System.out.println("full address"+fulladdress);

            if (fulladdress.isEmpty())
            {
//                        cityname.setTitle("waiting");
            }
            else
            {
                if(fulladdress.size()>0)
                {

                    addressline = fulladdress.get(0).getAddressLine(0);

                    System.out.println("address line..."+addressline);

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }

}
