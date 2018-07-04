package com.ducnguyen.mysuperchatapplication.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
        username = "";
        password = "";
        fullname = "";
        phonenumber = "";
        contacts = new ArrayList<>();
        conversations = new ArrayList<>();
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

    public User(String username, String password, String fullname, String phonenumber,
                ArrayList<String> contacts, ArrayList<String> conversations, long avttimestamp) {
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

    @Override
    public boolean equals(Object obj) {
        User other = (User)obj;
        Log.i("this", "" + this.toString()+ ", other: " + other.toString());
        return (this.username != null && this.password != null
                && this.fullname != null && this.phonenumber != null
                && this.contacts != null && this.conversations != null)
                && (this.username.equals(other.username)
                && this.password.equals(other.password)
                && this.fullname.equals(other.fullname)
                && this.phonenumber.equals(other.phonenumber)
                && this.avttimestamp == other.avttimestamp
                && equalLists(this.contacts, other.contacts)
                && equalLists(this.conversations, other.conversations));
    }

    public  boolean equalLists(List<String> a, List<String> b){
        // Check for sizes and nulls

        if (a == null && b == null) return true;


        if ((a == null && b!= null) || (a != null && b== null) || (a.size() != b.size()))
        {
            return false;
        }

        // Sort and compare the two lists
        Collections.sort(a);
        Collections.sort(b);
        return a.equals(b);
    }

    public LocalUser toLocalUser() {
        LocalUser localUser = new LocalUser();
        localUser.username = this.username;
        localUser.password = this.password;
        localUser.fullname = this.fullname;
        localUser.avttimestamp = this.avttimestamp;
        localUser.contacts = this.contacts;
        localUser.conversations = this.conversations;
        localUser.phonenumber = this.phonenumber;
        return localUser;
    }

    @Override
    public String toString() {
        return ("user: [" + this.username + ", " + this.password + ", " + this.fullname + ", " + this.phonenumber + ", " + this.contacts + ", " + this.conversations + ", " + this.avttimestamp + "]");
    }
}
