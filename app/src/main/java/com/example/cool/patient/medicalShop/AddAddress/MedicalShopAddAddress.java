package com.example.cool.patient.medicalShop.AddAddress;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.common.ChangePassword;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.common.MapsActivity;
import com.example.cool.patient.common.Offers;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.diagnostic.AddAddress.DiagnosticAddAddress;
import com.example.cool.patient.medicalShop.ManageAddress.MedicalShopManageAddress;
import com.example.cool.patient.medicalShop.MedicalChangePassword;
import com.example.cool.patient.medicalShop.MedicalShopDashboard;
import com.example.cool.patient.R;
import com.example.cool.patient.medicalShop.MedicalShopEditProfile;
import com.example.cool.patient.medicalShop.MedicalShopSideNavigatioExpandableSubList;
import com.example.cool.patient.medicalShop.MedicalShopSideNavigationExpandableListAdapter;
import com.example.cool.patient.subscriptionPlan.SubscriptionPlanAlertDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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

public class MedicalShopAddAddress extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    EditText MedicalName,address,pincode,contactPerson,mobile,landlineMobileNumber,comments,lat,lng,Experence,Emeregency_contact;
    EditText fromTime,toTime;
    SearchableSpinner city,state,district,Pharmacy_type;
    CheckBox availableService;
    ImageView centerImage;
    FloatingActionButton addCenterIcon;
    MagicButton btn_AddAddress;
    static String uploadServerUrl = null,addressId ;
    static String  myEmergencyContact,myExperience,myFromTime,myToTime,mypharmacytype,myMedicalName,myHospitalName,myAddress,myPincode,myContactPerson,myMobile,myLandlineMobileNumber,myComments,myLati,myLngi,myCity,myState,myDistrict;
    boolean myAvailableService;
    static String medicalId,medicalMobile;
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
    ArrayAdapter<String> adapter5;
    HashMap<Long, String> myCitiesList = new HashMap<Long, String>();
    HashMap<Long, String> myStatesList = new HashMap<Long, String>();
    List<String>pharmacyTypeList;
    HashMap<Long, String> mypharmacyTypeList = new HashMap<>();
    HashMap<Long, String> myAmTimeSlotsList = new HashMap<Long, String>();
    HashMap<Long, String> myPmTimeSlotsList = new HashMap<Long, String>();
    List<String> myDistrictsList = new ArrayList<String>();

    LinearLayout emergencyContactLayout;

    final int REQUEST_CODE_GALLERY1 = 999;
    Uri selectedCenterImageUri;
    Bitmap selectedCenterImageBitmap = null;
    String encodedCenterImage;

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

    // expandable list view

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    //sidenav fields
    TextView sidenavName,sidenavEmail,sidenavMobile;

    Dialog MyDialog;
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
        setContentView(R.layout.activity_medical_shop_add_address);

        baseUrl = new ApiBaseUrl();

        medicalId = getIntent().getStringExtra("id");
        medicalMobile = getIntent().getStringExtra("mobile");
        System.out.print("Medical in add address....."+medicalId);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        new GetAllCities().execute(baseUrl.getUrl()+"GetAllCity");

        new GetAllStates().execute(baseUrl.getUrl()+"GetAllState");

        new GetAllDistricts().execute(baseUrl.getUrl()+"GetAllDistrict");

        new GetMedicalDetails().execute(baseUrl.getUrl()+"MedicalShopByID"+"?id="+medicalId);

        new GetAllMedicalPharmacyList().execute(baseUrl.getUrl()+"GetPharmacyType");


        MedicalName = (EditText) findViewById(R.id.Medical_Hospital_name);
        address = (EditText) findViewById(R.id.Address);
        Experence =(EditText) findViewById(R.id.Experence);
        city = (SearchableSpinner) findViewById(R.id.cityId);
        state = (SearchableSpinner) findViewById(R.id.stateId);
        district = (SearchableSpinner) findViewById(R.id.districtId);
        Pharmacy_type =(SearchableSpinner) findViewById(R.id.Pharmacy_type);
        mobile = (EditText) findViewById(R.id.Mobile_Number);
        pincode = (EditText) findViewById(R.id.pincode);
        contactPerson = (EditText) findViewById(R.id.Frontoffice);
        Emeregency_contact =(EditText) findViewById(R.id.Emergency_Contact);
        landlineMobileNumber = (EditText) findViewById(R.id.landMobileNumber);
        comments = (EditText) findViewById(R.id.Comments_Others);
        getLatLong = findViewById(R.id.getlatlng);
        lat = (EditText) findViewById(R.id.Latitude);
        lng = (EditText) findViewById(R.id.Longitude);
        availableService = (CheckBox) findViewById(R.id.serviceAvailable);
        fromTime = findViewById(R.id.From);
        toTime = findViewById(R.id.To_Timing);
        //speciality = (TextView) findViewById(R.id.Select_Speciality);
        chooseTime = findViewById(R.id.From);
        ToTime = findViewById(R.id.To_Timing);



        centerImage = (ImageView) findViewById(R.id.diag_center_image);
        addCenterIcon = (FloatingActionButton) findViewById(R.id.addDiagCenterIcon);

        emergencyContactLayout = (LinearLayout)findViewById(R.id.emergencyContactLayout);

        availableService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewEmergencyContactField();
            }
        });


        getLatLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAddAddress();
            }
        });

        System.out.print("Medical in add address comments....."+comments.getText().toString());
