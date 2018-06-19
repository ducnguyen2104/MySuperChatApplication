package com.ducnguyenvan.mysuperchatapplication.Model;

import android.util.Log;

import com.ducnguyenvan.mysuperchatapplication.MainActivity;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.ConversationItem;
import com.ducnguyenvan.mysuperchatapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Conversation {

    public String cId;
    public Message lastMessage;
    public String title;
    public ArrayList<String> members;

    public Conversation( String cId, Message lastMessage, String title, ArrayList<String> members) {
        this.cId = cId;
        this.lastMessage = lastMessage;
        this.title = title;
        this.members = members;
    }

    public Conversation() {
        this.cId = "";
        this.lastMessage = new Message();
        this.title = "";
        this.members = new ArrayList<>();
    }

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
        if(map.get("cId") == null) return;
        this.cId = map.get("cId").toString();
        this.lastMessage.mapToObject((HashMap<String, Object>) map.get("lastMessage"));
        this.title = map.get("title").toString();
        this.members = (ArrayList<String>)map.get("members");
    }

    public ConversationItem makeConversationItem() {
        StringBuilder membersToStringBuilder = new StringBuilder();
        for(String s : this.getMembers()) {
            if(membersToStringBuilder.length() == 0) {
                membersToStringBuilder.append(s);
            }
            else {
                membersToStringBuilder.append(", " + s);
            }
        }
        String name = this.getMembers().size() == 2 ?
                (MainActivity.currentUser.getUsername().equals(this.members.get(0)) ? this.members.get(1) : this.members.get(0)) :
                membersToStringBuilder.toString();
        String lastMsg = this.getLastMessage().getMessage().contains("/-img:") ? this.getLastMessage().getName() + " đã gửi một ảnh"
                : (this.getLastMessage().getName()+ ": "+ this.getLastMessage().getMessage());
        return new ConversationItem(R.drawable.avt, this.getcId(), name, lastMsg,this.getLastMessage().getTimestamp()+"");
    }

    public LocalConversation toLocalConversation() {
        LocalConversation conversation = new LocalConversation();
        conversation.cId = this.cId;
        conversation.lastMessage = this.lastMessage;
        conversation.title = this.title;
        conversation.members = this.members;
        return conversation;
    }

    @Override
    public String toString() {
        return ("conversation: [" + this.cId + ", " + this.title + ", " + this.members + ", " + this.lastMessage + "]");
    }

    @Override
    public boolean equals(Object obj) {
        Conversation other = (Conversation)obj;
        return this.cId.equals(other.cId) &&
                this.lastMessage.equals(other.lastMessage) &&
                this.title.equals(other.title) &&
                equalLists(this.members, other.members);
    }

    public  boolean equalLists(List<String> a, List<String> b){
        // Check for sizes and nulls
        if (a == null && b == null) return true;
        if ((a == null && b!= null) || (a != null && b== null) || (a.size() != b.size()))
            return false;
        // Sort and compare the two lists
        Collections.sort(a);
        Collections.sort(b);
        return a.equals(b);
    }
}

