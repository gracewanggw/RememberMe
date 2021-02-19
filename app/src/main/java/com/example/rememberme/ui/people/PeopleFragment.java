package com.example.rememberme.ui.people;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rememberme.Framily;
import com.example.rememberme.R;

import java.util.List;

public class PeopleFragment extends Fragment {

    private HomeViewModel homeViewModel;

    CustomAdapter customAdapter;
    List<Framily> values;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_people, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    public void updateView(){

    }

    private class CustomAdapter extends ArrayAdapter<Framily> {
        private int resourceLayout;
        private Context mContext;
        List<Framily> framilies;

        public CustomAdapter(Context context, int resource, List<Framily> entries) {
            super(context, resource, entries);
            this.resourceLayout = resource;
            this.mContext = context;
            this.framilies = entries;
        }

        public Framily getItem(int position){
            return framilies.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = getLayoutInflater().inflate(R.layout.entry_view,null);

            Framily fram = framilies.get(position);




            return v;
        }
    }
}