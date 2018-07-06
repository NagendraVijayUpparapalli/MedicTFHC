package com.example.cool.patient.medicalShop;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.example.cool.patient.doctor.DoctorEditProfile;
import com.example.cool.patient.medicalShop.AddAddress.MedicalShopAddAddress;
import com.example.cool.patient.medicalShop.ManageAddress.MedicalShopManageAddress;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.bloder.magic.view.MagicButton;

public class MedicalShopEditProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    final Activity activity = this;
    EditText name,email,licenceNumber,mobileNumber,aadhar_num;
    Bitmap mIcon11;
    ImageView adharimage, licenceImage;
    List<String> Spaliaty;
    CheckBox cash_on_hand, swipe_card, net_banking, pay_paym;
    MagicButton gen_btn;
    RadioButton female,male;
    List<String>districtsList;
    List<String> myDistrictsList = new ArrayList<String>();
    ArrayAdapter<String> adapter1,adapter4;
    String Aadhar_num,Emergency_mobile;
    static String newName, mySurname, myName, myEmail, myMobile, mySalutation,mySpeciality,mySpecialityid,myQualification, myAddress1, myAddress2, myGender,
            myLicenceNumber,myuploadLicence,myadharimage,myAadhar_num;
    static boolean myMedicalPromotion,myDiagnosticPromotion,myBloodDonor ,mycash_on_hand,myswipe_card ,
            mynet_banking,mypay_paym;
    FloatingActionButton addLicenceIcon,addAadharIcon;
    final int REQUEST_CODE_GALLERY1 = 999,REQUEST_CODE_GALLERY2 = 44;
    //qr code get data fields
    static String qrName,qrGender,qrDob,qrFullAddress,qrAddress[],qrAddress1,qrAddress2,qrPincode;
    String myQrArrayList;
    Uri selectedLicenceImageUri,selectedAadharImageUri;
    Bitmap selectedLicenceImageBitmap = null,selectedAadharImageBitmap = null;
    String encodedAadharImage,encodedLicenceCertificateImage;
    ApiBaseUrl baseUrl;
    static String smsUrl = null;
    static String getUserId,mobile_number;
    static String uploadServerUrl = null;
    private String pt_name,pt_email,pt_licenceNumber,pt_mobileNumber,pt_aadhar_num;
    ProgressDialog progressDialog;

    String checkNewUser = null;

    // expandable list view

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
        setContentView(R.layout.activity_medical_shop_edit_profile);

        baseUrl = new ApiBaseUrl();

        mobile_number = getIntent().getStringExtra("mobile");
        getUserId = getIntent().getStringExtra("id");
        System.out.print("medical id in profile....."+getUserId+"...mobile.."+mobile_number);

        new GetMedicalDetails().execute(baseUrl.getUrl()+"MedicalShopByID"+"?id="+getUserId);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        mobileNumber = (EditText) findViewById(R.id.mobileNumber);
        female = (RadioButton) findViewById(R.id.femaleRadio);
        male = (RadioButton) findViewById(R.id.maleRadio);
        licenceNumber = (EditText) findViewById(R.id.licenceNumber);
        adharimage = (ImageView) findViewById(R.id.diag_aadhar_image);
        licenceImage = (ImageView) findViewById(R.id.Licence_Image);
        cash_on_hand = (CheckBox) findViewById(R.id.cash_on_hand);
        swipe_card = (CheckBox) findViewById(R.id.swipe_card);
        net_banking = (CheckBox) findViewById(R.id.net_banking);
//        pay_paym = (CheckBox) findViewById(R.id.pay_paym);
        aadhar_num = (EditText) findViewById(R.id.aadhaarNumber);
        addLicenceIcon = (FloatingActionButton) findViewById(R.id.Licence_ImageIcon);
        addAadharIcon = (FloatingActionButton) findViewById(R.id.addDiagAadharIcon);

        final RippleView rippleView = (RippleView) findViewById(R.id.rippleView);

        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editProfileValidate();
//                String js = formatDataAsJson();
//                new sendEditProfileDetails().execute(baseUrl.getUrl()+"MSEditprofile",js.toString());
            }
        });

        addLicenceIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(
                                MedicalShopEditProfile.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_GALLERY1
                        );

                    }
                });

        addAadharIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(
                                MedicalShopEditProfile.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_GALLERY2
                        );

                    }
                });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("Edit Profile");
