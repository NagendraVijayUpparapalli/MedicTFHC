package com.example.cool.patient.patient.MyDoctorAppointments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cool.patient.common.ApiBaseUrl;
import com.example.cool.patient.patient.PatientDashBoard;
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


public class PatientMyDoctorAppointments  extends AppCompatActivity {
    static String getUserId;
    String mobile_number,date,date2,status1;
    String AppointmentDate,DoctorName,Prescription,Timeslot,PatientName,AppointmentStatus,Reason,DoctorComment,paymentmode,Amount;
    String appointmentId,DoctorID;
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

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private YearMonthPickerDialog.OnDateSetListener onDateSetListener;
    ProgressDialog progressDialog;

    int i=0;

    ApiBaseUrl baseUrl;


    List<CalendarDay> events=null;
    List<String> doctorname=null;
    List<String> dates=null;
    List<String> patientnames=null;
    List<String> timeslot=null;
    List<String> statuss=null;
    List<String> reason=null;
    List<String> comment=null;
    List<String> amount=null;
    List<String> prescription=null;
    List<String> payment=null;
    List<String> appointmentIdlist = null;
    List<String> timings=null;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private List<PatientMyDoctorAppointmentDetailsClass> data_list = null;
    PatientMyDoctorAppointmentsHistoryAdapter patientMyDoctorAppointmentsHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_my_doctor_appointments);

        baseUrl = new ApiBaseUrl();

        recyclerView=(RecyclerView) findViewById(R.id.recycler_view);
        layoutManager=new LinearLayoutManager(this);

        calendarView=(com.prolificinteractive.materialcalendarview.MaterialCalendarView ) findViewById(R.id.calendar);

        Calendar cal=Calendar.getInstance();
        calendarView.setDateSelected(cal.getTime(),true);

//        arrayList=new ArrayList<PatientAppointmentDetails>();
//        patient_history_adapter=new Patient_History_Adapter(getApplicationContext(),R.layout.activity_view_full_patient_history,arrayList);
        //get user details based on id
        mobile_number = getIntent().getStringExtra("mobile");
        getUserId = getIntent().getStringExtra("id");
        System.out.print("userid in patientactivity....."+getUserId+".....phone..."+mobile_number);


        new GetPatientMyDocAppointmentDetails().execute(baseUrl.getUrl()+"MyDoctorAppointments"+"?PatientId="+getUserId);

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
                        Intent intent = new Intent(PatientMyDoctorAppointments.this,PatientDashBoard.class);
                        intent.putExtra("id",getUserId);
                        intent.putExtra("mobile",mobile_number);
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
    private class GetPatientMyDocAppointmentDetails extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(PatientMyDoctorAppointments.this);
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

        data_list=new ArrayList<>();

        events = new ArrayList<>();
        dates=new ArrayList<String>();

        try {
            JSONArray jsonArray=new JSONArray(result);

            for(int i=0;i<jsonArray.length();i++)
            {
//                System.out.println("hello");
                JSONObject  js= jsonArray.getJSONObject(i);
                appointmentId = js.getString("AppointmentID");
                DoctorID=js.getString("DoctorID");
                AppointmentDate = (String) js.get("AppointmentDate");
                DoctorName = (String) js.get("DoctorName");
                Prescription = (String) js.get("Prescription");

                System.out.println("my doctor id..."+DoctorID);

                Timeslot = (String) js.get("TimeSlot");
                PatientName = (String) js.get("PatientName");
                AppointmentStatus = (String) js.get("AppointmentStatus");
                Reason = (String) js.get("ReasonAppointment");
                DoctorComment = (String) js.get("DoctorComment");
                paymentmode = (String) js.get("PaymentMode");
                Amount = (String) js.get("Amount");
                String arr[]=AppointmentDate.split(" ");
                date=arr[0];

                PatientMyDoctorAppointmentDetailsClass patientAppointmentDetails=new PatientMyDoctorAppointmentDetailsClass(getUserId,mobile_number,appointmentId,DoctorID,AppointmentDate,DoctorName,Prescription,Timeslot,PatientName,AppointmentStatus,Reason,DoctorComment,paymentmode,Amount,date);

                if(date.equals(d))
                {
                    data_list.add(patientAppointmentDetails);
                }


//                System.out.println("getdate.."+date);
                Date date1 = simpleDateFormat.parse(date);
                CalendarDay day = CalendarDay.from(date1);
                events.add(day);
                dates.add(date);

            }

            calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                    System.out.println("Selected Date..."+date);

                    int m = date.getMonth()+1;
                    d = m+"/"+date.getDay()+"/"+date.getYear();
//                    System.out.println("length"+data_list.size());

                    new GetPatientMyDocAppointmentDetails().execute(baseUrl.getUrl()+"MyDoctorAppointments"+"?PatientId="+getUserId);

                    patientMyDoctorAppointmentsHistoryAdapter = new PatientMyDoctorAppointmentsHistoryAdapter(getApplicationContext(),data_list,d);

                    recyclerView.setLayoutManager(layoutManager);


                    recyclerView.setAdapter(patientMyDoctorAppointmentsHistoryAdapter);
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
