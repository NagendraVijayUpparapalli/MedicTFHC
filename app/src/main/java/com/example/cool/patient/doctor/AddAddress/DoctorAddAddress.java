package com.example.cool.patient.doctor.AddAddress;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.common.ChangePassword;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.common.Offers;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.common.Registration;
import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.doctor.DashBoardCalendar.DoctorDashboard;
import com.example.cool.patient.common.MapsActivity;
import com.example.cool.patient.R;
import com.example.cool.patient.doctor.DoctorChangePassword;
import com.example.cool.patient.doctor.DoctorEditProfile;
import com.example.cool.patient.doctor.DoctorSideNavigatioExpandableSubList;
import com.example.cool.patient.doctor.DoctorSideNavigationExpandableListAdapter;
import com.example.cool.patient.doctor.ManageAddress.DoctorManageAddress;
import com.example.cool.patient.doctor.TodaysAppointments.DoctorTodaysAppointmentsForPatient;
import com.example.cool.patient.subscriptionPlan.SubscriptionPlanAlertDialog;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.bloder.magic.view.MagicButton;

public class DoctorAddAddress extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    EditText hospitalName,address,pincode,contactPerson,fee,mobileNumber,comments,lat,lng,emergencyContactNumber;
    SearchableSpinner city,state,district;
    CheckBox availableService;
    MagicButton btn_AddAddress;
    RippleView rippleView;
    //doc timings alert
    ImageView ok_btn,cancel_btn;
    EditText appointments;
    Button show;
    Dialog MyDialog;
    TextView textTimings;
    LinearLayout layoutTimings,emergencyContactLayout;

    //send timings to api

    JSONArray jsonArray = new JSONArray();
    JSONObject data = new JSONObject();

    static String getUserId,mobile;
    static String uploadServerUrl = null,addressId,sunAppointmentsCount = "0",
            monAppointmentsCount = "0",tueAppointmentsCount = "0",wedApointmentsCount = "0",
            thuAppointmentsCount = "0",friAppointmentsCount = "0",satAppointmentsCount = "0";

    List<String> districtsList,citiesList,statesList,amTimeSlotsList,pmTimeSlotsList,allItemsList;
    String amItems[],pmItems[],allItems[];
    String myQrArrayList;
    String[] mydistrictlist;
    ArrayAdapter<String> adapter2,adapter3,adapter4;
    HashMap<Long, String> myCitiesList = new HashMap<Long, String>();
    HashMap<Long, String> myStatesList = new HashMap<Long, String>();

    HashMap<String, String> AllTimeSlotsList = new HashMap<String, String>();

    HashMap<String, String> myAmTimeSlotsList = new HashMap<String, String>();
    HashMap<String, String> myPmTimeSlotsList = new HashMap<String, String>();
    List<String> myDistrictsList = new ArrayList<String>();
    Button nextView;
    LinearLayout timingLayout,details_layout;
    TextView getLatLong;

    //add timings view variables
    EditText input;

    int count;

    CardView sunday,monday,tuesday,wednesday,thursday,friday,saturday;
    //  TextView mItemSelected,m1ItemSelected,mItemSelectedTuesday,mItemSelectedWednessday,mItemSelectedThurday,mItemSelectedFriday,mItemSelectedSaturday;
    public static String[] listItemsSunday = null,listItemsMonday,listItemsTuesday,listItemsWednessday,listItemsThursday,listItemsFriday,listItemsSaturday;
    boolean[] checkedItems_sunday,checkedItems_monday,checkedItems_tuesday,checkedItems_wednesday,checkedItems_thursday,checkedItems_friday,checkedItems_saturday;

//    static Map<String, List<String>> map  = null;

    static Map<String, List<String>> map = new HashMap<String, List<String>>();

    boolean[] checkedSunAmTimings,checkedSunPmTimings,checkedMonAmTimings,checkedMonPmTimings,checkedTueAmTimings,checkedTuePmTimings,
            checkedWedAmTimings,checkedWedPmTimings,checkedThuAmTimings,checkedThuPmTimings,checkedFriAmTimings,checkedFriPmTimings,
            checkedSatAmTimings,checkedSatPmTimings;

    String [] SunItems = null;

    public static List getmUserItemsSunItems =null;
    public static List getmUserItemsSunPmItems =null;
    List<String> getmUserItems_SunAmValue = new ArrayList<String>();
    List<String> getmUserItems_SunPmValue = new ArrayList<String>();

    List getmUserItemsMonItems =null;
    List<String> getmUserItems_MonAmValue = new ArrayList<String>();
    List<String> getmUserItems_MonPmValue = new ArrayList<String>();

    List getmUserItemsTueItems =null;
    List<String> getmUserItems_TueAmValue = new ArrayList<String>();
    List<String> getmUserItems_TuePmValue = new ArrayList<String>();

    List getmUserItemsWedItems =null;
    List<String> getmUserItems_WedAmValue = new ArrayList<String>();
    List<String> getmUserItems_WedPmValue = new ArrayList<String>();

    List getmUserItemsThurItems = new ArrayList<>();
    List<String> getmUserItems_ThurAmValue = new ArrayList<String>();
    List<String> getmUserItems_ThurPmValue = new ArrayList<String>();

    List getmUserItemsFriItems = new ArrayList<>();
    List<String> getmUserItems_FriAmValue = new ArrayList<String>();
    List<String> getmUserItems_FriPmValue = new ArrayList<String>();

    List getmUserItemsSatItems = new ArrayList<>();
    List<String> getmUserItems_SatAmValue = new ArrayList<String>();
    List<String> getmUserItems_SatPmValue = new ArrayList<String>();

    ApiBaseUrl baseUrl;

    ProgressDialog progressDialog;

    static String userId,myHospitalName,myAddress,myPincode,myContactPerson,myMobile,myFee,
            myComments,myLati,myLngi,myCity,myState,myDistrict,myEmergencyContact;
    boolean myAvailableService;

    // expandable list view

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    //sidenav fields
    TextView sidenavName,sidenavEmail,sidenavMobile;
    ImageView sidenavDoctorImage;

    Dialog MyDialog1;
    TextView message;
    LinearLayout oklink;

    //location fields
    LocationManager locationManager;
    String lattitude,longitude;
    Geocoder geocoder;
    List<Address> addresses;
    private static final int REQUEST_LOCATION = 1;
    static String mycity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_add_address);

        baseUrl = new ApiBaseUrl();

        getUserId = getIntent().getStringExtra("id");
        mobile = getIntent().getStringExtra("mobile");
        System.out.print("doctorid in addaddress....."+getUserId);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        hospitalName = (EditText) findViewById(R.id.Hospital_Name);
        address = (EditText) findViewById(R.id.Address);
        pincode = (EditText) findViewById(R.id.pincode);
        contactPerson = (EditText) findViewById(R.id.Frontoffice);
        fee = (EditText) findViewById(R.id.Consultation_Fee);

        mobileNumber = (EditText) findViewById(R.id.Mobile_Number);
        city = (SearchableSpinner) findViewById(R.id.cityId);
        state = (SearchableSpinner) findViewById(R.id.stateId);
        district = (SearchableSpinner) findViewById(R.id.districtId);

        comments = (EditText) findViewById(R.id.Comments_Others);
        getLatLong = findViewById(R.id.getlatlng);
        lat = (EditText) findViewById(R.id.Latitude);
        lng = (EditText) findViewById(R.id.Longitude);

        availableService = (CheckBox) findViewById(R.id.serviceAvailable);
        nextView = (Button) findViewById(R.id.next_link);
        timingLayout = (LinearLayout)findViewById(R.id.timing);
        details_layout = (LinearLayout)findViewById(R.id.details);
