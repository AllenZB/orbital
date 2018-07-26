package com.example.wxhgxj.tio;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private FirebaseAuth mAuth;
    private DatabaseReference currentUserDB;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private MainPagerAdapter mainPagerAdapter;
    private ViewPager mainViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.open, R.string.close);
        mAuth = FirebaseAuth.getInstance();
        currentUserDB = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
        mToggle.setDrawerIndicatorEnabled(true);
        //add toggle to wave the drawLayout
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mainViewPager = (ViewPager)findViewById(R.id.mainViewPager);
        mainViewPager.setAdapter(mainPagerAdapter);

        //set the navigation bar
        final NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Toast.makeText(MainActivity.this, "test", Toast.LENGTH_LONG).show();
                switch(item.getItemId()) {
                    case R.id.logoutButton:
                        logout();
                        break;
                    case R.id.usersButton:
                        Intent usersIntent = new Intent(MainActivity.this, UsersActivity.class);
                        usersIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(usersIntent);
                        break;
                    case R.id.chatButton:
                        Intent chatSessionIntent = new Intent(MainActivity.this, ChatSectionActivity.class);
                        chatSessionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(chatSessionIntent);
                        break;
                    case R.id.settingButton:
                        Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                        settingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(settingIntent);
                        break;
                    case R.id.calendarButton:
                        Intent calendarIntent = new Intent(MainActivity.this, CalendarActivity.class);
                        calendarIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(calendarIntent);
                        break;
                }
                return true;
            }
        });
        //initialize mAuth Listener direct to login page
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        currentUserDB.child("online").setValue("true");
    }

    private void logout() {
        mAuth.signOut();
        Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(logoutIntent);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(Calendar.getInstance().getTimeZone());
        //as the time zone for the ADK is not correct
        df.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
        String date = df.format(Calendar.getInstance().getTime());
        currentUserDB.child("online").setValue(date);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

}
