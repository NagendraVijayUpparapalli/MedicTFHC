package com.example.cool.patient.doctor.DashBoardCalendar;

import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.common.ChangePassword;
import com.example.cool.patient.doctor.DoctorEditProfile;
import com.example.cool.patient.doctor.DoctorSideNavigatioExpandableSubList;
import com.example.cool.patient.doctor.DoctorSideNavigationExpandableListAdapter;
import com.example.cool.patient.doctor.TodaysAppointments.*;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import java.text.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cool.patient.doctor.AddAddress.DoctorAddAddress;
import com.example.cool.patient.doctor.ManageAddress.DoctorManageAddress;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.R;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.subscriptionPlan.SubscriptionPlanAlertDialog;
import com.prolificinteractive.materialcalendarview.CalendarDay;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DoctorDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //get list of bloodbank based on current position
    //lat,long
    static String str ="";
    static String getUserId;
    //    Criteria criteria;
    LocationManager locationManager;
    String lattitude,longitude;
    double lat,lng;
    Geocoder geocoder;
    List<Address> addresses;
    List<Address> fulladdress;
    private static final int REQUEST_LOCATION = 1;
    static String selectedlocation=null;
    static String selectedItemText=null;

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    String city, mobile_number ;
    TextView current_city;


    //calendar fields

    String  StatusID, Status, TotalCount;
    int Dstatus;
    String  date2,geturl,todaydate;


    ImageView imageView,profileImage;
    TextView name,email,mobile;
    Bitmap mIcon11;

    TextView pending_count, Accept_count, Reschedule_count, Reject_count;
    CardView acceptcard,pendingcard,reschedulecard,rejectcard;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
    com.prolificinteractive.materialcalendarview.MaterialCalendarView calendarView;

    ApiBaseUrl baseUrl;

    // expandable list view

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    ProgressDialog progressDialog;

    private static long back_pressed;

    int backButtonCount = 0;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottomnav_home:
                    return true;
//                case R.id.bottomnav_editprofile:
//                    Intent i = new Intent(DoctorDashboard.this,DoctorEditProfile.class);
//                    i.putExtra("id",getUserId);
//                    startActivity(i);
//                    overridePendingTransition(R.anim.goup, R.anim.godown);
//                    return true;
                case R.id.Languages_options:
//                    Intent language = new Intent (DoctorDashboard.this,SelectCity.class);
//                    startActivity(language);
//                    overridePendingTransition(R.anim.goup, R.anim.godown);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        baseUrl = new ApiBaseUrl();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Criteria criteria = new Criteria();

        mobile_number = getIntent().getStringExtra("mobile");
        getUserId = getIntent().getStringExtra("id");
        System.out.print("userid in mainactivity....."+getUserId);

        new GetDoctorDetails().execute(baseUrl.getUrl()+"GetDoctorByID"+"?id="+getUserId);

        current_city = (TextView) findViewById(R.id.select_city);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.getBestProvider(criteria,true);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

