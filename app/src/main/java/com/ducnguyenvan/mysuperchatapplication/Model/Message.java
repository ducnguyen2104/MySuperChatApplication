package com.ducnguyenvan.mysuperchatapplication.Model;

public class Message {
    private String message;
    private String name;
    private long timestamp;

    public Message(String message, String name, long timestamp) {
        this.message = message;
        this.name = name;
        this.timestamp = timestamp;
    }

    public Message() {
        this.message = null;
        this.name = null;
        this.timestamp = 0;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
