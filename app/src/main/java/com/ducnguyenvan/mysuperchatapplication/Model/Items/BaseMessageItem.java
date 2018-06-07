package com.ducnguyenvan.mysuperchatapplication.Model.Items;

public abstract class BaseMessageItem implements Item{
    public String username;
    public String timestamp;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equalsContent(Object other) {
        return other instanceof BaseMessageItem &&
                ((BaseMessageItem) other).username.equals(this.username) &&
                ((BaseMessageItem) other).timestamp.equals(this.timestamp);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }
}
