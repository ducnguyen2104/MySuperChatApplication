package com.ducnguyen.mysuperchatapplication.History.CreateGroupChat;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

public class GrChatContactItemViewModel extends ViewModel {

    String username;
    ObservableBoolean isChecked;

    public GrChatContactItemViewModel() {
        isChecked = new ObservableBoolean(false);
        //isChecked.set(false);
    }

    public void onClick() {
        isChecked.set(!isChecked.get());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ObservableBoolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(ObservableBoolean isChecked) {
        this.isChecked = isChecked;
    }
}
