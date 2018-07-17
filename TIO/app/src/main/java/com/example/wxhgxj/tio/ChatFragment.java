package com.example.wxhgxj.tio;
//this one is the activity responding to the real chatting functionality

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private View chatView;
    private RecyclerView chatConversationList;
    private FirebaseUser currentUser;
    private DatabaseReference usersDB;
    private DatabaseReference chatDB;
    private DatabaseReference messagesDB;
    private DatabaseReference rootRef;
    private FirebaseRecyclerAdapter<Chat, ChatViewHolder> firebaseRecyclerAdapter;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chatView = inflater.inflate(R.layout.fragment_chat, container, false);
        chatConversationList = (RecyclerView)chatView.findViewById(R.id.chatConversationView);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = currentUser.getUid();
        chatConversationList.setHasFixedSize(true);
        chatConversationList.setLayoutManager(new LinearLayoutManager(getContext()));
        rootRef = FirebaseDatabase.getInstance().getReference();
        usersDB = rootRef.child("Users");
        chatDB = rootRef.child("Chat").child(currentUid);
        messagesDB = rootRef.child("Messages").child(currentUid);
        //get all the chat conversation which are not seen
        Query query = chatDB.orderByChild("Seen").equalTo(false);
        FirebaseRecyclerOptions<Chat> options = new FirebaseRecyclerOptions.Builder<Chat>()
                .setQuery(query, Chat.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Chat, ChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ChatViewHolder holder, int position, @NonNull Chat model) {
                //get the current item ID
                final String chatUid = getRef(position).getKey();
                //Log.v("chatUid", chatUid);
                //Log.v("check ref", usersDB.child(chatUid).toString());
                //get the chatUserName
                usersDB.child(chatUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String name = dataSnapshot.child("Name").getValue().toString();
                        String onlineStauts = dataSnapshot.child("online").getValue().toString();
                        holder.setChatFromName(name);
                        //visualize the online dot if user is online
                        holder.setChatUserOnline(onlineStauts);
                        //set the on click function to redirect to the chat page
                        holder.chatView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                chatIntent.putExtra("uid", chatUid);
                                chatIntent.putExtra("chatUserName", name);
                                startActivity(chatIntent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //get the last message content
                Query msgQuery = messagesDB.child(chatUid).limitToLast(1);
                msgQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //get the content and the time of the last message
                        String content = dataSnapshot.child("content").getValue().toString();
                        holder.setLastChatContent(content);
                        String time = dataSnapshot.child("time").getValue().toString();
                        holder.setLastChatTime(time);
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

            }

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_chat, parent, false);
                return new ChatViewHolder(view);
            }
        };
        chatConversationList.setAdapter(firebaseRecyclerAdapter);
        return chatView;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

}
