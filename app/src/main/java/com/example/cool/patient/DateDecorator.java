package com.example.cool.patient;


import android.content.Context;
import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;
/**
 * Created by Udayasri on 25-05-2018.
 */

class DateDecorator implements DayViewDecorator {
    CalendarDay date;
    Context context;
    public DateDecorator(Context diagnostic_book_appointment, CalendarDay date) {
        this.date=date;
        context=diagnostic_book_appointment;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date.equals(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.WHITE));
    }
}
