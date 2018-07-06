package com.example.cool.patient.common;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;

import com.example.cool.patient.R;
import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.diagnostic.AddAddress.DiagnosticAddAddress;
import com.example.cool.patient.diagnostic.DashBoardCalendar.DiagnosticDashboard;
import com.example.cool.patient.diagnostic.DiagnosticEditProfile;
import com.example.cool.patient.diagnostic.DiagnosticSideNavigationExpandableListAdapter;
import com.example.cool.patient.diagnostic.DiagnosticSideNavigationExpandableSubList;
import com.example.cool.patient.diagnostic.ManageAddress.DiagnosticManageAddress;
import com.example.cool.patient.diagnostic.TodaysAppointments.DiagnosticsTodaysAppointments;
import com.example.cool.patient.doctor.AddAddress.DoctorAddAddress;
import com.example.cool.patient.doctor.DashBoardCalendar.DoctorDashboard;
import com.example.cool.patient.doctor.DoctorEditProfile;
import com.example.cool.patient.doctor.DoctorSideNavigatioExpandableSubList;
import com.example.cool.patient.doctor.DoctorSideNavigationExpandableListAdapter;
import com.example.cool.patient.doctor.ManageAddress.DoctorManageAddress;
import com.example.cool.patient.doctor.TodaysAppointments.DoctorTodaysAppointmentsForPatient;
import com.example.cool.patient.medicalShop.AddAddress.MedicalShopAddAddress;
import com.example.cool.patient.medicalShop.ManageAddress.MedicalShopManageAddress;
import com.example.cool.patient.medicalShop.MedicalShopDashboard;
import com.example.cool.patient.medicalShop.MedicalShopEditProfile;
import com.example.cool.patient.medicalShop.MedicalShopSideNavigatioExpandableSubList;
import com.example.cool.patient.medicalShop.MedicalShopSideNavigationExpandableListAdapter;
import com.example.cool.patient.patient.MyDiagnosticAppointments.PatientMyDiagnosticAppointments;
import com.example.cool.patient.patient.MyDoctorAppointments.PatientMyDoctorAppointments;
import com.example.cool.patient.patient.MyFamily;
import com.example.cool.patient.patient.PatientDashBoard;
import com.example.cool.patient.patient.PatientEditProfile;
import com.example.cool.patient.patient.PatientSideNavigationExpandableListAdapter;
import com.example.cool.patient.patient.PatientSideNavigationExpandableSubList;
import com.example.cool.patient.patient.ViewDiagnosticsList.GetCurrentDiagnosticsList;
import com.example.cool.patient.patient.ViewDoctorsList.GetCurrentDoctorsList;
import com.example.cool.patient.subscriptionPlan.SubscriptionPlanAlertDialog;

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
import java.util.Locale;

