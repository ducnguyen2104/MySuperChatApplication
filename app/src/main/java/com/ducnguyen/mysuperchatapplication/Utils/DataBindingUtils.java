package com.ducnguyen.mysuperchatapplication.Utils;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.ducnguyen.mysuperchatapplication.MainActivity;
import com.ducnguyen.mysuperchatapplication.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
        Log.i("img msg", "" + source);
        final Context context = imageView.getContext();
        Bitmap bitmap = new ImageSaver(context).
                setFileName(source + ".jpg").
                setDirectoryName("messages").
                load();
        if(bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference msgStorageRef = storage.getReference().child("messages").child(source + ".jpg");
            GlideApp.with((Activity) context).load(msgStorageRef)
                    .into(imageView);
        }
    }

    @BindingAdapter("thumbnailsource")
    public static void setThumnailsource(final ImageView imageView, String source) {
        Log.i("thumbnail", "" + source);
        final Context context = imageView.getContext();
        Bitmap bitmap = new ImageSaver(context).
                setFileName(source + "thumbnail.jpg").
                setDirectoryName("messages").
                load();
        if(bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference msgStorageRef = storage.getReference().child("messages").child(source + "thumbnail.jpg");
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
            GlideApp.with((Activity) context).load(avtStorageReference)
                    .centerCrop()
                    .apply(RequestOptions.circleCropTransform())
                    //.diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.skipMemoryCache(true)
                    .into(imageView);
            /*MainActivity.database.child("users").child(name).child("avttimestamp").addListenerForSingleValueEvent(new ValueEventListener() {
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
            });*/
        }
    }

    @BindingAdapter("iconsrc")
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter("videosource")
    public static void setVideosource(PlayerView playerView, String videoName) {
        //init player
        SimpleExoPlayer player;
        boolean playWhenReady = false;
        int currentWindow = 0;
        long playbackPosition = 0;
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(playerView.getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());
        playerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        //player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.seekTo(currentWindow, playbackPosition);
        StorageReference videoRef = FirebaseStorage.getInstance().getReference().child("messages").child(videoName + ".mp4");
        videoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                MediaSource mediaSource = new ExtractorMediaSource.Factory(
                        new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                        createMediaSource(uri);;
                player.prepare(mediaSource, true, false);
            }
        });
    }

    @BindingAdapter("layout_height")
    public static void setLayoutHeight(View view, float height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) height;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("layout_width")
    public static void setLayoutWidth(View view, float width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = (int) width;
        view.setLayoutParams(layoutParams);
    }
}
