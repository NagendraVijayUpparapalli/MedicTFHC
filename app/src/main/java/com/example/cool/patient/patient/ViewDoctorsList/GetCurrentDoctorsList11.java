package com.example.cool.patient.patient.ViewDoctorsList;

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
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import com.example.cool.patient.doctor.DashBoardCalendar.DoctorDashboard;
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
import com.example.cool.patient.patient.ViewDiagnosticsList.GetCurrentDiagnosticsList;
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
import android.widget.ListView;

public class GetCurrentDoctorsList11 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final CharSequence[] states = {"---Speciality---", "Head", "nose", "eyes"};
    Dialog MyDialog;
    Dialog MyDialoganother;
    Button okBtn,cancelBtn;
    AlertDialog alertDialog1;

    private static SeekBar seek_bar;
    static TextView distance,availability;
    static int progress_value,dis = 20,availabilityCount;

    ProgressDialog progressDialog;
    ProgressDialog progressDialog1;
    ProgressDialog progressDialog2;
    //lat,long
    static String uploadServerUrl = null;
//    static String getcity=null;
    LocationManager locationManager;
    double lattitude,longitude;
    static double lat,lng;
    Geocoder geocoder;
    List<Address> addresses;
    List<Address> fulladdress;
    private static final int REQUEST_LOCATION = 1;

    static int myRangeDistance =0;


    static double selectedCitylat;
    static double selectedCitylong;

    static String selectedlocation=null;

    static String myDistance = null;
    double currentlatti,currentlongi;


    String addressline,mobile,email,pincode,city,state;
    TextView current_city;

    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    ArrayList<DoctorClass> myList;
    DoctorListAdapter adapter;


    ListView listview;

    ApiBaseUrl baseUrl;
    String getUserId;
    TextView userId;

    static String getcity=null;

    List<String> specialityList;
    HashMap<String, String> mySpecialitiesList = new HashMap<String, String>();
    ArrayAdapter<String > specialityAdapter;
    SearchableSpinner Speciality;

    String cur_addressId,mydoctorId,myaddressId,mydocName,myhospitalName,myaddress,mycity,mystate,myfee,mypaymentMode,myphone,myLati,myLongi,myImage;

    // expandable list view
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    //sidenav fields
    TextView sidenavName,sidenavEmail,sidenavAddress,sidenavMobile,sidenavBloodgroup;

    FloatingActionButton homebutton;

    TextView searchSpeciality;
    int jsondataCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appbar_doctorlist);

        baseUrl = new ApiBaseUrl();

        current_city = (TextView) findViewById(R.id.select_city);

//        searchSpeciality.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchSpeciality.setVisibility(View.GONE);
//                Speciality.setVisibility(View.VISIBLE);
//            }
//        });

        new GetAllSpeciality().execute(baseUrl.getUrl()+"GetSpeciality");

        current_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GetCurrentDoctorsList11.this,SelectCity.class);
                i.putExtra("module","docList");
                i.putExtra("userId",getUserId);
                i.putExtra("mobile",getIntent().getStringExtra("mobile"));
                startActivity(i);
            }
        });

        getUserId = getIntent().getStringExtra("userId");
        getcity = getIntent().getStringExtra("city");
        current_city.setText(getcity);
        System.out.print("userid in getdoctors list....."+getUserId);

        new GetPatientDetails().execute(baseUrl.getUrl()+"GetPatientByID"+"?ID="+getUserId);

        myList = new ArrayList<DoctorClass>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        toolbar.setTitle("Doctors");

//        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Toast.makeText(BloodBank.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(GetCurrentDoctorsList11.this,PatientDashBoard.class);
//                        intent.putExtra("id",getUserId);
//                        intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
//                        startActivity(intent);
//
//                    }
//                }
//
//        );

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
        else
        {
            getlatlng();
        }

        Speciality = (SearchableSpinner) findViewById(R.id.speciality);
        seek_bar = (SeekBar) findViewById(R.id.seekbar);
        distance = (TextView) findViewById(R.id.DistanceRange);
        availability = (TextView) findViewById(R.id.availability);
        seek_bar.setProgress(dis);


