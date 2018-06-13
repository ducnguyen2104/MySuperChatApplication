package com.ducnguyenvan.mysuperchatapplication.Model.Items;

import android.view.View;

public class YourMessageTextItem extends BaseMessageItem {
    public int img;
    public String message;
    public int visibility;

    public YourMessageTextItem() {
        this.img = 0;
        this.username = null;
        this.message = null;
        this.timestamp = null;
        this.realtimestamp = 0;
        this.visibility = View.VISIBLE;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public YourMessageTextItem(int img, String username, String message, String timestamp, long realtimestamp, boolean isFirst) {
        this.img = img;
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
        this.realtimestamp = realtimestamp;
        this.isFirst = isFirst;
        this.visibility = this.isFirst ? View.VISIBLE : View.INVISIBLE;
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
