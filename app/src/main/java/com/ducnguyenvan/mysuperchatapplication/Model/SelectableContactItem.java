package com.ducnguyenvan.mysuperchatapplication.Model;

public class SelectableContactItem {

    public int img;
    public String contactName;
    public boolean isSelected;

    public SelectableContactItem(int img, String contactName, boolean isSelected) {
        this.img = img;
        this.contactName = contactName;
        this.isSelected = isSelected;
    }

    public SelectableContactItem(int img, String contactName) {
        this.img = img;
        this.contactName = contactName;
        this.isSelected = false;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean selected) {
        isSelected = selected;
    }
}
