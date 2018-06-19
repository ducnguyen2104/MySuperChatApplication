package com.ducnguyenvan.mysuperchatapplication.Model.Items;

import android.databinding.Bindable;
import android.view.View;

import com.ducnguyenvan.mysuperchatapplication.BR;

public class MyImageMessageItem extends BaseMessageItem {
    @Bindable
    public String imgName; //img

    @Bindable
    public int statusTextViewVisibility; //visibility of the text view "Đang gửi..."

    @Bindable
    public int retryButtonVisibility; //visibility of retry button

    @Bindable
    public String status;

    public MyImageMessageItem(String username, String imgName, String timestamp, long realtimestamp, boolean isSending) {
        this.username = username;
        this.imgName = imgName;
        this.timestamp = timestamp;
        this.realtimestamp = realtimestamp;
        this.statusTextViewVisibility = (isSending? (View.VISIBLE) : View.GONE);
        this.retryButtonVisibility = View.INVISIBLE;
        this.status = "Đang gửi...";

    }

    public MyImageMessageItem() {
        this.username = null;
        this.imgName = null;
        this.timestamp = null;
        this.statusTextViewVisibility = (View.VISIBLE);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
        registry.notifyChange(this,BR.imgName);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatusTextViewVisibility() {
        return this.statusTextViewVisibility;
    }

    public void setStatusTextViewVisibility(int statusTextViewVisibility) {
        this.statusTextViewVisibility = (statusTextViewVisibility);
        registry.notifyChange(this, BR.statusTextViewVisibility);
    }

    public int getRetryButtonVisibility() {
        return retryButtonVisibility;
    }

    public void setRetryButtonVisibility(int retryButtonVisibility) {
        this.retryButtonVisibility = retryButtonVisibility;
        registry.notifyChange(this, BR.retryButtonVisibility);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        registry.notifyChange(this, BR.status);
    }
}

