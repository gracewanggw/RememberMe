package com.example.rememberme.ui.people;

import android.app.Activity;
import android.content.Intent;

import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rememberme.DB.RememberMeDbSource;
import com.example.rememberme.EditFramilyProfile;
import com.example.rememberme.Framily;
import com.example.rememberme.FramilyProfile;
import com.example.rememberme.R;
import com.example.rememberme.RoundImage;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PeopleFragment extends Fragment {

    Spinner spinnerSort;
    Button sortButton;
    CustomAdapter customAdapter;

    String sort = "Oldest to Newest";
    public final String SORT_KEY = "sort key";
    public final String SORT_IDX = "sort idx";
    Activity activity;

    GridView gridView;
    List<Framily> people;
    List<Framily> sorted;

    RememberMeDbSource dataSource;

    Fragment frag;


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

        frag = this;
        activity = this.getActivity();

        spinnerSort = root.findViewById(R.id.sort);
        ArrayAdapter<CharSequence> adapterSort = ArrayAdapter.createFromResource(activity,
                R.array.sort_types, android.R.layout.simple_spinner_item);
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapterSort);

        gridView = root.findViewById(R.id.people_grid_view);

        if(savedInstanceState!=null){
            sort = savedInstanceState.getString(SORT_KEY, "Oldest to Newest");
            spinnerSort.setSelection(savedInstanceState.getInt(SORT_IDX,0));
        }

        sortButton = root.findViewById(R.id.sort_button);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinnerSort.getSelectedItem()!=null){
                    sort = spinnerSort.getSelectedItem().toString();
                }
                sorted = new ArrayList<>();
                sortPeople();
                customAdapter.updateItems(sorted);
            }
        });
        updateView();
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString(SORT_KEY, sort);
        outState.putInt(SORT_IDX, spinnerSort.getSelectedItemPosition());
    }

    @Override
    public void onResume() {
        updateView();
        super.onResume();
    }

    public void updateView(){
        dataSource = new RememberMeDbSource(this.getActivity().getApplicationContext());
        dataSource.open();

        people = dataSource.fetchFramilyEntries();

        customAdapter = new CustomAdapter(people);
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

    public void sortPeople(){
        if(sort.equals("Oldest to Newest")){
            sorted = dataSource.fetchFramilyEntries();
        }
        else if(sort.equals("Newest to Oldest")){
            for(int i = people.size()-1; i>=0; i--){
                sorted.add(people.get(i));
            }
        }
        else if(sort.equals("Name")){
            ArrayList<String> firstNames = new ArrayList<>();
            HashMap<String,Integer> nametoId = new HashMap<>();

            int idx = 0;
            for(Framily person: people){
                firstNames.add(person.getNameFirst()+idx);
                nametoId.put(person.getNameFirst()+idx, idx);
                idx++;
            }
            java.util.Collections.sort(firstNames);
            for(String name: firstNames){
                sorted.add(people.get(nametoId.get(name)));
                idx--;
            }
        }
        else if(sort.equals("Relationship")){
            ArrayList<String> relationships = new ArrayList<>();
            HashMap<String,Integer> relationtoId = new HashMap<>();

            int idx = 0;
            for(Framily person: people){
                relationships.add(person.getRelationship()+idx);
                relationtoId.put(person.getRelationship()+idx, idx);
                idx++;
            }
            java.util.Collections.sort(relationships);
            for(String rel: relationships){
                sorted.add(people.get(relationtoId.get(rel)));
            }
        }
        else if(sort.equals("Location")){
            ArrayList<String> locations = new ArrayList<>();
            HashMap<String,Integer> locationtoId = new HashMap<>();

            int idx = 0;
            for(Framily person: people){
                locations.add(person.getLocation()+idx);
                locationtoId.put(person.getLocation()+idx, idx);
                idx++;
            }
            java.util.Collections.sort(locations);
            for(String loc: locations){
                sorted.add(people.get(locationtoId.get(loc)));
            }
        }
        else if(sort.equals("Age")){
            ArrayList<Integer> ages = new ArrayList<>();
            HashMap<Integer,Integer> agetoId = new HashMap<>();

            int idx = 0;
            for(Framily person: people){
                ages.add(person.getAge()+idx);
                agetoId.put(person.getAge()+idx, idx);
                idx++;
            }
            java.util.Collections.sort(ages);
            for(int age: ages){
                sorted.add(people.get(agetoId.get(age)));
            }
        }
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
            String name = fram.getNameFirst();
            String fileName = fram.getPhotoFileName();
            String relationship = fram.getRelationship();
            Log.d("gwang", "photo file name : " + fileName);
            if(fileName!=null){
                try {
                    Log.d("gwang", "photo file name not null");
                    FileInputStream fis = activity.openFileInput(fileName);
                    Bitmap bmap = BitmapFactory.decodeStream(fis);
                    RoundImage roundedImage = new RoundImage(bmap);
                    imageView.setImageDrawable(roundedImage);
                    fis.close();
                } catch (IOException e) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable._pic);
                    RoundImage roundedImage = new RoundImage(bitmap);
                    imageView.setImageDrawable(roundedImage);
                }
            }

            nameView.setText(name);
            relationView.setText(relationship);
       
            return v;
        }

        public void updateItems(List<Framily> people) {
            framilies.clear();
            framilies.addAll(people);
            this.notifyDataSetChanged();
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