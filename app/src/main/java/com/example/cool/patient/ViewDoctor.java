package com.example.cool.patient;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class ViewDoctor extends AppCompatActivity {

    MaterialCalendarView materialCalendarView;
    LinearLayout linearLayout;
    RelativeLayout relativeLayout;
    TextView myselecteddate;
    CalendarDay calendarDay;
    int mydate,m;
    static  String date,currentdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctor);

        myselecteddate = (TextView) findViewById(R.id.text);
        linearLayout = (LinearLayout)findViewById(R.id.mycalendar);
        relativeLayout = (RelativeLayout) findViewById(R.id.myform);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("View DoctorClass");


        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(BloodBank.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ViewDoctor.this,PatientDoctor.class);
                        startActivity(intent);

                    }
                }

        );

        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendar);

        Calendar calendar = Calendar.getInstance();
        materialCalendarView.setDateSelected(calendar.getTime(), true);
        currentdate = calendar.getTime().toString();
        System.out.println("current date...."+currentdate);
        String curr[] = currentdate.split(" ");
//        Toast.makeText(getApplicationContext(),currentdate, Toast.LENGTH_LONG).show();

        myselecteddate.setText(curr[1]+"\n"+curr[2]);

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Log.d("selected", "" + selected);
//                Toast.makeText(getApplicationContext(),date.toString(), Toast.LENGTH_LONG).show();

                if (selected == true) {
                    //It can't be show
//                    Toast.makeText(getApplicationContext(),"Hello fddssfsd....", Toast.LENGTH_LONG).show();

                    getDate(date);
                    linearLayout.setVisibility(View.INVISIBLE);
                    relativeLayout.setVisibility(View.VISIBLE);



                }
            }
        });

//        materialCalendarView.set
    }

    private void getDate(CalendarDay day) {
        mydate = day.getDay();
        m = day.getMonth();
        date=getMonthForInt(m);
        myselecteddate.setText(date+"\n"+mydate);
    }

    private String getMonthForInt(int m) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (m >= 0 && m <= 11 ) {
            month = months[m];
        }
        return month;
    }
}
