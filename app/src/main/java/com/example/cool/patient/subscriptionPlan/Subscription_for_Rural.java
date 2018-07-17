package com.example.cool.patient.subscriptionPlan;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.example.cool.patient.common.ChangePassword;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.common.Offers;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.diagnostic.AddAddress.DiagnosticAddAddress;
import com.example.cool.patient.diagnostic.DashBoardCalendar.DiagnosticDashboard;
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
import com.example.cool.patient.medicalShop.MedicalShopDashboard;
import com.example.cool.patient.medicalShop.MedicalShopEditProfile;
import com.example.cool.patient.medicalShop.MedicalShopSideNavigatioExpandableSubList;
import com.example.cool.patient.medicalShop.MedicalShopSideNavigationExpandableListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Subscription_for_Rural extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Dialog MyDialog;
    Button pay;
    Button cancel;
    String doctorName,diagName,subscriptionType,moduleName,moduleId,moduleMobile,paymentMode,planType,amount;
    ApiBaseUrl baseUrl;

    RadioGroup paymentRadioGroup;
    RadioButton paymentradioButton;
    int radiobuttonid;
    Button silverButton,goldButton,platiniumButton;

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
            setContentView(R.layout.activity_subscription_rural_in_doctor);
        }
        if(moduleName.equals("diag"))
        {
            setContentView(R.layout.activity_subscription_rural_in_diagnostic);
        }
        if(moduleName.equals("medical"))
        {
            setContentView(R.layout.activity_subscription_rural_in_medical);
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
            }
        });

        goldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planType = "GOLD";
                amount = "12000";
            }
        });

        platiniumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planType = "PLATINUM";
                amount = "15000";
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
            System.out.println("module type from doc...."+moduleName);
            System.out.println("subscriptionType in urban....."+subscriptionType);
        }

        else if(moduleName.equals("diag"))
        {
            subscriptionType = getIntent().getStringExtra("subscriptionType");
            diagName = getIntent().getStringExtra("diagName");
            moduleId = getIntent().getStringExtra("id");
            moduleMobile = getIntent().getStringExtra("mobile");


            System.out.println("module id from diag...."+moduleId);
            System.out.println("module type from diag....."+moduleName);
            System.out.print("subscriptionType in urban diag....."+subscriptionType);
        }
        else if(moduleName.equals("medicall"))
        {
            subscriptionType = getIntent().getStringExtra("subscriptionType");
//            diagName = getIntent().getStringExtra("diagName");
            moduleId = getIntent().getStringExtra("id");
            moduleMobile = getIntent().getStringExtra("mobile");


            System.out.println("module id from diag...."+moduleId);
            System.out.println("module type from diag....."+moduleName);
            System.out.print("subscriptionType in urban diag....."+subscriptionType);
        }

        Button button = (Button) findViewById(R.id.silver);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyCustomAlertDialog();
            }

        });

        Button gold = (Button) findViewById(R.id.gold);

        gold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyCustomAlertDialog();
            }

        });

        Button platinum = (Button) findViewById(R.id.platinium);

        platinum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyCustomAlertDialog();
            }

        });


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        setTitle("For RURAL");

