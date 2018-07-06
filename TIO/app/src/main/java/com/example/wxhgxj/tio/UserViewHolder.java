package com.example.wxhgxj.tio;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class UserViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public UserViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
    }

    public void setName(String name) {
        TextView userName = mView.findViewById(R.id.userNameDisplay);
        userName.setText(name);
    }

    public void setEmail(String email) {
        TextView userEmail = mView.findViewById(R.id.userEmailDisplay);
        userEmail.setText(email);
    }
}
