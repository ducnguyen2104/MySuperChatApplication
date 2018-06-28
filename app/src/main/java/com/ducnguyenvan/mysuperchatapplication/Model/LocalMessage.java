package com.ducnguyenvan.mysuperchatapplication.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.Map;



@Entity(tableName = "messages",
        primaryKeys = {"name", "realtimestamp"})
public class LocalMessage {
    public String message;
    @NonNull
    public String name;
    public String timestamp;
    @NonNull
    public long realtimestamp;
    public String convId;
    public boolean isUploaded;

    public long getRealtimestamp() {
        return realtimestamp;
    }

    public void setRealtimestamp(long realtimestamp) {
        this.realtimestamp = realtimestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getConvId() {
        return convId;
    }

    public void setConvId(String convId) {
        this.convId = convId;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    @Override
    public String toString() {
        return ("Local message: [" + this.name + ", " + this.message + ", " + this.timestamp + ", " + isUploaded + "]");
    }

    public Message toMessage() {
        Message msg = new Message();
        msg.message = this.message;
        msg.name = this.name;
        msg.timestamp = this.timestamp;
        msg.realtimestamp = this.realtimestamp;
        msg.convId = this.convId;
        return msg;
    }
}
