package com.example.wxhgxj.tio;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<Message> messageList;
    private FirebaseUser currentUser;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_message, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = currentUser.getUid();
        Message msg = messageList.get(position);
        String userFrom = msg.getFrom();
        TextView msgContent = holder.getMsgContentTextView();
        if(currentUid.equals(userFrom)) {
            msgContent.setBackgroundColor(Color.WHITE);
            msgContent.setTextColor(Color.parseColor("#000000"));
        } else {
            msgContent.setBackgroundColor(Color.YELLOW);
            msgContent.setTextColor(Color.parseColor("#fff17a0a"));
        }
        holder.setTime(msg.getTime());
        holder.setContent(msg.getContent());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
