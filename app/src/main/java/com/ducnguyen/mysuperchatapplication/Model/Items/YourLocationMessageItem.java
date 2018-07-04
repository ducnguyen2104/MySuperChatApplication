package com.ducnguyen.mysuperchatapplication.Model.Items;

import android.view.View;

public class YourLocationMessageItem extends BaseMessageItem {

    public int img;
    public double mLat;
    public double mLong;
    String message;
    public int visibility;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public YourLocationMessageItem(int img, String username, double mLat, double mLong, String timestamp, long realtimestamp, boolean isFirst) {
        this.img = img;
        this.username = username;
        this.message = this.username + " đã chia sẻ một vị trí." + '\n' + "Bấm vào để xem.";
        this.timestamp = timestamp;
        this.realtimestamp = realtimestamp;
        this.isFirst = isFirst;
        this.visibility = this.isFirst ? View.VISIBLE : View.INVISIBLE;
        this.mLat = mLat;
        this.mLong = mLong;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getmLat() {
        return mLat;
    }

    public void setmLat(double mLat) {
        this.mLat = mLat;
    }

    public double getmLong() {
        return mLong;
    }

    public void setmLong(double mLong) {
        this.mLong = mLong;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return "Others Location: " + this.username + ", " + this.mLat + ", " + this.mLong + ", " + this.timestamp;
    }
}
