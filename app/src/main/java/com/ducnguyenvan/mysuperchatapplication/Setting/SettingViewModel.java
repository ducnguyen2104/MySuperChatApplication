package com.ducnguyenvan.mysuperchatapplication.Setting;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

public class SettingViewModel extends ViewModel {

    public ObservableField<String> greeting = new ObservableField<>();

    public SettingViewModel(String name) {
        greeting.set("Xin chào " + name + " !");
    }

    public void onChangeAvtClick() {

    }

    public void onChangePasswordClick() {

    }
}
