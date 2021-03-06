package com.example.wxhgxj.tio;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    //define control widgets
    private EditText mLoginEmail;
    private EditText mLoginPassword;
    private Button mLoginButton;
    private Button mSignUpButton;
    private ProgressBar loginProgress;
    //define firebase auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize widgets
        mLoginEmail = (EditText)findViewById(R.id.loginEmail);
        mLoginPassword = (EditText)findViewById(R.id.loginPassword);
        mLoginButton = (Button)findViewById(R.id.loginButton);
        mSignUpButton = (Button)findViewById(R.id.signupButton);
        loginProgress = (ProgressBar)findViewById(R.id.loginProgress);
        loginProgress.setVisibility(View.INVISIBLE);
        //initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //check whether is logged in already

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProgress.setVisibility(View.VISIBLE);
                startSignIn();
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(registerIntent);
            }
        });
    }

    private void startSignIn() {
        String email = mLoginEmail.getText().toString();
        String password = mLoginPassword.getText().toString();
        //prevent user from leaving the text-field blank
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            loginProgress.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, "Fields Are Empty", Toast.LENGTH_LONG).show();
        } else {
            //proceed sign in operation
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()) {
                        loginProgress.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Sign In Problem", Toast.LENGTH_LONG).show();
                    }
                    loginProgress.setVisibility(View.GONE);
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            });
        }

    }
}