//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(MedicalShopEditProfile.this,MedicalShopDashboard.class);
//                        intent.putExtra("id",getUserId);
//                        intent.putExtra("mobile",mobile_number);
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

        sidenavName = (TextView) navigationView.findViewById(R.id.name1);
        sidenavEmail = (TextView) navigationView.findViewById(R.id.emailId1);
        sidenavMobile  = (TextView) navigationView.findViewById(R.id.mobile1);
//        adharimage = (ImageView) headerLayout.findViewById(R.id.profileImageId);


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
                    Intent contact = new Intent(MedicalShopEditProfile.this,MedicalShopEditProfile.class);
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
                    Intent contact = new Intent(MedicalShopEditProfile.this,Offers.class);
                    contact.putExtra("id",getUserId);
                    contact.putExtra("mobile",mobile_number);
                    contact.putExtra("module","medical");
                    startActivity(contact);

                } else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM7) {
                    // call some activity here

                    Intent contact = new Intent(MedicalShopEditProfile.this,ReachUs.class);
                    startActivity(contact);

                }

                else if (groupPosition == MedicalShopSideNavigationExpandableListAdapter.ITEM8) {
                    // call some activity here
                    Intent contact = new Intent(MedicalShopEditProfile.this,Login.class);
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

                        Intent about = new Intent(MedicalShopEditProfile.this,ChangePassword.class);
                        about.putExtra("mobile",mobile_number);
                        startActivity(about);

                    }
                    else if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM3_2) {

                        // call activity here

                    }

                } else if(groupPosition == MedicalShopSideNavigationExpandableListAdapter.Address) {
                    if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM2_1) {

                        Intent about = new Intent(MedicalShopEditProfile.this,MedicalShopAddAddress.class);
                        about.putExtra("id",getUserId);
                        about.putExtra("mobile",mobile_number);
                        startActivity(about);

                    }
                    else if (childPosition == MedicalShopSideNavigationExpandableListAdapter.SUBITEM2_2) {
                        Intent about = new Intent(MedicalShopEditProfile.this,MedicalShopManageAddress.class);
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

    public void editProfileValidate()
    {
        intialization();
        if(!validate())
        {
//            Toast.makeText(this,"Succesfully field" , Toast.LENGTH_SHORT).show();

        }
        else
        {
            String js = formatDataAsJson();
            new sendEditProfileDetails().execute(baseUrl.getUrl()+"MSEditprofile",js.toString());
        }
    }

    public void intialization()
    {
        pt_name  = name.getText().toString().trim();
        pt_email=email.getText().toString().trim();
        pt_licenceNumber=licenceNumber.getText().toString().trim();
        pt_mobileNumber = mobileNumber.getText().toString().trim();
        pt_aadhar_num = aadhar_num.getText().toString().trim();
    }
    public boolean validate()
    {
        boolean validate = true;
        if(pt_name.isEmpty())
        {
            name.setError("plese enter the name");
            validate  = false;

        }
        if(pt_email.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(pt_email).matches())
        {
            email.setError("please enter the email correct formate");
            validate = false;
        }
        if(pt_licenceNumber.isEmpty())
        {
            licenceNumber.setError("plese enter the LicenceNumber");
            validate=false;
        }

        if(pt_mobileNumber.isEmpty() || !Patterns.PHONE.matcher(pt_mobileNumber).matches())
        {
            mobileNumber.setError("please enter the mobile number");
            validate=false;
        }
        else if(pt_mobileNumber.length()<10 || pt_mobileNumber.length()>10)
        {
            mobileNumber.setError(" Invalied phone number ");
            validate=false;
        }

        if(pt_aadhar_num.isEmpty())
        {
            aadhar_num.setError("please enter the Adharnumber");
            validate=false;
        }

        if(!cash_on_hand.isChecked() && !net_banking.isChecked() && !swipe_card.isChecked())
        {
//            Toast.makeText(getApplicationContext(),"Please select one option",Toast.LENGTH_SHORT).show();

            cash_on_hand.setError("please checked");
            net_banking.setError("plwase checked");
            swipe_card.setError("please checked");
//            pay_paym.setError("please checked");
            validate=false;
        }


        return validate;
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

            Intent intent = new Intent(MedicalShopEditProfile.this,MedicalShopDashboard.class);
            intent.putExtra("id",getUserId);
            intent.putExtra("mobile",mobile_number);
            startActivity(intent);
        }
//
//        if(id==R.id.qricon)
//        {
////            qrScanIcon.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
//
//            IntentIntegrator integrator = new IntentIntegrator(activity);
//            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//            integrator.setPrompt("Scan");
//            integrator.setCameraId(0);
//            integrator.setBeepEnabled(false);
//            integrator.setBarcodeImageEnabled(false);
//            integrator.initiateScan();
//            return true;
////                CameraManager a = new CameraManager();
////                }
////            });
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    //get medical details based on id from api call

    private class GetMedicalDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(MedicalShopEditProfile.this);
            // Set progressdialog title
//            progressDialog.setTitle("Your searching process is");
            // Set progressdialog message
            progressDialog.setMessage("Loading...");

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

            Log.e("TAG result diagprofile", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            progressDialog.dismiss();
            getProfileDetails(result);
        }

    }

    private void getProfileDetails(String result) {
        try
        {
            JSONObject js = new JSONObject(result);
            if(js.has("LicenceCertificate")) {
//            DiagnosticsID

                myMobile = (String) js.get("MobileNumber");
                myEmail = (String) js.get("EmailID");
                myName = (String) js.get("FirstName");
                mySurname = (String) js.get("LastName");
                myLicenceNumber = (String) js.get("LicenceNumber");
                myAadhar_num = (String) js.get("AadharNumber");
                myuploadLicence = (String) js.get("LicenceCertificate");
                myadharimage = (String) js.get("AadharImage");
                myGender = (String) js.get("Gender");
                mySalutation = (String) js.get("Suffix");
                mycash_on_hand     =  (boolean) js.get("CashOnHand");
                myswipe_card       =  (boolean) js.get("CreditDebit");
                mynet_banking      =  (boolean) js.get("Netbanking");
//                mypay_paym         =   (boolean) js.get("Paytm");


                if(myGender.equals("Male"))
                {
                    newName = "Mr.";
                }
                else if(myGender.equals("Female"))
                {
                    newName = "Ms.";
                }
                else if(myGender.equals("Female"))
                {
                    newName = "Mrs.";
                }
                if(myGender.equals(""))
                {
//                    checkNewUser = "Yes";
                    name.setText(mySurname+" "+myName);

                    sidenavName.setText(myName+" "+mySurname);
                    sidenavEmail.setText(myEmail);
                    sidenavMobile.setText(myMobile);
                }
                else {
//                    checkNewUser = "No";
                    name.setText(newName + mySurname + " " + myName);

                    sidenavName.setText(myName+" "+mySurname);
                    sidenavEmail.setText(myEmail);
                    sidenavMobile.setText(myMobile);

                }

            }
            else {
////            DiagnosticsID
//                checkNewUser = "Yes";
                myMobile = (String) js.get("MobileNumber");
                myEmail = (String) js.get("EmailID");
                myName = (String) js.get("FirstName");
                mySurname = (String) js.get("LastName");
                myLicenceNumber = (String) js.get("LicenceNumber");
                myAadhar_num = (String) js.get("AadharNumber");

                myuploadLicence = (String) js.get("LicenceCertificate");
                myadharimage = (String) js.get("AadharImage");
                myGender = (String) js.get("Gender");

                mySalutation = (String) js.get("Suffix");
                mycash_on_hand     =  (boolean) js.get("CashOnHand");
                myswipe_card       =  (boolean) js.get("CreditDebit");
                mynet_banking      =  (boolean) js.get("Netbanking");
//                mypay_paym         =  (boolean) js.get("Paytm");

                name.setText(mySurname+" "+myName);

                sidenavName.setText(myName+" "+mySurname);
                sidenavEmail.setText(myEmail);
                sidenavMobile.setText(myMobile);
            }

            System.out.println("hand.."+mycash_on_hand);
            System.out.println("card.."+myswipe_card);
            System.out.println("net.."+mynet_banking);
            System.out.println("paytm.."+mypay_paym);

            if(myGender.equals("Male"))
            {
                male.setChecked(true);
            }
            else if(myGender.equals("Female"))
            {
                female.setChecked(true);
            }
            else if(myGender.equals(""))
            {
                male.setChecked(false);
                female.setChecked(false);
            }

            if(myAadhar_num.equals(""))
            {
                checkNewUser = "Yes";
                System.out.println("checkNewUser in no aadhar num.."+checkNewUser);
            }

            else
            {
                checkNewUser = "No";
                System.out.println("checkNewUser in no aadhar num.."+checkNewUser);
            }

            sidenavEmail.setText(myEmail);
            sidenavMobile.setText(myMobile);
            cash_on_hand.setChecked(mycash_on_hand);
            swipe_card.setChecked(myswipe_card);
            net_banking.setChecked(mynet_banking);
//            pay_paym.setChecked(mypay_paym);


            name.setEnabled(false);

//          salutation.setTextColor(this.getResources().getColor(R.color.colorPrimary));
//           surname.setText(mySurname);

            mobileNumber.setText(myMobile);
            email.setText(myEmail);
            licenceNumber.setText(myLicenceNumber);
            aadhar_num.setText(myAadhar_num);

            System.out.println("image url.."+ myadharimage);

            new GetAadharImageTask(adharimage).execute(baseUrl.getImageUrl()+myadharimage);

            new GetLicenceCertificateImageTask(licenceImage).execute(baseUrl.getImageUrl()+myuploadLicence);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    public class GetAadharImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public GetAadharImageTask(ImageView bmImage) {
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
            adharimage.setImageBitmap(result);
        }

    }

    private class GetLicenceCertificateImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public GetLicenceCertificateImageTask(ImageView bmImage) {
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
            licenceImage.setImageBitmap(result);
        }

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

        else if (requestCode == REQUEST_CODE_GALLERY2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY2);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();

                myQrArrayList = result.getContents();
                String arr[] = myQrArrayList.split("=");

                qrName = arr[1].replaceFirst(".$","");
                qrGender = arr[3].replaceFirst(".$","");
                qrDob = arr[4].replaceFirst(".$","");
                qrFullAddress = arr[5].replaceFirst(".$","");

                qrAddress = qrFullAddress.split(",");

                qrAddress1 = qrAddress[0]+","+qrAddress[1];
                qrAddress2 = qrAddress[2]+","+qrAddress[3];

                qrPincode = qrAddress[7];


//                System.out.println("a[0]..."+arr[0]);
                System.out.println("name..."+arr[1].replaceFirst(".$",""));
                System.out.println("aadhar..."+arr[2].replaceFirst(".$",""));
                System.out.println("gender..."+arr[3].replaceFirst(".$",""));
                System.out.println("dob..."+arr[4].replaceFirst(".$",""));
                System.out.println("address..."+arr[5].replaceFirst(".$",""));

                System.out.println("qr code data..."+result.getContents());
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == REQUEST_CODE_GALLERY1) {
//            onSelectFromGalleryResult(data);
//             Make sure the request was successful
            Log.d("hello","I'm out.");
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {

                selectedLicenceImageUri = data.getData();
                BufferedWriter out=null;
                try {
                    selectedLicenceImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedLicenceImageUri);


//            //certificate base64
                    final InputStream imageStream = getContentResolver().openInputStream(selectedLicenceImageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] b = baos.toByteArray();
                    encodedLicenceCertificateImage = Base64.encodeToString(b, Base64.DEFAULT);

                }
                catch (IOException e)
                {
                    System.out.println("Exception ");

                }
                licenceImage.setImageBitmap(selectedLicenceImageBitmap);
                Log.d("hello","I'm in.");

            }
        }


        else if (requestCode == REQUEST_CODE_GALLERY2) {
//            onSelectFromGalleryResult(data);
//             Make sure the request was successful
            Log.d("hello","I'm out.");
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {

                selectedAadharImageUri = data.getData();
                BufferedWriter out=null;
                try {
                    selectedAadharImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedAadharImageUri);

                    //aadhar base64
                    final InputStream imageStream1 = getContentResolver().openInputStream(selectedAadharImageUri);
                    final Bitmap selectedImage1 = BitmapFactory.decodeStream(imageStream1);

                    ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                    selectedImage1.compress(Bitmap.CompressFormat.JPEG,100,baos1);
                    byte[] b1 = baos1.toByteArray();
                    encodedAadharImage = Base64.encodeToString(b1, Base64.DEFAULT);

                }
                catch (IOException e)
                {
                    System.out.println("Exception ");

                }
                adharimage.setImageBitmap(selectedAadharImageBitmap);
                Log.d("hello","I'm in.");

            }
        }

        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String formatDataAsJson()
    {

        JSONObject data = new JSONObject();

//        Emergency_mobile =  emergencymobileNumber.getText().toString().trim();
//        System.out.println("emergency contact..."+Emergency_mobile);

        myEmail = email.getText().toString().trim();
        myMobile = mobileNumber.getText().toString().trim();
        myLicenceNumber = licenceNumber.getText().toString().trim();
        Aadhar_num = aadhar_num.getText().toString().trim();

        if(female.isChecked()){
            myGender = "Female";
        }
        else if(male.isChecked())
        {
            myGender = "Male";
        }
        System.out.println("aadhar num..."+Aadhar_num);


        if(cash_on_hand.isChecked())
        {
            mycash_on_hand = true;
            System.out.println("mycash_on_hand if..."+mycash_on_hand);
        }
        else
        {
            mycash_on_hand = false;
            System.out.println("mycash_on_hand if..."+mycash_on_hand);
        }

        if(swipe_card.isChecked())
        {
//            myDiagnosticPromotion = false;
            myswipe_card = true;
            System.out.println("myswipe_card if..."+ myswipe_card);

        }
        else
        {
            myswipe_card = false;
            System.out.println("myswipe_card else..."+ myswipe_card);
        }

        if(net_banking.isChecked())
        {
//            myDiagnosticPromotion = false;
            mynet_banking = true;
            System.out.println("mynet_banking if..."+ mynet_banking);
        }
        else
        {
            mynet_banking = false;
            System.out.println("mynet_banking if..."+ mynet_banking);
        }

        if(encodedLicenceCertificateImage == null)
        {
            licenceImage.buildDrawingCache();
            BitmapDrawable bitmapDrawable = (BitmapDrawable) licenceImage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos1);
            byte[] b1 = baos1.toByteArray();
            encodedLicenceCertificateImage = Base64.encodeToString(b1, Base64.DEFAULT);

//            System.out.println("image view encoded Image..."+encodedCertificateImage);
        }

        if(encodedAadharImage == null)
        {
            adharimage.buildDrawingCache();
            BitmapDrawable bitmapDrawable = (BitmapDrawable) adharimage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos1);
            byte[] b1 = baos1.toByteArray();
            encodedAadharImage = Base64.encodeToString(b1, Base64.DEFAULT);

