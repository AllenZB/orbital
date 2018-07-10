package com.example.wxhgxj.tio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    //define widgets
    private EditText registerName;
    private EditText registerEmail;
    private EditText registerPassword;
    private Button registerButton;
    private ProgressBar registerProgress;
    //define Firebase Auth
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //initialize widgets
        registerName = (EditText)findViewById(R.id.registerName);
        registerEmail = (EditText)findViewById(R.id.registerEmail);
        registerPassword = (EditText)findViewById(R.id.registerPassword);
        registerButton = (Button)findViewById(R.id.registerButton);
        registerProgress = (ProgressBar)findViewById(R.id.registerProgress);
        registerProgress.setVisibility(View.INVISIBLE);
        //initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        //register button on-click function
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerProgress.setVisibility(View.VISIBLE);
                startRegister();
            }
        });
    }

    //function for registering
    private void startRegister() {
        final String name = registerName.getText().toString();
        final String email = registerEmail.getText().toString();
        String password = registerPassword.getText().toString();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUerIdDB = mDatabase.child(userId);
                        currentUerIdDB.child("Name").setValue(name);
                        currentUerIdDB.child("Email").setValue(email);
                        registerProgress.setVisibility(View.GONE);
                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    } else {
                        registerProgress.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this, "Some Errors Occur!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            registerProgress.setVisibility(View.GONE);
            Toast.makeText(RegisterActivity.this, "Fields Are Empty!", Toast.LENGTH_LONG).show();
        }
    }
}
