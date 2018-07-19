package com.example.wxhgxj.tio;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class EventsActivity extends AppCompatActivity {

    private RecyclerView eventsList;
    private Button addEventBtn;
    private DatabaseReference rootRef;
    private FirebaseUser currentUser;
    private String currentUid;
    private FirebaseRecyclerAdapter<Event, EventViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        //initialize all the fields
        eventsList = (RecyclerView)findViewById(R.id.eventsList);
        addEventBtn = (Button)findViewById(R.id.addNewEventButton);
        rootRef = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUid = currentUser.getUid();
        eventsList.setHasFixedSize(true);
        eventsList.setLayoutManager(new LinearLayoutManager(this));
        Query query;
        String type = getIntent().getStringExtra("Query");
        if(type.equals("Date")) {
            String date = getIntent().getStringExtra("Date");
            query = rootRef.child("Events").child(currentUid).orderByChild("Date").equalTo(date);
        } else {
            String month = getIntent().getStringExtra("Month");
            int monthIndex = Integer.parseInt(month);
            query = rootRef.child("Events").child(currentUid).orderByChild("Month").equalTo(monthIndex);
        }
        FirebaseRecyclerOptions<Event> options = new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(query, Event.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EventViewHolder holder, final int position, @NonNull Event model) {
                //get the key of current event
                final String currentEventID = getRef(position).getKey();
                holder.setEventTitle(model.getTitle());
                String date = model.getDate();
                String time = date + ",  " + model.getTime();
                holder.setEventTime(time);
                holder.setEventDescription(model.getDescription());
                holder.setEventLocation(model.getLocation());
                Button editEventButton = holder.getEditButton();
                Button removeEventButton = holder.getRemoveButton();
                editEventButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent updateEventIntent = new Intent(EventsActivity.this, UpdateEventActivity.class);
                        updateEventIntent.putExtra("eventID", currentEventID);
                        setIntentExtra(updateEventIntent);
                        updateEventIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(updateEventIntent);
                    }
                });
                removeEventButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getRef(position).removeValue();
                    }
                });
            }
            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_event, parent, false);
                return new EventViewHolder(view);
            }
        };
        eventsList.setAdapter(firebaseRecyclerAdapter);

        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addEventIntent = new Intent(EventsActivity.this, AddEventActivity.class);
                setIntentExtra(addEventIntent);
                addEventIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(addEventIntent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    private void setIntentExtra(Intent intent) {
        String type = getIntent().getStringExtra("Query");
        if(type.equals("Date")) {
            intent.putExtra("Query", type);
            intent.putExtra("Date", getIntent().getStringExtra("Date"));
        }else {
            intent.putExtra("Query", type);
            intent.putExtra("Month", getIntent().getStringExtra("Month"));
        }
    }
}
