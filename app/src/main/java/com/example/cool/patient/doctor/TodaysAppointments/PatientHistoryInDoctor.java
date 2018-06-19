package com.example.cool.patient.doctor.TodaysAppointments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cool.patient.common.ApiBaseUrl;
//import com.example.cool.patient.EventDecorator;
import com.example.cool.patient.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PatientHistoryInDoctor extends AppCompatActivity {

    MaterialCalendarView calendarView;
    ImageView img,doctorImage;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    String Aaadharnumber,Patientname,Mobilenumber,reason,doctorname,speciality,appoinmentdate,doctorcomment;

    String date,d;

    TextView count;

    String patientId, doctorImageUrl, doctorId,doctorMobile;
    ApiBaseUrl baseUrl;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_history_in_doctor);

        baseUrl = new ApiBaseUrl();

        calendarView=(com.prolificinteractive.materialcalendarview.MaterialCalendarView ) findViewById(R.id.calendar);

        img=(ImageView) findViewById(R.id.img1);

        doctorMobile = getIntent().getStringExtra("doctorMobile");
        doctorId = getIntent().getStringExtra("id");
        patientId = getIntent().getStringExtra("patientId");
        doctorImageUrl = getIntent().getStringExtra("doctorImageUrl");

        count=(TextView)findViewById(R.id.appointmentcount);
        new GetPatientDetails().execute(baseUrl.getUrl()+"DocPatientHistory"+"?PatientID="+patientId);



        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showyearpicker();

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
//        toolbar.setTitle("Todays Appointments");
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PatientHistoryInDoctor.this,DoctorTodaysAppointmentsForPatient.class);
                        intent.putExtra("mobile",doctorMobile);
                        intent.putExtra("id",doctorId);
                        startActivity(intent);

                    }
                }

        );

    }

    public void showyearpicker()

    {

        YearMonthPickerDialog yearMonthPickerDialog = new YearMonthPickerDialog(this,
                new YearMonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onYearMonthSet(int year, int month) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        month=month+1;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
                        String date=month+"/"+8+"/"+year;
                        System.out.println("datepicker date"+date);
                        Date date1 = null;
                        try {
                            date1 = simpleDateFormat.parse(date);
                            CalendarDay day1 = CalendarDay.from(date1);

                            System.out.println("day1"+day1);
                            calendarView.setCurrentDate(day1);


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                });

        yearMonthPickerDialog.show();
    }

    private class GetPatientDetails extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(PatientHistoryInDoctor.this);
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

            Log.e("TAG result    ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            progressDialog.dismiss();
            getdetails(result);

        }
    }

    private void getdetails(String result) {
        final List<CalendarDay> events = new ArrayList<>();

        final List<String> aadhar = new ArrayList<String>();
        final List<String> patientnames = new ArrayList<>();
        final List<String> mobilenumbers = new ArrayList<>();
        final List<String> reasonlist = new ArrayList<>();
        final List<String> doctornames = new ArrayList<String>();
        final List<String> specialitylist = new ArrayList<>();
        final List<String> dates = new ArrayList<>();
        final List<String> comment = new ArrayList<>();


        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Aaadharnumber = jsonObject.getString("AadharNumber");
                Patientname = jsonObject.getString("PatientName");
                Mobilenumber = jsonObject.getString("MobileNumber");
                reason = jsonObject.getString("ReasonAppointments");
                doctorname = jsonObject.getString("DoctorName");
                speciality = jsonObject.getString("Speciality");
                appoinmentdate = jsonObject.getString("AppointmentDateStr");
                doctorcomment = jsonObject.getString("DoctorComment");

                String arr[] = appoinmentdate.split(" ");
                date = arr[0];
                Date date1 = simpleDateFormat.parse(date);
                CalendarDay day = CalendarDay.from(date1);
//                int month=day.getMonth();
//                System.out.println();
                aadhar.add(Aaadharnumber);
                patientnames.add(Patientname);
                mobilenumbers.add(Mobilenumber);
                reasonlist.add(reason);
                doctornames.add(doctorname);
                specialitylist.add(speciality);
                dates.add(date);
                comment.add(doctorcomment);
                events.add(day);

            }

            int length=jsonArray.length();
            String l=Integer.toString(length);
            count.setText(l);

            calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {


                    System.out.println("Selected Date" + date);

                    int m = date.getMonth() + 1;
                    d = m + "/" + date.getDay() + "/" + date.getYear();
                    System.out.println("dateeeee" + d);

                    if (dates.contains(d)) {


                        Intent i = new Intent(PatientHistoryInDoctor.this, ViewPatientHistoryInDoctor.class);
                        i.putExtra("date", d);
                        i.putExtra("patientId",patientId);
                        i.putExtra("doctorImageUrl",doctorImageUrl);
                        i.putExtra("id",doctorId);
                        i.putExtra("doctorMobile",doctorMobile);
                        i.putStringArrayListExtra("aadharnumber", (ArrayList<String>) aadhar);
                        i.putStringArrayListExtra("patientname", (ArrayList<String>) patientnames);
                        i.putStringArrayListExtra("mobilenumbers", (ArrayList<String>) mobilenumbers);
                        i.putStringArrayListExtra("reason", (ArrayList<String>) reasonlist);
                        i.putStringArrayListExtra("doctorname", (ArrayList<String>) doctornames);
                        i.putStringArrayListExtra("speciality", (ArrayList<String>) specialitylist);
                        i.putStringArrayListExtra("dates", (ArrayList<String>) dates);
                        i.putStringArrayListExtra("comment", (ArrayList<String>) comment);
                        startActivity(i);
                    } else {
                        showalert();
                    }

                }
            });

            EventDecorator eventDecorator = new EventDecorator(this, events);
            calendarView.addDecorator((DayViewDecorator) eventDecorator);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showalert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(PatientHistoryInDoctor.this);
        builder.setMessage("You don't have history today");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();

    }
}
