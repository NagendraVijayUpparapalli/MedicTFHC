package com.example.cool.patient.diagnostic.TodaysAppointments;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class MainPatientHistoryInDiagnostics extends AppCompatActivity {

    com.prolificinteractive.materialcalendarview.MaterialCalendarView calendarView;
    ImageView img;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
    String patientid;
    TextView count;
    String url,d;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    String AadharNumber,PatientName,Mobileno,centername,testname,comments,date;
    int RdId;
    DiagnosticsPatientHistoryAdapter adapter;
    List<CalendarDay> events=null;
    List<String> dates=null;
    List<String> timings=null;
    private List<DiagnosticsPatientHistoryDataClass> data_list = null;
    String diagId,diagMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_patient_history_in_diagnostics);

        calendarView=(com.prolificinteractive.materialcalendarview.MaterialCalendarView ) findViewById(R.id.calendar);

        img=(ImageView) findViewById(R.id.img1);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        layoutManager=new LinearLayoutManager(this);

        // count=(TextView)findViewById(R.id.appointmentcount);

        patientid=getIntent().getStringExtra("patientid");
        diagId = getIntent().getStringExtra("diagId");
        diagMobile = getIntent().getStringExtra("diagMobile");

//        i.putExtra("diagId",diagnosticId);

        System.out.println("patient id in main history.."+patientid);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showyearpicker();

            }
        });

        url="https://meditfhc.com/mapi/DiagPatientHistory";
        new GetPatientDetails().execute(url+"?PatientID="+patientid);

        System.out.println("diag id main his patient.."+getIntent().getStringExtra("diagId")+"...."+getIntent().getStringExtra("diagMobile"));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("Patient History");
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainPatientHistoryInDiagnostics.this,DiagnosticsTodaysAppointments.class);
                        intent.putExtra("userId",diagId);
                        intent.putExtra("mobile",diagMobile);
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
//            JSONArray jsonArray = new JSONArray(result);
            getdetails(result);

        }
    }

    private void getdetails(String result) {

        data_list=new ArrayList<>();

        events = new ArrayList<>();
        dates=new ArrayList<String>();

        timings=new ArrayList<String>();
        final String value=null;

        try {
            JSONArray jsonArray=new JSONArray(result);



            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject js= jsonArray.getJSONObject(i);
                AadharNumber = (String) js.get("AadharNumber");
                PatientName = (String) js.get("PatientName");
                Mobileno = (String) js.get("MobileNumber");
                centername = (String) js.get("CenterName");
                testname =(String) js.get("TestName");
                comments=(String) js.get("Comment");
                date=(String)  js.get("Date");
                RdId=js.getInt("Rdid");

                String arr[]=date.split(" ");
                date=arr[0];
                Date date1 = simpleDateFormat.parse(date);
                CalendarDay day = CalendarDay.from(date1);
                events.add(day);
                dates.add(date);
                DiagnosticsPatientHistoryDataClass patientHistoryDatainDiagnostics=new DiagnosticsPatientHistoryDataClass(patientid,diagId,diagMobile,AadharNumber,PatientName,Mobileno,centername,testname,comments,date,RdId);

                if(date.equals(d))
                {
                    data_list.add(patientHistoryDatainDiagnostics);
                }

            }

            calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                    System.out.println("Selected Date"+date);

                    int m = date.getMonth()+1;
                    d = m+"/"+date.getDay()+"/"+date.getYear();
                    System.out.println("length.."+data_list.size());

                    new GetPatientDetails().execute(url+"?PatientId="+patientid);
//
                    adapter=new DiagnosticsPatientHistoryAdapter(getApplicationContext(),data_list,d);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
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

}