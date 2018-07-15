package com.example.cool.patient.doctor.TodaysAppointments;

import android.app.ProgressDialog;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.common.ChangePassword;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.common.Offers;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.doctor.AddAddress.DoctorAddAddress;
import com.example.cool.patient.doctor.AddAddress.DoctorAddAddressFromMaps;
import com.example.cool.patient.doctor.DashBoardCalendar.DoctorDashboard;
import com.example.cool.patient.R;
import com.example.cool.patient.doctor.DoctorChangePassword;
import com.example.cool.patient.doctor.DoctorEditProfile;
import com.example.cool.patient.doctor.DoctorSideNavigatioExpandableSubList;
import com.example.cool.patient.doctor.DoctorSideNavigationExpandableListAdapter;
import com.example.cool.patient.doctor.ManageAddress.DoctorManageAddress;
import com.example.cool.patient.subscriptionPlan.SubscriptionPlanAlertDialog;

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
import java.util.HashMap;
import java.util.List;

public class DoctorTodaysAppointmentsForPatient  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    String PatientID,AppointmentID,Status1,PatientName,EmailID,MobileNo,ReasonAppointments,Comments,TimeSlots,AadharNumber,Prescription,age;
    int Dstatus;

    String docId,docMobile;
    LinearLayoutManager layoutManager;
    private DoctorTodaysAppointmentAdapter adapter;
    private List<PatientData_DoctorTodaysAppointmentsClass> data_list;

    ProgressDialog progressDialog;

    ApiBaseUrl baseUrl;

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
        setContentView(R.layout.activity_doctor_todays_appointments_for_patient);

        baseUrl = new ApiBaseUrl();
        docId = getIntent().getStringExtra("id");
        docMobile = getIntent().getStringExtra("mobile");

        data_list=new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.recycler_view);

        System.out.println("doc mobile..."+docMobile+"...doc id..."+docId);

        new GetDoctorDetails().execute(baseUrl.getUrl()+"GetDoctorByID"+"?id="+docId);

        new GetPatientDetails().execute(baseUrl.getUrl()+"GetDoctorTodayAppointments" + "?ID=" + docId);

        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DoctorTodaysAppointmentAdapter(this,data_list);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("Todays Appointments");
//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(DoctorTodaysAppointmentsForPatient.this,DoctorDashboard.class);
//                        intent.putExtra("mobile",docMobile);
//                        intent.putExtra("id",docId);
//                        startActivity(intent);
//                    }
//                }
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
                    Intent contact = new Intent(DoctorTodaysAppointmentsForPatient.this,DoctorEditProfile.class);
                    contact.putExtra("id",docId);
                    contact.putExtra("mobile",docMobile);
                    contact.putExtra("user","old");
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
//                    Intent i = new Intent(DoctorTodaysAppointmentsForPatient.this,SubscriptionPlanAlertDialog.class);
//                    i.putExtra("id",docId);
//                    i.putExtra("mobile",docMobile);
//                    i.putExtra("module","doc");
//                    startActivity(i);

                } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here
                    Intent contact = new Intent(DoctorTodaysAppointmentsForPatient.this,Offers.class);
                    contact.putExtra("id",docId);
                    contact.putExtra("mobile",docMobile);
                    contact.putExtra("module","doc");
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(DoctorTodaysAppointmentsForPatient.this,ReachUs.class);
                    contact.putExtra("id",docId);
                    contact.putExtra("module","doc");
                    contact.putExtra("mobile",docMobile);
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here
                    Intent contact = new Intent(DoctorTodaysAppointmentsForPatient.this,Login.class);
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

                        Intent i = new Intent(DoctorTodaysAppointmentsForPatient.this,DoctorDashboard.class);
                        i.putExtra("id",docId);
                        i.putExtra("mobile",docMobile);
                        startActivity(i);


                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM1_2) {

                        // call activity here

                        Intent i = new Intent(DoctorTodaysAppointmentsForPatient.this,DoctorTodaysAppointmentsForPatient.class);
                        i.putExtra("id",docId);
                        i.putExtra("mobile",docMobile);
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

                        Intent about = new Intent(DoctorTodaysAppointmentsForPatient.this,DoctorChangePassword.class);
                        about.putExtra("id",docId);
                        about.putExtra("mobile",docMobile);
                        startActivity(about);

                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                } else if(groupPosition == DoctorSideNavigationExpandableListAdapter.Address) {
                    if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_1) {


                        Intent about = new Intent(DoctorTodaysAppointmentsForPatient.this,DoctorAddAddress.class);
                        about.putExtra("id",docId);
                        about.putExtra("mobile",docMobile);
                        startActivity(about);

                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent about = new Intent(DoctorTodaysAppointmentsForPatient.this,DoctorManageAddress.class);
                        about.putExtra("id",docId);
                        about.putExtra("mobile",docMobile);
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

            Intent intent = new Intent(DoctorTodaysAppointmentsForPatient.this,DoctorDashboard.class);
            intent.putExtra("id",docId);
            intent.putExtra("mobile",docMobile);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    //Get patient details  based on doctor id and appointment date
    private class GetPatientDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(DoctorTodaysAppointmentsForPatient.this);
            // Set progressdialog title
//            progressDialog.setTitle("You are logging");
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

            Log.e("TAG result    ", result); // this is expecting a response code to be sent from your server upon receiving the POST data

            getDetails(result);
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();

        }
    }

    private void getDetails(String result) {
        try {

            JSONArray jsonArray=new JSONArray(result);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject js = jsonArray.getJSONObject(i);

                Dstatus = js.getInt("DStatus");
                Status1 = (String) js.get("Status");
                AppointmentID=js.getString("AppointmentID");
                PatientName=(String)js.get("PatientName");
                EmailID = (String) js.get("EmailID");
                MobileNo=(String) js.get("MobileNo");
                //Prescription=(String) js.get("Prescription");
                PatientID= js.getString("PatientID");
                Comments=(String) js.get("Comments");
                ReasonAppointments=(String) js.get("ReasonAppointments");
                // AadharNumber=(String) js.get("Aadharnumber");
                TimeSlots=(String) js.get("TimeSlots");
                age=(String)js.get("Age");


                PatientData_DoctorTodaysAppointmentsClass myPatientData=new PatientData_DoctorTodaysAppointmentsClass(docId,docMobile,Dstatus,Status1,AppointmentID,PatientName,EmailID,MobileNo,PatientID,Comments,ReasonAppointments,TimeSlots,age);

                data_list.add(myPatientData);

//                System.out.println("DStatus"+Dstatus);
//                System.out.println("Status1"+Status1);
//                System.out.println("AppointmentID"+AppointmentID);
//                System.out.println("EmailID"+EmailID);
//                System.out.println("MobileNo"+MobileNo);
//                System.out.println("Prescription"+Prescription);
//                System.out.println("PatientID"+PatientID);
//                System.out.println("Comments"+Comments);
//                System.out.println("ReasonAppointments"+ReasonAppointments);
//                System.out.println("AadharNumber"+AadharNumber);
//                System.out.println("TimeSlots"+TimeSlots);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}