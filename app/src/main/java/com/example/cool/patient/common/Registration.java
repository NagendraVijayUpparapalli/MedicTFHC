package com.example.cool.patient.common;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.example.cool.patient.R;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import br.com.bloder.magic.view.MagicButton;

public class Registration extends AppCompatActivity {

    ProgressDialog progressDialog;
    Dialog MyDialog;
    TextView message;
    LinearLayout oklink;

    Spinner spinner;
    ImageView Image;
    LinearLayout cardViewId;
    TextView fonts,user;
    Animation downnup,Cardviewdowntoup,Textviewdowntoup;

    EditText surname,name,mobile,email,password,confirm_password,usertype;
    String mySurname,myName,myMobile,myEmail,myPassword,myConfirm_password,myUsertype;
    String selectedUserType;
    MagicButton register;
    static String uploadServerUrl = null;
    static int userPosition;
    RelativeLayout relativeLayout;

    //location fields
    LocationManager locationManager;
    String lattitude,longitude;
    Geocoder geocoder;
    List<Address> addresses;
    private static final int REQUEST_LOCATION = 1;
    static String city;
    int utype;

    AlertDialog alertDialog1;
    CharSequence[] values = {" Patient ", " Doctor ", " Diagnostic "," Medical Shop"," Blood Bank "," Ambulance "};
    static String userType;
    ApiBaseUrl baseUrl;

    String smsUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        baseUrl = new ApiBaseUrl();
//        baseUrl.getUrl();

        uploadServerUrl = baseUrl.getUrl()+"UserRegistration";

        getUserType();

        //registration fields
        surname = (EditText) findViewById(R.id.surname);
        name = (EditText) findViewById(R.id.name);
        mobile = (EditText) findViewById(R.id.mobileNumber);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirmpassword);
//        register = (MagicButton) findViewById(R.id.btn_register);

        relativeLayout = (RelativeLayout) findViewById(R.id.registerLayout);

        final RippleView rippleView = (RippleView) findViewById(R.id.rippleView);

        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        //getSupportActionBar().hide();
        Typeface mytapeface = Typeface.createFromAsset(getAssets(),"Rosewood.ttf");
        TextView mytextview = (TextView) findViewById(R.id.font);
        mytextview.setTypeface(mytapeface);
        fonts = (TextView)findViewById(R.id.font);
        user = (TextView)findViewById(R.id.already_user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registration.this,Login.class);
                startActivity(i);
            }
        });
        Image = (ImageView)findViewById(R.id.image1);
        cardViewId=(LinearLayout)findViewById(R.id.layoutId);
        downnup = AnimationUtils.loadAnimation(this,R.anim.downtoup);

        Textviewdowntoup=AnimationUtils.loadAnimation(this,R.anim.textdowntoup);


        Image.setAnimation(downnup);
        fonts.setAnimation(downnup);
        Cardviewdowntoup= AnimationUtils.loadAnimation(this,R.anim.cardviewdowntoup);
        cardViewId.setAnimation(Cardviewdowntoup);
        fonts.setAnimation(Textviewdowntoup);
