package com.example.cool.patient.patient.MyDiagnosticAppointments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.common.ChangePassword;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.patient.MyDoctorAppointments.PatientMyDoctorAppointments;
import com.example.cool.patient.patient.PatientDashBoard;
import com.example.cool.patient.R;
import com.example.cool.patient.patient.PatientEditProfile;
import com.example.cool.patient.patient.PatientSideNavigationExpandableListAdapter;
import com.example.cool.patient.patient.PatientSideNavigationExpandableSubList;
import com.example.cool.patient.patient.ViewDiagnosticsList.GetCurrentDiagnosticsList;
import com.example.cool.patient.patient.ViewDoctorsList.GetCurrentDoctorsList;
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

public class PatientMyDiagnosticAppointments extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    static String getUserId;
    String mobile_number,uploadServerUrl,date,date2,status1;
    RecyclerView.LayoutManager layoutManager;
    String RequestDate,PatientName,CenterName,TestName,DiagnosticsStatus,DiagnosticReport,paymentmode,Amount,Comment;
    String AppointmentID,DiagAddressId;

    com.prolificinteractive.materialcalendarview.MaterialCalendarView calendarView;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
    private SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEEE", Locale.getDefault());
    String ss;
    String d;
    Date dayformat;
    String day;

    TextView accept,reject,cancel,reschedule,total,yearMonth;
    static final int DATE_DIALOG_ID = 1;
    private int mYear;
    private int mMonth;
    private int mDay;

    RecyclerView recyclerView;
    ImageView imageView;

    int acceptcnt,rejectcnt,cancelcnt,reschedulecnt;
    PatientMyDiagnosticAppointmentsHistoryAdapter myDiagnosticAppointmentsAdapter;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private YearMonthPickerDialog.OnDateSetListener onDateSetListener;
    ProgressDialog progressDialog;
    List<CalendarDay> events=null;
    private List<PatientMyDiagnosticAppointmentDetailsClass> data_list = null;

    ApiBaseUrl baseUrl;

    // expandable list view

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_diagnostic_appointments);

        baseUrl = new ApiBaseUrl();

        calendarView=(com.prolificinteractive.materialcalendarview.MaterialCalendarView ) findViewById(R.id.calendar);

        recyclerView=(RecyclerView) findViewById(R.id.recycler_view);
        layoutManager=new LinearLayoutManager(this);

        Calendar cal=Calendar.getInstance();
        calendarView.setDateSelected(cal.getTime(),true);


        //get user details based on id
        mobile_number = getIntent().getStringExtra("mobile");
        getUserId = getIntent().getStringExtra("id");
        System.out.print("userid in patient myDiagApp....."+getUserId);

        new GetPatientDetails().execute(baseUrl.getUrl()+"MyDiagAppointments"+"?PatientID="+getUserId);

        imageView=(ImageView) findViewById(R.id.img1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showyearpicker();

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("My Diagnostic Appointments");
//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(PatientMyDiagnosticAppointments.this,PatientDashBoard.class);
//                        intent.putExtra("id",getUserId);
//                        intent.putExtra("mobile",mobile_number);
//                        startActivity(intent);
//
//                    }
//                }
//
//        );

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
                    Intent editProfile = new Intent(PatientMyDiagnosticAppointments.this,PatientEditProfile.class);
                    editProfile.putExtra("id",getUserId);
                    startActivity(editProfile);

                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                    Intent contact = new Intent(PatientMyDiagnosticAppointments.this,AboutUs.class);
                    startActivity(contact);

                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here

                    Intent contact = new Intent(PatientMyDiagnosticAppointments.this,ReachUs.class);
                    startActivity(contact);


                }
                else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(PatientMyDiagnosticAppointments.this,Login.class);
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

                        Intent i = new Intent(PatientMyDiagnosticAppointments.this,GetCurrentDoctorsList.class);
                        i.putExtra("userId",getUserId);
                        i.putExtra("mobile",mobile_number);
                        startActivity(i);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_2) {
                        Intent i = new Intent(PatientMyDiagnosticAppointments.this,GetCurrentDiagnosticsList.class);
                        i.putExtra("userId",getUserId);
                        i.putExtra("mobile",mobile_number);
                        startActivity(i);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_3) {

                        // call activity here

//                        Intent in = new Intent(PatientDashBoard.this,GetCurrentMedicalShopsList.class);
//                        in.putExtra("userId",getUserId);
//                        in.putExtra("mobile",mobile_number);
//                        startActivity(in);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_4) {

                        // call activity here

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_5) {

                        // call activity here
//                        Intent bloodbank = new Intent(PatientDashBoard.this,BloodBank.class);
//                        startActivity(bloodbank);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_6) {

                        // call activity here

                    }

                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM3) {

                    if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM3_1) {

                        // call activity here
                        Intent intent = new Intent(PatientMyDiagnosticAppointments.this,ChangePassword.class);
                        intent.putExtra("mobile",mobile_number);
                        startActivity(intent);


                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                }

                else if(groupPosition == PatientSideNavigationExpandableListAdapter.ITEM2) {
                    if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_1) {

                        // call activity here

                        Intent intent = new Intent(PatientMyDiagnosticAppointments.this,PatientMyDoctorAppointments.class);
                        intent.putExtra("mobile",mobile_number);
                        intent.putExtra("id",getUserId);
                        startActivity(intent);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent intent = new Intent(PatientMyDiagnosticAppointments.this,PatientMyDiagnosticAppointments.class);
                        intent.putExtra("mobile",mobile_number);
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

                            System.out.println("day1...."+day1);
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

    //Get patient list based on id from api call
    private class GetPatientDetails extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(PatientMyDiagnosticAppointments.this);
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

            Log.e("TAG result    ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            progressDialog.dismiss();
            getdetails(result);

        }
    }

    private void getdetails(String result) {

        data_list=new ArrayList<>();

        events = new ArrayList<>();

        final String value=null;

        try {
            JSONArray jsonArray=new JSONArray(result);

            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject  js= jsonArray.getJSONObject(i);
                DiagAddressId = js.getString("DiagAddressID");
                AppointmentID = js.getString("AppintmentID");
                RequestDate = (String) js.get("RequestedDate");
                PatientName = (String) js.get("PatientName");
                CenterName = (String) js.get("CenterName");
                TestName = js.optString("TestName");
                DiagnosticsStatus = (String) js.get("Diagnosticstatus");
                DiagnosticReport = (String) js.get("DiagnosticReport");
                paymentmode=(String) js.get("PaymentMode");
                Amount = (String) js.get("Amount");
                Comment=(String) js.get("Comment");
                String arr[]=RequestDate.split(" ");
                date=arr[0];
                PatientMyDiagnosticAppointmentDetailsClass patientAppointmentDetailsinDiagnostics=new PatientMyDiagnosticAppointmentDetailsClass(DiagAddressId,getUserId,mobile_number,AppointmentID,RequestDate,PatientName,CenterName,TestName,DiagnosticsStatus,DiagnosticReport,paymentmode,Amount,Comment,date);

                //System.out.println("testname"+TestName);
                if(date.equals(d))
                {

                    data_list.add(patientAppointmentDetailsinDiagnostics);
                }



                Date date1 = simpleDateFormat.parse(date);
                CalendarDay day = CalendarDay.from(date1);
                events.add(day);
//                dates.add(date);
//                doctorname.add(DoctorName);
//                patientnames.add(PatientName);
//                timeslot.add(Timeslot);
//                statuss.add(AppointmentStatus);
//                reason.add(Reason);
//                comment.add(DoctorComment);
//                amount.add(Amount);
//                prescription.add(Prescription);
//                payment.add(paymentmode);



            }



            calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                    System.out.println("Selected Date"+date);

                    int m = date.getMonth()+1;
                    d = m+"/"+date.getDay()+"/"+date.getYear();
                    System.out.println("length"+data_list.size());

                    new GetPatientDetails().execute(baseUrl.getUrl()+"MyDiagAppointments"+"?PatientId="+getUserId);

                    myDiagnosticAppointmentsAdapter=new PatientMyDiagnosticAppointmentsHistoryAdapter(getApplicationContext(),data_list,d);

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(myDiagnosticAppointmentsAdapter);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        EventDecorator eventDecorator1 =new EventDecorator(this,events);
        calendarView.addDecorator((DayViewDecorator) eventDecorator1);
    }

}
