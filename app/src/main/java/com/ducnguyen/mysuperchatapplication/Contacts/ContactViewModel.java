package com.ducnguyen.mysuperchatapplication.Contacts;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;

import com.ducnguyen.mysuperchatapplication.Contacts.AddContact.AddContactActivity;

public class ContactViewModel extends ViewModel {

    public ContactViewModel(Context context) {
        this.context = context;
    }

    public Context context;

    public void onAddButtonClicked() {
        Intent intent = new Intent(context,AddContactActivity.class);
        context.startActivity(intent);
    }
}
