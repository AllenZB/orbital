package com.example.wxhgxj.tio;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
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
        final Date today = new Date();
        calendar.setSelectedDate(today);
        dates.add(new CalendarDay(today));
        calendar.addDecorator(new EventDecorator(Color.BLUE, dates));
        dates.clear();

        eventRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Event currentEvent = dataSnapshot.getValue(Event.class);
                String date = currentEvent.getDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                String todayDate = dateFormat.format(today);
                if(!date.equals(todayDate)) {
                    Date currentDate = dateFormat.parse(date, new ParsePosition(0));
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(currentDate);
                    int currentYear = cal.get(Calendar.YEAR);
                    int currentMonth = cal.get(Calendar.MONTH);
                    int currentDay = cal.get(Calendar.DAY_OF_MONTH);
                    Calendar myCalendar = new GregorianCalendar(currentYear, currentMonth, currentDay);
                    Date myDate = myCalendar.getTime();
                    dates.add(new CalendarDay(myDate));
                    calendar.addDecorator(new EventDecorator(Color.RED, dates));
                }
                currentMonthIndex = today.getMonth();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
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
