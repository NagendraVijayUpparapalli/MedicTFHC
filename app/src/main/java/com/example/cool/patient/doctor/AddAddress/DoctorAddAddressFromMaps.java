package com.example.cool.patient.doctor.AddAddress;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.doctor.DashBoardCalendar.DoctorDashboard;
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

public class DoctorAddAddressFromMaps extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    EditText hospitalName,address,pincode,contactPerson,fee,mobileNumber,comments,lat,lng,emergencyContactNumber;
    SearchableSpinner city,state,district;
    CheckBox availableService;
    MagicButton btn_AddAddress;
    RippleView  rippleView;
    Button nextView;
    LinearLayout timingLayout,details_layout,emergencyContactLayout;
    TextView getLatLong;

    //doc timings alert
    Button ok_btn,cancel_btn;
    EditText appointments;
    Button show;
    Dialog MyDialog;
    TextView textTimings;
    LinearLayout layoutTimings;

    //send timings to api

    JSONArray jsonArray = new JSONArray();
    JSONObject data = new JSONObject();

    static String getUserId;
    static String uploadServerUrl = null,addressId,sunAppointmentsCount = "0",
            monAppointmentsCount = "0",tueAppointmentsCount = "0",wedApointmentsCount = "0",
            thuAppointmentsCount = "0",friAppointmentsCount = "0",satAppointmentsCount = "0";


    static String regMobile,userId,myHospitalName,myAddress,myPincode,myContactPerson,myMobile,myFee,
            myComments,myLati,myLngi,myCity,myState,myDistrict,myEmergencyContact,myLatitude,myLongitude;
    boolean myAvailableService;


    List<String> districtsList,citiesList,statesList,amTimeSlotsList,pmTimeSlotsList,allItemsList;
    String amItems[],pmItems[],allItems[];
    String myQrArrayList;
    String[] mydistrictlist;
    ArrayAdapter<String> adapter2,adapter3,adapter4;
    HashMap<Long, String> myCitiesList = new HashMap<Long, String>();
    HashMap<Long, String> myStatesList = new HashMap<Long, String>();

    HashMap<String, String> AllTimeSlotsList = new HashMap<String, String>();
    int count;

    List<String> myDistrictsList = new ArrayList<String>();

    CardView sunday,monday,tuesday,wednesday,thursday,friday,saturday;

    static Map<String, List<String>> map = new HashMap<String, List<String>>();

    boolean[] checkedSunAmTimings,checkedSunPmTimings,checkedMonAmTimings,checkedMonPmTimings,checkedTueAmTimings,checkedTuePmTimings,
            checkedWedAmTimings,checkedWedPmTimings,checkedThuAmTimings,checkedThuPmTimings,checkedFriAmTimings,checkedFriPmTimings,
            checkedSatAmTimings,checkedSatPmTimings;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_add_address_from_maps);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("Add Address");
//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(DoctorAddAddressFromMaps.this,DoctorDashboard.class);
//                        intent.putExtra("id",userId);
//                        startActivity(intent);
//
//                    }
//                }
//        );

        baseUrl = new ApiBaseUrl();
        citiesList = new ArrayList<>();
        statesList = new ArrayList<>();
        districtsList = new ArrayList<String>();


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

        emergencyContactNumber = (EditText) findViewById(R.id.emergencyContact);
        emergencyContactLayout = (LinearLayout)findViewById(R.id.emergencyContactLayout);

        new GetAllCities().execute(baseUrl.getUrl()+"GetAllCity");

        new GetAllStates().execute(baseUrl.getUrl()+"GetAllState");

        new GetAllDistricts().execute(baseUrl.getUrl()+"GetAllDistrict");

        new GetTimeSlots().execute(baseUrl.getUrl()+"GetAllTimeSlot");


        regMobile = getIntent().getStringExtra("regMobile");
        userId = getIntent().getStringExtra("id");
        myHospitalName = getIntent().getStringExtra("hospitalName");
        myAddress = getIntent().getStringExtra("address");
        myCity = getIntent().getStringExtra("city");
        myState = getIntent().getStringExtra("state");
        myDistrict = getIntent().getStringExtra("district");
        myMobile = getIntent().getStringExtra("mobile");
        myPincode = getIntent().getStringExtra("pincode");
        myContactPerson = getIntent().getStringExtra("person");
        myFee = getIntent().getStringExtra("fee");
        myLatitude = getIntent().getStringExtra("lat");
        myLongitude = getIntent().getStringExtra("lng");


        hospitalName.setText(myHospitalName);
        address.setText(myAddress);

        mobileNumber.setText(myMobile);
        pincode.setText(myPincode);
        contactPerson.setText(myContactPerson);
        fee.setText(myFee);

        lat.setText(myLatitude);
        lng.setText(myLongitude);

