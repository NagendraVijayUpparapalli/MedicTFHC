package com.example.cool.patient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientBookAppointmentToDoctor extends AppCompatActivity {
    TextView doctorname,hospitalname,doornum,city,state,fee,payment,doctorphonenum,navigaton;
    EditText appointmentdate,patientname,age,patientmobileno,mail,aadharnum,reason;
    Button button;
    Spinner timings;
    ImageView doctorimage;

    AlertDialog alertDialog1;
    ProgressDialog progressDialog;

    int year1,month,day;


    static String newName,mySurname,myName,myEmail,myMobile,myGender,myMaritalStatus,myAadhar_num;
    Long myAge;
    boolean enableHistory;


    String user,cur_addressId,doctorId,mydocName,myhospitalName,myaddress,mycity,mystate,myfee,mypaymentMode,myphone,myLati,myLongi,myImage;

    ApiBaseUrl baseUrl;

    List<String> allItemsList, prevSunTimeSlotsList,prevMonTimeSlotsList,prevTueTimeSlotsList,prevWedTimeSlotsList,prevThurTimeSlotsList,prevFriTimeSlotsList,prevSatTimeSlotsList;
    ArrayAdapter<String > sunAdapter,monAdapter,tueAdapter,wedAdapter,thurAdapter,friAdapter,satAdapter;
    String myDayName;
    int getUserId,patientId;

    HashMap<String, String> AllTimeSlotsList = new HashMap<String, String>();

    static String smsUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_book_appointment_to_doctor);

        baseUrl = new ApiBaseUrl();

        mydocName = getIntent().getStringExtra("doctorName");
        user = getIntent().getStringExtra("user");
        patientId = getIntent().getIntExtra("userId",patientId);
        cur_addressId = getIntent().getStringExtra("addressId");
        doctorId = getIntent().getStringExtra("doctorId");
        myLati = getIntent().getStringExtra("lat");
        myLongi = getIntent().getStringExtra("long");
        myfee = getIntent().getStringExtra("fee");
        myphone = getIntent().getStringExtra("mobile");

//        InsertDocAppointment

        new GetTimeSlots().execute(baseUrl.getUrl()+"GetAllTimeSlot");

        new GetDoctorAllAddressDetails().execute(baseUrl.getUrl()+"DoctorGetAllAddress?ID="+doctorId);

        doctorname=(TextView) findViewById(R.id.Doctor_name);
        hospitalname=(TextView) findViewById(R.id.hospital_name);
        doornum=(TextView) findViewById(R.id.dr_no);
        city=(TextView) findViewById(R.id.city);
        state=(TextView) findViewById(R.id.state);
        fee=(TextView) findViewById(R.id.fee);
        payment=(TextView) findViewById(R.id.payment);
        doctorphonenum=(TextView) findViewById(R.id.phonenum);
        navigaton=(TextView) findViewById(R.id.navigation);

        appointmentdate=(EditText)findViewById(R.id.bookDate);
        patientname=(EditText)findViewById(R.id.patient_name);
        age=(EditText)findViewById(R.id.patient_age);
        patientmobileno=(EditText)findViewById(R.id.mobilenumber);
        mail=(EditText)findViewById(R.id.email);
        aadharnum=(EditText)findViewById(R.id.aadhaarNumber);
        reason=(EditText)findViewById(R.id.reason_for_Appointment);

        if(user.equals("Yes"))
        {
            getUserId = getIntent().getIntExtra("userId",getUserId);
            System.out.print("userid in patient book doc....."+getUserId);
            new GetPatientDetails().execute(baseUrl.getUrl()+"GetPatientByID"+"?ID="+getUserId);
        }

        timings=(Spinner)findViewById(R.id.timings);

        button=(Button)findViewById(R.id.submit);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                showalert();
            }
        });

