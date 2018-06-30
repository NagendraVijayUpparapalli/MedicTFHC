package com.example.cool.patient.patient.ViewBloodBanksList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ListView;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.common.ChangePassword;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.patient.MyDiagnosticAppointments.PatientMyDiagnosticAppointments;
import com.example.cool.patient.patient.MyDoctorAppointments.PatientMyDoctorAppointments;
import com.example.cool.patient.patient.PatientDashBoard;
import com.example.cool.patient.R;
import com.example.cool.patient.common.SelectCity;
import com.example.cool.patient.patient.PatientEditProfile;
import com.example.cool.patient.patient.PatientSideNavigationExpandableListAdapter;
import com.example.cool.patient.patient.PatientSideNavigationExpandableSubList;
import com.example.cool.patient.patient.ViewDiagnosticsList.GetCurrentDiagnosticsList;
import com.example.cool.patient.patient.ViewDoctorsList.GetCurrentDoctorsList;
import com.google.android.gms.maps.model.LatLng;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class BloodBank1 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //lat,long
    static String uploadServerUrl = null;
    LocationManager locationManager;
    String lattitude,longitude;
    static double lat,lng;
    Geocoder geocoder;
    List<Address> addresses;
    List<Address> fulladdress;
    private static final int REQUEST_LOCATION = 1;

    private static SeekBar seek_bar;
    private static TextView distance,availability;
    int progress_value,dis=20,availabilityCount;


    static double selectedCitylat;
    static double selectedCitylong;

    TextView selected_city;
    ListView listview;

    static String selected_location=null;
    static String getUserId,getcity=null,mobile;
    String addressline;

    ArrayList<BloodBankClass> arrayList;

    BloodBankAdapter adapter1;
    ApiBaseUrl baseUrl;

    // expandable list view

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    //sidenav fields
    TextView sidenavName,sidenavEmail,sidenavAddress,sidenavMobile,sidenavBloodgroup;

    FloatingActionButton homebutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank1);

        baseUrl = new ApiBaseUrl();

        selected_city = (TextView) findViewById(R.id.select_city);
        listview = (ListView)findViewById(R.id.mylist);

        getUserId = getIntent().getStringExtra("userId");
        getcity = getIntent().getStringExtra("city");
        mobile = getIntent().getStringExtra("mobile");
        System.out.print("city....."+getcity);
        selected_city.setText(getcity);

        new GetPatientDetails().execute(baseUrl.getUrl()+"GetPatientByID"+"?ID="+getUserId);

//        selected_location = selected_city.getText().toString();

        selected_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BloodBank1.this,SelectCity.class);
                i.putExtra("module","bloodbankList");
                i.putExtra("userId",getUserId);
                i.putExtra("mobile",mobile);
                startActivity(i);
            }
        });

        seek_bar = (SeekBar) findViewById(R.id.seekbar);
        distance = (TextView) findViewById(R.id.DistanceRange);
//        bw_dist = (TextView) findViewById(R.id.between_dist);
        availability = (TextView) findViewById(R.id.availability);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
//        else
//        {
        getlatlng();
//        }


        uploadServerUrl = baseUrl.getUrl()+"GetBloodBankDetails?Latitude="+lattitude+"&Longitude="+longitude+"&Distance="+dis;
        new GetBloodBankDetails().execute(uploadServerUrl);

        arrayList = new ArrayList<BloodBankClass>();

        listview = (ListView)findViewById(R.id.mylist);

        Collections.sort(arrayList, new Comparator<BloodBankClass>(){
            public int compare(BloodBankClass obj1, BloodBankClass obj2) {
                return obj1.getDistance().compareTo(obj2.getDistance());
            }
        });
