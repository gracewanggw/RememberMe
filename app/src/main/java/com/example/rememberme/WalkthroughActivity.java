package com.example.rememberme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rememberme.quiz.Quiz;

public class WalkthroughActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;
    ImageView page1;
    ImageView page2;
    ImageView page3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        page1 = findViewById(R.id.page1);
        page2 = findViewById(R.id.page2);
        page3 = findViewById(R.id.page3);
        Button start = findViewById(R.id.button_get_started);

        start.setOnClickListener(this);
        viewPager = findViewById(R.id.view_pager);
        SlideViewAdapter adapter = new SlideViewAdapter(this);
        viewPager.setAdapter(adapter);
        System.out.println("here0");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_get_started:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;

        }
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
                    imageView.setImageResource(R.drawable.start_graphic2);
                    page2.setImageResource(R.drawable.selected_slide);
                    page1.setImageResource(R.drawable.unselected_slide);
                    page3.setImageResource(R.drawable.unselected_slide);
                    title.setText("Welcome to RememberMe");
                    description.setText("An app made to help you remember your friends and family");
                    break;

                case 2:
                    imageView.setImageResource(R.drawable.start_graphic2);
                    page3.setImageResource(R.drawable.selected_slide);
                    page2.setImageResource(R.drawable.unselected_slide);
                    page1.setImageResource(R.drawable.unselected_slide);
                    title.setText("Welcome to RememberMe");
                    description.setText("An app made to help you remember your friends and family");
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