//        spinner = (Spinner) findViewById(R.id.spinner);

        final List<String> list = new ArrayList<String>();
        list.add("Select user type...");
        list.add("Patient");
        list.add("DoctorClass");
        list.add("Diagnostic");
        list.add("MedicalStore");
        list.add("Hospital");
        list.add("BloodBank");
        list.add("Ambulance");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Registration.this,R.layout.support_simple_spinner_dropdown_item,list){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                userPosition = position;
//                selectedUserType = (String) parent.getItemAtPosition(position);
//                // If user change the default selection
//                // First item is disable and it is used for hint
//                if(position > 0){
//                    // Notify the selected item text
//                    Toast.makeText
//                            (getApplicationContext(), "Selected : " + selectedUserType, Toast.LENGTH_SHORT)
//                            .show();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
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

    private String formatDataAsJson()
    {
        if(userPosition==0)
        {
            utype = 1;
        }
        if(userPosition==1)
        {
            utype = 2;
        }
        if(userPosition==2)
        {
            utype = 3;
        }
        if(userPosition==3)
        {
            utype = 4;
        }


        JSONObject data = new JSONObject();
        try{
            data.put("FirstName",surname.getText().toString());
            data.put("LastName",name.getText().toString());
            data.put("MobileNumber",mobile.getText().toString());
            data.put("EmailID",email.getText().toString());
            data.put("PatientPassword",password.getText().toString());
            data.put("UserType",utype);
            data.put("Longitude",longitude);
            data.put("Latitude",lattitude);
            return data.toString();

        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }

    private class SendRegistrationDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(Registration.this);
            // Set progressdialog title
//            progressDialog.setTitle("You are logging");
            // Set progressdialog message
            progressDialog.setMessage("You are logged in few seconds...");

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
                if (statuscode == 500) {

                    in = httpURLConnection.getErrorStream();
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

                else if(statuscode == 406){
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

            Log.e("TAG result    ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
//            JSONObject js = (JSONObject) JSONValue.parse(result);
            try
            {
                org.json.JSONObject jsono = new org.json.JSONObject(result);

                int s = jsono.getInt("Code");
                if(s == 200)
                {

                    String ss = (String) jsono.get("Message");
                    showMessage(ss);
                }
                else
                {
                    showErrorMessage(jsono.getString("Message"));
                }

                Log.e("Api response if.....", result);

            }
            catch (Exception e)
            {}

        }
    }

    public void showMessage(String responsemessage){

        MyDialog  = new Dialog(Registration.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.success_alert);

        message = (TextView) MyDialog.findViewById(R.id.message);
        oklink = (LinearLayout) MyDialog.findViewById(R.id.ok);

        message.setEnabled(true);
        oklink.setEnabled(true);

        message.setText("Registration Successfully Completed");

        oklink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Mytask().execute();
                String js = emailFormatDataAsJson();

                System.out.println("registration email json data..."+js.toString());

                new sendEmailRegistrationDetails().execute(baseUrl.getEmailUrl(),js.toString());

                Intent i2 = new Intent(Registration.this, Login.class);
                startActivity(i2);
            }
        });
        MyDialog.show();

    }

    public void showErrorMessage(String result){

        MyDialog  = new Dialog(Registration.this);
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

    public void showUserType(){

        AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);

        if(userType==null)
        {
            a_builder.setMessage("Please select user type for registration")
                    .setCancelable(false)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getUserType();
                        }
                    });
//            a_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int i) {
//
//                }
//            });
            AlertDialog alert = a_builder.create();
            alert.setTitle("Type of Registration");
            alert.show();
        }
        else
        {
            a_builder.setMessage("You selected type is"+userType)
                    .setCancelable(false)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            relativeLayout.setVisibility(View.VISIBLE);
//                        Intent i2 = new Intent(Registration.this, Login.class);
//                        startActivity(i2);
                        }
                    });
            a_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    Intent i2 = new Intent(Registration.this, Registration.class);
                    startActivity(i2);