public class ReachUs extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    ImageView img1, img2, img3;


    static String getUserId,mobile_number,module;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    //patient sidenav fields
    TextView sidenavName,sidenavEmail,sidenavAddress,sidenavMobile,sidenavBloodgroup;
    ProgressDialog progressDialog;

    //doc side nav fields
    ImageView imageView,profileImage;
    TextView docname,docemail,docmobile;
    Bitmap mIcon11;
    ProgressDialog progressDialog1;

    //medical shop side nav fields
    TextView medicalsidenavName,medicalsidenavEmail,medicalsidenavMobile;
    ProgressDialog progressDialog2;

    //diag sidenav fields
    TextView diagsidenavName,diagsidenavEmail,diagsidenavMobile;
    ProgressDialog progressDialog3;

    ApiBaseUrl baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        module = getIntent().getStringExtra("module");

        if(module.equals("patient"))
        {
            setContentView(R.layout.activity_reach_us);
        }
        if(module.equals("doc"))
        {
            setContentView(R.layout.activity_reach_us_doctor);
        }
        if(module.equals("diag"))
        {
            setContentView(R.layout.activity_reach_us_diagnostic);
        }
        if(module.equals("medical"))
        {
            setContentView(R.layout.activity_reach_us_medical);
        }


        baseUrl = new ApiBaseUrl();

        mobile_number = getIntent().getStringExtra("mobile");
        getUserId = getIntent().getStringExtra("id");


        System.out.print("userid in patientactivity....." + getUserId);

        img1 = (ImageView) findViewById(R.id.image1);
        img2 = (ImageView) findViewById(R.id.image2);
        img3 = (ImageView) findViewById(R.id.image3);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Object longitude = 82.2093830;
                Object latitude = 17.0011020;
                String uri = String.format(Locale.ENGLISH, "geo:0,0?q=17.0011020,82.2093830(Medic TFHC, Private Limited)", latitude, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
//                Intent intent=new Intent(ReachUs.this,Location1Activity.class);
//                startActivity(intent);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object longitude = 83.3072587;
                Object latitude = 17.7165192;
                String uri = String.format(Locale.ENGLISH, "geo:0,0?q=17.7165192,83.3072587(Medic TFHC, Private Limited)", latitude, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);

//                Intent intent=new Intent(ReachUs.this,Location2Activity.class);
//                startActivity(intent);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Object longitude = 78.4028431;
                Object latitude = 17.5002275;
                String uri = String.format(Locale.ENGLISH, "geo:0,0?q=17.5002275,78.4028431(Medic TFHC, Private Limited)", latitude, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);

//                Intent intent=new Intent(ReachUs.this,Location3Activity.class);
//                startActivity(intent);
            }
        });


        if(module.equals("patient"))
        {

            new GetPatientDetails().execute(baseUrl.getUrl()+"GetPatientByID"+"?ID="+getUserId);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);
            toolbar.setTitle("Reach Us");

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

//            View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);

            sidenavName = (TextView) navigationView.findViewById(R.id.name);
            sidenavAddress = (TextView) navigationView.findViewById(R.id.address);
            sidenavMobile = (TextView) navigationView.findViewById(R.id.mobile);
            sidenavEmail = (TextView) navigationView.findViewById(R.id.email);
            sidenavBloodgroup = (TextView) navigationView.findViewById(R.id.bloodgroup);


            expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

            expandableListDetail = PatientSideNavigationExpandableSubList.getData();
            expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
            expandableListAdapter = new

                    PatientSideNavigationExpandableListAdapter(this, expandableListTitle, expandableListDetail);
            expandableListView.setAdapter(expandableListAdapter);
            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener()

            {

                @Override
                public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
                }
            });

            expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()

            {
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
                        Intent editProfile = new Intent(ReachUs.this, PatientEditProfile.class);
                        editProfile.putExtra("id", getUserId);
                        editProfile.putExtra("mobile", mobile_number);
                        startActivity(editProfile);

                    }

                    else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM5) {
                        // call some activity here
                        Intent contact = new Intent(ReachUs.this,MyFamily.class);
                        contact.putExtra("id", getUserId);
                        contact.putExtra("mobile", mobile_number);
                        startActivity(contact);

                    } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM6) {
                        // call some activity here

                        Intent contact = new Intent(ReachUs.this,Offers.class);
                        contact.putExtra("id",getUserId);
                        contact.putExtra("mobile",mobile_number);
                        contact.putExtra("module","patient");
                        startActivity(contact);


                    }
                    else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM7) {
                        // call some activity here

                        Intent contact = new Intent(ReachUs.this,ReachUs.class);
                        contact.putExtra("id",getUserId);
                        contact.putExtra("mobile",mobile_number);
                        contact.putExtra("module","patient");
                        startActivity(contact);

                    }
                    else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM8) {
                        // call some activity here

                        Intent contact = new Intent(ReachUs.this,Login.class);
                        startActivity(contact);

                    }

                    return retVal;
                }
            });

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()

            {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {


                    if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM1) {
                        if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_1) {

                            Intent i = new Intent(ReachUs.this, GetCurrentDoctorsList.class);
                            i.putExtra("userId", getUserId);
                            i.putExtra("mobile", mobile_number);
                            startActivity(i);

                        } else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_2) {
                            Intent i = new Intent(ReachUs.this, GetCurrentDiagnosticsList.class);
                            i.putExtra("userId", getUserId);
                            i.putExtra("mobile", mobile_number);
                            startActivity(i);

                        } else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_3) {

                            // call activity here

//                        Intent in = new Intent(PatientDashBoard.this,GetCurrentMedicalShopsList.class);
//                        in.putExtra("userId",getUserId);
//                        in.putExtra("mobile",mobile_number);
//                        startActivity(in);

                        } else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_4) {

                            // call activity here

                        } else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_5) {

                            // call activity here
//                        Intent bloodbank = new Intent(PatientDashBoard.this,BloodBank.class);
//                        startActivity(bloodbank);

                        } else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_6) {

                            // call activity here

                        }

                    } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM3) {

                        if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM3_1) {

                            // call activity here
                            Intent intent = new Intent(ReachUs.this, ChangePassword.class);
                            intent.putExtra("mobile", mobile_number);
                            intent.putExtra("id", getUserId);
                            startActivity(intent);


                        } else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM3_2) {

                            // call activity here

                        }

                    } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM2) {
                        if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_1) {

                            // call activity here

                            Intent intent = new Intent(ReachUs.this, PatientMyDoctorAppointments.class);
                            intent.putExtra("mobile", mobile_number);
                            intent.putExtra("id", getUserId);
                            startActivity(intent);

                        } else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_2) {
                            Intent intent = new Intent(ReachUs.this, PatientMyDiagnosticAppointments.class);
                            intent.putExtra("mobile", mobile_number);
                            intent.putExtra("id", getUserId);
                            startActivity(intent);

                        } else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_3) {

                            // call activity here

                        } else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_4) {

                            // call activity here

                        } else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_5) {

                            // call activity here

                        } else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_6) {

                            // call activity here

                        }
                    }
                    return true;

                }
            });
        }
        else if(module.equals("doc"))
        {
            new GetDoctorDetails().execute(baseUrl.getUrl()+"GetDoctorByID"+"?id="+getUserId);


            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle("Reach Us");

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);


