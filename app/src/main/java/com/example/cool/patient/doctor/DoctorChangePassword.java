package com.example.cool.patient.doctor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.example.cool.patient.R;
import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.common.Offers;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.doctor.AddAddress.DoctorAddAddress;
import com.example.cool.patient.doctor.DashBoardCalendar.DoctorDashboard;
import com.example.cool.patient.doctor.DoctorEditProfile;
import com.example.cool.patient.doctor.DoctorSideNavigatioExpandableSubList;
import com.example.cool.patient.doctor.DoctorSideNavigationExpandableListAdapter;
import com.example.cool.patient.doctor.ManageAddress.DoctorManageAddress;
import com.example.cool.patient.doctor.TodaysAppointments.DoctorTodaysAppointmentsForPatient;
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

public class DoctorChangePassword extends AppCompatActivity
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
    ProgressDialog progressDialog;

    ImageView imageView,profileImage;
    TextView name,email,mymobile;
    Bitmap mIcon11;

    Dialog MyDialog;
    TextView message;
    LinearLayout oklink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_change_password);
        mobile_number = getIntent().getStringExtra("mobile");
        getUserId = getIntent().getStringExtra("id");
        System.out.print("userid in patientactivity....."+getUserId);


        baseUrl = new ApiBaseUrl();


        new GetDoctorDetails().execute(baseUrl.getUrl()+"GetDoctorByID"+"?id="+getUserId);

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

//        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_doctor_dashboard);


        name = (TextView) navigationView.findViewById(R.id.name);
        email = (TextView) navigationView.findViewById(R.id.emailId);
        mymobile = (TextView) navigationView.findViewById(R.id.mobile);
        profileImage = (ImageView) navigationView.findViewById(R.id.profileImageId);

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
                    Intent contact = new Intent(DoctorChangePassword.this,DoctorEditProfile.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",mobile_number);
                    contact.putExtra("user","old");
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
//                    Intent i = new Intent(DoctorChangePassword.this,SubscriptionPlanAlertDialog.class);
//                    i.putExtra("id",getUserId);
//                    i.putExtra("mobile",mobile_number);
//                    i.putExtra("module","doc");
//                    startActivity(i);

                } else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here
                    Intent contact = new Intent(DoctorChangePassword.this,Offers.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",mobile_number);
                    contact.putExtra("module","doc");
                    startActivity(contact);

                }


                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(DoctorChangePassword.this,ReachUs.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",mobile_number);
                    contact.putExtra("module","doc");
                    startActivity(contact);

                }

                else if (groupPosition == DoctorSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here
                    Intent contact = new Intent(DoctorChangePassword.this,Login.class);
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

                        Intent i = new Intent(DoctorChangePassword.this,DoctorDashboard.class);
                        i.putExtra("id",getUserId);
                        i.putExtra("mobile",mobile_number);
                        startActivity(i);


                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM1_2) {

                        // call activity here

                        Intent i = new Intent(DoctorChangePassword.this,DoctorTodaysAppointmentsForPatient.class);
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

                        Intent about = new Intent(DoctorChangePassword.this,DoctorChangePassword.class);
                        about.putExtra("mobile",mobile_number);
                        startActivity(about);

                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                } else if(groupPosition == DoctorSideNavigationExpandableListAdapter.Address) {
                    if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_1) {


                        Intent about = new Intent(DoctorChangePassword.this,DoctorAddAddress.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",mobile_number);
                        startActivity(about);

                    }
                    else if (childPosition == DoctorSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent about = new Intent(DoctorChangePassword.this,DoctorManageAddress.class);
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

            Intent intent = new Intent(DoctorChangePassword.this,DoctorDashboard.class);
            intent.putExtra("id",getUserId);
            intent.putExtra("mobile",mobile_number);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
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

    private class GetDocProfileImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public GetDocProfileImageTask(ImageView bmImage) {
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
            org.json.JSONObject js = new org.json.JSONObject(result);

            String myEmail = (String) js.get("EmailID");
            String myFirstName = (String) js.get("FirstName");
            String myLastName = (String) js.get("LastName");
            String mydoctorImage = (String) js.get("DoctorImage");
            String myMobile = (String) js.get("MobileNumber");

            System.out.println("name.."+myFirstName+".."+myLastName+".."+myEmail+".."+mydoctorImage);

            name.setText(myFirstName+" "+myLastName);
            email.setText(myEmail);
            mymobile.setText(myMobile);

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

        MyDialog  = new Dialog(DoctorChangePassword.this);
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
