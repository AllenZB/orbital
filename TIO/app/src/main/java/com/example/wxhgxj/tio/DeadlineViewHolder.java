package com.example.wxhgxj.tio;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DeadlineViewHolder extends RecyclerView.ViewHolder{

    private View deadlineView;
    private TextView deadlineTitle;
    private TextView newDeadlineTitle;
    private TextView deadlineTime;
    private TextView deadlineContent;
    private Button editDeadlineButton;
    private Button doneDeadlineButton;
    private EditText newDeadlineDate;
    private EditText newDeadlineTime;
    private EditText newDeadlineContent;
    private Button cancelDeadlineButton;
    private Button updateDeadlineButton;

    public DeadlineViewHolder(View itemView) {
        super(itemView);

        deadlineView = itemView;
        deadlineTitle = (TextView)deadlineView.findViewById(R.id.deadlineTimeTitle);
        newDeadlineTitle = (TextView)deadlineView.findViewById(R.id.newDeadlineTimeTitle);
        deadlineTime = (TextView)deadlineView.findViewById(R.id.deadlineTime);
        deadlineContent = (TextView)deadlineView.findViewById(R.id.deadlineContent);
        editDeadlineButton = (Button)deadlineView.findViewById(R.id.editDeadlineButton);
        doneDeadlineButton = (Button)deadlineView.findViewById(R.id.doneDeadlineButton);
        newDeadlineDate = (EditText)deadlineView.findViewById(R.id.editDeadlineDate);
        newDeadlineTime = (EditText)deadlineView.findViewById(R.id.editDeadlineTime);
        newDeadlineContent = (EditText)deadlineView.findViewById(R.id.editDeadlineContent);
        cancelDeadlineButton = (Button)deadlineView.findViewById(R.id.cancelDeadlineButton);
        updateDeadlineButton = (Button)deadlineView.findViewById(R.id.updateDeadlineButton);
    }

    public void setDeadline(String deadline) {
        deadlineTime.setText(deadline);
    }

    public void setDeadlineContent(String content) {
        deadlineContent.setText(content);
    }

    public EditText getNewDeadlineContent() {
        return newDeadlineContent;
    }

    public EditText getNewDeadlineTime() {
        return newDeadlineTime;
    }

    public EditText getNewDeadlineDate() {
        return newDeadlineDate;
    }

    public Button getCancelDeadlineButton() {
        return cancelDeadlineButton;
    }

    public Button getDoneDeadlineButton() {
        return doneDeadlineButton;
    }

    public Button getEditDeadlineButton() {
        return editDeadlineButton;
    }

    public Button getUpdateDeadlineButton() {
        return updateDeadlineButton;
    }

    public void hideDisplayDeadline() {
        deadlineTitle.setVisibility(View.GONE);
        deadlineTime.setVisibility(View.GONE);
        deadlineContent.setVisibility(View.GONE);
        editDeadlineButton.setVisibility(View.GONE);
        doneDeadlineButton.setVisibility(View.GONE);
    }

    public void showDisplayDeadline() {
        deadlineTitle.setVisibility(View.VISIBLE);
        deadlineTime.setVisibility(View.VISIBLE);
        deadlineContent.setVisibility(View.VISIBLE);
        editDeadlineButton.setVisibility(View.VISIBLE);
        doneDeadlineButton.setVisibility(View.VISIBLE);
    }

    public void hideEditDeadline() {
        newDeadlineTitle.setVisibility(View.GONE);
        newDeadlineTime.setVisibility(View.GONE);
        newDeadlineDate.setVisibility(View.GONE);
        newDeadlineContent.setVisibility(View.GONE);
        cancelDeadlineButton.setVisibility(View.GONE);
        updateDeadlineButton.setVisibility(View.GONE);
    }

    public void showEditDeadline() {
        newDeadlineTitle.setVisibility(View.VISIBLE);
        newDeadlineTime.setVisibility(View.VISIBLE);
        newDeadlineDate.setVisibility(View.VISIBLE);
        newDeadlineContent.setVisibility(View.VISIBLE);
        cancelDeadlineButton.setVisibility(View.VISIBLE);
        updateDeadlineButton.setVisibility(View.VISIBLE);
    }
}