//        layoutTimings = (LinearLayout)findViewById(R.id.layout_times);

        emergencyContactNumber = (EditText) findViewById(R.id.emergencyContact);
        emergencyContactLayout = (LinearLayout)findViewById(R.id.emergencyContactLayout);

        availableService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewEmergencyContactField();
            }
        });

        getLatLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAddAddress();
            }
        });

        nextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFullAddress();
            }
        });

        new GetAllCities().execute(baseUrl.getUrl()+"GetAllCity");

        new GetAllStates().execute(baseUrl.getUrl()+"GetAllState");

        new GetAllDistricts().execute(baseUrl.getUrl()+"GetAllDistrict");

        new GetTimeSlots().execute(baseUrl.getUrl()+"GetAllTimeSlot");

        new GetDoctorDetails().execute(baseUrl.getUrl()+"GetDoctorByID"+"?id="+getUserId);

        rippleView = (RippleView) findViewById(R.id.rippleView);
        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String js = formatDoctorTimingsDataAsJson();
                System.out.println("js time array"+js.toString());
                new insertDoctorAppointmentTimings().execute(baseUrl.getUrl()+"DoctorInsertTimeSlot",js.toString());
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("Add Address");
//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(DoctorAddAddress.this,DoctorDashboard.class);
//                        intent.putExtra("id",getUserId);
//                        intent.putExtra("mobile",mobile);
//                        startActivity(intent);
//
//                    }
//                }
//
//        );

        sunday = (CardView) findViewById(R.id.Sunday);
        monday = (CardView) findViewById(R.id.Monday);
        tuesday = (CardView) findViewById(R.id.Tuesday);
        wednesday =(CardView)findViewById(R.id.Wednesday);
        thursday = (CardView) findViewById(R.id.Thursday);
        friday = (CardView) findViewById(R.id.Friday);
        saturday = (CardView) findViewById(R.id.Saturday);

        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(DoctorAddAddress.this,
//                        "Saturday Appointment Timings", Toast.LENGTH_LONG).show();
                MySaturdayCustomAlertDialog();
            }
        });
        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(DoctorAddAddress.this,
//                        "Friday Appointment Timings", Toast.LENGTH_LONG).show();
                MyFridayCustomAlertDialog();
            }
        });

        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(DoctorAddAddress.this,
//                        "Thurday Appointment Timings", Toast.LENGTH_LONG).show();
                MyThursdayCustomAlertDialog();
            }
        });

        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(DoctorAddAddress.this,
//                        "wednessday Appointment Timings", Toast.LENGTH_LONG).show();
                MyWednesdayCustomAlertDialog();
            }
        });

        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(DoctorAddAddress.this,
//                        "Tuesday Appointment Timings", Toast.LENGTH_LONG).show();
                MyTuesdayCustomAlertDialog();
            }
        });



        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(DoctorAddAddress.this,
//                        "Monday Appointment Timings", Toast.LENGTH_LONG).show();
                MyMondayCustomAlertDialog();
            }
        });

        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(DoctorAddAddress.this,
//                        "Sunday Appointment Timings", Toast.LENGTH_LONG).show();
                MySundayCustomAlertDialog();
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

//        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_doctor_dashboard);

        sidenavName = (TextView) navigationView.findViewById(R.id.name);
        sidenavEmail = (TextView) navigationView.findViewById(R.id.emailId);
        sidenavMobile = (TextView) navigationView.findViewById(R.id.mobile);
        sidenavDoctorImage = (ImageView) navigationView.findViewById(R.id.profileImageId);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView1);
        expandableListDetail = DoctorSideNavigatioExpandableSubList.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new DoctorSideNavigationExpandableListAdapter(this, expandableListTitle, expandableListDetail);
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

                if (groupPosition == DoctorSideNavigationExpandableListAdapter.Services) {
                    retVal = false;
                } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.Address) {
                    retVal = false;
                } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM3) {
                    retVal = false;

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM4) {
                    // call some activity here
                    Intent contact = new Intent(DoctorAddAddress.this,DoctorEditProfile.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",mobile);
                    contact.putExtra("user","old");
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                    Intent i = new Intent(DoctorAddAddress.this,SubscriptionPlanAlertDialog.class);
                    i.putExtra("id",getUserId);
                    i.putExtra("mobile",mobile);
                    i.putExtra("module","doc");
                    startActivity(i);

                } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here
                    Intent contact = new Intent(DoctorAddAddress.this,Offers.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",mobile);
                    contact.putExtra("module","doc");
                    startActivity(contact);

                }
                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent i = new Intent(DoctorAddAddress.this,ReachUs.class);
                    i.putExtra("id",getUserId);
                    i.putExtra("mobile",mobile);
                    i.putExtra("module","doc");
                    startActivity(i);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here
                    Intent contact = new Intent(DoctorAddAddress.this,Login.class);
                    startActivity(contact);

                }

                return retVal;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                if (groupPosition == DoctorSideNavigationExpandableListAdapter.Services) {
                    if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM1_1) {

                        Intent i = new Intent(DoctorAddAddress.this,DoctorDashboard.class);
                        i.putExtra("id",getUserId);
                        i.putExtra("mobile",mobile);
                        startActivity(i);


                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM1_2) {

                        // call activity here

                        Intent i = new Intent(DoctorAddAddress.this,DoctorTodaysAppointmentsForPatient.class);
                        i.putExtra("id",getUserId);
                        i.putExtra("mobile",mobile);
                        startActivity(i);

                    }
