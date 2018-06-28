package com.ducnguyenvan.mysuperchatapplication.Model.Items;

import android.databinding.Bindable;
import android.view.View;

import com.ducnguyenvan.mysuperchatapplication.BR;

public class MyVideoMessageItem extends BaseMessageItem {
    @Bindable
    public String videoName; //img

    @Bindable
    public int vidStatusTextViewVisibility; //visibility of the text view "Đang gửi..."

    @Bindable
    public String duration;

    public MyVideoMessageItem(String username, String videoName, String duration, String timestamp, long realtimestamp, boolean isSending) {
        this.username = username;
        this.videoName = videoName;
        this.duration = duration;
        this.timestamp = timestamp;
        this.realtimestamp = realtimestamp;
        this.vidStatusTextViewVisibility = (isSending? (View.VISIBLE) : View.GONE);
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
        registry.notifyChange(this, BR.videoName);
    }

    public int getVidStatusTextViewVisibility() {
        return vidStatusTextViewVisibility;
    }

    public void setVidStatusTextViewVisibility(int vidStatusTextViewVisibility) {
        this.vidStatusTextViewVisibility = vidStatusTextViewVisibility;
        registry.notifyChange(this,BR.vidStatusTextViewVisibility);
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
        registry.notifyChange(this,BR.duration);
    }
}
