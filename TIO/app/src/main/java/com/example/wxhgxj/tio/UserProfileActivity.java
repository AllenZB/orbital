package com.example.wxhgxj.tio;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserProfileActivity extends AppCompatActivity {

    private TextView mUserName;
    private TextView mUserEmail;
    private Button contactOption;
    private DatabaseReference usersDB;
    private DatabaseReference selectedUserDB;
    private FirebaseUser currentUser;
    private DatabaseReference contactsDatabase;
    private boolean inContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mUserName = (TextView)findViewById(R.id.userNameProfile);
        mUserEmail = (TextView)findViewById(R.id.userEmailProfile);
        contactOption = (Button)findViewById(R.id.contactOption);
        final String selectedUserId = getIntent().getStringExtra("userId");
        usersDB = FirebaseDatabase.getInstance().getReference("Users");
        selectedUserDB = usersDB.child(selectedUserId);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUid = currentUser.getUid();
        contactsDatabase = usersDB.child(currentUid).child("Contacts");
        if(selectedUserId == currentUid) {
            contactOption.setVisibility(View.GONE);
        }

        contactsDatabase.child(selectedUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    inContact = true;
                    String text = "Remove from contacts";
                    contactOption.setText(text);
                } else {
                    inContact = false;
                    String text = "Add to contacts";
                    contactOption.setText(text);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        selectedUserDB.addValueEventListener(new ValueEventListener() {
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


        contactOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inContact) {
                    Log.v("Test", "try");
                    contactsDatabase.child(selectedUserId).removeValue();
                    String newText = "Add to contacts";
                    contactOption.setText(newText);
                    inContact = false;
                } else {
                    String time = Calendar.getInstance().getTime().toString();
                    contactsDatabase.child(selectedUserId).setValue(time);
                    String newText = "Remove from contacts";
                    contactOption.setText(newText);
                    inContact = true;
                }
            }
        });


    }
}
