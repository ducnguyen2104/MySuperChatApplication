package com.ducnguyenvan.mysuperchatapplication.Model;

public class ContactItem {

    public int img;
    public String contactName;

    public ContactItem(int img, String contactName) {
        this.img = img;
        this.contactName = contactName;
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
}
