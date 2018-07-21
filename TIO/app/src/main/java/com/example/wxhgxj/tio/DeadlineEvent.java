package com.example.wxhgxj.tio;

public class DeadlineEvent {


    private String Content;
    private String DeadlineDate;
    private String DeadlineTime;

    public DeadlineEvent(String content, String date, String time) {
        Content = content;
        DeadlineDate = date;
        DeadlineTime = time;
    }

    public DeadlineEvent() {};

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }


    public String getDeadlineDate() {
        return DeadlineDate;
    }

    public void setDeadlineDate(String date) {
        DeadlineDate = date;
    }

    public String getDeadlineTime() {
        return DeadlineTime;
    }

    public void setDeadlineTime(String time) {
        DeadlineTime = time;
    }
}
