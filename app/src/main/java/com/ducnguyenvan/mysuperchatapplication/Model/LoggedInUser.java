package com.ducnguyenvan.mysuperchatapplication.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class LoggedInUser {
    @PrimaryKey
    @NonNull
    String username;


    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }
}
