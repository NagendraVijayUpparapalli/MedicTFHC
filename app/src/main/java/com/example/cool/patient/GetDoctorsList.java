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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetDoctorsList extends AppCompatActivity
{
    public static final CharSequence[] states = {"---Speciality---", "Head", "nose", "eyes"};
    Dialog MyDialog;
    Dialog MyDialoganother;
    Button okBtn,cancelBtn;
    AlertDialog alertDialog1;

    ProgressDialog progressDialog;

    private static SeekBar seek_bar;
    private static TextView distance,bw_dist;
    static int progress_value,dis = 20;

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

    ArrayList<DoctorClass> myList;
    DoctorListAdapter adapter;


    ListView listview;

    ApiBaseUrl baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_doctors_list);

        baseUrl = new ApiBaseUrl();

        current_city = (TextView) findViewById(R.id.select_city);
        current_city.setText(city);



//        String js = formatDataAsJson();
//        uploadServerUrl = baseUrl.getUrl()+"GetDoctorsInRange";
//
//        new GetDoctors_N_List().execute(uploadServerUrl,js.toString());
//
//        myList = new ArrayList<DoctorClass>();
////
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        recyclerView.setHasFixedSize(true);
//
//
//        adapter = new DoctorListAdapter(this, myList);
        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);

        current_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GetDoctorsList.this,SelectCity.class);
                i.putExtra("docList","doclist");
                startActivity(i);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        toolbar.setTitle("Doctors List");

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(BloodBank.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(GetDoctorsList.this,MainActivity.class);
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




//        CardView cardView = (CardView) findViewById(R.id.card_view);
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showalert();
//            }
//        });

        MyDialog =  new Dialog(GetDoctorsList.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.speciality_based_layout);

        Spinner spinner1 = (Spinner)MyDialog.findViewById(R.id.speciality);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, states);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        spinner1.setAdapter(adapter); // Apply the adapter to the spinner
        okBtn = (Button)MyDialog.findViewById(R.id.ok_btn);
        cancelBtn = (Button)MyDialog.findViewById(R.id.cancel_btn);
        okBtn.setEnabled(true);
        cancelBtn.setEnabled(true);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_LONG).show();
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
        if (ActivityCompat.checkSelfPermission(GetDoctorsList.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (GetDoctorsList.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.print("helo this is if");

            ActivityCompat.requestPermissions(GetDoctorsList.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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
                uploadServerUrl = baseUrl.getUrl()+"GetDoctorsInRange";

                new GetDoctors_N_List().execute(uploadServerUrl,js.toString());


                myList = new ArrayList<DoctorClass>();
//
                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);


//                adapter = new DoctorListAdapter(this, myList);
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

            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                System.out.print("latii...."+lattitude);
                System.out.print("longi...."+lattitude);

                String js = formatDataAsJson();
                uploadServerUrl = baseUrl.getUrl()+"GetDoctorsInRange";

                new GetDoctors_N_List().execute(uploadServerUrl,js.toString());


                myList = new ArrayList<DoctorClass>();

                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);

//                adapter = new DoctorListAdapter(this, myList);
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
                uploadServerUrl = baseUrl.getUrl()+"GetDoctorsInRange";

                new GetDoctors_N_List().execute(uploadServerUrl,js.toString());


                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);


//                adapter = new DoctorListAdapter(this, myList);
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

//                textView.setText( lattitude);
//                textView1.setText(longitude);
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
//            if(radioButton.getText().equals("MobileNumber"))
//            {
//                data.put("MobileNumber",mobile_num.getText().toString());
//                data.put("UPassword",password1.getText().toString());
//                data.put("LoginType",1);
//                return data.toString();
//            }
//            else {
            data.put("Latitude","17.0010464");
            data.put("Longitude","82.2113167");
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


    //Get doctors list from api call

    private class GetDoctors_N_List extends AsyncTask<String, Void, String> {


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

                String doctorId = object.getString("DoctorID");
                String addressId = object.getString("AddressID");
                String mobile = object.getString("MobileNumber");
                String firstName = object.getString("FirstName");
                String lastName = object.getString("LastName");

                String Name = "Dr. "+firstName+" "+lastName;

                String qualification = object.getString("Qualification");
                String specialityName = object.getString("SpecialityName");
                String doctorImage = object.getString("DoctorImage");
                String experience = object.getString("Experience");
                String latitude = object.getString("Latitude");
                String longitude = object.getString("Longitude");
                String emergencyService = object.getString("EmergencyService");
                String consultationFee = "100";
                String consultationPrice = "100";
                String cashonHand = object.getString("CashOnHand");
                String creditDebit = object.getString("CreditDebit");
                String netBanking = object.getString("Netbanking");
                String paytm = object.getString("Paytm");


//                DoctorClass doctorClass = new DoctorClass(doctorId,addressId,mobile,Name,qualification,specialityName,
//                        doctorImage,experience,latitude,longitude,emergencyService,consultationFee,consultationPrice,cashonHand,creditDebit,netBanking,paytm);

//                doctorClass.setDoctorId(object.getString("DoctorID"));
//                doctorClass.setAddressId(object.getString("AddressID"));
//
//                String firstName = object.getString("FirstName");
//                String lastName = object.getString("LastName");
//
//                doctorClass.setName("Dr. "+firstName+" "+lastName);
//                doctorClass.setQualification(object.getString("Qualification"));
//                doctorClass.setSpecialityName(object.getString("SpecialityName"));
//                doctorClass.setDoctorImage(object.getString("DoctorImage"));
//
//                doctorClass.setExperience((object.getString("Experience")));
//
//                doctorClass.setLatitude((object.getString("Latitude")));
//                doctorClass.setLongitude((object.getString("Longitude")));
//                doctorClass.setEmergencyService(object.getString("EmergencyService"));
//
//                doctorClass.setConsultationPrice(object.getString("ConsultationFee"));
//
//                doctorClass.setCashonHand(object.getString("CashOnHand"));
//                doctorClass.setCreditDebit(object.getString("CreditDebit"));
//                doctorClass.setNetBanking(object.getString("Netbanking"));
//                doctorClass.setPaytm(object.getString("Paytm"));

//                myList.add(doctorClass);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void showMessage(){

        android.support.v7.app.AlertDialog.Builder a_builder = new android.support.v7.app.AlertDialog.Builder(GetDoctorsList.this);
        a_builder.setMessage("Doctors are not available for selected city")
                .setCancelable(false)
                .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Doctors Records");
        alert.show();
    }


    public void showalert()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(GetDoctorsList.this);
        alert.setTitle("Do you want to take Appointment for Register user?");
        //  alert.show();
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(GetDoctorsList.this, "YES", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(GetDoctorsList.this, "NO", Toast.LENGTH_SHORT).show();
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

        MyDialoganother =  new Dialog(GetDoctorsList.this);
        MyDialoganother.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialoganother.setContentView(R.layout.range_layout);

        seek_bar = (SeekBar) MyDialoganother.findViewById(R.id.seekbar);
        seek_bar.setProgress(dis);

        rangeBar();

        distance = (TextView) MyDialoganother.findViewById(R.id.DistanceRange);

        okBtn = (Button)MyDialoganother.findViewById(R.id.ok_btn);
        cancelBtn = (Button)MyDialoganother.findViewById(R.id.cancel_btn);
        okBtn.setEnabled(true);
        cancelBtn.setEnabled(true);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_LONG).show();

                String js = formatDataAsJson();
                uploadServerUrl = baseUrl.getUrl()+"GetDoctorsInRange";

                new GetDoctors_N_List().execute(uploadServerUrl,js.toString());

                myList = new ArrayList<DoctorClass>();
//
                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);


//                adapter = new DoctorListAdapter(this, myList);
//                layoutManager = new LinearLayoutManager(this);
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

        seek_bar.setProgress(dis);
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

//        String js = formatDataAsJson();
//        uploadServerUrl = baseUrl.getUrl()+"GetDoctorsInRange";
//
//        new GetDoctors_N_List().execute(uploadServerUrl,js.toString());
//
//        myList = new ArrayList<DoctorClass>();
//
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        recyclerView.setHasFixedSize(true);
//
//
//        adapter = new DoctorListAdapter(this, myList);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);

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

//                getaddress(selectedCitylat,selectedCitylong);


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