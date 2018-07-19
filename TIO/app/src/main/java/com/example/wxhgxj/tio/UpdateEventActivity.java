package com.example.wxhgxj.tio;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateEventActivity extends AppCompatActivity {

    private EditText newEventTitle;
    private EditText newEventDate;
    private EditText newEventStartTime;
    private EditText newEventEndTime;
    private EditText newEventLocation;
    private EditText newEventDescription;
    private Button updateEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

        newEventTitle = (EditText)findViewById(R.id.newEventTitleInput);
        newEventDate =(EditText)findViewById(R.id.newEventDateInput);
        newEventStartTime = (EditText)findViewById(R.id.newEventStartTimeInput);
        newEventEndTime = (EditText)findViewById(R.id.newEventEndTimeInput);
        newEventLocation = (EditText)findViewById(R.id.newEventLocationInput);
        newEventDescription = (EditText)findViewById(R.id.newEventDescriptionInput);
        updateEvent = (Button) findViewById(R.id.updateEventButton);
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String currentEventID = getIntent().getStringExtra("eventID");
        final DatabaseReference currentEventRef = FirebaseDatabase.getInstance().getReference("Events").child(currentUid).child(currentEventID);
        currentEventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newEventTitle.setText(dataSnapshot.child("Title").getValue().toString());
                newEventDate.setText(dataSnapshot.child("Date").getValue().toString());
                newEventStartTime.setText(dataSnapshot.child("StartTime").getValue().toString());
                newEventEndTime.setText(dataSnapshot.child("EndTime").getValue().toString());
                newEventLocation.setText(dataSnapshot.child("Location").getValue().toString());
                newEventDescription.setText(dataSnapshot.child("Description").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        updateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = newEventTitle.getText().toString();
                String date = newEventDate.getText().toString();
                String startTime = newEventStartTime.getText().toString();
                String endTime = newEventEndTime.getText().toString();
                String location = newEventLocation.getText().toString();
                String description = newEventDescription.getText().toString();
                int month = Integer.parseInt(date.substring(0,2)) - 1;
                HashMap<String, Object> eventMap = new HashMap<>();
                eventMap.put("Title", title);
                eventMap.put("Date", date);
                eventMap.put("Month", month);
                eventMap.put("StartTime", startTime);
                eventMap.put("EndTime", endTime);
                eventMap.put("Location", location);
                eventMap.put("Description", description);
                currentEventRef.updateChildren(eventMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if(databaseError != null) {
                            Log.d("EventLog", databaseError.getMessage().toString());
                        } else {
                            Intent eventsIntent = new Intent(UpdateEventActivity.this, EventsActivity.class);
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
