package com.ducnguyenvan.mysuperchatapplication.Register;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;

import com.ducnguyenvan.mysuperchatapplication.R;
import com.ducnguyenvan.mysuperchatapplication.databinding.ActivityRegisterBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    public static int scrWidth;
    public static int scrHeight;
    EditText usernameEdt;
    EditText passwordEdt;
    EditText confpassEdt;
    EditText fullnameEdt;
    EditText phoneEdt;
    Button registBtn;
    DatabaseReference database;
    RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding activityRegisterBinding = DataBindingUtil.setContentView(this,R.layout.activity_register);
        database = FirebaseDatabase.getInstance().getReference();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        RegisterActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        scrHeight = displayMetrics.heightPixels;
        scrWidth = displayMetrics.widthPixels;
        usernameEdt = (EditText)findViewById(R.id.regis_username);
        passwordEdt = (EditText)findViewById(R.id.regis_password);
        confpassEdt = (EditText)findViewById(R.id.regis_confirm_password);
        fullnameEdt = (EditText)findViewById(R.id.regis_fullname);
        phoneEdt = (EditText)findViewById(R.id.regis_phonenum);
        registBtn = (Button)findViewById(R.id.regis_btn);
        usernameEdt.setWidth(scrWidth/2);
        passwordEdt.setWidth(scrWidth/2);
        confpassEdt.setWidth(scrWidth/2);
        fullnameEdt.setWidth(scrWidth/2);
        phoneEdt.setWidth(scrWidth/2);
        registerViewModel = new RegisterViewModel(this);
        activityRegisterBinding.setRegisterViewModel(registerViewModel);
    }
}