//            View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_doctor_dashboard);

            docname = (TextView) navigationView.findViewById(R.id.name);
            docemail = (TextView) navigationView.findViewById(R.id.emailId);
            docmobile = (TextView) navigationView.findViewById(R.id.mobile);
            profileImage = (ImageView) navigationView.findViewById(R.id.profileImageId);

            //side navigation
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
                        Intent contact = new Intent(ReachUs.this,DoctorEditProfile.class);
                        contact.putExtra("id",getUserId);
                        contact.putExtra("mobile",mobile_number);
                        startActivity(contact);

                    }

                    else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM5) {
                        // call some activity here
                        Intent i = new Intent(ReachUs.this,SubscriptionPlanAlertDialog.class);
                        i.putExtra("id",getUserId);
                        i.putExtra("module","doc");
                        startActivity(i);

                    } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM6) {
                        // call some activity here
                        Intent contact = new Intent(ReachUs.this,Offers.class);
                        contact.putExtra("id",getUserId);
                        contact.putExtra("mobile",mobile_number);
                        contact.putExtra("module","doc");
                        startActivity(contact);

                    } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM7) {
                        // call some activity here

                        Intent i = new Intent(ReachUs.this,ReachUs.class);
                        i.putExtra("id",getUserId);
                        i.putExtra("mobile",mobile_number);
                        i.putExtra("module","doc");
                        startActivity(i);

                    }

                    else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM8) {
                        // call some activity here
                        Intent contact = new Intent(ReachUs.this,Login.class);
                        startActivity(contact);

                    }

                    return retVal;
                }
            });

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

                            Intent i = new Intent(ReachUs.this,DoctorDashboard.class);
                            i.putExtra("id",getUserId);
                            i.putExtra("mobile",mobile_number);
                            startActivity(i);


                        }
                        else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM1_2) {

                            // call activity here

                            Intent i = new Intent(ReachUs.this,DoctorTodaysAppointmentsForPatient.class);
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

                            Intent about = new Intent(ReachUs.this,ChangePassword.class);
                            about.putExtra("id",getUserId);
                            about.putExtra("mobile",mobile_number);
                            startActivity(about);

                        }
                        else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM3_2) {

                            // call activity here

                        }

                    } else if(groupPosition == DoctorSideNavigationExpandableListAdapter.Address) {
                        if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_1) {


                            Intent about = new Intent(ReachUs.this,DoctorAddAddress.class);
                            about.putExtra("id",getUserId);
                            about.putExtra("mobile",mobile_number);
                            startActivity(about);

                        }
                        else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_2) {
                            Intent about = new Intent(ReachUs.this,DoctorManageAddress.class);
                            about.putExtra("id",getUserId);
                            about.putExtra("mobile",mobile_number);
                            startActivity(about);

                        }

                    }
                    return true;

                }
            });
        }

        else if(module.equals("medical"))
        {
            new GetMedicalDetails().execute(baseUrl.getUrl()+"MedicalShopByID"+"?id="+getUserId);

            //side navigation
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle("Reach Us");

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

//            View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_medical_shop_dashboard);

            medicalsidenavName = (TextView) navigationView.findViewById(R.id.name);
            medicalsidenavEmail = (TextView) navigationView.findViewById(R.id.emailId);
            medicalsidenavMobile  = (TextView) navigationView.findViewById(R.id.mobile);
//            adharimage = (ImageView) headerLayout.findViewById(R.id.profileImageId);


            expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
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
                        Intent contact = new Intent(ReachUs.this,MedicalShopEditProfile.class);
                        contact.putExtra("id",getUserId);
                        contact.putExtra("mobile",mobile_number);
                        startActivity(contact);

                    }

                    else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM5) {
                        // call some activity here
//                    Intent about = new Intent(MedicalShopDashboard.this,SubscriptionPlanAlertDialog.class);
//                    startActivity(about);

                    } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM6) {
                        // call some activity here
                        Intent contact = new Intent(ReachUs.this,Offers.class);
                        contact.putExtra("id",getUserId);
                        contact.putExtra("mobile",mobile_number);
                        contact.putExtra("module","medical");
                        startActivity(contact);

                    } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM7) {
                        // call some activity here

                        Intent contact = new Intent(ReachUs.this,ReachUs.class);
                        contact.putExtra("id",getUserId);
                        contact.putExtra("mobile",mobile_number);
                        contact.putExtra("module","medical");
                        startActivity(contact);

                    }

                    else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM8) {
                        // call some activity here
                        Intent contact = new Intent(ReachUs.this,Login.class);
                        startActivity(contact);

                    }

                    return retVal;
                }
            });


            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {


                    if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM3) {

                        if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM3_1) {

                            // call activity here

                            Intent about = new Intent(ReachUs.this,ChangePassword.class);
                            about.putExtra("id",getUserId);
                            about.putExtra("mobile",mobile_number);
                            startActivity(about);

                        }
                        else if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM3_2) {

                            // call activity here

                        }

                    } else if(groupPosition == MedicalShopSideNavigationExpandableListAdapter.Address) {
                        if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM2_1) {

                            Intent about = new Intent(ReachUs.this,MedicalShopAddAddress.class);
                            about.putExtra("id",getUserId);
                            about.putExtra("mobile",mobile_number);
                            startActivity(about);

                        }
                        else if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM2_2) {
                            Intent about = new Intent(ReachUs.this,MedicalShopManageAddress.class);
                            about.putExtra("id",getUserId);
                            about.putExtra("mobile",mobile_number);
                            startActivity(about);

                        }

                    }
                    return true;

                }
            });
        }

        else if(module.equals("diag"))
        {

            new GetDiagnosticDetails().execute(baseUrl.getUrl()+"DiagnosticByID"+"?id="+getUserId);

            // side navigation

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle("Reach Us");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

//            View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_diagnostic_dashboard);

            diagsidenavName = (TextView) navigationView.findViewById(R.id.name);
            diagsidenavEmail = (TextView) navigationView.findViewById(R.id.email);
            diagsidenavMobile = (TextView) navigationView.findViewById(R.id.mobile);


            expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
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
                        Intent contact = new Intent(ReachUs.this,DiagnosticEditProfile.class);
                        contact.putExtra("id",getUserId);
                        contact.putExtra("mobile",mobile_number);
                        startActivity(contact);

                    }

                    else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM5) {
                        // call some activity here
                        Intent subscript = new Intent(ReachUs.this,SubscriptionPlanAlertDialog.class);
                        subscript.putExtra("id",getUserId);
                        subscript.putExtra("module","diag");
                        startActivity(subscript);

                    } else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM6) {
                        // call some activity here
                        Intent contact = new Intent(ReachUs.this,Offers.class);
                        contact.putExtra("id",getUserId);
                        contact.putExtra("mobile",mobile_number);
                        contact.putExtra("module","diag");
                        startActivity(contact);

                    } else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM7) {
                        // call some activity here

                        Intent contact = new Intent(ReachUs.this,ReachUs.class);
                        contact.putExtra("id",getUserId);
                        contact.putExtra("mobile",mobile_number);
                        contact.putExtra("module","diag");
                        startActivity(contact);

                    }

                    else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM8) {
                        // call some activity here
                        Intent contact = new Intent(ReachUs.this,Login.class);
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

                            Intent i = new Intent(ReachUs.this,DiagnosticDashboard.class);
                            i.putExtra("id",getUserId);
                            i.putExtra("mobile",mobile_number);
                            startActivity(i);

                        }
                        else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM1_2) {

                            // call activity here

                            Intent i = new Intent(ReachUs.this,DiagnosticsTodaysAppointments.class);
                            i.putExtra("userId",getUserId);
                            i.putExtra("mobile",mobile_number);
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

                            Intent about = new Intent(ReachUs.this,ChangePassword.class);
                            about.putExtra("id",getUserId);
                            about.putExtra("mobile",mobile_number);
                            startActivity(about);

                        }
                        else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM3_2) {

                            // call activity here

                        }

                    } else if(groupPosition == DiagnosticSideNavigationExpandableListAdapter.Address) {
                        if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM2_1) {


                            Intent about = new Intent(ReachUs.this,DiagnosticAddAddress.class);
                            about.putExtra("id",getUserId);
                            about.putExtra("mobile",mobile_number);
                            startActivity(about);

                        }
                        else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM2_2) {
                            Intent about = new Intent(ReachUs.this,DiagnosticManageAddress.class);
                            about.putExtra("id",getUserId);
                            about.putExtra("mobile",mobile_number);
                            startActivity(about);

                        }

                    }
                    return true;

                }
            });
        }

    }

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
            if(module.equals("patient"))
            {
                Intent intent = new Intent(ReachUs.this,PatientDashBoard.class);
                intent.putExtra("id",getUserId);
                intent.putExtra("mobile",mobile_number);
                startActivity(intent);
            }
            if(module.equals("doc"))
            {
                Intent intent = new Intent(ReachUs.this,DoctorDashboard.class);
                intent.putExtra("id",getUserId);
                intent.putExtra("mobile",mobile_number);
                startActivity(intent);
            }
            if(module.equals("diag"))
            {
                Intent intent = new Intent(ReachUs.this,DiagnosticDashboard.class);
                intent.putExtra("id",getUserId);
                intent.putExtra("mobile",mobile_number);
                startActivity(intent);
            }
            if(module.equals("medical"))
            {
                Intent intent = new Intent(ReachUs.this,MedicalShopDashboard.class);
                intent.putExtra("id",getUserId);
                intent.putExtra("mobile",mobile_number);
                startActivity(intent);
            }

        }

        return super.onOptionsItemSelected(item);
    }




    //Get patient list based on id from api call
    private class GetPatientDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(ReachUs.this);
            // Set progressdialog title
