package com.ducnguyen.mysuperchatapplication.Contacts.AddContact;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ducnguyen.mysuperchatapplication.Contacts.ContactsFragment;
import com.ducnguyen.mysuperchatapplication.MainActivity;
import com.ducnguyen.mysuperchatapplication.Model.User;
import com.ducnguyen.mysuperchatapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddContactActivity extends AppCompatActivity {

    EditText finđEdt;
    Button findBtn;
    Button backBtn;
    TextView resultTxt;
    ArrayList<String> contacts = MainActivity.currentUser.getContacts();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        finđEdt = (EditText)findViewById(R.id.add_contact_edt);
        findBtn = (Button)findViewById(R.id.add_contact_btn);
        resultTxt = (TextView)findViewById(R.id.add_contact_find_result);
        resultTxt.setVisibility(View.GONE);
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findUserByPhoneNumber();
            }
        });
        resultTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resultTxt.getVisibility() == View.VISIBLE && !resultTxt.getText().equals("Không tìm thấy, vui lòng kiểm tra lại số điện thoại")) {
                    Log.i("result", resultTxt.getText().toString());
                    addToContact();
                }
            }
        });
        backBtn = findViewById(R.id.add_contact_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void findUserByPhoneNumber() {
        if(checkEditTextNotNull()) {
            DatabaseReference databaseReference = MainActivity.database.child("users");
            Query userQuery = databaseReference.orderByChild("phonenumber").equalTo(finđEdt.getText().toString());
            userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Map<String,Object> map = (HashMap<String,Object>)singleSnapshot.getValue();
                        User user = new User();
                        user.mapToObject(map);
                        if(user.getUsername() != null) {
                            resultTxt.setText(user.getUsername());
                        }
                        else {
                            resultTxt.setText("Không tìm thấy, vui lòng kiểm tra lại số điện thoại");
                        }
                    }
                    resultTxt.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(),"Vui lòng nhập số điện thoại cần tìm", Toast.LENGTH_LONG).show();
        }
    }

    public boolean checkEditTextNotNull() {
        return finđEdt.getText() != null;
    }

    public void addToContact() {
        if(contacts == null) {
            contacts = new ArrayList<>();
        }
        contacts.add(resultTxt.getText().toString());
        final int contactsSize = contacts.size();
        MainActivity.currentUser.setContacts(contacts);
        String key = MainActivity.database.child("users").child(MainActivity.currentUser.getUsername()).child("contacts").push().getKey();
        MainActivity.database.child("users").child(MainActivity.currentUser.getUsername()).child("contacts").child(key).setValue(resultTxt.getText().toString())
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Thêm vào danh bạ thành công", Toast.LENGTH_LONG).show();
                finish();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Thêm vào danh bạ thất bại", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(resultTxt.getVisibility() == View.VISIBLE && !resultTxt.getText().toString().equals("Không tìm thấy, vui lòng kiểm tra lại số điện thoại")) {
            ContactsFragment.updateRvAfterAdd(resultTxt.getText().toString());
        }
    }
}
