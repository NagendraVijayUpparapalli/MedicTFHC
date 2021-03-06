package com.example.cool.patient.diagnostic.TodaysAppointments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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

import com.example.cool.patient.R;
import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.common.ChangePassword;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.common.Offers;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.diagnostic.AddAddress.DiagnosticAddAddress;
import com.example.cool.patient.diagnostic.DashBoardCalendar.DiagnosticDashboard;
import com.example.cool.patient.diagnostic.DiagnosticEditProfile;
import com.example.cool.patient.diagnostic.DiagnosticSideNavigationExpandableListAdapter;
import com.example.cool.patient.diagnostic.DiagnosticSideNavigationExpandableSubList;
import com.example.cool.patient.diagnostic.ManageAddress.DiagnosticManageAddress;
import com.example.cool.patient.subscriptionPlan.SubscriptionPlanAlertDialog;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainPatientHistoryInDiagnostics extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    com.prolificinteractive.materialcalendarview.MaterialCalendarView calendarView;
    ImageView img;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
    String patientid;
    TextView count;
    String d;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    String AadharNumber,PatientName,Mobileno,centername,testname,comments,date;
    int RdId;
    DiagnosticsPatientHistoryAdapter adapter;
    List<CalendarDay> events=null;
    List<String> dates=null;
    List<String> timings=null;
    private List<DiagnosticsPatientHistoryDataClass> data_list = null;
    String diagId,diagMobile;

    // expandable list view
    ProgressDialog progressDialog;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    //sidenav fields
    TextView sidenavName,sidenavEmail,sidenavMobile;
    ApiBaseUrl baseUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_patient_history_in_diagnostics);

        baseUrl = new ApiBaseUrl();

        calendarView=(com.prolificinteractive.materialcalendarview.MaterialCalendarView ) findViewById(R.id.calendar);

        img=(ImageView) findViewById(R.id.img1);


        // count=(TextView)findViewById(R.id.appointmentcount);

        patientid=getIntent().getStringExtra("patientid");
        diagId = getIntent().getStringExtra("diagId");
        diagMobile = getIntent().getStringExtra("diagMobile");

//        i.putExtra("diagId",diagnosticId);

        System.out.println("patient id in main history.."+patientid);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showyearpicker();

            }
        });


        new GetDiagnosticDetails().execute(baseUrl.getUrl()+"DiagnosticByID"+"?id="+diagId);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                System.out.println("Selected Date"+date);

                int m = date.getMonth()+1;
                d = m+"/"+date.getDay()+"/"+date.getYear();
                System.out.println("length.."+data_list.size());

                new GetPatientDetails().execute(baseUrl.getUrl()+"DiagPatientHistory"+"?PatientId="+patientid);

                data_list = new ArrayList<>();
//
                recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
                layoutManager=new LinearLayoutManager(MainPatientHistoryInDiagnostics.this);

//                adapter=new DiagnosticsPatientHistoryAdapter(getApplicationContext(),data_list,d);

            }
        });

        new GetPatientDetails().execute(baseUrl.getUrl()+"DiagPatientHistory"+"?PatientID="+patientid);

        data_list = new ArrayList<>();
//
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        layoutManager=new LinearLayoutManager(this);

        adapter=new DiagnosticsPatientHistoryAdapter(getApplicationContext(),data_list,d);


        System.out.println("diag id main his patient.."+diagId+"...."+diagMobile);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("Patient History");
