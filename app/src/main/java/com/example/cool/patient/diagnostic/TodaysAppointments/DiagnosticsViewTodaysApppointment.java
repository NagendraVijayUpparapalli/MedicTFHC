package com.example.cool.patient.diagnostic.TodaysAppointments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.R;
import com.example.cool.patient.common.ChangePassword;
import com.example.cool.patient.common.Login;
import com.example.cool.patient.common.Offers;
import com.example.cool.patient.common.ReachUs;
import com.example.cool.patient.common.aboutUs.AboutUs;
import com.example.cool.patient.diagnostic.AddAddress.DiagnosticAddAddress;
import com.example.cool.patient.diagnostic.DashBoardCalendar.DiagnosticDashboard;
import com.example.cool.patient.diagnostic.DiagnosticEditProfile;
import com.example.cool.patient.diagnostic.DiagnosticSideNavigationExpandableListAdapter;
import com.example.cool.patient.diagnostic.DiagnosticSideNavigationExpandableSubList;
import com.example.cool.patient.diagnostic.ManageAddress.DiagnosticManageAddress;
import com.example.cool.patient.subscriptionPlan.SubscriptionPlanAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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

public class DiagnosticsViewTodaysApppointment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    EditText Patientname,emailid,mobilenumer,aadharnumber,address;
    TextView testname,status;
    MultiAutoCompleteTextView comments;

    String Pname,eid,mnumer,aanumber,addres,ttname,statuss,cmt,prescrption,centername;
    int appointmentID,Dstatus,RDID;
    String PatientID,diagnosticId,diagMobile;
    String str,commnt;
    Button history;
    RippleView submit;
    FloatingActionButton camera,gallery;
    final int REQUEST_CODE_GALLERY1 = 999,REQUEST_CODE_GALLERY2 = 1;

    Uri selectedImageUri ;
    Bitmap selectedImageBitmap = null;
    ImageView prescription;
    static String encodedImage = null;

    ApiBaseUrl baseUrl;

    // expandable list view
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    //sidenav fields
    TextView sidenavName,sidenavEmail,sidenavMobile;

    ProgressDialog progressDialog;
    Dialog MyDialog;
    TextView message;
    LinearLayout oklink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostics_view_todays_apppointment);

        baseUrl = new ApiBaseUrl();

        Patientname=(EditText)findViewById(R.id.name);
        emailid=(EditText)findViewById(R.id.emailid);
        mobilenumer=(EditText)findViewById(R.id.mobilenumber);
        aadharnumber=(EditText)findViewById(R.id.aadharnumber);
        address=(EditText)findViewById(R.id.address);
        testname=(TextView)findViewById(R.id.testname);
        status=(TextView)findViewById(R.id.status);
        comments=(MultiAutoCompleteTextView)findViewById(R.id.Comments);

        prescription=(ImageView) findViewById(R.id.prescription);

        camera=(FloatingActionButton) findViewById(R.id.camera_icon);
        gallery=(FloatingActionButton)findViewById(R.id.gallery_icon);

        submit=(RippleView) findViewById(R.id.submit);
        history=(Button)findViewById(R.id.click);

        aanumber=getIntent().getStringExtra("Aadharnum");
        appointmentID=Integer.parseInt(getIntent().getStringExtra("appointmentid"));
        diagnosticId = getIntent().getStringExtra("diagId");
        diagMobile = getIntent().getStringExtra("diagMobile");

        new GetDiagnosticDetails().execute(baseUrl.getUrl()+"DiagnosticByID"+"?id="+diagnosticId);

        new GetPatientDetails().execute(baseUrl.getUrl()+"DiagnosticViewTodayAppDetails"+"?id="+appointmentID);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DiagnosticsViewTodaysApppointment.this,MainPatientHistoryInDiagnostics.class);
                i.putExtra("patientid",PatientID);
                i.putExtra("diagId",diagnosticId);
                i.putExtra("diagMobile",diagMobile);
                System.out.println("patient id in view diag todays.."+PatientID+".."+diagnosticId+"..."+diagMobile);
                startActivity(i);
            }
        });

        gallery.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(
                                DiagnosticsViewTodaysApppointment.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_GALLERY1
                        );

                    }
                });

        camera.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, REQUEST_CODE_GALLERY2);
                        }
                    }
                });

        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_GALLERY2);
        }



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json=formatDataAsJson();
                new SendDetails().execute(baseUrl.getUrl()+"DiagnosticUpdateTodayAppointment",json.toString());
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("View Todays Appointments");
//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(DiagnosticsViewTodaysApppointment.this,DiagnosticsTodaysAppointments.class);
//                        intent.putExtra("userId",diagnosticId);
//                        startActivity(intent);
//
//                    }
//                }
//
//        );


        // side navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_diagnostic_dashboard);

        sidenavName = (TextView) navigationView.findViewById(R.id.name1);
        sidenavEmail = (TextView) navigationView.findViewById(R.id.email1);
        sidenavMobile = (TextView) navigationView.findViewById(R.id.mobile1);

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
                    Intent contact = new Intent(DiagnosticsViewTodaysApppointment.this,DiagnosticEditProfile.class);
                    contact.putExtra("id",diagnosticId);
                    contact.putExtra("mobile",diagMobile);
                    contact.putExtra("user","old");
                    startActivity(contact);

                }

                else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM5) {
                    // call some activity here
                    Intent subscript = new Intent(DiagnosticsViewTodaysApppointment.this,SubscriptionPlanAlertDialog.class);
                    subscript.putExtra("id",diagnosticId);
                    subscript.putExtra("mobile",diagMobile);
                    subscript.putExtra("module","diag");
                    startActivity(subscript);

                } else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM6) {
                    // call some activity here
                    Intent contact = new Intent(DiagnosticsViewTodaysApppointment.this,Offers.class);
                    contact.putExtra("id",diagnosticId);
                    contact.putExtra("mobile",diagMobile);
                    contact.putExtra("module","diag");
                    startActivity(contact);

                } else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(DiagnosticsViewTodaysApppointment.this,ReachUs.class);
                    contact.putExtra("id",diagnosticId);
                    contact.putExtra("mobile",diagMobile);
                    contact.putExtra("module","diag");
                    startActivity(contact);

                }

                else if (groupPosition == DiagnosticSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here
                    Intent contact = new Intent(DiagnosticsViewTodaysApppointment.this,Login.class);
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

                        Intent i = new Intent(DiagnosticsViewTodaysApppointment.this,DiagnosticDashboard.class);
                        i.putExtra("id",diagnosticId);
                        i.putExtra("mobile",diagMobile);
                        startActivity(i);

                    }
                    else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM1_2) {

                        // call activity here

                        Intent i = new Intent(DiagnosticsViewTodaysApppointment.this,DiagnosticsTodaysAppointments.class);
                        i.putExtra("userId",diagnosticId);
                        i.putExtra("mobile",diagMobile);
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

                        Intent about = new Intent(DiagnosticsViewTodaysApppointment.this,ChangePassword.class);
                        about.putExtra("mobile",diagMobile);
                        startActivity(about);

                    }
                    else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                } else if(groupPosition == DiagnosticSideNavigationExpandableListAdapter.Address) {
                    if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM2_1) {


                        Intent about = new Intent(DiagnosticsViewTodaysApppointment.this,DiagnosticAddAddress.class);
                        about.putExtra("id",diagnosticId);
                        about.putExtra("mobile",diagMobile);
                        startActivity(about);

                    }
                    else if (childPosition == DiagnosticSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent about = new Intent(DiagnosticsViewTodaysApppointment.this,DiagnosticManageAddress.class);
                        about.putExtra("id",diagnosticId);
                        about.putExtra("mobile",diagMobile);
                        startActivity(about);

                    }

                }
                return true;

            }
        });

    }


    private class GetAadharImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public GetAadharImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
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
            prescription.setImageBitmap(result);
        }

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



    //    new GetDiagnosticDetails().execute(baseUrl.getUrl()+"DiagnosticByID"+"?id="+getUserId);

    //get diagnostic details based on id from api call
    private class GetDiagnosticDetails extends AsyncTask<String, Void, String> {

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
            getProfileDetails(result);
        }

    }

    private void getProfileDetails(String result) {
        try
        {
            JSONObject js = new JSONObject(result);

            String myMobile = (String) js.get("MobileNumber");
            String myEmail = (String) js.get("EmailID");
            String myName = (String) js.get("FirstName");
            String mySurname = (String) js.get("LastName");

            sidenavName.setText(myName+" "+mySurname);
            sidenavMobile.setText(myMobile);
            sidenavEmail.setText(myEmail);


        }
        catch (JSONException e)
        {
            e.printStackTrace();
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

            Intent intent = new Intent(DiagnosticsViewTodaysApppointment.this,DiagnosticDashboard.class);
            intent.putExtra("id",diagnosticId);
            intent.putExtra("mobile",diagMobile);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY1);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        else
        {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_GALLERY2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY1) {
            Log.d("hello","I'm out.");
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {

                selectedImageUri = data.getData();
                BufferedWriter out=null;
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    final InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    encodedImage = myEncodeImage(selectedImage);

                }
                catch (IOException e)
                {
                    System.out.println("Exception ");

                }

                prescription.setImageBitmap(selectedImageBitmap);

                Log.d("hello","I'm in.");

            }
        }

        else if(requestCode == REQUEST_CODE_GALLERY2)
        {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            prescription.setImageBitmap(thumbnail);

            prescription.buildDrawingCache();
            BitmapDrawable bitmapDrawable = (BitmapDrawable) prescription.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos1);
            byte[] b1 = baos1.toByteArray();
            encodedImage = Base64.encodeToString(b1, Base64.DEFAULT);

        }

        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private String myEncodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

