package com.example.cool.patient.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.example.cool.patient.R;
import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.patient.AmbulanceServices;
import com.example.cool.patient.patient.FindHospitals;
import com.example.cool.patient.patient.MyDiagnosticAppointments.PatientMyDiagnosticAppointments;
import com.example.cool.patient.patient.MyDoctorAppointments.PatientMyDoctorAppointments;
import com.example.cool.patient.patient.MyFamily;
import com.example.cool.patient.patient.PatientDashBoard;
import com.example.cool.patient.patient.PatientEditProfile;
import com.example.cool.patient.patient.PatientSideNavigationExpandableListAdapter;
import com.example.cool.patient.patient.PatientSideNavigationExpandableSubList;
import com.example.cool.patient.patient.ViewBloodBanksList.BloodBank;
import com.example.cool.patient.patient.ViewDiagnosticsList.GetCurrentDiagnosticsList;
import com.example.cool.patient.patient.ViewDoctorsList.GetCurrentDoctorsList;
import com.example.cool.patient.patient.ViewMedicalShopsList.GetCurrentMedicalShopsList;

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

public class ChangePassword extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    EditText mobile,curr_pswd,new_pswd,confirm_pswd;
    String phone_number,current_password,new_password,confirm_password;
    MagicButton btn_change_password;

    static String uploadServerUrl = null,mobile_number;
    static String getUserId;
    ApiBaseUrl baseUrl;

    Dialog MyDialog;
    TextView message;
    LinearLayout oklink;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    //sidenav fields
    TextView sidenavName,sidenavEmail,sidenavAddress,sidenavMobile,sidenavBloodgroup;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        baseUrl = new ApiBaseUrl();

        mobile_number = getIntent().getStringExtra("mobile");
        getUserId = getIntent().getStringExtra("id");
        System.out.print("userid in mainactivity....."+getUserId);

        new GetPatientDetails().execute(baseUrl.getUrl()+"GetPatientByID"+"?ID="+getUserId);

        mobile = (EditText) findViewById(R.id.mobile_number);
        curr_pswd = (EditText) findViewById(R.id.currentpassword);
        new_pswd = (EditText) findViewById(R.id.newpassword);
        confirm_pswd = (EditText) findViewById(R.id.confirmpassword);

        final RippleView rippleView = (RippleView) findViewById(R.id.rippleView);

