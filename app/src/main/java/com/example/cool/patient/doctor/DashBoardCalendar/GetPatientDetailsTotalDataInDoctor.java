package com.example.cool.patient.doctor.DashBoardCalendar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.R;
import com.example.cool.patient.common.ChangePassword;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.doctor.AddAddress.DoctorAddAddress;
import com.example.cool.patient.doctor.DoctorEditProfile;
import com.example.cool.patient.doctor.DoctorSideNavigatioExpandableSubList;
import com.example.cool.patient.doctor.DoctorSideNavigationExpandableListAdapter;
import com.example.cool.patient.doctor.ManageAddress.DoctorManageAddress;
import com.example.cool.patient.doctor.TodaysAppointments.DoctorTodaysAppointmentsForPatient;
import com.example.cool.patient.subscriptionPlan.SubscriptionPlanAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetPatientDetailsTotalDataInDoctor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView aadharnumber,mobilenumber,timeslot,patientname;
    RippleView rippleView;
    Spinner spinner;
    ProgressDialog progressDialog;

    String myPatientname,aadhar,mobilenum,timeslt,str,url,status1;
    static String doctorMobile,doctorId,patientId,appointmentDate,AppointmentID,DoctorComment,Approved,Amount,Prescrition,Payment=null;
    int AppointmentID1;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    List<String> statusList;

    String encodedLicenceImage;
    final int REQUEST_CODE_GALLERY1 = 999;
    Uri selectedLicenceImageUri;
    Bitmap selectedLicenceImageBitmap = null;

    FloatingActionButton camaraicon,licenceicon;
    ImageView image;
    //MultiAutoCompleteTextView comment;
    CheckBox netbanking,cashonhand,swipe_card;
    EditText amount,comment;
//    Button button;

    ArrayAdapter<String> statusAdapter;

    ApiBaseUrl baseUrl;

    String smsUrl = null,uploadServerUrl;
    String Address1,cityname;

    String getDoctorID, addressID;
    String  getAddressID;

    String mydoctorname,mydoctormobile;

    // expandable list view

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    //sidenav fields
    TextView sidenavName,sidenavEmail,sidenavMobile;
    ImageView sidenavDoctorImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_patient_details_total_data_in_doctor);

        baseUrl = new ApiBaseUrl();

        camaraicon = (FloatingActionButton) findViewById(R.id.prescription_camaraIcon);
        licenceicon = (FloatingActionButton) findViewById(R.id.Licence_ImageIcon);
        image = (ImageView) findViewById(R.id.prescription);
        comment=(EditText) findViewById(R.id.Comments_Others);
