package com.example.wxhgxj.tio;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ChatViewHolder extends RecyclerView.ViewHolder {
    View chatView;

    public ChatViewHolder(View itemView) {
        super(itemView);

        this.chatView = itemView;
    }

    public void setChatFromName(String name) {
        TextView chatUser = (TextView)chatView.findViewById(R.id.chatFromUser);
        chatUser.setText(name);
    }

    public void setLastChatTime(String time) {
        TextView lastTime = (TextView)chatView.findViewById(R.id.lastChatTime);
        lastTime.setText(time);
    }

    public void setLastChatContent(String content) {
        TextView lastContent = (TextView)chatView.findViewById(R.id.lastChatContent);
        lastContent.setText(content);
    }

    public void setChatUserOnline(String onlineStatus) {
        ImageView chatUserOnline = (ImageView)chatView.findViewById(R.id.chatUserOnline);
        if(onlineStatus.equals("true")) {
            chatUserOnline.setVisibility(View.VISIBLE);
        } else {
            chatUserOnline.setVisibility(View.INVISIBLE);
        }
    }
}
