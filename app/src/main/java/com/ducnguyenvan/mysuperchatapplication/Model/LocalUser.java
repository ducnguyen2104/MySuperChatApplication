package com.ducnguyenvan.mysuperchatapplication.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.ducnguyenvan.mysuperchatapplication.LocalDB.MyConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(tableName = "users")
public class LocalUser {
    @PrimaryKey
    @NonNull
    public String username;
    public String password;
    public String fullname;
    public String phonenumber;
    @TypeConverters(MyConverter.class)
    public ArrayList<String> contacts;
    @TypeConverters(MyConverter.class)
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

    public User toUser() {
        return new User(this.username,this.password,this.fullname,this.phonenumber,this.contacts,this.conversations,this.avttimestamp);
    }

    @Override
    public String toString() {
        return ("Local user: [" + this.username + ", " + this.password + ", " + this.fullname + ", " + this.phonenumber + ", " + this.contacts + ", " + this.conversations + ", " + this.avttimestamp + "]");
    }
}
