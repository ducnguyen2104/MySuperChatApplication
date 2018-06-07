package com.ducnguyenvan.mysuperchatapplication.Model.Items;

public class ContactItem implements Item{

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

    @Override
    public boolean equalsContent(Object other) {
        return  other instanceof ContactItem &&
               ((ContactItem) other).contactName.equals(this.contactName) &&
                ((ContactItem) other).img == this.img;
    }

    @Override
    public boolean equals(Object other) {
        return this == other;
    }
}

