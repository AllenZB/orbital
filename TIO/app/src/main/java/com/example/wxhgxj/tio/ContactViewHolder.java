package com.example.wxhgxj.tio;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ContactViewHolder extends RecyclerView.ViewHolder {

    protected View mView;

    public ContactViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
    }

    public void setName(String name) {
        TextView contactName = (TextView)mView.findViewById(R.id.contactName);
        contactName.setText(name);
    }

    public void setEmail(String email) {
        TextView contactEmail = (TextView)mView.findViewById(R.id.contactEmail);
        contactEmail.setText(email);
    }

    public void setTime(String time) {
        TextView contactTime = (TextView)mView.findViewById(R.id.contactTime);
        contactTime.setText(time);
    }

    public void setContactOnline(String onlineStatus) {
        ImageView contactOnline = (ImageView)mView.findViewById(R.id.contactOnline);
        if(onlineStatus.equals("true")) {
            contactOnline.setVisibility(View.VISIBLE);
        } else {
            contactOnline.setVisibility(View.INVISIBLE);
        }
    }
}