//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(MainPatientHistoryInDiagnostics.this,DiagnosticsTodaysAppointments.class);
//                        intent.putExtra("userId",diagId);
//                        intent.putExtra("mobile",diagMobile);
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

        sidenavName = (TextView) navigationView.findViewById(R.id.name);
        sidenavEmail = (TextView) navigationView.findViewById(R.id.email);
        sidenavMobile = (TextView) navigationView.findViewById(R.id.mobile);

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
                    Intent contact = new Intent(MainPatientHistoryInDiagnostics.this,DiagnosticEditProfile.class);
                    contact.putExtra("id",diagId);
                    contact.putExtra("mobile",diagMobile);
                    contact.putExtra("user","old");
                    startActivity(contact);

                }

                else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                    Intent subscript = new Intent(MainPatientHistoryInDiagnostics.this,SubscriptionPlanAlertDialog.class);
                    subscript.putExtra("id",diagId);
                    subscript.putExtra("mobile",diagMobile);
                    subscript.putExtra("module","diag");
                    startActivity(subscript);

                } else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here
                    Intent contact = new Intent(MainPatientHistoryInDiagnostics.this,Offers.class);
                    contact.putExtra("id",diagId);
                    contact.putExtra("mobile",diagMobile);
                    contact.putExtra("module","diag");
                    startActivity(contact);

                } else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(MainPatientHistoryInDiagnostics.this,ReachUs.class);
                    contact.putExtra("id",diagId);
                    contact.putExtra("module","diag");
                    contact.putExtra("mobile",diagMobile);
                    startActivity(contact);

                }

                else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here
                    Intent contact = new Intent(MainPatientHistoryInDiagnostics.this,Login.class);
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

                        Intent i = new Intent(MainPatientHistoryInDiagnostics.this,DiagnosticDashboard.class);
                        i.putExtra("id",diagId);
                        i.putExtra("mobile",diagMobile);
                        startActivity(i);

                    }
                    else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM1_2) {

                        // call activity here

                        Intent i = new Intent(MainPatientHistoryInDiagnostics.this,DiagnosticsTodaysAppointments.class);
                        i.putExtra("userId",diagId);
                        i.putExtra("mobile",diagMobile);
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

                        Intent about = new Intent(MainPatientHistoryInDiagnostics.this,ChangePassword.class);
                        about.putExtra("id",diagId);
                        about.putExtra("mobile",diagMobile);
                        about.putExtra("module","diag");
                        startActivity(about);

                    }
                    else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                } else if(groupPosition == DiagnosticSideNavigationExpandableListAdapter.Address) {
                    if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM2_1) {


                        Intent about = new Intent(MainPatientHistoryInDiagnostics.this,DiagnosticAddAddress.class);
                        about.putExtra("id",diagId);
                        about.putExtra("mobile",diagMobile);
                        startActivity(about);

                    }
                    else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent about = new Intent(MainPatientHistoryInDiagnostics.this,DiagnosticManageAddress.class);
                        about.putExtra("id",diagId);
                        about.putExtra("mobile",diagMobile);
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

            Intent intent = new Intent(MainPatientHistoryInDiagnostics.this,DiagnosticDashboard.class);
            intent.putExtra("id",diagId);
            intent.putExtra("mobile",diagMobile);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void showyearpicker()

    {

        YearMonthPickerDialog yearMonthPickerDialog = new YearMonthPickerDialog(this,
                new YearMonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onYearMonthSet(int year, int month) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        month=month+1;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
                        String date=month+"/"+8+"/"+year;
                        System.out.println("datepicker date"+date);
                        Date date1 = null;
                        try {
                            date1 = simpleDateFormat.parse(date);
                            CalendarDay day1 = CalendarDay.from(date1);

                            System.out.println("day1"+day1);
                            calendarView.setCurrentDate(day1);


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });

        yearMonthPickerDialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    private class GetPatientDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
//            progressDialog = new ProgressDialog(MainPatientHistoryInDiagnostics.this);
//            // Set progressdialog title
////            progressDialog.setTitle("Your searching process is");
//            // Set progressdialog message
//            progressDialog.setMessage("Loading...");
//
//            progressDialog.setIndeterminate(false);
//            // Show progressdialog
//            progressDialog.show();

            progressDialog = new ProgressDialog(MainPatientHistoryInDiagnostics.this);
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
//            JSONArray jsonArray = new JSONArray(result);

            progressDialog.dismiss();
            getdetails(result);

        }
    }

    private void getdetails(String result) {


        events = new ArrayList<>();
        dates=new ArrayList<String>();

        timings=new ArrayList<String>();
        final String value=null;
        int jsonCount =0;

        try {
            JSONArray jsonArray=new JSONArray(result);



            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject js= jsonArray.getJSONObject(i);
                AadharNumber = (String) js.get("AadharNumber");
                PatientName = (String) js.get("PatientName");
                Mobileno = (String) js.get("MobileNumber");
                centername = (String) js.get("CenterName");
                testname =(String) js.get("TestName");
                comments=(String) js.get("Comment");
                date=(String)  js.get("Date");
                RdId=js.getInt("Rdid");

                String arr[]=date.split(" ");
                date=arr[0];
                Date date1 = simpleDateFormat.parse(date);
                CalendarDay day = CalendarDay.from(date1);
                events.add(day);
                dates.add(date);

                Calendar cal=Calendar.getInstance();
                int year1=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day1=cal.get(Calendar.DAY_OF_MONTH);

                String currentDate = month+1+"/"+day1+"/"+year1;

                System.out.println("cur date..."+currentDate);

                System.out.println("json date..."+date);
                System.out.println("cal date..."+d);

                if(date.equals(currentDate) && d == null) {
                    jsonCount = 1;

                    DiagnosticsPatientHistoryDataClass patientHistoryDatainDiagnostics=new DiagnosticsPatientHistoryDataClass(patientid,diagId,diagMobile,AadharNumber,PatientName,Mobileno,centername,testname,comments,date,RdId);

                    data_list.add(patientHistoryDatainDiagnostics);

                    adapter=new DiagnosticsPatientHistoryAdapter(getApplicationContext(),data_list,d);

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }

                else if(date.equals(d) && d!=null)
                {
                    jsonCount = 1;

                    DiagnosticsPatientHistoryDataClass patientHistoryDatainDiagnostics=new DiagnosticsPatientHistoryDataClass(patientid,diagId,diagMobile,AadharNumber,PatientName,Mobileno,centername,testname,comments,date,RdId);

                    data_list.add(patientHistoryDatainDiagnostics);

                    adapter=new DiagnosticsPatientHistoryAdapter(getApplicationContext(),data_list,d);

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(jsonCount == 0)
        {
            showCurrentDateNotMatchMessage();
            recyclerView.setVisibility(View.GONE);
        }
        else
        {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
        }

        EventDecorator eventDecorator1 =new EventDecorator(this,events);
        calendarView.addDecorator((DayViewDecorator) eventDecorator1);
    }

    public void showCurrentDateNotMatchMessage(){

        android.support.v7.app.AlertDialog.Builder a_builder = new android.support.v7.app.AlertDialog.Builder(MainPatientHistoryInDiagnostics.this);
        a_builder.setMessage("No records found.")
                .setCancelable(false)
                .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Your selected date has");
        alert.show();
    }

}