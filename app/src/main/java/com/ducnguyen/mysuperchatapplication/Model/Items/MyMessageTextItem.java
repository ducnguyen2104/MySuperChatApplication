package com.ducnguyen.mysuperchatapplication.Model.Items;

public class MyMessageTextItem extends BaseMessageItem {
    public String message;

    public MyMessageTextItem(String username, String message, String timestamp, long realtimestamp) {
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
        this.realtimestamp = realtimestamp;
    }

    public MyMessageTextItem() {
        this.username = null;
        this.message = null;
        this.timestamp = null;
        this.realtimestamp = 0;
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

    @Override
    public String toString() {
        return "My Text: " + this.message + ", " + this.timestamp;
    }
}
