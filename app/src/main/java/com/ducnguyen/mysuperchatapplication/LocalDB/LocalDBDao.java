package com.ducnguyen.mysuperchatapplication.LocalDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ducnguyen.mysuperchatapplication.Model.LocalConversation;
import com.ducnguyen.mysuperchatapplication.Model.LocalMessage;
import com.ducnguyen.mysuperchatapplication.Model.LocalUser;
import com.ducnguyen.mysuperchatapplication.Model.LoggedInUser;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface LocalDBDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(LocalUser... localUsers);

    @Update
    void updateUsers(LocalUser... localUsers);

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

    @Query("UPDATE conversations SET message = (:newMsg), name = (:newName), realTimestamp = (:newRealTimestamp), timestamp = (:newTimestamp), isUploaded = (:isUploaded) WHERE cId = (:cIdIn)")
    public void updateLastMsg(String cIdIn, String newMsg, String newName, long newRealTimestamp, String newTimestamp, boolean isUploaded);

    @Query("SELECT * FROM conversations WHERE cId = :search")
    Single<LocalConversation> findConversationById(String search);

    @Query("SELECT * FROM conversations WHERE cId IN (:ids) ORDER BY realtimestamp DESC")
    Single<List<LocalConversation>> findConversationsByListIdsWhenLoggingIn(List<String> ids);

    @Query("UPDATE conversations SET isUploaded = (:isUploaded) WHERE convId = (:convId)")
    void updateLocalConversationSendStatus(String convId, boolean isUploaded);

    @Query("SELECT * FROM conversations WHERE isUploaded = (:search)")
    Single<List<LocalConversation>> findUploadFailedConversation(boolean search);

    @Query("UPDATE conversations SET isUploaded = (:isUploaded) WHERE convId = (:convId)")
    void updateLocalConvStatus(String convId, boolean isUploaded);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMessages(LocalMessage... localMessages);

    @Query("SELECT * FROM messages WHERE convId = :search ORDER BY realtimestamp")
    Single<List<LocalMessage>> findMessagesByConvId(String search);

    @Query("SELECT * FROM messages WHERE isUploaded = (:search)")
    Single<List<LocalMessage>> findUploadFailedMessage(boolean search);

    @Query("UPDATE messages SET isUploaded = (:isUploaded) WHERE convId = (:convId) AND name = (:username) AND realtimestamp = (:timestamp)")
    void updateLocalMsgSendStatus(String convId, String username, long timestamp, boolean isUploaded);

    @Query("SELECT * FROM messages WHERE convId = :search ORDER BY realtimestamp DESC LIMIT 1")
    public Flowable<LocalMessage> getLastMessageOfConversation(String search);

    @Query("SELECT * FROM LoggedInUser")
    Single<LoggedInUser> getLoggedInUser();

    @Delete
    void deleteLoggedInUser(LoggedInUser... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLoggedInUser(LoggedInUser... users);
}
