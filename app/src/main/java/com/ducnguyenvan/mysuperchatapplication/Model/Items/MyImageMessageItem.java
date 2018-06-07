package com.ducnguyenvan.mysuperchatapplication.Model.Items;

public class MyImageMessageItem extends BaseMessageItem {
    public int message; //img

    public MyImageMessageItem(String username, int message, String timestamp) {
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
    }

    public MyImageMessageItem() {
        this.username = null;
        this.message = -1;
        this.timestamp = null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