//        button=(Button) findViewById(R.id.button);
//        paytm=(CheckBox)findViewById(R.id.pay_paytm);
        netbanking=(CheckBox)findViewById(R.id.net_banking);
        cashonhand=(CheckBox)findViewById(R.id.cash_on_hand);
        swipe_card=(CheckBox)findViewById(R.id.swipe_card);
        amount=(EditText)findViewById(R.id.amount);

        spinner = (Spinner) findViewById(R.id.status);

        aadharnumber=(TextView)findViewById(R.id.aadhaarNumber);
        mobilenumber=(TextView)findViewById(R.id.mobilenumber);
        timeslot=(TextView)findViewById(R.id.time_slot);
        patientname=(TextView)findViewById(R.id.Patient_name);
        rippleView = (RippleView) findViewById(R.id.rippleView);


        myPatientname = getIntent().getStringExtra("patientname");
        aadhar = getIntent().getStringExtra("aadharnumber");
        mobilenum = getIntent().getStringExtra("mobilenumber");
        timeslt = getIntent().getStringExtra("timeslot");
        status1 = getIntent().getStringExtra("status");
        AppointmentID = getIntent().getStringExtra("AppointmentID");
        doctorId = getIntent().getStringExtra("doctorId");
        doctorMobile = getIntent().getStringExtra("doctorMobile");
        addressID=getIntent().getStringExtra("doctorAddressID");
        appointmentDate = getIntent().getStringExtra("appointmentDate");
        patientId = Integer.toString(getIntent().getIntExtra("patientID",1));

        statusList = new ArrayList<>();
        statusList.add("Select Status");
        statusList.add("Accept");
        statusList.add("Reschedule");
        statusList.add("Reject");

        if(status1.equals("Pending"))
        {
            amount.setFocusable(true);
            amount.setFocusableInTouchMode(true);
            amount.setClickable(true);

            comment.setFocusable(true);
            comment.setFocusableInTouchMode(true);
            comment.setClickable(true);

            spinner.setFocusable(true);
            spinner.setFocusableInTouchMode(true);
            spinner.setClickable(true);


            camaraicon.setFocusable(true);
            camaraicon.setFocusableInTouchMode(true);
            camaraicon.setClickable(true);

            licenceicon.setFocusable(true);
            licenceicon.setFocusableInTouchMode(true);
            licenceicon.setClickable(true);

            rippleView.setFocusable(true);
            rippleView.setFocusableInTouchMode(true);
            rippleView.setClickable(true);

            netbanking.setFocusable(true);
            netbanking.setFocusableInTouchMode(true);
            netbanking.setClickable(true);

            cashonhand.setFocusable(true);
            cashonhand.setFocusableInTouchMode(true);
            cashonhand.setClickable(true);
            cashonhand.setChecked(true);

            swipe_card.setFocusable(true);
            swipe_card.setFocusableInTouchMode(true);
            swipe_card.setClickable(true);

            netbanking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(netbanking.isChecked())
                    {
                        Payment="Net Banking";
                        cashonhand.setEnabled(false);
                        swipe_card.setEnabled(false);
                    }
                    else
                    {
                        cashonhand.setEnabled(true);
                        swipe_card.setEnabled(true);
                    }

                }
            });

            cashonhand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(cashonhand.isChecked())
                    {
                        Payment="Cash On Hand";
                        swipe_card.setEnabled(false);
                        netbanking.setEnabled(false);
                    }
                    else
                    {
                        swipe_card.setEnabled(true);
                        netbanking.setEnabled(true);
                    }
                }
            });


            swipe_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(swipe_card.isChecked())
                    {
                        Payment="Credit/Debit Card";
                        netbanking.setEnabled(false);
                        cashonhand.setEnabled(false);
                    }

                    else
                    {
                        netbanking.setEnabled(true);
                        cashonhand.setEnabled(true);
                    }

                }
            });



            rippleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    System.out.println("doctor comment..."+comment.getText().toString().trim());

                    System.out.println("Amount..."+amount.getText().toString());

                    System.out.println("status btn..."+spinner.getSelectedItem().toString());

                    String json=formatDataAsJson();
                    new SendAppointmentDetailsToUpdate().execute(baseUrl.getUrl()+"DoctotUpdateAppointment",json.toString());
                }
            });

            licenceicon.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(
                                    GetPatientDetailsTotalDataInDoctor.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_CODE_GALLERY1
                            );

                        }
                    });

            camaraicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, MY_CAMERA_REQUEST_CODE);
                    }
                }
            });

            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }

            DoctorComment = comment.getText().toString().trim();
            Amount = amount.getText().toString();
        }

        else
        {
            amount.setFocusable(false);
            amount.setFocusableInTouchMode(false);
            amount.setClickable(false);

            comment.setFocusable(false);
            comment.setFocusableInTouchMode(false);
            comment.setClickable(false);

            spinner.setFocusable(false);
            spinner.setFocusableInTouchMode(false);
            spinner.setClickable(false);
            spinner.setEnabled(false);


            camaraicon.setFocusable(false);
            camaraicon.setFocusableInTouchMode(false);
            camaraicon.setClickable(false);

            licenceicon.setFocusable(false);
            licenceicon.setFocusableInTouchMode(false);
            licenceicon.setClickable(false);

            rippleView.setFocusable(false);
            rippleView.setFocusableInTouchMode(false);
            rippleView.setClickable(false);

            netbanking.setFocusable(false);
            netbanking.setFocusableInTouchMode(false);
            netbanking.setClickable(false);

            cashonhand.setFocusable(false);
            cashonhand.setFocusableInTouchMode(false);
            cashonhand.setClickable(false);

            swipe_card.setFocusable(false);
            swipe_card.setFocusableInTouchMode(false);
            swipe_card.setClickable(false);
        }

        System.out.println("mobile number"+mobilenum);

        uploadServerUrl = baseUrl.getUrl()+"DoctorGetAllAddress?ID="+doctorId;
        new GetDoctorAllAddressDetails().execute(uploadServerUrl);
        System.out.println("aadhar in patient total data..."+aadhar);

        System.out.println("Appintmentid..."+AppointmentID1);
        System.out.println("patientid totaldatadoc..."+patientId);
        System.out.println("doctorid..."+doctorId);
        //AppointmentID=Integer.toString(AppointmentID1);
        //AppointmentID1=Integer.parseInt(AppointmentID);

        patientname.setText(myPatientname);
        mobilenumber.setText(mobilenum);
        aadharnumber.setText(aadhar);
        timeslot.setText(timeslt);

        new GetDoctorDetails().execute(baseUrl.getUrl()+"GetDoctorByID"+"?id="+doctorId);

