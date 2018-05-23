package com.ducnguyenvan.mysuperchatapplication.Login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;

import com.ducnguyenvan.mysuperchatapplication.R;
import com.ducnguyenvan.mysuperchatapplication.databinding.ActivityLoginBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    public static int scrWidth;
    public static int scrHeight;
    public EditText username;
    public EditText password;
    public Button login;
    public Button register;
    public DatabaseReference database;
    public LoginViewModel loginViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding activityLoginBinding =  DataBindingUtil.setContentView(this,R.layout.activity_login);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        database = FirebaseDatabase.getInstance().getReference();
        LoginActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        scrHeight = displayMetrics.heightPixels;
        scrWidth = displayMetrics.widthPixels;
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.btn_login);
        register = (Button)findViewById(R.id.btn_register);
        username.setWidth(scrWidth/2);
        password.setWidth(scrWidth/2);
        login.setWidth(scrWidth/3);
        register.setWidth(scrWidth/3);
        loginViewModel = new LoginViewModel(this);
        activityLoginBinding.setLoginViewModel(loginViewModel);
    }

}
