package com.ducnguyenvan.mysuperchatapplication.Model;

public class ConversationItem {

    private int img;
    private String conversationName;
    private String lastMsg;
    private String lastMsgTime;

    public ConversationItem(int img, String conversationName, String lastMsg, String lastMsgTime) {
        this.img = img;
        this.conversationName = conversationName;
        this.lastMsg = lastMsg;
        this.lastMsgTime = lastMsgTime;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(String lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }
}
