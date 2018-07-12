package com.example.wxhgxj.tio;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class TIO extends Application {

    private DatabaseReference mUsersDB;
    private DatabaseReference currentUserDB;
    private FirebaseUser currentUser;

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);

        if(!FirebaseApp.getApps(this).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mUsersDB = FirebaseDatabase.getInstance().getReference("Users");
        if(currentUser != null) {
            Log.v("CHECK", "Inside");
            final String currentUid = currentUser.getUid();
            currentUserDB = mUsersDB.child(currentUid);
            Log.v("DB", currentUserDB.toString());
            currentUserDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot != null) {
                        Log.v("UID", currentUid);
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        df.setTimeZone(Calendar.getInstance().getTimeZone());
                        df.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
                        String date = df.format(Calendar.getInstance().getTime());
                        currentUserDB.child("online").onDisconnect().setValue(date);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
