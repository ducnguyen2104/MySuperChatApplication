package com.ducnguyenvan.mysuperchatapplication.Login;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ducnguyenvan.mysuperchatapplication.MainActivity;
import com.ducnguyenvan.mysuperchatapplication.Model.User;
import com.ducnguyenvan.mysuperchatapplication.Register.RegisterActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginViewModel extends ViewModel {

    private final Context context;
    public String username;
    public String password;

    DatabaseReference database;


    public LoginViewModel(Context context) {
        this.context = context;
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
        DatabaseReference databaseReference = database.child("users").child(username);
        /*Query userQuery = databaseReference.orderByChild("username").equalTo(username);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            User user = new User();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    user = singleSnapshot.getValue(User.class);
                }
                Log.i("user", username +  ", " + password);
                if(user.getPassword() != null && user.getPassword().equals(password)){{
                    Intent intent = new Intent(context,MainActivity.class);
                    intent.putExtra("username",user.getUsername());
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }}
                else {
                    Toast.makeText(context,"Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
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
                if(pass != null && pass.equals(password)){{
                    Intent intent = new Intent(context,MainActivity.class);
                    intent.putExtra("username",uid);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }}
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
