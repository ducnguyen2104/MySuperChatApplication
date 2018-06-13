package com.ducnguyenvan.mysuperchatapplication.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    public String username;
    public String password;
    public String fullname;
    public String phonenumber;
    public ArrayList<String> contacts;
    public ArrayList<String> conversations;
    public long avttimestamp;

    public long getAvttimestamp() {
        return avttimestamp;
    }

    public void setAvttimestamp(long avttimestamp) {
        this.avttimestamp = avttimestamp;
    }

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
        avttimestamp = 0;
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
        this.avttimestamp = avttimestamp;
    }

    public User(String username, String password, String fullname, String phonenumber, ArrayList<String> contacts, ArrayList<String> conversations, long avttimestamp) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.phonenumber = phonenumber;
        this.contacts = contacts;
        this.conversations = conversations;
        this.avttimestamp = avttimestamp;
    }

    public void mapToObject(Map<String, Object> map) {
        this.username = map.get("username").toString();
        this.password = map.get("password").toString();
        this.fullname = map.get("fullname").toString();
        this.phonenumber = map.get("phonenumber").toString();
        HashMap<String,String> cont = (HashMap<String,String>)map.get("contacts");
        this.contacts = (cont != null ? new ArrayList<>(cont.values()) : new ArrayList<String>());
        HashMap<String,String> conv = (HashMap<String,String>)map.get("conversations");
        this.conversations = (conv != null ? new ArrayList<>(conv.values()) : new ArrayList<String>());
        this.avttimestamp = Long.parseLong(map.get("avttimestamp").toString());
    }
}
