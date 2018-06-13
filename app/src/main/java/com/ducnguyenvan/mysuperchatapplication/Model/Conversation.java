package com.ducnguyenvan.mysuperchatapplication.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Conversation {
    public String cId;
    public Message lastMessage;
    public String title;
    public ArrayList<String> members;

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public Conversation(String cId, Message lastMessage, String title, ArrayList<String> members) {
        this.cId = cId;
        this.lastMessage = lastMessage;
        this.title = title;
        this.members = members;
    }

    public Conversation() {
        this.cId = null;
        this.lastMessage = new Message();
        this.title = null;
        this.members = new ArrayList<>();
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

    public Map<String, Object> toMap() {
        HashMap<String,Object> result = new HashMap<>();
        result.put("cId", cId);
        result.put("lastMessage", lastMessage);
        result.put("title", title);
        result.put("members", members);

        return result;
    }

    public void mapToObject (Map<String,Object> map) {
        Log.i("map to obj", "" + map.get("cId"));
        if(map.get("cId") == null){
            return;
        }
        this.cId = map.get("cId").toString();
        this.lastMessage.mapToObject((HashMap<String, Object>) map.get("lastMessage"));
        this.title = map.get("title").toString();
        this.members = (ArrayList<String>)map.get("members");
    }
}

