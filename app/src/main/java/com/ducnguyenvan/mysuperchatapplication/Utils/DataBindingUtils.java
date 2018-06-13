package com.ducnguyenvan.mysuperchatapplication.Utils;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.ducnguyenvan.mysuperchatapplication.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DataBindingUtils  {
    public DataBindingUtils() {
    }

    @BindingAdapter("path")
    public static void setPath(final ImageView imageView, String path) {
        final Context context = imageView.getContext();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference avtStorageReference = storage.getReference().child("avartar").child(path + ".jpg");
        MainActivity.database.child("users").child(path).child("avttimestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String avttimestamp = dataSnapshot.getValue().toString();
                GlideApp.with((Activity) context).load(avtStorageReference)
                        .signature(new ObjectKey(avttimestamp))
                        .centerCrop()
                        .apply(RequestOptions.circleCropTransform())
                        //.diskCacheStrategy(DiskCacheStrategy.NONE)
                        //.skipMemoryCache(true)
                        .into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @BindingAdapter("msgsource")
    public static void setMsgsource(final ImageView imageView, String source) {
        final Context context = imageView.getContext();
        Bitmap bitmap = new ImageSaver(context).
                setFileName(source + ".jpg").
                setDirectoryName("messages").
                load();
        if(bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            Log.i("load firebase", "" + source);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference msgStorageRef = storage.getReference().child("messages").child(source + ".jpg");
            GlideApp.with((Activity) context).load(msgStorageRef)
                    .into(imageView);
        }
    }

    @BindingAdapter("conversationimg")
    public static void setConversationimg(final ImageView imageView, String name) {
        final Context context = imageView.getContext();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        boolean isGroupchat = name.toLowerCase().contains(", ".toLowerCase());
        final StorageReference avtStorageReference = storage.getReference().child("avartar").child((isGroupchat ? "group" : name) + ".jpg");
        if(isGroupchat) {
            GlideApp.with((Activity) context).load(avtStorageReference)
                    .centerCrop()
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
        }
        else {
            MainActivity.database.child("users").child(name).child("avttimestamp").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String avttimestamp = dataSnapshot.getValue().toString();
                    GlideApp.with((Activity) context).load(avtStorageReference)
                            .signature(new ObjectKey(avttimestamp))
                            .centerCrop()
                            .apply(RequestOptions.circleCropTransform())
                            //.diskCacheStrategy(DiskCacheStrategy.NONE)
                            //.skipMemoryCache(true)
                            .into(imageView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    @BindingAdapter("android:src")
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }
}
