package com.example.wxhgxj.tio;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    private View mContactView;
    private RecyclerView contactsList;
    private DatabaseReference mUsersDB;
    private DatabaseReference mContactsDB;
    private FirebaseRecyclerAdapter<Contacts, ContactViewHolder> firebaseRecyclerAdapter;


    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContactView = inflater.inflate(R.layout.fragment_contacts, container, false);
        contactsList = (RecyclerView)mContactView.findViewById(R.id.contactsList);
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUsersDB = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fireapp-1e8cc.firebaseio.com/Users");
        mContactsDB = mUsersDB.child(currentUid).child("Contacts");
        contactsList.setHasFixedSize(true);
        contactsList.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = mContactsDB;
        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(query, Contacts.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Contacts, ContactViewHolder>(options) {
            @NonNull
            @Override
            public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_contact, parent, false);
                return new ContactViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ContactViewHolder holder, int position, @NonNull Contacts model) {
                //get the id of the current view
                final String listUid = getRef(position).getKey();
                final ContactViewHolder currentHolder = holder;
                holder.setTime(model.getTime());
                mUsersDB.child(listUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String name = dataSnapshot.child("Name").getValue().toString();
                        String email = dataSnapshot.child("Email").getValue().toString();
                        String onlineStatus = dataSnapshot.child("online").getValue().toString();
                        //bind the data to the textview in the single user layout
                        currentHolder.setName(name);
                        currentHolder.setEmail(email);
                        currentHolder.setContactOnline(onlineStatus);
                        currentHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                chatIntent.putExtra("uid", listUid);
                                chatIntent.putExtra("chatUserName", name);
                                startActivity(chatIntent);
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //add the onclick function to the view
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "clicked" + listUid, Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        contactsList.setAdapter(firebaseRecyclerAdapter);
        return mContactView;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }



}