//                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM1_3) {
//
//                        // call activity here
//
//                    }


                } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM3) {

                    if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM3_1) {

                        // call activity here

                        Intent about = new Intent(DoctorAddAddress.this,DoctorChangePassword.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",mobile);
                        startActivity(about);

                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                } else if(groupPosition == DoctorSideNavigationExpandableListAdapter.Address) {
                    if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_1) {


                        Intent about = new Intent(DoctorAddAddress.this,DoctorAddAddress.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",mobile);
                        startActivity(about);

                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent about = new Intent(DoctorAddAddress.this,DoctorManageAddress.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",mobile);
                        startActivity(about);

                    }

                }
                return true;

            }
        });

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


    //get current location
    private void getLocation() {
        System.out.print("helo this is method");
        if (ActivityCompat.checkSelfPermission(DoctorAddAddress.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (DoctorAddAddress.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.print("helo this is if");

            ActivityCompat.requestPermissions(DoctorAddAddress.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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

                geocoder=new Geocoder(getApplicationContext());

                try {
                    addresses = geocoder.getFromLocation(latti, longi,1);
                    System.out.println("addresses"+addresses);

                    if (addresses.isEmpty())
                    {
//                        cityname.setTitle("waiting");
//                        present_location.setText("Waiting");
                    }
                    else
                    {
                        if(addresses.size()>0)
                        {
                            mycity = addresses.get(0).getLocality();
//                            present_location.setText(city);
////                            cityname.setTitle(city);
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

                geocoder=new Geocoder(getApplicationContext());

                try {
                    addresses = geocoder.getFromLocation(latti, longi,1);
                    System.out.println("addresses"+addresses);

                    if (addresses.isEmpty())
                    {
//                        cityname.setTitle("waiting");
//                        present_location.setText("Waiting");
                    }
                    else
                    {
                        if(addresses.size()>0)
                        {
                            mycity = addresses.get(0).getLocality();
//                            present_location.setText(city);
////                            cityname.setTitle(city);
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

                geocoder=new Geocoder(getApplicationContext());

                try {
                    addresses = geocoder.getFromLocation(latti, longi,1);
                    System.out.println("addresses"+addresses);

                    if (addresses.isEmpty())
                    {
//                        cityname.setTitle("waiting");
//                        present_location.setText("Waiting");
                    }
                    else
                    {
                        if(addresses.size()>0)
                        {
                            mycity = addresses.get(0).getLocality();
//                            present_location.setText(city);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //get doctor details based on id from api call
    private class GetDoctorDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            // Create a progressdialog
//            progressDialog = new ProgressDialog(DoctorAddAddress.this);
//            // Set progressdialog title
////            progressDialog.setTitle("Your searching process is");
//            // Set progressdialog message
//            progressDialog.setMessage("Loading...");
//
//            progressDialog.setIndeterminate(false);
//            // Show progressdialog
//            progressDialog.show();

            progressDialog = new ProgressDialog(DoctorAddAddress.this);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
            progressDialog.setContentView(R.layout.myprogress);
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
            progressDialog.dismiss();
            getProfileDetails(result);
        }

    }

    private void getProfileDetails(String result) {
        try
        {
            JSONObject js = new JSONObject(result);

                String myMobile = (String) js.get("MobileNumber");
                String myEmail = (String) js.get("EmailID");
                String myName = (String) js.get("FirstName");
                String mySurname = (String) js.get("LastName");

                String mydoctorImage = (String) js.get("DoctorImage");

                sidenavName.setText(myName+" "+mySurname);
                sidenavEmail.setText(myEmail);
                sidenavMobile.setText(myMobile);

            new GetProfileImageTask(sidenavDoctorImage).execute(baseUrl.getImageUrl()+mydoctorImage);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    private class GetProfileImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public GetProfileImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            sidenavDoctorImage.setImageBitmap(result);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.qricon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.qricon)
        {

            Intent intent = new Intent(DoctorAddAddress.this,DoctorDashboard.class);
            intent.putExtra("id",getUserId);
            intent.putExtra("mobile",mobile);
            startActivity(intent);

//            qrScanIcon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {

//            IntentIntegrator integrator = new IntentIntegrator(activity);
//            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//            integrator.setPrompt("Scan");
//            integrator.setCameraId(0);
//            integrator.setBeepEnabled(false);
//            integrator.setBarcodeImageEnabled(false);
//            integrator.initiateScan();
//            return true;
//                CameraManager a = new CameraManager();
//                }
//            });
        }

        return super.onOptionsItemSelected(item);
    }

    private String formatDoctorTimingsDataAsJson() {

        // iterate and display values
        System.out.println("Fetching Keys and corresponding [Multiple] Values n");

        try{

            org.json.simple.JSONArray allDataArray = new org.json.simple.JSONArray();

            for (Map.Entry<String, List<String>> entry : map.entrySet()) {

                String key = entry.getKey();
                List<String> values = entry.getValue();

                List mylistP = new ArrayList<>();
                mylistP.addAll(values);

                int i = 0;

                System.out.println("map values "+map);

//                System.out.println("items size "+getmUserItemsSunItems.size());

                if(key.equals("0"))
                {

                    String a[] = new String[getmUserItemsSunItems.size()];
                    //Loop index size()
                    for(int index = 0; index < a.length; index++) {

                        String lis = values.get(i);
                        a = lis.split(",");
                        List mylist = new ArrayList<>();
                        mylist.addAll(Arrays.asList(a));

                        System.out.println("sun map "+AllTimeSlotsList.toString());

                        JSONObject eachData = new JSONObject();

                        eachData.put("TsID", getTimeKeyFromValue(AllTimeSlotsList,mylist.get(index)));
                        eachData.put("TimeSlots", mylist.get(index));
                        eachData.put("AddressID", addressId);
                        eachData.put("DayNameID", key);
                        eachData.put("NoOfAppointments", sunAppointmentsCount);
                        allDataArray.add(eachData);

                    }

                }
                if(key.equals("1"))
                {

                    String a[] = new String[getmUserItemsMonItems.size()];
                    //Loop index size()
                    for(int index = 0; index < a.length; index++) {

                        String lis = values.get(i);
                        a = lis.split(",");
                        List mylist = new ArrayList<>();
                        mylist.addAll(Arrays.asList(a));

                        JSONObject eachData = new JSONObject();

                        eachData.put("TsID", getTimeKeyFromValue(AllTimeSlotsList,mylist.get(index)));
                        eachData.put("TimeSlots", mylist.get(index));
                        eachData.put("AddressID", addressId);
                        eachData.put("DayNameID", key);
                        eachData.put("NoOfAppointments", monAppointmentsCount);
                        allDataArray.add(eachData);

                    }

                }
                if(key.equals("2"))
                {

                    String a[] = new String[getmUserItemsTueItems.size()];
                    //Loop index size()
                    for(int index = 0; index < a.length; index++) {

                        String lis = values.get(i);
                        a = lis.split(",");
                        List mylist = new ArrayList<>();
                        mylist.addAll(Arrays.asList(a));

                        JSONObject eachData = new JSONObject();

                        eachData.put("TsID", getTimeKeyFromValue(AllTimeSlotsList,mylist.get(index)));
                        eachData.put("TimeSlots", mylist.get(index));
                        eachData.put("AddressID", addressId);
                        eachData.put("DayNameID", key);
                        eachData.put("NoOfAppointments", tueAppointmentsCount);
                        allDataArray.add(eachData);

                    }
                }


                if(key.equals("3"))
                {

                    String a[] = new String[getmUserItemsWedItems.size()];
                    //Loop index size()
                    for(int index = 0; index < a.length; index++) {

                        String lis = values.get(i);
                        a = lis.split(",");
                        List mylist = new ArrayList<>();
                        mylist.addAll(Arrays.asList(a));

                        JSONObject eachData = new JSONObject();

                        eachData.put("TsID", getTimeKeyFromValue(AllTimeSlotsList,mylist.get(index)));
                        eachData.put("TimeSlots", mylist.get(index));
                        eachData.put("AddressID", addressId);
                        eachData.put("DayNameID", key);
                        eachData.put("NoOfAppointments", wedApointmentsCount);
                        allDataArray.add(eachData);

                    }

                }


                if(key.equals("4"))
                {

                    String a[] = new String[getmUserItemsThurItems.size()];
                    //Loop index size()
                    for(int index = 0; index < a.length; index++) {

                        String lis = values.get(i);
                        a = lis.split(",");
                        List mylist = new ArrayList<>();
                        mylist.addAll(Arrays.asList(a));

                        JSONObject eachData = new JSONObject();

                        eachData.put("TsID", getTimeKeyFromValue(AllTimeSlotsList,mylist.get(index)));
                        eachData.put("TimeSlots", mylist.get(index));
                        eachData.put("AddressID", addressId);
                        eachData.put("DayNameID", key);
                        eachData.put("NoOfAppointments", thuAppointmentsCount);
                        allDataArray.add(eachData);

                    }

                }


                if(key.equals("5"))
                {
                    System.out.println("values size "+values.size());

                    String a[] = new String[getmUserItemsFriItems.size()];
                    //Loop index size()
                    for(int index = 0; index < a.length; index++) {

                        String lis = values.get(i);
                        a = lis.split(",");
                        List mylist = new ArrayList<>();
                        mylist.addAll(Arrays.asList(a));

                        JSONObject eachData = new JSONObject();

                        eachData.put("TsID", getTimeKeyFromValue(AllTimeSlotsList,mylist.get(index)));
                        eachData.put("TimeSlots", mylist.get(index));
                        eachData.put("AddressID", addressId);
                        eachData.put("DayNameID", key);
                        eachData.put("NoOfAppointments", friAppointmentsCount);
                        allDataArray.add(eachData);
                    }
                }

                if(key.equals("6"))
                {
                    System.out.println("values size "+values.size());

                    String a[] = new String[getmUserItemsSatItems.size()];
                    //Loop index size()
                    for(int index = 0; index < a.length; index++) {

                        String lis = values.get(i);
                        a = lis.split(",");
                        List mylist = new ArrayList<>();
                        mylist.addAll(Arrays.asList(a));

                        JSONObject eachData = new JSONObject();

                        eachData.put("TsID", getTimeKeyFromValue(AllTimeSlotsList,mylist.get(index)));
                        eachData.put("TimeSlots", mylist.get(index));
                        eachData.put("AddressID", addressId);
                        eachData.put("DayNameID", key);
                        eachData.put("NoOfAppointments", satAppointmentsCount);
                        allDataArray.add(eachData);

                    }

                }

            }

            System.out.println("final array = " + allDataArray.toString());

            System.out.println("final array size= " + allDataArray.size());

            return allDataArray.toString();

        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }

    private void viewEmergencyContactField() {
        if(availableService.isChecked()==true)
        {
            emergencyContactLayout.setVisibility(View.VISIBLE);
        }
        else if(availableService.isChecked()==false)
        {
            emergencyContactLayout.setVisibility(View.GONE);
        }
    }

    public void validateAddAddress()
    {
        if(!validate())
        {
//            Toast.makeText(this,"Succesfully field" , Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(DoctorAddAddress.this,MapsActivity.class);
            intent.putExtra("doc","docAdd");
            intent.putExtra("id",getUserId);
            intent.putExtra("regMobile",mobile);
            intent.putExtra("hospitalName",hospitalName.getText().toString());
            intent.putExtra("address",address.getText().toString());
            intent.putExtra("city",city.getSelectedItem().toString());
            intent.putExtra("state",state.getSelectedItem().toString());
            intent.putExtra("district",district.getSelectedItem().toString());
            intent.putExtra("mobile",mobileNumber.getText().toString());
            intent.putExtra("pincode",pincode.getText().toString());
            intent.putExtra("person",contactPerson.getText().toString());
            intent.putExtra("fee",fee.getText().toString());
            intent.putExtra("lati",lattitude);
            intent.putExtra("longi",longitude);
            startActivity(intent);
        }
    }

    public boolean validate()
    {
        boolean validate = true;
        if(hospitalName.getText().toString().trim().isEmpty())
        {
            hospitalName.setError("please enter the name");
            validate  = false;

        }
        if(address.getText().toString().trim().isEmpty())
        {
            address.setError("please enter the name");
            validate  = false;

        }
        if(pincode.getText().toString().trim().isEmpty())
        {
            pincode.setError("please enter the name");
            validate  = false;

        }
        if( contactPerson.getText().toString().trim().isEmpty())
        {
            contactPerson.setError("please enter contactperson");
            validate  = false;

        }
        if( fee.getText().toString().trim().isEmpty())
        {
            fee.setError("please enter fee");
            validate  = false;

        }
//        if( comments.getText().toString().trim().isEmpty())
//        {
//            comments.setError("please enter comments");
//            validate  = false;
//
//        }
//        if( lat.getText().toString().isEmpty())
//        {
//            lat.setError("please select location");
//            validate  = false;
//
//        }
//        if( lng.getText().toString().isEmpty())
//        {
//            lng.setError("please select location");
//            validate  = false;
//
//        }

        if(mobileNumber.getText().toString().trim().isEmpty() || !Patterns.PHONE.matcher(mobileNumber.getText().toString().trim()).matches())
        {
            mobileNumber.setError("please enter the mobile number");
            validate=false;
        }
        else if(mobileNumber.getText().toString().trim().length()<10 || mobileNumber.getText().toString().trim().length()>11)
        {
            mobileNumber.setError(" Invalid phone number ");
            validate=false;
        }


        return validate;
    }


    public void validateFullAddress()
    {
        if(!addressValidate())
        {
//            Toast.makeText(this,"Succesfully field" , Toast.LENGTH_SHORT).show();
        }
        else
        {
            String js = formatDataAsJson();
            Toast.makeText(this,"Succesfully field" , Toast.LENGTH_SHORT).show();

            new sendDoctorAddAdressDetails().execute(baseUrl.getUrl()+"DoctorAddAddress",js.toString());

            timingLayout.setVisibility(View.VISIBLE);
            details_layout.setVisibility(View.GONE);
        }
    }

    public boolean addressValidate()
    {
        boolean validate = true;
        if(hospitalName.getText().toString().trim().isEmpty())
        {
            hospitalName.setError("please enter the name");
            validate  = false;

        }
        if(address.getText().toString().trim().isEmpty())
        {
            address.setError("please enter the name");
            validate  = false;

        }
        if(pincode.getText().toString().trim().isEmpty())
        {
            pincode.setError("please enter the name");
            validate  = false;

        }
        if( contactPerson.getText().toString().trim().isEmpty())
        {
            contactPerson.setError("please enter contactperson");
            validate  = false;

        }
        if( fee.getText().toString().trim().isEmpty())
        {
            fee.setError("please enter fee");
            validate  = false;

        }
        if( comments.getText().toString().trim().isEmpty())
        {
            comments.setError("please enter comments");
            validate  = false;

        }
        if( lat.getText().toString().isEmpty())
        {
            lat.setError("please select location");
            validate  = false;

        }
        if( lng.getText().toString().isEmpty())
        {
            lng.setError("please select location");
            validate  = false;

        }

//        if(landlineMobileNumber.getText().toString().trim().isEmpty() || !Patterns.PHONE.matcher(landlineMobileNumber.getText().toString().trim()).matches())
//        {
//            landlineMobileNumber.setError("please enter the mobile number");
//            validate=false;
//        }
//        else if(landlineMobileNumber.getText().toString().trim().length()<10 || landlineMobileNumber.getText().toString().trim().length()>11)
//        {
//            landlineMobileNumber.setError(" Invalid phone number ");
//            validate=false;
//        }

        if(mobileNumber.getText().toString().trim().isEmpty() || !Patterns.PHONE.matcher(mobileNumber.getText().toString().trim()).matches())
        {
            mobileNumber.setError("please enter the mobile number");
            validate=false;
        }
        else if(mobileNumber.getText().toString().trim().length()<10 || mobileNumber.getText().toString().trim().length()>10)
        {
            mobileNumber.setError(" Invalid phone number ");
            validate=false;
        }

        if(availableService.isChecked() == true) {

            if(emergencyContactNumber.getText().toString().isEmpty() || !Patterns.PHONE.matcher(emergencyContactNumber.getText().toString()).matches())
            {
                emergencyContactNumber.setError("please fill emeregency number");
                validate=false;
            }

            else if (emergencyContactNumber.getText().toString().length() < 10 || emergencyContactNumber.getText().toString().length() > 10) {
                emergencyContactNumber.setError(" Invalid contact number ");
                validate = false;
            }
        }

        else
        {

            validate = true;

        }

        return validate;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    //Get timeslots list from api call
    private class GetTimeSlots extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result  states  ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getTimeSlots(result);
        }
    }

    private void getTimeSlots(String result) {
        try
        {
            JSONArray jsonArr = new JSONArray(result);

            allItemsList = new ArrayList<>();

            for (int i = 0; i < jsonArr.length(); i++) {

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                String timeId = jsonObj.getString("TsID");

                String timeValue = jsonObj.getString("TimeSlots");
                AllTimeSlotsList.put(timeId,timeValue);

                allItemsList.add(jsonObj.getString("TimeSlots"));
                String[] stockArr = new String[allItemsList.size()];

                allItems = allItemsList.toArray(stockArr);
                checkedSunAmTimings = new boolean[allItems.length];
                checkedMonAmTimings = new boolean[allItems.length];
                checkedTueAmTimings = new boolean[allItems.length];
                checkedWedAmTimings = new boolean[allItems.length];
                checkedThuAmTimings = new boolean[allItems.length];
                checkedFriAmTimings = new boolean[allItems.length];
                checkedSatAmTimings = new boolean[allItems.length];

            }
        }
        catch (JSONException e)
        {}
    }

    public void MySundayCustomAlertDialog(){

//        getmUserItemsSunItems = new ArrayList<>();

//        map = new HashMap<String, List<String>>();

//        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorAddAddress.this);
//        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorAddAddress.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.appointment_count_alert);// custom alert
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);

        ok_btn = (ImageView) MyDialog.findViewById(R.id.ok);
        cancel_btn = (ImageView) MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DoctorAddAddress.this,
                        "Timings", Toast.LENGTH_LONG).show();
                sunAppointmentsCount = appointments.getText().toString();
                System.out.println("count..."+sunAppointmentsCount);
                showalert(sunAppointmentsCount);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.show();
//        return get
    }

    public void showalert(String txt)
    {
        getmUserItemsSunItems = new ArrayList<>();

        getmUserItemsSunPmItems = new ArrayList<>();

//        map = new HashMap<String, List<String>>();

        TextView inputtext;
        final int value=Integer.parseInt(txt);
        System.out.println("value"+value);

        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorAddAddress.this);

        mBuilder2.setTitle("Appointments: "+txt);
        mBuilder2.setMultiChoiceItems(allItems, checkedSunAmTimings, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    String i = Integer.toString(position);
                    getmUserItems_SunAmValue.add(i);
                }else{
                    getmUserItems_SunAmValue.remove((Integer.valueOf(position)));
                }
            }
        });

//        mBuilder2.setCancelable(false);
        mBuilder2.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";

                for (int i = 0; i <  getmUserItems_SunAmValue.size(); i++) {
                    item = item + allItems[Integer.parseInt(getmUserItems_SunAmValue.get(i))];
                    if (i != getmUserItems_SunAmValue.size() - 1) {
                        item = item + ",";
                        count ++;
                    }
                }
                System.out.println("count"+count);
                getmUserItemsSunItems.add(item);
                System.out.println("size of list "+getmUserItemsSunItems.size());

//                if(count > value)
//                {
//                    Toast.makeText(getApplicationContext(),"you are entered more then your appointmnts",Toast.LENGTH_SHORT).show();
//
//                }
                map.put("0", getmUserItemsSunItems);

                MyDialog.cancel();
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

                for (int i = 0; i <   checkedSunAmTimings.length; i++) {
                    checkedSunAmTimings[i] = false;
                    getmUserItems_SunAmValue.clear();

                }
            }
        });
        AlertDialog mDialog1 = mBuilder2.create();
        mDialog1.show();
    }

    public void MyMondayCustomAlertDialog(){

//        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorAddAddress.this);
//        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorAddAddress.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.doctor_insert_timings);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);

        ok_btn = (ImageView) MyDialog.findViewById(R.id.ok);
        cancel_btn = (ImageView) MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DoctorAddAddress.this,
                        "Timings", Toast.LENGTH_LONG).show();
                monAppointmentsCount = appointments.getText().toString();
                System.out.println("mon count..."+monAppointmentsCount);
                showMonAlert(monAppointmentsCount);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.show();
    }

    public void showMonAlert(String txt)
    {
        getmUserItemsMonItems = new ArrayList<>();

//        map = new HashMap<String, List<String>>();

        TextView inputtext;
        final int value=Integer.parseInt(txt);
        System.out.println("value"+value);

        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorAddAddress.this);

        mBuilder2.setTitle("Appointments: "+txt);
        mBuilder2.setMultiChoiceItems(allItems, checkedMonAmTimings, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    String i = Integer.toString(position);
                    getmUserItems_MonAmValue.add(i);
                }else{
                    getmUserItems_MonAmValue.remove((Integer.valueOf(position)));
                }
            }
        });

