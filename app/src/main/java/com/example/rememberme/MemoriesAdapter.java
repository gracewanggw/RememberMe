package com.example.rememberme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MemoriesAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Memory> memories;
    TextView title;

    public MemoriesAdapter(Context context, ArrayList<Memory> memories) {
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
        title = view.findViewById(R.id.memory_title);
        title.setText(memories.get(position).getTitle());
        if (memories.get(position).getImage() != null) {
            Bitmap bmp= BitmapFactory.decodeByteArray(memories.get(position).getImage(), 0 , memories.get(position).getImage().length);
           // Bitmap rotatedBmp = ImageRotation.rotateImage(bmp, 90);
           // imageView.setImageBitmap(rotatedBmp);
            imageView.setImageBitmap(bmp);
        }
        else {
            imageView.setImageBitmap(null);
            imageView.setImageResource(R.drawable.memory);
        }

        return view;
    }
}
