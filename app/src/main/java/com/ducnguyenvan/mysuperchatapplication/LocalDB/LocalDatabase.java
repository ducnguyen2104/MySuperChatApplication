package com.ducnguyenvan.mysuperchatapplication.LocalDB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.ducnguyenvan.mysuperchatapplication.Model.LocalConversation;
import com.ducnguyenvan.mysuperchatapplication.Model.LocalMessage;
import com.ducnguyenvan.mysuperchatapplication.Model.LocalUser;
import com.ducnguyenvan.mysuperchatapplication.Model.LoggedInUser;

@Database(entities = {LocalUser.class, LocalConversation.class, LocalMessage.class, LoggedInUser.class}, version = 1)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract LocalDBDao localDBDao();
}
