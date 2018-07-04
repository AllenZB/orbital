package com.example.wxhgxj.tio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseOptions;


public class MainActivity extends AppCompatActivity {

    private Button msend;
    private EditText mValueField;
    private Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        mRef = new Firebase("https://fireapp-1e8cc.firebaseio.com/Users");
        msend = (Button) findViewById(R.id.add);
        mValueField = (EditText) findViewById(R.id.ValueField);
        msend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = mValueField.getText().toString();
                //Firebase mRefChild = mRef.child("Name");  add value to the specific child
                mRef.push().setValue(value);  //generate a unique and random child key
            }
        });
    }
}
