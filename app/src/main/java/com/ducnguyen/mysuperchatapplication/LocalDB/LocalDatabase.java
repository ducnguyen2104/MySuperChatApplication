package com.ducnguyen.mysuperchatapplication.LocalDB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.ducnguyen.mysuperchatapplication.Model.LocalConversation;
import com.ducnguyen.mysuperchatapplication.Model.LocalMessage;
import com.ducnguyen.mysuperchatapplication.Model.LocalUser;
import com.ducnguyen.mysuperchatapplication.Model.LoggedInUser;

@Database(entities = {LocalUser.class, LocalConversation.class, LocalMessage.class, LoggedInUser.class}, version = 2)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract LocalDBDao localDBDao();
}
