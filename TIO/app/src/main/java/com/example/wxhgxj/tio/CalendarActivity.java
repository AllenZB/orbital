package com.example.wxhgxj.tio;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarActivity extends AppCompatActivity {

    private MaterialCalendarView calendar;
    private Collection<CalendarDay> dates= new ArrayList<>();
    private DatabaseReference eventRef;
    private int currentMonthIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendar = (MaterialCalendarView)findViewById(R.id.calendar);
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        eventRef = FirebaseDatabase.getInstance().getReference("Events").child(currentUid);

        calendar.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        Calendar myCalendar = new GregorianCalendar(2018, Calendar.JULY, 12);
        Date myDate = myCalendar.getTime();
        Date today = new Date();
        calendar.setSelectedDate(today);
        dates.add(new CalendarDay(today));
        calendar.addDecorator(new EventDecorator(Color.BLUE, dates));
        dates.clear();
        currentMonthIndex = today.getMonth();
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                String date = new SimpleDateFormat("MM-dd-yyyy").format(calendarDay.getDate());
                Log.v("selected date", date);
                Intent eventsIntent = new Intent(CalendarActivity.this, EventsActivity.class);
                //pass to events activity to show the events on that day
                eventsIntent.putExtra("Query", "Date");
                eventsIntent.putExtra("Date", date);
                eventsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(eventsIntent);
            }
        });
        calendar.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
                Log.v("month change", calendarDay.getMonth() + "");
                currentMonthIndex = calendarDay.getMonth();
            }
        });
        calendar.setOnTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eventsIntent = new Intent(CalendarActivity.this, EventsActivity.class);
                //pass to events activity to show the events on that month
                eventsIntent.putExtra("Query", "Month");
                eventsIntent.putExtra("Month", currentMonthIndex + "");
                eventsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(eventsIntent);
            }
        });
    }
}
