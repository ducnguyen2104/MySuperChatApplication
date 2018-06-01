package com.ducnguyenvan.mysuperchatapplication.Contacts;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ducnguyenvan.mysuperchatapplication.MainActivity;
import com.ducnguyenvan.mysuperchatapplication.Model.ContactItem;
import com.ducnguyenvan.mysuperchatapplication.R;
import com.ducnguyenvan.mysuperchatapplication.databinding.FragmentContactsBinding;

import java.util.ArrayList;

public class ContactsFragment extends Fragment {

    EditText edt;
    Button btn;
    public static RecyclerView recyclerView;
    public static ArrayList<ContactItem> contacts; //= new ArrayList<>();
    ContactViewModel contactViewModel;
    FragmentContactsBinding fragmentContactsBinding;
    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    public ContactsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_contacts,container,false);
        contacts = new ArrayList<>();
        fragmentContactsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_contacts,container,false);
        View rootView = fragmentContactsBinding.getRoot();
        contactViewModel = new ContactViewModel(getActivity());
        fragmentContactsBinding.setContactViewModel(contactViewModel);
        edt = (EditText)rootView.findViewById(R.id.find_conversation_edt);
        edt.setWidth((int)(MainActivity.scrWidth*0.8));
        btn = (Button)rootView.findViewById(R.id.create_grchat_btn);
        btn.setWidth((int)(MainActivity.scrWidth*0.2));
        recyclerView = rootView.findViewById(R.id.contacts_rv);
        initRecyclerView(rootView);
        return rootView;
    }

    public void initRecyclerView(View rootView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        if (MainActivity.currentUser.getContacts() != null) {
            for(int i = 0; i < MainActivity.currentUser.getContacts().size(); i++) {
                contacts.add(new ContactItem(R.drawable.avt, MainActivity.currentUser.getContacts().get(i)));
            }
        }
        final ContactRvAdapter adapter = new ContactRvAdapter(contacts,getContext());
        recyclerView.setAdapter(adapter);

    }

    public static  void updateRvAfterAdd(String newContactUserName) {
        contacts.add(new ContactItem(R.drawable.avt, newContactUserName));
        recyclerView.getAdapter().notifyItemInserted(contacts.size() - 1);
    }
}
