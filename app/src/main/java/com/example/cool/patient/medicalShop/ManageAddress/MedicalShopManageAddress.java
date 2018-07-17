package com.example.cool.patient.medicalShop.ManageAddress;

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
import android.widget.*;
import android.widget.ListView;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.common.ChangePassword;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.common.Offers;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.medicalShop.AddAddress.MedicalShopAddAddress;
import com.example.cool.patient.medicalShop.MedicalChangePassword;
import com.example.cool.patient.medicalShop.MedicalShopDashboard;
import com.example.cool.patient.R;
import com.example.cool.patient.medicalShop.MedicalShopEditProfile;
import com.example.cool.patient.medicalShop.MedicalShopSideNavigatioExpandableSubList;
import com.example.cool.patient.medicalShop.MedicalShopSideNavigationExpandableListAdapter;
import com.example.cool.patient.subscriptionPlan.SubscriptionPlanAlertDialog;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MedicalShopManageAddress extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<MedicalShopManageAddressClass> myList;
    ListView listview;
    MedicalShopManageAddressAdapter adapter;

    //api url
    static String getUserId,regMobile;
    static String uploadServerUrl = null,myProfileImage;
    ApiBaseUrl baseUrl;

    MultiAutoCompleteTextView reason_Todelete;
    String reasonToDelete = null;

    ProgressDialog progressDialog;

    // expandable list view

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    //sidenav fields
    TextView sidenavName,sidenavEmail,sidenavMobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_shop_manage_address);

        baseUrl = new ApiBaseUrl();

        getUserId = getIntent().getStringExtra("id");
        regMobile = getIntent().getStringExtra("mobile");
        System.out.print("medicalid in manage address....."+getUserId);

        uploadServerUrl = baseUrl.getUrl()+"MSGetAddress?ID="+getUserId;

        new GetMedicalDetails().execute(baseUrl.getUrl()+"MedicalShopByID"+"?id="+getUserId);

        new GetMedicalAllAddressDetails().execute(uploadServerUrl);

        myList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        adapter = new MedicalShopManageAddressAdapter(this, myList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

//        adapter.notifyDataSetChanged();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Manage Address");

//        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Toast.makeText(BloodBank.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(MedicalShopManageAddress.this,MedicalShopDashboard.class);
//                        intent.putExtra("id",getUserId);
//                        intent.putExtra("mobile",regMobile);
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

//        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_medical_shop_dashboard);

        sidenavName = (TextView) navigationView.findViewById(R.id.name);
        sidenavEmail = (TextView) navigationView.findViewById(R.id.emailId);
        sidenavMobile  = (TextView) navigationView.findViewById(R.id.mobile);
//        adharimage = (ImageView) navigationView.findViewById(R.id.profileImageId);


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
                    Intent contact = new Intent(MedicalShopManageAddress.this,MedicalShopEditProfile.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",regMobile);
                    contact.putExtra("user","old");
                    startActivity(contact);

                }

                else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                    Intent about = new Intent(MedicalShopManageAddress.this,SubscriptionPlanAlertDialog.class);
                    about.putExtra("id",getUserId);
                    about.putExtra("mobile",regMobile);
                    about.putExtra("module","medical");
                    startActivity(about);

                } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here
                    Intent contact = new Intent(MedicalShopManageAddress.this,Offers.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",regMobile);
                    contact.putExtra("module","medical");
                    startActivity(contact);

                } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(MedicalShopManageAddress.this,ReachUs.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",regMobile);
                    contact.putExtra("module","medical");
                    startActivity(contact);

                }

                else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here
                    Intent contact = new Intent(MedicalShopManageAddress.this,Login.class);
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

                        Intent about = new Intent(MedicalShopManageAddress.this,MedicalChangePassword.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",regMobile);
                        startActivity(about);

                    }
                    else if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                } else if(groupPosition == MedicalShopSideNavigationExpandableListAdapter.Address) {
                    if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM2_1) {

                        Intent about = new Intent(MedicalShopManageAddress.this,MedicalShopAddAddress.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",regMobile);
                        startActivity(about);

                    }
                    else if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent about = new Intent(MedicalShopManageAddress.this,MedicalShopManageAddress.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",regMobile);
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

            Intent intent = new Intent(MedicalShopManageAddress.this,MedicalShopDashboard.class);
            intent.putExtra("id",getUserId);
            intent.putExtra("mobile",regMobile);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    //get medical details based on id from api call
    private class GetMedicalDetails extends AsyncTask<String, Void, String> {

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

//            (String) js.get("DoctorID");
            if(js.has("ShopImage")) {
                myProfileImage = (String) js.get("ShopImage");

                System.out.println("doc profile image url.." + myProfileImage);

                String myMobile = (String) js.get("MobileNumber");
                String myEmail = (String) js.get("EmailID");
                String myName = (String) js.get("FirstName");
                String mySurname = (String) js.get("LastName");

                sidenavName.setText(myName+" "+mySurname);
                sidenavEmail.setText(myEmail);
                sidenavMobile.setText(myMobile);

            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    //Get all addresses for doctor list from api call
    private class GetMedicalAllAddressDetails extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
//            progressDialog = new ProgressDialog(MedicalShopManageAddress.this);
//            // Set progressdialog title
////            progressDialog.setTitle("Your searching process is");
//            // Set progressdialog message
//            progressDialog.setMessage("Loading...");
//
//            progressDialog.setIndeterminate(false);
//            // Show progressdialog
//            progressDialog.show();

            progressDialog = new ProgressDialog(MedicalShopManageAddress.this);
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
            Log.e("Api response.....", result);
            progressDialog.dismiss();
            try
            {
                JSONObject jsono = new JSONObject(result);
                String ss = (String) jsono.get("Message");
                if(ss.equals("No data found."))
                {
                    showMessage();
                    Log.e("Api response if.....", result);
                }
                else
                {
                    getData(result);
                    adapter.notifyDataSetChanged();
                    Log.e("Api response else.....", result);
                }
            }
            catch (Exception e)
            {}
            getData(result);
            adapter.notifyDataSetChanged();
//            Log.e("Api response.....", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }

    private void getData(String result) {
        try {

            JSONArray jarray = new JSONArray(result);

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);
                MedicalShopManageAddressClass bb = new MedicalShopManageAddressClass();
                bb.setMedicalshopID(getUserId);
                bb.setAddressId(object.getString("AddressID"));
                bb.setAddress1(object.getString("Address1"));
                bb.setFrom(object.getString("FromTime"));
                bb.setTo(object.getString("ToTime"));
                bb.setHospitalName(object.getString("ShopName"));
                bb.setStateId(object.getString("StateID"));
                bb.setCityId(object.getString("CityID"));
                bb.setPharmacyKey(object.getString("PharmacyType"));
                bb.setExperience(object.getString("Experience"));
                bb.setStateName(object.getString("StateName"));
                bb.setMobileNumber(object.getString("MobileNumber"));
                // bb.setConsultationFee(object.getString("ConsultationPrice"));

                bb.setEmergencyContactNumber(object.getString("EmergencyContact"));
                bb.setCenterImage(object.getString("ShopImage"));
                bb.setRegisteredMobileNumber(regMobile);
                String cityy = object.getString("CityName");
                bb.setCityName(object.getString("CityName"));
                bb.setZipcode(object.getString("PinCode"));
                bb.setLandLineNo(object.getString("LandlineNo"));
                bb.setContactPerson(object.getString("ContactPerson"));
                bb.setLatitude((object.getString("Latitude")));
                bb.setLongitude((object.getString("Longitude")));
                bb.setEmergencyservice(object.getBoolean("EmergencyService"));////
                bb.setComment(object.getString("Comment"));
//                bb.setDeleteReason();
                bb.setDistrict(object.getString("District"));

                System.out.println("url in medical..."+object.getString("ShopImage"));

                myList.add(bb);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void showMessage(){

        android.support.v7.app.AlertDialog.Builder a_builder = new android.support.v7.app.AlertDialog.Builder(MedicalShopManageAddress.this);
        a_builder.setMessage("Addresses are not available for ur profile")
                .setCancelable(false)
                .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Address list");
        alert.show();

    }


}
