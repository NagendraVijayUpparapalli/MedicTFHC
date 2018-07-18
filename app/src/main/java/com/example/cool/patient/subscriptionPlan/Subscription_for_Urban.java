package com.example.cool.patient.subscriptionPlan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.common.Offers;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.diagnostic.AddAddress.DiagnosticAddAddress;
import com.example.cool.patient.diagnostic.DashBoardCalendar.DiagnosticDashboard;
import com.example.cool.patient.diagnostic.DiagnosticChangePassword;
import com.example.cool.patient.diagnostic.DiagnosticEditProfile;
import com.example.cool.patient.diagnostic.DiagnosticSideNavigationExpandableListAdapter;
import com.example.cool.patient.diagnostic.DiagnosticSideNavigationExpandableSubList;
import com.example.cool.patient.diagnostic.ManageAddress.DiagnosticManageAddress;
import com.example.cool.patient.diagnostic.TodaysAppointments.DiagnosticsTodaysAppointments;
import com.example.cool.patient.doctor.AddAddress.DoctorAddAddress;
import com.example.cool.patient.doctor.DashBoardCalendar.DoctorDashboard;
import com.example.cool.patient.R;
import com.example.cool.patient.doctor.DoctorChangePassword;
import com.example.cool.patient.doctor.DoctorEditProfile;
import com.example.cool.patient.doctor.DoctorSideNavigatioExpandableSubList;
import com.example.cool.patient.doctor.DoctorSideNavigationExpandableListAdapter;
import com.example.cool.patient.doctor.ManageAddress.DoctorManageAddress;
import com.example.cool.patient.doctor.TodaysAppointments.DoctorTodaysAppointmentsForPatient;
import com.example.cool.patient.medicalShop.AddAddress.MedicalShopAddAddress;
import com.example.cool.patient.medicalShop.ManageAddress.MedicalShopManageAddress;
import com.example.cool.patient.medicalShop.MedicalChangePassword;
import com.example.cool.patient.medicalShop.MedicalShopDashboard;
import com.example.cool.patient.medicalShop.MedicalShopEditProfile;
import com.example.cool.patient.medicalShop.MedicalShopSideNavigatioExpandableSubList;
import com.example.cool.patient.medicalShop.MedicalShopSideNavigationExpandableListAdapter;

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

import org.json.JSONException;
import org.json.JSONObject;


public class Subscription_for_Urban extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Dialog MyDialog;
    Button pay;
    Button cancel;
    String doctorName,diagName,subscriptionType,moduleName,moduleId,moduleMobile,paymentMode,planType,amount;
    ApiBaseUrl baseUrl;

    RadioGroup paymentRadioGroup;
    RadioButton paymentradioButton;
    Button silverButton,goldButton,platiniumButton;
    RadioButton cash,debit,paytm,net;

    //---side navigation fields---
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

    //---side navigation fields---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        moduleName = getIntent().getStringExtra("module");

        if(moduleName.equals("doc"))
        {
            setContentView(R.layout.activity_subscription_urban_in_doctor);
        }
        if(moduleName.equals("diag"))
        {
            setContentView(R.layout.activity_subscription_urban_in_diagnostic);
        }
        if(moduleName.equals("medical"))
        {
            setContentView(R.layout.activity_subscription_urban_in_medical);
        }

        baseUrl = new ApiBaseUrl();
        silverButton = (Button) findViewById(R.id.silver);
        goldButton = (Button) findViewById(R.id.gold);
        platiniumButton = (Button) findViewById(R.id.platinium);

        silverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                planType = "SILVER";
                amount = "9000";
                MyCustomAlertDialog();
            }
        });

        goldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planType = "GOLD";
                amount = "12000";
                MyCustomAlertDialog();
            }
        });

        platiniumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planType = "PLATINUM";
                amount = "15000";
                MyCustomAlertDialog();
            }
        });

        moduleName = getIntent().getStringExtra("module");

        if(moduleName.equals("doc"))
        {

            subscriptionType = getIntent().getStringExtra("subscriptionType");
            doctorName = getIntent().getStringExtra("docName");
            moduleId = getIntent().getStringExtra("id");
            moduleMobile = getIntent().getStringExtra("mobile");

            System.out.println("module id from doc...."+moduleId);
            System.out.println("module name...."+moduleName);
            System.out.println("doc name...."+doctorName);
            System.out.println("subscriptionType in urban....."+subscriptionType);
        }

        else if(moduleName.equals("diag"))
        {
            subscriptionType = getIntent().getStringExtra("subscriptionType");
            moduleName = getIntent().getStringExtra("diagName");
            moduleId = getIntent().getStringExtra("id");
            moduleMobile = getIntent().getStringExtra("mobile");

            System.out.println("module id from diag...."+moduleId);
            System.out.println("module name....."+moduleName);
            System.out.print("subscriptionType in urban diag....."+subscriptionType);
        }
        else if(moduleName.equals("medical"))
        {
            subscriptionType = getIntent().getStringExtra("subscriptionType");
//            moduleName = getIntent().getStringExtra("diagName");
            moduleId = getIntent().getStringExtra("id");
            moduleMobile = getIntent().getStringExtra("mobile");

            System.out.println("module id from diag...."+moduleId);
            System.out.println("module name....."+moduleName);
            System.out.print("subscriptionType in urban diag....."+subscriptionType);
        }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
