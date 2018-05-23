package com.ducnguyenvan.mysuperchatapplication.Model;

public class Conversation {
    public String cId;
    public Message lastMessage;
    public String title;

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public Conversation(String cId, Message lastMessage, String title) {
        this.cId = cId;
        this.lastMessage = lastMessage;
        this.title = title;
    }

    public Conversation() {
        this.cId = null;
        this.lastMessage = null;
        this.title = null;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

