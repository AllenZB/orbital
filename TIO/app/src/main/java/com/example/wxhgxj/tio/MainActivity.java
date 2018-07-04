package com.example.wxhgxj.tio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class MainActivity extends AppCompatActivity {

    private Button msend;
    private EditText mValueField;
    private ListView mListView;
    private Firebase mRef;
    private FirebaseListAdapter<String> firebaseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRef = new Firebase("https://fireapp-1e8cc.firebaseio.com/Users");
        msend = (Button) findViewById(R.id.add);
        mValueField = (EditText) findViewById(R.id.ValueField);
        mListView = (ListView) findViewById(R.id.listview);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fireapp-1e8cc.firebaseio.com/");
        Query query = databaseReference.child("Users");
        FirebaseListOptions<String> options = new FirebaseListOptions.Builder<String>()
                .setQuery(query, String.class)
                .setLayout(android.R.layout.simple_list_item_1)
                .build();
        firebaseListAdapter = new FirebaseListAdapter<String>(options) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView = (TextView) v.findViewById(android.R.id.text1);
                textView.setText(model);
            }
        };

        msend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = mValueField.getText().toString();
                //Firebase mRefChild = mRef.child("Name");  add value to the specific child
                mRef.push().setValue(value);  //generate a unique and random child key
            }
        });

        mListView.setAdapter(firebaseListAdapter);
    }

    @Override
    protected void onStart(){
        super.onStart();
        firebaseListAdapter.startListening();
    }
}