//        mBuilder2.setCancelable(false);
        mBuilder2.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";

                for (int i = 0; i <  getmUserItems_MonAmValue.size(); i++) {
                    item = item + allItems[Integer.parseInt(getmUserItems_MonAmValue.get(i))];
                    if (i != getmUserItems_MonAmValue.size() - 1) {
                        item = item + ",";
                        count ++;
                    }
                }
                System.out.println("count"+count);
                getmUserItemsMonItems.add(item);

                map.put("1", getmUserItemsMonItems);

                MyDialog.cancel();
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

                for (int i = 0; i <   checkedMonAmTimings.length; i++) {
                    checkedMonAmTimings[i] = false;
                    getmUserItems_MonAmValue.clear();

                }
            }
        });
        AlertDialog mDialog1 = mBuilder2.create();
        mDialog1.show();
    }

    public void MyTuesdayCustomAlertDialog(){

//        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorAddAddress.this);
//        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorAddAddress.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.appointment_count_alert);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);

        ok_btn = (ImageView) MyDialog.findViewById(R.id.ok);
        cancel_btn = (ImageView) MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DoctorAddAddress.this,
                        "Timings", Toast.LENGTH_LONG).show();
                tueAppointmentsCount = appointments.getText().toString();
                System.out.println("tue count..."+tueAppointmentsCount);
                showTueAlert(tueAppointmentsCount);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.show();
    }

    public void showTueAlert(String txt)
    {
        getmUserItemsTueItems = new ArrayList<>();

        TextView inputtext;
        final int value=Integer.parseInt(txt);
        System.out.println("value"+value);

        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorAddAddress.this);

        mBuilder2.setTitle("Appointments: "+txt);
        mBuilder2.setMultiChoiceItems(allItems, checkedTueAmTimings, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    String i = Integer.toString(position);
                    getmUserItems_TueAmValue.add(i);
                }else{
                    getmUserItems_TueAmValue.remove((Integer.valueOf(position)));
                }
            }
        });

