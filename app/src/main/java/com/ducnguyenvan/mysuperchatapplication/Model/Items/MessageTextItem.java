package com.ducnguyenvan.mysuperchatapplication.Model.Items;

public class MessageTextItem extends BaseMessageItem {
    public int img;
    public String message;

    public MessageTextItem() {
        this.img = 0;
        this.username = null;
        this.message = null;
        this.timestamp = null;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public MessageTextItem(int img, String username, String message, String timestamp) {
        this.img = img;
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;

    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
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
}
