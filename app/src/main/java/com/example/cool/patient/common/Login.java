package com.example.cool.patient.common;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.cool.patient.diagnostic.DiagnosticEditProfile;
import com.example.cool.patient.doctor.DoctorEditProfile;
import com.example.cool.patient.medicalShop.MedicalShopDashboard;
import com.example.cool.patient.medicalShop.MedicalShopEditProfile;
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

public class Login extends Activity {

    ImageView Image;
    LinearLayout cardViewId;
    TextView fonts;
    MagicButton button;
    Animation downnup,Cardviewdowntoup,Textviewdowntoup;
    RadioGroup radioGroup;
    RadioButton radioButton;
    int radiobuttonid;
    EditText mobile_num,password1,aadhar_num;
    CheckBox showPasswordCheckBox;
    TextView new_user,forgot,present_location;
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
        new_user = (TextView) findViewById(R.id.new_user);
        forgot = (TextView)findViewById(R.id.forgot);

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

        radiobuttonid = radioGroup.getCheckedRadioButtonId();

        System.out.println("radio...."+radiobuttonid);

        radioButton = (RadioButton) findViewById(radiobuttonid);

        password1 = (EditText) findViewById(R.id.password);
        aadhar_num = (EditText) findViewById(R.id.aadhar_Number);

        showPasswordCheckBox = (CheckBox) findViewById(R.id.cbShowPwd);

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

//        button = (MagicButton) findViewById(R.id.btn_login);

        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateLogin();


            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.mobileRadio)
                {
                    mobile_num.setVisibility(View.VISIBLE);
                    aadhar_num.setVisibility(View.GONE);
                }
                else if(checkedId==R.id.aadharRadio)
                {
//                    aadhar_num.setCompoundDrawablesWithIntrinsicBounds(null, null,getResources().getDrawable(R.drawable.aadharr), null);
                    mobile_num.setVisibility(View.GONE);
                    aadhar_num.setVisibility(View.VISIBLE);
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

    private String formatDataAsJson()
    {
        JSONObject data = new JSONObject();

        System.out.println("radio text.."+radioButton.getText());

        try{
            if(radioButton.getText().equals("Mobile Number"))
            {
                data.put("MobileNumber",mobile_num.getText().toString());
                data.put("UPassword",password1.getText().toString());
                data.put("LoginType",1);
                return data.toString();
            }
            else {
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

    public void validateLogin()
    {
        intialization();
        if(!validate())
        {
//            Toast.makeText(this,"Please enter above fields" , Toast.LENGTH_SHORT).show();
        }
        else
        {

            password=password1.getText().toString().trim();
            phone = mobile_num.getText().toString().trim();
            aadhar = aadhar_num.getText().toString().trim();


            String js = formatDataAsJson();
            System.out.println("btn clickable text...");
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
            progressDialog.setMessage("You are logged in few seconds...");

            progressDialog.setIndeterminate(false);
            // Show progressdialog
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

                else if(js.has("ModuleName"))
                {
                    module_name = js.getString("ModuleName");

                    userId =  js.getString("UserID");
                    System.out.println("module...."+module_name);
                    if(phone.equalsIgnoreCase(js.getString("MobileNumber")) && password.equalsIgnoreCase(js.getString("UPassword")))
                    {
                        if(module_name.equalsIgnoreCase("Patient"))
                        {
                            Intent i2 = new Intent(Login.this, PatientEditProfile.class);
                            i2.putExtra("mobile",phone);
                            i2.putExtra("id",userId);
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
//                        Log.e("city in login result...", city);
                            startActivity(i2);
                        }
                        else if(module_name.equalsIgnoreCase("Diagnostics"))
                        {
                            Intent i2 = new Intent(Login.this, DiagnosticEditProfile.class);
                            i2.putExtra("mobile",phone);
                            i2.putExtra("module","diag");
                            i2.putExtra("id",userId);
                            startActivity(i2);
                        }
                        else if(module_name.equalsIgnoreCase("Medicalshop"))
                        {
                            Intent i2 = new Intent(Login.this, MedicalShopEditProfile.class);
                            i2.putExtra("mobile",phone);
                            i2.putExtra("id",userId);
                            startActivity(i2);
                        }

                    }
                    else if(aadhar.equalsIgnoreCase(js.getString("MobileNumber")) && password.equalsIgnoreCase(js.getString("UPassword")))
                    {
                        if(module_name.equalsIgnoreCase("Patient"))
                        {

                            Intent i2 = new Intent(Login.this, PatientEditProfile.class);
                            i2.putExtra("id",userId);
//                        i2.putExtra("location",city);
//                        Log.e("city in login result...", city);
//                        System.out.print("city in loginactivity....."+city);
                            startActivity(i2);
                        }
                        else if(module_name.equalsIgnoreCase("Doctor"))
                        {
                            Intent i2 = new Intent(Login.this, DoctorEditProfile.class);
                            i2.putExtra("id",userId);
//                        i2.putExtra("location",city);
//                        Log.e("city in login result...", city);
                            startActivity(i2);
                        }
                        else if(module_name.equalsIgnoreCase("Diagnostics"))
                        {
                            Intent i2 = new Intent(Login.this, DiagnosticEditProfile.class);
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

    public boolean validate()
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

//        if(aadhar.isEmpty())
//        {
//            aadhar_num.setError("please enter the number");
//            validate=false;
//        }
//        else if(aadhar.length()!=12)
//        {
//            aadhar_num.setError(" Invalid aadhar number ");
//            validate=false;
//        }

        return validate;
    }
}