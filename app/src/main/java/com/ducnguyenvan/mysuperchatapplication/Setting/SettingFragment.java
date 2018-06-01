package com.ducnguyenvan.mysuperchatapplication.Setting;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ducnguyenvan.mysuperchatapplication.GlideApp;
import com.ducnguyenvan.mysuperchatapplication.MainActivity;
import com.ducnguyenvan.mysuperchatapplication.R;
import com.ducnguyenvan.mysuperchatapplication.databinding.FragmentSettingBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class SettingFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    Button btn;
    ImageView img;
    SettingViewModel settingViewModel;
    FragmentSettingBinding fragmentSettingBinding;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference avtStorageReference = storage.getReference().child("avartar").child(MainActivity.currentUser.getUsername() + ".jpg");

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    public SettingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentSettingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        //View rootView = inflater.inflate(R.layout.fragment_setting,container,false);
        View rootView = fragmentSettingBinding.getRoot();
        settingViewModel = new SettingViewModel(MainActivity.currentUser.getFullname());
        fragmentSettingBinding.setSettingViewModel(settingViewModel);
        btn = (Button) rootView.findViewById(R.id.change_avt);
        img = (ImageView) rootView.findViewById(R.id.avt);
        GlideApp.with(getActivity())
                .load(avtStorageReference)
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(img);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(getActivity());
                startActivityForResult(chooseImageIntent, PICK_IMAGE_REQUEST);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                if (bitmap == null) {
                    break;
                }
                img.setImageBitmap(bitmap);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference avtStorageReference = storage.getReference().child("avartar").child(MainActivity.currentUser.getUsername() + ".jpg");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] dataArr = baos.toByteArray();
                avtStorageReference.putBytes(dataArr)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getContext(), "Upload complete", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_LONG).show();
                            }
                        });
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
