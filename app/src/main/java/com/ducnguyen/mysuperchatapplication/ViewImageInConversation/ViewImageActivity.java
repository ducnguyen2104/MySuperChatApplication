package com.ducnguyen.mysuperchatapplication.ViewImageInConversation;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ducnguyen.mysuperchatapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ViewImageActivity extends AppCompatActivity {

    protected CustomImageView customImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        customImageView = findViewById(R.id.customImg);
        String path = getIntent().getStringExtra("path");
        customImageView.loadImageWithPath(path);
        /*StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef. child("messages").child(path + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(customImageView == null) {
                    Toast.makeText(getApplicationContext(), "null img view", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(uri == null) {
                    Toast.makeText(getApplicationContext(), "null uri", Toast.LENGTH_SHORT).show();
                    return;
                }
                customImageView.loadImageWithPath(uri);
                // Got the download URL for 'users/me/profile.png'
            }
        });*/
    }
}