//        myLatitude = getIntent().getStringExtra("lat");
//        myLongitude = getIntent().getStringExtra("lng");

        final RippleView rippleView = (RippleView) findViewById(R.id.rippleView);

        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateFullAddress();
            }

        });
        addCenterIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(
                                MedicalShopAddAddress.this,
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

                timePickerDialog = new TimePickerDialog(MedicalShopAddAddress.this, new TimePickerDialog.OnTimeSetListener() {
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

                timePickerDialog = new TimePickerDialog(MedicalShopAddAddress.this, new TimePickerDialog.OnTimeSetListener() {
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("Add Address");
//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(MedicalShopAddAddress.this,MedicalShopDashboard.class);
//                        intent.putExtra("id",getUserId);
//                        startActivity(intent);
//
//                    }
//                }
//
//        );

        //side navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_medical_shop_dashboard);

        sidenavName = (TextView) navigationView.findViewById(R.id.name);
        sidenavEmail = (TextView) navigationView.findViewById(R.id.emailId);
        sidenavMobile  = (TextView) navigationView.findViewById(R.id.mobile);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView1);
        expandableListDetail = MedicalShopSideNavigatioExpandableSubList.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new MedicalShopSideNavigationExpandableListAdapter(this, expandableListTitle, expandableListDetail);
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

                if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.Services) {
                    retVal = false;
                } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.Address) {
                    retVal = false;
                } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM3) {
                    retVal = false;

                }

                else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM4) {
                    // call some activity here
                    Intent contact = new Intent(MedicalShopAddAddress.this,MedicalShopEditProfile.class);
                    contact.putExtra("id",medicalId);
                    contact.putExtra("mobile",medicalMobile);
                    contact.putExtra("user","old");
                    startActivity(contact);

                }

                else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                    Intent about = new Intent(MedicalShopAddAddress.this,SubscriptionPlanAlertDialog.class);
                    about.putExtra("id",medicalId);
                    about.putExtra("mobile",medicalMobile);
                    about.putExtra("module","medical");
                    startActivity(about);

                } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here
                    Intent contact = new Intent(MedicalShopAddAddress.this,Offers.class);
                    contact.putExtra("id",medicalId);
                    contact.putExtra("mobile",medicalMobile);
                    contact.putExtra("module","medical");
                    startActivity(contact);

                } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(MedicalShopAddAddress.this,ReachUs.class);
                    contact.putExtra("id",medicalId);
                    contact.putExtra("mobile",medicalMobile);
                    contact.putExtra("module","medical");
                    startActivity(contact);

                }

                else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here
                    Intent contact = new Intent(MedicalShopAddAddress.this,Login.class);
                    startActivity(contact);

                }

                return retVal;
            }
        });


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


