package com.example.rememberme.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.rememberme.EditUserProfileActivity;
import com.example.rememberme.R;
import com.example.rememberme.RoundImage;

import java.io.FileInputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {
    ImageView photo;
    RoundImage roundedImage;

    private NotificationsViewModel notificationsViewModel;
    TextView edit;
    TextView age;
    TextView birthday;
    TextView location;
    TextView contact1;
    TextView contact2;
    TextView name;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        photo = root.findViewById(R.id.photo_profile);
        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable._pic);
        roundedImage = new RoundImage(bm);
        photo.setImageDrawable(roundedImage);

        age = root.findViewById(R.id.age);
        birthday = root.findViewById(R.id.birthday);
        location = root.findViewById(R.id.location);
        contact1 = root.findViewById(R.id.emergency_contact_1);
        contact2 = root.findViewById(R.id.emergency_contact_2);
        name = root.findViewById(R.id.name);

        updateView();

        contact1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: " + contact1.getText()));
                startActivity(intent);
            }
        });

        contact2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: " + contact2.getText()));
                startActivity(intent);
            }
        });

        edit = root.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditUserProfileActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        updateView();
        super.onStart();
    }

    public void updateView(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        age.setText(sharedPreferences.getString(EditUserProfileActivity.AGE_KEY,""));
        birthday.setText(sharedPreferences.getString(EditUserProfileActivity.BIRTHDAY_KEY,""));
        location.setText(sharedPreferences.getString(EditUserProfileActivity.LOCATION_KEY,""));
        contact1.setText(sharedPreferences.getString(EditUserProfileActivity.PHONE1_KEY,""));
        contact2.setText(sharedPreferences.getString(EditUserProfileActivity.PHONE2_KEY,""));
        String nameStr = sharedPreferences.getString(EditUserProfileActivity.FIRST_NAME_KEY, "") +
                " " + sharedPreferences.getString(EditUserProfileActivity.LAST_NAME_KEY,"");
        name.setText(nameStr);
        try {
            FileInputStream fis = this.getActivity().openFileInput(EditUserProfileActivity.saveImgFileName);
            Bitmap bmap = BitmapFactory.decodeStream(fis);
            roundedImage = new RoundImage(bmap);
            photo.setImageDrawable(roundedImage);
            fis.close();
        } catch (IOException e) {

        }

    }
}