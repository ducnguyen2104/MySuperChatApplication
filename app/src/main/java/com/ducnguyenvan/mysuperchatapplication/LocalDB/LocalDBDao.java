package com.ducnguyenvan.mysuperchatapplication.LocalDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ducnguyenvan.mysuperchatapplication.Model.LocalConversation;
import com.ducnguyenvan.mysuperchatapplication.Model.LocalMessage;
import com.ducnguyenvan.mysuperchatapplication.Model.LocalUser;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface LocalDBDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUsers(LocalUser... localUsers);

    @Update
    public void updateUsers(LocalUser... localUsers);

    @Query("SELECT * FROM users WHERE username = :search")
    Flowable<LocalUser> getUserByName(String search);

    @Query("SELECT * FROM users WHERE phonenumber = :search")
    Flowable<List<LocalUser>> findUserByPhoneNumber(String search);

    @Query("SELECT * FROM users")
    Single<LocalUser> getAllUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertConversations(LocalConversation... localConversations);

    @Update
    public void updateConversations(LocalConversation... localConversations);

    @Query("UPDATE conversations SET message = (:newMsg), name = (:newName), realTimestamp = (:newRealTimestamp), timestamp = (:newTimestamp) WHERE cId = (:cIdIn)")
    public void updateLastMsg(String cIdIn, String newMsg, String newName, long newRealTimestamp, String newTimestamp);

    @Query("SELECT * FROM conversations WHERE cId = :search")
    Single<LocalConversation> findConversationById(String search);

    @Query("SELECT * FROM conversations WHERE cId IN (:ids)")
    Single<List<LocalConversation>> findConversationsByListIdsWhenLoggingIn(List<String> ids);

    /*@Query("SELECT * FROM conversations WHERE members = :search")
    public Flowable<Conversation> findConversationByMembers(ArrayList<String> search);*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMessages(LocalMessage... localMessages);

    @Query("SELECT * FROM messages WHERE convId = :search")
    Single<List<LocalMessage>> findMessagesByConvId(String search);

    @Query("SELECT * FROM messages WHERE convId = :search ORDER BY realtimestamp DESC LIMIT 1")
    public Flowable<LocalMessage> getLastMessageOfConversation(String search);
}
