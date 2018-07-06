//package com.example.cool.patient.medicalShop;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//
//public class MedicalChangePassword extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_medical_change_password);
//    }
//}


package com.example.cool.patient.medicalShop;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.example.cool.patient.R;
import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.common.Offers;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.common.aboutUs.AboutUs;
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
import com.example.cool.patient.doctor.DoctorEditProfile;
import com.example.cool.patient.doctor.DoctorSideNavigatioExpandableSubList;
import com.example.cool.patient.doctor.DoctorSideNavigationExpandableListAdapter;
import com.example.cool.patient.doctor.ManageAddress.DoctorManageAddress;
import com.example.cool.patient.doctor.TodaysAppointments.DoctorTodaysAppointmentsForPatient;
import com.example.cool.patient.medicalShop.AddAddress.MedicalShopAddAddress;
import com.example.cool.patient.medicalShop.ManageAddress.MedicalShopManageAddress;
import com.example.cool.patient.medicalShop.MedicalShopEditProfile;
import com.example.cool.patient.medicalShop.MedicalShopSideNavigatioExpandableSubList;
import com.example.cool.patient.medicalShop.MedicalShopSideNavigationExpandableListAdapter;
import com.example.cool.patient.patient.MyDiagnosticAppointments.PatientMyDiagnosticAppointments;
import com.example.cool.patient.patient.MyDoctorAppointments.PatientMyDoctorAppointments;
import com.example.cool.patient.patient.PatientEditProfile;
import com.example.cool.patient.patient.PatientSideNavigationExpandableListAdapter;
import com.example.cool.patient.patient.PatientSideNavigationExpandableSubList;
import com.example.cool.patient.patient.ViewDiagnosticsList.GetCurrentDiagnosticsList;
import com.example.cool.patient.patient.ViewDoctorsList.GetCurrentDoctorsList;
import com.example.cool.patient.subscriptionPlan.SubscriptionPlanAlertDialog;

import org.json.JSONException;
import org.json.simple.JSONObject;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
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

import br.com.bloder.magic.view.MagicButton;

public class MedicalChangePassword extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText mobile,curr_pswd,new_pswd,confirm_pswd;
    String phone_number,current_password,new_password,confirm_password;
    MagicButton btn_change_password;
    static String getUserId;
    static String uploadServerUrl = null,mobile_number;
    ApiBaseUrl baseUrl;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    //sidenav fields
    TextView sidenavName,sidenavEmail,sidenavMobile;

    Dialog MyDialog;
    TextView message;
    LinearLayout oklink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_change_password);
        mobile_number = getIntent().getStringExtra("mobile");
        getUserId = getIntent().getStringExtra("id");
        System.out.print("userid in patientactivity....."+getUserId);


        baseUrl = new ApiBaseUrl();

        mobile_number = getIntent().getStringExtra("mobile");

        mobile = (EditText) findViewById(R.id.mobile_number);
        curr_pswd = (EditText) findViewById(R.id.currentpassword);
        new_pswd = (EditText) findViewById(R.id.newpassword);
        confirm_pswd = (EditText) findViewById(R.id.confirmpassword);

        new GetMedicalDetails().execute(baseUrl.getUrl()+"MedicalShopByID"+"?id="+getUserId);

        final RippleView rippleView = (RippleView) findViewById(R.id.rippleView);


//        button = (MagicButton) findViewById(R.id.btn_login);

        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordvalidate();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                    Intent contact = new Intent(MedicalChangePassword.this,MedicalShopEditProfile.class);
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
                    Intent contact = new Intent(MedicalChangePassword.this,Offers.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",mobile_number);
                    contact.putExtra("module","medical");
                    startActivity(contact);

                } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(MedicalChangePassword.this,ReachUs.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",mobile_number);
                    contact.putExtra("module","medical");
                    startActivity(contact);

                }

                else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here
                    Intent contact = new Intent(MedicalChangePassword.this,Login.class);
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

                        Intent about = new Intent(MedicalChangePassword.this,MedicalChangePassword.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",mobile_number);
                        startActivity(about);

                    }
                    else if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                } else if(groupPosition == MedicalShopSideNavigationExpandableListAdapter.Address) {
                    if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM2_1) {

                        Intent about = new Intent(MedicalChangePassword.this,MedicalShopAddAddress.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",mobile_number);
                        startActivity(about);

                    }
                    else if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent about = new Intent(MedicalChangePassword.this,MedicalShopManageAddress.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",mobile_number);
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

    public void changePasswordvalidate()
    {
        intialization();
        if(!validate())
        {
//            Toast.makeText(this,"Please enter above fields" , Toast.LENGTH_SHORT).show();
        }

        else
        {
            String js = formatDataAsJson();
            new SendChangePasswordDetails().execute(baseUrl.getUrl()+"ChangePassword",js.toString());
        }
    }


    public void intialization()
    {
//        surname,name,mobile,email,password,confirm_password,usertype;
        current_password = curr_pswd.getText().toString();
        new_password = new_pswd.getText().toString().trim();
        confirm_password = confirm_pswd.getText().toString().trim();
    }

    public boolean validate()
    {
        boolean validate = true;

        if(current_password.length()<8)
        {
            curr_pswd.setError("password should be 8 charactors or more than 8 charactors ");
            validate = false;
        }

        if(new_pswd.length()<8)
        {
            new_pswd.setError("password should be 8 charactors or more than 8 charactors ");
            validate = false;
        }

        if(!new_password.equals(confirm_password))
        {
            confirm_pswd.setError("password doesn't maatch");
        }

        return validate;
    }


    //-------manage address---------//
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

            Log.e("TAG result profile", result); // this is expecting a response code to be sent from your server upon receiving the POST data
//            progressDialog.dismiss();
            getProfileDetails(result);
        }

    }

    private void getProfileDetails(String result) {
        try
        {
            org.json.JSONObject js = new org.json.JSONObject(result);

            String myMobile = (String) js.get("MobileNumber");
            String myEmail = (String) js.get("EmailID");
            String myName = (String) js.get("FirstName");
            String mySurname = (String) js.get("LastName");

            System.out.println("medical shop name.." + myName);

            sidenavName.setText(myName+" "+mySurname);
            sidenavEmail.setText(myEmail);
            sidenavMobile.setText(myMobile);



        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }


    private String formatDataAsJson()
    {
        JSONObject data = new JSONObject();
        try{
            data.put("MobileNumber",mobile_number);
            data.put("Password",curr_pswd.getText().toString());
            data.put("NewPassword",new_pswd.getText().toString());
            return data.toString();

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



    private class SendChangePasswordDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {
                System.out.println("dsfafssss....");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                Log.d("Service","Started");
                httpURLConnection.setDoOutput(true);
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

            Log.e("TAG result    ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
//            JSONObject js = (JSONObject) JSONValue.parse(result);
            try
            {
                org.json.JSONObject jsono = new org.json.JSONObject(result);
                String ss = (String) jsono.get("Message");
                showMessage(ss);
                Log.e("Api response if.....", result);

            }
            catch (Exception e)
            {}


        }
    }

    public void showMessage(String responsemessage){

        MyDialog  = new Dialog(MedicalChangePassword.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.success_alert);

        message = (TextView) MyDialog.findViewById(R.id.message);
        oklink = (LinearLayout) MyDialog.findViewById(R.id.ok);

        message.setEnabled(true);
        oklink.setEnabled(true);

        message.setText(responsemessage);

        oklink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyDialog.cancel();

            }
        });
        MyDialog.show();

    }
}
