package com.example.wxhgxj.tio;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.Console;


public class UsersActivity extends AppCompatActivity {

    private RecyclerView mUsersList;
    private DatabaseReference mUsersDB;
    private FirebaseRecyclerAdapter<User, UserViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mUsersList = (RecyclerView)findViewById(R.id.allUsersView);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));
        mUsersDB = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fireapp-1e8cc.firebaseio.com/Users");

        Query query = mUsersDB;
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
                //bind the data to the textview in the single user layout
                holder.setName(model.getName());
                //Log.v("name", model.getName());
                holder.setEmail(model.getEmail());
                //get the id of the current view
                final String userId = getRef(position).getKey();
                //add the onclick function to the view
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(userId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            Intent settingIntent = new Intent(UsersActivity.this, SettingActivity.class);
                            settingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(settingIntent);
                        } else {
                            Intent profileIntent = new Intent(UsersActivity.this, UserProfileActivity.class);
                            profileIntent.putExtra("userId", userId);
                            startActivity(profileIntent);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_user, parent, false);
                return new UserViewHolder(view);
            }
        };
        mUsersList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }
}
