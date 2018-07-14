package com.example.wxhgxj.tio;

public class Chat {
    private boolean Seen;
    private String Time;

    public Chat(boolean seen, String time) {
        this.Seen = seen;
        this.Time = time;
    }

    public Chat() {}

    public boolean isSeen() {
        return Seen;
    }

    public void setSeen(boolean seen) {
        this.Seen = seen;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }
}