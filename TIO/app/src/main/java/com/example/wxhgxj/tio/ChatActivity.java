package com.example.wxhgxj.tio;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class ChatActivity extends AppCompatActivity {

    String chatUserID;
    private DatabaseReference usersDB;
    private DatabaseReference rootRef;
    private FirebaseUser currentUser;
    private String currentUid;
    private ImageView chatUserImg;
    private EditText chatMsg;
    private TextView chatUserTitle;
    private TextView lastSeen;
    private RecyclerView messageView;
    private List<Message> messageList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    //limit the num of the message in each load;
    final private int pageMessageLimit = 15;
    private int currentPageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatUserID = getIntent().getStringExtra("uid");

        //get the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        //set the action bar with customize view
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.chatbar_custom, null);
        actionBar.setCustomView(actionBarView);
        //get some Firebase references
        rootRef = FirebaseDatabase.getInstance().getReference();
        usersDB = rootRef.child("Users");
        actionBar.setTitle(chatUserID);
        String chatUserName = getIntent().getStringExtra("chatUserName");
        //Log.v("name", chatUserName);
        //initialize widgets
        chatUserTitle = (TextView)findViewById(R.id.chatUserName);
        chatUserImg = (ImageView)findViewById(R.id.chatUserImage);
        lastSeen = (TextView)findViewById(R.id.chatUserLastSeen);
        //assign the chat user title
        chatUserTitle.setText(chatUserName);
        //assign the last-seen value
        usersDB.child(chatUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //"true" or the last online time
                String onlineStatus = dataSnapshot.child("online").getValue().toString();
                if(onlineStatus.equals("true")) {
                    lastSeen.setText("Online Now");
                } else {
                    lastSeen.setText("last seen at " + onlineStatus);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //get current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUid = currentUser.getUid();
        //add the chat log when start the chat
        rootRef.child("Chat").child(currentUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(chatUserID)) {
                    //initialize the chat info
                    Map chatAddMap = new HashMap();
                    chatAddMap.put("Seen", false);
                    chatAddMap.put("Time", getCurrentTime());
                    Map chatUserAddMap = new HashMap();
                    chatUserAddMap.put("Chat/" + currentUid + "/" + chatUserID, chatAddMap);
                    chatUserAddMap.put("Chat/" + chatUserID + "/" + currentUid, chatAddMap);

                    rootRef.updateChildren(chatUserAddMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if(databaseError != null) {
                                Log.d("ChatLog", databaseError.getMessage().toString());
                            }
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //initialize the message related widgets
        chatMsg = (EditText)findViewById(R.id.chatMessage);
        //initialize the swipe refresh layout
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.messageSwipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //increment the page index
                currentPageIndex ++;
                //prevent load show the msg repeatedly
                messageList.clear();
                loadMessage();
            }
        });
        ImageButton sendMsg = (ImageButton)findViewById(R.id.sendMessageButton);
        //the onclick function for send button
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        //initialize the message list
        messageView = (RecyclerView)findViewById(R.id.messageView);
        linearLayoutManager = new LinearLayoutManager(this);
        //messageList.clear();
        loadMessage();
        messageView.setHasFixedSize(true);
        messageView.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(messageList);
        messageView.setAdapter(messageAdapter);
    }

    private void loadMessage() {
        Query msgQuery = rootRef.child("Messages").child(currentUid).child(chatUserID)
                .limitToLast(currentPageIndex * pageMessageLimit);
        msgQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message msg = dataSnapshot.getValue(Message.class);
                messageList.add(msg);
                messageAdapter.notifyDataSetChanged();
                messageView.scrollToPosition(messageList.size() - 1);
                //stop refreshing and make the turing ring disappear
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Message msg = dataSnapshot.getValue(Message.class);
                messageList.remove(msg);
                messageAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
   }
    private void sendMessage() {
        String msg = chatMsg.getText().toString();
        if(!TextUtils.isEmpty(msg)) {
            chatMsg.setText("");
            //set the two reference for storing messages
            String currentUserRef = "Messages/" + currentUid + "/" + chatUserID;
            String chatUserRef = "Messages/" + chatUserID + "/" + currentUid;
            //get the push id for the current message
            String pushId = rootRef.child("Messages").child(currentUid).child(chatUserID).push().getKey();
            //set the message map
            Map messageAddMap = new HashMap();
            messageAddMap.put("content", msg);
            messageAddMap.put("seen", false);
            messageAddMap.put("type", "text");
            messageAddMap.put("time", getCurrentTime());
            messageAddMap.put("from", currentUid);
            //set the messages add user map
            Map messageAddUserMap = new HashMap();
            messageAddUserMap.put(currentUserRef + "/" + pushId, messageAddMap);
            messageAddUserMap.put(chatUserRef + "/" + pushId, messageAddMap);
            //update children
            rootRef.updateChildren(messageAddUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                   if(databaseError != null) {
                        Log.d("ChatMessageLog", databaseError.getMessage().toString());
                    }
               }
            });
        }
    }

    private String getCurrentTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(Calendar.getInstance().getTimeZone());
        df.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
        String currentTime = df.format(Calendar.getInstance().getTime());
        return currentTime;
    }
}
