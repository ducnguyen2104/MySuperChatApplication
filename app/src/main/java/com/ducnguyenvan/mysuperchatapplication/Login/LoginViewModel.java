package com.ducnguyenvan.mysuperchatapplication.Login;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ducnguyenvan.mysuperchatapplication.LocalDB.LocalDatabase;
import com.ducnguyenvan.mysuperchatapplication.MainActivity;
import com.ducnguyenvan.mysuperchatapplication.Model.LoggedInUser;
import com.ducnguyenvan.mysuperchatapplication.Model.User;
import com.ducnguyenvan.mysuperchatapplication.Register.RegisterActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    private final Context context;
    public String username;
    public String password;

    DatabaseReference database;


    public LoginViewModel(Context context) {
        this.context = context;
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        database = FirebaseDatabase.getInstance().getReference();
    }


    public void onLoginBtnClicked() {
        Log.i("login", "clicked");
        verifyUserAndLogin();
    }

    public void onRegisterBtnClicked() {
        Log.i("regist", "clicked");
        Intent intent = new Intent(context,RegisterActivity.class);
        context.startActivity(intent);
    }

    private void verifyUserAndLogin() {
        if(username.contains("" + '.') || username.contains("" + '#') || username.contains("" + '$')
                || username.contains("" + '[') || username.contains("" + ']') || username.contains("" + '*')) {
            Toast.makeText(context,"Tên đăng nhập không được chứa các ký tự đặc biệt", Toast.LENGTH_LONG).show();
            return;
        }
        DatabaseReference databaseReference = database.child("users").child(username);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            User user = new User();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> map = (HashMap<String,Object>) dataSnapshot.getValue();
                //user = dataSnapshot.getValue(User.class);
                //ArrayList<User> users = new ArrayList<>(map.values());
                Log.i("user snapshot",   ", map:" + map);
                String pass = map.get("password").toString();
                String uid = map.get("username").toString();
                if(pass != null && pass.equals(password)){
                    LocalDatabase localDatabase = Room.databaseBuilder(context,LocalDatabase.class, "localDB")
                            .build();
                    Single.just(1).observeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Integer integer) {
                            LoggedInUser loggedInUser = new LoggedInUser();
                            loggedInUser.setUsername(uid);
                            localDatabase.localDBDao().insertLoggedInUser(loggedInUser);
                            Intent intent = new Intent(context,MainActivity.class);
                            //intent.putExtra("username",uid);
                            context.startActivity(intent);
                            ((Activity)context).finish();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });

                }
                else {
                    Toast.makeText(context,"Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
