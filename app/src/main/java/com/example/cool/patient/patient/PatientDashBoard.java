package com.example.cool.patient.patient;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.widget.ListView;

import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.common.ChangePassword;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.patient.MyDiagnosticAppointments.PatientMyDiagnosticAppointments;
import com.example.cool.patient.patient.MyDoctorAppointments.PatientMyDoctorAppointments;
import com.example.cool.patient.patient.ViewBloodBanksList.BloodBank;
import com.example.cool.patient.patient.ViewDiagnosticsList.GetCurrentDiagnosticsList;
import com.example.cool.patient.patient.ViewDoctorsList.GetCurrentDoctorsList;
import com.example.cool.patient.patient.ViewMedicalShopsList.GetCurrentMedicalShopsList;
import com.example.cool.patient.R;
import com.example.cool.patient.common.ReachUs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PatientDashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //get list of bloodbank based on current position
    //lat,long
    static String uploadServerUrl = null;
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


    TextView current_city;
    static double selectedCitylat;
    static double selectedCitylong;


    ListView listview;


    LinearLayout line1,line2,line3;
    TextView line0;
    Animation uptodown,downtoup;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    String city, mobile_number ;


    // expandable list view

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

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
//                    Intent i = new Intent(PatientDashBoard.this,PatientEditProfile.class);
//                    i.putExtra("id",getUserId);
//                    startActivity(i);
//                    overridePendingTransition(R.anim.goup, R.anim.godown);
//                    return true;
                case R.id.Languages_options:
//                   Intent language = new Intent (PatientDashBoard.this,DashboardSelectCity.class);
//                   startActivity(language);
//                    overridePendingTransition(R.anim.goup, R.anim.godown);
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        Criteria criteria = new Criteria();

        mobile_number = getIntent().getStringExtra("mobile");
        getUserId = getIntent().getStringExtra("id");
        System.out.print("userid in mainactivity....."+getUserId);

//        System.out.print("city....."+city);

        current_city = (TextView) findViewById(R.id.select_city);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.getBestProvider(criteria,true);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        //change icon colors  in navigation
//        ArcNavigationView anv=(ArcNavigationView) findViewById(R.id.nav_view);
//        anv.setItemIconTintList(null);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


