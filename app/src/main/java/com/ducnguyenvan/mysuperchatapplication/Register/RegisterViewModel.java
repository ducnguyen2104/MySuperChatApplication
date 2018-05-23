package com.ducnguyenvan.mysuperchatapplication.Register;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.ducnguyenvan.mysuperchatapplication.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterViewModel extends ViewModel {
    private final Context context;
    public String username;
    public String password;
    public String confirmPassword;
    public String fullname;
    public String phoneNumber;
    DatabaseReference database;

    public RegisterViewModel(Context context) {
        this.context = context;
        database = FirebaseDatabase.getInstance().getReference();
    }

    public void onRegisterButtonClicked() {
        if(isAllFieldFilled()) {
            if(checkEqualPassword()) {
                checkExistingAccountAndCreateUser();
            }
            else {
                Toast.makeText(context,"Xác nhận mật khẩu không đúng", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(context,"Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isAllFieldFilled() {
        return username.length() != 0 && password.length() != 0
                && confirmPassword.length() != 0 && fullname.length() != 0
                && phoneNumber.length() != 0;
    }

    public boolean checkEqualPassword() {
        return password.equals(confirmPassword);
    }

    private void writeNewUser(String name, String pass, String fullname, String phonenumber) {
        User user = new User(name,pass,fullname,phonenumber);
        database.child("users").child(name).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"Đăng ký thành công",Toast.LENGTH_LONG).show();
                        ((Activity)context).finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"Đăng ký thất bại, vui lòng thử lại",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void checkExistingAccountAndCreateUser() {
        DatabaseReference databaseReference = database.child("users");
        Query userQuery = databaseReference.orderByChild("username").equalTo(username);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            User user = new User();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    user = singleSnapshot.getValue(User.class);
                }
                if(user.getUsername() != null) {
                    Toast.makeText(context,"Tên đăng nhập đã tồn tại", Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    writeNewUser(username,password,fullname,phoneNumber);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
