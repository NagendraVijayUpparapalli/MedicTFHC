package com.example.cool.patient.diagnostic.AddAddress;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
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
import com.example.cool.patient.common.Offers;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.diagnostic.DashBoardCalendar.DiagnosticDashboard;
import com.example.cool.patient.R;
import com.example.cool.patient.diagnostic.DiagnosticChangePassword;
import com.example.cool.patient.diagnostic.DiagnosticEditProfile;
import com.example.cool.patient.diagnostic.DiagnosticSideNavigationExpandableListAdapter;
import com.example.cool.patient.diagnostic.DiagnosticSideNavigationExpandableSubList;
import com.example.cool.patient.diagnostic.ManageAddress.DiagnosticManageAddress;
import com.example.cool.patient.diagnostic.TodaysAppointments.DiagnosticsTodaysAppointments;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.bloder.magic.view.MagicButton;

public class DiagnosticAddAddressFromMaps extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    EditText diagnosticName,address,pincode,contactPerson,mobile,landlineMobileNumber,comments,lat,lng,emergencyContactNumber;
    SearchableSpinner city,state,district;
    CheckBox availableService;
    ImageView centerImage;
    MagicButton btn_AddAddress;
    RippleView rippleView;
    LinearLayout emergencyContactLayout;

    ProgressDialog progressDialog;


    static String uploadServerUrl = null,addressId ;

    static String getUserId,regMobile;
    TextView speciality;

    String[] ListItems;

    boolean[] checkedItems;
    List<String> seletedSpecialityItems = null;
    List<String> getmUserItems_Value = new ArrayList<String>();
    Map<String, List<String>> map = new HashMap<String, List<String>>();

    //get specialities fields
    HashMap<Long, String> mySpecialityList = new HashMap<Long, String>();
    List<String> specialitiesList;

    List<String> districtsList,citiesList,statesList;
    String myQrArrayList;
    String[] mydistrictlist;
    ArrayAdapter<String> adapter2,adapter3,adapter4;
    HashMap<Long, String> myCitiesList = new HashMap<Long, String>();
    HashMap<Long, String> myStatesList = new HashMap<Long, String>();
    HashMap<Long, String> myAmTimeSlotsList = new HashMap<Long, String>();
    HashMap<Long, String> myPmTimeSlotsList = new HashMap<Long, String>();
    List<String> myDistrictsList = new ArrayList<String>();

    // base64 image variables
    FloatingActionButton addCenterIcon,addCenterCameraIcon;
    final int REQUEST_CODE_GALLERY1 = 999,REQUEST_CODE_GALLERY2 = 44;
    Uri selectedCenterImageUri;
    Bitmap selectedCenterImageBitmap = null;
    String encodedCenterImage;

    ApiBaseUrl baseUrl;

    //timings variables
    EditText fromTime,toTime;
    TimePickerDialog timePickerDialog;
    TimePickerDialog timePickerDialog1;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;

    //get lat,lng on touch map

    static String myDiagnosticName,myAddress,myPincode,myContactPerson,myMobile,myLandlineMobileNumber,myComments,
            myemergencyContactNumber,myLati,myLngi,mySpeciality,myCity,myState,myDistrict,myFromTime,myToTime;
    boolean myAvailableService;

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
    TextView message1;
    LinearLayout oklink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic_add_address_from_maps);

        baseUrl = new ApiBaseUrl();
        citiesList = new ArrayList<>();
        statesList = new ArrayList<>();
        districtsList = new ArrayList<String>();

        new GetAllCities().execute(baseUrl.getUrl()+"GetAllCity");

        new GetAllStates().execute(baseUrl.getUrl()+"GetAllState");

        new GetAllDistricts().execute(baseUrl.getUrl()+"GetAllDistrict");


        new GetAllDiagSpecialities().execute(baseUrl.getUrl()+"GetDiagSpeciality");

        diagnosticName = (EditText) findViewById(R.id.Diagnostic_Name);
        address = (EditText) findViewById(R.id.Address);
        city = (SearchableSpinner) findViewById(R.id.cityId);
        state = (SearchableSpinner) findViewById(R.id.stateId);
        district = (SearchableSpinner) findViewById(R.id.districtId);
        mobile = (EditText) findViewById(R.id.Mobile_Number);
        pincode = (EditText) findViewById(R.id.pincode);
        contactPerson = (EditText) findViewById(R.id.Frontoffice);
        landlineMobileNumber = (EditText) findViewById(R.id.landMobileNumber);
        comments = (EditText) findViewById(R.id.Comments_Others);
        getLatLong = findViewById(R.id.getlatlng);
        lat = (EditText) findViewById(R.id.Latitude);
        lng = (EditText) findViewById(R.id.Longitude);
        availableService = (CheckBox) findViewById(R.id.serviceAvailable);
        speciality = (TextView) findViewById(R.id.Select_Speciality);
        fromTime = findViewById(R.id.From);
        toTime = findViewById(R.id.To_Timing);

        centerImage = (ImageView) findViewById(R.id.diag_center_image);
        addCenterIcon = (FloatingActionButton) findViewById(R.id.addDiagCenterIcon);
