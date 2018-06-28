package com.ducnguyenvan.mysuperchatapplication.Model.Items;

import android.databinding.Bindable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;

import com.ducnguyenvan.mysuperchatapplication.Conversation.ConversationActivity;

import java.io.IOException;

import com.ducnguyenvan.mysuperchatapplication.BR;
import com.ducnguyenvan.mysuperchatapplication.R;

public class YourAudioMessageItem extends BaseMessageItem {

    public int img;
    @Bindable
    public String audioName;
    public String duration;
    public int visibility;
    public MediaPlayer mPlayer;
    public boolean isPlaying;
    @Bindable
    public int iconSource;


    public YourAudioMessageItem(int img, String username, String audioName, String duration, String timestamp, long realTimestamp, boolean isFirst) {
        this.img = img;
        this.username = username;
        this.audioName = audioName;
        this.duration = duration;
        this.timestamp = timestamp;
        this.isFirst = isFirst;
        this.realtimestamp = realTimestamp;
        this.visibility = isFirst ? View.VISIBLE : View.INVISIBLE;
        mPlayer = null;
        isPlaying = false;
        setIconSource(R.drawable.yourplay);
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getIconSource() {
        return iconSource;
    }

    public void setIconSource(int iconSource) {
        this.iconSource = iconSource;
        registry.notifyChange(this, BR.iconSource);
    }

    public void startPlaying() {
        Log.i("your audio msg item", "key: " + audioName);
        setIconSource(R.drawable.yourstop);
        isPlaying = true;
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(ConversationActivity.externalCachedir + "/" + audioName + ".mp3");
            mPlayer.prepare();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    isPlaying = false;
                    setIconSource(R.drawable.yourplay);
                }
            });
            mPlayer.start();
        } catch (IOException e) {
            //Toast.makeText(getApplicationContext(),"player.prepare() failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
        isPlaying = false;
        setIconSource(R.drawable.yourplay);
    }
}
