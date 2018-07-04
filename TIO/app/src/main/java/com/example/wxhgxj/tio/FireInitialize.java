package com.example.wxhgxj.tio;

import android.app.Application;

import com.firebase.client.Firebase;

public class FireInitialize extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