//        current_city.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(DoctorDashboard.this,DoctorDashBoardSelectCity.class);
//                startActivity(i);
//            }
//        });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_doctor_dashboard);

        name = (TextView) headerLayout.findViewById(R.id.name);
        email = (TextView) headerLayout.findViewById(R.id.emailId);
        mobile = (TextView) headerLayout.findViewById(R.id.mobile);
        profileImage = (ImageView) headerLayout.findViewById(R.id.profileImageId);

        //calendar item variables
        imageView = (ImageView) findViewById(R.id.img1);
        pending_count = (TextView) findViewById(R.id.count_pending);
        Accept_count = (TextView) findViewById(R.id.count_Accept);
        Reschedule_count = (TextView) findViewById(R.id.count_Reschedule);
        Reject_count = (TextView) findViewById(R.id.count_Reject);


        pendingcard = (CardView) findViewById(R.id.pendingCard);
        acceptcard = (CardView) findViewById(R.id.acceptCard);
        reschedulecard = (CardView) findViewById(R.id.rescheduleCard);
        rejectcard = (CardView) findViewById(R.id.rejectCard);




        pendingcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DoctorDashboard.this,GetPatientDetailsListInDoctor.class);
                intent.putExtra("doctorId",getUserId);
                intent.putExtra("date",date2);
                intent.putExtra("status","Pending");
                intent.putExtra("mobile",mobile_number);
                startActivity(intent);
            }
        });

        acceptcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DoctorDashboard.this,GetPatientDetailsListInDoctor.class);
                intent.putExtra("doctorId",getUserId);
                intent.putExtra("date",date2);
                intent.putExtra("status","Accept");
                intent.putExtra("mobile",mobile_number);
                startActivity(intent);
            }
        });

        reschedulecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DoctorDashboard.this,GetPatientDetailsListInDoctor.class);
                intent.putExtra("doctorId",getUserId);
                intent.putExtra("date",date2);
                intent.putExtra("status","Reschedule");
                intent.putExtra("mobile",mobile_number);
                startActivity(intent);
            }
        });

        rejectcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DoctorDashboard.this,GetPatientDetailsListInDoctor.class);
                intent.putExtra("doctorId",getUserId);
                intent.putExtra("date",date2);
                intent.putExtra("status","Reject");
                intent.putExtra("mobile",mobile_number);
                startActivity(intent);
            }
        });

        calendarView = (com.prolificinteractive.materialcalendarview.MaterialCalendarView) findViewById(R.id.calendar);

        Calendar cal = Calendar.getInstance();

        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH)+1;
        int day=cal.get(Calendar.DAY_OF_MONTH);

        System.out.println("getday" + year);
        System.out.println("getmonth" + month);
        System.out.println("getyear" + day);

        if (day < 10 && month < 10) {
            todaydate = year + "-" + "0" + month + "-" + "0" + day;
        }
        else if(day <10 && month >10)
        {
            todaydate = year + "-" + month + "-" + "0" + day;
        }
        else if(day >10 && month <10)
        {
            todaydate = year + "-" +"0"+month + "-" + day;
        }

        else {
            todaydate = year + "-" + month + "-" + day;
        }


        new GetAppointmentCount().execute(baseUrl.getUrl()+"GetDoctorAppointmentsCalendar"+"?Id="+getUserId+"&AppointmentDate="+todaydate);

        calendarView.setDateSelected(cal.getTime(), true);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                int month = date.getMonth() + 1;
                int day = date.getDay();
                int year = date.getYear();
                if (day < 10 && month < 10) {
                    date2 = year + "-" + "0" + month + "-" + "0" + day;
                }
                else if(day <10 && month >10)
                {
                    date2 = year + "-" + month + "-" + "0" + day;
                }
                else if(day >10 && month <10)
                {
                    date2 = year + "-" +"0"+month + "-" + day;
                }

                else {
                    date2 = year + "-" + month + "-" + day;
                }


                System.out.println("date2" + date2);

                new GetAppointmentCount().execute(baseUrl.getUrl()+"GetDoctorAppointmentsCalendar"+"?Id="+getUserId+"&AppointmentDate="+date2);


            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                showyearpicker();
            }
        });


        //side navigation
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
                    Intent contact = new Intent(DoctorDashboard.this,DoctorEditProfile.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",mobile_number);
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                    Intent i = new Intent(DoctorDashboard.this,SubscriptionPlanAlertDialog.class);
                    i.putExtra("id",getUserId);
                    i.putExtra("module","doc");
                    startActivity(i);

                } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here
                    Intent contact = new Intent(DoctorDashboard.this,AboutUs.class);
                    startActivity(contact);

                } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(DoctorDashboard.this,ReachUs.class);
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here
                    Intent contact = new Intent(DoctorDashboard.this,Login.class);
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

                        Intent i = new Intent(DoctorDashboard.this,DoctorDashboard.class);
                        i.putExtra("id",getUserId);
                        i.putExtra("mobile",mobile_number);
                        startActivity(i);


                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM1_2) {

                        // call activity here

                        Intent i = new Intent(DoctorDashboard.this,DoctorTodaysAppointmentsForPatient.class);
                        i.putExtra("id",getUserId);
                        i.putExtra("mobile",mobile_number);
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

                        Intent about = new Intent(DoctorDashboard.this,ChangePassword.class);
                        about.putExtra("mobile",mobile_number);
                        startActivity(about);

                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                } else if(groupPosition == DoctorSideNavigationExpandableListAdapter.Address) {
                    if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_1) {


                        Intent about = new Intent(DoctorDashboard.this,DoctorAddAddress.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",mobile_number);
                        startActivity(about);

                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent about = new Intent(DoctorDashboard.this,DoctorManageAddress.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",mobile_number);
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


    private void getLocation() {
        System.out.print("helo this is method");
        if (ActivityCompat.checkSelfPermission(DoctorDashboard.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (DoctorDashboard.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.print("helo this is if");

            ActivityCompat.requestPermissions(DoctorDashboard.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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
                        current_city.setText("Waiting");
                    }
                    else
                    {
                        if(addresses.size()>0)
                        {
                            city=addresses.get(0).getLocality();
                            current_city.setText(city);
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
                        current_city.setText("Waiting");
                    }
                    else
                    {
                        if(addresses.size()>0)
                        {
                            city=addresses.get(0).getLocality();
                            current_city.setText(city);
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
                        current_city.setText("Waiting");
                    }
                    else
                    {
                        if(addresses.size()>0)
                        {
                            city=addresses.get(0).getLocality();
                            current_city.setText(city);
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


    private void showyearpicker() {

        YearMonthPickerDialog yearMonthPickerDialog = new YearMonthPickerDialog(this,
                new YearMonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onYearMonthSet(int year, int month) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        month = month + 1;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
                        String date = month + "/" + 8 + "/" + year;
                        System.out.println("datepicker date" + date);
                        Date date1 = null;
                        try {
                            date1 = simpleDateFormat.parse(date);
                            CalendarDay day1 = CalendarDay.from(date1);

                            System.out.println("day1" + day1);
                            calendarView.setCurrentDate(day1);


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

        yearMonthPickerDialog.show();
    }

    //Get Appointment count based on doctor id and appointment date
    private class GetAppointmentCount extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(DoctorDashboard.this);
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

            Log.e("TAG result    ", result);
            progressDialog.dismiss();
            getcount(result);
        }
    }

    private void getcount(String result) {
        try {

            JSONArray jsonArray=new JSONArray(result);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject js = jsonArray.getJSONObject(i);

                Dstatus = js.getInt("DStatus");
                StatusID = (String) js.get("StatusID");
                Status = (String) js.get("Status");
                TotalCount = (String) js.get("TotalCount");

                System.out.println("DStatus"+Dstatus);
                System.out.println("StatusID"+StatusID);
                System.out.println("Status"+Status);
                System.out.println("TotalCount"+TotalCount);

                if(StatusID.equals("0"))
                {
                    System.out.println("this is pending ");
                    pending_count.setText(TotalCount);
                }
                else if(StatusID.equals("1"))
                {
                    System.out.println("this is reject");
                    Reject_count.setText(TotalCount);
                }
                else if(StatusID.equals("2"))
                {
                    System.out.println("this is accept ");
                    Accept_count.setText(TotalCount);
                }
                else if(StatusID.equals("3"))
                {
                    System.out.println("this is reschedule ");
                    Reschedule_count.setText(TotalCount);
                }
                else {
                    System.out.println("this is else");
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
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

                String myEmail = (String) js.get("EmailID");
                String myFirstName = (String) js.get("FirstName");
                String myLastName = (String) js.get("LastName");
                String mydoctorImage = (String) js.get("DoctorImage");
                String myMobile = (String) js.get("MobileNumber");

                System.out.println("name.."+myFirstName+".."+myLastName+".."+myEmail+".."+mydoctorImage);

                name.setText(myFirstName+" "+myLastName);
                email.setText(myEmail);
                mobile.setText(myMobile);

            new GetProfileImageTask(profileImage).execute(baseUrl.getImageUrl()+mydoctorImage);

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
            mIcon11 = null;
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
            profileImage.setImageBitmap(result);
        }

    }


//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

//    @Override
//    public void onBackPressed(){
//        if (back_pressed + 2000 > System.currentTimeMillis()){
//            super.onBackPressed();
//        }
//        else{
//            Toast.makeText(getBaseContext(), "Press once again to exit", Toast.LENGTH_SHORT).show();
//            back_pressed = System.currentTimeMillis();
//        }
//    }


    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();

            backButtonCount++;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.doctor_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

            Intent intent = new Intent (DoctorDashboard.this, DoctorTodaysAppointmentsForPatient.class);
            intent.putExtra("mobile",mobile_number);
            intent.putExtra("userId",getUserId);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent (DoctorDashboard.this,DoctorAddAddress.class);
            intent.putExtra("id",getUserId);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent (DoctorDashboard.this,DoctorManageAddress.class);
            intent.putExtra("id",getUserId);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        else if(id == R.id.Logout)  {
            Intent logout =  new Intent(DoctorDashboard.this,Login.class);
            startActivity(logout);
        }
        else if(id == R.id.change_password)  {
            Intent change =  new Intent(DoctorDashboard.this,ChangePassword.class);
            change.putExtra("mobile",mobile_number);
            startActivity(change);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
