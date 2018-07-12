package com.example.wxhgxj.tio;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ChatActivity extends AppCompatActivity {

    String chatUserID;
    private DatabaseReference usersDB;
    private ImageView chatUserImg;
    private TextView chatMsg;
    private TextView chatUserTitle;
    private TextView lastSeen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatUserID = getIntent().getStringExtra("uid");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.chatbar_custom, null);
        actionBar.setCustomView(actionBarView);
        usersDB = FirebaseDatabase.getInstance().getReference("Users");
        actionBar.setTitle(chatUserID);
        String chatUserName = getIntent().getStringExtra("chatUserName");
        //Log.v("name", chatUserName);
        chatUserTitle = (TextView)findViewById(R.id.chatUserName);
        chatUserImg = (ImageView)findViewById(R.id.chatUserImage);
        lastSeen = (TextView)findViewById(R.id.chatUserLastSeen);
        chatUserTitle.setText(chatUserName);
        usersDB.child(chatUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //"true" or the last online time
                String onlineStatus = dataSnapshot.child("online").getValue().toString();
                if(onlineStatus.equals("true")) {
                    lastSeen.setText("Online Now");
                } else {
                    lastSeen.setText(onlineStatus);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