//        lat.setText(String.format("%.6f", myLatitude));
//        lng.setText(String.format("%.6f", myLongitude));

        emergencyContactNumber = (EditText) findViewById(R.id.emergencyContact);
        emergencyContactLayout = (LinearLayout)findViewById(R.id.emergencyContactLayout);
        availableService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewEmergencyContactField();
            }
        });

        nextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFullAddress();
            }
        });


//        btn_AddAddress = (MagicButton) findViewById(R.id.btn_addAddress);

        rippleView=(RippleView)  findViewById(R.id.rippleView);
        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String js = formatDoctorTimingsDataAsJson();
//                System.out.println("js time array"+js.toString());
                new insertDoctorAppointmentTimings().execute(baseUrl.getUrl()+"DoctorInsertTimeSlot",js.toString());
            }
        });


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
                MySaturdayCustomAlertDialog();
            }
        });
        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFridayCustomAlertDialog();
            }
        });

        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyThursdayCustomAlertDialog();
            }
        });

        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyWednesdayCustomAlertDialog();
            }
        });


        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyTuesdayCustomAlertDialog();
            }
        });



        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyMondayCustomAlertDialog();
            }
        });

        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    Intent contact = new Intent(DoctorAddAddressFromMaps.this,DoctorEditProfile.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",regMobile);
                    contact.putExtra("user","old");
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
//                    Intent i = new Intent(DoctorAddAddressFromMaps.this,SubscriptionPlanAlertDialog.class);
//                    i.putExtra("id",getUserId);
//                    i.putExtra("mobile",regMobile);
//                    i.putExtra("module","doc");
//                    startActivity(i);

                } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here
                    Intent contact = new Intent(DoctorAddAddressFromMaps.this,Offers.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",regMobile);
                    contact.putExtra("module","doc");
                    startActivity(contact);

                } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(DoctorAddAddressFromMaps.this,ReachUs.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",regMobile);
                    contact.putExtra("module","doc");
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here
                    Intent contact = new Intent(DoctorAddAddressFromMaps.this,Login.class);
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


//                Toast.makeText(
//                        getApplicationContext(),
//                        expandableListTitle.get(groupPosition)
//                                + " -> "
//                                + expandableListDetail.get(
//                                expandableListTitle.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT
//                ).show();
                if (groupPosition == DoctorSideNavigationExpandableListAdapter.Services) {
                    if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM1_1) {

                        Intent i = new Intent(DoctorAddAddressFromMaps.this,DoctorDashboard.class);
                        i.putExtra("id",getUserId);
                        i.putExtra("mobile",regMobile);
                        startActivity(i);


                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM1_2) {

                        // call activity here

                        Intent i = new Intent(DoctorAddAddressFromMaps.this,DoctorTodaysAppointmentsForPatient.class);
                        i.putExtra("id",getUserId);
                        i.putExtra("mobile",regMobile);
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

                        Intent about = new Intent(DoctorAddAddressFromMaps.this,DoctorChangePassword.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",regMobile);
                        startActivity(about);

                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                } else if(groupPosition == DoctorSideNavigationExpandableListAdapter.Address) {
                    if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_1) {


                        Intent about = new Intent(DoctorAddAddressFromMaps.this,DoctorAddAddress.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",regMobile);
                        startActivity(about);

                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent about = new Intent(DoctorAddAddressFromMaps.this,DoctorManageAddress.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",regMobile);
                        startActivity(about);

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


    //get doctor details based on id from api call
    private class GetDoctorDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(DoctorAddAddressFromMaps.this);
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

            Intent intent = new Intent(DoctorAddAddressFromMaps.this,DoctorDashboard.class);
            intent.putExtra("id",getUserId);
            intent.putExtra("mobile",regMobile);
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

    public void validateFullAddress()
    {
        if(!addressValidate())
        {
//            Toast.makeText(this,"Succesfully field" , Toast.LENGTH_SHORT).show();
        }
        else
        {
            String js = formatDataAsJson();
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
            address.setError("please enter address");
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
        if(emergencyContactNumber.getText().toString().isEmpty() || !Patterns.PHONE.matcher(emergencyContactNumber.getText().toString()).matches())
        {
            emergencyContactNumber.setError("please enter valid number");
            validate=false;
        }
        else if(emergencyContactNumber.getText().toString().length()<10 || emergencyContactNumber.getText().toString().length()>10)
        {
            emergencyContactNumber.setError(" Invalid phone number ");
            validate=false;
        }

        return validate;
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

        myEmergencyContact = emergencyContactNumber.getText().toString().trim();

        if(availableService.isChecked()){
            myAvailableService = true;
        }
        else if(!availableService.isChecked())
        {
            myAvailableService = false;
        }

        try{
            if(availableService.isChecked())
            {
                data.put("DoctorID",userId);
                data.put("Address1",myAddress);
                data.put("HospitalName",myHospitalName);

                data.put("StateID",getStateKeyFromValue(myStatesList,myState));
                data.put("CityID",getCityKeyFromValue(myCitiesList,myCity));

                data.put("ZipCode",myPincode);
                data.put("LandlineNo",myMobile);
                data.put("EmergencyContact",myEmergencyContact);
                data.put("District",myDistrict);
                data.put("FrontofficeContactPerson",myContactPerson);

                data.put("ConsultationFee",myFee);///
                data.put("EmergencyService", myAvailableService);
                data.put("Latitude",myLati);
                data.put("Longitude", myLngi);
                data.put("PromotionalOffer", myComments);///

                return data.toString();
            }
            else if(!availableService.isChecked())
            {
                data.put("DoctorID",userId);
                data.put("Address1",myAddress);
                data.put("HospitalName",myHospitalName);

                data.put("StateID",getStateKeyFromValue(myStatesList,myState));
                data.put("CityID",getCityKeyFromValue(myCitiesList,myCity));

                data.put("ZipCode",myPincode);
                data.put("LandlineNo",myMobile);

//                data.put("EmergencyContact",myEmergencyContact);

                data.put("District",myDistrict);
                data.put("FrontofficeContactPerson",myContactPerson);

                data.put("ConsultationFee",myFee);///
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


    public void MySundayCustomAlertDialog(){

        getmUserItemsSunItems = new ArrayList<>();

//        map = new HashMap<String, List<String>>();

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorAddAddressFromMaps.this);
        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorAddAddressFromMaps.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.doctor_insert_timings);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);

        ok_btn = (Button)MyDialog.findViewById(R.id.ok);
        cancel_btn = (Button)MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DoctorAddAddressFromMaps.this,
                        "Timings", Toast.LENGTH_LONG).show();
                sunAppointmentsCount = appointments.getText().toString();
                System.out.println("count..."+sunAppointmentsCount);
                showSunalert(sunAppointmentsCount);
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

    public void showSunalert(String txt)
    {
        getmUserItemsSunItems = new ArrayList<>();

        getmUserItemsSunPmItems = new ArrayList<>();

//        map = new HashMap<String, List<String>>();

        TextView inputtext;
        final int value=Integer.parseInt(txt);
        System.out.println("value"+value);

        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorAddAddressFromMaps.this);

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

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorAddAddressFromMaps.this);
        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorAddAddressFromMaps.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.doctor_insert_timings);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);

        ok_btn = (Button)MyDialog.findViewById(R.id.ok);
        cancel_btn = (Button)MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DoctorAddAddressFromMaps.this,
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

        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorAddAddressFromMaps.this);

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

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorAddAddressFromMaps.this);
        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorAddAddressFromMaps.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.doctor_insert_timings);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);

        ok_btn = (Button)MyDialog.findViewById(R.id.ok);
        cancel_btn = (Button)MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DoctorAddAddressFromMaps.this,
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

        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorAddAddressFromMaps.this);

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

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorAddAddressFromMaps.this);
        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorAddAddressFromMaps.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.doctor_insert_timings);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);

        ok_btn = (Button)MyDialog.findViewById(R.id.ok);
        cancel_btn = (Button)MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DoctorAddAddressFromMaps.this,
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

        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorAddAddressFromMaps.this);

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

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorAddAddressFromMaps.this);
        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorAddAddressFromMaps.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.doctor_insert_timings);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);

        ok_btn = (Button)MyDialog.findViewById(R.id.ok);
        cancel_btn = (Button)MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DoctorAddAddressFromMaps.this,
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

        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorAddAddressFromMaps.this);

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

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorAddAddressFromMaps.this);
        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorAddAddressFromMaps.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.doctor_insert_timings);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);

        ok_btn = (Button)MyDialog.findViewById(R.id.ok);
        cancel_btn = (Button)MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DoctorAddAddressFromMaps.this,
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

        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorAddAddressFromMaps.this);

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

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorAddAddressFromMaps.this);
        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorAddAddressFromMaps.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.doctor_insert_timings);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);

        ok_btn = (Button)MyDialog.findViewById(R.id.ok);
        cancel_btn = (Button)MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DoctorAddAddressFromMaps.this,
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

        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorAddAddressFromMaps.this);

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
                addressId = js.getString("DataValue");