//        decode base64 string to image
//        b = Base64.decode(encImage, Base64.DEFAULT);
//        Bitmap decodedImage = BitmapFactory.decodeByteArray(b, 0, b.length);
//        decodeimg.setImageBitmap(decodedImage);

        return encImage;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private class SendDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
//            progressDialog = new ProgressDialog(DiagnosticsViewTodaysApppointment.this);
//            // Set progressdialog title
////            progressDialog.setTitle("You are logging");
//            // Set progressdialog message
//            progressDialog.setMessage("Loading...");
//
//            progressDialog.setIndeterminate(false);
//            // Show progressdialog
//            progressDialog.show();

            progressDialog = new ProgressDialog(DiagnosticsViewTodaysApppointment.this);
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

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setUseCaches(false);
                //connection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
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
            progressDialog.dismiss();

            try
            {
                org.json.JSONObject jsono = new org.json.JSONObject(result);

                int s = jsono.getInt("Code");
                if(s == 200)
                {

                    String ss = (String) jsono.get("Message");
                    showSuccessMessage(ss);
                }
                else
                {
                    showErrorMessage(jsono.getString("Message"));
                }

                Log.e("Api response if.....", result);

            }
            catch (Exception e)
            {}

            Log.e("TAG result    ", result); // this is expecting a response code to be sent from your server upon receiving the POST data

        }
    }

    private String formatDataAsJson()
    {
        commnt = comments.getText().toString();

        System.out.println("comments"+commnt);
        JSONObject data = new JSONObject();

        try{

            data.put("DiagnosticsAppID", appointmentID);
            data.put("DStatus",Dstatus);
            data.put("Comment",commnt);
            //data.put("Prescription","");
            data.put("Prescription",encodedImage);


            return data.toString();

        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }

    public void showSuccessMessage(String responsemessage){

        MyDialog  = new Dialog(DiagnosticsViewTodaysApppointment.this);
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
                Intent intent = new Intent(DiagnosticsViewTodaysApppointment.this,DiagnosticsTodaysAppointments.class);
                intent.putExtra("userId",diagnosticId);
                intent.putExtra("mobile",diagMobile);
                startActivity(intent);
            }
        });
        MyDialog.show();

    }

    public void showErrorMessage(String result){

        MyDialog  = new Dialog(DiagnosticsViewTodaysApppointment.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.cancel_alertdialog);

        message = (TextView) MyDialog.findViewById(R.id.message);
        oklink = (LinearLayout) MyDialog.findViewById(R.id.ok);

        message.setEnabled(true);
        oklink.setEnabled(true);

        message.setText(result);

        oklink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });
        MyDialog.show();

    }


    //Get patient details  based on doctor id and appointment date
    private class GetPatientDetails extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result    ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getDetails(result);

        }
    }

    private void getDetails(String result) {
        try {

            String myprescription = "",mycomments = "";
            JSONArray jsonArray=new JSONArray(result);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject js = jsonArray.getJSONObject(i);

                Dstatus = js.getInt("DStatus");
                RDID=js.getInt("RDID");
                Pname=(String)js.get("PatientName");
                ttname=(String)js.get("TestName");
                addres=(String)js.get("Address1");
                centername = (String) js.get("CenterName");
                eid=(String) js.get("EmailID");
                mnumer=(String) js.get("MobileNo");
                myprescription = js.getString("Prescription");
                mycomments = js.getString("Comments");
                PatientID=js.getString("PatientID");

            }
            Patientname.setText(Pname);
            emailid.setText(eid);
            mobilenumer.setText(mnumer);
            aadharnumber.setText(aanumber);
            address.setText(addres);
            comments.setText(mycomments);

            Patientname.setEnabled(false);
            Patientname.setClickable(false);

            emailid.setEnabled(false);
            emailid.setClickable(false);

            mobilenumer.setEnabled(false);
            mobilenumer.setClickable(false);

            aadharnumber.setEnabled(false);
            aadharnumber.setClickable(false);

            address.setEnabled(false);
            address.setClickable(false);

            testname.setText(ttname);

            new GetAadharImageTask(prescription).execute(baseUrl.getImageUrl()+myprescription);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
