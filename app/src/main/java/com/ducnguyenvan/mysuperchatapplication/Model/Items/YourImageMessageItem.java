package com.ducnguyenvan.mysuperchatapplication.Model.Items;

import android.view.View;

public class YourImageMessageItem extends BaseMessageItem {
    public int img; //i mean... avatar
    public String imgName; //i mean... img
    public int visibility;

    public YourImageMessageItem(int img, String username, String imgName, String timestamp, long realTimestamp, boolean isFirst) {
        this.img = img;
        this.username = username;
        this.imgName = imgName;
        this.timestamp = timestamp;
        this.isFirst = isFirst;
        this.realtimestamp = realTimestamp;
        this.visibility = isFirst ? View.VISIBLE : View.INVISIBLE;
    }

    public YourImageMessageItem() {
        this.username = null;
        this.imgName = null;
        this.timestamp = null;
        this.isFirst = true;
        this.visibility = View.VISIBLE;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
}
