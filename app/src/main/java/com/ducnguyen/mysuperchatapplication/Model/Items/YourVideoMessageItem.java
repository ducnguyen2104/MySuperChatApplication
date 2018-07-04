package com.ducnguyen.mysuperchatapplication.Model.Items;

import android.databinding.Bindable;
import android.view.View;

import com.ducnguyen.mysuperchatapplication.BR;

public class YourVideoMessageItem extends BaseMessageItem {

    public int img; //i mean... avatar

    public String videoName; //i mean... img

    @Bindable
    public int visibility;

    @Bindable
    public String duration;

    @Bindable
    public int thumbnailVisibility;

    @Bindable
    public int playerVisibility;

    @Bindable
    public float height;

    @Bindable
    public float width;

    public YourVideoMessageItem(int img, String username, String videoName, String duration, String timestamp, long realTimestamp, boolean isFirst, float height, float width) {
        this.img = img;
        this.username = username;
        this.videoName = videoName;
        this.duration = duration;
        this.timestamp = timestamp;
        this.isFirst = isFirst;
        this.realtimestamp = realTimestamp;
        this.setVisibility(isFirst ? View.VISIBLE : View.INVISIBLE);
        this.thumbnailVisibility = View.VISIBLE;
        this.playerVisibility = View.GONE;
        this.height = height;
        this.width = width;
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

    public int getThumbnailVisibility() {
        return thumbnailVisibility;
    }

    public void setThumbnailVisibility(int thumbnailVisibility) {
        this.thumbnailVisibility = thumbnailVisibility;
        registry.notifyChange(this,BR.thumbnailVisibility);
    }

    public int getPlayerVisibility() {
        return playerVisibility;
    }

    public void setPlayerVisibility(int playerVisibility) {
        this.playerVisibility = playerVisibility;
        registry.notifyChange(this,BR.playerVisibility);
    }

    public void playVideo() {
        setThumbnailVisibility(View.GONE);
        setPlayerVisibility(View.VISIBLE);
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
        registry.notifyChange(this,BR.height);
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
        registry.notifyChange(this,BR.width);
    }

    @Override
    public String toString() {
        return "Others Video: " + this.username + ", " + this.videoName+ ", " + this.timestamp;
    }
}
