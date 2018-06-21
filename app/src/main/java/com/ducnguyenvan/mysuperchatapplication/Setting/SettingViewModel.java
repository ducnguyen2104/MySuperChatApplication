package com.ducnguyenvan.mysuperchatapplication.Setting;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;

import com.ducnguyenvan.mysuperchatapplication.Login.LoginActivity;
import com.ducnguyenvan.mysuperchatapplication.MainActivity;
import com.ducnguyenvan.mysuperchatapplication.Model.LoggedInUser;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SettingViewModel extends ViewModel {

    public ObservableField<String> greeting = new ObservableField<>();
    public Context context;

    public SettingViewModel(String name, Context context) {
        greeting.set("Xin chào " + name + " !");
        this.context = context;
    }

    public void onChangeAvtClick() {

    }

    public void onChangePasswordClick() {

    }

    public void onSignOutClick() {
        Single.just(1).observeOn(Schedulers.io()).subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer integer) {
                LoggedInUser loggedInUser = new LoggedInUser();
                loggedInUser.setUsername(MainActivity.currentUser.username);
                MainActivity.localDatabase.localDBDao().deleteLoggedInUser(loggedInUser);
                Intent intent = new Intent(context,LoginActivity.class);
                context.startActivity(intent);
                ((Activity)context).finish();
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