//        adapter1.notifyDataSetChanged();


        adapter1 = new BloodBankAdapter(getApplicationContext(), R.layout.row, arrayList);

        listview.setAdapter(adapter1);

        adapter1.notifyDataSetChanged();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {
                // TODO Auto-generated method stub
//                Toast.makeText(getApplicationContext(), arrayList.get(position).getName(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(BloodBank1.this,ViewBloodBank.class);

                lat = Double.parseDouble(arrayList.get(position).getLati());
                lng = Double.parseDouble(arrayList.get(position).getLongi());

                System.out.println("lattttt...."+lat);
                System.out.println("lattttt...."+lng);
                getaddress(lat,lng);
                String lt = String.valueOf(lat);
                String lg = String.valueOf(lng);

                i.putExtra("image",arrayList.get(position).getImage());
                i.putExtra("name",arrayList.get(position).getName());
                i.putExtra("lati",lt);
                i.putExtra("longi",lg);
                i.putExtra("city",arrayList.get(position).getLocation());
                i.putExtra("person_name",arrayList.get(position).getContact_person());
                i.putExtra("phone",arrayList.get(position).getMobile());
                i.putExtra("email","NA");
                i.putExtra("addressline",addressline);
                i.putExtra("mobile",mobile);
                i.putExtra("id",getUserId);
                startActivity(i);

            }
        });

        arrayList = new ArrayList<BloodBankClass>();
        listview = (ListView)findViewById(R.id.mylist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Blood Bank");

//        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Toast.makeText(BloodBank.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(BloodBank1.this,PatientDashBoard.class);
//                        intent.putExtra("id",getUserId);
//                        intent.putExtra("mobile",mobile);
//                        startActivity(intent);
//
//                    }
//                }
//
//        );


        //home button
        homebutton = (FloatingActionButton) findViewById(R.id.home);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BloodBank1.this,PatientDashBoard.class);
                intent.putExtra("id",getUserId);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
            }
        });

        // side navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);

        sidenavName = (TextView) headerLayout.findViewById(R.id.name);
        sidenavAddress = (TextView) headerLayout.findViewById(R.id.address);
        sidenavMobile = (TextView) headerLayout.findViewById(R.id.mobile);
        sidenavEmail = (TextView) headerLayout.findViewById(R.id.email);
        sidenavBloodgroup = (TextView) headerLayout.findViewById(R.id.bloodgroup);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = PatientSideNavigationExpandableSubList.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new PatientSideNavigationExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                boolean retVal = true;

                if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM1) {
                    retVal = false;
                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM2) {
                    retVal = false;
                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM3) {
                    retVal = false;

                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM4) {
                    // call some activity here
                    Intent editProfile = new Intent(BloodBank1.this,PatientEditProfile.class);
                    editProfile.putExtra("id",getUserId);
                    startActivity(editProfile);

                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                    Intent contact = new Intent(BloodBank1.this,AboutUs.class);
                    startActivity(contact);

                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here

                    Intent contact = new Intent(BloodBank1.this,ReachUs.class);
                    startActivity(contact);


                }
                else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(BloodBank1.this,Login.class);
                    startActivity(contact);

                }

                return retVal;
            }
        });