//        mBuilder2.setCancelable(false);
        mBuilder2.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";

                for (int i = 0; i <  getmUserItems_TueAmValue.size(); i++) {
                    item = item + allItems[Integer.parseInt(getmUserItems_TueAmValue.get(i))];
                    if (i != getmUserItems_TueAmValue.size() - 1) {
                        item = item + ",";
                        count ++;
                    }
                }
                System.out.println("count"+count);
                getmUserItemsTueItems.add(item);

                map.put("2", getmUserItemsTueItems);

                MyDialog.cancel();
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

                for (int i = 0; i <   checkedTueAmTimings.length; i++) {
                    checkedTueAmTimings[i] = false;
                    getmUserItems_TueAmValue.clear();

                }
            }
        });
        AlertDialog mDialog1 = mBuilder2.create();
        mDialog1.show();
    }


    public void MyWednesdayCustomAlertDialog(){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorAddAddress.this);
        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorAddAddress.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.doctor_insert_timings);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);

        ok_btn = (ImageView) MyDialog.findViewById(R.id.ok);
        cancel_btn = (ImageView) MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DoctorAddAddress.this,
                        "Timings", Toast.LENGTH_LONG).show();
                wedApointmentsCount = appointments.getText().toString();
                System.out.println("wed count..."+wedApointmentsCount);
                showWedAlert(wedApointmentsCount);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.show();

    }

    public void showWedAlert(String txt)
    {
        getmUserItemsWedItems = new ArrayList<>();

        TextView inputtext;
        final int value=Integer.parseInt(txt);
        System.out.println("value"+value);

        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorAddAddress.this);

        mBuilder2.setTitle("Appointments: "+txt);
        mBuilder2.setMultiChoiceItems(allItems, checkedWedAmTimings, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    String i = Integer.toString(position);
                    getmUserItems_WedAmValue.add(i);
                }else{
                    getmUserItems_WedAmValue.remove((Integer.valueOf(position)));
                }
            }
        });

