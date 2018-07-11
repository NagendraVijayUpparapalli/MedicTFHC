package com.example.cool.patient.patient.ViewDiagnosticsList;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.common.ChangePassword;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.common.Offers;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.diagnostic.DashBoardCalendar.DiagnosticDashboard;
import com.example.cool.patient.patient.MyDiagnosticAppointments.PatientMyDiagnosticAppointments;
import com.example.cool.patient.patient.MyDoctorAppointments.PatientMyDoctorAppointments;
import com.example.cool.patient.patient.MyFamily;
import com.example.cool.patient.patient.PatientDashBoard;
import com.example.cool.patient.R;
import com.example.cool.patient.common.SelectCity;
import com.example.cool.patient.patient.PatientEditProfile;
import com.example.cool.patient.patient.PatientSideNavigationExpandableListAdapter;
import com.example.cool.patient.patient.PatientSideNavigationExpandableSubList;
import com.example.cool.patient.patient.ViewBloodBanksList.BloodBank;
import com.example.cool.patient.patient.ViewDoctorsList.GetCurrentDoctorsList;
import com.example.cool.patient.patient.ViewMedicalShopsList.GetCurrentMedicalShopsList;
import com.google.android.gms.maps.model.LatLng;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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
import java.util.Map;

import android.widget.ListView;

