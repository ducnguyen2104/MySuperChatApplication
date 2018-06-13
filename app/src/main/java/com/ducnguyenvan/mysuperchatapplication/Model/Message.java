package com.ducnguyenvan.mysuperchatapplication.Model;

import java.util.Map;

public class Message {
    private String message;
    private String name;
    private String timestamp;
    private long realtimestamp;

    public Message(String message, String name, String timestamp, long realtimestamp) {
        this.message = message;
        this.name = name;
        this.timestamp = timestamp;
        this.realtimestamp = realtimestamp;
    }

    public long getRealtimestamp() {
        return realtimestamp;
    }

    public void setRealtimestamp(long realtimestamp) {
        this.realtimestamp = realtimestamp;
    }

    public Message() {
        this.message = null;
        this.name = null;
        this.timestamp = null;
        this.realtimestamp = 0;

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

    public void mapToObject(Map<String,Object> map) {
        this.message = map.get("message").toString();
        this.name = map.get("name").toString();
        this.timestamp = map.get("timestamp").toString();
        this.realtimestamp = Long.parseLong(String.valueOf(map.get("realtimestamp")));
    }
}
