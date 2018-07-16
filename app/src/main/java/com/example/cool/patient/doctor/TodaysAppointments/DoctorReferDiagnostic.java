package com.example.cool.patient.doctor.TodaysAppointments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.example.cool.patient.doctor.ManageAddress.DoctorManageAddress;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorReferDiagnostic extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Context context;
    private ReferDiagRecyclerViewAdapter adapter;
    private ArrayList<String> arrayList;
    private Button selectButton;
    SearchableSpinner spinner;

    HashMap<Long, String> myDiagSpecialityList = new HashMap<Long, String>();
    List<String> specialityList;
    ArrayAdapter<String> spinnerAdapter;

    List selectedSpecialityListItems =null;

    HashMap<Long, String> myDiagRangeList = new HashMap<Long, String>();
    List<String> diagCenterList;

    ProgressDialog progressDialog;
    ProgressDialog progressDialog1;

    String appointmentId = null;

    ApiBaseUrl baseUrl;

    String referStatus;

    // expandable list view

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    //sidenav fields
    TextView sidenavName,sidenavEmail,sidenavMobile;
    ImageView sidenavDoctorImage;

    String doctorId,doctorMobile;

    Dialog MyDialog1;
    TextView message;
    LinearLayout oklink;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_refer_diagnostic);

        baseUrl = new ApiBaseUrl();

        appointmentId = getIntent().getStringExtra("aid");

        referStatus = getIntent().getStringExtra("refer");

        doctorMobile = getIntent().getStringExtra("mobile");
        doctorId = getIntent().getStringExtra("id");

        System.out.println("precript.."+getIntent().getStringExtra("prescription"));

        if(referStatus.equals("Yes"))
        {
            new GetAllDiagSpeciality().execute(baseUrl.getUrl()+"GetDiagSpeciality");

            new GetDiagnosticsRange().execute(baseUrl.getUrl()+"GetDiagnosticRange?AppointmentID="+appointmentId);
        }
        else
        {
//            new GetDiagnosticsRange().execute(baseUrl.getUrl()+"GetDiagnosticRange?AppointmentID="+appointmentId);
            String js = formatDoctorTimingsDataAsJson();
            new sendDoctorReferDiagnosticDetails().execute(baseUrl.getUrl()+"DoctotUpdateTodaysAppointment",js.toString());
        }

        selectButton = (Button) findViewById(R.id.select_button);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check the current text of Select Button
                if (selectButton.getText().toString().equals(getResources().getString(R.string.select_all))) {

                    //If Text is Select All then loop to all array List items and check all of them
                    for (int i = 0; i < arrayList.size(); i++)
                        adapter.checkCheckBox(i, true);

                    //After checking all items change button text
                    selectButton.setText(getResources().getString(R.string.deselect_all));
                } else {
                    //If button text is Deselect All remove check from all items
                    adapter.removeSelection();

                    //After checking all items change button text
                    selectButton.setText(getResources().getString(R.string.select_all));
                }
            }
        });

