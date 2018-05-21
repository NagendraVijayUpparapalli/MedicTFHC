package com.example.cool.patient;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DoctorCalendar extends AppCompatActivity {

    MaterialCalendarView calendarView;
//    CalendarView calendarView1;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    private SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEEE", Locale.getDefault());

    Date dayformat;

    int acnt=0,rjcnt=0,rscnt=0,pencnt=0;
    static String strJson=null,date=null;

    String data = "";
    static String d = null, date11 =null,day=null;
    StringBuffer buffer;

    Date date1;

    List<CalendarDay> events = new ArrayList<>();
    List<CalendarDay> events1 = new ArrayList<>();
    List<CalendarDay> events2 = new ArrayList<>();
    List<CalendarDay> events3 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_calendar);

//        strJson = "{\"Dates\":[{\"id\": \"01\",\"date\": \"12/2/2018\",\"status\":\"Accept\"}," +
//                "{\"id\": \"02\",\"date\": \"12/2/2018\",\"status\":\"Reject\"}]}";

        strJson = "{\"Dates\":[{\"id\": \"01\",\"date\": \"12/2/2018\",\"status\":\"Accept\"}," +
                "{\"id\": \"02\",\"date\": \"12/2/2018\",\"status\":\"Reject\"}" +
                ",{\"id\": \"03\",\"date\": \"9/3/2018\",\"status\":\"Pending\"}," +
                "{\"id\": \"04\",\"date\": \"12/2/2018\",\"status\":\"Reject\"}," +
                "{\"id\": \"05\",\"date\": \"6/3/2018\",\"status\":\"Reschedule\"}," +
                "{\"id\": \"06\",\"date\": \"6/3/2018\",\"status\":\"Accept\"}]}";
        calendarView = (MaterialCalendarView)findViewById(R.id.calendar);

//        calendarView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                StringBuffer buffer = new StringBuffer();
//                buffer.append("Accepted :"+ accnt+"\n");
//                buffer.append("Rejected :"+ rejcnt+"\n");
//                buffer.append("Re-Schedule :"+ rescnt+"\n");
//                buffer.append("Pending :"+ pendcnt+"\n");
//
//                // Show all data
//                showMessage("Title",buffer.toString());
//
//            }
//        });


        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Log.d("selected", "" + selected);
                //It can't be show
                Toast.makeText(DoctorCalendar.this, "enterDateSelected" + date, Toast.LENGTH_SHORT).show();

                if (selected == true) {
                    //It can't be show
                    if(events.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        System.out.println("material date....."+date);
                        comparedate(date);
                    }
                }
            }
        });

        try {
            JSONObject jsonRootObject;
            jsonRootObject = new JSONObject(strJson);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("Dates");
            String status="";
            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = Integer.parseInt(jsonObject.optString("id").toString());
                String date = jsonObject.optString("date").toString();
                status=jsonObject.optString("status").toString();
                //data += "Blog Number "+(i+1)+" : \n id= "+ id +"\n Date= "+ date +" \n\n\n\n ";

                Log.d("Date ", "" + date);
                Log.d("Status ", "" + status);
                if(status.equals("Accept") || status.equals("Reject") || status.equals("Pending") || status.equals("Reschedule"))
                {
                    date1 = simpleDateFormat.parse(date);
                    CalendarDay day = CalendarDay.from(date1);
                    events.add(day);
//                    acnt++;
                }
            }
            //output.setText(data);
        } catch (JSONException e) {e.printStackTrace();} catch (ParseException e) {
            e.printStackTrace();
        }

//        EventDecorator eventDecorator = new EventDecorator(Color.GREEN, events);
//        calendarView.addDecorator((DayViewDecorator) eventDecorator);
//
//        EventDecorator eventDecorator1 = new EventDecorator(Color.RED, events1);
//        calendarView.addDecorator((DayViewDecorator) eventDecorator1);
//
//        EventDecorator eventDecorator2 = new EventDecorator(Color.CYAN, events2);
//        calendarView.addDecorator((DayViewDecorator) eventDecorator2);

