package com.ducnguyenvan.mysuperchatapplication.Model.Items;

import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

public abstract class BaseMessageItem implements Observable, Item  {

    public String username;
    public String timestamp;
    public long realtimestamp;
    public boolean isFirst;
    protected PropertyChangeRegistry registry = new PropertyChangeRegistry();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getRealtimestamp() {
        return realtimestamp;
    }

    public void setRealtimestamp(long realtimestamp) {
        this.realtimestamp = realtimestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(boolean first) {
        isFirst = first;
    }

    @Override
    public boolean equalsContent(Object other) {
        return other instanceof BaseMessageItem &&
                ((BaseMessageItem) other).username.equals(this.username) &&
                ((BaseMessageItem) other).realtimestamp == this.realtimestamp;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.remove(callback);
    }
}
