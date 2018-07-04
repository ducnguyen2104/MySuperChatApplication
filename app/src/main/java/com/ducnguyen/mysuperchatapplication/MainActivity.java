package com.ducnguyen.mysuperchatapplication;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.ducnguyen.mysuperchatapplication.Contacts.ContactsFragment;
import com.ducnguyen.mysuperchatapplication.History.HistoryFragment;
import com.ducnguyen.mysuperchatapplication.LocalDB.LocalDatabase;
import com.ducnguyen.mysuperchatapplication.Login.LoginActivity;
import com.ducnguyen.mysuperchatapplication.Model.LocalConversation;
import com.ducnguyen.mysuperchatapplication.Model.LocalMessage;
import com.ducnguyen.mysuperchatapplication.Model.LocalUser;
import com.ducnguyen.mysuperchatapplication.Model.LoggedInUser;
import com.ducnguyen.mysuperchatapplication.Model.User;
import com.ducnguyen.mysuperchatapplication.Register.RegisterActivity;
import com.ducnguyen.mysuperchatapplication.Setting.SettingFragment;
import com.ducnguyen.mysuperchatapplication.Utils.MergeData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static int scrWidth;
    public static int scrHeight;
    public static float scale;

    public static User currentUser;
    public static DatabaseReference database;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public static LocalDatabase localDatabase;

    public static MergeData mergeData;

    public static String username;


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get local database instance
        localDatabase = Room.databaseBuilder(getApplicationContext(), LocalDatabase.class, "localDB")//.fallbackToDestructiveMigration()
                .build();
        localDatabase.localDBDao().getLoggedInUser().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<LoggedInUser>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(LoggedInUser loggedInUser) {
                if (loggedInUser == null || loggedInUser.getUsername() == null) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                username = loggedInUser.getUsername();
                Single.just(username).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        currentUser = new User();
                        //get screen width and height
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        MainActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        scrHeight = displayMetrics.heightPixels;
                        scrWidth = displayMetrics.widthPixels;
                        scale = getApplicationContext().getResources().getDisplayMetrics().density;
                        //get user in local db
                        localDatabase.localDBDao().getUserByName(username)//single
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<LocalUser>() {
                                    @Override
                                    public void onSubscribe(Subscription s) {

                                    }

                                    @Override
                                    public void onNext(LocalUser localUser) {
                                        currentUser = localUser.toUser();
                                    }

                                    @Override
                                    public void onError(Throwable t) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });


                        //get user from firebase and update current user, local database
                        database = FirebaseDatabase.getInstance().getReference();
                        final User firebaseUser = new User();
                        DatabaseReference databaseReference = database.child("users").child(username);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                                firebaseUser.mapToObject(map);
                                if (!currentUser.equals(firebaseUser)) {
                                    currentUser = firebaseUser;
                                    Single.just(firebaseUser).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new SingleObserver<User>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(User user) {
                                            MainActivity.localDatabase.localDBDao().insertUsers(user.toLocalUser());
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }
                                    });

                                }
                                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                                mViewPager = (ViewPager) findViewById(R.id.container);
                                mViewPager.setAdapter(mSectionsPagerAdapter);
                                mViewPager.setOffscreenPageLimit(2);
                                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                                tabLayout.setupWithViewPager(mViewPager);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                if (currentUser.getUsername().equals("")) {
                                    Toast.makeText(getApplicationContext(), "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

                        reuploadAllFailedUploadMessage();
                        reUploadAllFaileđUploadConversation();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        reuploadAllFailedUploadMessage();
        reUploadAllFaileđUploadConversation();
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);

        builder.setTitle("Thoát");
        builder.setMessage("Thoát ứng dụng?");
        builder.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                currentUser = null;
                finish();
                MainActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HistoryFragment.newInstance();
                case 1:
                    return ContactsFragment.newInstance();
                case 2:
                    return SettingFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Tin nhắn";
                case 1:
                    return "Danh bạ";
                case 2:
                    return "Cài đặt";

            }
            return null;
        }

    }

    public static void reuploadAllFailedUploadMessage() {
        localDatabase.localDBDao().findUploadFailedMessage(false).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<LocalMessage>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<LocalMessage> localMessages) {
                        //reupload to firebase
                        for (LocalMessage single : localMessages) {
                            String key = database.child("messages").child(single.convId).push().getKey();
                            database.child("messages").child(single.convId).child(single.convId + single.getRealtimestamp()).setValue(single.toMessage()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //update local msg status
                                    Single.just(1).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(Integer integer) {
                                            localDatabase.localDBDao().updateLocalMsgSendStatus(single.convId, single.name, single.realtimestamp, true);
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }
                                    });
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public static void reUploadAllFaileđUploadConversation() {
        localDatabase.localDBDao().findUploadFailedConversation(false).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new SingleObserver<List<LocalConversation>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<LocalConversation> localConversations) {
                //reupload to firebase
                for (LocalConversation single : localConversations) {
                    database.child("conversations").child(single.getcId()).child("lastMessage").setValue(single.lastMessage)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Single.just(1).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(Integer integer) {
                                            localDatabase.localDBDao().updateLocalConvStatus(single.getcId(), true);
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }
                                    });
                                }
                            });
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

}