//                if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.Services) {
//                    if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM1_1) {
//
//                        Intent i = new Intent(DiagnosticDashboard.this,DiagnosticDashboard.class);
//                        startActivity(i);
//
//                    }
//                    else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM1_2) {
//
//                        // call activity here
//
////                        Intent i = new Intent(DiagnosticDashboard.this,DoctorTodaysAppointmentsForPatient.class);
////                        i.putExtra("userId",getUserId);
////                        startActivity(i);
//
//                    }
////                    else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM1_3) {
////
////                        // call activity here
////
////                    }
//
//
//                }
                if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM3) {

                    if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM3_1) {

                        // call activity here

                        Intent about = new Intent(MedicalShopAddAddress.this,MedicalChangePassword.class);
                        about.putExtra("id",medicalId);
                        about.putExtra("mobile",myMobile);
                        startActivity(about);

                    }
                    else if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                } else if(groupPosition == MedicalShopSideNavigationExpandableListAdapter.Address) {
                    if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM2_1) {

                        Intent about = new Intent(MedicalShopAddAddress.this,MedicalShopAddAddress.class);
                        about.putExtra("id",medicalId);
                        about.putExtra("mobile",medicalMobile);
                        startActivity(about);

                    }
                    else if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent about = new Intent(MedicalShopAddAddress.this,MedicalShopManageAddress.class);
                        about.putExtra("id",medicalId);
                        about.putExtra("mobile",medicalMobile);
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
        if (ActivityCompat.checkSelfPermission(MedicalShopAddAddress.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MedicalShopAddAddress.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.print("helo this is if");

            ActivityCompat.requestPermissions(MedicalShopAddAddress.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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

            Log.e("TAG result medicprofile", result); // this is expecting a response code to be sent from your server upon receiving the POST data
//            progressDialog.dismiss();
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

            sidenavName.setText(myName+" "+mySurname);
            sidenavEmail.setText(myEmail);
            sidenavMobile.setText(myMobile);


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    //home icon
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

            Intent intent = new Intent(MedicalShopAddAddress.this,MedicalShopDashboard.class);
            intent.putExtra("id",medicalId);
            intent.putExtra("mobile",medicalMobile);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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
            Intent intent = new Intent(MedicalShopAddAddress.this,MapsActivity.class);
            intent.putExtra("doc","medicAdd");
            intent.putExtra("id",medicalId);
            intent.putExtra("regMobile",medicalMobile);
            intent.putExtra("diagName",MedicalName.getText().toString());
            intent.putExtra("address",address.getText().toString());
            intent.putExtra("Experience",Experence.getText().toString());
            intent.putExtra("city",city.getSelectedItem().toString());
            intent.putExtra("state",state.getSelectedItem().toString());
            intent.putExtra("district",district.getSelectedItem().toString());
            intent.putExtra("PharmacyType",Pharmacy_type.getSelectedItem().toString());
            intent.putExtra("mobile",mobile.getText().toString());
            intent.putExtra("pincode",pincode.getText().toString());
            intent.putExtra("person",contactPerson.getText().toString());
            intent.putExtra("landmobile",landlineMobileNumber.getText().toString());
            intent.putExtra("comments",comments.getText().toString());
            intent.putExtra("lati",lattitude);
            intent.putExtra("longi",longitude);
            startActivity(intent);
        }
    }

    public boolean validate()
    {
        boolean validate = true;
        if(MedicalName.getText().toString().trim().isEmpty())
        {
            MedicalName.setError("please enter the name");
            validate  = false;

        }
        if(address.getText().toString().trim().isEmpty())
        {
            address.setError("please enter the address");
            validate  = false;

        }
        if(pincode.getText().toString().trim().isEmpty())
        {
            pincode.setError("please enter the pincode");
            validate  = false;

        }
        if( contactPerson.getText().toString().trim().isEmpty())
        {
            contactPerson.setError("please enter contactperson");
            validate  = false;

        }
        if( Experence.getText().toString().trim().isEmpty())
        {
            Experence.setError("please enter Experience");
            validate  = false;

        }
        if(mobile.getText().toString().trim().isEmpty() || !Patterns.PHONE.matcher(mobile.getText().toString().trim()).matches())
        {
            mobile.setError("please enter the mobile number");
            validate=false;
        }
        else if(mobile.getText().toString().trim().length()<10 || mobile.getText().toString().trim().length()>10)
        {
            mobile.setError(" Invalid phone number ");
            validate=false;
        }
        if(landlineMobileNumber.getText().toString().trim().isEmpty() || !Patterns.PHONE.matcher(landlineMobileNumber.getText().toString().trim()).matches())
        {
            landlineMobileNumber.setError("please enter the mobile number");
            validate=false;
        }
        else if(landlineMobileNumber.getText().toString().trim().length()<10 || landlineMobileNumber.getText().toString().trim().length()>10)
        {
            landlineMobileNumber.setError(" Invalid phone number ");
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

            System.out.println("js..."+js.toString());
            new sendEditProfileDetails().execute(baseUrl.getUrl()+"MSAddAddress",js.toString());
        }
    }

    public boolean addressValidate()
    {
        boolean validate = true;
        if(MedicalName.getText().toString().trim().isEmpty())
        {
            MedicalName.setError("please enter the name");
            validate  = false;

        }
        if(address.getText().toString().trim().isEmpty())
        {
            address.setError("please enter the address");
            validate  = false;

        }
        if(pincode.getText().toString().trim().isEmpty())
        {
            pincode.setError("please enter the pincode");
            validate  = false;

        }
        if( contactPerson.getText().toString().trim().isEmpty())
        {
            contactPerson.setError("please enter contactperson");
            validate  = false;

        }
        if( Experence.getText().toString().trim().isEmpty())
        {
            Experence.setError("please enter Experience");
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

        if(mobile.getText().toString().trim().isEmpty() || !Patterns.PHONE.matcher(mobile.getText().toString().trim()).matches())
        {
            mobile.setError("please enter the mobile number");
            validate=false;
        }
        else if(mobile.getText().toString().trim().length()<10 || mobile.getText().toString().trim().length()>10)
        {
            mobile.setError(" Invalid phone number ");
            validate=false;
        }

        if(availableService.isChecked() == true) {

                if(Emeregency_contact.getText().toString().isEmpty() || !Patterns.PHONE.matcher(Emeregency_contact.getText().toString()).matches())
                {
                    Emeregency_contact.setError("please enter valid number");
                    validate=false;
                }

                else if (Emeregency_contact.getText().toString().length() < 10 || Emeregency_contact.getText().toString().length() > 10) {
                    Emeregency_contact.setError(" Invalid phone number ");
                    validate = false;
                }
            }

        else
        {

            validate = true;

        }

        if(landlineMobileNumber.getText().toString().trim().isEmpty() || !Patterns.PHONE.matcher(landlineMobileNumber.getText().toString().trim()).matches())
        {
            landlineMobileNumber.setError("please enter the mobile number");
            validate=false;
        }
        else if(landlineMobileNumber.getText().toString().trim().length()<10 || landlineMobileNumber.getText().toString().trim().length()>10)
        {
            landlineMobileNumber.setError(" Invalid phone number ");
            validate=false;
        }

        return validate;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

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

            try {
                js= new JSONObject(result);
                int s = js.getInt("Code");
                if(s == 200)
                {
                    addressId = js.getString("DataValue");
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

        MyDialog  = new Dialog(MedicalShopAddAddress.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.success_alert);

        message = (TextView) MyDialog.findViewById(R.id.message);
        oklink = (LinearLayout) MyDialog.findViewById(R.id.ok);

        message.setEnabled(true);
        oklink.setEnabled(true);

        message.setText(result);

        oklink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicalShopAddAddress.this,MedicalShopDashboard.class);
                intent.putExtra("id",medicalId);
                intent.putExtra("mobile",medicalMobile);
                startActivity(intent);
            }
        });
        MyDialog.show();

//        AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
//
//        a_builder.setMessage(message)
//                .setCancelable(false)
//                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        dialog.cancel();
//                        Intent intent = new Intent(MedicalShopAddAddress.this,MedicalShopDashboard.class);
//                        intent.putExtra("id",medicalId);
//                        intent.putExtra("mobile",medicalMobile);
//                        startActivity(intent);
//                    }
//                });
//        AlertDialog alert = a_builder.create();
//        alert.setTitle("Edit Profile");
//        alert.show();

    }

    public void showErrorMessage(String result){

        MyDialog  = new Dialog(MedicalShopAddAddress.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.cancel_alertdialog);

        message = (TextView) MyDialog.findViewById(R.id.message);
        oklink = (LinearLayout) MyDialog.findViewById(R.id.ok);

        message.setEnabled(true);
        oklink.setEnabled(true);

        message.setText(result);

        oklink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });
        MyDialog.show();