//        btn_AddAddress = (MagicButton)findViewById(R.id.gen_btn);
        rippleView=(RippleView)findViewById(R.id.rippleView);

        regMobile = getIntent().getStringExtra("regMobile");
        getUserId = getIntent().getStringExtra("id");
        myLatitude = getIntent().getStringExtra("lat");
        myLongitude = getIntent().getStringExtra("lng");

        myDiagnosticName = getIntent().getStringExtra("diagName");
        myAddress = getIntent().getStringExtra("address");
        myCity = getIntent().getStringExtra("city");
        myState = getIntent().getStringExtra("state");
        myDistrict = getIntent().getStringExtra("district");
        myMobile = getIntent().getStringExtra("mobile");
        myPincode = getIntent().getStringExtra("pincode");
        myContactPerson = getIntent().getStringExtra("person");
        myLandlineMobileNumber = getIntent().getStringExtra("landmobile");
        myComments = getIntent().getStringExtra("comments");

        System.out.print("diagid in add address comments....."+myComments);

        System.out.print("diagid in add address....."+getUserId);

        new GetDiagnosticDetails().execute(baseUrl.getUrl()+"DiagnosticByID"+"?id="+getUserId);

        diagnosticName.setText(myDiagnosticName);
        address.setText(myAddress);

        mobile.setText(myMobile);
        pincode.setText(myPincode);
        contactPerson.setText(myContactPerson);
        landlineMobileNumber.setText(myLandlineMobileNumber);
        comments.setText(myComments);

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


        getLatLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                validateAddAddress();

            }
        });


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
                                DiagnosticAddAddressFromMaps.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_GALLERY1
                        );

                    }
                });

        addCenterCameraIcon = (FloatingActionButton) findViewById(R.id.addDiagCenterCameraIcon);

        addCenterCameraIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, REQUEST_CODE_GALLERY2);
                        }
                    }
                });

        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showalert();
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(DiagnosticAddAddressFromMaps.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        fromTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();



            }
        });


        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(DiagnosticAddAddressFromMaps.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        toTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });




        speciality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                seletedSpecialityItems = new ArrayList<>();

