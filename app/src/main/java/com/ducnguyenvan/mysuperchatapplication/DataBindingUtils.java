package com.ducnguyenvan.mysuperchatapplication;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DataBindingUtils  {
    public DataBindingUtils() {
    }

    @BindingAdapter("path")
    public static void setPath(ImageView imageView, String path) {
        final Context context = imageView.getContext();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference avtStorageReference = storage.getReference().child("avartar").child(path + ".jpg");
        GlideApp.with((Activity) context).load(avtStorageReference)
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }

    @BindingAdapter("conversationimg")
    public static void setConversationimg(ImageView imageView, String name) {
        Context context = imageView.getContext();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        boolean isGroupchat = name.toLowerCase().contains(", ".toLowerCase());
        StorageReference avtStorageReference = storage.getReference().child("avartar").child((isGroupchat ? "group" : name) + ".jpg");
        GlideApp.with((Activity) context).load(avtStorageReference)
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }
    @BindingAdapter("android:src")
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }
}
