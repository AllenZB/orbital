package com.example.wxhgxj.tio;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class UserProfileActivity extends AppCompatActivity {

    private TextView mUserName;
    private TextView mUserEmail;
    private DatabaseReference firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mUserName = (TextView)findViewById(R.id.userNameProfile);
        mUserEmail = (TextView)findViewById(R.id.userEmailProfile);
        String userId = getIntent().getStringExtra("userId");
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("Name").getValue().toString();
                String userEmail = dataSnapshot.child("Email").getValue().toString();
                mUserName.setText(userName);
                mUserEmail.setText(userEmail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
