package com.ducnguyen.mysuperchatapplication.Model;

import android.support.annotation.Px;
import android.util.Log;

import com.ducnguyen.mysuperchatapplication.MainActivity;
import com.ducnguyen.mysuperchatapplication.Model.Items.BaseMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.MyAudioMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.MyImageMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.MyLocationMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.MyMessageTextItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.MyVideoMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.YourAudioMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.YourImageMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.YourLocationMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.YourMessageTextItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.YourVideoMessageItem;
import com.ducnguyen.mysuperchatapplication.R;

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

    public void mapToObject(Map<String, Object> map) {
        this.message = map.get("message").toString();
        this.name = map.get("name").toString();
        this.timestamp = map.get("timestamp").toString();
        this.realtimestamp = Long.parseLong(String.valueOf(map.get("realtimestamp")));
        this.convId = map.get("convId").toString();

    }

    @Override
    public String toString() {
        return ("message: [" + this.name + ", " + this.message + ", " + this.timestamp + "," + this.getRealtimestamp() + ", " + this.convId + "]");
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
        Message other = (Message) obj;
        return this.message.equals(other.message) &&
                this.name.equals(other.name) &&
                this.realtimestamp == other.realtimestamp &&
                this.convId.equals(other.convId);
    }

    public BaseMessageItem toMessageItem(boolean isFirst) {
        if (this.getMessage().contains("/-img:")) { //img message
            String imgName = this.getMessage().substring(6, this.getMessage().length());
            if (this.getName().equals(MainActivity.currentUser.getUsername())) {
                return (new MyImageMessageItem(this.getName(), imgName, this.getTimestamp(), this.getRealtimestamp(), false));
            } else {
                return (new YourImageMessageItem(R.drawable.avt, this.getName(), imgName, this.getTimestamp(), this.getRealtimestamp(), isFirst));
            }
        } else if (this.getMessage().contains("/-audio:")) { //audio message
            int durationIndex = this.message.indexOf("/-duration:");
            String audioName = this.getMessage().substring(8, durationIndex - 1);
            String duration = this.getMessage().substring(durationIndex + 11, this.getMessage().length());
            if (this.getName().equals(MainActivity.currentUser.getUsername())) {
                return (new MyAudioMessageItem(this.getName(), audioName, duration, this.getTimestamp(), this.getRealtimestamp()));
            } else {
                return (new YourAudioMessageItem(R.drawable.avt, this.getName(), audioName, duration, this.getTimestamp(), this.getRealtimestamp(), isFirst));
            }
        } else if (this.getMessage().contains("/-video:")) { //video message
            int durationIndex = this.message.indexOf("/-duration:");
            int heightIndex = this.message.indexOf("/-height:");
            int widthIndex = this.message.indexOf("/-width:");

            String videoName = this.getMessage().substring(8, durationIndex - 1);
            String duration = this.getMessage().substring(durationIndex + 11, heightIndex - 1);
            String heightString = this.getMessage().substring(heightIndex + 9, widthIndex - 1);
            String widthString = this.getMessage().substring(widthIndex + 8, this.getMessage().length());
            int heightPx = Integer.parseInt(heightString);
            int widthPx = Integer.parseInt(widthString);
            boolean isVertical = heightPx >= widthPx;
            Log.i("msgOrientation", "" + isVertical);

            //width = 150dp (vertical), 200(horizontal)
            float heightDp = isVertical ? heightPx * 150 / widthPx : heightPx * 200 / widthPx;
            float calculatedHeightPx = (heightDp * MainActivity.scale + 0.5f);
            float calculatedWidthPx = isVertical ? (150 * MainActivity.scale + 0.5f) : (200 * MainActivity.scale + 0.5f);
            if (this.getName().equals(MainActivity.currentUser.getUsername())) {
                return (new MyVideoMessageItem(this.getName(), videoName, duration, this.getTimestamp(), this.getRealtimestamp(), false, calculatedHeightPx, calculatedWidthPx));
            } else {
                return (new YourVideoMessageItem(R.drawable.avt, this.getName(), videoName, duration, this.getTimestamp(), this.getRealtimestamp(), isFirst, calculatedHeightPx, calculatedWidthPx));
            }
        } else if (this.getMessage().contains("/-lat:")) { //location message
            int longIndex = this.getMessage().indexOf("/-long:");
            String latString = this.getMessage().substring(6, longIndex - 1);
            String longString = this.getMessage().substring(longIndex + 7, this.getMessage().length());
            double mLat = Double.parseDouble(latString);
            double mLong = Double.parseDouble(longString);

            if (this.getName().equals(MainActivity.currentUser.getUsername())) {
                return (new MyLocationMessageItem(mLat, mLong, this.getTimestamp(), this.getRealtimestamp()));
            } else {
                return (new YourLocationMessageItem(R.drawable.avt, this.getName(), mLat, mLong, this.getTimestamp(), this.getRealtimestamp(), isFirst));
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