//        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupClickListener() {
//
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                boolean retVal = true;
//
//                if (groupPosition == CustomExpandableListAdapter.ITEM1) {
//                    retVal = false;
//                } else if (groupPosition == CustomExpandableListAdapter.ITEM2) {
//                    retVal = false;
//                } else if (groupPosition == CustomExpandableListAdapter.ITEM3) {
//
//                    // call some activity here
//                } else if (groupPosition == CustomExpandableListAdapter.ITEM4) {
//                    // call some activity here
//
//                }
//                return retVal;
//            }
//        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM1) {
                    if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_1) {

                        Intent i = new Intent(BloodBank1.this,GetCurrentDoctorsList.class);
                        i.putExtra("userId",getUserId);
                        i.putExtra("mobile",mobile);
                        startActivity(i);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_2) {
                        Intent i = new Intent(BloodBank1.this,GetCurrentDiagnosticsList.class);
                        i.putExtra("userId",getUserId);
                        i.putExtra("mobile",mobile);
                        startActivity(i);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_3) {

                        // call activity here

//                        Intent in = new Intent(PatientDashBoard.this,GetCurrentMedicalShopsList.class);
//                        in.putExtra("userId",getUserId);
//                        in.putExtra("mobile",mobile_number);
//                        startActivity(in);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_4) {

                        // call activity here

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_5) {

                        // call activity here
//                        Intent bloodbank = new Intent(PatientDashBoard.this,BloodBank.class);
//                        startActivity(bloodbank);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_6) {

                        // call activity here

                    }

                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM3) {

                    if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM3_1) {

                        // call activity here
                        Intent intent = new Intent(BloodBank1.this,ChangePassword.class);
                        intent.putExtra("mobile",mobile);
                        startActivity(intent);


                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                }

                else if(groupPosition == PatientSideNavigationExpandableListAdapter.ITEM2) {
                    if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_1) {

                        // call activity here

                        Intent intent = new Intent(BloodBank1.this,PatientMyDoctorAppointments.class);
                        intent.putExtra("mobile",mobile);
                        intent.putExtra("id",getUserId);
                        startActivity(intent);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent intent = new Intent(BloodBank1.this,PatientMyDiagnosticAppointments.class);
                        intent.putExtra("mobile",mobile);
                        intent.putExtra("id",getUserId);
                        startActivity(intent);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_3) {

                        // call activity here

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_4) {

                        // call activity here

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_5) {

                        // call activity here

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_6) {

                        // call activity here

                    }
                }
                return true;

            }
        });

        rangeBar();

    }

    //Get patient list based on id from api call
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

            Log.e("TAG result    ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getProfileDetails(result);

        }
    }

    private void getProfileDetails(String result) {

        try
        {
            JSONObject js = new JSONObject(result);

            String myName = (String) js.get("FirstName");
            String mySurname = (String) js.get("LastName");
            String myMobile = (String) js.get("MobileNumber");
            String myEmail = (String) js.get("EmailID");
            String myAddress1 = (String) js.get("Address1");
            String myAddress2 = (String) js.get("Address2");
            String myBlood_group = (String) js.get("BloodGroup");

//                TextView sidenavName,sidenavEmail,sidenavAddress,sidenavMobile;

            sidenavName.setText(mySurname+" "+myName);
            sidenavMobile.setText(myMobile);
            sidenavEmail.setText(myEmail);
            sidenavBloodgroup.setText(myBlood_group);


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    public void showMessage(){

        android.support.v7.app.AlertDialog.Builder a_builder = new android.support.v7.app.AlertDialog.Builder(BloodBank1.this);
        a_builder.setMessage("Blood Banks are not available for selected city")
                .setCancelable(false)
                .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = a_builder.create();
        alert.setTitle("BloodBanks Records");
        alert.show();

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
                    if(fulladdress.size()>0)
                    {

                        addressline = fulladdress.get(0).getAddressLine(0);

                        System.out.println("address line..."+addressline);
                    }
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void getlatlng()
    {
        if(Geocoder.isPresent())
        {
            try {
                selected_location = getcity;
                System.out.println("getlatlong method city....."+selected_location);
                Geocoder gc=new Geocoder(this);
                List<Address> addresses1=gc.getFromLocationName(selected_location,5);
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

//                uploadServerUrl = "https://medictfhc.com/mapi/GetBloodBankDetails?Latitude=17.717100&Longitude=83.309200&Distance=5";
                uploadServerUrl = baseUrl.getUrl()+"GetBloodBankDetails?Latitude="+selectedCitylat+"&Longitude="+selectedCitylong+"&Distance="+dis;
                new GetBloodBankDetails().execute(uploadServerUrl);
                arrayList = new ArrayList<BloodBankClass>();
                listview = (ListView)findViewById(R.id.mylist);

//                Collections.sort(arrayList, new CustomComaprator());
//                adapter1 = new BloodBankAdapter(this,str,arrayList);


                Collections.sort(arrayList, new Comparator<BloodBankClass>(){
                    public int compare(BloodBankClass obj1, BloodBankClass obj2) {
                        return obj1.getDistance().compareTo(obj2.getDistance());
                    }
                });
//                adapter1.notifyDataSetChanged();


                adapter1 = new BloodBankAdapter(getApplicationContext(), R.layout.row, arrayList);

                listview.setAdapter(adapter1);
                adapter1.notifyDataSetChanged();

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                            long id) {
                        // TODO Auto-generated method stub
//                        Toast.makeText(getApplicationContext(), arrayList.get(position).getName(), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(BloodBank1.this,ViewBloodBank.class);
                        Bundle bundle = new Bundle();

                        lat = Double.parseDouble(arrayList.get(position).getLati());
                        lng = Double.parseDouble(arrayList.get(position).getLongi());

                        System.out.println("lattttt...."+lat);
                        System.out.println("longiii...."+lng);

//                        Toast.makeText(getApplicationContext(), String.valueOf(lat), Toast.LENGTH_LONG).show();
                        getaddress(lat,lng);

                        String lt = String.valueOf(lat);
                        String lg = String.valueOf(lng);

//                        bundle.putInt("image", arrayList.get(position).getImage());
                        i.putExtra("image",arrayList.get(position).getImage());
                        i.putExtra("name",arrayList.get(position).getName());
                        i.putExtra("person_name",arrayList.get(position).getContact_person());
                        i.putExtra("lati",lt);
                        i.putExtra("longi",lg);
                        i.putExtra("phone",arrayList.get(position).getMobile());
                        i.putExtra("city",arrayList.get(position).getLocation());
                        i.putExtra("email","medic@gmail.com");
                        i.putExtra("mobile",mobile);
                        i.putExtra("id",getUserId);
                        i.putExtra("addressline",addressline);
//                        i.putExtras(bundle);
                        startActivity(i);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    //Get bloodbanks list from api call
    private class GetBloodBankDetails extends AsyncTask<String, Void, String> {

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
                System.out.println("params bb...."+params[0]);
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
                    availabilityCount = 0;
                    System.out.println("bb availabilityCount...."+availabilityCount);

                    availability.setText(Integer.toString(availabilityCount));

                    showMessage();
                    Log.e("Api response if.....", result);
                }
                else
                {
                    getData(result);
                    adapter1.notifyDataSetChanged();
                    Log.e("Api response else.....", result);
                }
            }
            catch (Exception e)
            {}
            getData(result);
            adapter1.notifyDataSetChanged();
//            Log.e("Api response.....", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }

    private void getData(String result) {
        try {
            JSONObject jsono = new JSONObject(result);
//            if(jsono.get("Message").equals("No data found."))
//            {
//                showMessage();
//            }
//            else
//            {
            JSONArray jarray = jsono.getJSONArray("BloodbankList");

            availabilityCount = jarray.length();
            System.out.println("bb availabilityCount...."+availabilityCount);

            availability.setText(Integer.toString(availabilityCount));

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);
                BloodBankClass bb = new BloodBankClass();

                bb.setName(object.getString("BloodBankName"));
                bb.setMobile(object.getString("MobileNumber"));
                bb.setLocation(getcity);
                bb.setContact_person(object.getString("ContactPerson"));
                bb.setLat((object.getString("Latitude")));
                bb.setLng((object.getString("Longitude")));
                //bb.setAvailability("Yes");
                bb.setDistance(object.getString("distance"));
                bb.setImage(object.getString("BloodbankImage"));

                arrayList.add(bb);
            }

//            adapter1 = new BloodBankAdapter(getApplicationContext(), R.layout.row, arrayList);
//                 listview.setAdapter(adapter1);
//                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//                                            long id) {
//                        // TODO Auto-generated method stub
//                        Toast.makeText(getApplicationContext(), arrayList.get(position).getName(), Toast.LENGTH_LONG).show();
//                    }
//                });


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void rangeBar()
    {

//        bw_dist.setText("Distance :"+dis+"Km");
//        bw_dist.setText("Distance :"+seek_bar.getProgress()+"Km");

        seek_bar.setProgress(dis);

        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value = progress;
                System.out.println("progress...."+progress);
                distance.setText(progress+" Km") ;

//                bw_dist.setText("Distance :"+progress+"Km");

//                Toast.makeText(BloodBank.this,"SeekBar is in progress",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(BloodBank.this,"SeekBar is in StartTracking",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                distance.setText(progress_value+" Km");
//                bw_dist.setText("Distance stop value :"+progress_value+"Km");
                dis = progress_value;
                System.out.println("dis.."+dis);
//                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                    buildAlertMessageNoGps();
//
//                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                    getLocation();
//                }

                getlatlng();
//                Toast.makeText(BloodBank.this,dis,Toast.LENGTH_LONG).show();
            }
        });

        distance.setText(seek_bar.getProgress()+" Km");

        uploadServerUrl = "http://meditfhc.com/mapi/GetBloodBankDetails?Latitude="+lattitude+"&Longitude="+longitude+"&Distance="+dis;
        new GetBloodBankDetails().execute(uploadServerUrl);

        arrayList = new ArrayList<BloodBankClass>();

        listview = (ListView)findViewById(R.id.mylist);

        Collections.sort(arrayList, new Comparator<BloodBankClass>(){
            public int compare(BloodBankClass obj1, BloodBankClass obj2) {
                return obj1.getDistance().compareTo(obj2.getDistance());
            }
        });
//        adapter1.notifyDataSetChanged();


        adapter1 = new BloodBankAdapter(getApplicationContext(), R.layout.row, arrayList);

        listview.setAdapter(adapter1);

        adapter1.notifyDataSetChanged();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {
                // TODO Auto-generated method stub
//                Toast.makeText(getApplicationContext(), arrayList.get(position).getName(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(BloodBank1.this,ViewBloodBank.class);

                lat = Double.parseDouble(arrayList.get(position).getLati());
                lng = Double.parseDouble(arrayList.get(position).getLongi());

                System.out.println("lattttt...."+lat);
                System.out.println("lattttt...."+lng);
                getaddress(lat,lng);
                String lt = String.valueOf(lat);
                String lg = String.valueOf(lng);

                i.putExtra("image",arrayList.get(position).getImage());
                i.putExtra("name",arrayList.get(position).getName());
                i.putExtra("lati",lt);
                i.putExtra("longi",lg);
                i.putExtra("city",arrayList.get(position).getLocation());
                i.putExtra("person_name",arrayList.get(position).getContact_person());
                i.putExtra("phone",arrayList.get(position).getMobile());
                i.putExtra("email","NA");
                i.putExtra("addressline",addressline);
                i.putExtra("mobile",mobile);
                i.putExtra("id",getUserId);
                startActivity(i);

            }
        });

    }
}