//        setTitle(" For URBAN");
//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if(moduleName.equals("doc"))
//                        {
//                            Intent intent = new Intent(Subscription_for_Urban.this,DoctorDashboard.class);
//                            intent.putExtra("id",moduleId);
//                            intent.putExtra("mobile",moduleMobile);
//                            startActivity(intent);
//                        }
//                        else if(moduleName.equals("diag"))
//                        {
//                            Intent intent = new Intent(Subscription_for_Urban.this,DiagnosticDashboard.class);
//                            intent.putExtra("id",moduleId);
//                            intent.putExtra("mobile",moduleMobile);
//                            startActivity(intent);
//                        }
//                        else if(moduleName.equals("medical"))
//                        {
//                            Intent intent = new Intent(Subscription_for_Urban.this,MedicalShopDashboard.class);
//                            intent.putExtra("id",moduleId);
//                            intent.putExtra("mobile",moduleMobile);
//                            startActivity(intent);
//                        }
//
//                    }
//                }
//        );


        if(moduleName.equals("doc"))
        {
            new GetDoctorDetails().execute(baseUrl.getUrl()+"GetDoctorByID"+"?id="+moduleId);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle("For URBAN");

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
                        Intent contact = new Intent(Subscription_for_Urban.this,DoctorEditProfile.class);
                        contact.putExtra("id",moduleId);
                        contact.putExtra("mobile",moduleMobile);
                        contact.putExtra("user","old");
                        startActivity(contact);

                    }

                    else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM5) {
                        // call some activity here
                        Intent i = new Intent(Subscription_for_Urban.this,SubscriptionPlanAlertDialog.class);
                        i.putExtra("id",moduleId);
                        i.putExtra("mobile",moduleMobile);
                        i.putExtra("module","doc");
                        startActivity(i);

                    } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM6) {
                        // call some activity here
                        Intent contact = new Intent(Subscription_for_Urban.this,Offers.class);
                        contact.putExtra("id",moduleId);
                        contact.putExtra("mobile",moduleMobile);
                        contact.putExtra("module","doc");
                        startActivity(contact);

                    } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM7) {
                        // call some activity here

                        Intent i = new Intent(Subscription_for_Urban.this,ReachUs.class);
                        i.putExtra("id",moduleId);
                        i.putExtra("mobile",moduleMobile);
                        i.putExtra("module","doc");
                        startActivity(i);

                    }

                    else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM8) {
                        // call some activity here
                        Intent contact = new Intent(Subscription_for_Urban.this,Login.class);
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

                            Intent i = new Intent(Subscription_for_Urban.this,DoctorDashboard.class);
                            i.putExtra("id",moduleId);
                            i.putExtra("mobile",moduleMobile);
                            startActivity(i);
                        }
                        else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM1_2) {

                            // call activity here

                            Intent i = new Intent(Subscription_for_Urban.this,DoctorTodaysAppointmentsForPatient.class);
                            i.putExtra("id",moduleId);
                            i.putExtra("mobile",moduleMobile);
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

                            Intent about = new Intent(Subscription_for_Urban.this,DoctorChangePassword.class);
                            about.putExtra("id",moduleId);
                            about.putExtra("mobile",moduleMobile);
                            startActivity(about);

                        }
                        else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM3_2) {

                            // call activity here

                        }

                    } else if(groupPosition == DoctorSideNavigationExpandableListAdapter.Address) {
                        if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_1) {


                            Intent about = new Intent(Subscription_for_Urban.this,DoctorAddAddress.class);
                            about.putExtra("id",moduleId);
                            about.putExtra("mobile",moduleMobile);
                            startActivity(about);

                        }
                        else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_2) {
                            Intent about = new Intent(Subscription_for_Urban.this,DoctorManageAddress.class);
                            about.putExtra("id",moduleId);
                            about.putExtra("mobile",moduleMobile);
                            startActivity(about);

                        }

                    }
                    return true;

                }
            });
        }

        else if(moduleName.equals("medical"))
        {
            new GetMedicalDetails().execute(baseUrl.getUrl()+"MedicalShopByID"+"?id="+moduleId);

            //side navigation
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle("For URBAN");

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

//            View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_medical_shop_dashboard);

            medicalsidenavName = (TextView) navigationView.findViewById(R.id.name1);
            medicalsidenavEmail = (TextView) navigationView.findViewById(R.id.emailId1);
            medicalsidenavMobile  = (TextView) navigationView.findViewById(R.id.mobile1);
//            adharimage = (ImageView) headerLayout.findViewById(R.id.profileImageId);


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
                        Intent contact = new Intent(Subscription_for_Urban.this,MedicalShopEditProfile.class);
                        contact.putExtra("id",moduleId);
                        contact.putExtra("mobile",moduleMobile);
                        contact.putExtra("user","old");
                        startActivity(contact);

                    }

                    else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM5) {
                        // call some activity here
                        Intent contact = new Intent(Subscription_for_Urban.this,SubscriptionPlanAlertDialog.class);
                        contact.putExtra("id",moduleId);
                        contact.putExtra("mobile",moduleMobile);
                        contact.putExtra("module","medical");
                        startActivity(contact);

                    } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM6) {
                        // call some activity here
                        Intent contact = new Intent(Subscription_for_Urban.this,Offers.class);
                        contact.putExtra("id",moduleId);
                        contact.putExtra("mobile",moduleMobile);
                        contact.putExtra("module","medical");
                        startActivity(contact);

                    } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM7) {
                        // call some activity here

                        Intent contact = new Intent(Subscription_for_Urban.this,ReachUs.class);
                        contact.putExtra("id",moduleId);
                        contact.putExtra("mobile",moduleMobile);
                        contact.putExtra("module","medical");
                        startActivity(contact);

                    }

                    else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM8) {
                        // call some activity here
                        Intent contact = new Intent(Subscription_for_Urban.this,Login.class);
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

                            Intent about = new Intent(Subscription_for_Urban.this,MedicalChangePassword.class);
                            about.putExtra("id",moduleId);
                            about.putExtra("mobile",moduleMobile);
                            startActivity(about);

                        }
                        else if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM3_2) {

                            // call activity here

                        }

                    } else if(groupPosition == MedicalShopSideNavigationExpandableListAdapter.Address) {
                        if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM2_1) {

                            Intent about = new Intent(Subscription_for_Urban.this,MedicalShopAddAddress.class);
                            about.putExtra("id",moduleId);
                            about.putExtra("mobile",moduleMobile);
                            startActivity(about);

                        }
                        else if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM2_2) {
                            Intent about = new Intent(Subscription_for_Urban.this,MedicalShopManageAddress.class);
                            about.putExtra("id",moduleId);
                            about.putExtra("mobile",moduleMobile);
                            startActivity(about);

                        }

                    }
                    return true;

                }
            });
        }

        else if(moduleName.equals("diag"))
        {

            new GetDiagnosticDetails().execute(baseUrl.getUrl()+"DiagnosticByID"+"?id="+moduleId);

            // side navigation

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle("For URBAN");

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

//            View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_diagnostic_dashboard);

            diagsidenavName = (TextView) navigationView.findViewById(R.id.name1);
            diagsidenavEmail = (TextView) navigationView.findViewById(R.id.email1);
            diagsidenavMobile = (TextView) navigationView.findViewById(R.id.mobile1);


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
                        Intent contact = new Intent(Subscription_for_Urban.this,DiagnosticEditProfile.class);
                        contact.putExtra("id",moduleId);
                        contact.putExtra("mobile",moduleMobile);
                        contact.putExtra("user","old");
                        startActivity(contact);

                    }

                    else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM5) {
                        // call some activity here
                        Intent subscript = new Intent(Subscription_for_Urban.this,SubscriptionPlanAlertDialog.class);
                        subscript.putExtra("id",moduleId);
                        subscript.putExtra("mobile",moduleMobile);
                        subscript.putExtra("module","diag");
                        startActivity(subscript);

                    } else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM6) {
                        // call some activity here
                        Intent contact = new Intent(Subscription_for_Urban.this,Offers.class);
                        contact.putExtra("id",moduleId);
                        contact.putExtra("mobile",moduleMobile);
                        contact.putExtra("module","diag");
                        startActivity(contact);

                    } else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM7) {
                        // call some activity here

                        Intent contact = new Intent(Subscription_for_Urban.this,ReachUs.class);
                        contact.putExtra("id",moduleId);
                        contact.putExtra("mobile",moduleMobile);
                        contact.putExtra("module","diag");
                        startActivity(contact);

                    }

                    else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM8) {
                        // call some activity here
                        Intent contact = new Intent(Subscription_for_Urban.this,Login.class);
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

                            Intent i = new Intent(Subscription_for_Urban.this,DiagnosticDashboard.class);
                            i.putExtra("id",moduleId);
                            i.putExtra("mobile",moduleMobile);
                            startActivity(i);

                        }
                        else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM1_2) {

                            // call activity here

                            Intent i = new Intent(Subscription_for_Urban.this,DiagnosticsTodaysAppointments.class);
                            i.putExtra("userId",moduleId);
                            i.putExtra("mobile",moduleMobile);
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

                            Intent about = new Intent(Subscription_for_Urban.this,DiagnosticChangePassword.class);
                            about.putExtra("id",moduleId);
                            about.putExtra("mobile",moduleMobile);
                            startActivity(about);

                        }
                        else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM3_2) {

                            // call activity here

                        }

                    } else if(groupPosition == DiagnosticSideNavigationExpandableListAdapter.Address) {
                        if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM2_1) {


                            Intent about = new Intent(Subscription_for_Urban.this,DiagnosticAddAddress.class);
                            about.putExtra("id",moduleId);
                            about.putExtra("mobile",moduleMobile);
                            startActivity(about);

                        }
                        else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM2_2) {
                            Intent about = new Intent(Subscription_for_Urban.this,DiagnosticManageAddress.class);
                            about.putExtra("id",moduleId);
                            about.putExtra("mobile",moduleMobile);
                            startActivity(about);

                        }

                    }
                    return true;

                }
            });
        }


    }

    public void MyCustomAlertDialog(){
        MyDialog =  new Dialog(Subscription_for_Urban.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.subscription_paymentmode_alert);

        pay = (Button)MyDialog.findViewById(R.id.Pay);
        cancel = (Button)MyDialog.findViewById(R.id.Cancel);

         cash = (RadioButton) MyDialog.findViewById(R.id.cash_on_hand);
         debit = (RadioButton) MyDialog.findViewById(R.id.credit_debit);
         paytm = (RadioButton) MyDialog.findViewById(R.id.paytm);
         net = (RadioButton) MyDialog.findViewById(R.id.net_banking);


        pay.setEnabled(true);
        cancel.setEnabled(true);

//        pay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            if(cash.isChecked())
//            {
//                paymentMode = "Cash On Hand";
//            }
//            if(paytm.isChecked())
//            {
//                paymentMode = "Pay Using Paytm";
//            }
//                if(debit.isChecked())
//                {
//                    paymentMode = "Debit Card";
//                }
//            if(net.isChecked())
//            {
//                paymentMode = "Net Banking";
//            }
//                String js = formatDataAsJson();
//                System.out.println("subscript json data...."+js.toString());
//
//                new sendSubscriptionDetails().execute(baseUrl.getUrl()+"Subscription",js.toString());
//            }
//        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });
//
//        final CheckBox checkBox = (CheckBox)findViewById(R.id.cash_on_hand);
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                checkBox.setVisibility(View.GONE);//TO HIDE THE BUTTON
//                checkBox.setVisibility(View.VISIBLE);//TO SHOW THE BUTTON
//            }
//        });

        MyDialog.setCancelable(false);
        MyDialog.setCanceledOnTouchOutside(false);
        MyDialog.show();


    }



    private String formatDataAsJson()
    {

        JSONObject data = new JSONObject();

        System.out.println("plan type.."+planType);
        System.out.println("amount.."+amount);
        System.out.println("payment mode.."+paymentMode);

        try{


            if(moduleName.equals("doc"))
            {
                data.put("PaymentMode",paymentMode);
                data.put("SubscriptionType",subscriptionType);
                data.put("PlanType",planType);
                data.put("UserID",moduleId);
                data.put("Amount",amount);
                data.put("DocName",doctorName);
                return data.toString();
            }
            else if(moduleName.equals("diag"))
            {
                data.put("PaymentMode",paymentMode);
                data.put("SubscriptionType",subscriptionType);
                data.put("PlanType",planType);
                data.put("UserID",moduleId);
                data.put("Amount",amount);
                data.put("DiagnosticName",diagName);
                return data.toString();
            }

        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    //send subscription details to api
    private class sendSubscriptionDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

//            HttpURLConnection connection=null;
            HttpURLConnection httpURLConnection = null;
            try {
                System.out.println("dsfafssss....");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setUseCaches(false);
//                httpURLConnection.setChunkedStreamingMode(1024);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
//                httpURLConnection.setRequestProperty("Accept", "application/json");
                Log.d("Service","Started");
//                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                //write
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
            Log.e("TAG result diag profile", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            JSONObject js;

            try {
                js= new JSONObject(result);
                int s = js.getInt("Code");
                if(s == 200)
                {
                    showSuccessMessage(js.getString("Message"));
                }
                else if(s == 404)
                {
                    showErrorMessage(js.getString("Message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    public void showSuccessMessage(String message){

        AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
                        Intent intent = new Intent(Subscription_for_Urban.this,DiagnosticDashboard.class);
                        intent.putExtra("id",moduleId);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Edit Profile");
        alert.show();
    }

    public void showErrorMessage(String message){

        AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Edit Profile");
        alert.show();

    }

    //get doctor details based on id from api call
    private class GetDoctorDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
//            progressDialog1 = new ProgressDialog(ReachUs.this);
//            // Set progressdialog title
////            progressDialog.setTitle("You are logging");
//            // Set progressdialog message
//            progressDialog1.setMessage("Loading..");
//
//            progressDialog1.setIndeterminate(false);
//            // Show progressdialog
//            progressDialog1.show();

            progressDialog1 = new ProgressDialog(Subscription_for_Urban.this);
            progressDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog1.setIndeterminate(true);
            progressDialog1.setCancelable(true);
            progressDialog1.show();
            progressDialog1.setContentView(R.layout.myprogress);
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
//            progressDialog2 = new ProgressDialog(ReachUs.this);
//            // Set progressdialog title
////            progressDialog.setTitle("Your searching process is");
//            // Set progressdialog message
//            progressDialog2.setMessage("Loading...");
//
//            progressDialog2.setIndeterminate(false);
//            // Show progressdialog
//            progressDialog2.show();

            progressDialog2 = new ProgressDialog(Subscription_for_Urban.this);
            progressDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog2.setIndeterminate(true);
            progressDialog2.setCancelable(true);
            progressDialog2.show();
            progressDialog2.setContentView(R.layout.myprogress);
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


            medicalsidenavName.setText(myName+" "+mySurname);
            medicalsidenavEmail.setText(myEmail);
            medicalsidenavMobile.setText(myMobile);


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
//            progressDialog3 = new ProgressDialog(ReachUs.this);
//            // Set progressdialog title
////            progressDialog.setTitle("You are logging");
//            // Set progressdialog message
//            progressDialog3.setMessage("Loading..");
//
//            progressDialog3.setIndeterminate(false);
//            // Show progressdialog
//            progressDialog3.show();

            progressDialog3 = new ProgressDialog(Subscription_for_Urban.this);
            progressDialog3.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog3.setIndeterminate(true);
            progressDialog3.setCancelable(true);
            progressDialog3.show();
            progressDialog3.setContentView(R.layout.myprogress);


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



}