//        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
//
//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if(moduleName.equals("doc"))
//                        {
//                            Intent intent = new Intent(Subscription_for_Rural.this,DoctorDashboard.class);
//                            intent.putExtra("id",moduleId);
//                            intent.putExtra("mobile",moduleMobile);
//                            startActivity(intent);
//                        }
//                        else if(moduleName.equals("diag"))
//                        {
//                            Intent intent = new Intent(Subscription_for_Rural.this,DiagnosticDashboard.class);
//                            intent.putExtra("id",moduleId);
//                            intent.putExtra("mobile",moduleMobile);
//                            startActivity(intent);
//                        }
//                        else if(moduleName.equals("medical"))
//                        {
//                            Intent intent = new Intent(Subscription_for_Rural.this,MedicalShopDashboard.class);
//                            intent.putExtra("id",moduleId);
//                            intent.putExtra("mobile",moduleMobile);
//                            startActivity(intent);
//                        }
//
//
//                    }
//                }
//
//        );


        if(moduleName.equals("doc"))
        {
//            new GetDoctorDetails().execute(baseUrl.getUrl()+"GetDoctorByID"+"?id="+moduleId);


            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle("For RURAL");

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
                        Intent contact = new Intent(Subscription_for_Rural.this,DoctorEditProfile.class);
                        contact.putExtra("id",moduleId);
                        contact.putExtra("mobile",moduleMobile);
                        contact.putExtra("user","old");
                        startActivity(contact);

                    }

                    else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM5) {
                        // call some activity here
                        Intent i = new Intent(Subscription_for_Rural.this,SubscriptionPlanAlertDialog.class);
                        i.putExtra("id",moduleId);
                        i.putExtra("mobile",moduleMobile);
                        i.putExtra("module","doc");
                        startActivity(i);

                    } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM6) {
                        // call some activity here
                        Intent contact = new Intent(Subscription_for_Rural.this,Offers.class);
                        contact.putExtra("id",moduleId);
                        contact.putExtra("mobile",moduleMobile);
                        contact.putExtra("module","doc");
                        startActivity(contact);

                    } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM7) {
                        // call some activity here

                        Intent i = new Intent(Subscription_for_Rural.this,ReachUs.class);
                        i.putExtra("id",moduleId);
                        i.putExtra("mobile",moduleMobile);
                        i.putExtra("module","doc");
                        startActivity(i);

                    }

                    else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM8) {
                        // call some activity here
                        Intent contact = new Intent(Subscription_for_Rural.this,Login.class);
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

                            Intent i = new Intent(Subscription_for_Rural.this,DoctorDashboard.class);
                            i.putExtra("id",moduleId);
                            i.putExtra("mobile",moduleMobile);
                            startActivity(i);
                        }
                        else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM1_2) {

                            // call activity here

                            Intent i = new Intent(Subscription_for_Rural.this,DoctorTodaysAppointmentsForPatient.class);
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

                            Intent about = new Intent(Subscription_for_Rural.this,DoctorChangePassword.class);
                            about.putExtra("id",moduleId);
                            about.putExtra("mobile",moduleMobile);
                            startActivity(about);

                        }
                        else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM3_2) {

                            // call activity here

                        }

                    } else if(groupPosition == DoctorSideNavigationExpandableListAdapter.Address) {
                        if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_1) {


                            Intent about = new Intent(Subscription_for_Rural.this,DoctorAddAddress.class);
                            about.putExtra("id",moduleId);
                            about.putExtra("mobile",moduleMobile);
                            startActivity(about);

                        }
                        else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_2) {
                            Intent about = new Intent(Subscription_for_Rural.this,DoctorManageAddress.class);
                            about.putExtra("id",moduleId);
                            about.putExtra("mobile",moduleMobile);
                            startActivity(about);

                        }

                    }
                    return true;

                }
            });
        }

