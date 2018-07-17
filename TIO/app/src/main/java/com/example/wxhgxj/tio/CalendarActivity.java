package com.example.wxhgxj.tio;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarActivity extends AppCompatActivity {

    private MaterialCalendarView calendar;
    private Collection<CalendarDay> dates= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendar = (MaterialCalendarView)findViewById(R.id.calendar);
        calendar.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        Calendar myCalendar = new GregorianCalendar(2018, Calendar.JULY, 12);
        Date myDate = myCalendar.getTime();
        Date today = new Date();
        calendar.setSelectedDate(today);
        dates.add(new CalendarDay(today));
        calendar.addDecorator(new EventDecorator(Color.BLUE, dates));
        dates.clear();
    }
}
