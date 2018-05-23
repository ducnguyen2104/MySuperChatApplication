package com.ducnguyenvan.mysuperchatapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.ducnguyenvan.mysuperchatapplication.Contacts.ContactsFragment;
import com.ducnguyenvan.mysuperchatapplication.History.HistoryFragment;
import com.ducnguyenvan.mysuperchatapplication.Model.User;
import com.ducnguyenvan.mysuperchatapplication.Setting.SettingFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public static int scrWidth;
    public static int scrHeight;

    public static User currentUser;
    public static DatabaseReference database;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        MainActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        scrHeight = displayMetrics.heightPixels;
        scrWidth = displayMetrics.widthPixels;
        String username = getIntent().getStringExtra("username");
        database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseReference = database.child("users");
        Query userQuery = databaseReference.orderByChild("username").equalTo(username);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    currentUser = singleSnapshot.getValue(User.class);
                    Log.i("main conversation", currentUser.getConversations()+"");
                }
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                mViewPager = (ViewPager) findViewById(R.id.container);
                mViewPager.setAdapter(mSectionsPagerAdapter);
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(mViewPager);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        mViewPager = (ViewPager) findViewById(R.id.container);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);
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
}
