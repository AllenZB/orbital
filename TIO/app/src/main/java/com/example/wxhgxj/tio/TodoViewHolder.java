package com.example.wxhgxj.tio;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class TodoViewHolder extends RecyclerView.ViewHolder{

    private View todoView;
    private TextView todoContent;
    private Button editButton;
    private Button doneButton;
    private EditText newTodoContent;
    private Button updateButton;
    private Button cancelButton;

    public TodoViewHolder(View itemView) {
        super(itemView);

        todoView = itemView;
        todoContent = (TextView)todoView.findViewById(R.id.todoContent);
        editButton = (Button)todoView.findViewById(R.id.editTodoButton);
        doneButton = (Button)todoView.findViewById(R.id.doneTodoButton);
        newTodoContent = (EditText)todoView.findViewById(R.id.editTodoContent);
        updateButton = (Button)todoView.findViewById(R.id.updateTodoEvent);
        cancelButton = (Button)todoView.findViewById(R.id.cancelTodoInput);
    }

    public void setContent(String content) {
        todoContent.setText(content);
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getDoneButton() {
        return doneButton;
    }

    public EditText getNewTodoContent() {
        return newTodoContent;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void hideUpdate() {
        newTodoContent.setVisibility(View.GONE);
        updateButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
    }

    public void showUpdate() {
        newTodoContent.setVisibility(View.VISIBLE);
        updateButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
    }

    public void hideDisplay() {
        todoContent.setVisibility(View.GONE);
        editButton.setVisibility(View.GONE);
        doneButton.setVisibility(View.GONE);
    }

    public void showDisplay() {
        todoContent.setVisibility(View.VISIBLE);
        editButton.setVisibility(View.VISIBLE);
        doneButton.setVisibility(View.VISIBLE);
    }

}
