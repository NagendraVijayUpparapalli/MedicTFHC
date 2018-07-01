package com.example.cool.patient.patient.BookAppointment;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.patient.PatientDashBoard;
import com.example.cool.patient.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class PatientBookAppointmentToDiagnostics extends AppCompatActivity {

    com.prolificinteractive.materialcalendarview.MaterialCalendarView calendarView;
    RelativeLayout calenderlayout,imagelayout,mainlayout,addresslayout;
    Button button,submit;
    int year,month,day;
    String date;
    Dialog MyDialog,addresspopupdialog,acknowledgedialog;
    TextView cancel,ok,cancel1,ok1;
    CheckBox checkPatientSameUser,checkhomeSample,checkEnableHistory,checkAcknowledge;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    boolean[] checkDiagSpecialityItems;
    String[] allDiagSpecialityItems;
    List<String> DiagSpecialityItemsList;
    HashMap<String, String> AllDiagSpecialityList = new HashMap<String, String>();

    public static List getSelectedSpecialityItemsList =null;
    List<String> getSelectedSpecialityValueList = new ArrayList<String>();
    int count;
    static Map<String, List<String>> map = new HashMap<String, List<String>>();

    ApiBaseUrl baseUrl;

    ProgressDialog progressDialog;


    static String mycomment,mypatientId,myemailId,myaddressId,myContactPerson,mymobileNumber,myaddress1,
            mycityName,mystateName, mypincode,myAppointmentDate,myAadhaarNumber,mydiagnosticId,mycenterName;

    static Long mystateId,mycityId;
    boolean myPatientSameUser,myEnableHistory,myHomeSample,myAcknowledement,mycash_on_hand,mydebit_card ,mynet_banking,mypay_paytm;

    EditText contactPerson,mobileNumber,emailId,aadhaarNumber,address,pincode,comments;

    TextView CenterName,Address,City,State,availableTimings,MobileNumber,PaymentMode,Navigation;
    ImageView prescription,center_image;
    FloatingActionButton addPrescriptionGalleryFloatingButton,addPrescriptionCameraFloatingButton;
    Bitmap mIcon11;
    static String encodedPrescriptionImage = null;
    Uri selectedImageUri ;
    Bitmap selectedImageBitmap = null;
    final int REQUEST_CODE_GALLERY1 = 999, REQUEST_CODE_GALLERY2 = 1;

    SearchableSpinner city,state;
    List<String> citiesList,statesList;
    ArrayAdapter<String > stateAdapter,cityAdapter;
    HashMap<Long, String> myCitiesList = new HashMap<Long, String>();
    HashMap<Long, String> myStatesList = new HashMap<Long, String>();

    RippleView rippleView,rippleView1;


    String mydiagaddress, mydiagcity,mydiagmobile, mydiagStateName,mydiagLongitude,mydiagLatitude,
            mydiagCenterImage,fromTime,toTime, smsUrl = null,myPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_book_appointment_to_diagnostics);

        baseUrl = new ApiBaseUrl();

