package com.example.cool.patient.doctor.TodaysAppointments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.style.ForegroundColorSpan;

import com.example.cool.patient.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Udayasri on 09-02-2018.
 */


class EventDecorator implements DayViewDecorator {

    private HashSet<CalendarDay> dates;
    private Drawable drawable;

    public EventDecorator(Activity context, Collection<CalendarDay> events) {
        this.dates= new HashSet<> (events);
        drawable = ContextCompat.getDrawable(context, R.drawable.event_circle);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {

        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {

        view.setSelectionDrawable(drawable);

//        view.addSpan(new ForegroundColorSpan(Color.BLACK));
        view.addSpan(new ForegroundColorSpan(Color.WHITE));

    }

}