//            progressDialog.setTitle("You are logging");
            // Set progressdialog message
            progressDialog.setMessage("Loading..");

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
            getProfileDetails(result);

        }
    }

    private void getProfileDetails(String result) {

        try
        {
            JSONObject js = new JSONObject(result);

            String myName = (String) js.get("FirstName");
            String mySurname = (String) js.get("LastName");
            String myMobile = (String) js.get("MobileNumber");
            String myAddress1 = (String) js.get("Address1");
            String myAddress2 = (String) js.get("Address2");
            String myBlood_group = (String) js.get("BloodGroup");

//                TextView sidenavName,sidenavEmail,sidenavAddress,sidenavMobile;

            sidenavName.setText(mySurname+" "+myName);
            sidenavMobile.setText(myMobile);
            String myEmail = (String) js.get("EmailID");
            sidenavEmail.setText(myEmail);
            sidenavBloodgroup.setText(myBlood_group);


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }


    //get doctor details based on id from api call
    private class GetDoctorDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog1 = new ProgressDialog(ReachUs.this);
            // Set progressdialog title
//            progressDialog.setTitle("You are logging");
            // Set progressdialog message
            progressDialog1.setMessage("Loading..");

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
            progressDialog1.dismiss();
            getDocProfileDetails(result);
        }

    }

    private void getDocProfileDetails(String result) {
        try
        {
            JSONObject js = new JSONObject(result);

            String myEmail = (String) js.get("EmailID");
            String myFirstName = (String) js.get("FirstName");
            String myLastName = (String) js.get("LastName");
            String mydoctorImage = (String) js.get("DoctorImage");
            String myMobile = (String) js.get("MobileNumber");

            System.out.println("name.."+myFirstName+".."+myLastName+".."+myEmail+".."+mydoctorImage);

            docname.setText(myFirstName+" "+myLastName);
            docemail.setText(myEmail);
            docmobile.setText(myMobile);

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


    //get medical details based on id from api call

    private class GetMedicalDetails extends AsyncTask<String, Void, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog2 = new ProgressDialog(ReachUs.this);
            // Set progressdialog title
//            progressDialog.setTitle("Your searching process is");
            // Set progressdialog message
            progressDialog2.setMessage("Loading...");

            progressDialog2.setIndeterminate(false);
            // Show progressdialog
            progressDialog2.show();
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

            Log.e("TAG result medicprofile", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            progressDialog2.dismiss();
            getMedicalProfileDetails(result);
        }

    }

    private void getMedicalProfileDetails(String result) {
        try
        {
            JSONObject js = new JSONObject(result);


            String myMobile = (String) js.get("MobileNumber");
            String myEmail = (String) js.get("EmailID");
            String myName = (String) js.get("FirstName");
            String mySurname = (String) js.get("LastName");


            sidenavName.setText(myName+" "+mySurname);
            sidenavEmail.setText(myEmail);
            sidenavMobile.setText(myMobile);


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }


    //    new GetDiagnosticDetails().execute(baseUrl.getUrl()+"DiagnosticByID"+"?id="+getUserId);

    //get diagnostic details based on id from api call
    private class GetDiagnosticDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog3 = new ProgressDialog(ReachUs.this);
            // Set progressdialog title
//            progressDialog.setTitle("You are logging");
            // Set progressdialog message
            progressDialog3.setMessage("Loading..");

            progressDialog3.setIndeterminate(false);
            // Show progressdialog
            progressDialog3.show();
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
            progressDialog3.dismiss();
            getDiagProfileDetails(result);
        }

    }

    private void getDiagProfileDetails(String result) {
        try
        {
            JSONObject js = new JSONObject(result);


            String myMobile = (String) js.get("MobileNumber");
            String myEmail = (String) js.get("EmailID");
            String myName = (String) js.get("FirstName");
            String mySurname = (String) js.get("LastName");

            diagsidenavName.setText(myName+" "+mySurname);
            diagsidenavMobile.setText(myMobile);
            diagsidenavEmail.setText(myEmail);


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}