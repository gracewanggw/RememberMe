package com.example.rememberme.quiz;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.rememberme.R;

import java.util.ArrayList;

public class QuestionAdapter extends ArrayAdapter{ // extends ArrayAdapter<Question> {
    private Context mContext;
    private ArrayList<ArrayList<String>> questions;
    private ViewHolder holder = null;
    public ArrayList<Boolean> mChecked = new ArrayList<Boolean>();


    public QuestionAdapter(Context context, ArrayList<ArrayList<String>> items) {
        super(context, R.layout.ques_result, items);
        mContext = context;
        questions = items;
        for(int k = 0; k < items.size(); k++){
            mChecked.add(false);
        }
    }


    private class ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        CheckBox checked;
        boolean ischecked;
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
            mChecked.set(position, true);
        }

        holder.textView1.setText(text1);
        holder.textView2.setText(text2);
        holder.textView3.setText(text3);
        holder.checked.setTag(Integer.valueOf(position)); // set the tag so we can identify the correct row in the listener
        holder.checked.setChecked(mChecked.get(position));
        holder.checked.setOnCheckedChangeListener(mListener); // set the listener

        return convertView;
    }

    CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mChecked.set((Integer)buttonView.getTag(), isChecked); // get the tag so we know the row and store the status
        }
    };
}