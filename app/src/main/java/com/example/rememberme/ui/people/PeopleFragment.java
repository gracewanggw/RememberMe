package com.example.rememberme.ui.people;

import android.content.Intent;

import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rememberme.DB.RememberMeDbSource;
import com.example.rememberme.EditFramilyProfile;
import com.example.rememberme.Framily;
import com.example.rememberme.FramilyProfile;
import com.example.rememberme.R;
import com.example.rememberme.RoundImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class PeopleFragment extends Fragment {

    GridView gridView;
    List<Framily> people;

    RememberMeDbSource dataSource;


    private HomeViewModel homeViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_people, container, false);

        setHasOptionsMenu(true);

        gridView = root.findViewById(R.id.people_grid_view);

        updateView();
        return root;
    }

    @Override
    public void onResume() {
        updateView();
        super.onResume();
    }

    public void updateView(){

        dataSource = new RememberMeDbSource(getActivity().getApplicationContext());
        dataSource.open();

        people = dataSource.fetchFramilyEntries();

        CustomAdapter customAdapter = new CustomAdapter(people);
        gridView.setAdapter(customAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FramilyProfile.class);
                intent.putExtra(FramilyProfile.ID_KEY,customAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });

    }

    // adapter to show grid view of people and their names and relationship from database
    private class CustomAdapter extends BaseAdapter{
        List<Framily> framilies;
        FileInputStream fis;

        public CustomAdapter(List<Framily> people){
            framilies = people;
        }

        @Override
        public int getCount() {
            return framilies.size();
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Framily getItem(int position){
            return framilies.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = getLayoutInflater().inflate(R.layout.people_grid_item,null);

            ImageView imageView = (ImageView) v.findViewById(R.id.framily_Image);
            TextView nameView = v.findViewById(R.id.framily_Name);
            TextView relationView = v.findViewById(R.id.framily_Relationship);

            Framily fram = framilies.get(position);
            byte[] image = fram.getImage();
            String name = fram.getNameFirst();
            String relationship = fram.getRelationship();
            if (image != null) {
                Log.d("rdudak", "Bitmap for " + name + ": " + image.toString());
                Bitmap bmp= BitmapFactory.decodeByteArray(image, 0 , image.length);
                imageView.setImageBitmap(bmp);
                if(bmp!=null){
                    RoundImage roundedImage = new RoundImage(bmp);
                    imageView.setImageDrawable(roundedImage);
                    imageView.setImageBitmap(bmp);
                    Log.d("rdudak", "profile photo set");
                }
//                RoundImage roundedImage = new RoundImage(bmp);
//                imageView.setImageDrawable(roundedImage);
//                imageView.setImageBitmap(bmp);
//                Log.d("rdudak", "profile photo set");
            }
            else {
                Log.d("rdudak", "default photo set");
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable._pic);
                RoundImage roundedImage = new RoundImage(bitmap);
                imageView.setImageDrawable(roundedImage);
            }

//            Bitmap bm = BitmapFactory.decodeResource(getResources(),image);
//            RoundImage roundedImage = new RoundImage(bm);
//            imageView.setImageDrawable(roundedImage);

            nameView.setText(name);
            relationView.setText(relationship);

            return v;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
       inflater.inflate(R.menu.add_person_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.add_person){
            Intent intent = new Intent(getActivity(), EditFramilyProfile.class);
            startActivity(intent);
        }
        return true;
    }
}