//                if(s == 1017)
//                {
//                    addressId = js.getString("DataValue");
////                    showSuccessMessage(js.getString("Message"));
//                }
//                else
//                {
//                    showErrorMessage(js.getString("Message"));
//                }

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
            progressDialog = new ProgressDialog(DoctorAddAddressFromMaps.this);
            // Set progressdialog title
//            progressDialog.setTitle("You are logging");
            // Set progressdialog message
            progressDialog.setMessage("Loading");

            progressDialog.setIndeterminate(false);
            // Show progressdialog
            progressDialog.show();
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

        MyDialog1  = new Dialog(DoctorAddAddressFromMaps.this);
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
                Intent intent = new Intent(DoctorAddAddressFromMaps.this,DoctorDashboard.class);
                intent.putExtra("id",userId);
                intent.putExtra("mobile",regMobile);
                startActivity(intent);
            }
        });
        MyDialog1.show();
    }

    public void showErrorMessage(String result){

        MyDialog1  = new Dialog(DoctorAddAddressFromMaps.this);
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

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                Long cityKey = jsonObj.getLong("Key");
                String cityValue = jsonObj.getString("Value");
                myCitiesList.put(cityKey,cityValue);
                citiesList.add(jsonObj.getString("Value"));
                System.out.print("mycity list.."+myCitiesList);
                System.out.print("city list.."+citiesList);
            }

            citiesList.add(0,myCity);
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
            statesList.add(0,myState);
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
            districtsList.add(0,myDistrict);
            adapter4 = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, districtsList);
            adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
            district.setAdapter(adapter4); // Apply the adapter to the spinner
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