//        current_city.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(PatientDashBoard.this,SelectCity.class);
//                i.putExtra("module","patientDashB");
//                i.putExtra("userId",getUserId);
//                i.putExtra("mobile",mobile_number);
//                startActivity(i);
//            }
//        });


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
                    Intent editProfile = new Intent(PatientDashBoard.this,PatientEditProfile.class);
                    editProfile.putExtra("id",getUserId);
                    startActivity(editProfile);

                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                    Intent contact = new Intent(PatientDashBoard.this,AboutUs.class);
                    startActivity(contact);

                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here

                    Intent contact = new Intent(PatientDashBoard.this,ReachUs.class);
                    startActivity(contact);


                }
                else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(PatientDashBoard.this,Login.class);
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

                        Intent i = new Intent(PatientDashBoard.this,GetCurrentDoctorsList.class);
                        i.putExtra("userId",getUserId);
                        i.putExtra("mobile",mobile_number);
                        startActivity(i);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_2) {
                        Intent i = new Intent(PatientDashBoard.this,GetCurrentDiagnosticsList.class);
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
                        Intent intent = new Intent(PatientDashBoard.this,ChangePassword.class);
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

                        Intent intent = new Intent(PatientDashBoard.this,PatientMyDoctorAppointments.class);
                        intent.putExtra("mobile",mobile_number);
                        intent.putExtra("id",getUserId);
                        startActivity(intent);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent intent = new Intent(PatientDashBoard.this,PatientMyDiagnosticAppointments.class);
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


        listview = (android.widget.ListView)findViewById(R.id.mylist);


        final List<String> list = new ArrayList<String>();
        list.add(city);
        list.add("Addateegala");
        list.add("Ainavilli");
        list.add("Alamuru");
        list.add("Allavaram");
        list.add("Amalapuram");
        list.add("Ambajipeta");
        list.add("Anakapalle");
        list.add("Anandapuram");
        list.add("Ananthagiri");
        list.add("Anaparthy");
        list.add("Araku Valley");
        list.add("Atchutapuram");
        list.add("Atreyapuram");
        list.add("Bheemunipatnam");
        list.add("Biccavolu");
        list.add("Butchayyapeta");
        list.add("Cheedikada");
        list.add("Chintapalle");
        list.add("Chodavaram");
        list.add("Devarapalle");
        list.add("Devipatnam");
        list.add("Dumbriguda");
        list.add("G.Madugula");
        list.add("Gajuwaka");
        list.add("Gandepalle");
        list.add("Gangavaram");
        list.add("Gokavaram");
        list.add("Gollaprolu");
        list.add("Golugonda");
        list.add("Gudem Kotha Veedhi");
        list.add("Hukumpeta");
        list.add("I.Polavaram");
        list.add("Jaggampeta");
        list.add("K.Kotapadu");
        list.add("Kadiam");
        list.add("Kajuluru");
        list.add("Kakinada Urban");
        list.add("Kapileswarapuram");
        list.add("Karapa");
        list.add("Kasimkota");
        list.add("Katrenikona");
        list.add("Kirlampudi");
        list.add("Korukonda");
        list.add("Kotananduru");
        list.add("Kotauratla");
        list.add("Kothapalle");
        list.add("Kothapeta");
        list.add("Koyyuru");
        list.add("Madugula");
        list.add("Makavarapalem");
        list.add("Malikipuram");
        list.add("Mamidikuduru");
        list.add("Mandapeta");
        list.add("Maredumilli");
        list.add("Mummidivaram");
        list.add("Munagapaka");
        list.add("Munchingi Puttu");
        list.add("Nakkapalle");
        list.add("Narsipatnam");
        list.add("Nathavaram");
        list.add("P Gannavaram");
        list.add("Paderu");
        list.add("Padmanabham");
        list.add("Pamarru");
        list.add("Paravada");
        list.add("Payakaraopeta");
        list.add("Peda Bayalu");
        list.add("Pedagantyada");
        list.add("Pedapudi");
        list.add("Peddapuram");
        list.add("Pendurthi");
        list.add("Pithapuram");
        list.add("Prathipadu");
        list.add("Rajahmundry Rural");
        list.add("Rajahmundry Urban");
        list.add("Rajanagaram");
        list.add("Rajavommangi");
        list.add("Ramachandrapuram");
        list.add("Rambilli");
        list.add("Rampachodavaram");
        list.add("Rangampeta");
        list.add("Ravikamatham");
        list.add("Ravulapalem");
        list.add("Rayavaram");
        list.add("Razole");
        list.add("Rolugunta");
        list.add("Rowthulapudi");
        list.add("S Rayavaram");
        list.add("Sabbavaram");
        list.add("Sakhinetipalle");
        list.add("Samalkota");
        list.add("Sankhavaram");
        list.add("Seethanagaram");
        list.add("Thallarevu");
        list.add("Thondangi");
        list.add("Tuni");
        list.add("Uppalaguptam");
        list.add("Y Ramavaram");
        list.add("Yelamanchili");
        list.add("Yeleswaram");
        list.add("Palakollu");
        list.add("Rajamahendravaram");
        list.add("Kakinada");
        list.add("Anantapur");
        list.add("Bathalapalli");
        list.add("Guntakal");
        list.add("Hindupur");
        list.add("Kadiri");
        list.add("Puttaparthy");
        list.add("Chittoor");
        list.add("Kuppam");
        list.add("Madanapalli");
        list.add("Tirumala");
        list.add("Tirupati");
        list.add("Guntur");
        list.add("Narasaraopeta");
        list.add("Piduguralla");
        list.add("Repalle");
        list.add("Tenali");
        list.add("Gudivada");
        list.add("Machilipatnam");
        list.add("Vijayawada");
        list.add("Adoni ");
        list.add("Kurnool ");
        list.add("Nandyal ");
        list.add("Chirala ");
        list.add("Markapur ");
        list.add("Ongole ");
        list.add("Gudur");
        list.add("Kavali");
        list.add("Nellore");
        list.add("Ragolu");
        list.add("Rajam");
        list.add("Sangivalasa");
        list.add("Parvathipuram");
        list.add("Eluru");
        list.add("Jangareddygudem");
        list.add("Narasapuram");
        list.add("Tanuku");
        list.add("PRODDATUR");
        list.add("Pulivendula");
        list.add("srikakulam");
        list.add("vijayanagaram");
        list.add("Bhimavaram");

//        Collections.sort(list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PatientDashBoard.this,R.layout.support_simple_spinner_dropdown_item,list){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.YELLOW);
                }
                else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);



         // line0 = (TextView)findViewById(R.id.l1_animation);

        line1 = (LinearLayout)findViewById(R.id.l2_animation);
        line2 =(LinearLayout)findViewById(R.id.animation);
        line3 =(LinearLayout)findViewById(R.id.l4_animation);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
//        line0.setAnimation(uptodown);
        line1.setAnimation(downtoup);
        line2.setAnimation(downtoup);
        line3.setAnimation(downtoup);