//        mBuilder2.setCancelable(false);
        mBuilder2.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";

                for (int i = 0; i <  getmUserItems_WedAmValue.size(); i++) {
                    item = item + allItems[Integer.parseInt(getmUserItems_WedAmValue.get(i))];
                    if (i != getmUserItems_WedAmValue.size() - 1) {
                        item = item + ",";
                        count ++;
                    }
                }
                System.out.println("count"+count);
                getmUserItemsWedItems.add(item);

                map.put("3", getmUserItemsWedItems);

                MyDialog.cancel();
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

                for (int i = 0; i <   checkedWedAmTimings.length; i++) {
                    checkedWedAmTimings[i] = false;
                    getmUserItems_WedAmValue.clear();

                }
            }
        });
        AlertDialog mDialog1 = mBuilder2.create();
        mDialog1.show();
    }


    public void MyThursdayCustomAlertDialog(){

//        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorAddAddress.this);
//        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorAddAddress.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.appointment_count_alert);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);

        ok_btn = (ImageView) MyDialog.findViewById(R.id.ok);
        cancel_btn = (ImageView) MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DoctorAddAddress.this,
                        "Timings", Toast.LENGTH_LONG).show();
                thuAppointmentsCount = appointments.getText().toString();
                System.out.println("thur count..."+thuAppointmentsCount);
                showThuAlert(thuAppointmentsCount);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.show();

    }

    public void showThuAlert(String txt)
    {
        getmUserItemsThurItems = new ArrayList<>();

        TextView inputtext;
        final int value=Integer.parseInt(txt);
        System.out.println("value"+value);

        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorAddAddress.this);

        mBuilder2.setTitle("Appointments: "+txt);
        mBuilder2.setMultiChoiceItems(allItems, checkedThuAmTimings, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    String i = Integer.toString(position);
                    getmUserItems_ThurAmValue.add(i);
                }else{
                    getmUserItems_ThurAmValue.remove((Integer.valueOf(position)));
                }
            }
        });