//            System.out.println("image view encoded Image..."+encodedCertificateImage);
        }

        try{

            if(checkNewUser.equals("Yes"))
            {
                data.put("MedicalShopID",getUserId);
                data.put("FirstName",mySurname);
                data.put("LastName",myName);
                data.put("MobileNumber",myMobile);
                data.put("EmailID",myEmail);
                data.put("LicenceNumber",myLicenceNumber);
                data.put("AadharNumber",Aadhar_num);
                data.put("Gender",myGender);
                data.put("CashOnHand", mycash_on_hand);
                data.put("Netbanking", mynet_banking);
                data.put("Paytm", "");
                data.put("CreditDebit", myswipe_card);
                data.put("Suffix", newName);
                data.put("AadharImage",encodedAadharImage);
                data.put("LicenceCertificate",encodedLicenceCertificateImage);

                return data.toString();
            }
            else if(checkNewUser.equals("No"))
            {
                data.put("MedicalShopID",getUserId);
                data.put("FirstName",mySurname);
                data.put("LastName",myName);
                data.put("MobileNumber",myMobile);
                data.put("EmailID",myEmail);
                data.put("LicenceNumber",myLicenceNumber);
//                data.put("AadharNumber",Aadhar_num);
                data.put("Gender",myGender);
                data.put("CashOnHand", mycash_on_hand);
                data.put("Netbanking", mynet_banking);
                data.put("Paytm", "");
                data.put("CreditDebit", myswipe_card);
                data.put("Suffix", newName);
                data.put("AadharImage",encodedAadharImage);
                data.put("LicenceCertificate",encodedLicenceCertificateImage);

                return data.toString();
            }


        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }

    //send diagnostic edit profile details to api
    private class sendEditProfileDetails extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(MedicalShopEditProfile.this);
            // Set progressdialog title