//        appointmentdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new GetAllTimingsBasedOnAddressId().execute(baseUrl.getUrl()+"GetAllTimeSlotbyAddressid?AddresID="+cur_addressId);
//
//
//            }
//        });

        doctorname.setText(mydocName);
        fee.setText(myfee);
        doctorphonenum.setText(myphone);


        System.out.println("docname.."+mydocName);
        System.out.println("addres id.."+cur_addressId);

        System.out.println("fee.."+myfee);
        System.out.println("phone.."+myphone);

        Calendar cal=Calendar.getInstance();
        year1=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH);
        day=cal.get(Calendar.DAY_OF_MONTH);
        int dayName = cal.DAY_OF_WEEK;

        appointmentdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(PatientBookAppointmentToDoctor.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        monthOfYear = monthOfYear;


                        String datte =dayOfMonth+"/"+monthOfYear+"/"+year;
//
                        new GetAllTimingsBasedOnAddressId().execute(baseUrl.getUrl()+"GetAllTimeSlotbyAddressid?AddresID="+cur_addressId);
//
                        SimpleDateFormat simpleDateformat = new SimpleDateFormat("dd/mm/yyyy"); // the day of the week abbreviated
                        try {
                            Date mtdat = simpleDateformat.parse(datte);
                            DateFormat dateFormat = new SimpleDateFormat("E");
                            myDayName = dateFormat.format(mtdat);
                            System.out.println("day name value.."+myDayName);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

//                        System.out.println("day id cal.."+dayOfWeek);

                        String myselecteddate = null;
                        if(monthOfYear+1<=9)
                        {
                            int mm = monthOfYear+1;
                            myselecteddate =year+"-"+0+mm+"-"+dayOfMonth;
                        }
                        else if(monthOfYear+1>9)
                        {
                            int mm = monthOfYear+1;
                            myselecteddate =year+"-"+mm+"-"+dayOfMonth;
                        }

                        appointmentdate.setText(myselecteddate);

                    }
                }, year1,month,day);
                datePickerDialog.show();


            }
        });

    }


    //Get timeslots list from api call
    private class GetTimeSlots extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result  states  ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getTimeSlots(result);
        }
    }

    private void getTimeSlots(String result) {
        try
        {
            JSONArray jsonArr = new JSONArray(result);

            allItemsList = new ArrayList<>();

            for (int i = 0; i < jsonArr.length(); i++) {

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                String timeId = jsonObj.getString("TsID");

                String timeValue = jsonObj.getString("TimeSlots");
                AllTimeSlotsList.put(timeId,timeValue);

            }
        }
        catch (JSONException e)
        {}
    }

    public static Object getTimeKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }



    //send appointment details for booking doctor from api call

    private class sendAppointmentDetailsToDoctor extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(PatientBookAppointmentToDoctor.this);
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

            Log.e("TAG result current   ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
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

//                        new Mytask().execute();
                        Intent intent = new Intent(PatientBookAppointmentToDoctor.this,MainActivity.class);
//                        intent.putExtra("id",getUserId);
                        startActivity(intent);
                    }
                });
        android.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Your Doctor Appointment");
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

    private String formatDataAsJson()
    {
        JSONObject data = new JSONObject();

        System.out.println("history.."+enableHistory);

        try{
            if(user.equals("Yes") && enableHistory==true)
            {
                data.put("ReasonAppointments",reason.getText().toString());
                data.put("AddressID",cur_addressId);
                data.put("PatientID", getUserId);
                data.put("TimeSlots",getTimeKeyFromValue(AllTimeSlotsList,timings.getSelectedItem().toString()));
//                data.put("Address1","");
                data.put("Aadharnumber",aadharnum.getText().toString());
                data.put("AppointmentDatestring",appointmentdate.getText().toString());
                data.put("PatientSameUser", true);
                data.put("PatientName", patientname.getText().toString());

                data.put("Age",age.getText().toString());
                data.put("MobileNo",patientmobileno.getText().toString());
                data.put("EmailID", mail.getText().toString());
                data.put("EnableHistory", true);
                return data.toString();
            }

            else if(user.equals("Yes") && enableHistory==false)
            {
                data.put("ReasonAppointments",reason.getText().toString());
                data.put("AddressID",cur_addressId);
                data.put("PatientID", getUserId);
                data.put("TimeSlots",getTimeKeyFromValue(AllTimeSlotsList,timings.getSelectedItem().toString()));
//                data.put("Address1","");
                data.put("Aadharnumber",aadharnum.getText().toString());
                data.put("AppointmentDatestring",appointmentdate.getText().toString());
                data.put("PatientSameUser", true);
                data.put("PatientName", patientname.getText().toString());

                data.put("Age",age.getText().toString());
                data.put("MobileNo",patientmobileno.getText().toString());
                data.put("EmailID", mail.getText().toString());
                data.put("EnableHistory", false);
                return data.toString();
            }
            else if(user.equals("No") && enableHistory==true)
            {
                data.put("ReasonAppointments",reason.getText().toString());
                data.put("AddressID",cur_addressId);
                data.put("PatientID", getUserId);
                data.put("TimeSlots",getTimeKeyFromValue(AllTimeSlotsList,timings.getSelectedItem().toString()));
//                data.put("Address1","");
                data.put("Aadharnumber",aadharnum.getText().toString());
                data.put("AppointmentDatestring",appointmentdate.getText().toString());
                data.put("PatientSameUser", false);
                data.put("PatientName", patientname.getText().toString());

                data.put("Age",age.getText().toString());
                data.put("MobileNo",patientmobileno.getText().toString());
                data.put("EmailID", mail.getText().toString());
                data.put("EnableHistory", true);
                return data.toString();
            }

            else if(user.equals("No") && enableHistory==false)
            {
                data.put("ReasonAppointments",reason.getText().toString());
                data.put("AddressID",cur_addressId);
                data.put("PatientID", getUserId);
                data.put("TimeSlots",getTimeKeyFromValue(AllTimeSlotsList,timings.getSelectedItem().toString()));
//                data.put("Address1","");
                data.put("Aadharnumber",aadharnum.getText().toString());
                data.put("AppointmentDatestring",appointmentdate.getText().toString());
                data.put("PatientSameUser", false);
                data.put("PatientName", patientname.getText().toString());

                data.put("Age",age.getText().toString());
                data.put("MobileNo",patientmobileno.getText().toString());
                data.put("EmailID", mail.getText().toString());
                data.put("EnableHistory", false);
                return data.toString();
            }
        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }

    private void showalert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(PatientBookAppointmentToDoctor.this);
        builder.setMessage("Do you want enable this appointment in your history?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                enableHistory = true;
                String js = formatDataAsJson();
                new sendAppointmentDetailsToDoctor().execute(baseUrl.getUrl()+"InsertDocAppointment",js.toString());
                dialogInterface.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enableHistory = false;
                String js = formatDataAsJson();
                new sendAppointmentDetailsToDoctor().execute(baseUrl.getUrl()+"InsertDocAppointment",js.toString());
                dialogInterface.cancel();
            }
        });
        builder.show();
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

            myName = (String) js.get("FirstName");
            mySurname = (String) js.get("LastName");
            myMobile = (String) js.get("MobileNumber");
            myEmail = (String) js.get("EmailID");
            myGender = (String) js.get("Gender");
            myMaritalStatus = (String) js.get("MaritalStatus");
            myAadhar_num = (String) js.get("AadharNumber");

            myAge = js.getLong("Age");

            if(myGender.equals("Male"))
            {
                newName = "Mr.";
            }
            else if(myGender.equals("Female") && myMaritalStatus.equals("Single") )
            {
                newName = "Ms.";
            }
            else if(myGender.equals("Female") && myMaritalStatus.equals("Married") )
            {
                newName = "Mrs.";
            }

            patientname.setText(myName+" "+mySurname);
            patientmobileno.setText(myMobile);
            mail.setText(myEmail);
            age.setText(myAge.toString());
            aadharnum.setText(myAadhar_num);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }


    //Get timings based on addressId from api call
    public class GetAllTimingsBasedOnAddressId extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(PatientBookAppointmentToDoctor.this);
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

            Log.e("TAG result prev timings", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getPreviousTiming(result);
            progressDialog.dismiss();

        }
    }

    private void getPreviousTiming(String result) {
        try
        {
            JSONArray jsonArr = new JSONArray(result);
            prevSunTimeSlotsList = new ArrayList<>();
            prevMonTimeSlotsList = new ArrayList<>();
            prevTueTimeSlotsList = new ArrayList<>();
            prevWedTimeSlotsList = new ArrayList<>();
            prevThurTimeSlotsList = new ArrayList<>();
            prevFriTimeSlotsList = new ArrayList<>();
            prevSatTimeSlotsList = new ArrayList<>();

            for (int i = 0; i < jsonArr.length(); i++) {

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                Long dayNameId = jsonObj.getLong("DayName");
                if(dayNameId==0 && myDayName.equals("Sat"))
                {
                    prevSunTimeSlotsList.add(jsonObj.getString("TimeSlots"));
                    sunAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, prevSunTimeSlotsList);
                    sunAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
                    timings.setAdapter(sunAdapter); // Apply the adapter to the spinner

                }

                if(dayNameId==1 && myDayName.equals("Sun"))
                {
                    prevMonTimeSlotsList.add(jsonObj.getString("TimeSlots"));
                    monAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, prevMonTimeSlotsList);
                    monAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
                    timings.setAdapter(monAdapter); // Apply the adapter to the spinner

                }

                if(dayNameId==2 && myDayName.equals("Mon"))
                {
                    prevTueTimeSlotsList.add(jsonObj.getString("TimeSlots"));
                    tueAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, prevTueTimeSlotsList);
                    tueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
                    timings.setAdapter(tueAdapter); // Apply the adapter to the spinner

                }

                if(dayNameId==3 && myDayName.equals("Tue"))
                {
                    prevWedTimeSlotsList.add(jsonObj.getString("TimeSlots"));
                    wedAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, prevWedTimeSlotsList);
                    wedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
                    timings.setAdapter(wedAdapter); // Apply the adapter to the spinner

                }

                if(dayNameId==4 && myDayName.equals("Wed"))
                {
                    prevThurTimeSlotsList.add(jsonObj.getString("TimeSlots"));
                    thurAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, prevThurTimeSlotsList);
                    thurAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
                    timings.setAdapter(thurAdapter); // Apply the adapter to the spinner

                }

                if(dayNameId==5 && myDayName.equals("Thu"))
                {
                    prevFriTimeSlotsList.add(jsonObj.getString("TimeSlots"));
                    friAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, prevFriTimeSlotsList);
                    friAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
                    timings.setAdapter(friAdapter); // Apply the adapter to the spinner

                }

                if(dayNameId==6 && myDayName.equals("Fri"))
                {
                    prevSatTimeSlotsList.add(jsonObj.getString("TimeSlots"));
                    satAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, prevSatTimeSlotsList);
                    satAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
                    timings.setAdapter(satAdapter); // Apply the adapter to the spinner

                }

            }

            System.out.println("sun prev.."+prevSunTimeSlotsList);
            System.out.println("mon prev.."+prevMonTimeSlotsList);
            System.out.println("tue prev.."+prevTueTimeSlotsList);
            System.out.println("wed prev.."+prevWedTimeSlotsList);
            System.out.println("thur prev.."+prevThurTimeSlotsList);
            System.out.println("fri prev.."+prevFriTimeSlotsList);
            System.out.println("sat prev.."+prevSatTimeSlotsList);
        }
        catch (JSONException e)
        {}
    }

    //Get all addresses for doctor list from api call
    private class GetDoctorAllAddressDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(PatientBookAppointmentToDoctor.this);
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

            getAllAddress(result);
            progressDialog.dismiss();
        }
    }

    private void getAllAddress(String result) {
        try {

            JSONArray jarray = new JSONArray(result);

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);


                String myaddressId = object.getString("AddressID");

                System.out.println("addrid.."+myaddressId);

                if(myaddressId.equals(cur_addressId))
                {
                    System.out.println("if cond");
                    myaddress = object.getString("Address1");
                    myhospitalName = object.getString("HospitalName");
                    mystate = object.getString("StateName");

                    mycity = object.getString("CityName");

                    hospitalname.setText(myhospitalName);
                    doornum.setText(myaddress);
                    city.setText(mycity);
                    state.setText(mystate);

                    break;
                }
//                else {
//                    continue;
//                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    ////Patient booking appointment successfull sms////

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


                String username = patientname.getText().toString();
                String doctorname = mydocName;
                String date = appointmentdate.getText().toString();
                String address = myaddress+","+mycity;
                String link="https://www.medictfhc.com/";

                String message="Hi "+username+", You have successfully sent an appointment request to Dr."+doctorname+"on"+date+"at"+address+".Thank You."+"Click here to Login:"+baseUrl.getLink();
                smsUrl = baseUrl.getSmsUrl();
                String uname="MedicTr";
                String password="X!g@c$R2";
                String sender="MEDICC";
                String destination=myMobile;

                String encode_message= URLEncoder.encode(message, "UTF-8");
                StringBuilder stringBuilder=new StringBuilder(smsUrl);
                stringBuilder.append("uname="+URLEncoder.encode(uname, "UTF-8"));
                stringBuilder.append("&pass="+URLEncoder.encode(password, "UTF-8"));
                stringBuilder.append("&send="+URLEncoder.encode(sender, "UTF-8"));
                stringBuilder.append("&dest="+URLEncoder.encode(destination, "UTF-8"));
                stringBuilder.append("&msg="+encode_message);

                smsUrl=stringBuilder.toString();
                System.out.println("smsUrl..."+smsUrl);
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