//        populateRecyclerView();
        onClickEvent();

        spinner = (SearchableSpinner) findViewById(R.id.spinner);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("Refer Diagnostic");

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
                    Intent contact = new Intent(DoctorReferDiagnostic.this,DoctorEditProfile.class);
                    contact.putExtra("id",doctorId);
                    contact.putExtra("mobile",doctorMobile);
                    contact.putExtra("user","old");
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                    Intent i = new Intent(DoctorReferDiagnostic.this,SubscriptionPlanAlertDialog.class);
                    i.putExtra("id",doctorId);
                    i.putExtra("mobile",doctorMobile);
                    i.putExtra("module","doc");
                    startActivity(i);

                } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here
                    Intent contact = new Intent(DoctorReferDiagnostic.this,Offers.class);
                    contact.putExtra("id",doctorId);
                    contact.putExtra("mobile",doctorMobile);
                    contact.putExtra("module","doc");
                    startActivity(contact);

                }

                 else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(DoctorReferDiagnostic.this,ReachUs.class);
                    contact.putExtra("id",doctorId);
                    contact.putExtra("mobile",doctorMobile);
                    contact.putExtra("module","doc");
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here
                    Intent contact = new Intent(DoctorReferDiagnostic.this,Login.class);
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

                        Intent i = new Intent(DoctorReferDiagnostic.this,DoctorDashboard.class);
                        i.putExtra("id",doctorId);
                        i.putExtra("mobile",doctorMobile);
                        startActivity(i);


                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM1_2) {

                        // call activity here

                        Intent i = new Intent(DoctorReferDiagnostic.this,DoctorTodaysAppointmentsForPatient.class);
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

                        Intent about = new Intent(DoctorReferDiagnostic.this,DoctorChangePassword.class);
                        about.putExtra("id",doctorId);
                        about.putExtra("mobile",doctorMobile);
                        startActivity(about);

                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                } else if(groupPosition == DoctorSideNavigationExpandableListAdapter.Address) {
                    if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_1) {


                        Intent about = new Intent(DoctorReferDiagnostic.this,DoctorAddAddress.class);
                        about.putExtra("id",doctorId);
                        about.putExtra("mobile",doctorMobile);
                        startActivity(about);

                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent about = new Intent(DoctorReferDiagnostic.this,DoctorManageAddress.class);
                        about.putExtra("id",doctorId);
                        about.putExtra("mobile",doctorMobile);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    //Get all diagnostic specialities from api call
    public class GetAllDiagSpeciality extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(DoctorReferDiagnostic.this);
            // Set progressdialog title
            progressDialog.setTitle("Your searching process is");
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

            progressDialog.dismiss();
            Log.e("TAG result prev timings", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getAllDiagSpecialities(result);


        }
    }


    private void getAllDiagSpecialities(String result) {
        try
        {
            JSONArray jsonArr = new JSONArray(result);
            specialityList = new ArrayList<>();


            for (int i = 0; i < jsonArr.length(); i++) {

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                Long cityKey = jsonObj.getLong("Key");
                String cityValue = jsonObj.getString("Value");
                myDiagSpecialityList.put(cityKey,cityValue);
                specialityList.add(jsonObj.getString("Value"));



            }

            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);


            arrayList = new ArrayList<>();
            for (int i = 0; i <specialityList.size(); i++)
                arrayList.add(specialityList.get(i));//Adding items to recycler view
            adapter = new ReferDiagRecyclerViewAdapter(context, arrayList);
            recyclerView.setAdapter(adapter);

        }
        catch (JSONException e)
        {}
    }


    //Get all diagnostic range from api call
    public class GetDiagnosticsRange extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog1 = new ProgressDialog(DoctorReferDiagnostic.this);
            // Set progressdialog title
            progressDialog1.setTitle("Your searching process is");
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
            progressDialog1.dismiss();

            Log.e("TAG result diag centers", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getDiagRange(result);


        }
    }

    private void getDiagRange(String result) {
        try
        {
            JSONArray jsonArr = new JSONArray(result);
            diagCenterList = new ArrayList<>();


            for (int i = 0; i < jsonArr.length(); i++) {

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                Long cityKey = jsonObj.getLong("Key");
                String cityValue = jsonObj.getString("Value");
                myDiagRangeList.put(cityKey,cityValue);
                diagCenterList.add(jsonObj.getString("Value"));

            }

            // Initializing an ArrayAdapter
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                    this,R.layout.refer_diag_spinner_item,diagCenterList
            );
            spinnerArrayAdapter.setDropDownViewResource(R.layout.refer_diag_spinner_item);
            spinner.setAdapter(spinnerArrayAdapter);

//            System.out.println("sun prev.."+prevSunTimeSlotsList);

        }
        catch (JSONException e)
        {}
    }

    public void showMessage(String s){

        final AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(s)
                .setCancelable(false)
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(DoctorReferDiagnostic.this,DoctorReferDiagnostic.class);
////                        intent.putExtra("id",getUserId);
//                        startActivity(intent);
                        dialog.cancel();
                    }
                })

                .setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String js = formatDoctorTimingsDataAsJson();
                        System.out.println("js.."+js.toString());
                        new sendDoctorReferDiagnosticDetails().execute(baseUrl.getUrl()+"InsertDoctorReferDiagnostics",js.toString());

                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("You selected specialities");
        alert.show();

    }


    private void onClickEvent() {
        findViewById(R.id.show_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedSpecialityListItems = new ArrayList();

                SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
                //Check if item is selected or not via size
                if (selectedRows.size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    //Loop to all the selected rows array
                    for (int i = 0; i < selectedRows.size(); i++) {

                        //Check if selected rows have value i.e. checked item
                        if (selectedRows.valueAt(i)) {

                            //Get the checked item text from array list by getting keyAt method of selectedRowsarray
                            String selectedRowLabel = arrayList.get(selectedRows.keyAt(i));
                            System.out.println("item please"+selectedRowLabel);

                            //append the row label text
                            selectedSpecialityListItems.add(selectedRowLabel);
                            stringBuilder.append(selectedRowLabel + "\n");

                            System.out.println("item please...."+stringBuilder.toString());
                        }
                    }
//                    Toast.makeText(getApplicationContext(),  stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                    showMessage(stringBuilder.toString());
                }



            }
        });

//        findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
//                //Check if item is selected or not via size
//                if (selectedRows.size() > 0) {
//                    //Loop to all the selected rows array
//                    for (int i = (selectedRows.size() - 1); i >= 0; i--) {
//
//                        //Check if selected rows have value i.e. checked item
//                        if (selectedRows.valueAt(i)) {
//
//                            //remove the checked item
//                            arrayList.remove(selectedRows.keyAt(i));
//                        }
//                    }
//
//                    //notify the adapter and remove all checked selection
//                    adapter.removeSelection();
//                }
//            }
//        });