//        mBuilder2.setCancelable(false);
        mBuilder2.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";

                for (int i = 0; i <  getmUserItems_ThurAmValue.size(); i++) {
                    item = item + allItems[Integer.parseInt(getmUserItems_ThurAmValue.get(i))];
                    if (i != getmUserItems_ThurAmValue.size() - 1) {
                        item = item + ",";
                        count ++;
                    }
                }
                System.out.println("count"+count);
                getmUserItemsThurItems.add(item);

                map.put("4", getmUserItemsThurItems);

                MyDialog.cancel();
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

                for (int i = 0; i <   checkedThuAmTimings.length; i++) {
                    checkedThuAmTimings[i] = false;
                    getmUserItems_ThurAmValue.clear();
                }
            }
        });
        AlertDialog mDialog1 = mBuilder2.create();
        mDialog1.show();
    }

    public void MyFridayCustomAlertDialog(){

//        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorAddAddress.this);
//        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorAddAddress.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.doctor_insert_timings);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);

        ok_btn = (ImageView) MyDialog.findViewById(R.id.ok);
        cancel_btn = (ImageView) MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DoctorAddAddress.this,
                        "Timings", Toast.LENGTH_LONG).show();
                friAppointmentsCount = appointments.getText().toString();
                System.out.println("fri count..."+friAppointmentsCount);
                showFriAlert(friAppointmentsCount);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.show();

    }

    public void showFriAlert(String txt)
    {
        getmUserItemsFriItems = new ArrayList<>();

        TextView inputtext;
        final int value=Integer.parseInt(txt);
        System.out.println("value"+value);

        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorAddAddress.this);

        mBuilder2.setTitle("Appointments: "+txt);
        mBuilder2.setMultiChoiceItems(allItems, checkedFriAmTimings, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    String i = Integer.toString(position);
                    getmUserItems_FriAmValue.add(i);
                }else{
                    getmUserItems_FriAmValue.remove((Integer.valueOf(position)));
                }
            }
        });

//        mBuilder2.setCancelable(false);
        mBuilder2.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";

                for (int i = 0; i <  getmUserItems_FriAmValue.size(); i++) {
                    item = item + allItems[Integer.parseInt(getmUserItems_FriAmValue.get(i))];
                    if (i != getmUserItems_FriAmValue.size() - 1) {
                        item = item + ",";
                        count ++;
                    }
                }
                System.out.println("count"+count);
                getmUserItemsFriItems.add(item);

                map.put("5", getmUserItemsFriItems);

                MyDialog.cancel();
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

                for (int i = 0; i <   checkedFriAmTimings.length; i++) {
                    checkedFriAmTimings[i] = false;
                    getmUserItems_FriAmValue.clear();
                }
            }
        });
        AlertDialog mDialog1 = mBuilder2.create();
        mDialog1.show();
    }


    public void MySaturdayCustomAlertDialog(){

//        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorAddAddress.this);
//        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorAddAddress.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.appointment_count_alert);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);

        ok_btn = (ImageView) MyDialog.findViewById(R.id.ok);
        cancel_btn = (ImageView) MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DoctorAddAddress.this,
                        "Timings", Toast.LENGTH_LONG).show();
                satAppointmentsCount = appointments.getText().toString();
                System.out.println("sat count..."+satAppointmentsCount);
                showSatAlert(satAppointmentsCount);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.show();

    }


    public void showSatAlert(String txt)
    {
        getmUserItemsSatItems = new ArrayList<>();

        TextView inputtext;
        final int value=Integer.parseInt(txt);
        System.out.println("value"+value);

        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorAddAddress.this);

        mBuilder2.setTitle("Appointments: "+txt);
        mBuilder2.setMultiChoiceItems(allItems, checkedSatAmTimings, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    String i = Integer.toString(position);
                    getmUserItems_SatAmValue.add(i);
                }else{
                    getmUserItems_SatAmValue.remove((Integer.valueOf(position)));
                }
            }
        });

