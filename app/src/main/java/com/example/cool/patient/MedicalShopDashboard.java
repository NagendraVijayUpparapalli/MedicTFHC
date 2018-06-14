package com.example.cool.patient;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MedicalShopDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
    String city, mobile_number ;

    // expandable list view

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottomnav_home:
                    return true;
//                case R.id.bottomnav_editprofile:
//                    Intent i = new Intent(DiagnosticDashboard.this,DiagnosticEditProfile.class);
//                    i.putExtra("id",getUserId);
//                    startActivity(i);
//                    overridePendingTransition(R.anim.goup, R.anim.godown);
//                    return true;
                case R.id.Languages_options:
//                    Intent language = new Intent (MedicalShopDashboard.this,SelectCity.class);
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
        setContentView(R.layout.activity_medical_shop_dashboard);


        mobile_number = getIntent().getStringExtra("mobile");
        getUserId = getIntent().getStringExtra("id");
        System.out.print("id in medical dashboard....."+getUserId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView1);
        expandableListDetail = MedicalShopSideNavigatioExpandableSubList.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new MedicalShopSideNavigationExpandableListAdapter(this, expandableListTitle, expandableListDetail);
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

                if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.Services) {
                    retVal = false;
                } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.Address) {
                    retVal = false;
                } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM3) {
                    retVal = false;

                }

                else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM4) {
                    // call some activity here
                    Intent contact = new Intent(MedicalShopDashboard.this,MedicalShopEditProfile.class);
                    contact.putExtra("id",getUserId);
                    startActivity(contact);

                }

                else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                    Intent about = new Intent(MedicalShopDashboard.this,SubscriptionPlanAlertDialog.class);
                    startActivity(about);

                } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here
                    Intent contact = new Intent(MedicalShopDashboard.this,AboutUs.class);
                    startActivity(contact);

                } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(MedicalShopDashboard.this,ReachUs.class);
                    startActivity(contact);

                }

                else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here
                    Intent contact = new Intent(MedicalShopDashboard.this,Login.class);
                    startActivity(contact);

                }

                return retVal;
            }
        });


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


//                if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.Services) {
//                    if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM1_1) {
//
//                        Intent i = new Intent(DiagnosticDashboard.this,DiagnosticDashboard.class);
//                        startActivity(i);
//
//                    }
//                    else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM1_2) {
//
//                        // call activity here
//
////                        Intent i = new Intent(DiagnosticDashboard.this,DoctorTodaysAppointmentsForPatient.class);
////                        i.putExtra("userId",getUserId);
////                        startActivity(i);
//
//                    }
////                    else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM1_3) {
////
////                        // call activity here
////
////                    }
//
//
//                }
                 if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM3) {

                    if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM3_1) {

                        // call activity here

                        Intent about = new Intent(MedicalShopDashboard.this,ChangePassword.class);
                        about.putExtra("mobile",mobile_number);
                        startActivity(about);

                    }
                    else if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                } else if(groupPosition == MedicalShopSideNavigationExpandableListAdapter.Address) {
                    if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM2_1) {
                        
                        Intent about = new Intent(MedicalShopDashboard.this,MedicalShopAddAddress.class);
                        about.putExtra("id",getUserId);
                        startActivity(about);

                    }
                    else if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent about = new Intent(MedicalShopDashboard.this,MedicalShopManageAddress.class);
                        about.putExtra("id",getUserId);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.medical_shop_dashboard, menu);
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

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