//        selectButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Check the current text of Select Button
//                if (selectButton.getText().toString().equals(getResources().getString(R.string.select_all))) {
//
//                    //If Text is Select All then loop to all array List items and check all of them
//                    for (int i = 0; i < arrayList.size(); i++)
//                        adapter.checkCheckBox(i, true);
//
//                    //After checking all items change button text
//                    selectButton.setText(getResources().getString(R.string.deselect_all));
//                } else {
//                    //If button text is Deselect All remove check from all items
//                    adapter.removeSelection();
//
//                    //After checking all items change button text
//                    selectButton.setText(getResources().getString(R.string.select_all));
//                }
//            }
//        });
    }


    private String formatDoctorTimingsDataAsJson() {

        // iterate and display values
//        System.out.println("Fetching Keys and corresponding [Multiple] Values n");

        JSONObject data = new JSONObject();

        try{

            if(referStatus.equals("Yes"))
            {

                org.json.simple.JSONArray allDataArray = new org.json.simple.JSONArray();

                String a[] = new String[selectedSpecialityListItems.size()];
                //Loop index size()
                for(int index = 0; index < a.length; index++) {

                    JSONObject eachData = new JSONObject();

                    eachData.put("SpecialityID", getSpecialityKeyFromValue(myDiagSpecialityList,selectedSpecialityListItems.get(index)));
                    allDataArray.add(eachData);

                }

                data.put("PatientID",getIntent().getStringExtra("pid"));
                data.put("DiagnosticCenterID",getDiagnsticCenterKeyFromValue(myDiagRangeList,spinner.getSelectedItem().toString()));
                data.put("Prescription",getIntent().getStringExtra("prescription"));

                data.put("DComment",getIntent().getStringExtra("reason"));
                data.put("AppointmentID",getIntent().getStringExtra("aid"));

                data.put("PatientName",getIntent().getStringExtra("name"));
                data.put("SpecialityLst",allDataArray.toString());



                System.out.println("final array = " + allDataArray.toString());

                System.out.println("final array size= " + allDataArray.size());

                return data.toString();
            }

            else
            {

                data.put("AppointmentID",getIntent().getStringExtra("aid"));
                data.put("Comments",getIntent().getStringExtra("reason"));
                data.put("ReferDiagnostic",0);

                data.put("ReferMedicalShop",0);
                data.put("Prescription",getIntent().getStringExtra("prescription"));
                return data.toString();
            }

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

    public static Object getDiagnsticCenterKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }


    //send doctor refer diagnostic center details
    private class sendDoctorReferDiagnosticDetails extends AsyncTask<String, Void, String> {

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
                System.out.println("params refer add....."+params[1]);
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
            Log.e("TAG result docreferdiag", result); // this is expecting a response code to be sent from your server upon receiving the POST data
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

    public void showSuccessMessage(String message1){

        MyDialog1  = new Dialog(DoctorReferDiagnostic.this);
        MyDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog1.setContentView(R.layout.edit_success_alert);

        message = (TextView) MyDialog1.findViewById(R.id.message);
        oklink = (LinearLayout) MyDialog1.findViewById(R.id.ok);

        message.setEnabled(true);
        oklink.setEnabled(true);

        message.setText(message1);

        oklink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorReferDiagnostic.this,DoctorDashboard.class);
                intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
                intent.putExtra("id",getIntent().getStringExtra("id"));
                startActivity(intent);
            }
        });
        MyDialog1.show();

//        android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);
//
//        a_builder.setMessage(message)
//                .setCancelable(false)
//                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        dialog.cancel();
//
////                        new Mytask().execute();
//                        Intent intent = new Intent(DoctorReferDiagnostic.this,DoctorDashboard.class);
//                        intent.putExtra("mobile",getIntent().getStringExtra("mobile"));
//                        intent.putExtra("id",getIntent().getStringExtra("id"));
//                        startActivity(intent);
//                    }
//                });
//        android.app.AlertDialog alert = a_builder.create();
//        alert.setTitle("Your Appointment");
//        alert.show();

    }

    public void showErrorMessage(String message1){

        MyDialog1  = new Dialog(DoctorReferDiagnostic.this);
        MyDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog1.setContentView(R.layout.edit_fail_alert);

        message = (TextView) MyDialog1.findViewById(R.id.message);
        oklink = (LinearLayout) MyDialog1.findViewById(R.id.ok);

        message.setEnabled(true);
        oklink.setEnabled(true);

        message.setText(message1);

        oklink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog1.cancel();
            }
        });
        MyDialog1.show();


//        android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);
//
//        a_builder.setMessage(message)
//                .setCancelable(false)
//                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//        android.app.AlertDialog alert = a_builder.create();
//        alert.setTitle("Your Appointment");
//        alert.show();

    }


}
