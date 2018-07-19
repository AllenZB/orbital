package com.example.wxhgxj.tio;

import android.content.Intent;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AddEventActivity extends AppCompatActivity {

    private EditText eventTitle;
    private EditText eventDate;
    private EditText eventStartTime;
    private EditText eventEndTime;
    private EditText eventLocation;
    private EditText eventDescription;
    private Button addNewEvent;
    private String defaultDate;
    private String defaultTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        //initialize fields
        eventTitle = (EditText)findViewById(R.id.newEventTitleInput);
        eventDate = (EditText)findViewById(R.id.newEventDateInput);
        eventStartTime = (EditText)findViewById(R.id.newEventStartTimeInput);
        eventEndTime = (EditText)findViewById(R.id.eventEndTimeInput);
        eventLocation = (EditText)findViewById(R.id.newEventLocationInput);
        eventDescription = (EditText)findViewById(R.id.newEventDescriptionInput);
        addNewEvent = (Button)findViewById(R.id.addnewEventButton);
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("Events").child(currentUid);
        String type = getIntent().getStringExtra("Query");
        if(type.equals("Date")) {
            defaultDate = getIntent().getStringExtra("Date");
        } else {
            int monthIndex = Integer.parseInt(getIntent().getStringExtra("Month"));
            String today = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
            String modifier = monthIndex > 8 ? "" : "0";
            defaultDate = modifier + (monthIndex + 1) + "-01" + today.substring(5);
        }
        defaultTime = new SimpleDateFormat("HH:mm").format(new Date());
        eventDate.setText(defaultDate);
        eventStartTime.setText(defaultTime);
        eventEndTime.setText(defaultTime);
        addNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference newEventRef = eventsRef.push();
                String title = eventTitle.getText().toString();
                String date = eventDate.getText().toString();
                String startTime = eventStartTime.getText().toString();
                String endTime = eventEndTime.getText().toString();
                String location = eventLocation.getText().toString();
                String description = eventDescription.getText().toString();
                int month = Integer.parseInt(date.substring(0,2)) - 1;
                HashMap<String, Object> eventMap = new HashMap<>();
                eventMap.put("Title", title);
                eventMap.put("Date", date);
                eventMap.put("Month", month);
                eventMap.put("StartTime", startTime);
                eventMap.put("EndTime", endTime);
                eventMap.put("Location", location);
                eventMap.put("Description", description);
                newEventRef.updateChildren(eventMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if(databaseError != null) {
                            Log.d("EventLog", databaseError.getMessage().toString());
                        } else {
                            Intent eventsIntent = new Intent(AddEventActivity.this, EventsActivity.class);
                            String type = getIntent().getStringExtra("Query");
                            if(type.equals("Date")) {
                                eventsIntent.putExtra("Query", type);
                                eventsIntent.putExtra("Date", getIntent().getStringExtra("Date"));
                            } else {
                                eventsIntent.putExtra("Query", type);
                                eventsIntent.putExtra("Month", getIntent().getStringExtra("Month"));
                            }
                            eventsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(eventsIntent);
                        }
                    }
                });
            }
        });
    }
}
