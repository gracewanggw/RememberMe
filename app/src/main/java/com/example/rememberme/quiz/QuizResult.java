package com.example.rememberme.quiz;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.rememberme.R;
import androidx.fragment.app.DialogFragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//Dialog to show the result from a quiz after done
public class QuizResult extends DialogFragment implements DialogInterface.OnClickListener {

    public static final int PERSON = 0;
    public static final int REVIEW = 1;
    public static final String QUIZ_KEY = "type of quiz";

//    public static int MODE = Context.MODE_PRIVATE;
//    public SharedPreferences preferences;
//    public static final String PREFERENCE_NAME = "saved info";

    private int type;
    private TextView correctCt;
    private TextView wrongCt;
    private TextView percent;

    public Dialog onCreateDialog(Bundle savedInstanceState){

        Dialog dialog = null;
        View dialogView;
        Bundle bundle = getArguments();
        type = bundle.getInt(QUIZ_KEY);
        //preferences = getActivity().getSharedPreferences("MySharedPref", MODE);

        //returns a view that is defined in xml
        dialogView = getActivity().getLayoutInflater().inflate(R.layout.result_dialog, null);

        correctCt = dialogView.findViewById(R.id.numCorrect);
        wrongCt = dialogView.findViewById(R.id.numWrong);
        percent = dialogView.findViewById(R.id.percentage);

        //builder.setView(dialogView)
        AlertDialog.Builder commentBuilder = new AlertDialog.Builder(getActivity());
        commentBuilder.setView(dialogView);

        commentBuilder.setPositiveButton("Done", this);
        //
        dialog = commentBuilder.create();
        return dialog;
    }

    public void onClick(DialogInterface dialogInterface, int item){

        if(item == DialogInterface.BUTTON_POSITIVE) {
            if(type == REVIEW) {
                //for wrong answer if not in wrong answers already
                //add to the set
                Toast.makeText(getActivity(), "add right answers", Toast.LENGTH_SHORT );
            }
            //fpr review quizes, remove the right answers from the review set
            if(type == REVIEW){
                Toast.makeText(getActivity(), "remove right answers", Toast.LENGTH_SHORT );
            }
        }else{
            //throw some error
        }

    }

}






