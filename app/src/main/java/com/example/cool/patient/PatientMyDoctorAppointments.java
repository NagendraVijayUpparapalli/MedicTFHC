package com.example.cool.patient;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class PatientMyDoctorAppointments  extends AppCompatActivity {
    static int getUserId;
    String mobile_number,uploadServerUrl,date,date2,status1;
//    PatientAppointmentDetails patient;
    String AppointmentDate,DoctorName,Prescription,Timeslot,PatientName,AppointmentStatus,Reason,DoctorComment,paymentmode,Amount;

    com.prolificinteractive.materialcalendarview.MaterialCalendarView calendarView;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M/dd/yyyy", Locale.getDefault());
    private SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEEE", Locale.getDefault());
    String ss;
    String d;
    Date dayformat;
    String day;

    TextView accept,reject,cancel,reschedule,total,yearMonth;
    static final int DATE_DIALOG_ID = 1;
    private int mYear;
    private int mMonth;
    private int mDay;

    ImageView imageView;

    int acceptcnt,rejectcnt,cancelcnt,reschedulecnt;
//    Patient_History_Adapter patient_history_adapter;
//    ArrayList<PatientAppointmentDetails> arrayList;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private YearMonthPickerDialog.OnDateSetListener onDateSetListener;

    int i=0;

    ApiBaseUrl baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_doctor_appointments);

        baseUrl = new ApiBaseUrl();

        calendarView=(com.prolificinteractive.materialcalendarview.MaterialCalendarView ) findViewById(R.id.calendar);

        accept=(TextView)findViewById(R.id.Accept_count);
        reject=(TextView)findViewById(R.id.Reject_count);
        cancel=(TextView)findViewById(R.id.Cancel_count);
        reschedule=(TextView)findViewById(R.id.Reschedule_count);
        total=(TextView)findViewById(R.id.Total_count);

        Calendar cal=Calendar.getInstance();
        calendarView.setDateSelected(cal.getTime(),true);

//        arrayList=new ArrayList<PatientAppointmentDetails>();
//        patient_history_adapter=new Patient_History_Adapter(getApplicationContext(),R.layout.activity_view_full_patient_history,arrayList);
        //get user details based on id
        mobile_number = getIntent().getStringExtra("mobile");
        getUserId = getIntent().getIntExtra("id",getUserId);
        System.out.print("userid in patientactivity....."+getUserId);


        new GetPatientDetails().execute(baseUrl.getUrl()+"MyDoctorAppointments"+"?PatientId="+getUserId);

        imageView=(ImageView) findViewById(R.id.img1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showyearpicker();

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("My Doctor Appointments");
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PatientMyDoctorAppointments.this,MainActivity.class);
                        intent.putExtra("id",getUserId);
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
            //getProfileDetails(result);
            getdetails(result);

        }
    }

    private void getdetails(String result) {

        final List<CalendarDay> events = new ArrayList<>();
        final List<String> doctorname=new ArrayList<String>();
        final List<String> dates=new ArrayList<String>();
        final List<String> patientnames=new ArrayList<>();
        final List<String> timeslot=new ArrayList<>();
        final List<String> statuss=new ArrayList<>();
        final List<String> reason=new ArrayList<>();
        final List<String> comment=new ArrayList<>();
        final List<String> amount=new ArrayList<>();
        final  List<String> prescription=new ArrayList<>();
        final  List<String> payment=new ArrayList<>();

        try {
            JSONArray jsonArray=new JSONArray(result);

            for(int i=0;i<jsonArray.length();i++)
            {
                System.out.println("hello");
                JSONObject  js= jsonArray.getJSONObject(i);
                AppointmentDate = (String) js.get("AppointmentDate");
                DoctorName = (String) js.get("DoctorName");
                Prescription = (String) js.get("Prescription");
                Timeslot = (String) js.get("TimeSlot");
                PatientName = (String) js.get("PatientName");
                AppointmentStatus = (String) js.get("AppointmentStatus");
                Reason = (String) js.get("ReasonAppointment");
                DoctorComment = (String) js.get("DoctorComment");
                paymentmode = (String) js.get("PaymentMode");
                Amount = (String) js.get("Amount");
                String arr[]=AppointmentDate.split(" ");
                date=arr[0];


                System.out.println("getdate"+date);
                Date date1 = simpleDateFormat.parse(date);
                CalendarDay day = CalendarDay.from(date1);
                events.add(day);
                dates.add(date);
                doctorname.add(DoctorName);
                patientnames.add(PatientName);
                timeslot.add(Timeslot);
                statuss.add(AppointmentStatus);
                reason.add(Reason);
                comment.add(DoctorComment);
                amount.add(Amount);
                prescription.add(Prescription);
                payment.add(paymentmode);

            }



            for(int i=0;i<dates.size();i++)
            {
                date2=dates.get(i);
                System.out.println("list of dates"+date2);

            }
            for(int i=0;i<statuss.size();i++)
            {
                status1=statuss.get(i);
            }

            if(statuss.contains("Accept"))
            {
                acceptcnt++;
                accept.setText(":"+Integer.toString(acceptcnt));

            }
            else if(statuss.contains("Reject"))
            {
                rejectcnt++;
                reject.setText(Integer.toString(rejectcnt));
            }
            else  if(statuss.contains("Reschedule"))
            {
                reschedulecnt++;
                reschedule.setText(Integer.toString(reschedulecnt));

            }
            calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                    System.out.println("Selected Date"+date);

                    int m = date.getMonth()+1;
                    d = m+"/"+date.getDay()+"/"+date.getYear();
                    System.out.println("dateeeee"+d);

                    if (dates.contains(d)) {


                        Intent i = new Intent(PatientMyDoctorAppointments.this, ViewPatientMyDoctorAppointment.class);
                        i.putExtra("date", d);
                        i.putStringArrayListExtra("doctorname", (ArrayList<String>) doctorname);
                        i.putStringArrayListExtra("dates", (ArrayList<String>) dates);
                        i.putStringArrayListExtra("patientname", (ArrayList<String>) patientnames);
                        i.putStringArrayListExtra("timeslot", (ArrayList<String>) timeslot);
                        i.putStringArrayListExtra("status", (ArrayList<String>) statuss);
                        i.putStringArrayListExtra("reason", (ArrayList<String>) reason);
                        i.putStringArrayListExtra("comment", (ArrayList<String>) comment);
                        i.putStringArrayListExtra("amount", (ArrayList<String>) amount);
                        i.putStringArrayListExtra("prescription", (ArrayList<String>) prescription);
                        i.putStringArrayListExtra("payment", (ArrayList<String>) payment);
                        startActivity(i);
                    }

                    else {
                        showalert();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        EventDecorator eventDecorator1 =new EventDecorator(this,events);
        calendarView.addDecorator((DayViewDecorator) eventDecorator1);
    }



    private void showalert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(PatientMyDoctorAppointments.this);
        builder.setMessage("You don't have history today");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();

    }

}
