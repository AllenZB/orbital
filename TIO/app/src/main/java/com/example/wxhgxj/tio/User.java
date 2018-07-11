package com.example.wxhgxj.tio;

import java.util.HashMap;

public class User {




    private String Name;
    private String Email;
    private HashMap<String, Contacts> Contacts;

    public User() {

    }

    public User(String name, String email) {
        Name = name;
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public HashMap<String, Contacts> getContacts() {
        return Contacts;
    }

    public void setContacts(HashMap<String, Contacts> contacts) {
        this.Contacts = contacts;
    }
}