//        {"Select Status","Accept","Reschedule","Reject"};

        if(status1.equals("Pending"))
        {
            statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusList);
            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
            spinner.setAdapter(statusAdapter); // Apply the adapter to the spinner
        }
        else if(status1.equals("Accept"))
        {
            statusList.add(0,"Accept");
            statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusList);
            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
            spinner.setAdapter(statusAdapter); // Apply the adapter to the spinner
        }
        else if(status1.equals("Reschedule"))
        {
            statusList.add(0,"Reschedule");
            statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusList);
            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
            spinner.setAdapter(statusAdapter); // Apply the adapter to the spinner
        }
        else if(status1.equals("Reject"))
        {
            statusList.add(0,"Reject");
            statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusList);
            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
            spinner.setAdapter(statusAdapter); // Apply the adapter to the spinner
        }







//        paytm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Payment = "Pay with Paytm";
//
//                netbanking.setEnabled(false);
//                cashonhand.setEnabled(false);
//                creditcard.setEnabled(false);
//            }
//        });




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
//        toolbar.setTitle("Edit Profile");
//        toolbar.setNavigationOnClickListener(
////                new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
//////                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
////                        Intent intent = new Intent(GetPatientDetailsTotalDataInDoctor.this,GetPatientDetailsListInDoctor.class);
////                        intent.putExtra("doctorId",doctorId);
////                        intent.putExtra("date",appointmentDate);
////                        intent.putExtra("doctorMobile",doctorMobile);
////                        startActivity(intent);
////
////                    }
////                }
////
////        );


        //side navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_doctor_dashboard);

        sidenavName = (TextView) headerLayout.findViewById(R.id.name);
        sidenavEmail = (TextView) headerLayout.findViewById(R.id.emailId);
        sidenavMobile = (TextView) headerLayout.findViewById(R.id.mobile);
        sidenavDoctorImage = (ImageView) headerLayout.findViewById(R.id.profileImageId);

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
                    Intent contact = new Intent(GetPatientDetailsTotalDataInDoctor.this,DoctorEditProfile.class);
                    contact.putExtra("id",doctorId);
                    contact.putExtra("mobile",doctorMobile);
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                    Intent i = new Intent(GetPatientDetailsTotalDataInDoctor.this,SubscriptionPlanAlertDialog.class);
                    i.putExtra("id",doctorId);
                    i.putExtra("module","doc");
                    startActivity(i);

                } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here
                    Intent contact = new Intent(GetPatientDetailsTotalDataInDoctor.this,AboutUs.class);
                    startActivity(contact);

                } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(GetPatientDetailsTotalDataInDoctor.this,ReachUs.class);
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here
                    Intent contact = new Intent(GetPatientDetailsTotalDataInDoctor.this,Login.class);
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

                        Intent i = new Intent(GetPatientDetailsTotalDataInDoctor.this,DoctorDashboard.class);
                        i.putExtra("id",doctorId);
                        i.putExtra("mobile",doctorMobile);
                        startActivity(i);


                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM1_2) {

                        // call activity here

                        Intent i = new Intent(GetPatientDetailsTotalDataInDoctor.this,DoctorTodaysAppointmentsForPatient.class);
                        i.putExtra("id",doctorId);
                        i.putExtra("mobile",doctorMobile);
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

                        Intent about = new Intent(GetPatientDetailsTotalDataInDoctor.this,ChangePassword.class);
                        about.putExtra("mobile",doctorMobile);
                        startActivity(about);

                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                } else if(groupPosition == DoctorSideNavigationExpandableListAdapter.Address) {
                    if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_1) {


                        Intent about = new Intent(GetPatientDetailsTotalDataInDoctor.this,DoctorAddAddress.class);
                        about.putExtra("id",doctorId);
                        about.putExtra("mobile",doctorMobile);
                        startActivity(about);

                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent about = new Intent(GetPatientDetailsTotalDataInDoctor.this,DoctorManageAddress.class);
                        about.putExtra("id",doctorId);
                        about.putExtra("mobile",doctorMobile);
                        startActivity(about);

                    }

                }
                return true;

            }
        });

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

            Intent intent = new Intent(GetPatientDetailsTotalDataInDoctor.this,DoctorDashboard.class);
            intent.putExtra("id",doctorId);
            intent.putExtra("mobile",doctorMobile);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    //get doctor details based on id from api call
    private class GetDoctorDetails extends AsyncTask<String, Void, String> {

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
            getProfileDetails(result);
        }

    }

    private void getProfileDetails(String result) {
        try {
            JSONObject object = new JSONObject(result);

            mydoctorname = object.getString("FirstName");
            mydoctormobile = object.getString("MobileNumber");

            String myMobile = (String) object.get("MobileNumber");
            String myEmail = (String) object.get("EmailID");
            String myName = (String) object.get("FirstName");
            String mySurname = (String) object.get("LastName");

            String mydoctorImage = (String) object.get("DoctorImage");

            sidenavName.setText(myName+" "+mySurname);
            sidenavEmail.setText(myEmail);
            sidenavMobile.setText(myMobile);

            new GetProfileImageTask(sidenavDoctorImage).execute(baseUrl.getImageUrl()+mydoctorImage);

            System.out.println("doc name...."+mydoctorname);
            System.out.println("doc mobile...."+mydoctormobile);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }



    //Get all addresses for doctor list from api call
    private class GetDoctorAllAddressDetails extends AsyncTask<String, Void, String> {

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
            Log.e("Api response.....", result);
            try
            {
                JSONObject jsono = new JSONObject(result);
                String ss = (String) jsono.get("Message");
                if(ss.equals("No data found."))
                {
                    //showMessage();
                    Log.e("Api response if.....", result);
                }
                else
                {
                    getData(result);
                    Log.e("Api response else.....", result);
                }
            }
            catch (Exception e)
            {}
            getData(result);
//            Log.e("Api response.....", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }

    private void getData(String result) {
        try {

            JSONArray jarray = new JSONArray(result);

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);
                getDoctorID = object.getString("DoctorID");
                getAddressID = object.getString("AddressID");
                if(getAddressID.equals(addressID))
                {
                    Address1=object.getString("Address1");
                    cityname=object.getString("CityName");
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MY_CAMERA_REQUEST_CODE)
        {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(thumbnail);
        }
        else if (requestCode == REQUEST_CODE_GALLERY1) {
//            onSelectFromGalleryResult(data);
//             Make sure the request was successful
            Log.d("hello","I'm out.");
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {

                selectedLicenceImageUri = data.getData();
                BufferedWriter out=null;
                try {
                    selectedLicenceImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedLicenceImageUri);

                    //licence base64
                    final InputStream imageStream = getContentResolver().openInputStream(selectedLicenceImageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] b = baos.toByteArray();
                    encodedLicenceImage = Base64.encodeToString(b, Base64.DEFAULT);
                }
                catch (IOException e)
                {
                    System.out.println("Exception ");

                }
                image.setImageBitmap(selectedLicenceImageBitmap);
                Log.d("hello","I'm in.");

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    //send appointment details to update
    private class SendAppointmentDetailsToUpdate extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(GetPatientDetailsTotalDataInDoctor.this);
            // Set progressdialog title
//            mProgressDialog.setTitle("Image");
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

            progressDialog.dismiss();

            JSONObject js;

            try {
                js= new JSONObject(result);
                int s = js.getInt("Code");
                if(s == 1017)
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

            Log.e("TAG result    ", result); // this is expecting a response code to be sent from your server upon receiving the POST data

        }
    }

    public void showSuccessMessage(String message){

        android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
                        new MyTask().execute();
                        Intent intent = new Intent(GetPatientDetailsTotalDataInDoctor.this,DoctorDashboard.class);
                        intent.putExtra("id",doctorId);
                        startActivity(intent);
                    }
                });
        android.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Your Appointment");
        alert.show();
    }

    public void showErrorMessage(String message){

        android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Updating Your Appointment");
        alert.show();

    }


    private String formatDataAsJson()
    {
        JSONObject data = new JSONObject();

//        if(paytm.isChecked())
//        {
//            Payment = "Pay with Paytm";
//        }


        if(netbanking.isChecked())
        {
            Payment = "Online Banking";
        }

        if(cashonhand.isChecked())
        {
            Payment="Cash on Hand";
        }

        if(swipe_card.isChecked())
        {
            Payment="Debit/Credit card Swipe";
        }

        System.out.println("payment mode..."+Payment);

        if(encodedLicenceImage == null)
        {
            image.buildDrawingCache();
            BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos1);
            byte[] b1 = baos1.toByteArray();
            encodedLicenceImage = Base64.encodeToString(b1, Base64.DEFAULT);
        }

