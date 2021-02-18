package com.example.rememberme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class MemoriesAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Integer> memories;

    public MemoriesAdapter(Context context, ArrayList<Integer> memories) {
        this.mContext = context;
        this.memories = memories;
    }

    @Override
    public int getCount() {
        return memories.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.grid_data, null);

        ImageView imageView = view.findViewById(R.id.images);
        imageView.setImageResource(memories.get(position));

        return view;
    }
}
