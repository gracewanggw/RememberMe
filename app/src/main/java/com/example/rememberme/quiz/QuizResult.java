package com.example.rememberme.quiz;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

//Dialog to show the result from a quiz after done
public class QuizResult extends DialogFragment implements DialogInterface.OnClickListener {

    public static final String DIALOG_KEY = "dialog key";
    public static final String UNIT_KEY = "u";
    public static final String CAMERA_KEY = "camera code";
    public static final String SETTINGS_COMMENT_KEY = "sc";

    public static int MODE = Context.MODE_PRIVATE;
    public SharedPreferences preferences;
    public static final String PREFERENCE_NAME = "saved info";

    private int score;
    private EditText scoreDisplay;

    public Dialog onCreateDialog(Bundle savedInstanceState){

        Dialog dialog = null;
        View dialogView;
        Bundle bundle = getArguments();
        id = bundle.getInt(DIALOG_KEY);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());


        if(id == COMMENT_DIALOG) {
            //returns a view that is defined in xml
            dialogView = getActivity().getLayoutInflater().inflate(R.layout.comment_dialog, null);

            settingsComments = dialogView.findViewById(R.id.comment_settings);

            settingsComments.setText(preferences.getString(SETTINGS_COMMENT_KEY,""));

            //builder.setView(dialogView)
            AlertDialog.Builder commentBuilder = new AlertDialog.Builder(getActivity());
            commentBuilder.setView(dialogView);

            commentBuilder.setPositiveButton("OK", this);
            commentBuilder.setNegativeButton("Cancel", this);
            //
            dialog = commentBuilder.create();
        }else if(id == UNIT_DIALOG){
            //returns a view that is defined in xml
            dialogView = getActivity().getLayoutInflater().inflate(R.layout.unit_dialog, null);

            unitId = preferences.getInt(UNIT_KEY,-1);

            prefUnits = dialogView.findViewById(R.id.unitOptions);

            RadioButton toSet;

            if(unitId != -1) {
                if(unitId == 1) {
                    toSet = dialogView.findViewById(R.id.imperial_btn);
                }else{
                    toSet = dialogView.findViewById(R.id.metric_button);
                }
                toSet.setChecked(true);
            }

            //builder.setView(dialogView)
            AlertDialog.Builder unitBuilder = new AlertDialog.Builder(getActivity());
            unitBuilder.setView(dialogView);

            unitBuilder.setPositiveButton("OK", this);
            unitBuilder.setNegativeButton("Cancel", this);
            //
            dialog = unitBuilder.create();

        }
        else if(id == IMG_DIALOG){
            //returns a view that is defined in xml
            dialogView = getActivity().getLayoutInflater().inflate(R.layout.image_source, null);

            //builder.setView(dialogView)
            AlertDialog.Builder imgBuilder = new AlertDialog.Builder(getActivity());
            imgBuilder.setView(dialogView);

            // add the buttons
            imgBuilder.setPositiveButton("Take New Picture", this);
            imgBuilder.setNeutralButton("Choose From Gallery", this);

            dialog = imgBuilder.create();

        }else{
            Log.d("dani", "dialog error");
        }

        return dialog;
    }

    public void onClick(DialogInterface dialogInterface, int item){

        if(item == DialogInterface.BUTTON_POSITIVE){

            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            if(id == COMMENT_DIALOG){

                editor.putString(SETTINGS_COMMENT_KEY, settingsComments.getText().toString());
            }else if(id == UNIT_DIALOG) {
                int unitsId = prefUnits.getCheckedRadioButtonId();
                if(unitsId == R.id.imperial_btn) {
                    editor.putInt(UNIT_KEY, 1);
                }else{
                    editor.putInt(UNIT_KEY, 0);
                }

            }else if(id == IMG_DIALOG) {
                camlist.onCameraClick();
            }
            editor.apply();

        }  else if (item == DialogInterface.BUTTON_NEUTRAL){
            if(id == IMG_DIALOG){
                galllist.onGalleryClick();
            }
        }

    }



}






