package com.example.cool.patient.doctor.TodaysAppointments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
//import com.example.cool.patient.EventDecorator;
import com.example.cool.patient.R;
import com.example.cool.patient.common.ChangePassword;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.doctor.AddAddress.DoctorAddAddress;
import com.example.cool.patient.doctor.AddAddress.DoctorAddAddressFromMaps;
import com.example.cool.patient.doctor.DashBoardCalendar.DoctorDashboard;
import com.example.cool.patient.doctor.DoctorEditProfile;
import com.example.cool.patient.doctor.DoctorSideNavigatioExpandableSubList;
import com.example.cool.patient.doctor.DoctorSideNavigationExpandableListAdapter;
import com.example.cool.patient.doctor.ManageAddress.DoctorManageAddress;
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

public class PatientHistoryInDoctor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    MaterialCalendarView calendarView;
    ImageView img,doctorImage;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    String Aaadharnumber,Patientname,Mobilenumber,reason,doctorname,speciality,appoinmentdate,doctorcomment;

    String date,d;

    TextView count;

    String patientId, doctorImageUrl, doctorId,doctorMobile;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_history_in_doctor);

        baseUrl = new ApiBaseUrl();

        calendarView=(com.prolificinteractive.materialcalendarview.MaterialCalendarView ) findViewById(R.id.calendar);

        img=(ImageView) findViewById(R.id.img1);

        doctorMobile = getIntent().getStringExtra("doctorMobile");
        doctorId = getIntent().getStringExtra("id");
        patientId = getIntent().getStringExtra("patientId");
        doctorImageUrl = getIntent().getStringExtra("doctorImageUrl");

        count=(TextView)findViewById(R.id.appointmentcount);

        new GetDoctorDetails().execute(baseUrl.getUrl()+"GetDoctorByID"+"?id="+doctorId);

        new GetPatientHistoryDetails().execute(baseUrl.getUrl()+"DocPatientHistory"+"?PatientID="+patientId);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showyearpicker();

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
//        toolbar.setTitle("Todays Appointments");
//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(PatientHistoryInDoctor.this,DoctorTodaysAppointmentsForPatient.class);
//                        intent.putExtra("mobile",doctorMobile);
//                        intent.putExtra("id",doctorId);
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

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_doctor_dashboard);

        sidenavName = (TextView) headerLayout.findViewById(R.id.name);
        sidenavEmail = (TextView) headerLayout.findViewById(R.id.emailId);
        sidenavMobile = (TextView) headerLayout.findViewById(R.id.mobile);
        sidenavDoctorImage = (ImageView) headerLayout.findViewById(R.id.profileImageId);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
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
                    Intent contact = new Intent(PatientHistoryInDoctor.this,DoctorEditProfile.class);
                    contact.putExtra("id",doctorId);
                    contact.putExtra("mobile",doctorMobile);
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                    Intent i = new Intent(PatientHistoryInDoctor.this,SubscriptionPlanAlertDialog.class);
                    i.putExtra("id",doctorId);
                    i.putExtra("module","doc");
                    startActivity(i);

                } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here
                    Intent contact = new Intent(PatientHistoryInDoctor.this,AboutUs.class);
                    startActivity(contact);

                } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(PatientHistoryInDoctor.this,ReachUs.class);
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here
                    Intent contact = new Intent(PatientHistoryInDoctor.this,Login.class);
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

                        Intent i = new Intent(PatientHistoryInDoctor.this,DoctorDashboard.class);
                        i.putExtra("id",doctorId);
                        i.putExtra("mobile",doctorMobile);
                        startActivity(i);


                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM1_2) {

                        // call activity here

                        Intent i = new Intent(PatientHistoryInDoctor.this,DoctorTodaysAppointmentsForPatient.class);
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

                        Intent about = new Intent(PatientHistoryInDoctor.this,ChangePassword.class);
                        about.putExtra("mobile",doctorMobile);
                        startActivity(about);

                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                } else if(groupPosition == DoctorSideNavigationExpandableListAdapter.Address) {
                    if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_1) {


                        Intent about = new Intent(PatientHistoryInDoctor.this,DoctorAddAddress.class);
                        about.putExtra("id",doctorId);
                        about.putExtra("mobile",doctorMobile);
                        startActivity(about);

                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent about = new Intent(PatientHistoryInDoctor.this,DoctorManageAddress.class);
                        about.putExtra("id",doctorId);
                        about.putExtra("mobile",doctorMobile);
                        startActivity(about);

                    }

                }
                return true;

            }
        });

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

            Intent intent = new Intent(PatientHistoryInDoctor.this,DoctorDashboard.class);
            intent.putExtra("id",doctorId);
            intent.putExtra("mobile",doctorMobile);
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

    private class GetPatientHistoryDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(PatientHistoryInDoctor.this);
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
        final List<CalendarDay> events = new ArrayList<>();

        final List<String> aadhar = new ArrayList<String>();
        final List<String> patientnames = new ArrayList<>();
        final List<String> mobilenumbers = new ArrayList<>();
        final List<String> reasonlist = new ArrayList<>();
        final List<String> doctornames = new ArrayList<String>();
        final List<String> specialitylist = new ArrayList<>();
        final List<String> dates = new ArrayList<>();
        final List<String> comment = new ArrayList<>();


        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Aaadharnumber = jsonObject.getString("AadharNumber");
                Patientname = jsonObject.getString("PatientName");
                Mobilenumber = jsonObject.getString("MobileNumber");
                reason = jsonObject.getString("ReasonAppointments");
                doctorname = jsonObject.getString("DoctorName");
                speciality = jsonObject.getString("Speciality");
                appoinmentdate = jsonObject.getString("AppointmentDateStr");
                doctorcomment = jsonObject.getString("DoctorComment");

                String arr[] = appoinmentdate.split(" ");
                date = arr[0];
                Date date1 = simpleDateFormat.parse(date);
                CalendarDay day = CalendarDay.from(date1);
