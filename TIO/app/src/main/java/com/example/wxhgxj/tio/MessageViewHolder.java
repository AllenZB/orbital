package com.example.wxhgxj.tio;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    private View mView;

    public MessageViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
    }

    public void setTime(String time) {
        TextView msgTime = (TextView)mView.findViewById(R.id.messageTime);
        msgTime.setText(time);
    }

    public void setContent(String content) {
        TextView msgContent = (TextView)mView.findViewById(R.id.messageContent);
        msgContent.setText(content);
    }
}
