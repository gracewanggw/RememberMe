package com.example.rememberme.quiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rememberme.R;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class QuestionAdapter  extends ArrayAdapter{ // extends ArrayAdapter<Question> {
    private Context mContext;
    private ArrayList<ArrayList<String>> questions;
    private ViewHolder holder = null;

    public QuestionAdapter(Context context, ArrayList<ArrayList<String>> items) {
        super(context, R.layout.ques_result, items);
        mContext = context;
        questions = items;
    }

    private class ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        CheckBox checked;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ques_result, null);
            holder.textView1 = (TextView) convertView.findViewById(R.id.ques);
            holder.textView2 = (TextView) convertView.findViewById(R.id.realans);
            holder.textView3 = (TextView) convertView.findViewById(R.id.yourans);
            holder.checked = (CheckBox) convertView.findViewById(R.id.checked);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String corr_ans = questions.get(position).get(1);
        String ans = questions.get(position).get(2);
        String text1 = "Q: " + questions.get(position).get(0);
        String text2 = "Correct Answer: "+ corr_ans;
        String text3 = "Your Answer: "+ ans;

        if(corr_ans.equals(ans)){
            holder.textView1.setBackgroundColor(Color.parseColor("#dafaca"));
            holder.textView2.setBackgroundColor(Color.parseColor("#dafaca"));
            holder.textView3.setBackgroundColor(Color.parseColor("#dafaca"));
            holder.checked.setBackgroundColor(Color.parseColor("#dafaca"));
        }else{
            holder.textView1.setBackgroundColor(Color.parseColor("#e5a3ba"));
            holder.textView2.setBackgroundColor(Color.parseColor("#e5a3ba"));
            holder.textView3.setBackgroundColor(Color.parseColor("#e5a3ba"));
            holder.checked.setBackgroundColor(Color.parseColor("#e5a3ba"));


        }
        holder.textView1.setText(text1);
        holder.textView2.setText(text2);
        holder.textView3.setText(text3);
        return convertView;
    }
}