//                int month=day.getMonth();
//                System.out.println();
                aadhar.add(Aaadharnumber);
                patientnames.add(Patientname);
                mobilenumbers.add(Mobilenumber);
                reasonlist.add(reason);
                doctornames.add(doctorname);
                specialitylist.add(speciality);
                dates.add(date);
                comment.add(doctorcomment);
                events.add(day);

            }

            int length=jsonArray.length();
            String l=Integer.toString(length);
            count.setText(l);

            calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {


                    System.out.println("Selected Date" + date);

                    int m = date.getMonth() + 1;
                    d = m + "/" + date.getDay() + "/" + date.getYear();
                    System.out.println("dateeeee" + d);

                    if (dates.contains(d)) {


                        Intent i = new Intent(PatientHistoryInDoctor.this, ViewPatientHistoryInDoctor.class);
                        i.putExtra("date", d);
                        i.putExtra("patientId",patientId);
                        i.putExtra("doctorImageUrl",doctorImageUrl);
                        i.putExtra("id",doctorId);
                        i.putExtra("doctorMobile",doctorMobile);
                        i.putStringArrayListExtra("aadharnumber", (ArrayList<String>) aadhar);
                        i.putStringArrayListExtra("patientname", (ArrayList<String>) patientnames);
                        i.putStringArrayListExtra("mobilenumbers", (ArrayList<String>) mobilenumbers);
                        i.putStringArrayListExtra("reason", (ArrayList<String>) reasonlist);
                        i.putStringArrayListExtra("doctorname", (ArrayList<String>) doctornames);
                        i.putStringArrayListExtra("speciality", (ArrayList<String>) specialitylist);
                        i.putStringArrayListExtra("dates", (ArrayList<String>) dates);
                        i.putStringArrayListExtra("comment", (ArrayList<String>) comment);
                        startActivity(i);
                    } else {
                        showalert();
                    }

                }
            });

            EventDecorator eventDecorator = new EventDecorator(this, events);
            calendarView.addDecorator((DayViewDecorator) eventDecorator);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showalert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(PatientHistoryInDoctor.this);
        builder.setMessage("You don't have history today");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();

    }
}
