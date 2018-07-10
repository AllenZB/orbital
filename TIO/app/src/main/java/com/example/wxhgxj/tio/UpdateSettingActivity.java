package com.example.wxhgxj.tio;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateSettingActivity extends AppCompatActivity {

    private EditText newUserName;
    private EditText oldUserPassword;
    private EditText newUserPassword;
    private Button updateAccount;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_setting);

        progressBar = (ProgressBar)findViewById(R.id.updateProgress);
        progressBar.setVisibility(View.INVISIBLE);
        newUserName = (EditText)findViewById(R.id.newUserName);
        oldUserPassword = (EditText) findViewById(R.id.oldUserPassword);
        newUserPassword = (EditText)findViewById(R.id.newUserPassword);
        updateAccount = (Button)findViewById(R.id.updateAccount);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUid = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);

        updateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String newName = newUserName.getText().toString();
                String oldPassword = oldUserPassword.getText().toString();
                final String newPassword = newUserPassword.getText().toString();
                databaseReference.child("Name").setValue(newName);
                final String email = currentUser.getEmail();
                AuthCredential credential = EmailAuthProvider.getCredential(email,oldPassword);
                currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            currentUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(!task.isSuccessful()){
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(UpdateSettingActivity.this, "Something is wrong", Toast.LENGTH_LONG).show();
                                    }else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(UpdateSettingActivity.this, "Successfully changed!", Toast.LENGTH_LONG).show();
                                        Intent settingIntent = new Intent(UpdateSettingActivity.this, SettingActivity.class);
                                        settingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(settingIntent);
                                    }
                                }
                            });
                        }else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(UpdateSettingActivity.this, "Authentication Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }
}
