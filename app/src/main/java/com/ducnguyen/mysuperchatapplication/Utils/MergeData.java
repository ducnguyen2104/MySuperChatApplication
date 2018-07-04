package com.ducnguyen.mysuperchatapplication.Utils;

import android.support.annotation.NonNull;

import com.ducnguyen.mysuperchatapplication.MainActivity;
import com.ducnguyen.mysuperchatapplication.Model.LocalUser;
import com.ducnguyen.mysuperchatapplication.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;


public class MergeData {

    public Flowable<Timed<User>> getMergedUserData(String username) {
        return Flowable.mergeDelayError(
                getLocalUserData(username).subscribeOn(Schedulers.io()),
                getFirebaseUserData(username)
                        .timestamp()
                        .doOnNext(userTimed -> MainActivity.localDatabase.localDBDao().insertUsers(userTimed.value().toLocalUser()))
                        .subscribeOn(Schedulers.io())
        );
    }

    public Flowable<Timed<User>> getUserData(String username, User currentUser) {
        return getMergedUserData(username)
                .filter(user -> user != null && user.value() != null && (!currentUser.getUsername().equals(username) || user.value().getAvttimestamp() >= currentUser.getAvttimestamp()));
    }

    public Flowable<User> getFirebaseUserData(String username) {
        User firebaseUser = new User();
        Flowable<User> flowable = Flowable.just(firebaseUser);
        MainActivity.database.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                User firebaseUser = new User();
                firebaseUser.mapToObject(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return flowable;
    }

    public Flowable<Timed<User>> getLocalUserData(String username) {
        return MainActivity.localDatabase.localDBDao().getUserByName(username)
                .map(LocalUser::toUser).timestamp();
    }

}