//                Toast.makeText(Registration.this, "Cancel", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alert = a_builder.create();
            alert.setTitle("Type of Registration");
            alert.show();
        }



    }

    public void register()
    {
        intialization();
        if(!validate())
        {
//            Toast.makeText(this,"Please enter above fields" , Toast.LENGTH_SHORT).show();
        }

        else
        {
            String js = formatDataAsJson();
            System.out.println("params...."+js.toString());
            new SendRegistrationDetails().execute(uploadServerUrl,js.toString());
        }
    }


    public void intialization()
    {
//        surname,name,mobile,email,password,confirm_password,usertype;

        mySurname = surname.getText().toString().trim();
        myName = name.getText().toString().trim();
        myMobile = mobile.getText().toString().trim();
        myEmail = email.getText().toString().trim();
        myPassword = password.getText().toString().trim();
        myConfirm_password = confirm_password.getText().toString().trim();
    }

    public boolean validate()
    {
        boolean validate = true;

        if(mySurname.isEmpty())
        {
            surname.setError("Please enter First Name");
            validate=false;
        }
        if(myName.isEmpty())
        {
            name.setError("Please enter Last Name");
            validate=false;
        }
        if(myPassword.length()<8)
        {
            password.setError("password should be 8 charactors or more than 8 charactors ");
            validate = false;
        }
        if(myMobile.isEmpty() || !Patterns.PHONE.matcher(myMobile).matches())
        {
            mobile.setError("please enter the mobile number");
            validate=false;
        }

        else if(myMobile.length()<10 || myMobile.length()>10)
        {
            mobile.setError(" Invalid phone number ");
            validate=false;
        }

        if(myEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(myEmail).matches())
        {
            email.setError("please enter valid email id");
            validate=false;
        }

        if(!myPassword.equals(myConfirm_password))
        {
            confirm_password.setError("password doesn't maatch");
        }

        return validate;
    }

    private void getLocation() {
        System.out.print("helo this is method");
        if (ActivityCompat.checkSelfPermission(Registration.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (Registration.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.print("helo this is if");

            ActivityCompat.requestPermissions(Registration.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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

    public void getUserType()
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Do you want to Register for ??");


        alert.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                userType = (String) values[item];
//                textview.setText(values[item]);
                switch (item) {
                    case 0:
                        userPosition = item;
//                        Toast.makeText(Registration.this, "Patient", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        userPosition = item;
//                        Toast.makeText(Registration.this, "DoctorClass", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        userPosition = item;
//                        Toast.makeText(Registration.this, "Diagnostic Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        userPosition = item;
//                        Toast.makeText(Registration.this, "medical Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        userPosition = item;
//                        Toast.makeText(Registration.this, "Blood Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 5:
                        userPosition = item;
//                        Toast.makeText(Registration.this, "Ambulance Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    default:
//                        userPosition = 5;
//                        Toast.makeText(Registration.this, item, Toast.LENGTH_LONG).show();
//                        Toast.makeText(Registration.this, "Please Select One of these User type", Toast.LENGTH_SHORT).show();
//                        Intent i2 = new Intent(Registration.this, Registration.class);
//                        startActivity(i2);
                        break;

                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = alert.create();
        //  alert.show();


        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

//                alert.setCancelable(true);
//                Toast.makeText(Registration.this, userPosition, Toast.LENGTH_LONG).show();
//                Toast.makeText(Registration.this, userPosition, Toast.LENGTH_SHORT).show();
//                if(i==5)
//                {
//                    Intent i2 = new Intent(Registration.this, ChangePassword.class);
//                    startActivity(i2);
//                }
//                else {

                    showUserType();
//                }

//                Toast.makeText(Registration.this, "OK", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Intent i2 = new Intent(Registration.this, Login.class);
                startActivity(i2);
//                Toast.makeText(Registration.this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        });

        alert.show();
    }

    //////Registration SMS/////////
    private class Mytask extends AsyncTask<Void, Void,Void>
    {

        URL myURL=null;
        HttpURLConnection myURLConnection=null;
        BufferedReader reader=null;

        @Override
        protected Void doInBackground(Void... voids) {
            try
            {
                String usertype = null;

                if(utype == 1)
                {
                    usertype = "Patient";
                }
                if(utype == 2)
                {
                    usertype = "Doctor";
                }
                if(utype == 3)
                {
                    usertype = "Diagnostic";
                }
                if(utype == 4)
                {
                    usertype = "Medical Shop";
                }

                String phone = mobile.getText().toString();
                String password1 = password.getText().toString();

                String message="You have successfully registered as a "+usertype+", "+"and your User ID: "+phone+" and Password: "+password1+". Thank You. "+"Click here to Login: "+baseUrl.getLink();
                smsUrl = baseUrl.getSmsUrl();
                String uname="MedicTr";
                String password="X!g@c$R2";
                String sender="MEDICC";
                String destination = mobile.getText().toString();

                String encode_message= URLEncoder.encode(message, "UTF-8");
                StringBuilder stringBuilder=new StringBuilder(smsUrl);
                stringBuilder.append("uname="+URLEncoder.encode(uname, "UTF-8"));
                stringBuilder.append("&pass="+URLEncoder.encode(password, "UTF-8"));
                stringBuilder.append("&send="+URLEncoder.encode(sender, "UTF-8"));
                stringBuilder.append("&dest="+URLEncoder.encode(destination, "UTF-8"));
                stringBuilder.append("&msg="+encode_message);

                smsUrl=stringBuilder.toString();
                System.out.println("smsUrl "+smsUrl);
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
//            Toast.makeText(getApplicationContext(), "the message", Toast.LENGTH_LONG).show();
            super.onPreExecute();
        }
    }

    private String emailFormatDataAsJson()
    {
        org.json.JSONObject data = new org.json.JSONObject();

        String usertype = null;

        if(utype == 1)
        {
            usertype = "Patient";
        }
        if(utype == 2)
        {
            usertype = "Doctor";
        }
        if(utype == 3)
        {
            usertype = "Diagnostic";
        }
        if(utype == 4)
        {
            usertype = "Medical Shop";
        }

        String phone = mobile.getText().toString();
        String password1 = password.getText().toString();

        String message="You have successfully registered as a "+usertype+", "+"and your User ID: "+phone+" and Password: "+password1+". Thank You. "+"Click here to Login: "+baseUrl.getLink();

        try
        {
            data.put("includeFooter","Yes");
            data.put("password","X!g@c$R2");
            data.put("userName","MedicTr");

            org.json.JSONObject messageJsonData = new org.json.JSONObject();
            messageJsonData.put("custRef","423423423");
            messageJsonData.put("fromEmail","services@medictfhc.com");
            messageJsonData.put("html","The answer is Business");
            messageJsonData.put("recipient",email.getText().toString());
            messageJsonData.put("fromName","Medic");
            messageJsonData.put("replyTo","services@medictfhc.com");
            messageJsonData.put("subject","Registered Successfully");
            messageJsonData.put("text","Hello");

            org.json.JSONObject mTagJsonData = new org.json.JSONObject();
            mTagJsonData.put("mtag1","testing");

            messageJsonData.put("mtag",new org.json.JSONObject(mTagJsonData.toString()));

            org.json.JSONObject templateJsonData = new org.json.JSONObject();
            templateJsonData.put("templateId","MedicEmail2");

            org.json.JSONObject templateValuesJsonData = new org.json.JSONObject();
            templateValuesJsonData.put("UserNameParamater",surname.getText().toString()+" "+name.getText().toString());
            templateValuesJsonData.put("MessageParamater",message);
            templateJsonData.put("templateValues",new org.json.JSONObject(templateValuesJsonData.toString()));

            messageJsonData.put("template",new org.json.JSONObject(templateJsonData.toString()));

            data.put("message",new org.json.JSONObject(messageJsonData.toString()));

            System.out.println("js obj..."+data);

            return data.toString();

        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }

    //send RegistrationDetails to user call
    private class sendEmailRegistrationDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(Registration.this);
            // Set progressdialog title
            progressDialog.setTitle("Your searching process is");
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

                httpURLConnection.setUseCaches(false);
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

            Log.e("TAG result email   ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            progressDialog.dismiss();

            org.json.JSONObject js;

//            try {
//                js= new JSONObject(result);
//                int s = js.getInt("Code");
//                if(s == 200)
//                {
//
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

}
