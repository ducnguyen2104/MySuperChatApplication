package com.ducnguyen.mysuperchatapplication.Model.Items;

import android.databinding.Bindable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ducnguyen.mysuperchatapplication.BR;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyVideoMessageItem extends BaseMessageItem {
    @Bindable
    public String videoName; //video

    @Bindable
    public int vidStatusTextViewVisibility; //visibility of the text view "Đang gửi..."

    @Bindable
    public String duration;

    @Bindable
    public int thumbnailVisibility;

    @Bindable
    public int playerVisibility;

    @Bindable
    public SimpleExoPlayer player;

    @Bindable
    public float height;

    @Bindable
    public float width;

    public MyVideoMessageItem(String username, String videoName, String duration, String timestamp, long realtimestamp, boolean isSending, float height, float width) {
        this.username = username;
        this.videoName = videoName;
        this.duration = duration;
        this.timestamp = timestamp;
        this.realtimestamp = realtimestamp;
        this.vidStatusTextViewVisibility = (isSending ? (View.VISIBLE) : View.GONE);
        this.thumbnailVisibility = View.VISIBLE;
        this.playerVisibility = View.GONE;
        this.height = height;
        this.width = width;
        Log.i("vidMsgItem", "width: " + width);
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
        registry.notifyChange(this, BR.videoName);
    }

    public int getVidStatusTextViewVisibility() {
        return vidStatusTextViewVisibility;
    }

    public void setVidStatusTextViewVisibility(int vidStatusTextViewVisibility) {
        this.vidStatusTextViewVisibility = vidStatusTextViewVisibility;
        registry.notifyChange(this, BR.vidStatusTextViewVisibility);
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
        registry.notifyChange(this, BR.duration);
    }

    public int getThumbnailVisibility() {
        return thumbnailVisibility;
    }

    public void setThumbnailVisibility(int thumbnailVisibility) {
        this.thumbnailVisibility = thumbnailVisibility;
        registry.notifyChange(this, BR.thumbnailVisibility);
    }

    public int getPlayerVisibility() {
        return playerVisibility;
    }

    public void setPlayerVisibility(int playerVisibility) {
        this.playerVisibility = playerVisibility;
        registry.notifyChange(this, BR.playerVisibility);
    }

    public void playVideo() {
        setThumbnailVisibility(View.GONE);
        setPlayerVisibility(View.VISIBLE);
    }

    public SimpleExoPlayer getPlayer() {
        return player;
    }

    public void setPlayer(SimpleExoPlayer player) {
        this.player = player;
        registry.notifyChange(this, BR.player);
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
        registry.notifyChange(this, BR.height);
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
        registry.notifyChange(this, BR.width);
    }

    /*private void initializePlayer() {
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(this),
                    new DefaultTrackSelector(), new DefaultLoadControl());

            playerView.setPlayer(player);

            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
            StorageReference videoRef = FirebaseStorage.getInstance().getReference().child("messages").child(videoName + ".mp4");
            videoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    MediaSource mediaSource = buildMediaSource(uri);
                    player.prepare(mediaSource, true, false);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Tải video thất bại", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
            //Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/mysuperchatapplication.appspot.com/o/messages%2Fahihi*ahuhu%2F-LFuxWlHZjyUSNyYQ6rF.mp3?alt=media&token=d598637e-5788-4fe6-8836-3c8e39e00e07");
            //MediaSource mediaSource = buildMediaSource(uri);
            //player.prepare(mediaSource, true, false);
        }*/
    @Override
    public String toString() {
        return "My Video: " + this.videoName + ", " + this.timestamp;
    }
}
