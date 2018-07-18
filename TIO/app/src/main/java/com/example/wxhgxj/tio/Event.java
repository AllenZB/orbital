package com.example.wxhgxj.tio;




public class Event {
    private String Title;
    private String Date;
    private String Time;
    private String Description;
    private String Location;

    public Event(String title, String date, String time, String description, String location) {
        Title = title;
        Date = date;
        Time = time;
        Description = description;
        Location = location;
    }

    public Event() {}

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
