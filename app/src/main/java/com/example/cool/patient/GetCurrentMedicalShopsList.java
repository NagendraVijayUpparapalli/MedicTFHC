package com.example.cool.patient;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import android.widget.ListView;

public class GetCurrentMedicalShopsList extends AppCompatActivity {

    public static final CharSequence[] states = {"---Speciality---", "Head", "nose", "eyes"};
    Dialog MyDialog;
    Dialog MyDialoganother;
    Button okBtn,cancelBtn;
    AlertDialog alertDialog1;

    private static SeekBar seek_bar;
    private static TextView distance,bw_dist;
    static int progress_value,dis = 20;

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

    static String selectedlocation=null;

    String addressline,mobile,email,pincode,city,state;
    TextView current_city;

    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    ArrayList<MedicalShopClass> myList;
    MedicalShopListAdapter adapter;


    ListView listview;

    ApiBaseUrl baseUrl;
    String getUserId,usermobileNumber;
    TextView userId;

    List<String>pharmacyTypeList;
    HashMap<Long, String> mypharmacyTypeList = new HashMap<>();
    ArrayAdapter<String> adapter5;
    SearchableSpinner pharmacyType;

    String cur_addressId,mydoctorId,myaddressId,mydocName,myhospitalName,myaddress,mycity,mystate,myfee,mypaymentMode,myphone,myLati,myLongi,myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_current_medical_shops_list);

        baseUrl = new ApiBaseUrl();

        current_city = (TextView) findViewById(R.id.select_city);
        current_city.setText(city);

        new GetAllMedicalPharmacyList().execute(baseUrl.getUrl()+"GetPharmacyType");

        getUserId = getIntent().getStringExtra("userId");
        System.out.print("userid in get medical list....."+getUserId);

        myList = new ArrayList<MedicalShopClass>();

        current_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GetCurrentMedicalShopsList.this,SelectCity.class);
                i.putExtra("module","medicalList");
                i.putExtra("userId",getUserId);
                i.putExtra("mobile",getIntent().getStringExtra("mobile"));
                startActivity(i);
            }
        });


        usermobileNumber = getIntent().getStringExtra("mobile");

        myList = new ArrayList<MedicalShopClass>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Medical List");

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(BloodBank.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(GetCurrentMedicalShopsList.this,MainActivity.class);
                        intent.putExtra("id",getUserId);
                        intent.putExtra("mobile",usermobileNumber);
                        startActivity(intent);

                    }
                }

        );

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        MyDialog =  new Dialog(GetCurrentMedicalShopsList.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.speciality_based_layout);

        pharmacyType = (SearchableSpinner)MyDialog.findViewById(R.id.speciality);

        okBtn = (Button)MyDialog.findViewById(R.id.ok_btn);
        cancelBtn = (Button)MyDialog.findViewById(R.id.cancel_btn);
        okBtn.setEnabled(true);
        cancelBtn.setEnabled(true);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_LONG).show();
                anotheralert();

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anotheralert();
            }
        });
        MyDialog.setCancelable(false);
        MyDialog.setCanceledOnTouchOutside(false);
        MyDialog.show();

    }

    //Get MedicalPharmacyList list from api call
    private class GetAllMedicalPharmacyList extends AsyncTask<String, Void, String> {

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
            getPharmacyList(result);

        }
    }
    private void getPharmacyList(String result) {

        System.out.print("outside for");

        try
        {
            System.out.print("in try");

            JSONArray jsonArr = new JSONArray(result);
            pharmacyTypeList = new ArrayList<>();

            for (int i = 0; i < jsonArr.length(); i++) {

                System.out.print("in for");

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                Long pharmacyKey = jsonObj.getLong("Key");
                String pharmacyValue = jsonObj.getString("Value");

                System.out.print("key..."+pharmacyKey);
                System.out.print("value..."+pharmacyValue);

                mypharmacyTypeList.put(pharmacyKey,pharmacyValue);

                System.out.print("mypharmacyTypeList map.."+mypharmacyTypeList);

                pharmacyTypeList.add(jsonObj.getString("Value"));

                System.out.print("pharmacyTypeList.."+pharmacyTypeList);
            }

            adapter5 = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, pharmacyTypeList);
            adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
            pharmacyType.setAdapter(adapter5); // Apply the adapter to the spinner

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



    private void getLocation() {
        System.out.print("helo this is method");
        if (ActivityCompat.checkSelfPermission(GetCurrentMedicalShopsList.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (GetCurrentMedicalShopsList.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.print("helo this is if");

            ActivityCompat.requestPermissions(GetCurrentMedicalShopsList.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            System.out.print("helo this is else");

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                System.out.print("latii...."+lattitude);
                System.out.print("longi...."+longitude);


                String js = formatDataAsJson();
                uploadServerUrl = baseUrl.getUrl()+"GetMedicalShopsInRange";

                new GetMedicalShops_N_List().execute(uploadServerUrl,js.toString());

                myList = new ArrayList<MedicalShopClass>();
//
                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);


                adapter = new MedicalShopListAdapter(this, myList);
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                geocoder=new Geocoder(getApplicationContext());

                try {
                    addresses = geocoder.getFromLocation(latti, longi,1);
                    System.out.println("addresses"+addresses);

                    if (addresses.isEmpty())
                    {
//                        cityname.setTitle("waiting");
                        current_city.setText("Waiting");
                    }
                    else
                    {
                        if(addresses.size()>0)
                        {
                            city=addresses.get(0).getLocality();
                            current_city.setText(city);
//                            cityname.setTitle(city);
                            System.out.println("city name..."+city);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }

            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                System.out.print("latii...."+lattitude);
                System.out.print("longi...."+lattitude);

                String js = formatDataAsJson();
                uploadServerUrl = baseUrl.getUrl()+"GetMedicalShopsInRange";

                new GetMedicalShops_N_List().execute(uploadServerUrl,js.toString());

                myList = new ArrayList<MedicalShopClass>();

                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);

                adapter = new MedicalShopListAdapter(this, myList);
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

//                textView.setText("latitude"+lattitude);
//                textView1.setText("longitude"+longitude);

                geocoder=new Geocoder(getApplicationContext());

                try {
                    addresses = geocoder.getFromLocation(latti, longi,1);
                    System.out.println("addresses"+addresses);

                    if (addresses.isEmpty())
                    {
//                        cityname.setTitle("waiting");
                        current_city.setText("Waiting");
                    }
                    else
                    {
                        if(addresses.size()>0)
                        {
                            city=addresses.get(0).getLocality();
                            current_city.setText(city);
//                            cityname.setTitle(city);
                            System.out.println("city name"+city);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }

            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                System.out.print("latii...."+lattitude);
                System.out.print("longi...."+lattitude);

                String js = formatDataAsJson();
                uploadServerUrl = baseUrl.getUrl()+"GetMedicalShopsInRange";

                new GetMedicalShops_N_List().execute(uploadServerUrl,js.toString());

                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);


                adapter = new MedicalShopListAdapter(this, myList);
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                geocoder=new Geocoder(getApplicationContext());

                try {
                    addresses = geocoder.getFromLocation(latti, longi,1);
                    System.out.println("addresses"+addresses);

                    if (addresses.isEmpty())
                    {
//                        cityname.setTitle("waiting");
                        current_city.setText("Waiting");
                    }
                    else
                    {
                        if(addresses.size()>0)
                        {
                            city=addresses.get(0).getLocality();
                            current_city.setText(city);
//                            cityname.setTitle(city);
                            System.out.println("city name"+city);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }

            }else{

                Toast.makeText(this,"Unable to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    private String formatDataAsJson()
    {
        JSONObject data = new JSONObject();

        try{
            data.put("Latitude",lattitude);
            data.put("Longitude",longitude);
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

    private String specialityBasedFormatDataAsJson()
    {
        JSONObject data = new JSONObject();

        try{
            data.put("Latitude",lattitude);
            data.put("Longitude",longitude);
            data.put("Distance", dis);
            data.put("Speciality", pharmacyType.getSelectedItem().toString());
            return data.toString();
//            }
        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }

    //Get MedicalShops list from api call
    private class GetMedicalShops_N_List extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(GetCurrentMedicalShopsList.this);
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

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);

                String  MedicalID = object.getString("MedicalShopID");


                System.out.print("MedicalIDshopID....."+MedicalID);
                String addressId = object.getString("AddressID");
                System.out.print("addressIDmedicalshop" +addressId );

                String mobile = object.getString("MobileNumber");
//

                String ShopName = object.getString("ShopName");
                String ContactPerson = object.getString("ContactPerson");

                String LandlineNo = object.getString("LandlineNo");

                String medicImage = object.getString("ShopImage");

                String latitude = object.getString("Latitude");
                String longitude = object.getString("Longitude");

                String emergencyService = "";

                if(object.has("EmergencyService"))
                {
                     emergencyService = object.getString("EmergencyService");
                }
                else
                {
                     emergencyService = "";
                }


                String cashonHand = object.getString("CashOnHand");
                String creditDebit = object.getString("CreditDebit");
                String netBanking = object.getString("Netbanking");
                String paytm = object.getString("Paytm");


                MedicalShopClass medicalClass = new MedicalShopClass(MedicalID,addressId,getUserId,usermobileNumber,mobile,ShopName,ContactPerson,LandlineNo,
                        medicImage,latitude,longitude,emergencyService,cashonHand,creditDebit,netBanking,paytm);

                myList.add(medicalClass);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void showMessage(){

        android.support.v7.app.AlertDialog.Builder a_builder = new android.support.v7.app.AlertDialog.Builder(GetCurrentMedicalShopsList.this);
        a_builder.setMessage("Not available for selected city")
                .setCancelable(false)
                .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Medical Shop Records");
        alert.show();
    }


    public void showalert()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(GetCurrentMedicalShopsList.this);
        alert.setTitle("Do you want to take Appointment for Register user?");
        //  alert.show();
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
//                Toast.makeText(GetCurrentMedicalShopsList.this, "YES", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
//                Toast.makeText(GetCurrentMedicalShopsList.this, "NO", Toast.LENGTH_SHORT).show();
                alertDialog1.cancel();
            }
        });
        alert.setCancelable(false);
        alertDialog1 = alert.create();
        alertDialog1.setCanceledOnTouchOutside(false);
        alert.show();
    }

    public void anotheralert()
    {

        MyDialoganother =  new Dialog(GetCurrentMedicalShopsList.this);
        MyDialoganother.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialoganother.setContentView(R.layout.range_layout);

        seek_bar = (SeekBar) MyDialoganother.findViewById(R.id.seekbar);
        seek_bar.setProgress(dis);

        rangeBar();


        adapter = new MedicalShopListAdapter(this, myList);
        layoutManager = new LinearLayoutManager(this);

        distance = (TextView) MyDialoganother.findViewById(R.id.DistanceRange);

        okBtn = (Button)MyDialoganother.findViewById(R.id.ok_btn);
        cancelBtn = (Button)MyDialoganother.findViewById(R.id.cancel_btn);
        okBtn.setEnabled(true);
        cancelBtn.setEnabled(true);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_LONG).show();

                String js = specialityBasedFormatDataAsJson();
                uploadServerUrl = baseUrl.getUrl()+"GetMedicalShopsInRange";

                new GetMedicalShops_N_List().execute(uploadServerUrl,js.toString());

                myList = new ArrayList<MedicalShopClass>();
//
                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);


                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                MyDialog.dismiss();
                MyDialoganother.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.show();

            }
        });
        MyDialoganother.setCancelable(false);
        MyDialoganother.setCanceledOnTouchOutside(false);
        MyDialoganother.show();
    }

    public void rangeBar()
    {

        seek_bar = (SeekBar) MyDialoganother.findViewById(R.id.seekbar);
        distance = (TextView) MyDialoganother.findViewById(R.id.DistanceRange);

        adapter = new MedicalShopListAdapter(this, myList);
        layoutManager = new LinearLayoutManager(this);

        seek_bar.setProgress(20);
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value = progress;
                System.out.println("progress...."+progress);
                distance.setText("Distance in progress :"+progress+"Km") ;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(BloodBank.this,"SeekBar is in StartTracking",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                distance.setText("Distance :"+progress_value+"Km");
//                bw_dist.setText("Distance stop value :"+progress_value+"Km");
                dis = progress_value;
                System.out.println("dis.."+dis);
                getlatlng();

            }
        });

        distance.setText("Distance :"+seek_bar.getProgress()+"Km");

        String js = specialityBasedFormatDataAsJson();
        uploadServerUrl = baseUrl.getUrl()+"GetMedicalShopsInRange";

        new GetMedicalShops_N_List().execute(uploadServerUrl,js.toString());

        myList = new ArrayList<MedicalShopClass>();
//
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }


    private void getlatlng()
    {
        if(Geocoder.isPresent())
        {
            try {
                selectedlocation = city;
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

                String js = specialityBasedFormatDataAsJson();
                uploadServerUrl = baseUrl.getUrl()+"GetMedicalShopsInRange";

                new GetMedicalShops_N_List().execute(uploadServerUrl,js.toString());

                myList = new ArrayList<MedicalShopClass>();
//
                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);


                adapter = new MedicalShopListAdapter(this, myList);
                layoutManager = new LinearLayoutManager(this);

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
