package com.example.cool.patient.common;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.example.cool.patient.diagnostic.DashBoardCalendar.DiagnosticDashboard;
import com.example.cool.patient.diagnostic.DiagnosticEditProfile;
import com.example.cool.patient.doctor.DashBoardCalendar.DoctorDashboard;
import com.example.cool.patient.doctor.DoctorEditProfile;
import com.example.cool.patient.medicalShop.MedicalShopDashboard;
import com.example.cool.patient.medicalShop.MedicalShopEditProfile;
import com.example.cool.patient.patient.PatientDashBoard;
import com.example.cool.patient.patient.PatientEditProfile;
import com.example.cool.patient.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import br.com.bloder.magic.view.MagicButton;

public class Login extends AppCompatActivity {

    ImageView Image;
    LinearLayout cardViewId;
    TextView fonts;
    MagicButton button;
    Animation downnup,Cardviewdowntoup,Textviewdowntoup;
    RadioGroup radioGroup;
    RadioButton radioButton;
    int radiobuttonid,loginTypeId;
    EditText mobile_num,password1,aadhar_num;
    CheckBox showPasswordCheckBox;
    TextView present_location;
    LinearLayout new_user,forgot;
    private TextView mTextMessage;

    String  password,phone,aadhar;

    LocationManager locationManager;
    String lattitude,longitude;
    double lat,lng;
    Geocoder geocoder;
    List<Address> addresses;
    List<Address> fulladdress;
    private static final int REQUEST_LOCATION = 1;

    Button login;


    static String uploadServerUrl = null;
    static String module_name="";
    static String userId;
    static int logintype;
    static String str ="",city;
    ApiBaseUrl baseUrl;

    ProgressDialog progressDialog;
    static String access_token = null;

    private CheckBox rem_mobile_aadhaar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String PREF_NAME = "prefs";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_Mobile = "mobile";
    private static final String KEY_Aadhaar = "aadhaar";
    private static final String KEY_PASS = "password";

    LinearLayout aadharLayout,mobileLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        baseUrl = new ApiBaseUrl();

//        String id = MyAppStorageClass.getInstance().getPreferenceManager().getString(MyPreferenceManager.KEY_ID);
//        String accessToken = MyAppStorageClass.getInstance().getPreferenceManager().getString("access_token");

//        SharedPreferences sharedPreferences=mContext.getSharedPreferences("", Context.MODE_PRIVATE);
//        String access_token = sharedPreferences.getString("access_token", null);
//        System.out.println("my token value login..."+accessToken);


//        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
//        access_token = sharedPreferences.getString("access_token", null);
//
//        System.out.println("my token value login..."+access_token);


        Typeface mytapeface = Typeface.createFromAsset(getAssets(),"Rosewood.ttf");
        TextView mytextview = (TextView) findViewById(R.id.font);
        mytextview.setTypeface(mytapeface);
        fonts = (TextView)findViewById(R.id.font);
        Image = (ImageView)findViewById(R.id.image1);
        cardViewId=(LinearLayout)findViewById(R.id.layoutId);

        present_location = (TextView) findViewById(R.id.current_location);
        new_user = (LinearLayout) findViewById(R.id.new_user);
        forgot = (LinearLayout)findViewById(R.id.forgot);

//        mobileLayout = (LinearLayout) findViewById(R.id.mobileLayout);
//        aadharLayout = (LinearLayout) findViewById(R.id.aadharLayout);

        downnup = AnimationUtils.loadAnimation(this,R.anim.downtoup);

        Textviewdowntoup=AnimationUtils.loadAnimation(this,R.anim.textdowntoup);

        Image.setAnimation(downnup);
        fonts.setAnimation(downnup);
        Cardviewdowntoup=AnimationUtils.loadAnimation(this,R.anim.cardviewdowntoup);
        cardViewId.setAnimation(Cardviewdowntoup);
        fonts.setAnimation(Textviewdowntoup);

        mobile_num = (EditText) findViewById(R.id.mobileNumber);

        // find the radioButton by returned id
        radioGroup = (RadioGroup) findViewById(R.id.logintype_radio);
//
//        radiobuttonid = radioGroup.getCheckedRadioButtonId();
//
//        System.out.println("radio...."+radiobuttonid);
//
//        radioButton = (RadioButton) findViewById(radiobuttonid);

