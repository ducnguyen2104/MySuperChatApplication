package com.ducnguyen.mysuperchatapplication.Model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.ducnguyen.mysuperchatapplication.LocalDB.MyConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "conversations")
public class LocalConversation {

    @PrimaryKey
    @NonNull
    public String cId;
    @Embedded
    public Message lastMessage;
    public String title;
    @TypeConverters(MyConverter.class)
    public ArrayList<String> members;
    public boolean isUploaded;

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

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public Map<String, Object> toMap() {
        HashMap<String,Object> result = new HashMap<>();
        result.put("cId", cId);
        result.put("lastMessage", lastMessage);
        result.put("title", title);
        result.put("members", members);
        return result;
    }

    public Conversation toConversation() {
        Conversation conversation = new Conversation();
        conversation.cId = this.cId;
        conversation.lastMessage = this.lastMessage;
        conversation.title = this.title;
        conversation.members = this.members;
        return conversation;
    }


    @Override
    public String toString() {
        return ("Local conversation: [" + this.cId + ", " + this.title + ", " + this.members + ", " + this.lastMessage + ", " + this.isUploaded + "]");
    }
}
