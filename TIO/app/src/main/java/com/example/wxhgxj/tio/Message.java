package com.example.wxhgxj.tio;

public class Message {
    private String content;
    private String time;
    private String type;
    private boolean seen;
    private String from;

    public Message(String content, String time, String type, boolean seen, String from) {
        this.content = content;
        this.time = time;
        this.type = type;
        this.seen = seen;
        this.from = from;
    }

    public Message() {}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

}
