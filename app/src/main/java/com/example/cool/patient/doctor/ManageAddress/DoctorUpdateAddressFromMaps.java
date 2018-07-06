
package com.example.cool.patient.doctor.ManageAddress;

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

import com.andexert.library.RippleView;
import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.common.ChangePassword;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.common.Offers;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.doctor.AddAddress.DoctorAddAddress;
import com.example.cool.patient.doctor.DashBoardCalendar.DoctorDashboard;
import com.example.cool.patient.R;
import com.example.cool.patient.doctor.DoctorChangePassword;
import com.example.cool.patient.doctor.DoctorEditProfile;
import com.example.cool.patient.doctor.DoctorSideNavigatioExpandableSubList;
import com.example.cool.patient.doctor.DoctorSideNavigationExpandableListAdapter;
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

public class DoctorUpdateAddressFromMaps extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    EditText hospitalName,address,pincode,contactPerson,fee,landlineMobileNumber,comments,lat,lng,emergencyContactNumber;
    SearchableSpinner city,state,district;
    CheckBox availableService;
    MagicButton btn_AddAddress;

    List<String> districtsList,citiesList,statesList;

    RippleView rippleView;
    //doc timings alert
    Button ok_btn,cancel_btn;
    EditText appointments;
    Button show;
    Dialog MyDialog;
    TextView textTimings;
    LinearLayout layoutTimings;

    List<String> allItemsList, prevSunTimeSlotsList,prevMonTimeSlotsList,prevTueTimeSlotsList,prevWedTimeSlotsList,prevThurTimeSlotsList,prevFriTimeSlotsList,prevSatTimeSlotsList;

    Button sunday,monday,tuesday,wednesday,thursday,friday,saturday;
    String allItems[],allSunPrevItems[],allMonPrevItems[],allTuePrevItems[],allWedPrevItems[],allThurPrevItems[],allFriPrevItems[],allSatPrevItems[];

    static Map<String, List<String>> map = new HashMap<String, List<String>>();
    HashMap<String, String> AllTimeSlotsList = new HashMap<String, String>();

    boolean[] checkedSunAmTimings,checkedSunPmTimings,checkedMonAmTimings,checkedMonPmTimings,checkedTueAmTimings,checkedTuePmTimings,
            checkedWedAmTimings,checkedWedPmTimings,checkedThuAmTimings,checkedThuPmTimings,checkedFriAmTimings,checkedFriPmTimings,
            checkedSatAmTimings,checkedSatPmTimings;

    boolean[] checkedPrevSunTimings,checkedPrevMonTimings,checkedPrevTueTimings,
            checkedPrevWedTimings,checkedPrevThuTimings,checkedPrevFriTimings,
            checkedPrevSatTimings;

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


    String[] mydistrictlist;
    ArrayAdapter<String> adapter2,adapter3,adapter4;
    HashMap<Long, String> myCitiesList = new HashMap<Long, String>();
    HashMap<Long, String> myStatesList = new HashMap<Long, String>();
    List<String> myDistrictsList = new ArrayList<String>();
    TextView nextView;
    LinearLayout timingLayout,details_layout,emergencyContactLayout ;


    //    static int getUserId;
    static String uploadServerUrl = null,addressId,sunAppointmentsCount = "0",
            monAppointmentsCount = "0",tueAppointmentsCount = "0",wedApointmentsCount = "0",
            thuAppointmentsCount = "0",friAppointmentsCount = "0",satAppointmentsCount = "0";

    static String sunPrevAppointmentsCount = "0",
            monPrevAppointmentsCount = "0",tuePrevAppointmentsCount = "0",wedPrevApointmentsCount = "0",
            thuPrevAppointmentsCount = "0",friPrevAppointmentsCount = "0",satPrevAppointmentsCount = "0";
    int count;

    static String regMobile,userId,myAddressId,myHospitalName,myAddress,myPincode,myContactPerson,myFee,myLandlineMobileNumber,
            myComments,myCity,myState,myDistrict,myEmergencyContact,myLatitude,myLongitude;
    boolean myAvailableService;

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
        setContentView(R.layout.activity_doctor_update_address_from_maps);

        baseUrl = new ApiBaseUrl();

        hospitalName = (EditText) findViewById(R.id.Hospital_Name);
        address = (EditText) findViewById(R.id.Address);
        pincode = (EditText) findViewById(R.id.pincode);
        contactPerson = (EditText) findViewById(R.id.Frontoffice);
        fee = (EditText) findViewById(R.id.Consultation_Fee);

        emergencyContactNumber = (EditText) findViewById(R.id.emergencyContact);

        landlineMobileNumber = (EditText) findViewById(R.id.Mobile_Number);
        comments = (EditText) findViewById(R.id.Comments_Others);//promotional offer
        lat = (EditText) findViewById(R.id.Latitude);
        lng = (EditText) findViewById(R.id.Longitude);
        city = (SearchableSpinner) findViewById(R.id.cityId);
        state = (SearchableSpinner) findViewById(R.id.stateId);
        district = (SearchableSpinner) findViewById(R.id.districtId);
        availableService = (CheckBox) findViewById(R.id.serviceAvailable);

        nextView = (TextView) findViewById(R.id.next_link);

        timingLayout = (LinearLayout)findViewById(R.id.timing);
        details_layout = (LinearLayout)findViewById(R.id.details);

        myAddressId = getIntent().getStringExtra("addressId");
        userId = getIntent().getStringExtra("id");
        regMobile = getIntent().getStringExtra("regMobile");
        myHospitalName = getIntent().getStringExtra("hospitalName");
        myAddress = getIntent().getStringExtra("address");
        myCity = getIntent().getStringExtra("city");
        myPincode = getIntent().getStringExtra("pincode");
        myDistrict = getIntent().getStringExtra("district");
        myState = getIntent().getStringExtra("state");
        myContactPerson = getIntent().getStringExtra("contactName");
        myLandlineMobileNumber = getIntent().getStringExtra("mobile");
        myContactPerson = getIntent().getStringExtra("person");
        myLatitude = getIntent().getStringExtra("lat");
        myLongitude = getIntent().getStringExtra("lng");
        myFee = getIntent().getStringExtra("fee");
        myEmergencyContact = getIntent().getStringExtra("emergencyContact");
        myComments = getIntent().getStringExtra("comments");
        myAvailableService = getIntent().getBooleanExtra("emergencyService",true);

        System.out.println("hospital name"+myHospitalName);

        System.out.println("emergency service.."+myAvailableService);

        new GetPreviousTimings().execute(baseUrl.getUrl()+"GetAllTimeSlotbyAddressid?AddresID="+myAddressId);

        new GetAllCities().execute(baseUrl.getUrl()+"GetAllCity");

        new GetAllStates().execute(baseUrl.getUrl()+"GetAllState");

        new GetAllDistricts().execute(baseUrl.getUrl()+"GetAllDistrict");

        new GetDoctorDetails().execute(baseUrl.getUrl()+"GetDoctorByID"+"?id="+userId);

        new GetTimeSlots().execute(baseUrl.getUrl()+"GetAllTimeSlot");

        hospitalName.setText(myHospitalName);
        address.setText(myAddress);
        pincode.setText(myPincode);
        contactPerson.setText(myContactPerson);
        landlineMobileNumber.setText(myLandlineMobileNumber);

        lat.setText(String.format("%.6f", myLatitude));
        lng.setText(String.format("%.6f", myLongitude));

        fee.setText(myFee);

        comments.setText(myComments);
        availableService.setChecked(myAvailableService);

        emergencyContactNumber = (EditText) findViewById(R.id.emergencyContact);
        emergencyContactLayout = (LinearLayout)findViewById(R.id.emergencyContactLayout);

        if(availableService.isChecked()==true)
        {
            emergencyContactLayout.setVisibility(View.VISIBLE);
            emergencyContactNumber.setText(myEmergencyContact);
        }
        if(availableService.isChecked()==false)
        {
            emergencyContactLayout.setVisibility(View.GONE);
//            emergencyContactNumber.setText(myEmergencyContact);
        }

        nextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFullAddress();
            }
        });


        //btn_AddAddress = (MagicButton) findViewById(R.id.btn_addAddress);
        rippleView=(RippleView) findViewById(R.id.rippleView);
        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String js = formatDoctorTimingsDataAsJson();
                System.out.println("js time array"+js.toString());
                new insertDoctorAppointmentTimings().execute(baseUrl.getUrl()+"DoctorInsertTimeSlot",js.toString());
            }
        });

        sunday = (Button) findViewById(R.id.Sunday);
        monday = (Button) findViewById(R.id.Monday);
        tuesday = (Button) findViewById(R.id.Tuesday);
        wednesday =(Button)findViewById(R.id.Wednesday);
        thursday = (Button) findViewById(R.id.Thursday);
        friday = (Button) findViewById(R.id.Friday);
        saturday = (Button) findViewById(R.id.Saturday);

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("Update Address");
//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(DoctorUpdateAddressFromMaps.this,DoctorManageAddress.class);
//                        intent.putExtra("id",userId);
//                        intent.putExtra("mobile",regMobile);
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
                    Intent contact = new Intent(DoctorUpdateAddressFromMaps.this,DoctorEditProfile.class);
                    contact.putExtra("id",userId);
                    contact.putExtra("mobile",regMobile);
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                    Intent i = new Intent(DoctorUpdateAddressFromMaps.this,SubscriptionPlanAlertDialog.class);
                    i.putExtra("id",userId);
                    i.putExtra("mobile",regMobile);
                    i.putExtra("module","doc");
                    startActivity(i);

                } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here
                    Intent contact = new Intent(DoctorUpdateAddressFromMaps.this,Offers.class);
                    contact.putExtra("id",userId);
                    contact.putExtra("mobile",regMobile);
                    contact.putExtra("module","doc");
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(DoctorUpdateAddressFromMaps.this,ReachUs.class);
                    contact.putExtra("mobile",regMobile);
                    contact.putExtra("id",userId);
                    contact.putExtra("module","doc");
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here
                    Intent contact = new Intent(DoctorUpdateAddressFromMaps.this,Login.class);
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

                        Intent i = new Intent(DoctorUpdateAddressFromMaps.this,DoctorDashboard.class);
                        i.putExtra("id",userId);
                        i.putExtra("mobile",regMobile);
                        startActivity(i);


                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM1_2) {

                        // call activity here

                        Intent i = new Intent(DoctorUpdateAddressFromMaps.this,DoctorTodaysAppointmentsForPatient.class);
                        i.putExtra("id",userId);
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

                        Intent about = new Intent(DoctorUpdateAddressFromMaps.this,DoctorChangePassword.class);
                        about.putExtra("id",userId);
                        about.putExtra("mobile",regMobile);
                        startActivity(about);

                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                } else if(groupPosition == DoctorSideNavigationExpandableListAdapter.Address) {
                    if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_1) {


                        Intent about = new Intent(DoctorUpdateAddressFromMaps.this,DoctorAddAddress.class);
                        about.putExtra("id",userId);
                        about.putExtra("mobile",regMobile);
                        startActivity(about);

                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent about = new Intent(DoctorUpdateAddressFromMaps.this,DoctorManageAddress.class);
                        about.putExtra("id",userId);
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
            progressDialog = new ProgressDialog(DoctorUpdateAddressFromMaps.this);
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

            Intent intent = new Intent(DoctorUpdateAddressFromMaps.this,DoctorDashboard.class);
            intent.putExtra("id",userId);
            intent.putExtra("mobile",regMobile);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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
            System.out.println("js data..."+js.toString());

            new sendDoctorUpdateAdressDetails().execute(baseUrl.getUrl()+"UpdateDoctorAddress",js.toString());

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

                System.out.println("map values index.. "+values.get(i));

                System.out.println("items size "+getmUserItemsSunItems.size());

                System.out.println("items ele "+getmUserItemsSunItems.toString());

                if(key.equals("0"))
                {

                    String a[] = new String[getmUserItemsSunItems.size()];
                    //Loop index size()
                    for(int index = 0; index < a.length; index++) {

                        String lis = getmUserItemsSunItems.toString();
                        a = lis.split(", ");

                        String s = a[0];
                        String last = a[a.length-1];

                        if(index == 0)
                        {
                            s = s.substring(1);
                            System.out.println("a first value.."+s);
                            a[index] = s;
                        }

                        if(index == a.length-1)
                        {
                            last = last.substring(0,last.length()-1);
                            System.out.println("a last value.."+last);
                            a[index] = last;
                        }

                        List mylist = new ArrayList<>();
                        mylist.addAll(Arrays.asList(a));

                        System.out.println("sun map "+mylist.toString());

                        JSONObject eachData = new JSONObject();

                        eachData.put("TsID", getTimeKeyFromValue(AllTimeSlotsList,mylist.get(index)));
                        eachData.put("TimeSlots", mylist.get(index));
                        eachData.put("AddressID", myAddressId);
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


                        String lis = getmUserItemsMonItems.toString();
                        a = lis.split(", ");

                        String s = a[0];
                        String last = a[a.length-1];

                        if(index == 0)
                        {
                            s = s.substring(1);
                            System.out.println("a first value.."+s);
                            a[index] = s;
                        }

                        if(index == a.length-1)
                        {
                            last = last.substring(0,last.length()-1);
                            System.out.println("a last value.."+last);
                            a[index] = last;
                        }


                        List mylist = new ArrayList<>();
                        mylist.addAll(Arrays.asList(a));

                        JSONObject eachData = new JSONObject();

                        eachData.put("TsID", getTimeKeyFromValue(AllTimeSlotsList,mylist.get(index)));
                        eachData.put("TimeSlots", mylist.get(index));
                        eachData.put("AddressID", myAddressId);
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

                        String lis = getmUserItemsTueItems.toString();
                        a = lis.split(", ");

                        String s = a[0];
                        String last = a[a.length-1];

                        if(index == 0)
                        {
                            s = s.substring(1);
                            System.out.println("a first value.."+s);
                            a[index] = s;
                        }

                        if(index == a.length-1)
                        {
                            last = last.substring(0,last.length()-1);
                            System.out.println("a last value.."+last);
                            a[index] = last;
                        }

                        List mylist = new ArrayList<>();
                        mylist.addAll(Arrays.asList(a));

                        JSONObject eachData = new JSONObject();

                        eachData.put("TsID", getTimeKeyFromValue(AllTimeSlotsList,mylist.get(index)));
                        eachData.put("TimeSlots", mylist.get(index));
                        eachData.put("AddressID", myAddressId);
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

                        String lis = getmUserItemsWedItems.toString();
                        a = lis.split(", ");

                        String s = a[0];
                        String last = a[a.length-1];

                        if(index == 0)
                        {
                            s = s.substring(1);
                            System.out.println("a first value.."+s);
                            a[index] = s;
                        }

                        if(index == a.length-1)
                        {
                            last = last.substring(0,last.length()-1);
                            System.out.println("a last value.."+last);
                            a[index] = last;
                        }

                        List mylist = new ArrayList<>();
                        mylist.addAll(Arrays.asList(a));

                        JSONObject eachData = new JSONObject();

                        eachData.put("TsID", getTimeKeyFromValue(AllTimeSlotsList,mylist.get(index)));
                        eachData.put("TimeSlots", mylist.get(index));
                        eachData.put("AddressID", myAddressId);
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

                        String lis = getmUserItemsThurItems.toString();
                        a = lis.split(", ");

                        String s = a[0];
                        String last = a[a.length-1];

                        if(index == 0)
                        {
                            s = s.substring(1);
                            System.out.println("a first value.."+s);
                            a[index] = s;
                        }

                        if(index == a.length-1)
                        {
                            last = last.substring(0,last.length()-1);
                            System.out.println("a last value.."+last);
                            a[index] = last;
                        }

                        List mylist = new ArrayList<>();
                        mylist.addAll(Arrays.asList(a));

                        JSONObject eachData = new JSONObject();

                        eachData.put("TsID", getTimeKeyFromValue(AllTimeSlotsList,mylist.get(index)));
                        eachData.put("TimeSlots", mylist.get(index));
                        eachData.put("AddressID", myAddressId);
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

                        String lis = getmUserItemsFriItems.toString();
                        a = lis.split(", ");

                        String s = a[0];
                        String last = a[a.length-1];

                        if(index == 0)
                        {
                            s = s.substring(1);
                            System.out.println("a first value.."+s);
                            a[index] = s;
                        }

                        if(index == a.length-1)
                        {
                            last = last.substring(0,last.length()-1);
                            System.out.println("a last value.."+last);
                            a[index] = last;
                        }

                        List mylist = new ArrayList<>();
                        mylist.addAll(Arrays.asList(a));

                        JSONObject eachData = new JSONObject();

                        eachData.put("TsID", getTimeKeyFromValue(AllTimeSlotsList,mylist.get(index)));
                        eachData.put("TimeSlots", mylist.get(index));
                        eachData.put("AddressID", myAddressId);
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

                        String lis = getmUserItemsSatItems.toString();
                        a = lis.split(", ");

                        String s = a[0];
                        String last = a[a.length-1];

                        if(index == 0)
                        {
                            s = s.substring(1);
                            System.out.println("a first value.."+s);
                            a[index] = s;
                        }

                        if(index == a.length-1)
                        {
                            last = last.substring(0,last.length()-1);
                            System.out.println("a last value.."+last);
                            a[index] = last;
                        }

                        List mylist = new ArrayList<>();
                        mylist.addAll(Arrays.asList(a));

                        JSONObject eachData = new JSONObject();

                        eachData.put("TsID", getTimeKeyFromValue(AllTimeSlotsList,mylist.get(index)));
                        eachData.put("TimeSlots", mylist.get(index));
                        eachData.put("AddressID", myAddressId);
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
        myLandlineMobileNumber = landlineMobileNumber.getText().toString().trim();
        myComments = comments.getText().toString().trim();
        myLatitude = lat.getText().toString().trim();
        myLongitude = lng.getText().toString().trim();
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

                data.put("AddressID",myAddressId);
                data.put("Address1",myAddress);
                data.put("HospitalName",myHospitalName);

                data.put("StateID",getStateKeyFromValue(myStatesList,myState));
                data.put("CityID",getCityKeyFromValue(myCitiesList,myCity));

                data.put("ZipCode",myPincode);
                data.put("LandlineNo",myLandlineMobileNumber);
                data.put("EmergencyContact",myEmergencyContact);
                data.put("District",myDistrict);
                data.put("FrontofficeContactPerson",myContactPerson);

                data.put("ConsultationFee",myFee);
                data.put("EmergencyService", myAvailableService);
                data.put("Latitude",myLatitude);
                data.put("Longitude", myLongitude);
                data.put("PromotionalOffer", myComments);///

                return data.toString();
            }
            else if(!availableService.isChecked())
            {
                data.put("AddressID",myAddressId);
                data.put("Address1",myAddress);
                data.put("HospitalName",myHospitalName);

                data.put("StateID",getStateKeyFromValue(myStatesList,myState));
                data.put("CityID",getCityKeyFromValue(myCitiesList,myCity));

                data.put("ZipCode",myPincode);
                data.put("LandlineNo",myLandlineMobileNumber);

//                data.put("EmergencyContact",myEmergencyContact);

                data.put("District",myDistrict);
                data.put("FrontofficeContactPerson",myContactPerson);

                data.put("ConsultationFee",myFee);
                data.put("EmergencyService", myAvailableService);
                data.put("Latitude",myLatitude);
                data.put("Longitude", myLongitude);
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

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorUpdateAddressFromMaps.this);
        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorUpdateAddressFromMaps.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.doctor_insert_timings);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);
        appointments.setText(sunPrevAppointmentsCount);

        ok_btn = (Button)MyDialog.findViewById(R.id.ok);
        cancel_btn = (Button)MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
    }

    public void showSunalert(String txt)
    {
        getmUserItemsSunItems = new ArrayList<>();

        TextView inputtext;
        final int value=Integer.parseInt(txt);

        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorUpdateAddressFromMaps.this);

        if(sunPrevAppointmentsCount.equals("0"))
        {
            String[] stockArr = new String[0];

            allSunPrevItems = new String[0];

        }

        else
        {
            for (int i = 0; i < allSunPrevItems.length; i++) {
                if (AllTimeSlotsList.containsValue(prevSunTimeSlotsList.get(i))) {
//                Toast.makeText(getApplicationContext(),prevSunTimeSlotsList.get(i),Toast.LENGTH_SHORT).show();

                    int pos = Arrays.asList(allItems).indexOf(prevSunTimeSlotsList.get(i).toString());
                    System.out.println("pos.." + pos);
                    checkedSunAmTimings[pos] = true;
                    getmUserItemsSunItems.add(prevSunTimeSlotsList.get(i).toString());
                }
            }
        }

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

        mBuilder2.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";

                for (int i = 0; i <  getmUserItems_SunAmValue.size(); i++) {
                    item = item + allItems[Integer.parseInt(getmUserItems_SunAmValue.get(i))];
                    if (i != getmUserItems_SunAmValue.size() - 1) {
                        item = item + ", ";
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

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorUpdateAddressFromMaps.this);
        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorUpdateAddressFromMaps.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.doctor_insert_timings);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);
        appointments.setText(monPrevAppointmentsCount);

        ok_btn = (Button)MyDialog.findViewById(R.id.ok);
        cancel_btn = (Button)MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        TextView inputtext;
        final int value=Integer.parseInt(txt);

        System.out.println("mon ap count"+value);

        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorUpdateAddressFromMaps.this);

        if(monPrevAppointmentsCount.equals("0"))
        {
            String[] stockArr = new String[0];

            allMonPrevItems = new String[0];

//            checkedPrevMonTimings = new boolean[allMonPrevItems.length];
        }

        else
        {
            for(int i=0;i<allMonPrevItems.length;i++)
            {
                if(AllTimeSlotsList.containsValue(prevMonTimeSlotsList.get(i)))
                {
//                Toast.makeText(getApplicationContext(),prevMonTimeSlotsList.get(i),Toast.LENGTH_SHORT).show();

                    int pos = Arrays.asList(allItems).indexOf(prevMonTimeSlotsList.get(i).toString());
                    System.out.println("pos.."+pos);
                    checkedMonAmTimings[pos] = true;
                    getmUserItemsMonItems.add(prevMonTimeSlotsList.get(i).toString());
                }
            }
        }


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
                        item = item + ", ";
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

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorUpdateAddressFromMaps.this);
        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorUpdateAddressFromMaps.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.doctor_insert_timings);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);
        appointments.setText(tuePrevAppointmentsCount);

        ok_btn = (Button)MyDialog.findViewById(R.id.ok);
        cancel_btn = (Button)MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        System.out.println("tue ap count"+value);

        if(tuePrevAppointmentsCount.equals("0"))
        {
            String[] stockArr = new String[0];

            allTuePrevItems = new String[0];

//            checkedPrevTueTimings = new boolean[allTuePrevItems.length];
        }

        else
        {
            for (int i = 0; i < allTuePrevItems.length; i++) {
                if (AllTimeSlotsList.containsValue(prevTueTimeSlotsList.get(i))) {
//                Toast.makeText(getApplicationContext(),prevTueTimeSlotsList.get(i),Toast.LENGTH_SHORT).show();

                    int pos = Arrays.asList(allItems).indexOf(prevTueTimeSlotsList.get(i).toString());
                    System.out.println("pos.." + pos);
                    checkedTueAmTimings[pos] = true;
                    getmUserItemsTueItems.add(prevTueTimeSlotsList.get(i).toString());
                }
            }
        }

        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorUpdateAddressFromMaps.this);

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
                        item = item + ", ";
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

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorUpdateAddressFromMaps.this);
        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorUpdateAddressFromMaps.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.doctor_insert_timings);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);
        appointments.setText(wedPrevApointmentsCount);

        ok_btn = (Button)MyDialog.findViewById(R.id.ok);
        cancel_btn = (Button)MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        System.out.println("wed ap count"+value);

        if(wedPrevApointmentsCount.equals("0"))
        {
            String[] stockArr = new String[0];

            allWedPrevItems = new String[0];

//            checkedPrevTueTimings = new boolean[allTuePrevItems.length];
        }

        else
        {
            for (int i = 0; i < allWedPrevItems.length; i++) {
                if (AllTimeSlotsList.containsValue(prevWedTimeSlotsList.get(i))) {
//                Toast.makeText(getApplicationContext(),prevWedTimeSlotsList.get(i),Toast.LENGTH_SHORT).show();

                    int pos = Arrays.asList(allItems).indexOf(prevWedTimeSlotsList.get(i).toString());
                    System.out.println("pos.." + pos);
                    checkedWedAmTimings[pos] = true;
                    getmUserItemsWedItems.add(prevWedTimeSlotsList.get(i).toString());
                }
            }
        }

        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorUpdateAddressFromMaps.this);

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
                        item = item + ", ";
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

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorUpdateAddressFromMaps.this);
        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorUpdateAddressFromMaps.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.doctor_insert_timings);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);
        appointments.setText(thuPrevAppointmentsCount);

        ok_btn = (Button)MyDialog.findViewById(R.id.ok);
        cancel_btn = (Button)MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        System.out.println("thur ap count"+value);

        if(thuPrevAppointmentsCount.equals("0"))
        {
            String[] stockArr = new String[0];

            allThurPrevItems = new String[0];

//            checkedPrevTueTimings = new boolean[allTuePrevItems.length];
        }

        else
        {
            for (int i = 0; i < allThurPrevItems.length; i++) {
                if (AllTimeSlotsList.containsValue(prevThurTimeSlotsList.get(i))) {
//                Toast.makeText(getApplicationContext(),prevThurTimeSlotsList.get(i),Toast.LENGTH_SHORT).show();

                    int pos = Arrays.asList(allItems).indexOf(prevThurTimeSlotsList.get(i).toString());
                    System.out.println("pos.." + pos);
                    checkedThuAmTimings[pos] = true;
                    getmUserItemsThurItems.add(prevThurTimeSlotsList.get(i).toString());
                }
            }
        }


        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorUpdateAddressFromMaps.this);

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
                        item = item + ", ";
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

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorUpdateAddressFromMaps.this);
        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorUpdateAddressFromMaps.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.doctor_insert_timings);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);

        appointments.setText(friPrevAppointmentsCount);

        ok_btn = (Button)MyDialog.findViewById(R.id.ok);
        cancel_btn = (Button)MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        System.out.println("fri ap count"+value);

        if(friPrevAppointmentsCount.equals("0"))
        {
            String[] stockArr = new String[0];

            allFriPrevItems = new String[0];

//            checkedPrevTueTimings = new boolean[allTuePrevItems.length];
        }

        else
        {
            for (int i = 0; i < allFriPrevItems.length; i++) {
                if (AllTimeSlotsList.containsValue(prevFriTimeSlotsList.get(i))) {
//                Toast.makeText(getApplicationContext(),prevFriTimeSlotsList.get(i),Toast.LENGTH_SHORT).show();

                    int pos = Arrays.asList(allItems).indexOf(prevFriTimeSlotsList.get(i).toString());
                    System.out.println("pos.." + pos);
                    checkedFriAmTimings[pos] = true;
                    getmUserItemsFriItems.add(prevFriTimeSlotsList.get(i).toString());
                }
            }
        }


        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorUpdateAddressFromMaps.this);

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
                        item = item + ", ";
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

        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorUpdateAddressFromMaps.this);
        builder1.setTitle("how many appointments want ??");

        MyDialog  = new Dialog(DoctorUpdateAddressFromMaps.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.doctor_insert_timings);
        MyDialog.setTitle("My Custom Dialog");

        appointments = (EditText) MyDialog.findViewById(R.id.appointmentsCount);

        appointments.setText(satPrevAppointmentsCount);

        ok_btn = (Button)MyDialog.findViewById(R.id.ok);
        cancel_btn = (Button)MyDialog.findViewById(R.id.cancel);

        ok_btn.setEnabled(true);
        cancel_btn.setEnabled(true);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        System.out.println("sat ap count"+value);

        if(satPrevAppointmentsCount.equals("0"))
        {
            String[] stockArr = new String[0];

            allSatPrevItems = new String[0];

//            checkedPrevTueTimings = new boolean[allTuePrevItems.length];
        }

        else
        {
            for (int i = 0; i < allSatPrevItems.length; i++) {
                if (AllTimeSlotsList.containsValue(prevSatTimeSlotsList.get(i))) {
                    //                Toast.makeText(getApplicationContext(),prevSatTimeSlotsList.get(i),Toast.LENGTH_SHORT).show();

                    int pos = Arrays.asList(allItems).indexOf(prevSatTimeSlotsList.get(i).toString());
                    System.out.println("pos.." + pos);
                    checkedSatAmTimings[pos] = true;
                    getmUserItemsSatItems.add(prevSatTimeSlotsList.get(i).toString());
                }
            }
        }


        final AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DoctorUpdateAddressFromMaps.this);

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



        mBuilder2.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";

                for (int i = 0; i <  getmUserItems_SatAmValue.size(); i++) {
                    item = item + allItems[Integer.parseInt(getmUserItems_SatAmValue.get(i))];
                    if (i != getmUserItems_SatAmValue.size() - 1) {
                        item = item + ", ";
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

    //send doctor updated address details
    private class sendDoctorUpdateAdressDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {
                System.out.println("dsfafssss....");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setDoOutput(true);
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

//            try {
//                js= new JSONObject(result);
//                int s = js.getInt("Code");
////                if(s == 1017)
////                {
////                    addressId = js.getString("DataValue");
////                    showSuccessMessage(js.getString("Message"));
////                }
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


    private class insertDoctorAppointmentTimings extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(DoctorUpdateAddressFromMaps.this);
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
            progressDialog.dismiss();

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

        MyDialog1  = new Dialog(DoctorUpdateAddressFromMaps.this);
        MyDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog1.setContentView(R.layout.edit_success_alert);

        message = (TextView) MyDialog1.findViewById(R.id.message);
        oklink = (LinearLayout) MyDialog1.findViewById(R.id.ok);

        message.setEnabled(true);
        oklink.setEnabled(true);

        message.setText("Address Updated Successfully");

        oklink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorUpdateAddressFromMaps.this,DoctorDashboard.class);
                intent.putExtra("id",userId);
                intent.putExtra("mobile",regMobile);
                startActivity(intent);
            }
        });
        MyDialog1.show();

    }

    public void showErrorMessage(String result){

        MyDialog1  = new Dialog(DoctorUpdateAddressFromMaps.this);
        MyDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog1.setContentView(R.layout.edit_fail_alert);

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



    //Get previous timings list from api call
    public class GetPreviousTimings extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result prev timings", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getPreviousTiming(result);

        }
    }

    private void getPreviousTiming(String result) {
        try
        {
            JSONArray jsonArr = new JSONArray(result);
            prevSunTimeSlotsList = new ArrayList<>();
            prevMonTimeSlotsList = new ArrayList<>();
            prevTueTimeSlotsList = new ArrayList<>();
            prevWedTimeSlotsList = new ArrayList<>();
            prevThurTimeSlotsList = new ArrayList<>();
            prevFriTimeSlotsList = new ArrayList<>();
            prevSatTimeSlotsList = new ArrayList<>();

            for (int i = 0; i < jsonArr.length(); i++) {

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                Long dayNameId = jsonObj.getLong("DayName");

                if(dayNameId==0)
                {
                    prevSunTimeSlotsList.add(jsonObj.getString("TimeSlots"));

                    sunPrevAppointmentsCount = jsonObj.getString("NoOfAppointments");

                    String[] stockArr = new String[prevSunTimeSlotsList.size()];

                    allSunPrevItems = prevSunTimeSlotsList.toArray(stockArr);

                    checkedPrevSunTimings = new boolean[allSunPrevItems.length];

                }

                if(dayNameId==1)
                {
                    prevMonTimeSlotsList.add(jsonObj.getString("TimeSlots"));

                    monPrevAppointmentsCount = jsonObj.getString("NoOfAppointments");

                    String[] stockArr = new String[prevMonTimeSlotsList.size()];

                    allMonPrevItems = prevSunTimeSlotsList.toArray(stockArr);

                    checkedPrevMonTimings = new boolean[allMonPrevItems.length];


                }

                if(dayNameId==2)
                {
                    prevTueTimeSlotsList.add(jsonObj.getString("TimeSlots"));

                    tuePrevAppointmentsCount = jsonObj.getString("NoOfAppointments");

                    String[] stockArr = new String[prevTueTimeSlotsList.size()];

                    allTuePrevItems = prevTueTimeSlotsList.toArray(stockArr);

                    checkedPrevTueTimings = new boolean[allTuePrevItems.length];

                }

                if(dayNameId==3)
                {
                    prevWedTimeSlotsList.add(jsonObj.getString("TimeSlots"));

                    wedPrevApointmentsCount = jsonObj.getString("NoOfAppointments");

                    String[] stockArr = new String[prevWedTimeSlotsList.size()];

                    allWedPrevItems = prevWedTimeSlotsList.toArray(stockArr);

                    checkedPrevWedTimings = new boolean[allWedPrevItems.length];

                }

                if(dayNameId==4)
                {
                    prevThurTimeSlotsList.add(jsonObj.getString("TimeSlots"));

                    thuPrevAppointmentsCount = jsonObj.getString("NoOfAppointments");

                    String[] stockArr = new String[prevThurTimeSlotsList.size()];

                    allThurPrevItems = prevThurTimeSlotsList.toArray(stockArr);

                    checkedPrevThuTimings = new boolean[allThurPrevItems.length];
                }

                if(dayNameId==5)
                {
                    prevFriTimeSlotsList.add(jsonObj.getString("TimeSlots"));

                    friPrevAppointmentsCount = jsonObj.getString("NoOfAppointments");

                    String[] stockArr = new String[prevFriTimeSlotsList.size()];

                    allFriPrevItems = prevFriTimeSlotsList.toArray(stockArr);

                    checkedPrevFriTimings = new boolean[allFriPrevItems.length];

                }

                if(dayNameId==6)
                {
                    prevSatTimeSlotsList.add(jsonObj.getString("TimeSlots"));

                    satPrevAppointmentsCount = jsonObj.getString("NoOfAppointments");

                    String[] stockArr = new String[prevSatTimeSlotsList.size()];

                    allSatPrevItems = prevSatTimeSlotsList.toArray(stockArr);

                    checkedPrevSatTimings = new boolean[allSatPrevItems.length];
                }

            }

            System.out.println("sun prev.."+prevSunTimeSlotsList);
            System.out.println("mon prev.."+prevMonTimeSlotsList);
            System.out.println("tue prev.."+prevTueTimeSlotsList);
            System.out.println("wed prev.."+prevWedTimeSlotsList);
            System.out.println("thur prev.."+prevThurTimeSlotsList);
            System.out.println("fri prev.."+prevFriTimeSlotsList);
            System.out.println("sat prev.."+prevSatTimeSlotsList);
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
//                System.out.print("mycity list.."+myCitiesList);
//                System.out.print("city list.."+citiesList);
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

