package com.ducnguyen.mysuperchatapplication.Conversation;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ducnguyen.mysuperchatapplication.History.HistoryFragment;
import com.ducnguyen.mysuperchatapplication.History.HistoryRvAdapter;
import com.ducnguyen.mysuperchatapplication.MainActivity;
import com.ducnguyen.mysuperchatapplication.Map.MapActivity;
import com.ducnguyen.mysuperchatapplication.Model.Conversation;
import com.ducnguyen.mysuperchatapplication.Model.Items.BaseItemViewHolder;
import com.ducnguyen.mysuperchatapplication.Model.Items.BaseMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.ConversationItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.MyAudioMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.MyImageMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.MyLocationMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.MyVideoMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.YourAudioMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.YourImageMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.YourLocationMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.Items.YourVideoMessageItem;
import com.ducnguyen.mysuperchatapplication.Model.LocalConversation;
import com.ducnguyen.mysuperchatapplication.Model.LocalMessage;
import com.ducnguyen.mysuperchatapplication.Model.Message;
import com.ducnguyen.mysuperchatapplication.R;
import com.ducnguyen.mysuperchatapplication.Utils.ImagePicker;
import com.ducnguyen.mysuperchatapplication.Utils.ImageSaver;
import com.ducnguyen.mysuperchatapplication.Utils.VideoPicker;
import com.ducnguyen.mysuperchatapplication.ViewImageInConversation.ViewImageActivity;
import com.ducnguyen.mysuperchatapplication.databinding.ActivityConversationBinding;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ConversationActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS = 200;
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 201;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToReadExternalAccepted = false;
    private boolean permissionToWriteExternalAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_VIDEO_REQUEST = 2;
    private static final int PICK_LOCATION_REQUEST = 3;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                permissionToRecordAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                permissionToReadExternalAccepted = (grantResults[1] == PackageManager.PERMISSION_GRANTED);
                permissionToWriteExternalAccepted = (grantResults[2] == PackageManager.PERMISSION_GRANTED);
                break;
        }
        //permission not granted
        if (!permissionToRecordAccepted || !permissionToReadExternalAccepted || !permissionToWriteExternalAccepted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
        }
    }

    static RecyclerView recyclerView;
    static MessageRvAdapter adapter;
    public static String externalCachedir;
    static ArrayList<BaseMessageItem> messageItems = new ArrayList<>();
    ActivityConversationBinding activityConversationBinding;
    static ConversationViewModel conversationViewModel;
    EditText editText;
    ImageButton btn;
    ImageButton sendImgButton;
    ImageButton sendVideoButton;
    ImageButton emoji;
    ImageButton audio;
    ImageButton recorđAudio;
    ImageButton sendLocation;
    TextView recordTxt;
    LinearLayout recordLayout;
    static String currentCId;
    static String membersString;
    static boolean isConversationCreated = false;
    static DatabaseReference messageRef;
    GridView gridView;
    static ChildEventListener childEventListener;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    boolean isRecording;
    String key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
        }
        externalCachedir = getExternalCacheDir().getAbsolutePath();
        currentCId = getIntent().getStringExtra("cId"); //username1|username2
        membersString = getIntent().getStringExtra("membersString"); //!= null is conversation has 2 or more members
        if (membersString == null) {
            membersString = "";
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ConversationActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int scrWidth = displayMetrics.widthPixels;
        activityConversationBinding = DataBindingUtil.setContentView(this, R.layout.activity_conversation);
        editText = findViewById(R.id.conversation_edt);
        btn = findViewById(R.id.conversation_btn_send);
        sendImgButton = findViewById(R.id.send_img_btn);
        sendVideoButton = findViewById(R.id.send_video_btn);
        audio = findViewById(R.id.record_audio);
        recorđAudio = findViewById(R.id.start_stop_recording);
        recordLayout = findViewById(R.id.record_layout);
        recordTxt = findViewById(R.id.record_txt);
        sendLocation = findViewById(R.id.send_location_btn);
        isRecording = false;
        recorđAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRecording) {
                    key = MainActivity.database.child("messages").child(ConversationActivity.currentCId).push().getKey();
                    startRecording();
                    isRecording = true;
                    recordTxt.setText("Đang ghi âm... Nhấn để gửi");
                } else {
                    stopRecording();
                    isRecording = false;
                    recordTxt.setText("Nhấn để ghi âm");
                }
            }
        });
        sendImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(getApplicationContext());
                startActivityForResult(chooseImageIntent, PICK_IMAGE_REQUEST);
            }
        });
        sendVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);*/
                Intent intent = VideoPicker.getPickVideoIntent(getApplicationContext());
                startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
            }
        });
        sendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(ConversationActivity.this), PICK_LOCATION_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        conversationViewModel = new ConversationViewModel(this);
        activityConversationBinding.setConversationViewModel(conversationViewModel);
        recyclerView = (RecyclerView) findViewById(R.id.conversation_rv);
        messageRef = MainActivity.database.child("messages").child(currentCId);
        DatabaseReference convRef = MainActivity.database.child("conversations").child(currentCId);
        //check local db if conversation has been already created
        MainActivity.localDatabase.localDBDao().findConversationById(currentCId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<LocalConversation>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(LocalConversation localConversation) {
                isConversationCreated = true;
                initRecyclerView();
            }

            @Override
            public void onError(Throwable e) {
                initRecyclerView();
            }
        });
        //check firebase if conversation has been already created
        MainActivity.database.child("conversations").child(currentCId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isConversationCreated = dataSnapshot.getValue() != null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        emoji = findViewById(R.id.add_emoji_btn);
        gridView = findViewById(R.id.emoji_grid);
        gridView.setVisibility(View.GONE);
        ArrayList<String> emojiStringArr = new ArrayList<>();
        emojiStringArr.add(new String(Character.toChars(0x1F4A9)));
        emojiStringArr.add(new String(Character.toChars(0x1F44C)));
        emojiStringArr.add(new String(Character.toChars(0x1F601)));
        emojiStringArr.add(new String(Character.toChars(0x1F61B)));
        emojiStringArr.add(new String(Character.toChars(0x1F61C)));
        emojiStringArr.add(new String(Character.toChars(0x1F618)));
        emojiStringArr.add(new String(Character.toChars(0x1F604)));
        emojiStringArr.add(new String(Character.toChars(0x1F606)));
        emojiStringArr.add(new String(Character.toChars(0x1F923)));
        emojiStringArr.add(new String(Character.toChars(0x1F622)));
        emojiStringArr.add(new String(Character.toChars(0x1F62D)));
        emojiStringArr.add(new String(Character.toChars(0x1F610)));
        emojiStringArr.add(new String(Character.toChars(0x1F631)));
        emojiStringArr.add(new String(Character.toChars(0x1F635)));
        emojiStringArr.add(new String(Character.toChars(0x1F621)));
        emojiStringArr.add(new String(Character.toChars(0x1F615)));
        emojiStringArr.add(new String(Character.toChars(0x1F44D)));
        emojiStringArr.add(new String(Character.toChars(0x1F493)));
        emojiStringArr.add(new String(Character.toChars(0x1F427)));
        emojiStringArr.add(new String(Character.toChars(0x1F60A)));
        emojiStringArr.add(new String(Character.toChars(0x1F609)));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emojiStringArr);
        gridView.setAdapter(arrayAdapter);
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gridView.getVisibility() == View.GONE) {
                    if (recordLayout.getVisibility() == View.VISIBLE) {
                        recordLayout.setVisibility(View.GONE);
                    }
                    gridView.setVisibility(View.VISIBLE);
                    if (messageItems.size() > 1) {
                        recyclerView.scrollToPosition(messageItems.size() - 1);
                    }
                } else {
                    gridView.setVisibility(View.GONE);
                }
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = gridView.getItemAtPosition(position);
                String s = (String) o;
                editText.setText(editText.getText() + s);
            }
        });
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recordLayout.getVisibility() == View.GONE) {
                    if (gridView.getVisibility() == View.VISIBLE) {
                        gridView.setVisibility(View.GONE);
                    }
                    recordLayout.setVisibility(View.VISIBLE);
                    if (messageItems.size() > 1) {
                        recyclerView.scrollToPosition(messageItems.size() - 1);
                    }
                } else {
                    recordLayout.setVisibility(View.GONE);
                }
            }
        });
        //listener for messages ref
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message newMessage = new Message();
                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                newMessage.mapToObject(map);
                Log.i("newMsgNewConv", newMessage.toString());
                if (newMessage.getName().equals(MainActivity.currentUser.username)) {
                    return;
                }
                //newMessage = dataSnapshot.getValue(Message.class);
                for (int i = 0; i < messageItems.size(); i++) {
                    if ((newMessage.getName().equals(messageItems.get(i).getUsername()) && newMessage.getRealtimestamp() == messageItems.get(i).getRealtimestamp())) {
                        //if message is already present, return
                        return;
                    }
                }
                boolean isFirst = messageItems.size() == 0 || !newMessage.getName().equals(messageItems.get(messageItems.size() - 1).getUsername());
                messageItems.add(newMessage.toMessageItem(isFirst));
                //adapter.updateList(messageItems);
                adapter.notifyItemInserted(messageItems.size() - 1);
                recyclerView.scrollToPosition(messageItems.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public static void initRecyclerView() {
        Log.i("ConvAct", "init rv");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MessageRvAdapter(messageItems, recyclerView.getContext(), new OnMsgItemClickListener() {
            @Override
            public void onItemClick(BaseMessageItem item) {
                if (item instanceof MyImageMessageItem) {
                    Intent intent = new Intent(recyclerView.getContext(), ViewImageActivity.class);
                    intent.putExtra("path", ((MyImageMessageItem) item).getImgName());
                    recyclerView.getContext().startActivity(intent);
                } else if (item instanceof YourImageMessageItem) {
                    Intent intent = new Intent(recyclerView.getContext(), ViewImageActivity.class);
                    intent.putExtra("path", ((YourImageMessageItem) item).getImgName());
                    recyclerView.getContext().startActivity(intent);
                } else if (item instanceof MyAudioMessageItem) {
                    if (!((MyAudioMessageItem) item).isPlaying) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("messages").child(currentCId).child(((MyAudioMessageItem) item).audioName + ".mp3");
                        File localFile = new File(ConversationActivity.externalCachedir + "/" + ((MyAudioMessageItem) item).audioName + ".mp3");
                        if (localFile.exists()) {
                            ((MyAudioMessageItem) item).startPlaying();
                        } else {
                            try {
                                localFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            storageReference.getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                    ((MyAudioMessageItem) item).startPlaying();
                                }
                            });
                        }
                    } else {
                        ((MyAudioMessageItem) item).stopPlaying();
                    }
                } else if (item instanceof YourAudioMessageItem) {
                    if (!((YourAudioMessageItem) item).isPlaying) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("messages").child(currentCId).child(((YourAudioMessageItem) item).audioName + ".mp3");
                        File localFile = new File(ConversationActivity.externalCachedir + "/" + ((YourAudioMessageItem) item).audioName + ".mp3");
                        if (localFile.exists()) {
                            ((YourAudioMessageItem) item).startPlaying();
                        } else {
                            try {
                                localFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            storageReference.getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                    ((YourAudioMessageItem) item).startPlaying();
                                }
                            });
                        }
                    } else {
                        ((YourAudioMessageItem) item).stopPlaying();
                    }
                } else if (item instanceof MyVideoMessageItem) {
                    if (((MyVideoMessageItem) item).vidStatusTextViewVisibility == View.VISIBLE) {

                        return;
                    }
                    /*Intent intent = new Intent(adapter.context,PlayerActivity.class);
                    intent.putExtra("videoName", ((MyVideoMessageItem) item).getVideoName());
                    adapter.context.startActivity(intent);*/
                    ((MyVideoMessageItem) item).playVideo();

                } else if (item instanceof YourVideoMessageItem) {
                    /*Intent intent = new Intent(adapter.context,PlayerActivity.class);
                    intent.putExtra("videoName", ((YourVideoMessageItem) item).getVideoName());
                    adapter.context.startActivity(intent);*/
                    ((YourVideoMessageItem) item).playVideo();

                } else if(item instanceof MyLocationMessageItem) {
                    Intent intent = new Intent(adapter.context, MapActivity.class);
                    intent.putExtra("long", ((MyLocationMessageItem) item).getmLong());
                    intent.putExtra("lat", ((MyLocationMessageItem) item).getmLat());
                    adapter.context.startActivity(intent);
                } else if(item instanceof YourLocationMessageItem) {
                    Intent intent = new Intent(adapter.context, MapActivity.class);
                    intent.putExtra("long", ((YourLocationMessageItem) item).getmLong());
                    intent.putExtra("lat", ((YourLocationMessageItem) item).getmLat());
                    adapter.context.startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(adapter);

        //load local
        MainActivity.localDatabase.localDBDao().findMessagesByConvId(currentCId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<LocalMessage>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<LocalMessage> localMessages) {
                        for (LocalMessage single : localMessages) {
                            messageItems.add(single.toMessage().toMessageItem(messageItems.size() == 0 || !single.getName().equals(messageItems.get(messageItems.size() - 1).getUsername())));
                        }
                        recyclerView.scrollToPosition(messageItems.size() - 1);
                        //load firebase
                        DatabaseReference databaseReference = MainActivity.database.child("messages").child(currentCId);
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<BaseMessageItem> newItemList = new ArrayList<>();
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    Message message = new Message();
                                    Map<String, Object> map = (HashMap<String, Object>) singleSnapshot.getValue();
                                    message.mapToObject(map);
                                    if (message.getMessage() != null) {
                                        isConversationCreated = true;
                                    } else {
                                        return;
                                    }
                                    //newMessagesItems.add(message.toMessageItem(messageItems.size() == 0 || !message.getName().equals(messageItems.get(messageItems.size() - 1).getUsername())));
                                    Single.just(message).subscribeOn(Schedulers.io()).subscribe(new SingleObserver<Message>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(Message message) {
                                            //update in local db
                                            MainActivity.localDatabase.localDBDao().insertMessages(message.toLocalMessage(true));
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }
                                    });
                                    newItemList.add(message.toMessageItem(newItemList.size() == 0 || !message.getName().equals(newItemList.get(newItemList.size() - 1).getUsername())));
                                    Log.i("newMsgItemNewConv", newItemList.get(newItemList.size() - 1).toString());
                                    //adapter.notifyItemInserted(messageItems.size() - 1);
                                }
                                messageItems = newItemList;
                                adapter.updateList(newItemList);
                                recyclerView.scrollToPosition(messageItems.size() - 1);
                                updateRecyclerView();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });


    }

    @Override
    protected void onDestroy() {
        messageItems.clear();
        activityConversationBinding.unbind();
        messageRef.removeEventListener(childEventListener);
        recyclerView = null;
        conversationViewModel = null;
        isConversationCreated = false;
        currentCId = null;
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public static void updateRecyclerView() {
        messageRef.addChildEventListener(childEventListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_REQUEST: {
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                if (bitmap == null) {
                    break;
                }
                //img.setImageBitmap(bitmap);
                final String key = MainActivity.database.child("messages").child(currentCId).push().getKey();
                //save to storage
                new ImageSaver(getApplicationContext()).
                        setFileName(key + ".jpg").
                        setDirectoryName("messages").
                        save(bitmap);
                //create message object
                long realTimestamp = System.currentTimeMillis();
                Date date = new Date(realTimestamp);
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String timestamp = sdf.format(date);
                final Message newMessage = new Message("/-img:" + key, MainActivity.currentUser.getUsername(), timestamp, realTimestamp, currentCId);
                //add message item
                final MyImageMessageItem newMsgItem = new MyImageMessageItem(newMessage.getName(), key, newMessage.getTimestamp(), newMessage.getRealtimestamp(), true);
                //add to local db
                Single.just(1).observeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        MainActivity.localDatabase.localDBDao().insertMessages(newMessage.toLocalMessage(false));
                        if (!isConversationCreated) { //conversation's not yet created
                            if (membersString != null && membersString.length() > 0) { //group chat
                                int memNumber = 0;
                                ArrayList<Integer> indexes = new ArrayList<>();
                                indexes.add(membersString.indexOf('*'));
                                for (int i = 0; i < memNumber - 2; i++) { //number of indexes = number of members - 1
                                    indexes.add(membersString.indexOf('*', (indexes.get(i) + 1)));
                                }
                                ArrayList<String> usersArr = new ArrayList<>();
                                usersArr.add(membersString.substring(0, indexes.get(0)));
                                for (int i = 0; i < indexes.size(); i++) {
                                    if (i + 1 < indexes.size()) {
                                        usersArr.add(membersString.substring(indexes.get(i) + 1, indexes.get(i + 1)));

                                    } else {
                                        usersArr.add(membersString.substring(indexes.get(i) + 1, membersString.length()));
                                    }
                                }
                                Conversation conversation = new Conversation(currentCId, newMessage, membersString.replace("*", ", "), usersArr);

                                //add to local db
                                MainActivity.localDatabase.localDBDao().insertConversations(conversation.toLocalConversation(false));
                                ConversationItem newItem = conversation.makeConversationItem();
                                HistoryFragment.conversationsItem.add(newItem);

                                //update firebase
                                MainActivity.database.child("conversations").child(currentCId).setValue(conversation);
                                //String msgKey = MainActivity.database.child("messages").child(currentCId).push().getKey();
                                //MainActivity.database.child("messages").child(currentCId).child(msgKey).setValue(newMessage);
                                for (String s : usersArr) {
                                    String key = MainActivity.database.child("users").child(s).child("conversations").push().getKey();
                                    MainActivity.database.child("users").child(s).child("conversations").child(key).setValue(currentCId);
                                }
                                //update UI
                                Single.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Integer>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(Integer integer) {
                                        HistoryFragment.adapter = new HistoryRvAdapter(HistoryFragment.conversationsItem, getApplicationContext());
                                        HistoryFragment.recyclerView.setAdapter(HistoryFragment.adapter);
                                        ConversationActivity.initRecyclerView();
                                        ConversationActivity.isConversationCreated = true;
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }
                                });
                            } else { //not a group chat
                                Log.i("currentCid", "" + currentCId);
                                int dividerIndex = currentCId.indexOf('*');
                                String user1 = currentCId.substring(0, dividerIndex);
                                String user2 = currentCId.substring(dividerIndex + 1, currentCId.length());
                                ArrayList<String> userArr = new ArrayList<>();
                                userArr.add(user1);
                                userArr.add(user2);
                                Conversation conversation = new Conversation(currentCId, newMessage, user1 + " and " + user2, userArr);
                                MainActivity.localDatabase.localDBDao().insertConversations(conversation.toLocalConversation(false));
                                ConversationItem newItem = conversation.makeConversationItem();
                                HistoryFragment.conversationsItem.add(newItem);
                                //update firebase
                                MainActivity.database.child("conversations").child(currentCId).setValue(conversation);
                                //String msgKey = MainActivity.database.child("messages").child(currentCId).push().getKey();
                                //MainActivity.database.child("messages").child(currentCId).child(msgKey).setValue(newMessage);
                                String key1 = MainActivity.database.child("users").child(user1).child("conversations").push().getKey();
                                MainActivity.database.child("users").child(user1).child("conversations").child(key1).setValue(currentCId);
                                String key2 = MainActivity.database.child("users").child(user2).child("conversations").push().getKey();
                                MainActivity.database.child("users").child(user2).child("conversations").child(key2).setValue(currentCId);
                                //update UI
                                Single.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Integer>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(Integer integer) {
                                        HistoryFragment.adapter = new HistoryRvAdapter(HistoryFragment.conversationsItem, getApplicationContext());
                                        HistoryFragment.recyclerView.setAdapter(HistoryFragment.adapter);
                                        ConversationActivity.initRecyclerView();
                                        ConversationActivity.isConversationCreated = true;
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }
                                });
                            }

                        } else { //conversation is already created
                            Single.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Integer>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Integer integer) {
                                    messageItems.add(newMsgItem);
                                    recyclerView.getAdapter().notifyItemInserted(messageItems.size() - 1);
                                    recyclerView.scrollToPosition(messageItems.size() - 1);
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });

                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
                //add to storage
                FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference msgStorageReference = storage.getReference().child("messages").child(key + ".jpg");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] dataArr = baos.toByteArray();
                uploadImageToServer(msgStorageReference, dataArr, newMessage, key, newMsgItem);
                break;
            }
            case PICK_VIDEO_REQUEST: {
                Log.i("vidMsg", "" + isConversationCreated);
                /*if (data == null || data.getData() == null) {
                    Toast.makeText(getApplicationContext(),"Failed to choose video, Intent data = null",Toast.LENGTH_SHORT).show();
                    break;
                }*/
                //Uri selectedVideoUri = data.getData();
                Uri selectedVideoUri = VideoPicker.getVideoUriFromResult(getApplicationContext(), resultCode, data);
                if (selectedVideoUri == null) {
                    Toast.makeText(getApplicationContext(), "Failed to choose video, Uri = null", Toast.LENGTH_SHORT).show();
                    break;
                }
                // OI FILE Manager
                //String filemanagerstring = selectedVideoUri.getPath();

                // MEDIA GALLERY
                //String selectedVideoPath = generatePath(selectedVideoUri, ConversationActivity.this);
                String selectedVideoPath = VideoPicker.getVideoFromResult(getApplicationContext(), resultCode, data);
                //Log.i("pathComparation", "" + selectedVideoPath + ", " + selectedVideoPath2);
                if (selectedVideoPath == null || selectedVideoPath.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Failed to choose video, Path = null", Toast.LENGTH_SHORT).show();
                    break;
                }
                final String vidKey = MainActivity.database.child("messages").child(currentCId).push().getKey();

                //get vid long
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                //use one of overloaded setDataSource() functions to set your data source
                File vidFile = new File(selectedVideoPath);
                retriever.setDataSource(getApplicationContext(), selectedVideoUri);

                //create thumbnail
                Bitmap thumbnail = retriever.getFrameAtTime();
                int heightPx = thumbnail.getHeight();
                int widthPx = thumbnail.getWidth();
                boolean isVertical = heightPx >= widthPx;
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long timeInMillisec = Long.parseLong(time);
                Log.i("vidMsg", heightPx + ", " + widthPx);
                retriever.release();

                String vidDuration = String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes(timeInMillisec),
                        TimeUnit.MILLISECONDS.toSeconds(timeInMillisec) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMillisec)));

                //save thumbnail to storage
                new ImageSaver(getApplicationContext()).
                        setFileName(vidKey + "thumbnail.jpg").
                        setDirectoryName("messages").
                        save(thumbnail);

                //create message object
                long realTimestamp = System.currentTimeMillis();
                Date date = new Date(realTimestamp);
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String timestamp = sdf.format(date);
                final Message newMessage = new Message("/-video:" + vidKey + " /-duration:" + vidDuration + " /-height:" + heightPx + " /-width:" + widthPx, MainActivity.currentUser.getUsername(), timestamp, realTimestamp, currentCId);
                Log.i("vidMsg", "" + newMessage.toString());

                //width = 200dp
                int heightDp = isVertical ? heightPx * 150 / widthPx : heightPx * 150 / widthPx;
                float calculatedHeightPx = (heightDp * MainActivity.scale + 0.5f);
                float calculatedWidthPx = isVertical ? (150 * MainActivity.scale + 0.5f) : (200 * MainActivity.scale + 0.5f);
                final MyVideoMessageItem newMsgItem = new MyVideoMessageItem(MainActivity.currentUser.getUsername(), vidKey, vidDuration, timestamp, realTimestamp, true, calculatedHeightPx, calculatedWidthPx);

                //now have msg and msg item
                if (!isConversationCreated) { //conversation's not yet created
                    if (membersString != null && membersString.length() > 0) { //group chat
                        int memNumber = 0;
                        ArrayList<Integer> indexes = new ArrayList<>();
                        indexes.add(membersString.indexOf('*'));
                        for (int i = 0; i < memNumber - 2; i++) { //number of indexes = number of members - 1
                            indexes.add(membersString.indexOf('*', (indexes.get(i) + 1)));
                        }
                        ArrayList<String> usersArr = new ArrayList<>();
                        usersArr.add(membersString.substring(0, indexes.get(0)));
                        for (int i = 0; i < indexes.size(); i++) {
                            if (i + 1 < indexes.size()) {
                                usersArr.add(membersString.substring(indexes.get(i) + 1, indexes.get(i + 1)));

                            } else {
                                usersArr.add(membersString.substring(indexes.get(i) + 1, membersString.length()));
                            }
                        }
                        Conversation conversation = new Conversation(currentCId, newMessage, membersString.replace("*", ", "), usersArr);

                        //add to local db
                        Single.just(1).observeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Integer integer) {
                                MainActivity.localDatabase.localDBDao().insertConversations(conversation.toLocalConversation(false));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });

                        ConversationItem newItem = conversation.makeConversationItem();
                        HistoryFragment.conversationsItem.add(newItem);
                        HistoryFragment.adapter = new HistoryRvAdapter(HistoryFragment.conversationsItem, getApplicationContext());
                        HistoryFragment.recyclerView.setAdapter(HistoryFragment.adapter);
                        ConversationActivity.initRecyclerView();
                        ConversationActivity.isConversationCreated = true;
                        //update firebase
                        MainActivity.database.child("conversations").child(currentCId).setValue(conversation);
                        //String msgKey = MainActivity.database.child("messages").child(currentCId).push().getKey();
                        //MainActivity.database.child("messages").child(currentCId).child(msgKey).setValue(newMessage);
                        for (String s : usersArr) {
                            String key = MainActivity.database.child("users").child(s).child("conversations").push().getKey();
                            MainActivity.database.child("users").child(s).child("conversations").child(key).setValue(currentCId);
                        }
                    } else { //not a group chat
                        Log.i("currentCid", "" + currentCId);
                        int dividerIndex = currentCId.indexOf('*');
                        String user1 = currentCId.substring(0, dividerIndex);
                        String user2 = currentCId.substring(dividerIndex + 1, currentCId.length());
                        ArrayList<String> userArr = new ArrayList<>();
                        userArr.add(user1);
                        userArr.add(user2);
                        Conversation conversation = new Conversation(currentCId, newMessage, user1 + " and " + user2, userArr);
                        ConversationItem newItem = conversation.makeConversationItem();
                        HistoryFragment.conversationsItem.add(newItem);

                        Single.just(1).observeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Integer integer) {
                                MainActivity.localDatabase.localDBDao().insertConversations(conversation.toLocalConversation(false));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });

                        //update firebase
                        MainActivity.database.child("conversations").child(currentCId).setValue(conversation);
                        //String msgKey = MainActivity.database.child("messages").child(currentCId).push().getKey();
                        //MainActivity.database.child("messages").child(currentCId).child(msgKey).setValue(newMessage);
                        String key1 = MainActivity.database.child("users").child(user1).child("conversations").push().getKey();
                        MainActivity.database.child("users").child(user1).child("conversations").child(key1).setValue(currentCId);
                        String key2 = MainActivity.database.child("users").child(user2).child("conversations").push().getKey();
                        MainActivity.database.child("users").child(user2).child("conversations").child(key2).setValue(currentCId);
                    }

                } else { //conversation is already created
                    Log.i("vidMsg", "conversation created");
                    Single.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Integer integer) {
                            messageItems.add(newMsgItem);
                            recyclerView.getAdapter().notifyItemInserted(messageItems.size() - 1);
                            recyclerView.scrollToPosition(messageItems.size() - 1);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });

                }
                //compress video
                Single.just(selectedVideoPath).subscribeOn(Schedulers.io())
                        .map(s -> {
                            File selectedVideoFile = new File(s);
                            String videoDirectory = selectedVideoFile.getParent();
                            return SiliCompressor.with(getApplicationContext()).compressVideo(selectedVideoPath, videoDirectory);
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onSuccess(String s) {
                                //compress complete
                                if (s == null || s.length() == 0) {
                                    Toast.makeText(getApplicationContext(), "can not compress video", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                //add to storage
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                final StorageReference videoMsgStorageReference = storage.getReference().child("messages");
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                                byte[] dataArr = baos.toByteArray();
                                uploadVideoToServer(s, videoMsgStorageReference, dataArr, newMessage, vidKey, newMsgItem);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
                break;
            }
            case PICK_LOCATION_REQUEST: {
                if (resultCode == RESULT_OK) {
                    double myLat = 0;
                    double myLong = 0;
                    Place place = PlacePicker.getPlace(this, data);
                    myLat = place.getLatLng().latitude;
                    myLong = place.getLatLng().longitude;
                    long realTimestamp = System.currentTimeMillis();
                    Date date = new Date(realTimestamp);
                    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String timestamp = sdf.format(date);
                    //create new message object
                    Message newMsg = new Message("/-lat:" + myLat + " /-long:" + myLong, MainActivity.currentUser.getUsername(), timestamp, realTimestamp, currentCId);
                    //create new message item
                    BaseMessageItem newItem = newMsg.toMessageItem(true);
                    if(isConversationCreated) { //already have conversation
                        messageItems.add(newItem);
                        //update UI
                        adapter.notifyItemInserted(messageItems.size() - 1);
                        recyclerView.scrollToPosition(messageItems.size() - 1);
                        //update local db and firebase
                        Single.just(1).subscribeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Integer integer) {
                                MainActivity.localDatabase.localDBDao().insertMessages(newMsg.toLocalMessage(false));
                                MainActivity.localDatabase.localDBDao().updateLastMsg(currentCId, newMsg.getMessage(), newMsg.getName(), newMsg.getRealtimestamp(), newMsg.getTimestamp(), false);
                                String newKey = MainActivity.database.child("messages").child(currentCId).push().getKey();
                                MainActivity.database.child("messages").child(currentCId).child(newKey).setValue(newMsg).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Single.just(1).observeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onSuccess(Integer integer) {
                                                MainActivity.localDatabase.localDBDao().updateLocalMsgSendStatus(currentCId, MainActivity.currentUser.getUsername(),newMsg.getRealtimestamp(), true);
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }
                                        });
                                    }
                                });
                                MainActivity.database.child("conversations").child(currentCId).child("lastMessage").setValue(newMsg).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Single.just(1).observeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onSuccess(Integer integer) {
                                                MainActivity.localDatabase.localDBDao().updateLocalConversationSendStatus(currentCId,true);

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }
                                        });
                                    }
                                });

                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
                    }
                    else { //create new conversation
                        if (membersString != null && membersString.length() > 0) { //group chat
                            int memNumber = 0;
                            ArrayList<Integer> indexes = new ArrayList<>();
                            indexes.add(membersString.indexOf('*'));
                            for (int i = 0; i < memNumber - 2; i++) { //number of indexes = number of members - 1
                                indexes.add(membersString.indexOf('*', (indexes.get(i) + 1)));
                            }
                            ArrayList<String> usersArr = new ArrayList<>();
                            usersArr.add(membersString.substring(0, indexes.get(0)));
                            for (int i = 0; i < indexes.size(); i++) {
                                if (i + 1 < indexes.size()) {
                                    usersArr.add(membersString.substring(indexes.get(i) + 1, indexes.get(i + 1)));

                                } else {
                                    usersArr.add(membersString.substring(indexes.get(i) + 1, membersString.length()));
                                }
                            }
                            Conversation conversation = new Conversation(currentCId, newMsg, membersString.replace("*", ", "), usersArr);

                            //add to local db
                            Single.just(1).observeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Integer integer) {
                                    MainActivity.localDatabase.localDBDao().insertConversations(conversation.toLocalConversation(false));
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });

                            ConversationItem newMsgItem = conversation.makeConversationItem();
                            HistoryFragment.conversationsItem.add(newMsgItem);
                            HistoryFragment.adapter = new HistoryRvAdapter(HistoryFragment.conversationsItem, getApplicationContext());
                            HistoryFragment.recyclerView.setAdapter(HistoryFragment.adapter);
                            ConversationActivity.initRecyclerView();
                            ConversationActivity.isConversationCreated = true;
                            //update firebase
                            MainActivity.database.child("conversations").child(currentCId).setValue(conversation);
                            //String msgKey = MainActivity.database.child("messages").child(currentCId).push().getKey();
                            //MainActivity.database.child("messages").child(currentCId).child(msgKey).setValue(newMessage);
                            for (String s : usersArr) {
                                String key = MainActivity.database.child("users").child(s).child("conversations").push().getKey();
                                MainActivity.database.child("users").child(s).child("conversations").child(key).setValue(currentCId);
                            }
                        } else { //not a group chat
                            Log.i("currentCid", "" + currentCId);
                            int dividerIndex = currentCId.indexOf('*');
                            String user1 = currentCId.substring(0, dividerIndex);
                            String user2 = currentCId.substring(dividerIndex + 1, currentCId.length());
                            ArrayList<String> userArr = new ArrayList<>();
                            userArr.add(user1);
                            userArr.add(user2);
                            Conversation conversation = new Conversation(currentCId, newMsg, user1 + " and " + user2, userArr);
                            ConversationItem newMsgItem = conversation.makeConversationItem();
                            HistoryFragment.conversationsItem.add(newMsgItem);

                            Single.just(1).observeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Integer integer) {
                                    MainActivity.localDatabase.localDBDao().insertConversations(conversation.toLocalConversation(false));
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });

                            //update firebase
                            MainActivity.database.child("conversations").child(currentCId).setValue(conversation);
                            //String msgKey = MainActivity.database.child("messages").child(currentCId).push().getKey();
                            //MainActivity.database.child("messages").child(currentCId).child(msgKey).setValue(newMessage);
                            String key1 = MainActivity.database.child("users").child(user1).child("conversations").push().getKey();
                            MainActivity.database.child("users").child(user1).child("conversations").child(key1).setValue(currentCId);
                            String key2 = MainActivity.database.child("users").child(user2).child("conversations").push().getKey();
                            MainActivity.database.child("users").child(user2).child("conversations").child(key2).setValue(currentCId);
                        }

                    }
                }
            }
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public void uploadImageToServer(StorageReference msgStorageReference, byte[] dataArr, final Message newMessage, final String key, final MyImageMessageItem newMsgItem) {
        newMsgItem.setStatus("Đang gửi...");
        msgStorageReference.putBytes(dataArr)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        MainActivity.database.child("conversations").child(currentCId).child("lastMessage").setValue(newMessage);
                        MainActivity.database.child("messages").child(currentCId).child(key).setValue(newMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Single.just(1).observeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(Integer integer) {
                                        MainActivity.localDatabase.localDBDao().updateLocalMsgSendStatus(currentCId, newMessage.getName(), newMessage.getRealtimestamp(), true);
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }
                                });
                            }
                        });
                        //delete local img on success
                        /*new ImageSaver(getApplicationContext()).
                                setFileName(key + ".jpg").
                                setDirectoryName("messages").
                                deleteFile();*/
                        //newMsgItem.setImgName(currentCId + "/" + key);
                        //hide text view "Đang gửi..."
                        newMsgItem.setStatusTextViewVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "complete", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        newMsgItem.setRetryButtonVisibility(View.VISIBLE);
                        newMsgItem.setStatus("Gửi thất bại");
                    }
                });
    }

    public void uploadVideoToServer(String videoPath, StorageReference msgStorageReference, byte[] dataArr, final Message newMessage, final String key, final MyVideoMessageItem newMsgItem) {
        //upload thumbnail
        msgStorageReference.child(key + "thumbnail.jpg").putBytes(dataArr)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        MainActivity.database.child("conversations").child(currentCId).child("lastMessage").setValue(newMessage);
                        MainActivity.database.child("messages").child(currentCId).child(key).setValue(newMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Single.just(1).observeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(Integer integer) {
                                        MainActivity.localDatabase.localDBDao().updateLocalMsgSendStatus(currentCId, newMessage.getName(), newMessage.getRealtimestamp(), true);
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }
                                });
                            }
                        });
                        //delete local img on success
                        /*new ImageSaver(getApplicationContext()).
                                setFileName(key + ".jpg").
                                setDirectoryName("messages").
                                deleteFile();*/
                        //newMsgItem.setVideoName(currentCId + "/" + key);
                    }
                });
        //upload video
        Uri file = Uri.fromFile(new File(videoPath));
        msgStorageReference.child(key + ".mp4").putFile(file).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                //hide text view "Đang gửi..."
                newMsgItem.setVidStatusTextViewVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Đã gửi", Toast.LENGTH_SHORT).show();
                newMsgItem.setVideoName(newMsgItem.videoName);
            }
        });
    }

    public void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(ConversationActivity.externalCachedir + "/" + key + ".mp3");
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "recorder.prepare() failed", Toast.LENGTH_SHORT).show();
        }
        mRecorder.start();
    }

    public void stopRecording() {
        try {
            mRecorder.stop();
        } catch (RuntimeException ex) {
            Toast.makeText(getApplicationContext(), "nothing to record", Toast.LENGTH_SHORT).show();
        }
        mRecorder.release();
        mRecorder = null;
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(ConversationActivity.externalCachedir + "/" + key + ".mp3");
            mPlayer.prepare();
            int millis = mPlayer.getDuration();
            String duration = String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
            );
            long realTimestamp = System.currentTimeMillis();
            Date date = new Date(realTimestamp);
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String timestamp = sdf.format(date);
            //update conversation RecyclerView
            MyAudioMessageItem newItem = new MyAudioMessageItem(MainActivity.currentUser.getUsername(), key, duration, timestamp, realTimestamp);
            messageItems.add(newItem);
            adapter.notifyItemInserted(messageItems.size() - 1);
            recyclerView.scrollToPosition(messageItems.size() - 1);
            Message message = new Message("/-audio:" + key + " /-duration:" + duration, MainActivity.currentUser.getUsername(), timestamp, realTimestamp, currentCId);
            //update local and firebase database
            Single.just(1).observeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(Integer integer) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    Uri file = Uri.fromFile(new File(ConversationActivity.externalCachedir + "/" + key + ".mp3"));
                    storageReference.child("messages/" + currentCId + "/" + key + ".mp3").putFile(file)
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    Toast.makeText(getApplicationContext(), "Upload audio complete", Toast.LENGTH_SHORT).show();
                                }
                            });
                    MainActivity.localDatabase.localDBDao().insertMessages(message.toLocalMessage(false));
                    MainActivity.localDatabase.localDBDao().updateLastMsg(currentCId, message.message, message.name, message.realtimestamp, message.timestamp, false);
                    MainActivity.database.child("messages").child(currentCId).child(key).setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Single.just(1).observeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Integer integer) {
                                    MainActivity.localDatabase.localDBDao().updateLocalMsgSendStatus(currentCId, MainActivity.currentUser.getUsername(), message.realtimestamp, true);
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });
                        }
                    });
                    MainActivity.database.child("conversations").child(currentCId).child("lastMessage").setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Single.just(1).observeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Integer integer) {
                                    MainActivity.localDatabase.localDBDao().updateLocalConversationSendStatus(currentCId, true);
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });
                        }
                    });
                }

                @Override
                public void onError(Throwable e) {

                }
            });
            //mPlayer.start();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "player.prepare() failed", Toast.LENGTH_SHORT).show();
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public String generatePath(Uri uri, Context context) {
        String filePath = null;
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat) {
            filePath = generateFromKitkat(uri, context);
        }

        if (filePath != null) {
            return filePath;
        }

        Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DATA}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return filePath == null ? uri.getPath() : filePath;
    }

    @TargetApi(19)
    private String generateFromKitkat(Uri uri, Context context) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String wholeID = DocumentsContract.getDocumentId(uri);

            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Video.Media.DATA};
            String sel = MediaStore.Video.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().
                    query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);


            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }

            cursor.close();
        }
        return filePath;
    }
}
