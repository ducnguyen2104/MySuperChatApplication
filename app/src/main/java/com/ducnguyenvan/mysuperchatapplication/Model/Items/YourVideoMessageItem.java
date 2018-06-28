package com.ducnguyenvan.mysuperchatapplication.Model.Items;

import android.databinding.Bindable;
import android.view.View;

import com.ducnguyenvan.mysuperchatapplication.BR;

public class YourVideoMessageItem extends BaseMessageItem {
    public int img; //i mean... avatar
    public String videoName; //i mean... img
    @Bindable
    public int visibility;
    @Bindable
    public String duration;

    public YourVideoMessageItem(int img, String username, String videoName, String duration, String timestamp, long realTimestamp, boolean isFirst) {
        this.img = img;
        this.username = username;
        this.videoName = videoName;
        this.duration = duration;
        this.timestamp = timestamp;
        this.isFirst = isFirst;
        this.realtimestamp = realTimestamp;
        this.setVisibility(isFirst ? View.VISIBLE : View.INVISIBLE);
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
        registry.notifyChange(this, BR.visibility);
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
        registry.notifyChange(this, BR.duration);
    }
}