        password1 = (EditText) findViewById(R.id.password);
        aadhar_num = (EditText) findViewById(R.id.aadharNumber);

        //store mobile number or aadhar number for future use
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        showPasswordCheckBox = (CheckBox) findViewById(R.id.cbShowPwd);
        rem_mobile_aadhaar = (CheckBox) findViewById(R.id.remember);

        if(sharedPreferences.getBoolean(KEY_REMEMBER, false))
            rem_mobile_aadhaar.setChecked(true);
        else
            rem_mobile_aadhaar.setChecked(false);

        if(!sharedPreferences.getString(KEY_Mobile,"").equals(""))
        {
            mobile_num.setText(sharedPreferences.getString(KEY_Mobile,""));
            rem_mobile_aadhaar.setChecked(false);
        }

        else if(!sharedPreferences.getString(KEY_Aadhaar,"").equals(""))
        {
            aadhar_num.setText(sharedPreferences.getString(KEY_Aadhaar,""));
            rem_mobile_aadhaar.setChecked(false);
        }

//        mobile_num.setText(sharedPreferences.getString(KEY_Mobile,""));
//        aadhar_num.setText(sharedPreferences.getString(KEY_Aadhaar,""));
        password1.setText(sharedPreferences.getString(KEY_PASS,""));


        rem_mobile_aadhaar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                managePrefs();
            }
        });

        // add onCheckedListener on checkbox
        // when user clicks on this checkbox, this is the handler.
        showPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        final RippleView rippleView = (RippleView) findViewById(R.id.rippleView);

        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password=password1.getText().toString().trim();
                phone = mobile_num.getText().toString().trim();
                aadhar = aadhar_num.getText().toString().trim();

                if(!phone.equals(""))
                {
                    System.out.println("mobile validate");
                    validateMobileLogin();
                }
                else
                {
                    System.out.println("aadhaar validate");
                    validateAadhaarLogin();
                }

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId==R.id.mobileRadio)
                {
                    aadhar_num.setText("");
                    mobile_num.setVisibility(View.VISIBLE);
                    aadhar_num.setVisibility(View.GONE);

//                    mobileLayout.setVisibility(View.VISIBLE);
//                    aadharLayout.setVisibility(View.GONE);
                }
                else if(checkedId==R.id.aadharRadio)
                {
                    mobile_num.setText("");
                    mobile_num.setVisibility(View.GONE);
                    aadhar_num.setVisibility(View.VISIBLE);

//                    mobileLayout.setVisibility(View.GONE);
//                    aadharLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(Login.this, Registration.class);
              startActivity(intent);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(Login.this, ForgotPassword.class);
              startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        startActivity(intent);
    }

    private void managePrefs(){
        if(rem_mobile_aadhaar.isChecked()){

            if(!mobile_num.getText().toString().trim().equals(""))
            {
                editor.putString(KEY_Mobile, mobile_num.getText().toString().trim());
                editor.putString(KEY_PASS, password1.getText().toString().trim());
                editor.putBoolean(KEY_REMEMBER, true);
                editor.apply();
            }
            else
            {
                editor.putString(KEY_Aadhaar, aadhar_num.getText().toString().trim());
                editor.putString(KEY_PASS, password1.getText().toString().trim());
                editor.putBoolean(KEY_REMEMBER, true);
                editor.apply();
            }

        }else{
            editor.putBoolean(KEY_REMEMBER, false);
//            editor.remove(KEY_PASS);//editor.putString(KEY_PASS,"");
            editor.remove(KEY_Mobile);//editor.putString(KEY_Mobile, "");
            editor.remove(KEY_Aadhaar);//editor.putString(KEY_Aadhaar, "");
            editor.apply();
        }
    }

    private String formatDataAsJson()
    {
        JSONObject data = new JSONObject();

        try{
            if(!mobile_num.getText().toString().trim().equals(""))
            {
                System.out.println("mobile json text.."+mobile_num.getText().toString().trim());
                data.put("MobileNumber",mobile_num.getText().toString());
                data.put("UPassword",password1.getText().toString());
                data.put("LoginType",1);
                return data.toString();
            }
            else if(!aadhar_num.getText().toString().trim().equals("")) {
                System.out.println("aadhar json text.."+aadhar_num.getText().toString().trim());
                data.put("MobileNumber", aadhar_num.getText().toString());
                data.put("UPassword", password1.getText().toString());
                data.put("LoginType", 2);
                return data.toString();
            }
        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }

    public void validateMobileLogin()
    {
        System.out.println("phone validate...");

        intialization();
        if(!mobileValidate())
        {
            Toast.makeText(this,"Please enter above fields" , Toast.LENGTH_SHORT).show();

//            System.out.println("btn clickable text if condition...");
        }
        else
        {

            password=password1.getText().toString().trim();
            phone = mobile_num.getText().toString().trim();

            System.out.println("phone text..."+phone);
            String js = formatDataAsJson();
            System.out.println("btn clickable text phone...");
            new SendDetails().execute(baseUrl.getUrl()+"UserLogin",js.toString());
//            new SendDetails().execute(baseUrl.getUrl()+"UserLoginNew",js.toString());
        }
    }

    public void validateAadhaarLogin()
    {
        System.out.println("aadhaar validate...");
        intialization();
        if(!aadhaarValidate())
        {
//            Toast.makeText(this,"Please enter above fields" , Toast.LENGTH_SHORT).show();

            System.out.println("btn clickable text if condition...");
        }
        else
        {

            password=password1.getText().toString().trim();

            aadhar = aadhar_num.getText().toString().trim();

//            System.out.println("phone text..."+phone);
//            System.out.println("aadhaar text..."+aadhar);
            String js = formatDataAsJson();
            System.out.println("btn clickable text aadhaar...");
            new SendDetails().execute(baseUrl.getUrl()+"UserLogin",js.toString());
//            new SendDetails().execute(baseUrl.getUrl()+"UserLoginNew",js.toString());
        }
    }

    private class SendDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(Login.this);
            // Set progressdialog title
//            progressDialog.setTitle("You are logging");
            // Set progressdialog message
            progressDialog.setMessage("Logging in...");

            progressDialog.setIndeterminate(false);
            // Show progressdialog
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setP
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            String auth = "Bearer "+access_token;

            System.out.println("auth token...."+auth);

            HttpURLConnection httpURLConnection = null;
            try {
                System.out.println("dsfafssss....");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();

                httpURLConnection.setUseCaches(false);
//                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
//                httpURLConnection.setRequestProperty("Authorization",auth);
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
//                    showMessage();
                    in = httpURLConnection.getErrorStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(in);

                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data += current;
                    }
                }
                else if(statuscode == 401){
//                    showMessage();
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
            str = result;

            Log.e("TAG result login...", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            progressDialog.dismiss();
            JSONObject js;

            try {
                js = new JSONObject(str);

                if(js.has("Code"))
                {
                    showMessage();
                }

                else if(js.has("ModuleName") && js.getBoolean("IsEdited")==false)
                {
                    module_name = js.getString("ModuleName");

                    userId =  js.getString("UserID");
                    System.out.println("module...."+module_name);
                    if(js.getString("MobileNumber").length()==10)
                    {
                        if(phone.equalsIgnoreCase(js.getString("MobileNumber")) && password.equalsIgnoreCase(js.getString("UPassword")))
                        {
                            if(module_name.equalsIgnoreCase("Patient"))
                            {
                                Intent i2 = new Intent(Login.this, PatientEditProfile.class);
                                i2.putExtra("mobile",phone);
                                i2.putExtra("id",userId);
                                i2.putExtra("user","new");
//                        Log.e("city in login result...", city);
//                        System.out.print("city in loginactivity....."+city);
                                startActivity(i2);
                            }
                            else if(module_name.equalsIgnoreCase("Doctor"))
                            {
                                Intent i2 = new Intent(Login.this, DoctorEditProfile.class);
                                i2.putExtra("mobile",phone);
                                i2.putExtra("module","doc");
                                i2.putExtra("id",userId);
                                i2.putExtra("user","new");
//                        Log.e("city in login result...", city);
                                startActivity(i2);
                            }
                            else if(module_name.equalsIgnoreCase("Diagnostics"))
                            {
                                Intent i2 = new Intent(Login.this, DiagnosticEditProfile.class);
                                i2.putExtra("mobile",phone);
                                i2.putExtra("module","diag");
                                i2.putExtra("id",userId);
                                i2.putExtra("user","new");
                                startActivity(i2);
                            }
                            else if(module_name.equalsIgnoreCase("Medicalshop"))
                            {
                                Intent i2 = new Intent(Login.this, MedicalShopEditProfile.class);
                                i2.putExtra("mobile",phone);
                                i2.putExtra("id",userId);
                                i2.putExtra("user","new");
                                startActivity(i2);
                            }

                        }
                    }
                    else
                    {
                        if(aadhar.equalsIgnoreCase(js.getString("MobileNumber")) && password.equalsIgnoreCase(js.getString("UPassword")))
                        {
                            if(module_name.equalsIgnoreCase("Patient"))
                            {
                                Intent i2 = new Intent(Login.this, PatientEditProfile.class);
                                i2.putExtra("mobile",phone);
                                i2.putExtra("id",userId);
                                i2.putExtra("user","new");
    //                        Log.e("city in login result...", city);
    //                        System.out.print("city in loginactivity....."+city);
                                startActivity(i2);
                            }
                            else if(module_name.equalsIgnoreCase("Doctor"))
                            {
                                Intent i2 = new Intent(Login.this, DoctorEditProfile.class);
                                i2.putExtra("mobile",phone);
                                i2.putExtra("id",userId);
                                i2.putExtra("user","new");
                                startActivity(i2);
                            }
                            else if(module_name.equalsIgnoreCase("Diagnostics"))
                            {
                                Intent i2 = new Intent(Login.this, DiagnosticEditProfile.class);
                                i2.putExtra("mobile",phone);
                                i2.putExtra("id",userId);
                                i2.putExtra("user","new");
                                startActivity(i2);
                            }
                            else if(module_name.equalsIgnoreCase("Medicalshop"))
                            {
                                Intent i2 = new Intent(Login.this, MedicalShopEditProfile.class);
                                i2.putExtra("mobile",phone);
                                i2.putExtra("id",userId);
                                i2.putExtra("user","new");
                                startActivity(i2);
                            }
                        }
                    }


                }

                else if(js.has("ModuleName") && js.getBoolean("IsEdited")==true)
                {
                    module_name = js.getString("ModuleName");

                    userId =  js.getString("UserID");
                    System.out.println("module...."+module_name);
                    if(js.getString("MobileNumber").length()==10)
                    {
                        if(phone.equalsIgnoreCase(js.getString("MobileNumber")) && password.equalsIgnoreCase(js.getString("UPassword")))
                        {
                            if(module_name.equalsIgnoreCase("Patient"))
                            {
                                Intent i2 = new Intent(Login.this, PatientDashBoard.class);
                                i2.putExtra("mobile",phone);
                                i2.putExtra("id",userId);
                                startActivity(i2);
                            }
                            else if(module_name.equalsIgnoreCase("Doctor"))
                            {
                                Intent i2 = new Intent(Login.this, DoctorDashboard.class);
                                i2.putExtra("mobile",phone);
                                i2.putExtra("id",userId);
                                startActivity(i2);
                            }
                            else if(module_name.equalsIgnoreCase("Diagnostics"))
                            {
                                Intent i2 = new Intent(Login.this, DiagnosticDashboard.class);
                                i2.putExtra("mobile",phone);
                                i2.putExtra("id",userId);
                                startActivity(i2);
                            }
                            else if(module_name.equalsIgnoreCase("Medicalshop"))
                            {
                                Intent i2 = new Intent(Login.this, MedicalShopDashboard.class);
                                i2.putExtra("mobile",phone);
                                i2.putExtra("id",userId);
                                startActivity(i2);
                            }

                        }
                    }
                    else
                    {
                        if(aadhar.equalsIgnoreCase(js.getString("MobileNumber")) && password.equalsIgnoreCase(js.getString("UPassword")))
                        {
                            if(module_name.equalsIgnoreCase("Patient"))
                            {
                                Intent i2 = new Intent(Login.this, PatientDashBoard.class);
                                i2.putExtra("mobile",phone);
                                i2.putExtra("id",userId);
                                startActivity(i2);
                            }
                            else if(module_name.equalsIgnoreCase("Doctor"))
                            {
                                Intent i2 = new Intent(Login.this, DoctorDashboard.class);
                                i2.putExtra("mobile",phone);
                                i2.putExtra("id",userId);
                                startActivity(i2);
                            }
                            else if(module_name.equalsIgnoreCase("Diagnostics"))
                            {
                                Intent i2 = new Intent(Login.this, DiagnosticDashboard.class);
                                i2.putExtra("mobile",phone);
                                i2.putExtra("id",userId);
                                startActivity(i2);
                            }
                            else if(module_name.equalsIgnoreCase("Medicalshop"))
                            {
                                Intent i2 = new Intent(Login.this, MedicalShopDashboard.class);
                                i2.putExtra("mobile",phone);
                                i2.putExtra("id",userId);
                                startActivity(i2);
                            }
                        }
                    }

                }


//                else
//                {
////                    Toast.makeText(getApplicationContext(),"Your credentials are not correct please check",Toast.LENGTH_SHORT).show();
//                    showMessage();
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    private void getLocation() {
        System.out.print("helo this is method");
        if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (Login.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.print("helo this is if");

            ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            System.out.print("helo this is else");

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                System.out.print("latii...."+lattitude);
                System.out.print("longi...."+longitude);

                geocoder=new Geocoder(getApplicationContext());

                try {
                    addresses = geocoder.getFromLocation(latti, longi,1);
                    System.out.println("addresses"+addresses);

                    if (addresses.isEmpty())
                    {
//                        cityname.setTitle("waiting");
//                        present_location.setText("Waiting");
                    }
                    else
                    {
                        if(addresses.size()>0)
                        {
                            city=addresses.get(0).getLocality();
//                            present_location.setText(city);
////                            cityname.setTitle(city);
                            System.out.println("city name"+city);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }

            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                System.out.print("latii...."+lattitude);
                System.out.print("longi...."+lattitude);

                geocoder=new Geocoder(getApplicationContext());

                try {
                    addresses = geocoder.getFromLocation(latti, longi,1);
                    System.out.println("addresses"+addresses);

                    if (addresses.isEmpty())
                    {
//                        cityname.setTitle("waiting");
//                        present_location.setText("Waiting");
                    }
                    else
                    {
                        if(addresses.size()>0)
                        {
                            city=addresses.get(0).getLocality();
//                            present_location.setText(city);
////                            cityname.setTitle(city);
                            System.out.println("city name"+city);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }

            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                System.out.print("latii...."+lattitude);
                System.out.print("longi...."+lattitude);

                geocoder=new Geocoder(getApplicationContext());

                try {
                    addresses = geocoder.getFromLocation(latti, longi,1);
                    System.out.println("addresses"+addresses);

                    if (addresses.isEmpty())
                    {
//                        cityname.setTitle("waiting");
//                        present_location.setText("Waiting");
                    }
                    else
                    {
                        if(addresses.size()>0)
                        {
                            city=addresses.get(0).getLocality();
//                            present_location.setText(city);
//                            cityname.setTitle(city);
                            System.out.println("city name"+city);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }

            }else{

                Toast.makeText(this,"Unable to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }


    public void showMessage(){

        AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage("Your credentials are not correct please check")
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Login Credentials");
        alert.show();

    }

    public void intialization()
    {
        password = password1.getText().toString().trim();
        phone = mobile_num.getText().toString().trim();
        aadhar = aadhar_num.getText().toString().trim();
    }

    public boolean mobileValidate()
    {
        boolean validate = true;

        if(password.isEmpty())
        {
            password1.setError("please enter the password");
            validate=false;
        }
        else if(password.length()<8)
        {
            password1.setError("password should be 8 charactors or more than 8 charactors ");
            validate = false;

        }
        if(phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches())
        {
            mobile_num.setError("please enter the number");
            validate=false;
        }
        else if(phone.length()<10 || phone.length()>10)
        {
            mobile_num.setError(" Invalid phone number ");
            validate=false;
        }

        return validate;
    }

    public boolean aadhaarValidate()
    {
        boolean validate = true;

        if(password.isEmpty())
        {
            password1.setError("please enter the password");
            validate=false;
        }
        else if(password.length()<8)
        {
            password1.setError("password should be 8 charactors or more than 8 charactors ");
            validate = false;

        }

        if(aadhar.isEmpty())
        {
            aadhar_num.setError("please enter aadhar number");
            validate=false;
        }

//        else if(aadhar.length()>12)
//        {
//            aadhar_num.setError(" Invalid aadhar number ");
//            validate=false;
//        }
//        else if(aadhar.length()<12)
//        {
//            aadhar_num.setError(" Invalid aadhar number ");
//            validate=false;
//        }

        return validate;
    }
}