//
        Speciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(!Speciality.getSelectedItem().toString().equals("---Select Speciality---"))
                {
                    String js = formatDataAsJson();
                    uploadServerUrl = baseUrl.getUrl()+"GetDoctorsInRange";

                    new GetDoctors_N_ListBasedonSpeciality().execute(uploadServerUrl,js.toString());

                    myList = new ArrayList<DoctorClass>();

                    adapter = new DoctorListAdapter(GetCurrentDoctorsList11.this, myList);
                    layoutManager = new LinearLayoutManager(GetCurrentDoctorsList11.this);

                    recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                    recyclerView.setHasFixedSize(true);

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        seek_bar = (SeekBar) findViewById(R.id.seekbar);
        distance = (TextView) findViewById(R.id.DistanceRange);

        seek_bar.setProgress(20);

        distance.setText(seek_bar.getProgress()+" Km");

        rangeBar();


        //home button
        homebutton = (FloatingActionButton) findViewById(R.id.home);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetCurrentDoctorsList11.this,PatientDashBoard.class);
                intent.putExtra("id",getUserId);
                intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                startActivity(intent);
            }
        });

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
                    Intent editProfile = new Intent(GetCurrentDoctorsList11.this,PatientEditProfile.class);
                    editProfile.putExtra("id",getUserId);
                    editProfile.putExtra("mobile",mobile);
                    startActivity(editProfile);

                }

                else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                    Intent contact = new Intent(GetCurrentDoctorsList11.this,MyFamily.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",mobile);
                    contact.putExtra("module","patient");
                    startActivity(contact);

                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here

                    Intent contact = new Intent(GetCurrentDoctorsList11.this,Offers.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",mobile);
                    contact.putExtra("module","patient");
                    startActivity(contact);


                }

                else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(GetCurrentDoctorsList11.this,ReachUs.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",mobile);
                    contact.putExtra("module","patient");
                    startActivity(contact);

                }
                else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here

                    Intent contact = new Intent(GetCurrentDoctorsList11.this,Login.class);
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

                        Intent i = new Intent(GetCurrentDoctorsList11.this,GetCurrentDoctorsList.class);
                        i.putExtra("userId",getUserId);
                        i.putExtra("mobile",mobile);
                        startActivity(i);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_2) {
                        Intent i = new Intent(GetCurrentDoctorsList11.this,GetCurrentDiagnosticsList.class);
                        i.putExtra("userId",getUserId);
                        i.putExtra("mobile",mobile);
                        startActivity(i);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_3) {

                        // call activity here

                        Intent in = new Intent(GetCurrentDoctorsList11.this,GetCurrentMedicalShopsList.class);
                        in.putExtra("userId",getUserId);
                        in.putExtra("mobile",mobile);
                        startActivity(in);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_4) {

                        // call activity here
                        Intent contact = new Intent(GetCurrentDoctorsList11.this,AboutUs.class);
                        startActivity(contact);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_5) {

                        // call activity here
                        Intent bloodbank = new Intent(GetCurrentDoctorsList11.this,BloodBank.class);
                        bloodbank.putExtra("userId",getUserId);
                        bloodbank.putExtra("mobile",mobile);
                        startActivity(bloodbank);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_6) {

                        // call activity here
                        // call activity here
                        Intent contact = new Intent(GetCurrentDoctorsList11.this,AboutUs.class);
                        startActivity(contact);

                    }

                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM3) {

                    if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM3_1) {

                        // call activity here
                        Intent intent = new Intent(GetCurrentDoctorsList11.this,ChangePassword.class);
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

                        Intent intent = new Intent(GetCurrentDoctorsList11.this,PatientMyDoctorAppointments.class);
                        intent.putExtra("mobile",mobile);
                        intent.putExtra("id",getUserId);
                        startActivity(intent);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent intent = new Intent(GetCurrentDoctorsList11.this,PatientMyDiagnosticAppointments.class);
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


    //    get doctor specialities from api call
    private class GetAllSpeciality extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(GetCurrentDoctorsList11.this);
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
            specialityList = new ArrayList<>();

            for (int i = 0; i < jsonArr.length(); i++) {
                System.out.print("myspeciality for loop in..");

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                String specialityKey = jsonObj.getString("SpecialityID");
                String specialityValue = jsonObj.getString("Speciality");
                mySpecialitiesList.put(specialityKey,specialityValue);
                specialityList.add(jsonObj.getString("Speciality"));

                specialityAdapter = new ArrayAdapter<String> (this, R.layout.custom_spinner, specialityList);
                specialityAdapter.setDropDownViewResource(R.layout.custom_spinner); // Specify the layout to use when the list of choices appears
                Speciality.setAdapter(specialityAdapter); // Apply the adapter to the spinner


//                System.out.print("myspeciality list.."+mySpecialitiesList);
//                System.out.print("speciality list in get method.."+specialityList);
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

        try
        {
            data.put("Latitude",selectedCitylat);
            data.put("Longitude",selectedCitylong);
            data.put("Distance", dis);
            return data.toString();
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
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog2 = new ProgressDialog(GetCurrentDoctorsList11.this);
            // Set progressdialog title
//            progressDialog.setTitle("Your searching process is");
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

            progressDialog2.dismiss();
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
                    availabilityCount = 0;
                    System.out.println("doctors availabilityCount...."+availabilityCount);

                    availability.setText(Integer.toString(availabilityCount));

                    showMessage();

                    Log.e("Api response if.....", result);
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

        }
    }

    private void getData(String result) {
        int count = 0;
        try {

            JSONArray jarray = new JSONArray(result);

            availabilityCount = jarray.length();

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);

                String doctorId = object.getString("DoctorID");

                String addressId = object.getString("AddressID");

                String mobile = object.getString("MobileNumber");
                String firstName = object.getString("FirstName");
                String lastName = object.getString("LastName");

                String Name = firstName+" "+lastName;

                String qualification = object.getString("Qualification");
                String specialityName = object.getString("SpecialityName");
                String doctorImage = object.getString("DoctorImage");
                String experience = object.getString("Experience");
                String mylatii= object.getString("Latitude");
                String mylongii = object.getString("Longitude");

                double myDistances = distance(Double.parseDouble(mylatii),Double.parseDouble(mylongii),currentlatti,currentlongi);

                System.out.println("distance from current in doc to ur location...."+myDistances);

                double dis = Math.round(myDistances*1000)/1000.000;

                System.out.println("double dis..."+dis);

                myDistance = String.format("%.1f", dis)+" km";

                System.out.println("dist decimal round...."+myDistance);

                String emergencyService = "";

                if(object.has("EmergencyService"))
                {
                    emergencyService = object.getString("EmergencyService");
                }

                else
                {
                    emergencyService = "";
                }


                String consultationFee = object.getString("ConsultationFee");
                String consultationPrice = object.getString("ConsultationFee");

                String cashonHand = object.getString("CashOnHand");
                String creditDebit = object.getString("CreditDebit");
                String netBanking = object.getString("Netbanking");
                String paytm = object.getString("Paytm");


                double myDis = Integer.parseInt(String.format("%.0f", dis));

                System.out.println("remove decimal dis..."+myDis);

                if(dis <= myRangeDistance)
                {
                    count +=1;
                    DoctorClass doctorClass = new DoctorClass(doctorId,addressId,getUserId,mobile,Name,qualification,specialityName,
                            doctorImage,experience,mylatii,mylongii,myDistance,emergencyService,consultationFee,consultationPrice,cashonHand,creditDebit,netBanking,paytm);

                    myList.add(doctorClass);

                    System.out.println("doctors distance based availabilityCount...."+count);

                    availability.setText(Integer.toString(count));
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
        }
        else
        {
            availability.setText(Integer.toString(count));
        }
    }

    //Get doctors list based on speciality from api call
    private class GetDoctors_N_ListBasedonSpeciality extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog1 = new ProgressDialog(GetCurrentDoctorsList11.this);
            // Set progressdialog title
//            progressDialog.setTitle("Your searching process is");
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

                    showMessage();
//                    {
                    availabilityCount = 0;
                    System.out.println("doctors availabilityCount...."+availabilityCount);

                    availability.setText(Integer.toString(availabilityCount));



                    Log.e("Api response if.....", result);
//                    }
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
//            getDataBasedonSpeciality(result);
//
//            recyclerView.setHasFixedSize(true);
//            recyclerView.setLayoutManager(layoutManager);
//            recyclerView.setAdapter(adapter);

        }
    }

    private void getDataBasedonSpeciality(String result) {
        int count = 0;
        try {

            JSONArray jarray = new JSONArray(result);

            availabilityCount = jarray.length();
            System.out.println("doctors availabilityCount...."+availabilityCount);
            int i = 0;

            for (i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);

                String doctorId = object.getString("DoctorID");

                String addressId = object.getString("AddressID");

                String mobile = object.getString("MobileNumber");
                String firstName = object.getString("FirstName");
                String lastName = object.getString("LastName");

                String Name = firstName+" "+lastName;

                String qualification = object.getString("Qualification");
                String specialityName = object.getString("SpecialityName");
                String doctorImage = object.getString("DoctorImage");
                String experience = object.getString("Experience");
                String mylatii= object.getString("Latitude");
                String mylongii = object.getString("Longitude");

                double myDistances = distance(Double.parseDouble(mylatii),Double.parseDouble(mylongii),currentlatti,currentlongi);

                System.out.println("distance from current in doc to ur location...."+myDistances);

                double dis = Math.round(myDistances*1000)/1000.000;
                myDistance = String.format("%.1f", dis)+" km";
                System.out.println("dist decimal round...."+myDistance);

                String emergencyService = "";

                if(object.has("EmergencyService"))
                {
                    emergencyService = object.getString("EmergencyService");
                }

                else
                {
                    emergencyService = "";
                }


                String consultationFee = object.getString("ConsultationFee");
                String consultationPrice = object.getString("ConsultationFee");

                String cashonHand = object.getString("CashOnHand");
                String creditDebit = object.getString("CreditDebit");
                String netBanking = object.getString("Netbanking");
                String paytm = object.getString("Paytm");



                if(Speciality.getSelectedItem().toString().equals(object.getString("SpecialityName")) && dis <= myRangeDistance)
                {
                    count  = count+1;

                    jsondataCount = 1;

                    DoctorClass doctorClass = new DoctorClass(doctorId,addressId,getUserId,mobile,Name,qualification,specialityName,
                            doctorImage,experience,mylatii,mylongii,myDistance,emergencyService,consultationFee,consultationPrice,cashonHand,creditDebit,netBanking,paytm);

                    myList.add(doctorClass);

                    System.out.println("doctors  spec availabilityCount...."+count);
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
            showSpecialityNotMatchMessage();
        }
        else
        {
            availability.setText(Integer.toString(count));
        }

//        if(jsondataCount == 0)
//        {
//            availability.setText(Integer.toString(0));
//            showSpecialityNotMatchMessage();
//        }
    }

    public void showSpecialityNotMatchMessage(){

        android.support.v7.app.AlertDialog.Builder a_builder = new android.support.v7.app.AlertDialog.Builder(GetCurrentDoctorsList11.this);
        a_builder.setMessage("Your selected speciality has no records.")
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
        double mydist = dist/0.62137;
        return (mydist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public void showMessage(){

        android.support.v7.app.AlertDialog.Builder a_builder = new android.support.v7.app.AlertDialog.Builder(GetCurrentDoctorsList11.this);
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
        AlertDialog.Builder alert = new AlertDialog.Builder(GetCurrentDoctorsList11.this);
        alert.setTitle("Do you want to take Appointment for Register user?");
        //  alert.show();
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(GetCurrentDoctorsList11.this, "YES", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(GetCurrentDoctorsList11.this, "NO", Toast.LENGTH_SHORT).show();
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

        adapter = new DoctorListAdapter(this, myList);
        layoutManager = new LinearLayoutManager(this);

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
//                bw_dist.setText("Distance stop value :"+progress_value+"Km");
                myRangeDistance = progress_value;
                dis = progress_value;
                System.out.println("dis.."+dis);

                System.out.println("myrange dis..."+myRangeDistance);
                getRangeData();
            }
        });

        myRangeDistance = seek_bar.getProgress();
//        System.out.println("myrange dis..."+myRangeDistance);
        distance.setText(seek_bar.getProgress()+" Km");

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

    private void getRangeData()
    {
        specialityList = new ArrayList<>();

        specialityList.add(0,"---Select Speciality---");

        new GetAllSpeciality().execute(baseUrl.getUrl()+"GetSpeciality");

        String js = formatDataAsJson();
        uploadServerUrl = baseUrl.getUrl()+"GetDoctorsInRange";

        new GetDoctors_N_List().execute(uploadServerUrl,js.toString());

        myList = new ArrayList<DoctorClass>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        adapter = new DoctorListAdapter(GetCurrentDoctorsList11.this, myList);
        layoutManager = new LinearLayoutManager(GetCurrentDoctorsList11.this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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


//                String js = formatDataAsJson();
//                uploadServerUrl = baseUrl.getUrl()+"GetDoctorsInRange";
//
//                new GetDoctors_N_List().execute(uploadServerUrl,js.toString());
//
//                myList = new ArrayList<DoctorClass>();
//                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//                recyclerView.setHasFixedSize(true);
//
//                adapter = new DoctorListAdapter(GetCurrentDoctorsList11.this, myList);
//                layoutManager = new LinearLayoutManager(GetCurrentDoctorsList11.this);
//
//                recyclerView.setLayoutManager(layoutManager);
//                recyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();

                System.out.println("selectedCitylat....."+selectedCitylat+"selectedCitylong....."+selectedCitylong);


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
