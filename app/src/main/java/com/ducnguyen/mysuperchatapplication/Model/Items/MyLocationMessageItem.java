package com.ducnguyen.mysuperchatapplication.Model.Items;

import com.ducnguyen.mysuperchatapplication.MainActivity;

public class MyLocationMessageItem extends BaseMessageItem {
    double mLat;
    double mLong;
    String message;

    public MyLocationMessageItem(double mLat, double mLong, String timestamp, long realtimestamp) {
        this.mLat = mLat;
        this.mLong = mLong;
        this.username = MainActivity.currentUser.getUsername();
        this.timestamp = timestamp;
        this.realtimestamp = realtimestamp;
        this.message = this.username + " đã chia sẻ một vị trí" + '\n' + "Bấm vào để xem.";
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "My Location: " + this.username + ", " + this.mLat + ", " + this.mLong + ", " + this.timestamp;
    }
}