//        mBuilder2.setCancelable(false);
        mBuilder2.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";

                for (int i = 0; i <  getmUserItems_SatAmValue.size(); i++) {
                    item = item + allItems[Integer.parseInt(getmUserItems_SatAmValue.get(i))];
                    if (i != getmUserItems_SatAmValue.size() - 1) {
                        item = item + ",";
                        count ++;
                    }
                }
                System.out.println("count"+count);
                getmUserItemsSatItems.add(item);

                map.put("6", getmUserItemsSatItems);

                MyDialog.cancel();
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

                for (int i = 0; i <   checkedSatAmTimings.length; i++) {
                    checkedSatAmTimings[i] = false;
                    getmUserItems_SatAmValue.clear();
                }
            }
        });
        AlertDialog mDialog1 = mBuilder2.create();
        mDialog1.show();
    }


    public static Object getCityKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    public static Object getStateKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    public static Object getTimeKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }


    private String formatDataAsJson()
    {

        JSONObject data = new JSONObject();

//        System.out.println("emergency contact..."+Emergency_mobile);

        myHospitalName = hospitalName.getText().toString().trim();
        myAddress = address.getText().toString().trim();
        myPincode = pincode.getText().toString().trim();
        myContactPerson = contactPerson.getText().toString();
        myFee = fee.getText().toString();
        myMobile = mobileNumber.getText().toString().trim();
        myComments = comments.getText().toString().trim();
        myLati = lat.getText().toString().trim();
        myLngi = lng.getText().toString().trim();
        myCity= city.getSelectedItem().toString();
        myState= state.getSelectedItem().toString();
        myDistrict= district.getSelectedItem().toString();



        if(availableService.isChecked()){
            myAvailableService = true;
            myEmergencyContact = emergencyContactNumber.getText().toString().trim();
        }
        else if(!availableService.isChecked())
        {
            myAvailableService = false;
            myEmergencyContact = "";
        }

        try{
            if(availableService.isChecked())
            {
                data.put("DoctorID",getUserId);
                data.put("Address1",myAddress);
                data.put("HospitalName",myHospitalName);

                data.put("StateID",getStateKeyFromValue(myStatesList,myState));
                data.put("CityID",getCityKeyFromValue(myCitiesList,myCity));

                data.put("ZipCode",myPincode);
                data.put("LandlineNo",myMobile);
                data.put("EmergencyContact",myEmergencyContact);
                data.put("District",myDistrict);
                data.put("FrontofficeContactPerson",myContactPerson);

                data.put("ConsultationFee",myFee);
                data.put("EmergencyService", myAvailableService);
                data.put("Latitude",myLati);
                data.put("Longitude", myLngi);
                data.put("PromotionalOffer", myComments);///

                return data.toString();
            }
            else if(!availableService.isChecked())
            {
                data.put("DoctorID",getUserId);
                data.put("Address1",myAddress);
                data.put("HospitalName",myHospitalName);

                data.put("StateID",getStateKeyFromValue(myStatesList,myState));
                data.put("CityID",getCityKeyFromValue(myCitiesList,myCity));

                data.put("ZipCode",myPincode);
                data.put("LandlineNo",myMobile);

                data.put("EmergencyContact",myEmergencyContact);

                data.put("District",myDistrict);
                data.put("FrontofficeContactPerson",myContactPerson);

                data.put("ConsultationFee",myFee);
                data.put("EmergencyService", myAvailableService);
                data.put("Latitude",myLati);
                data.put("Longitude", myLngi);
                data.put("PromotionalOffer", myComments);///

                return data.toString();
            }

        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }

    //send doctor add address details
    private class sendDoctorAddAdressDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

//            HttpURLConnection connection=null;
            HttpURLConnection httpURLConnection = null;
            try {
                System.out.println("dsfafssss....");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setUseCaches(false);
//                httpURLConnection.setChunkedStreamingMode(1024);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
//                httpURLConnection.setRequestProperty("Accept", "application/json");
                Log.d("Service","Started");
//                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                //write
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                System.out.println("params doc add....."+params[1]);
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
            Log.e("TAG result doc add   ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            JSONObject js;

            try {
                js= new JSONObject(result);
                int s = js.getInt("Code");
                if(s == 200)
                {
                    addressId = js.getString("DataValue");
//                    showSuccessMessage(js.getString("Message"));
                }
                else
                {
                    showErrorMessage(js.getString("Message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    private class insertDoctorAppointmentTimings extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
//            progressDialog = new ProgressDialog(DoctorAddAddress.this);
//            // Set progressdialog title
////            progressDialog.setTitle("You are logging");
//            // Set progressdialog message
//            progressDialog.setMessage("Loading");
//
//            progressDialog.setIndeterminate(false);
//            // Show progressdialog
//            progressDialog.show();

            progressDialog = new ProgressDialog(DoctorAddAddress.this);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
            progressDialog.setContentView(R.layout.myprogress);
        }

        @Override
        protected String doInBackground(String... params) {

            String data = "";

//            HttpURLConnection connection=null;
            HttpURLConnection httpURLConnection = null;
            try {
                System.out.println("dsfafssss....");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setUseCaches(false);
//                httpURLConnection.setChunkedStreamingMode(1024);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
//                httpURLConnection.setRequestProperty("Accept", "application/json");
                Log.d("Service","Started");
//                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                //write
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                System.out.println("params doc add....."+params[1]);
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
            Log.e("TAG result doc add   ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            progressDialog.dismiss();
            JSONObject js;

            try {
                js= new JSONObject(result);
                int s = js.getInt("Code");
                if(s == 200)
                {

                    showSuccessMessage(js.getString("Message"));
                }
                else
                {
                    showErrorMessage(js.getString("Message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    public void showSuccessMessage(String result){

        MyDialog1  = new Dialog(DoctorAddAddress.this);
        MyDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog1.setContentView(R.layout.success_alert);

        message = (TextView) MyDialog1.findViewById(R.id.message);
        oklink = (LinearLayout) MyDialog1.findViewById(R.id.ok);

        message.setEnabled(true);
        oklink.setEnabled(true);

        message.setText(result);

        oklink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorAddAddress.this,DoctorDashboard.class);
                intent.putExtra("id",getUserId);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
            }
        });
        MyDialog1.show();

    }

    public void showErrorMessage(String result){

        MyDialog1  = new Dialog(DoctorAddAddress.this);
        MyDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog1.setContentView(R.layout.cancel_alertdialog);

        message = (TextView) MyDialog1.findViewById(R.id.message);
        oklink = (LinearLayout) MyDialog1.findViewById(R.id.ok);

        message.setEnabled(true);
        oklink.setEnabled(true);

        message.setText(result);

        oklink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog1.cancel();
            }
        });
        MyDialog1.show();

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

                System.out.print("hellooooooooooooooooo....");
                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                Long cityKey = jsonObj.getLong("Key");
                String cityValue = jsonObj.getString("Value");
                myCitiesList.put(cityKey,cityValue);
                citiesList.add(jsonObj.getString("Value"));
//                System.out.print("mycity list.."+myCitiesList);
//                System.out.print("city list.."+citiesList);
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