//                Toast.makeText(DiagnosticAddAddressFromMaps.this,
//                        "Speciality", Toast.LENGTH_LONG).show();
                AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(DiagnosticAddAddressFromMaps.this);
                mBuilder2.setTitle("Your Specialities");
                mBuilder2.setMultiChoiceItems(ListItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                        if(isChecked){
                            String i = Integer.toString(position);
                            getmUserItems_Value.add(i);
                        }else{
                            getmUserItems_Value.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder2.setCancelable(false);
                mBuilder2.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";

                        for (int i = 0; i <  getmUserItems_Value.size(); i++) {
                            item = item + ListItems[Integer.parseInt(getmUserItems_Value.get(i))];
                            if (i != getmUserItems_Value.size() - 1) {
                                item = item + ",";

                            }
                        }


                        seletedSpecialityItems.add(item);
                        map.put("0",seletedSpecialityItems);


                        // mItemSelectedFriday.setText("Friday=>"+item);




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
                        for (int i = 0; i <   checkedItems.length; i++) {
                            checkedItems[i] = false;
                            getmUserItems_Value.clear();
                            //   mItemSelectedFriday.setText("");
                        }

                    }

                });

                AlertDialog mDialog1 = mBuilder2.create();
                mDialog1.show();
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
//                        Intent intent = new Intent(DiagnosticAddAddressFromMaps.this,DiagnosticDashboard.class);
//                        intent.putExtra("id",getUserId);
//                        intent.putExtra("mobile",regMobile);
//                        startActivity(intent);
//
//                    }
//                }
//
//        );

        // side navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_diagnostic_dashboard);

        sidenavName = (TextView) navigationView.findViewById(R.id.name1);
        sidenavEmail = (TextView) navigationView.findViewById(R.id.email1);
        sidenavMobile = (TextView) navigationView.findViewById(R.id.mobile1);


        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView1);
        expandableListDetail = DiagnosticSideNavigationExpandableSubList.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new DiagnosticSideNavigationExpandableListAdapter(this, expandableListTitle, expandableListDetail);
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

                if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.Services) {
                    retVal = false;
                } else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.Address) {
                    retVal = false;
                } else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM3) {
                    retVal = false;

                }

                else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM4) {
                    // call some activity here
                    Intent contact = new Intent(DiagnosticAddAddressFromMaps.this,DiagnosticEditProfile.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",regMobile);
                    contact.putExtra("user","old");
                    startActivity(contact);

                }

                else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                   Intent subscript = new Intent(DiagnosticAddAddressFromMaps.this,SubscriptionPlanAlertDialog.class);
                    subscript.putExtra("id",getUserId);
                    subscript.putExtra("mobile",regMobile);
                    subscript.putExtra("module","diag");
                    startActivity(subscript);

                } else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here
                    Intent contact = new Intent(DiagnosticAddAddressFromMaps.this,Offers.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",regMobile);
                    contact.putExtra("module","diag");
                    startActivity(contact);

                } else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(DiagnosticAddAddressFromMaps.this,ReachUs.class);
                    contact.putExtra("mobile",regMobile);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("module","diag");
                    startActivity(contact);

                }

                else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here
                    Intent contact = new Intent(DiagnosticAddAddressFromMaps.this,Login.class);
                    startActivity(contact);

                }

                return retVal;
            }
        });


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.Services) {
                    if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM1_1) {

                        Intent i = new Intent(DiagnosticAddAddressFromMaps.this,DiagnosticDashboard.class);
                        i.putExtra("id",getUserId);
                        i.putExtra("mobile",regMobile);
                        startActivity(i);

                    }
                    else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM1_2) {

                        // call activity here

                        Intent i = new Intent(DiagnosticAddAddressFromMaps.this,DiagnosticsTodaysAppointments.class);
                        i.putExtra("userId",getUserId);
                        i.putExtra("mobile",regMobile);
                        startActivity(i);

                    }
