package com.example.rememberme.ui.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rememberme.EditFramilyProfile;
import com.example.rememberme.R;
import com.example.rememberme.RoundImage;

public class ProfileFragment extends Fragment {
    ImageView photo;
    RoundImage roundedImage;

    private NotificationsViewModel notificationsViewModel;
    TextView edit;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        photo = root.findViewById(R.id.photo_profile);
        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable._pic);
        roundedImage = new RoundImage(bm);
        photo.setImageDrawable(roundedImage);

        edit = root.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditFramilyProfile.class);
                startActivity(intent);
            }
        });
        return root;
    }
}