public class GetCurrentDiagnosticsList11 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final CharSequence[] states = {"---Speciality---", "Head", "nose", "eyes"};
//    Dialog MyDialog;
//    Dialog MyDialoganother;
//    Button okBtn,cancelBtn;
    AlertDialog alertDialog1;

    private static SeekBar seek_bar;
    private static TextView distance,availability;
    static int progress_value,dis = 20,availabilityCount;

    ProgressDialog progressDialog;
    ProgressDialog progressDialog1;
    ProgressDialog progressDialog2;
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

    // expandable list view
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    //sidenav fields
    TextView sidenavName,sidenavEmail,sidenavAddress,sidenavMobile,sidenavBloodgroup;

    FloatingActionButton homebutton;

    int jsondataCount = 0,myRangeDistance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appbar_diagnosticlist);

        baseUrl = new ApiBaseUrl();

        getUserId = getIntent().getStringExtra("userId");
        getcity = getIntent().getStringExtra("city");
        mobile = getIntent().getStringExtra("mobile");

        System.out.print("userid in getdiag list....."+getUserId);

        current_city = (TextView) findViewById(R.id.select_city);
        current_city.setText(getcity);

        new GetPatientDetails().execute(baseUrl.getUrl()+"GetPatientByID"+"?ID="+getUserId);

        new GetAllDiagSpecialities().execute(baseUrl.getUrl()+"GetDiagSpeciality");

        myList = new ArrayList<DiagnosticsClass>();


        current_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GetCurrentDiagnosticsList11.this,SelectCity.class);
                i.putExtra("module","diagList");
                i.putExtra("userId",getUserId);
                i.putExtra("mobile",mobile);
                startActivity(i);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Diagnostics");

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


        specialitiesList = new ArrayList<>();

        specialitiesList.add(0,"---Select Test---");

        Speciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(!Speciality.getSelectedItem().toString().equals("---Select Test---"))
                {
                    String js = formatDataAsJson();
                    uploadServerUrl = baseUrl.getUrl()+"GetDiagnosticsInRange";

                    new GetDiagnostics_N_ListBasedonSpeciality().execute(uploadServerUrl,js.toString());

                    myList = new ArrayList<DiagnosticsClass>();
//
                    recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                    recyclerView.setHasFixedSize(true);

                    adapter = new DiagnosticsListAdapter(GetCurrentDiagnosticsList11.this, myList);
                    layoutManager = new LinearLayoutManager(GetCurrentDiagnosticsList11.this);

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //home button
        homebutton = (FloatingActionButton) findViewById(R.id.home);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetCurrentDiagnosticsList11.this,PatientDashBoard.class);
                intent.putExtra("id",getUserId);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
            }
        });

        rangeBar();

        //side navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);

        sidenavName = (TextView) navigationView.findViewById(R.id.name);
        sidenavAddress = (TextView) navigationView.findViewById(R.id.address);
        sidenavMobile = (TextView) navigationView.findViewById(R.id.mobile);
        sidenavEmail = (TextView) navigationView.findViewById(R.id.email);
        sidenavBloodgroup = (TextView) navigationView.findViewById(R.id.bloodgroup);

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
                    Intent editProfile = new Intent(GetCurrentDiagnosticsList11.this,PatientEditProfile.class);
                    editProfile.putExtra("id",getUserId);
                    editProfile.putExtra("mobile",mobile);
                    startActivity(editProfile);

                }

                else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                    Intent contact = new Intent(GetCurrentDiagnosticsList11.this,MyFamily.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",mobile);
                    contact.putExtra("module","patient");
                    startActivity(contact);

                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here

                    Intent contact = new Intent(GetCurrentDiagnosticsList11.this,Offers.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",mobile);
                    contact.putExtra("module","patient");
                    startActivity(contact);


                }

                else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(GetCurrentDiagnosticsList11.this,ReachUs.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",mobile);
                    contact.putExtra("module","patient");
                    startActivity(contact);

                }
                else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here

                    Intent contact = new Intent(GetCurrentDiagnosticsList11.this,Login.class);
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

                        Intent i = new Intent(GetCurrentDiagnosticsList11.this,GetCurrentDoctorsList.class);
                        i.putExtra("userId",getUserId);
                        i.putExtra("mobile",mobile);
                        startActivity(i);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_2) {
                        Intent i = new Intent(GetCurrentDiagnosticsList11.this,GetCurrentDiagnosticsList.class);
                        i.putExtra("userId",getUserId);
                        i.putExtra("mobile",mobile);
                        startActivity(i);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_3) {

                        // call activity here

                        Intent in = new Intent(GetCurrentDiagnosticsList11.this,GetCurrentMedicalShopsList.class);
                        in.putExtra("userId",getUserId);
                        in.putExtra("mobile",mobile);
                        startActivity(in);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_4) {

                        // call activity here
                        // call activity here
                        Intent contact = new Intent(GetCurrentDiagnosticsList11.this,AboutUs.class);
                        startActivity(contact);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_5) {

                        // call activity here
                        Intent bloodbank = new Intent(GetCurrentDiagnosticsList11.this,BloodBank.class);
                        bloodbank.putExtra("userId",getUserId);
                        bloodbank.putExtra("mobile",mobile);
                        startActivity(bloodbank);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_6) {

                        // call activity here
                        // call activity here
                        Intent contact = new Intent(GetCurrentDiagnosticsList11.this,AboutUs.class);
                        startActivity(contact);

                    }

                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM3) {

                    if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM3_1) {

                        // call activity here
                        Intent intent = new Intent(GetCurrentDiagnosticsList11.this,ChangePassword.class);
                        intent.putExtra("id",getUserId);
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

                        Intent intent = new Intent(GetCurrentDiagnosticsList11.this,PatientMyDoctorAppointments.class);
                        intent.putExtra("mobile",mobile);
                        intent.putExtra("id",getUserId);
                        startActivity(intent);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent intent = new Intent(GetCurrentDiagnosticsList11.this,PatientMyDiagnosticAppointments.class);
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

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
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
            progressDialog.dismiss();
            getSpecialities(result);

        }
    }

    private void getSpecialities(String result) {
        try
        {
            JSONArray jsonArr = new JSONArray(result);

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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog1 = new ProgressDialog(GetCurrentDiagnosticsList11.this);
            // Set progressdialog title
//            progressDialog1.setTitle("Your searching process is");
            // Set progressdialog message
            progressDialog1.setMessage("Loading...");

            progressDialog1.setIndeterminate(false);
            // Show progressdialog
            progressDialog1.show();
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
                System.out.println("params..diag11..."+params[1]);
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

            progressDialog1.dismiss();

            String data = result;

            try
            {
                Object json = new JSONTokener(data).nextValue();
                if(json instanceof JSONObject)
                {
                    JSONObject jsono = new JSONObject(result);
                    String ss = (String) jsono.get("Message");
//                    if(ss.equals("No data found."))
//                    {
                    showMessage();
                    availabilityCount = 0;
                    System.out.println("medical availabilityCount...."+availabilityCount);

                    availability.setText(Integer.toString(availabilityCount));
                    Log.e("Api response if.....", result);
//                    }
//                    }
                }
                else if(json instanceof JSONArray)
                {

                    getData(result);
                    adapter.notifyDataSetChanged();
                    Log.e("Api response else.....", result);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }



//            try
//            {
//                JSONObject jsono = new JSONObject(result);
//                String ss = (String) jsono.get("Message");
//                if(ss.equals("No data found."))
//                {
//                    showMessage();
//                    availabilityCount = 0;
//                    System.out.println("medical availabilityCount...."+availabilityCount);
//
//                    availability.setText(Integer.toString(availabilityCount));
//                    Log.e("Api response if.....", result);
//                }
//                else
//                {
//                    getData(result);
//                    adapter.notifyDataSetChanged();
//                    Log.e("Api response else.....", result);
//                }
//            }
//            catch (Exception e)
//            {}
//            getData(result);
//
//            recyclerView.setHasFixedSize(true);
//            recyclerView.setLayoutManager(layoutManager);
//            recyclerView.setAdapter(adapter);

        }
    }

    private void getData(String result) {

        int count = 0;
        try {

            JSONArray jarray = new JSONArray(result);

            availabilityCount = jarray.length();
            System.out.println("diag availabilityCount...."+availabilityCount);

            availability.setText(Integer.toString(availabilityCount));

            for (int i = 0; i < jarray.length(); i++)
            {
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

                String mylatii = object.getString("Latitude");
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


                double myDistances = distance(Double.parseDouble(mylatii),Double.parseDouble(mylongii),selectedCitylat,selectedCitylong);

                System.out.println("distance from current in doc to ur location...."+myDistances);

                double dis = Math.round(myDistances*1000)/1000.000;
                myDistance = String.format("%.1f", dis)+" km";
                System.out.println("dist decimal round...."+myDistance);

                String addressId = object.getString("AddressID");
                String centerImage = object.getString("CenterImage");

                if(dis <= myRangeDistance)
                {
                    count  = count+1;

                    jsondataCount = 1;

                    DiagnosticsClass diagnosticsClass = new DiagnosticsClass(mobile,diagId,getUserId,centerName,cashOnHand,
                            creditDebit,paytm,netBanking,landLineNumber,contactPerson,mylatii,mylongii,myDistance,emergencyService,addressId,centerImage);

                    myList.add(diagnosticsClass);
                    availability.setText(Integer.toString(count));
                }
                else
                {
                    jsondataCount = 0;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if(count == 0)
        {
            availability.setText(Integer.toString(0));
            showMessage();
        }
        else
        {
            availability.setText(Integer.toString(count));
        }
    }

    //Get diagnostics list Based on Speciality from api call
    private class GetDiagnostics_N_ListBasedonSpeciality extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog2 = new ProgressDialog(GetCurrentDiagnosticsList11.this);
            // Set progressdialog title
//            progressDialog2.setTitle("Your searching process is");
            // Set progressdialog message
            progressDialog2.setMessage("Loading...");

            progressDialog2.setIndeterminate(false);
            // Show progressdialog
            progressDialog2.show();
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
                System.out.println("params...diag11 spec.."+params[1]);
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

            progressDialog2.dismiss();

            String data = result;

            try
            {
                Object json = new JSONTokener(data).nextValue();
                if(json instanceof JSONObject)
                {
                    JSONObject jsono = new JSONObject(result);
                    String ss = (String) jsono.get("Message");
                    showMessage();
                    availabilityCount = 0;
                    System.out.println("medical availabilityCount...."+availabilityCount);

                    availability.setText(Integer.toString(availabilityCount));
                    Log.e("Api response if.....", result);
                }
                else if(json instanceof JSONArray)
                {
                    getDataBasedonSpeciality(result);
                    adapter.notifyDataSetChanged();
                    Log.e("Api response else.....", result);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


        }
    }

    private void getDataBasedonSpeciality(String result) {

        int count =0;
        try {

            JSONArray jarray = new JSONArray(result);


            availabilityCount = jarray.length();
            System.out.println("diag availabilityCount...."+availabilityCount);

            availability.setText(Integer.toString(availabilityCount));

            for (int i = 0; i < jarray.length(); i++)
            {
                JSONObject object = jarray.getJSONObject(i);

                JSONArray jsonArray1=new JSONArray((object.getString("KV_DiagSpeciality")));

                for(int j=0;j<jsonArray1.length();j++)
                {

                    JSONObject jsonObject=jsonArray1.getJSONObject(j);
                    int SpecialityID=jsonObject.getInt("Key");
                    String testName =jsonObject.getString("Value");

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

                    String mylatii = object.getString("Latitude");
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


                    double myDistances = distance(Double.parseDouble(mylatii),Double.parseDouble(mylongii),selectedCitylat,selectedCitylong);

                    System.out.println("distance from current in doc to ur location...."+myDistances);

                    double dis = Math.round(myDistances*1000)/1000.000;
                    myDistance = String.format("%.1f", dis)+" km";
                    System.out.println("dist decimal round...."+myDistance);

                    String addressId = object.getString("AddressID");
                    String centerImage = object.getString("CenterImage");

                    if(Speciality.getSelectedItem().toString().equals(testName) && dis <= myRangeDistance)
                    {
                        count  = count+1;

                        jsondataCount = 1;

                        DiagnosticsClass diagnosticsClass = new DiagnosticsClass(mobile,diagId,getUserId,centerName,cashOnHand,
                                creditDebit,paytm,netBanking,landLineNumber,contactPerson,mylatii,mylongii,myDistance,emergencyService,addressId,centerImage);

                        myList.add(diagnosticsClass);
                        availability.setText(Integer.toString(count));
                    }
                    else
                    {
                        jsondataCount = 0;
                    }



                }


            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if(count == 0)
        {
            availability.setText(Integer.toString(0));
            showSpecialityTestNotMatchMessage();
        }
        else
        {
            availability.setText(Integer.toString(count));
        }
    }

    public void showSpecialityTestNotMatchMessage(){

        android.support.v7.app.AlertDialog.Builder a_builder = new android.support.v7.app.AlertDialog.Builder(GetCurrentDiagnosticsList11.this);
        a_builder.setMessage("Your selected test has no records.")
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

    public static Object getPharmacyKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
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
        a_builder.setMessage("No records found for your city")
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

    public void rangeBar()
    {

        seek_bar = (SeekBar) findViewById(R.id.seekbar);
        distance = (TextView) findViewById(R.id.DistanceRange);

        seek_bar.setProgress(20);
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                myRangeDistance = progress;
                progress_value = progress;
                System.out.println("progress...."+progress);
                distance.setText(progress+" Km") ;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(BloodBank.this,"SeekBar is in StartTracking",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                distance.setText(progress_value+" Km");

                myRangeDistance = progress_value;
                dis = progress_value;
                System.out.println("dis.."+dis);

                new GetAllDiagSpecialities().execute(baseUrl.getUrl()+"GetDiagSpeciality");

                String js = formatDataAsJson();
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

        myRangeDistance = seek_bar.getProgress();

        distance.setText(seek_bar.getProgress()+" Km");

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

                String js = formatDataAsJson();
                uploadServerUrl = baseUrl.getUrl()+"GetDiagnosticsInRange";

                new GetDiagnostics_N_List().execute(uploadServerUrl,js.toString());

                myList = new ArrayList<DiagnosticsClass>();

                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);

                adapter = new DiagnosticsListAdapter(GetCurrentDiagnosticsList11.this, myList);
                layoutManager = new LinearLayoutManager(GetCurrentDiagnosticsList11.this);

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

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