//                    else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM1_3) {
//
//                        // call activity here
//
//                    }


                } else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM3) {

                    if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM3_1) {

                        // call activity here

                        Intent about = new Intent(DiagnosticAddAddressFromMaps.this,DiagnosticChangePassword.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",regMobile);
                        startActivity(about);

                    }
                    else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                } else if(groupPosition == DiagnosticSideNavigationExpandableListAdapter.Address) {
                    if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM2_1) {


                        Intent about = new Intent(DiagnosticAddAddressFromMaps.this,DiagnosticAddAddress.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",regMobile);
                        startActivity(about);

                    }
                    else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent about = new Intent(DiagnosticAddAddressFromMaps.this,DiagnosticManageAddress.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",regMobile);
                        startActivity(about);

                    }

                }
                return true;

            }
        });

    }


    //    new GetDiagnosticDetails().execute(baseUrl.getUrl()+"DiagnosticByID"+"?id="+getUserId);

    //get diagnostic details based on id from api call
    private class GetDiagnosticDetails extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result diagprofile", result); // this is expecting a response code to be sent from your server upon receiving the POST data
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
            sidenavMobile.setText(myMobile);
            sidenavEmail.setText(myEmail);


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

            Intent intent = new Intent(DiagnosticAddAddressFromMaps.this,DiagnosticDashboard.class);
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

            fromTime.setText("00:00 AM");
            fromTime.setEnabled(false);

            toTime.setText("00:00 PM");
            toTime.setEnabled(false);

        }
        else if(availableService.isChecked()==false)
        {
            emergencyContactLayout.setVisibility(View.GONE);
            fromTime.setEnabled(true);
            toTime.setEnabled(true);
        }
    }


    public boolean validate()
    {
        boolean validate = true;
        if(diagnosticName.getText().toString().trim().isEmpty())
        {
            diagnosticName.setError("please enter the name");
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
        else if(landlineMobileNumber.getText().toString().trim().length()<10 || landlineMobileNumber.getText().toString().trim().length()>11)
        {
            landlineMobileNumber.setError(" Invalid number ");
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

    public void validateFullAddress()
    {
        if(!addressValidate())
        {
//            Toast.makeText(this,"Succesfully field" , Toast.LENGTH_SHORT).show();
        }
        else
        {
            String js = formatDataAsJson();
            System.out.println("js diag address.."+js);
            new sendAddAddressDetails().execute(baseUrl.getUrl()+"DiagnosticAddAddress",js.toString());
//            Toast.makeText(this,"Succesfully field" , Toast.LENGTH_SHORT).show();
        }
    }

    public boolean addressValidate()
    {
        boolean validate = true;
        if(diagnosticName.getText().toString().trim().isEmpty())
        {
            diagnosticName.setError("please enter the name");
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

        if(landlineMobileNumber.getText().toString().trim().isEmpty() || !Patterns.PHONE.matcher(landlineMobileNumber.getText().toString().trim()).matches())
        {
            landlineMobileNumber.setError("please enter the mobile number");
            validate=false;
        }
        else if(landlineMobileNumber.getText().toString().trim().length()<10 || landlineMobileNumber.getText().toString().trim().length()>11)
        {
            landlineMobileNumber.setError(" Invalid number ");
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

    //image permissions
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
        else if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_GALLERY2);
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        try
        {

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

            else if(requestCode == REQUEST_CODE_GALLERY2)
            {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                centerImage.setImageBitmap(thumbnail);

                centerImage.buildDrawingCache();
                BitmapDrawable bitmapDrawable = (BitmapDrawable) centerImage.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos1);
                byte[] b1 = baos1.toByteArray();
                encodedCenterImage = Base64.encodeToString(b1, Base64.DEFAULT);

            }

            else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        catch (NullPointerException ex)
        {
            ex.printStackTrace();
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




    private String formatDataAsJson()
    {

        JSONObject data = new JSONObject();

//        System.out.println("emergency contact..."+Emergency_mobile);

        myDiagnosticName = diagnosticName.getText().toString().trim();
        myAddress = address.getText().toString().trim();
        myPincode = pincode.getText().toString().trim();
        myContactPerson = contactPerson.getText().toString();
        myMobile = mobile.getText().toString();
        myLandlineMobileNumber = landlineMobileNumber.getText().toString().trim();
        myemergencyContactNumber = emergencyContactNumber.getText().toString().trim();
        myComments = comments.getText().toString().trim();
        myLati = lat.getText().toString().trim();
        myLngi = lng.getText().toString().trim();
        myCity= city.getSelectedItem().toString();
        myState= state.getSelectedItem().toString();
//        mySpeciality = speciality.///////////
        myDistrict= district.getSelectedItem().toString();
        myFromTime = fromTime.getText().toString();
        myToTime = toTime.getText().toString();

        if(availableService.isChecked()){
            myAvailableService = true;
        }
        else if(!availableService.isChecked())
        {
            myAvailableService = false;
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
        }

        try{

            org.json.simple.JSONArray allDataArray = new org.json.simple.JSONArray();


            for (Map.Entry<String, List<String>> entry : map.entrySet()) {

                String key = entry.getKey();
                List<String> values = entry.getValue();

                String a[] = new String[seletedSpecialityItems.size()];

                System.out.println("spec seleted items.."+seletedSpecialityItems);

                System.out.println("spec seleted items sizezzz.."+seletedSpecialityItems.size());

                int i = 0;

                //Loop index size()
                for(int index = 0; index < a.length; index++) {

                    String lis = values.get(i);
                    a = lis.split(",");
                    List mylist = new ArrayList<>();
                    mylist.addAll(Arrays.asList(a));

                    JSONObject eachData = new JSONObject();
                    eachData.put("SpecialityID", getSpecialityKeyFromValue(mySpecialityList,mylist.get(index)));
                    allDataArray.add(eachData);

                }

            }

            System.out.println("js diag Array.."+allDataArray.toJSONString());




            if(availableService.isChecked()==true){
                myAvailableService = true;

                data.put("DiagnosticsID",getUserId);
                data.put("CenterName",myDiagnosticName);
                data.put("Address1",myAddress);
                data.put("StateID",getStateKeyFromValue(myStatesList,myState));
                data.put("CityID",getCityKeyFromValue(myCitiesList,myCity));

                data.put("PinCode",myPincode);
                data.put("LandlineNo",myLandlineMobileNumber);
                data.put("ContactPerson",myContactPerson);
                data.put("MobileNumber",myMobile);
                data.put("EmergencyContact", emergencyContactNumber.getText().toString());
                data.put("Comment", myComments);
                data.put("EmergencyService", myAvailableService);
                data.put("Latitude",myLati);
                data.put("Longitude", myLngi);
                data.put("FromTime", myFromTime);
                data.put("ToTime", myToTime);
                data.put("CenterImage", encodedCenterImage);
                data.put("District",myDistrict);
                data.accumulate("SpecialityLst",new JSONArray(allDataArray.toJSONString()));

                System.out.println("js obj..."+data);

                return data.toString();
            }
            else if(availableService.isChecked()==false)
            {
                myAvailableService = false;
                data.put("DiagnosticsID",getUserId);
                data.put("CenterName",myDiagnosticName);
                data.put("Address1",myAddress);
                data.put("StateID",getStateKeyFromValue(myStatesList,myState));
                data.put("CityID",getCityKeyFromValue(myCitiesList,myCity));

                data.put("PinCode",myPincode);
                data.put("LandlineNo",myLandlineMobileNumber);
                data.put("ContactPerson",myContactPerson);
                data.put("MobileNumber",myMobile);
                data.put("EmergencyContact", "");
                data.put("Comment", myComments);
                data.put("EmergencyService", false);
                data.put("Latitude",myLati);
                data.put("Longitude", myLngi);
                data.put("FromTime", myFromTime);
                data.put("ToTime", myToTime);
                data.put("CenterImage", encodedCenterImage);
                data.put("District",myDistrict);
                data.accumulate("SpecialityLst",new JSONArray(allDataArray.toJSONString()));

                System.out.println("js obj..."+data);

                return data.toString();
            }


//            System.out.println("js obj..."+data);
//
//            data.put("DiagnosticsID",getUserId);
//            data.put("CenterName",myDiagnosticName);
//            data.put("Address1",myAddress);
//            data.put("StateID",getStateKeyFromValue(myStatesList,myState));
//            data.put("CityID",getCityKeyFromValue(myCitiesList,myCity));
//
//            data.put("PinCode",myPincode);
//            data.put("LandlineNo",myLandlineMobileNumber);
//            data.put("ContactPerson",myContactPerson);
//            data.put("MobileNumber",myMobile);
//            data.put("EmergencyContact", myMobile);
//            data.put("Comment", myComments);
//            data.put("EmergencyService", true);
//            data.put("Latitude",myLati);
//            data.put("Longitude", myLngi);
//            data.put("FromTime", myFromTime);
//            data.put("ToTime", myToTime);
//            data.put("CenterImage", encodedCenterImage);
//            data.put("District",myDistrict);
//            data.accumulate("SpecialityLst",new JSONArray(allDataArray.toJSONString()));
//
//            System.out.println("js obj..."+data);

//            for(int j =0;j<allDataArray.size();j++)
//            {
//                JSONObject js = (JSONObject) allDataArray.get(j);
//                String a = js.toString();
//                a.replaceAll("\\\\","");
//                System.out.println("js replace.."+a);
//            }

//            String s = allDataArray.toString();
//
//            s.replaceAll("\\\\","");
//            System.out.println("js string.."+s);

            return data.toString();

        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }

    public static Object getSpecialityKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    //send diagnostic add address details
    private class sendAddAddressDetails extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
//            progressDialog = new ProgressDialog(DiagnosticAddAddressFromMaps.this);
//            // Set progressdialog title
////            progressDialog.setTitle("You are logging");
//            // Set progressdialog message
//            progressDialog.setMessage("Loading");
//
//            progressDialog.setIndeterminate(false);
//            // Show progressdialog
//            progressDialog.show();

            progressDialog = new ProgressDialog(DiagnosticAddAddressFromMaps.this);
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
            progressDialog.dismiss();
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

    public void showSuccessMessage(String message){

        MyDialog  = new Dialog(DiagnosticAddAddressFromMaps.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.success_alert);

        message1 = (TextView) MyDialog.findViewById(R.id.message);
        oklink = (LinearLayout) MyDialog.findViewById(R.id.ok);

        message1.setEnabled(true);
        oklink.setEnabled(true);

        message1.setText(message);

        oklink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DiagnosticAddAddressFromMaps.this,DiagnosticDashboard.class);
                intent.putExtra("id",getUserId);
                intent.putExtra("mobile",regMobile);
                startActivity(intent);
            }
        });
        MyDialog.show();
//
//        AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
//
//        a_builder.setMessage(message)
//                .setCancelable(false)
//                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        dialog.cancel();
//                        Intent intent = new Intent(DiagnosticAddAddressFromMaps.this,DiagnosticDashboard.class);
//                        intent.putExtra("id",getUserId);
//                        intent.putExtra("mobile",regMobile);
//                        startActivity(intent);
//                    }
//                });
//        AlertDialog alert = a_builder.create();
//        alert.setTitle("Add Address");
//        alert.show();

    }

    public void showErrorMessage(String message){

        MyDialog  = new Dialog(DiagnosticAddAddressFromMaps.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.cancel_alertdialog);

        message1 = (TextView) MyDialog.findViewById(R.id.message);
        oklink = (LinearLayout) MyDialog.findViewById(R.id.ok);

        message1.setEnabled(true);
        oklink.setEnabled(true);

        message1.setText(message);

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
//        alert.setTitle("Add Address");
//        alert.show();

    }

    public void showalert() {

        timePickerDialog1 = new TimePickerDialog(DiagnosticAddAddressFromMaps.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                if (hourOfDay >= 12) {
                    amPm = "PM";
                } else {
                    amPm = "AM";
                }
                toTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
            }
        }, currentHour, currentMinute, false);

        timePickerDialog1.show();
    }

    //Get DiagSpecialities list from api call
    private class GetAllDiagSpecialities extends AsyncTask<String, Void, String> {

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
            }

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
