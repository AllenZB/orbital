package com.example.wxhgxj.tio;

public class TodoEvent {
    private String Content;

    public TodoEvent(String content) {
        Content = content;
    }

    public TodoEvent() {};

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