//        EventDecorator eventDecorator = new EventDecorator(Color.BLUE, events);
//        calendarView.addDecorator((DayViewDecorator) eventDecorator);
//        calendarView.setSelectionColor(Color.parseColor("#00BCD4"));

    }

    public void comparedate(CalendarDay calendarDay)
    {
        try {
            JSONObject jsonRootObject;
            jsonRootObject = new JSONObject(strJson);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("Dates");
            String status="";
            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = Integer.parseInt(jsonObject.optString("id").toString());
                date11 = jsonObject.optString("date").toString();
                status=jsonObject.optString("status").toString();

                int m = calendarDay.getMonth()+1;
                d = calendarDay.getDay()+"/"+m+"/"+calendarDay.getYear();
                dayformat = simpleDateFormat.parse(d);
                day = simpleDateFormat1.format(dayformat);
                System.out.println("dayyy...."+day);
                System.out.println("dt.."+calendarDay.getDay());
//                Toast.makeText(getApplication(),d,Toast.LENGTH_LONG).show();

                if(d.equals(date11))
                {
                    viewStatus(calendarDay);
                }
            }

            Toast.makeText(DoctorCalendar.this, "onClick" + date, Toast.LENGTH_SHORT).show();
//                        //output.setText(data);
        } catch (JSONException e) {e.printStackTrace();} catch (ParseException e) {
            e.printStackTrace();
        }

    }


    public void viewStatus(CalendarDay calendarDay)
    {
        try {
            JSONObject jsonRootObject;
            jsonRootObject = new JSONObject(strJson);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("Dates");
            String status="";
            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = Integer.parseInt(jsonObject.optString("id").toString());
                date11 = jsonObject.optString("date").toString();
                status=jsonObject.optString("status").toString();
                //data += "Blog Number "+(i+1)+" : \n id= "+ id +"\n Date= "+ date +" \n\n\n\n ";

                Log.d("Date ", "" + date11);
                Log.d("Status ", "" + status);
                int m = calendarDay.getMonth()+1;
                d = calendarDay.getDay()+"/"+m+"/"+calendarDay.getYear();
                dayformat = simpleDateFormat.parse(d);
                day = simpleDateFormat1.format(dayformat);
                System.out.println("dayyy...."+day);
                System.out.println("dt.."+calendarDay.getDay());
//                Toast.makeText(getApplication(),d,Toast.LENGTH_LONG).show();

                if(d.equals(date11))
                {
                    if(status.equals("Accept"))
                    {
                        acnt++;
                    }
                    if(status.equals("Reject"))
                    {
                        rjcnt++;
                    }
                    else if(status.equals("Pending"))
                    {
                        pencnt++;
                    }
                    else if(status.equals("Reschedule"))
                    {
                        rscnt++;
//                                    System.out.println("res.."+rscnt);
                    }

                    buffer = new StringBuffer();
                    buffer.append("Accepted :"+ acnt+"\n");
                    buffer.append("Rejected :"+ rjcnt+"\n");
                    buffer.append("Re-Schedule :"+ rscnt+"\n");
                    buffer.append("Pending :"+ pencnt+"\n");
                }
            }

            // Show all data
            showMessage("Title",buffer.toString());

            Toast.makeText(DoctorCalendar.this, "onClick" + date, Toast.LENGTH_SHORT).show();
//                        //output.setText(data);
        } catch (JSONException e) {e.printStackTrace();} catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void showMessage(String title,String Message){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(true);
//        builder.setTitle(title);
//        builder.setMessage(Message);
//        builder.show();

        AlertDialog.Builder a_builder = new AlertDialog.Builder(DoctorCalendar.this);
        a_builder.setMessage(Message)
                .setCancelable(false)
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Status");
        alert.show();

        acnt = 0;rscnt=0;rjcnt=0;pencnt=0;
    }
}



