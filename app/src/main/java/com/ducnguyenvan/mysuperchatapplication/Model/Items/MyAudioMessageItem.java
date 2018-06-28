package com.ducnguyenvan.mysuperchatapplication.Model.Items;

import android.databinding.Bindable;

import android.media.MediaPlayer;
import android.util.Log;

import com.ducnguyenvan.mysuperchatapplication.Conversation.ConversationActivity;
import com.ducnguyenvan.mysuperchatapplication.R;

import java.io.IOException;

import com.ducnguyenvan.mysuperchatapplication.BR;

public class MyAudioMessageItem extends BaseMessageItem {
    @Bindable
    public String audioName;

    public String duration;

    public MediaPlayer mPlayer;

    public boolean isPlaying;

    @Bindable
    public int iconSource;

    public MyAudioMessageItem(String username, String audioName, String duration, String timestamp, long realtimestamp) {
        this.username = username;
        this.audioName = audioName;
        this.timestamp = timestamp;
        this.realtimestamp = realtimestamp;
        this.duration = duration;
        this.isPlaying = false;
        this.setIconSource(R.drawable.myplay);
    }

    public void startPlaying() {
        Log.i("my audio msg item", "key: " + audioName);
        setIconSource(R.drawable.mystop);
        isPlaying = true;
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(ConversationActivity.externalCachedir + "/" + audioName + ".mp3");
            mPlayer.prepare();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    isPlaying = false;
                    setIconSource(R.drawable.myplay);
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
        setIconSource(R.drawable.myplay);
    }

    public int getIconSource() {
        return iconSource;
    }

    public void setIconSource(int iconSource) {
        this.iconSource = iconSource;
        registry.notifyChange(this, BR.iconSource);
    }
}
