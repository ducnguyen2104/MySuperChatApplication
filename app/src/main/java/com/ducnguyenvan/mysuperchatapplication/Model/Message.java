package com.ducnguyenvan.mysuperchatapplication.Model;

import com.ducnguyenvan.mysuperchatapplication.MainActivity;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.BaseMessageItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.MyImageMessageItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.MyMessageTextItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.YourImageMessageItem;
import com.ducnguyenvan.mysuperchatapplication.Model.Items.YourMessageTextItem;
import com.ducnguyenvan.mysuperchatapplication.R;

import java.util.Map;

public class Message {
    public String message;
    public String name;
    public String timestamp;
    public long realtimestamp;
    public String convId;


    public Message(String message, String name, String timestamp, long realtimestamp, String convId) {
        this.message = message;
        this.name = name;
        this.timestamp = timestamp;
        this.realtimestamp = realtimestamp;
        this.convId = convId;
    }

    public long getRealtimestamp() {
        return realtimestamp;
    }

    public void setRealtimestamp(long realtimestamp) {
        this.realtimestamp = realtimestamp;
    }

    public Message() {
        this.message = "";
        this.name = "";
        this.timestamp = "";
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

    public String getConvId() {
        return convId;
    }

    public void setConvId(String convId) {
        this.convId = convId;
    }

    public void mapToObject(Map<String,Object> map) {
        this.message = map.get("message").toString();
        this.name = map.get("name").toString();
        this.timestamp = map.get("timestamp").toString();
        this.realtimestamp = Long.parseLong(String.valueOf(map.get("realtimestamp")));
        this.convId = map.get("convId").toString();

    }

    @Override
    public String toString() {
        return ("message: [" + this.name + ", " + this.message + ", " + this.timestamp + "]");
    }

    public LocalMessage toLocalMessage(boolean isUploaded) {
        LocalMessage msg = new LocalMessage();
        msg.message = this.message;
        msg.name = this.name;
        msg.timestamp = this.timestamp;
        msg.realtimestamp = this.realtimestamp;
        msg.convId = this.convId;
        msg.isUploaded = isUploaded;
        return msg;
    }

    @Override
    public boolean equals(Object obj) {
        Message other = (Message)obj;
        return this.message.equals(other.message) &&
                this.name.equals(other.name) &&
                this.realtimestamp == other.realtimestamp &&
                this.convId.equals(other.convId);
    }

    public BaseMessageItem toMessageItem(boolean isFirst) {
        if (this.getMessage().contains("/-img:")) { //img message
            String imgName = this.getConvId() + "/" + this.getMessage().substring(6, this.getMessage().length());
            if (this.getName().equals(MainActivity.currentUser.getUsername())) {
                return (new MyImageMessageItem(this.getName(), imgName, this.getTimestamp(), this.getRealtimestamp(), false));
            } else {
                return (new YourImageMessageItem(R.drawable.avt, this.getName(), imgName, this.getTimestamp(), this.getRealtimestamp(), isFirst));
            }
        } else { //text message
            if (this.getName().equals(MainActivity.currentUser.getUsername())) {
               return (new MyMessageTextItem(this.getName(), this.getMessage(), this.getTimestamp(), this.getRealtimestamp()));
            } else {
                return (new YourMessageTextItem(R.drawable.avt, this.getName(), this.getMessage(), this.getTimestamp(), this.getRealtimestamp(), isFirst));
            }
        }
    }
}