//        button = (MagicButton) findViewById(R.id.btn_login);

        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changePasswordvalidate();
            }
        });


        //side navigation

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Change Password");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);

        sidenavName = (TextView) navigationView.findViewById(R.id.name);
        sidenavAddress = (TextView) navigationView.findViewById(R.id.address);
        sidenavMobile = (TextView) navigationView.findViewById(R.id.mobile);
        sidenavEmail = (TextView) navigationView.findViewById(R.id.email);
        sidenavBloodgroup = (TextView) navigationView.findViewById(R.id.bloodgroup);


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
                    Intent editProfile = new Intent(ChangePassword.this,PatientEditProfile.class);
                    editProfile.putExtra("id",getIntent().getStringExtra("id"));
                    editProfile.putExtra("mobile",mobile_number);
                    editProfile.putExtra("user","old");
                    startActivity(editProfile);

                }
                else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                    Intent contact = new Intent(ChangePassword.this,MyFamily.class);
                    contact.putExtra("id",getIntent().getStringExtra("id"));
                    contact.putExtra("mobile",mobile_number);
                    startActivity(contact);

                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here

                    Intent contact = new Intent(ChangePassword.this,Offers.class);
                    contact.putExtra("id",getIntent().getStringExtra("id"));
                    contact.putExtra("mobile",mobile_number);
                    contact.putExtra("module","patient");
                    startActivity(contact);


                }
                else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent editProfile = new Intent(ChangePassword.this,ReachUs.class);
                    editProfile.putExtra("id",getIntent().getStringExtra("id"));
                    editProfile.putExtra("mobile",mobile_number);
                    editProfile.putExtra("module","patient");
                    startActivity(editProfile);

                }
                else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here

                    Intent contact = new Intent(ChangePassword.this,Login.class);
                    startActivity(contact);

                }

                return retVal;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM1) {
                    if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_1) {

                        Intent i = new Intent(ChangePassword.this,GetCurrentDoctorsList.class);
                        i.putExtra("userId",getIntent().getStringExtra("id"));
                        i.putExtra("mobile",mobile_number);
                        startActivity(i);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_2) {
                        Intent i = new Intent(ChangePassword.this,GetCurrentDiagnosticsList.class);
                        i.putExtra("userId",getIntent().getStringExtra("id"));
                        i.putExtra("mobile",mobile_number);
                        startActivity(i);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_3) {

                        // call activity here

                        Intent in = new Intent(ChangePassword.this,GetCurrentMedicalShopsList.class);
                        in.putExtra("userId",getIntent().getStringExtra("id"));
                        in.putExtra("mobile",mobile_number);
                        startActivity(in);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_4) {

                        // call activity here
                        // call activity here
                        Intent contact = new Intent(ChangePassword.this,FindHospitals.class);
                        contact.putExtra("id",getIntent().getStringExtra("id"));
                        contact.putExtra("mobile",mobile_number);
                        startActivity(contact);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_5) {

                        // call activity here
                        Intent bloodbank = new Intent(ChangePassword.this,BloodBank.class);
                        bloodbank.putExtra("userId",getIntent().getStringExtra("id"));
                        bloodbank.putExtra("mobile",mobile_number);
                        startActivity(bloodbank);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM1_6) {

                        // call activity here
                        Intent contact = new Intent(ChangePassword.this,AmbulanceServices.class);
                        contact.putExtra("id",getIntent().getStringExtra("id"));
                        contact.putExtra("mobile",mobile_number);
                        startActivity(contact);
                        startActivity(contact);

                    }

                } else if (groupPosition == PatientSideNavigationExpandableListAdapter.ITEM3) {

                    if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM3_1) {

                        // call activity here
                        Intent intent = new Intent(ChangePassword.this,ChangePassword.class);
                        intent.putExtra("id",getIntent().getStringExtra("id"));
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

                        Intent intent = new Intent(ChangePassword.this,PatientMyDoctorAppointments.class);
                        intent.putExtra("mobile",mobile_number);
                        intent.putExtra("id",getIntent().getStringExtra("userId"));
                        startActivity(intent);

                    }
                    else if (childPosition == PatientSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent intent = new Intent(ChangePassword.this,PatientMyDiagnosticAppointments.class);
                        intent.putExtra("mobile",mobile_number);
                        intent.putExtra("id",getIntent().getStringExtra("userId"));
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

            Intent intent = new Intent(ChangePassword.this,PatientDashBoard.class);
            intent.putExtra("id",getUserId);
            intent.putExtra("mobile",mobile_number);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
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



    //Get patient list based on id from api call
    private class GetPatientDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(ChangePassword.this);
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
            org.json.JSONObject js = new org.json.JSONObject(result);

            String myName = (String) js.get("FirstName");
            String mySurname = (String) js.get("LastName");
            String myMobile = (String) js.get("MobileNumber");
            String myEmail = (String) js.get("EmailID");
            String myAddress1 = (String) js.get("Address1");
            String myAddress2 = (String) js.get("Address2");
            String myBlood_group = (String) js.get("BloodGroup");

//                TextView sidenavName,sidenavEmail,sidenavAddress,sidenavMobile;

            sidenavName.setText(mySurname+" "+myName);
            sidenavMobile.setText(myMobile);
            sidenavEmail.setText(myEmail);
            sidenavBloodgroup.setText(myBlood_group);


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

        MyDialog  = new Dialog(ChangePassword.this);
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

                Intent editProfile = new Intent(ChangePassword.this,PatientDashBoard.class);
                editProfile.putExtra("id",getIntent().getStringExtra("id"));
                editProfile.putExtra("mobile",mobile_number);
                startActivity(editProfile);

                MyDialog.cancel();

            }
        });
        MyDialog.show();

    }
}