//            progressDialog.setTitle("You are logging");
            // Set progressdialog message
            progressDialog.setMessage("Loading");

            progressDialog.setIndeterminate(false);
            // Show progressdialog
            progressDialog.show();
        }

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
            Log.e("TAG result profile   ", result); // this is expecting a response code to be sent from your server upon receiving the POST data

            progressDialog.dismiss();
            JSONObject js;

            try {
                js= new JSONObject(result);
                int s = js.getInt("Code");
                if(s == 200)
                {
                    showSuccessMessage(js.getString("Message"));
                }
                else
                {
                    showErrorMessage(js.getString("Message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    public void showSuccessMessage(String result){

        MyDialog  = new Dialog(MedicalShopEditProfile.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.edit_success_alert);

        message = (TextView) MyDialog.findViewById(R.id.message);
        oklink = (LinearLayout) MyDialog.findViewById(R.id.ok);

        message.setEnabled(true);
        oklink.setEnabled(true);

        message.setText("Profile Updated Successfully");

        oklink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicalShopEditProfile.this,MedicalShopDashboard.class);
                intent.putExtra("id",getUserId);
                intent.putExtra("mobile",mobile_number);
                startActivity(intent);
            }
        });
        MyDialog.show();

//        AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
//
//        a_builder.setMessage(message)
//                .setCancelable(false)
//                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        dialog.cancel();
//                        Intent intent = new Intent(MedicalShopEditProfile.this,MedicalShopDashboard.class);
//                        intent.putExtra("id",getUserId);
//                        intent.putExtra("mobile",mobile_number);
//                        startActivity(intent);
//                    }
//                });
//        AlertDialog alert = a_builder.create();
//        alert.setTitle("Edit Profile");
//        alert.show();
    }

    public void showErrorMessage(String result){

        MyDialog  = new Dialog(MedicalShopEditProfile.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.edit_fail_alert);

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

    }

    /////Editprofile sms////
    private class Mytask extends AsyncTask<Void, Void,Void>
    {

        URL myURL=null;
        HttpURLConnection myURLConnection=null;
        BufferedReader reader=null;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // HttpURLConnection conn = (HttpURLConnection) new URL("https://www.mgage.solutions/SendSMS/sendmsg.php?uname=MedicTr&pass=X!g@c$R2&send=MEDICC&dest=8465887420&msg=Hi%20Gud%20Morning").openConnection();
                //HttpURLConnection conn = (HttpURLConnection) new URL("https://www.mgage.solutions/SendSMS/sendmsg.php?uname=MedicTr&pass=X!g@c$R2&send=MEDICC&dest=8465887420&msg=Hi%20Gud%20Morning").openConnection();


                String message="Your profile has been successfully updated for MEDIC TFHC.COM"+". Thank you."+"Click here to Login:"+baseUrl.getLink();
                smsUrl = baseUrl.getSmsUrl();
                String uname="MedicTr";
                String password="X!g@c$R2";
                String sender="MEDICC";
                String destination = myMobile;

                String encode_message= URLEncoder.encode(message, "UTF-8");
                StringBuilder stringBuilder=new StringBuilder(smsUrl);
                stringBuilder.append("uname="+ URLEncoder.encode(uname, "UTF-8"));
                stringBuilder.append("&pass="+URLEncoder.encode(password, "UTF-8"));
                stringBuilder.append("&send="+URLEncoder.encode(sender, "UTF-8"));
                stringBuilder.append("&dest="+URLEncoder.encode(destination, "UTF-8"));
                stringBuilder.append("&msg="+encode_message);

                smsUrl=stringBuilder.toString();
                System.out.println("smsUrl.. "+smsUrl);
                myURL=new URL(smsUrl);

                myURLConnection=(HttpURLConnection) myURL.openConnection();
                myURLConnection.connect();
                reader=new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                String response;
                while ((response = reader.readLine()) != null) {
                    Log.d("RESPONSE", "" + response);
                }
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            return null;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "the message", Toast.LENGTH_LONG).show();
            super.onPreExecute();
        }
    }


}