//        "Accept","Reschedule","Reject"

        int statusId =0;
        if(spinner.getSelectedItem().toString().equals("Reject"))
        {
            statusId = 1;
        }
        if(spinner.getSelectedItem().toString().equals("Accept"))
        {
            statusId = 2;
        }
        if(spinner.getSelectedItem().toString().equals("Reschedule"))
        {
            statusId = 3;
        }

        try{

            data.put("AppointmentID", AppointmentID);
            data.put("DoctorComment",comment.getText().toString().trim());
            data.put("Approved",statusId);
            data.put("Payment",Payment);
            data.put("Amount",amount.getText().toString());
            data.put("Prescription",encodedLicenceImage);

            return data.toString();

        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }

    private void showalert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(GetPatientDetailsTotalDataInDoctor.this);
        builder.setMessage("Appointment updated successfully.");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void showalert1() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(GetPatientDetailsTotalDataInDoctor.this);
        builder.setMessage("Appointment updated not successfull.");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    // Doctor after reject the patient appointment:

    private class MyTask extends AsyncTask<Void, Void,Void>
    {

        URL myURL=null;
        HttpURLConnection myURLConnection=null;
        BufferedReader reader=null;

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                String username = myPatientname;
                String doctorname = mydoctorname;
                String doctormobilenum = mydoctormobile;
                String Status=spinner.getSelectedItem().toString();
                String address=Address1+","+cityname;

                String message= null;

                if(Status.equals("Reject"))
                {
                    message="Hi "+username+", Sorry for your in convince your appointment has been "+Status+" by Dr."+doctorname+" on "+appointmentDate+" at "+address+". Please call "+doctormobilenum+" for any assistance/re-schedule. "+" Thank You."+" Click here to navigate:"+baseUrl.getLink();
                }
                if(Status.equals("Reschedule"))
                {
                    message="Hi "+username+", Sorry for your in convince your appointment has been "+Status+" by Dr."+doctorname+" on "+appointmentDate+" at "+address+". Please call "+doctormobilenum+" for any assistance/re-schedule. "+" Thank You."+" Click here to navigate:"+baseUrl.getLink();
                }
                else if(Status.equals("Accept"))
                {
                    message="Hi "+username+", Your appointment has confirmed by Dr."+doctorname+" on "+appointmentDate+" at "+address+". Please call "+doctormobilenum+" for any assistance/re-schedule."+" Thank You."+" Click here to navigate:"+baseUrl.getLink();
                }

                System.out.println("message"+message);
                smsUrl = baseUrl.getSmsUrl();
                String uname="MedicTr";
                String password="X!g@c$R2";
                String sender="MEDICC";
                String destination = mobilenum;

                String encode_message= URLEncoder.encode(message, "UTF-8");
                StringBuilder stringBuilder=new StringBuilder(smsUrl);
                stringBuilder.append("uname="+URLEncoder.encode(uname, "UTF-8"));
                stringBuilder.append("&pass="+URLEncoder.encode(password, "UTF-8"));
                stringBuilder.append("&send="+URLEncoder.encode(sender, "UTF-8"));
                stringBuilder.append("&dest="+URLEncoder.encode(destination, "UTF-8"));

                stringBuilder.append("&msg="+encode_message);

                smsUrl=stringBuilder.toString();
                System.out.println("smsUrl.. "+smsUrl);
                myURL=new URL(smsUrl);

                myURLConnection=(HttpURLConnection) myURL.openConnection();
                myURLConnection.connect();
                reader=new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                String response;
                while ((response = reader.readLine()) != null) {
                    Log.d("RESPONSE", "" + response);
                }
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            return null;
        }

        @Override
        protected void onPreExecute() {
//            Toast.makeText(getApplicationContext(), "the message", Toast.LENGTH_LONG).show();
            super.onPreExecute();
        }
    }

}
