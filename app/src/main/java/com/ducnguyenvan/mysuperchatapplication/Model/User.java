package com.ducnguyenvan.mysuperchatapplication.Model;

import java.util.ArrayList;

public class User {
    public String username;
    public String password;
    public String fullname;
    public String phonenumber;
    public ArrayList<String> contacts;
    public ArrayList<String> conversations;

    public ArrayList<String> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<String> contacts) {
        this.contacts = contacts;
    }

    public ArrayList<String> getConversations() {
        return conversations;
    }

    public void setConversations(ArrayList<String> conversations) {
        this.conversations = conversations;
    }

    public User() {
        username = null;
        password = null;
        fullname = null;
        phonenumber = null;
        contacts = null;
        conversations = null;

    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String username, String password, String fullname, String phonenumber) {

        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.phonenumber = phonenumber;
        this.contacts = new ArrayList<>();
        this.conversations = new ArrayList<>();
    }

    public User(String username, String password, String fullname, String phonenumber, ArrayList<String> contacts, ArrayList<String> conversations) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.phonenumber = phonenumber;
        this.contacts = contacts;
        this.conversations = conversations;
    }
}