//        else if(module.equals("medical"))
//        {
//            new ReachUs.GetMedicalDetails().execute(baseUrl.getUrl()+"MedicalShopByID"+"?id="+getUserId);
//
//            //side navigation
//            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//            setSupportActionBar(toolbar);
//            toolbar.setTitle("For RURAL");
//
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//            drawer.addDrawerListener(toggle);
//            toggle.syncState();
//
//            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//            navigationView.setNavigationItemSelectedListener(this);
//
////            View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_medical_shop_dashboard);
//
//            medicalsidenavName = (TextView) navigationView.findViewById(R.id.name);
//            medicalsidenavEmail = (TextView) navigationView.findViewById(R.id.emailId);
//            medicalsidenavMobile  = (TextView) navigationView.findViewById(R.id.mobile);
////            adharimage = (ImageView) headerLayout.findViewById(R.id.profileImageId);
//
//
//            expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
//            expandableListDetail = MedicalShopSideNavigatioExpandableSubList.getData();
//            expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
//            expandableListAdapter = new MedicalShopSideNavigationExpandableListAdapter(this, expandableListTitle, expandableListDetail);
//            expandableListView.setAdapter(expandableListAdapter);
//            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//
//                @Override
//                public void onGroupExpand(int groupPosition) {
////                Toast.makeText(getApplicationContext(),
////                        expandableListTitle.get(groupPosition) + " List Expanded.",
////                        Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                    boolean retVal = true;
//
//                    if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.Services) {
//                        retVal = false;
//                    } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.Address) {
//                        retVal = false;
//                    } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM3) {
//                        retVal = false;
//
//                    }
//
//                    else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM4) {
//                        // call some activity here
//                        Intent contact = new Intent(ReachUs.this,MedicalShopEditProfile.class);
//                        contact.putExtra("id",getUserId);
//                        contact.putExtra("mobile",mobile_number);
//                        contact.putExtra("user","old");
//                        startActivity(contact);
//
//                    }
//
//                    else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM5) {
//                        // call some activity here
//                        Intent contact = new Intent(ReachUs.this,SubscriptionPlanAlertDialog.class);
//                        contact.putExtra("id",getUserId);
//                        contact.putExtra("mobile",mobile_number);
//                        contact.putExtra("module","medical");
//                        startActivity(contact);
//
//                    } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM6) {
//                        // call some activity here
//                        Intent contact = new Intent(ReachUs.this,Offers.class);
//                        contact.putExtra("id",getUserId);
//                        contact.putExtra("mobile",mobile_number);
//                        contact.putExtra("module","medical");
//                        startActivity(contact);
//
//                    } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM7) {
//                        // call some activity here
//
//                        Intent contact = new Intent(ReachUs.this,ReachUs.class);
//                        contact.putExtra("id",getUserId);
//                        contact.putExtra("mobile",mobile_number);
//                        contact.putExtra("module","medical");
//                        startActivity(contact);
//
//                    }
//
//                    else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM8) {
//                        // call some activity here
//                        Intent contact = new Intent(ReachUs.this,Login.class);
//                        startActivity(contact);
//
//                    }
//
//                    return retVal;
//                }
//            });
//
//
//            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//                @Override
//                public boolean onChildClick(ExpandableListView parent, View v,
//                                            int groupPosition, int childPosition, long id) {
//
//
//                    if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM3) {
//
//                        if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM3_1) {
//
//                            // call activity here
//
//                            Intent about = new Intent(ReachUs.this,ChangePassword.class);
//                            about.putExtra("id",getUserId);
//                            about.putExtra("mobile",mobile_number);
//                            startActivity(about);
//
//                        }
//                        else if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM3_2) {
//
//                            // call activity here
//
//                        }
//
//                    } else if(groupPosition == MedicalShopSideNavigationExpandableListAdapter.Address) {
//                        if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM2_1) {
//
//                            Intent about = new Intent(ReachUs.this,MedicalShopAddAddress.class);
//                            about.putExtra("id",getUserId);
//                            about.putExtra("mobile",mobile_number);
//                            startActivity(about);
//
//                        }
//                        else if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM2_2) {
//                            Intent about = new Intent(ReachUs.this,MedicalShopManageAddress.class);
//                            about.putExtra("id",getUserId);
//                            about.putExtra("mobile",mobile_number);
//                            startActivity(about);
//
//                        }
//
//                    }
//                    return true;
//
//                }
//            });
//        }
//
//        else if(module.equals("diag"))
//        {
//
//            new ReachUs.GetDiagnosticDetails().execute(baseUrl.getUrl()+"DiagnosticByID"+"?id="+getUserId);
//
//            // side navigation
//
//            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//            setSupportActionBar(toolbar);
//            toolbar.setTitle("For RURAL");
//
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//            drawer.addDrawerListener(toggle);
//            toggle.syncState();
//
//            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//            navigationView.setNavigationItemSelectedListener(this);
//
////            View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_diagnostic_dashboard);
//
//            diagsidenavName = (TextView) navigationView.findViewById(R.id.name);
//            diagsidenavEmail = (TextView) navigationView.findViewById(R.id.email);
//            diagsidenavMobile = (TextView) navigationView.findViewById(R.id.mobile);
//
//
//            expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
//            expandableListDetail = DiagnosticSideNavigationExpandableSubList.getData();
//            expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
//            expandableListAdapter = new DiagnosticSideNavigationExpandableListAdapter(this, expandableListTitle, expandableListDetail);
//            expandableListView.setAdapter(expandableListAdapter);
//            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//
//                @Override
//                public void onGroupExpand(int groupPosition) {
////                Toast.makeText(getApplicationContext(),
////                        expandableListTitle.get(groupPosition) + " List Expanded.",
////                        Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                    boolean retVal = true;
//
//                    if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.Services) {
//                        retVal = false;
//                    } else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.Address) {
//                        retVal = false;
//                    } else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM3) {
//                        retVal = false;
//
//                    }
//
//                    else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM4) {
//                        // call some activity here
//                        Intent contact = new Intent(ReachUs.this,DiagnosticEditProfile.class);
//                        contact.putExtra("id",getUserId);
//                        contact.putExtra("mobile",mobile_number);
//                        contact.putExtra("user","old");
//                        startActivity(contact);
//
//                    }
//
//                    else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM5) {
//                        // call some activity here
//                        Intent subscript = new Intent(ReachUs.this,SubscriptionPlanAlertDialog.class);
//                        subscript.putExtra("id",getUserId);
//                        subscript.putExtra("mobile",mobile_number);
//                        subscript.putExtra("module","diag");
//                        startActivity(subscript);
//
//                    } else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM6) {
//                        // call some activity here
//                        Intent contact = new Intent(ReachUs.this,Offers.class);
//                        contact.putExtra("id",getUserId);
//                        contact.putExtra("mobile",mobile_number);
//                        contact.putExtra("module","diag");
//                        startActivity(contact);
//
//                    } else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM7) {
//                        // call some activity here
//
//                        Intent contact = new Intent(ReachUs.this,ReachUs.class);
//                        contact.putExtra("id",getUserId);
//                        contact.putExtra("mobile",mobile_number);
//                        contact.putExtra("module","diag");
//                        startActivity(contact);
//
//                    }
//
//                    else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM8) {
//                        // call some activity here
//                        Intent contact = new Intent(ReachUs.this,Login.class);
//                        startActivity(contact);
//
//                    }
//
//                    return retVal;
//                }
//            });
//
//
//            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//                @Override
//                public boolean onChildClick(ExpandableListView parent, View v,
//                                            int groupPosition, int childPosition, long id) {
//
//
//                    if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.Services) {
//                        if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM1_1) {
//
//                            Intent i = new Intent(ReachUs.this,DiagnosticDashboard.class);
//                            i.putExtra("id",getUserId);
//                            i.putExtra("mobile",mobile_number);
//                            startActivity(i);
//
//                        }
//                        else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM1_2) {
//
//                            // call activity here
//
//                            Intent i = new Intent(ReachUs.this,DiagnosticsTodaysAppointments.class);
//                            i.putExtra("userId",getUserId);
//                            i.putExtra("mobile",mobile_number);
//                            startActivity(i);
//
//                        }
////                    else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM1_3) {
////
////                        // call activity here
////
////                    }
//
//
//                    } else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM3) {
//
//                        if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM3_1) {
//
//                            // call activity here
//
//                            Intent about = new Intent(ReachUs.this,ChangePassword.class);
//                            about.putExtra("id",getUserId);
//                            about.putExtra("mobile",mobile_number);
//                            startActivity(about);
//
//                        }
//                        else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM3_2) {
//
//                            // call activity here
//
//                        }
//
//                    } else if(groupPosition == DiagnosticSideNavigationExpandableListAdapter.Address) {
//                        if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM2_1) {
//
//
//                            Intent about = new Intent(ReachUs.this,DiagnosticAddAddress.class);
//                            about.putExtra("id",getUserId);
//                            about.putExtra("mobile",mobile_number);
//                            startActivity(about);
//
//                        }
//                        else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM2_2) {
//                            Intent about = new Intent(ReachUs.this,DiagnosticManageAddress.class);
//                            about.putExtra("id",getUserId);
//                            about.putExtra("mobile",mobile_number);
//                            startActivity(about);
//
//                        }
//
//                    }
//                    return true;
//
//                }
//            });
//        }


    }


    public void MyCustomAlertDialog(){
        MyDialog =  new Dialog(Subscription_for_Rural.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.subscription_paymentmode_alert);


        // find the radioButton by returned id
        paymentRadioGroup = (RadioGroup) MyDialog.findViewById(R.id.paymentModeType_radioGroup);

        radiobuttonid = paymentRadioGroup.getCheckedRadioButtonId();

        System.out.println("radio...."+radiobuttonid);

        paymentradioButton = (RadioButton) MyDialog.findViewById(radiobuttonid);

        pay.setEnabled(true);
        cancel.setEnabled(true);

//        pay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String js = formatDataAsJson();
//                System.out.println("subscript json data...."+js.toString());
//
////                new sendSubscriptionDetails().execute(baseUrl.getUrl()+"Subscription",js.toString());
//            }
//        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });

        MyDialog.setCancelable(false);
        MyDialog.setCanceledOnTouchOutside(false);
        MyDialog.show();


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

    private String formatDataAsJson()
    {

        JSONObject data = new JSONObject();

        if(paymentradioButton.getText().equals(" Cash On Hand"))
        {
            paymentMode = "Cash On Hand";
        }
        if(paymentradioButton.getText().equals(" Pay Using Paytm"))
        {
            paymentMode = "Pay Using Paytm";
        }
        if(paymentradioButton.getText().equals(" Debit Card"))
        {
            paymentMode = "Debit Card";
        }
        if(paymentradioButton.getText().equals(" Net Banking"))
        {
            paymentMode = "Net Banking";
        }

        try{

            if(moduleName.equals("doc") )
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
                data.put("CenterName",diagName);
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

//            try {
//                js= new JSONObject(result);
//                int s = js.getInt("Code");
//                if(s == 1017)
//                {
//                    showSuccessMessage(js.getString("Message"));
//                }
//                else
//                {
//                    showErrorMessage(js.getString("Message"));
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }


        }
    }
//    public void showSuccessMessage(String message){
//
//        AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
//
//        a_builder.setMessage(message)
//                .setCancelable(false)
//                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        dialog.cancel();
//                        Intent intent = new Intent(Subscription_for_Rural.this,DiagnosticDashboard.class);
//                        intent.putExtra("id",moduleId);
//                        startActivity(intent);
//                    }
//                });
//        AlertDialog alert = a_builder.create();
//        alert.setTitle("Edit Profile");
//        alert.show();
//    }
//
//    public void showErrorMessage(String message){
//
//        AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
//
//        a_builder.setMessage(message)
//                .setCancelable(false)
//                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = a_builder.create();
//        alert.setTitle("Edit Profile");
//        alert.show();
//
//    }


}
