package com.ducnguyenvan.mysuperchatapplication.Model.Items;

public class MyMessageTextItem extends BaseMessageItem {
    public String message;

    public MyMessageTextItem(String username, String message, String timestamp) {
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
    }

    public MyMessageTextItem() {
        this.username = null;
        this.message = null;
        this.timestamp = null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
