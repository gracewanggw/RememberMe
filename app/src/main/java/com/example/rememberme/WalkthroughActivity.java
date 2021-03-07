package com.example.rememberme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rememberme.quiz.Quiz;

public class WalkthroughActivity extends AppCompatActivity{

    ViewPager viewPager;
    ImageView page1;
    ImageView page2;
    ImageView page3;

    public final static String SHARED_PREFS = "sharedPrefs";
    public final static String OPENED_KEY = "opened";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(openedAlready()){
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }
        else{
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
            SharedPreferences.Editor editors = sharedPreferences.edit();
            editors.putBoolean(OPENED_KEY,true);
            editors.commit();
        }
        setContentView(R.layout.activity_walkthrough);

        page1 = findViewById(R.id.page1);
        page2 = findViewById(R.id.page2);
        page3 = findViewById(R.id.page3);
        Button start = findViewById(R.id.button_get_started);


        Context context = this;

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        viewPager = findViewById(R.id.view_pager);
        SlideViewAdapter adapter = new SlideViewAdapter(this);
        viewPager.setAdapter(adapter);
        Log.d("gwang", "here");

    }

    public boolean openedAlready(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        return sharedPreferences.getBoolean(OPENED_KEY,false);
    }

    private class SlideViewAdapter extends PagerAdapter {

        Context context;

        public SlideViewAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.slide_screen,container,false);

            ImageView imageView = view.findViewById(R.id.start_graphic);
            TextView title = view.findViewById(R.id.title);
            TextView description = view.findViewById(R.id.description);
            Button start = view.findViewById(R.id.button_get_started);
            Log.d("gwang", position + "position");
            switch(position){
                case 0:
                    imageView.setImageResource(R.drawable.start_graphic_1);
                    page1.setImageResource(R.drawable.selected_slide);
                    page2.setImageResource(R.drawable.unselected_slide);
                    page3.setImageResource(R.drawable.unselected_slide);
                    title.setText("Welcome to RememberMe");
                    description.setText("An app made to help you remember your friends and family");
                    break;

                case 1:
                    imageView.setImageResource(R.drawable.start_graphic_2);
                    page1.setImageResource(R.drawable.unselected_slide);
                    page2.setImageResource(R.drawable.selected_slide);
                    page3.setImageResource(R.drawable.unselected_slide);
                    title.setText("Quiz");
                    description.setText("You can quiz yourself on information about your friends and family");
                    break;

                case 2:
                    imageView.setImageResource(R.drawable.start_graphic_3);
                    page1.setImageResource(R.drawable.unselected_slide);
                    page2.setImageResource(R.drawable.unselected_slide);
                    page3.setImageResource(R.drawable.selected_slide);
                    title.setText("Memories");
                    description.setText("You can look through old memories you had with your friends and family");
                    break;

            }

            container.addView(view);
            return view;
        }


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object){
            container.removeView((View)object);
        }



    }
}