//        final MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.sound);



        CardView btnOpen = (CardView)findViewById(R.id.doc_cardView);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mediaPlayer.start();
                Intent in = new Intent(PatientDashBoard.this,GetCurrentDoctorsList.class);
                in.putExtra("userId",getUserId);
                in.putExtra("mobile",mobile_number);
                startActivity(in);
            }
        });

        CardView diag = (CardView)findViewById(R.id.diag_cardView);
        diag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mediaPlayer.start();
                Intent in = new Intent(PatientDashBoard.this,GetCurrentDiagnosticsList.class);
                in.putExtra("userId",getUserId);
                in.putExtra("mobile",mobile_number);
                startActivity(in);
            }
        });

        CardView medical = (CardView)findViewById(R.id.medical_cardView);
        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mediaPlayer.start();
                Intent in = new Intent(PatientDashBoard.this,GetCurrentMedicalShopsList.class);
                in.putExtra("userId",getUserId);
                in.putExtra("mobile",mobile_number);
                startActivity(in);
            }
        });

        CardView bloodb = (CardView)findViewById(R.id.bloodbank);
        bloodb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mediaPlayer.start();
                Intent in = new Intent(PatientDashBoard.this,BloodBank.class);
                in.putExtra("userId",getUserId);
                in.putExtra("mobile",mobile_number);
                startActivity(in);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        setTitle("Dashboard");


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    private void getLocation() {
        System.out.print("helo this is method");
        if (ActivityCompat.checkSelfPermission(PatientDashBoard.this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (PatientDashBoard.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.print("helo this is if");

            ActivityCompat.requestPermissions(PatientDashBoard.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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


    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            PatientDashBoard.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(viewPager.getCurrentItem() == 0){
                        viewPager.setCurrentItem(1);
                    } else if(viewPager.getCurrentItem() == 1){
                        viewPager.setCurrentItem(2);
                    } else if(viewPager.getCurrentItem() == 2) {
                        viewPager.setCurrentItem(3);
                    } else if(viewPager.getCurrentItem() == 3) {
                        viewPager.setCurrentItem(4);

                    } else if(viewPager.getCurrentItem() == 4) {
                         viewPager.setCurrentItem(5);
                    } else if(viewPager.getCurrentItem() == 5) {
                        viewPager.setCurrentItem(6);
                    } else if(viewPager.getCurrentItem() == 6) {
                        viewPager.setCurrentItem(0);
                    }

                }
            });

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_EditProfile) {
//            Intent i = new Intent(PatientDashBoard.this,EditProfile.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.goup, R.anim.godown);
//            return true;
//        }
//        if(id == R.id.Language_options) {
//            Intent i1 = new Intent(PatientDashBoard.this,Language_Optional.class);


//            startActivity(i1);
//            overridePendingTransition(R.anim.goup, R.anim.godown);
//            return true;
//        }
//        if(id == R.id.Logout) {
//           Intent i1 = new Intent(PatientDashBoard.this,Login.class);
//           startActivity(i1);
//            overridePendingTransition(R.anim.goup, R.anim.godown);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
////            Intent in = new Intent(PatientDashBoard.this,Main4Activity.class);
////            startActivity(in);
//            //Set the fragment initially
////            Dashboard_Fragment fragment = new Dashboard_Fragment();
////            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
////            manager.beginTransaction().replace(R.id.MainLayout,fragment)
////                                       .commit();
//
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
////            Doctor_Fragment fragment1 = new Doctor_Fragment();
////            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
////            manager.beginTransaction().replace(R.id.MainLayout,fragment1)
////                    .commit();
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        } else if (id == R.id.crowd)  {
////            Intent crowd = new Intent(PatientDashBoard.this,crowdFunding.class);
////            startActivity(crowd);
//        }
//
//        else if (id == R.id.history)  {
////            Intent history = new Intent(PatientDashBoard.this,PatientHistory.class);
////            startActivity(history);
//        }
//
//        else if(id== R.id.Achieve) {
////            Intent aciveive = new Intent(PatientDashBoard.this,WelcomeActivity.class);
////            startActivity(aciveive);
//        } else if(id == R.id.certificate){
//            Intent certificate = new Intent(PatientDashBoard.this, sliderparts.class);
//            startActivity(certificate);
//        }else if(id == R.id.subscription) {
//            Intent subscription = new Intent(PatientDashBoard.this,Subscription_for_Urban.class);
//            startActivity(subscription);
//        }else if (id == R.id.video) {
//            Intent video = new Intent(PatientDashBoard.this,tvvideomedic.class);
//            startActivity(video);
//        } else if (id == R.id.contactus) {
//            Intent contact = new Intent(PatientDashBoard.this , ContactUs.class);
//            startActivity(contact);
//        } else if (id == R.id.about)  {
//            Intent abouts = new Intent(PatientDashBoard.this , AboutUs.class);
//            startActivity(abouts);
//        } else if (id == R.id.reach)  {
//            Intent reachs = new Intent(PatientDashBoard.this,ReachUs.class);
//            startActivity(reachs);
//        }
////        else if(id== R.id.doctor_appointment) {
////            Intent appointment = new Intent(PatientDashBoard.this,AppointmentTimings.class);
////            startActivity(appointment);
////        } else if (id == R.id.doctor_calendar) {
////            Intent calender = new Intent(PatientDashBoard.this,DoctorCalendar.class);
////            startActivity(calender);
////        } else if(id == R.id.dimages) {
////            Intent image =  new Intent(PatientDashBoard.this,DoctorsActvity.class);
////            startActivity(image);
////        }
//        else if(id == R.id.Logout)  {
//            Intent logout =  new Intent(PatientDashBoard.this,Login.class);
//            startActivity(logout);
//        }
//        else if(id == R.id.change_password)  {
//            Intent change =  new Intent(PatientDashBoard.this,ChangePassword.class);
//            change.putExtra("mobile",mobile_number);
//            startActivity(change);
//
//        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
