package com.example.wxhgxj.tio;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class EventViewHolder extends RecyclerView.ViewHolder{

    private View eventView;

    public EventViewHolder(View itemView) {
        super(itemView);

        eventView = itemView;
    }

    public void setEventTitle(String title) {
        TextView eventTitle = (TextView)eventView.findViewById(R.id.eventTitle);
        eventTitle.setText(title);
    }

    public void setEventTime(String time) {
        TextView eventTime = (TextView)eventView.findViewById(R.id.eventTime);
        eventTime.setText(time);
    }

    public void setEventLocation(String location) {
        TextView eventLocation = (TextView)eventView.findViewById(R.id.eventLocation);
        eventLocation.setText(location);
    }

    public void setEventDescription(String description) {
        TextView eventDescription = (TextView)eventView.findViewById(R.id.eventDescription);
        eventDescription.setText(description);
    }

    public Button getEditButton() {
        return (Button) eventView.findViewById(R.id.editEventButton);
    }

    public Button getRemoveButton() {
        return (Button)eventView.findViewById(R.id.removeEventButton);
    }
}
