package com.example.wxhgxj.tio;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeadlineFragment extends Fragment {


    public DeadlineFragment() {
        // Required empty public constructor
    }

    private View deadlineView;
    private RecyclerView deadlineList;
    private DatabaseReference deadlineRef;
    private Button addDeadlineButton;
    private FirebaseRecyclerAdapter<DeadlineEvent, DeadlineViewHolder> firebaseRecyclerAdapter;
    private EditText addDeadlineContent;
    private TextView addDeadlineTitle;
    private EditText addDeadlineDate;
    private EditText addDeadlineTime;
    private Button cancelDeadlineButton;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        deadlineView = inflater.inflate(R.layout.fragment_deadline, container, false);
        deadlineList = (RecyclerView)deadlineView.findViewById(R.id.deadlineList);
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        deadlineRef = FirebaseDatabase.getInstance().getReference("DeadLine").child(currentUid);
        addDeadlineContent = (EditText)deadlineView.findViewById(R.id.newDeadlineEventInput);
        addDeadlineTitle = (TextView)deadlineView.findViewById(R.id.deadlineInputTitle);
        addDeadlineDate = (EditText)deadlineView.findViewById(R.id.newDeadlineDate);
        addDeadlineTime = (EditText)deadlineView.findViewById(R.id.newDeadlineTime) ;
        addDeadlineButton = (Button)deadlineView.findViewById(R.id.addDeadlineButton);
        cancelDeadlineButton = (Button)deadlineView.findViewById(R.id.cancelDeadlineButton);

        deadlineList.setHasFixedSize(true);
        deadlineList.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = deadlineRef.orderByChild("OrderTime");
        FirebaseRecyclerOptions<DeadlineEvent> options = new FirebaseRecyclerOptions.Builder<DeadlineEvent>()
                .setQuery(query, DeadlineEvent.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DeadlineEvent, DeadlineViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DeadlineViewHolder holder, int position, @NonNull DeadlineEvent model) {
                final String date = model.getDeadlineDate();
                final String time = model.getDeadlineTime();
                String deadline = "On " + date + " At " + time;
                holder.setDeadline(deadline);
                final String content = model.getContent();
                holder.setDeadlineContent(content);
                final String currentDeadlineKey = getRef(position).getKey();
                final EditText newDeadlineContent = holder.getNewDeadlineContent();
                final EditText newDeadlineTime = holder.getNewDeadlineTime();
                final EditText newDeadlineDate = holder.getNewDeadlineDate();
                Button editDeadlineButton = holder.getEditDeadlineButton();
                Button doneDeadlineButton = holder.getDoneDeadlineButton();
                Button cancelDeadlineButton = holder.getCancelDeadlineButton();
                Button updateDeadlineButton = holder.getUpdateDeadlineButton();
                editDeadlineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.hideDisplayDeadline();
                        holder.showEditDeadline();
                        newDeadlineDate.setText(date);
                        newDeadlineTime.setText(time);
                        newDeadlineContent.setText(content);
                    }
                });
                doneDeadlineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deadlineRef.child(currentDeadlineKey).removeValue();
                    }
                });
                cancelDeadlineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.hideEditDeadline();
                        holder.showDisplayDeadline();
                    }
                });
                updateDeadlineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String date = newDeadlineDate.getText().toString();
                        String time = newDeadlineTime.getText().toString();
                        String content = newDeadlineContent.getText().toString();
                        deadlineRef.child(currentDeadlineKey).child("DeadlineDate").setValue(date);
                        deadlineRef.child(currentDeadlineKey).child("DeadlineTime").setValue(time);
                        deadlineRef.child(currentDeadlineKey).child("Content").setValue(content);
                        deadlineRef.child(currentDeadlineKey).child("OrderTime").setValue(date + "-" + time);
                        holder.hideEditDeadline();
                        holder.showDisplayDeadline();
                    }
                });
            }
            @NonNull
            @Override
            public DeadlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_deadline_event, parent, false);
                return new DeadlineViewHolder(view);
            }
        };
        addDeadlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = addDeadlineButton.getText().toString();
                if(text.equals("Add New Deadline Event")) {
                    addDeadlineContent.setVisibility(View.VISIBLE);
                    addDeadlineTitle.setVisibility(View.VISIBLE);
                    addDeadlineDate.setVisibility(View.VISIBLE);
                    addDeadlineTime.setVisibility(View.VISIBLE);
                    cancelDeadlineButton.setVisibility(View.VISIBLE);
                    Date today = new Date();
                    String defaultDate = new SimpleDateFormat("MM-dd-yyyy").format(today);
                    String defaultTime = new SimpleDateFormat("HH:SS").format(today);
                    addDeadlineDate.setText(defaultDate);
                    addDeadlineTime.setText(defaultTime);
                    addDeadlineButton.setText("Confirm");
                } else {
                    String content = addDeadlineContent.getText().toString();
                    String date = addDeadlineDate.getText().toString();
                    String time = addDeadlineTime.getText().toString();
                    DatabaseReference newDeadRef = deadlineRef.push();
                    newDeadRef.child("DeadlineDate").setValue(date);
                    newDeadRef.child("DeadlineTime").setValue(time);
                    newDeadRef.child("Content").setValue(content);
                    newDeadRef.child("OrderTime").setValue(date + "-" + time);
                    addDeadlineContent.setVisibility(View.GONE);
                    addDeadlineTitle.setVisibility(View.GONE);
                    addDeadlineDate.setVisibility(View.GONE);
                    addDeadlineTime.setVisibility(View.GONE);
                    cancelDeadlineButton.setVisibility(View.GONE);
                    addDeadlineButton.setText("Add New Deadline Event");
                }
            }
        });
        cancelDeadlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDeadlineContent.setVisibility(View.GONE);
                addDeadlineTitle.setVisibility(View.GONE);
                addDeadlineDate.setVisibility(View.GONE);
                addDeadlineTime.setVisibility(View.GONE);
                cancelDeadlineButton.setVisibility(View.GONE);
                addDeadlineButton.setText("Add New Deadline Event");
            }
        });
        deadlineList.setAdapter(firebaseRecyclerAdapter);
        return deadlineView;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

}