//        CenterName,Address,City,State,Fee,MobileNumber,PaymentMode,Navigation

        CenterName = (TextView)findViewById(R.id.diagnosticCenter_name);
        Address = (TextView)findViewById(R.id.dr_no);
        City = (TextView)findViewById(R.id.city);
        State = (TextView)findViewById(R.id.state);
        availableTimings = (TextView)findViewById(R.id.availableTimings);
        MobileNumber = (TextView)findViewById(R.id.phonenum);
        PaymentMode = (TextView)findViewById(R.id.payment);
        Navigation = (TextView)findViewById(R.id.navigation);
        prescription = (ImageView) findViewById(R.id.prescription);
        center_image = (ImageView) findViewById(R.id.center_image);


        MobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phn = MobileNumber.getText().toString();

                System.out.println("phone no in diag..."+phn);

                Intent callintent = new Intent(Intent.ACTION_CALL);
                callintent.setData(Uri.parse("tel:"+phn));
                if (ActivityCompat.checkSelfPermission(PatientBookAppointmentToDiagnostics.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callintent);
            }
        });

        addPrescriptionGalleryFloatingButton = (FloatingActionButton) findViewById(R.id.galleryIcon);
        addPrescriptionCameraFloatingButton = (FloatingActionButton) findViewById(R.id.cameraIcon);

        addPrescriptionGalleryFloatingButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(
                                PatientBookAppointmentToDiagnostics.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_GALLERY1
                        );

                    }
                });

        addPrescriptionCameraFloatingButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, MY_CAMERA_REQUEST_CODE);
                        }
                    }
                });

        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }

        mydiagnosticId = getIntent().getStringExtra("diagid");
        myaddressId = getIntent().getStringExtra("addressId");
        mycenterName = getIntent().getStringExtra("centerName");
        mypatientId = getIntent().getStringExtra("patientId");

        CenterName.setText(mycenterName);

        new GetAllCities().execute(baseUrl.getUrl()+"GetAllCity");

        new GetAllStates().execute(baseUrl.getUrl()+"GetAllState");

        new GetDiagnosticDetails().execute(baseUrl.getUrl()+"DiagnosticByID"+"?id="+mydiagnosticId);

        new GetDiagnosticCenterbyAddressIDDetails().execute(baseUrl.getUrl()+"DiagnosticCenterbyAdressByID?AddressID="+myaddressId);


        new GetPatientDetails().execute(baseUrl.getUrl()+"GetPatientByID"+"?ID="+mypatientId);

        System.out.println("patientId in patient diagbook.."+mypatientId);
        System.out.println("diagid in patient diagbook.."+mydiagnosticId);
        System.out.println("addrId in patient diagbook.."+myaddressId);
        System.out.println("centername in patient diagbook.."+mycenterName);


        Navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object latitude = mydiagLatitude;
                Object longitude = mydiagLongitude;
                String uri = String.format(Locale.ENGLISH, "geo:0,0?q="+mydiagLatitude+","+mydiagLongitude+"("+mycenterName+")", latitude, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        calendarView=(com.prolificinteractive.materialcalendarview.MaterialCalendarView ) findViewById(R.id.calendar);
        calenderlayout=(RelativeLayout)findViewById(R.id.calenderlayout);
        imagelayout=(RelativeLayout)findViewById(R.id.uploadlayout);
        mainlayout=(RelativeLayout) findViewById(R.id.rellay1);
        addresslayout=(RelativeLayout)findViewById(R.id.addresslayout);
        // button=(Button)findViewById(R.id.submit);
        rippleView=(RippleView) findViewById(R.id.rippleView);
        // submit=(Button) findViewById(R.id.submit1);
        rippleView1=(RippleView) findViewById(R.id.rippleView1);
        contactPerson = (EditText) findViewById(R.id.contact_person);
        mobileNumber = (EditText) findViewById(R.id.mobilenumber);
        emailId = (EditText) findViewById(R.id.email);
        aadhaarNumber = (EditText) findViewById(R.id.aadhaarNumber);
        address = (EditText) findViewById(R.id.Address);
        pincode = (EditText) findViewById(R.id.Pincode);
        city = (SearchableSpinner) findViewById(R.id.citySpinner);
        state = (SearchableSpinner) findViewById(R.id.stateSpinner);

        new GetDiagnosticsAllAddressDetails().execute(baseUrl.getUrl()+"DiagnosticGetAllAddress?ID="+mydiagnosticId);

        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prescription.getDrawable()==null)
                {

                    shoalertdialog();

                }
                else {
                    showpopup();
                }

            }
        });

        new GetPreviousSpecialityBasedonAddressID().execute(baseUrl.getUrl()+"DiagnosticGetAddressByID?ID="+mydiagnosticId+"&AddressID="+myaddressId);

        Calendar cal=Calendar.getInstance();

        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH)+1;
        day=cal.get(Calendar.DAY_OF_MONTH);

        if(month<10 && day<10)
        {
            date="0"+month+"/"+"0"+day+"/"+year;
        }

        else
        {
            date=month+"/"+day+"/"+year;
        }

        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CalendarDay day = CalendarDay.from(date1);
        DateDecorator dateDecorator=new DateDecorator(this,day);

        calendarView.addDecorator((DayViewDecorator) dateDecorator);
        calendarView.setDateSelected(cal.getTime(),true);


        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                int month = date.getMonth() + 1;
                int day = date.getDay();
                int year = date.getYear();

                System.out.println("year.."+year+"...month.."+month+"...day.."+day);

                if (day ==10 && month <10) {
                    myAppointmentDate = year + "-" + "0" + month + "-" + day;
                }

                else if (day <10 && month < 10) {
                    myAppointmentDate = year + "-" + "0" + month + "-" + "0" + day;
                }
                else if(day <10 && month >10)
                {
                    myAppointmentDate = year + "-" + month + "-" + "0" + day;
                }
                else if(day >10 && month <10)
                {
                    myAppointmentDate = year + "-" +"0"+month + "-" + day;
                }
                else {
                    myAppointmentDate = year + "-" + month + "-" + day;
                }

                System.out.println("selected date in diag book..." + myAppointmentDate);
                showalert();
            }
        });
    }

    private void shoalertdialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(PatientBookAppointmentToDiagnostics.this);
        builder.setTitle("you have to upload prescription");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
    }


    //get diagnostic details based on id from api call

    private class GetDiagnosticDetails extends AsyncTask<String, Void, String> {

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Create a progressdialog
//            progressDialog = new ProgressDialog(PatientBookAppointmentToDiagnostics.this);
//            // Set progressdialog title
////            progressDialog.setTitle("Your searching process is");
//            // Set progressdialog message
//            progressDialog.setMessage("Loading...");
//
//            progressDialog.setIndeterminate(false);
//            // Show progressdialog
//            progressDialog.show();
//        }

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
            getDiagProfileDetails(result);
        }

    }

    private void getDiagProfileDetails(String result) {
        try
        {
            JSONObject js = new JSONObject(result);

            mycash_on_hand     =  (boolean) js.get("CashOnHand");
            mydebit_card       =  (boolean) js.get("CreditDebit");
            mynet_banking      =  (boolean) js.get("Netbanking");
            mypay_paytm         =   (boolean) js.get("Paytm");

            if(mycash_on_hand == true && mydebit_card == true || mynet_banking ==true ||  mypay_paytm == true)
            {
                myPayment = "Cash on Hand "+","+"Online Banking";
            }

            else if(mycash_on_hand == true)
            {
                myPayment = "Cash on Hand ";
            }

            else
            {
                myPayment = "-";
            }

            PaymentMode.setText(myPayment);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
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


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY1) {
//            onSelectFromGalleryResult(data);
//             Make sure the request was successful
            Log.d("hello","I'm out.");
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {

                selectedImageUri = data.getData();
                BufferedWriter out=null;
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                    final InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] b = baos.toByteArray();
                    encodedPrescriptionImage = Base64.encodeToString(b, Base64.DEFAULT);


                }
                catch (IOException e)
                {
                    System.out.println("Exception ");

                }
                prescription.setImageBitmap(selectedImageBitmap);
                Log.d("hello","I'm in.");

            }
        }

        else if(requestCode == MY_CAMERA_REQUEST_CODE)
        {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            prescription.setImageBitmap(thumbnail);
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Get diagnostic center by addressID details from api call
    private class GetDiagnosticCenterbyAddressIDDetails extends AsyncTask<String, Void, String> {

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
            getCenterData(result);
        }
    }

    private void getCenterData(String result) {
        try {

            JSONObject object = new JSONObject(result);
            mydiagCenterImage = object.getString("CenterImage");
            fromTime = object.getString("FromTime");
            toTime = object.getString("ToTime");
            availableTimings.setText(fromTime+" AM - "+toTime+" PM");
            new GetImageTask(center_image).execute(baseUrl.getImageUrl()+mydiagCenterImage);

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Get cities list from api call
    private class GetAllCities extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result  cities ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getCities(result);

        }
    }

    private void getCities(String result) {
        try
        {

            JSONArray jsonArr = new JSONArray(result);
            citiesList = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++) {

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                Long cityKey = jsonObj.getLong("Key");
                String cityValue = jsonObj.getString("Value");
                myCitiesList.put(cityKey,cityValue);
                citiesList.add(jsonObj.getString("Value"));
                System.out.print("mycity list.."+myCitiesList);
                System.out.print("city list.."+citiesList);
            }

        }
        catch (JSONException e)
        {}
    }

    //Get states list from api call
    private class GetAllStates extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result  states  ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getStates(result);
        }
    }

    private void getStates(String result) {
        try
        {
            JSONArray jsonArr = new JSONArray(result);
            statesList = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++) {

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                Long stateKey = jsonObj.getLong("Key");
                String stateValue = jsonObj.getString("Value");
                myStatesList.put(stateKey,stateValue);
                statesList.add(jsonObj.getString("Value"));
            }
        }
        catch (JSONException e)
        {}
    }


    //Get previous Speciality list from api call
    public class GetPreviousSpecialityBasedonAddressID extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result prev spec", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getPreviousSpeciality(result);

        }
    }

    private void getPreviousSpeciality(String result) {
        try
        {
            JSONObject js = new JSONObject(result);

            if(js.has("SpecialityLst"))
            {
                JSONArray jsonArr = new JSONArray(js.getString("SpecialityLst"));

                DiagSpecialityItemsList = new ArrayList<>();

                System.out.println("inside try");

                for (int i = 0; i < jsonArr.length(); i++) {

                    System.out.println("inside for");

                    org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                    String specialityId = jsonObj.getString("SpecialityID");

                    String specialityValue = jsonObj.getString("Speciality");

                    DiagSpecialityItemsList.add(specialityValue);

                    String[] stockArr = new String[DiagSpecialityItemsList.size()];
                    AllDiagSpecialityList.put(specialityId,specialityValue);

                    allDiagSpecialityItems = DiagSpecialityItemsList.toArray(stockArr);

                    checkDiagSpecialityItems = new boolean[allDiagSpecialityItems.length];

                }
            }
            else
            {

            }

            System.out.println("diag specialities list in patient bookApp.."+DiagSpecialityItemsList);

            System.out.println("all diag specialities list in patient bookApp.."+allDiagSpecialityItems);

        }
        catch (JSONException e)
        {}
    }

    private void showalert() {

        getSelectedSpecialityItemsList = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(PatientBookAppointmentToDiagnostics.this);
        builder.setTitle("Required tests");

        builder.setMultiChoiceItems(allDiagSpecialityItems, checkDiagSpecialityItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    String i = Integer.toString(position);
                    getSelectedSpecialityValueList.add(i);
                }else{
                    getSelectedSpecialityValueList.remove((Integer.valueOf(position)));
                }
            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                String item = "";
                for (int i = 0; i <  getSelectedSpecialityValueList.size(); i++) {
                    item = item + allDiagSpecialityItems[Integer.parseInt(getSelectedSpecialityValueList.get(i))];
                    if (i != getSelectedSpecialityValueList.size() - 1) {
                        item = item + ",";
                        count ++;
                    }
                }

                System.out.println("count.."+count);
                getSelectedSpecialityItemsList.add(item);
                System.out.println("size of spect list..."+getSelectedSpecialityItemsList.size());

                map.put("0", getSelectedSpecialityItemsList);

//                MyDialog.cancel();

                calenderlayout.setVisibility(View.INVISIBLE);
                mainlayout.setPadding(0,50,0,0);
                mainlayout.setVisibility(View.VISIBLE);
                imagelayout.setVisibility(View.VISIBLE);

            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog mDialog1 = builder.create();
        mDialog1.show();

    }

    private void showpopup()
    {

        MyDialog = new Dialog(PatientBookAppointmentToDiagnostics.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.commentspopup);
        MyDialog.show();

        checkPatientSameUser=(CheckBox)MyDialog.findViewById(R.id.address);
        checkhomeSample=(CheckBox)MyDialog.findViewById(R.id.sample);
        comments=(EditText)MyDialog.findViewById(R.id.Comments);

        cancel = (TextView) MyDialog.findViewById(R.id.cancel);
        ok=(TextView)  MyDialog.findViewById(R.id.ok);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Addresspopup();
                MyDialog.cancel();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Addresspopup();
                MyDialog.cancel();

            }
        });
    }


    private void Addresspopup() {

        imagelayout.setVisibility(View.INVISIBLE);
        addresslayout.setVisibility(View.VISIBLE);


        if(checkhomeSample.isChecked())
        {
            myHomeSample = true;
        }
        else if(!checkhomeSample.isChecked())
        {
            myHomeSample = false;
        }

        if(checkPatientSameUser.isChecked()==true)
        {
            myPatientSameUser = false;
            contactPerson.setText("");

            mobileNumber.setText("");
            emailId.setText("");
            aadhaarNumber.setText("");
            address.setText("");
            pincode.setText("");

            stateAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, statesList);
            stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
            state.setAdapter(stateAdapter); // Apply the adapter to the state spinner

            cityAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, citiesList);
            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
            city.setAdapter(cityAdapter); // Apply the adapter to the spinner
        }

        else if(checkPatientSameUser.isChecked()==false)
        {
            myPatientSameUser = true;
            contactPerson.setText(myContactPerson);
            mobileNumber.setText(mymobileNumber);
            emailId.setText(myemailId);
            aadhaarNumber.setText(myAadhaarNumber);
            address.setText(myaddress1);
            pincode.setText(mypincode);

            long lng = mystateId;
            int i = (int) lng;
            String getStateName = String.valueOf(statesList.get(i));
            System.out.println("state name.."+getStateName);

            statesList.add(0,getStateName);
            stateAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, statesList);
            stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
            state.setAdapter(stateAdapter); // Apply the adapter to the spinner

            String getCityName = String.valueOf(myCitiesList.get(mycityId));
            System.out.println("get city name.."+getCityName);

            citiesList.add(0,getCityName);
            cityAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, citiesList);
            cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
            city.setAdapter(cityAdapter); // Apply the adapter to the spinner

        }

        rippleView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
                MyDialog.cancel();
            }
        });

    }
    public void validation()
    {
        intialization();
        if(!validate())
        {
            Toast.makeText(this,"Please enter above fields" , Toast.LENGTH_SHORT).show();
        }
        else
        {
            acknowledgepopup();
        }
    }

    public void intialization()
    {
        myContactPerson = contactPerson.getText().toString();
        mymobileNumber = mobileNumber.getText().toString();
        myaddress1 = address.getText().toString();
        mystateName = state.getSelectedItem().toString();
        mycityName = city.getSelectedItem().toString();
        mypincode = pincode.getText().toString();
        myAadhaarNumber = aadhaarNumber.getText().toString();

    }

    public boolean validate()
    {
        boolean validate = true;

        if(mymobileNumber.isEmpty() || !Patterns.PHONE.matcher(mymobileNumber).matches())
        {
            mobileNumber.setError("please enter the mobile number");
            validate=false;
        }

        else if(mymobileNumber.length()<10 || mymobileNumber.length()>10)
        {
            mobileNumber.setError(" Invalid mobile number ");
            validate=false;
        }

        if(myContactPerson.isEmpty())
        {
            contactPerson.setError("please enter contact person");
            validate=false;
        }

        if(myaddress1.isEmpty())
        {
            address.setError("please enter address");
            validate=false;
        }
        if(mypincode.isEmpty())
        {
            pincode.setError("please enter pincode");
            validate=false;
        }
//        if(mystateName.isEmpty())
//        {
//            state.setError("please enter aadhaar number");
//            validate=false;
//        }


//        if(mycityName.isEmpty())
//        {
//            city.setError("please enter reason");
//            validate=false;
//        }
        return validate;
    }
    private void acknowledgepopup() {

        acknowledgedialog = new Dialog(PatientBookAppointmentToDiagnostics.this);
        acknowledgedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        acknowledgedialog.setContentView(R.layout.acknowledgepopup);
        acknowledgedialog.show();

        checkEnableHistory=(CheckBox)acknowledgedialog.findViewById(R.id.enableHistory);
        checkAcknowledge=(CheckBox)acknowledgedialog.findViewById(R.id.acknowledge);

        cancel1 = (TextView) acknowledgedialog.findViewById(R.id.cancel);
        ok1=(TextView)  acknowledgedialog.findViewById(R.id.ok);
        cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acknowledgedialog.cancel();
            }
        });
        ok1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String js = formatDataAsJson();
                new sendAppointmentDetailsToDiagnostics().execute(baseUrl.getUrl()+"RequestDiagnostic",js.toString());
                acknowledgedialog.cancel();
                System.out.println("json data in diagbook..."+js.toString());
            }
        });
    }

    private String formatDataAsJson()
    {
        JSONObject data = new JSONObject();

//            "Comments":"good", "PatientID":1063,"EmailID":"sudarsanakapoor143@gmail.com", "AddressID":"2282",
//            "HomeSample":"true", "ContactPerson":"gayathri", "MobileNo":"8341495888", "Address1":"kakinada",
//            "CityID":1, "StateID":1, "PinCode":"5330033", "PatientSameUser":"true", "EnableHistory":"true",
//            "AppointmentDatestring":"2018-05-04", "Aadharnumber":"996065871092", "Prescription":"", "TestID":1

        mycomment = comments.getText().toString().trim();
        myemailId = emailId.getText().toString();
        myContactPerson = contactPerson.getText().toString();
        mymobileNumber = mobileNumber.getText().toString();
        myaddress1 = address.getText().toString();
        mystateName = state.getSelectedItem().toString();
        mycityName = city.getSelectedItem().toString();
        mypincode = pincode.getText().toString();
        myAadhaarNumber = aadhaarNumber.getText().toString();


        if(checkhomeSample.isChecked())
        {
            myHomeSample = true;
        }

        else if(!checkhomeSample.isChecked())
        {
            myHomeSample = false;
        }

        if(checkPatientSameUser.isChecked())
        {
            myPatientSameUser = true;
        }

        else if(!checkPatientSameUser.isChecked())
        {
            myPatientSameUser = false;
        }

        if(checkEnableHistory.isChecked())
        {
            myEnableHistory = true;
        }
        else if(!checkEnableHistory.isChecked())
        {
            myEnableHistory = false;
        }

        if(checkAcknowledge.isChecked())
        {
            myAcknowledement = true;
        }

        else if(!checkAcknowledge.isChecked())
        {
            myAcknowledement = false;
        }


        System.out.println("history.."+myEnableHistory);
        System.out.println("homesample.."+myHomeSample);
        System.out.println("patientsameuser.."+myPatientSameUser);
        System.out.println("acknowledgement.."+myAcknowledement);

        if(encodedPrescriptionImage == null)
        {
            prescription.buildDrawingCache();
            BitmapDrawable bitmapDrawable = (BitmapDrawable) prescription.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos1);
            byte[] b1 = baos1.toByteArray();
            encodedPrescriptionImage = Base64.encodeToString(b1, Base64.DEFAULT);

            System.out.println("image view encoded Image..."+encodedPrescriptionImage);

        }

        System.out.println("image from gallery encoded Image..."+encodedPrescriptionImage);

        try{

            StringBuilder stringBuilder = new StringBuilder();

            for (Map.Entry<String, List<String>> entry : map.entrySet()) {

                String key = entry.getKey();
                List<String> values = entry.getValue();

                List mylistP = new ArrayList<>();
                mylistP.addAll(values);

                int i = 0;

                System.out.println("map "+getSelectedSpecialityItemsList.toString());

                System.out.println("map values..."+map);

                String a[] = new String[getSelectedSpecialityItemsList.size()];

                System.out.println("length.... "+a.length);

                //Loop index size()

                for(int index = 0; index < a.length; index++) {

                    String lis = values.get(i);
                    a = lis.split(",");
                    List mylist = new ArrayList<>();
                    mylist.addAll(Arrays.asList(a));

                    String specKey = (String) getSpecialityKeyFromValue(AllDiagSpecialityList,mylist.get(index));

                    System.out.println("spec key idss.... "+specKey);

                    stringBuilder.append(specKey+",");

                }
                System.out.println("spec idss.for.. "+stringBuilder.toString());
            }

            System.out.println("spec idss. outside map.. "+stringBuilder.toString());

            data.put("Comments",comments.getText().toString().trim());
            data.put("PatientID",mypatientId);
            data.put("EmailID", emailId.getText().toString());
            data.put("AddressID",myaddressId);
            data.put("HomeSample",myHomeSample);
            data.put("ContactPerson",contactPerson.getText().toString());
            data.put("MobileNo", mymobileNumber);
            data.put("Address1",new String(myaddress1));

            data.put("CityID",getCityKeyFromValue(myCitiesList,mycityName));
            data.put("StateID",getStateKeyFromValue(myStatesList,mystateName));
            data.put("PinCode",pincode.getText().toString());
            data.put("PatientSameUser", myPatientSameUser);
            data.put("EnableHistory",myEnableHistory);
            data.put("AppointmentDatestring", myAppointmentDate);
            data.put("Aadharnumber", aadhaarNumber.getText().toString());
            data.put("Prescription",encodedPrescriptionImage);
            data.put("TestID", stringBuilder.toString());

            return data.toString();

        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }

    //send appointment details for booking diagnostics to api call

    private class sendAppointmentDetailsToDiagnostics extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(PatientBookAppointmentToDiagnostics.this);
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

            Log.e("TAG result diag book   ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
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

    public void showSuccessMessage(String message){

        android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();

                        new Mytask().execute();
                        Intent intent = new Intent(PatientBookAppointmentToDiagnostics.this,PatientDashBoard.class);
                        intent.putExtra("id",mypatientId);
                        startActivity(intent);
                    }
                });
        android.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Your Diagnostic Appointment");
        alert.show();
    }

    public void showErrorMessage(String message){

        android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Edit Profile");
        alert.show();

    }


    public static Object getSpecialityKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    public static Object getCityKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    public static Object getStateKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    //Get patient list based on id from api call
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
            getProfileDetails(result);

        }
    }

    private void getProfileDetails(String result) {

        try
        {
            JSONObject js = new JSONObject(result);

//                myContactPerson = (String) js.get("FirstName")+" "+(String) js.get("LastName");///wrong data from api

            myContactPerson = "Sudarsana Rao";

            mymobileNumber = (String) js.get("MobileNumber");
            myemailId = (String) js.get("EmailID");
            myaddress1 = (String) js.get("Address1")+","+(String) js.get("Address2");
            myAadhaarNumber = (String) js.get("AadharNumber");

            mystateId = js.getLong("State");
            mycityId = js.getLong("City");
            mypincode = (String) js.get("ZipCode");

            System.out.println("city key.."+mycityId);
            System.out.println("state key.."+mystateId);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    ////Patient booking appointment to diagnostics////

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


                String username=myContactPerson;
                String centername=mycenterName;
                String address=mydiagaddress+","+mydiagcity;
                String diagmobilenum = mydiagmobile;

                String message="Hi "+username+", You have successfully sent the prescription to "+centername+" at "+address+"Please call" +diagmobilenum+ "for any assistance"+". Thank You."+" Click here to navigate:"+baseUrl.getLink();

                String uname="MedicTr";
                String password="X!g@c$R2";
                String sender="MEDICC";
                String destination = mymobileNumber;
                smsUrl = baseUrl.getSmsUrl();

                String encode_message= URLEncoder.encode(message, "UTF-8");
                StringBuilder stringBuilder=new StringBuilder(baseUrl.getSmsUrl());
                stringBuilder.append("uname="+URLEncoder.encode(uname, "UTF-8"));
                stringBuilder.append("&pass="+URLEncoder.encode(password, "UTF-8"));
                stringBuilder.append("&send="+URLEncoder.encode(sender, "UTF-8"));
                stringBuilder.append("&dest="+URLEncoder.encode(destination, "UTF-8"));
                stringBuilder.append("&msg="+encode_message);

                smsUrl=stringBuilder.toString();
                System.out.println("mainurl "+smsUrl);
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

    //Get all addresses for diagnostics list from api call
    private class GetDiagnosticsAllAddressDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(PatientBookAppointmentToDiagnostics.this);
            // Set progressdialog title
//            progressDialog.setTitle("You are logging");
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
            progressDialog.dismiss();
            Log.e("Api response.....", result);

            getData(result);

        }
    }

    private void getData(String result) {

        try {

            JSONArray jarray = new JSONArray(result);

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);


                String addressId = object.getString("AddressID");

                System.out.println("addrid.."+addressId);

                if(addressId.equals(myaddressId))
                {
                    System.out.println("if cond");
                    mydiagaddress = object.getString("Address1");
                    mydiagcity = object.getString("CityName");
                    mydiagmobile = object.getString("MobileNumber");
                    mydiagStateName = myStatesList.get(object.get("StateID"));
                    mydiagLatitude = object.getString("Latitude");
                    mydiagLongitude = object.getString("Longitude");

                    Address.setText(mydiagaddress);
                    State.setText(mydiagStateName);
                    City.setText(mydiagcity);
                    MobileNumber.setText(mydiagmobile);



                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private class GetImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public GetImageTask(ImageView bmImage) {
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
            center_image.setImageBitmap(result);
        }

    }

}