//        AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
//
//        a_builder.setMessage(message)
//                .setCancelable(false)
//                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = a_builder.create();
//        alert.setTitle("Edit Profile");
//        alert.show();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY1);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GALLERY1) {
//            onSelectFromGalleryResult(data);
//             Make sure the request was successful
            Log.d("hello","I'm out.");
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {

                selectedCenterImageUri = data.getData();
                BufferedWriter out=null;
                try {
                    selectedCenterImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedCenterImageUri);

                    //certificate base64
                    final InputStream imageStream = getContentResolver().openInputStream(selectedCenterImageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//            encodedImage = myEncodeImage(selectedImage);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] b = baos.toByteArray();
                    encodedCenterImage = Base64.encodeToString(b, Base64.DEFAULT);

                }
                catch (IOException e)
                {
                    System.out.println("Exception ");

                }
                centerImage.setImageBitmap(selectedCenterImageBitmap);
                Log.d("hello","I'm in.");

            }
        }

        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String formatDataAsJson()
    {

        JSONObject data = new JSONObject();

        myMedicalName = MedicalName.getText().toString().trim();
        myAddress = address.getText().toString().trim();
        myPincode = pincode.getText().toString().trim();
        myContactPerson = contactPerson.getText().toString();
        myMobile = mobile.getText().toString();

        myLandlineMobileNumber = landlineMobileNumber.getText().toString().trim();
        myComments = comments.getText().toString().trim();
        myLati = lat.getText().toString().trim();
        myLngi = lng.getText().toString().trim();
        myCity= city.getSelectedItem().toString();
        myState= state.getSelectedItem().toString();
        mypharmacytype = Pharmacy_type.getSelectedItem().toString();

//        mySpeciality = speciality.///////////
        myDistrict= district.getSelectedItem().toString();
        myFromTime = fromTime.getText().toString();
        myToTime = toTime.getText().toString();

        if(availableService.isChecked()){
            myAvailableService = true;
            myEmergencyContact = Emeregency_contact.getText().toString().trim();
        }
        else if(!availableService.isChecked())
        {
            myAvailableService = false;
            myEmergencyContact = "";
        }

        if(encodedCenterImage == null)
        {
            centerImage.buildDrawingCache();
            BitmapDrawable bitmapDrawable = (BitmapDrawable) centerImage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos1);
            byte[] b1 = baos1.toByteArray();
            encodedCenterImage = Base64.encodeToString(b1, Base64.DEFAULT);

//            System.out.println("image view encoded Image..."+encodedCertificateImage);
        }

        try{



            data.put("MedicalShopID",medicalId);
            data.put("ShopName",myMedicalName);
            data.put("Address1",myAddress);
            data.put("Experience",myExperience);
            data.put("MobileNumber",myMobile);
            data.put("StateID",getStateKeyFromValue(myStatesList,myState));
            data.put("CityID",getCityKeyFromValue(myCitiesList,myCity));
            data.put("PharmacyType",getPharmacyTyprKeyFromValue(mypharmacyTypeList,mypharmacytype));
            data.put("District",myDistrict);
            data.put("PinCode",myPincode);
            data.put("LandlineNo",myLandlineMobileNumber);
            data.put("ContactPerson",myContactPerson);
            data.put("Comment", myComments);
            data.put("EmergencyService", myAvailableService);
            data.put("EmergencyContact",myEmergencyContact);
//            data.put("Speciality",mySpeciality);////////

            data.put("Latitude",myLati);
            data.put("Longitude", myLngi);
            data.put("FromTime", myFromTime);
            data.put("ToTime", myToTime);
            data.put("ShopImage", encodedCenterImage);
            data.put("District",myDistrict);


            return data.toString();

        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
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
    public static Object getPharmacyTyprKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }


    public void showalert() {

        timePickerDialog1 = new TimePickerDialog(MedicalShopAddAddress.this, new TimePickerDialog.OnTimeSetListener() {
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
            Pharmacy_type.setAdapter(adapter5); // Apply the adapter to the spinner

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
