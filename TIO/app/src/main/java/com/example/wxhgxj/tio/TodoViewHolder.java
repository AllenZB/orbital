package com.example.wxhgxj.tio;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class TodoViewHolder extends RecyclerView.ViewHolder{

    private View todoView;

    public TodoViewHolder(View itemView) {
        super(itemView);

        todoView = itemView;
    }

    public void setContent(String content) {
        TextView todoContent = (TextView) todoView.findViewById(R.id.todoContent);
        todoContent.setText(content);
    }

    public Button getEditButton() {
        return (Button)todoView.findViewById(R.id.editTodobutton);
    }

    public Button getDoneButton() {
        return (Button)todoView.findViewById(R.id.doneTodoButton);
    }

    public void showUpdateContent() {
        todoView.findViewById(R.id.newTodoContent).setVisibility(View.VISIBLE);
    }

    public void hideUpdateContent() {
        todoView.findViewById(R.id.newTodoContent).setVisibility(View.GONE);
    }

}
