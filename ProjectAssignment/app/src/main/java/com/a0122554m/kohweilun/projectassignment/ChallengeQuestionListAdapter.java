package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by JR on 28/10/17.
 */

public class ChallengeQuestionListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] question_nums;
    private final String[] more_details;

    public ChallengeQuestionListAdapter(Activity context, String[] question_nums, int[] question_types) {
        super(context, R.layout.challenge_quiz_single_question, question_nums);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.question_nums = question_nums;
        this.more_details = new String[question_types.length];

        for (int i = 0; i < question_types.length; i++) {
            switch (question_types[i]) {
                case (0):
                    more_details[i] = "Normal";
                    break;
                case (1):
                    more_details[i] = "Beacon";
                    break;
                case (2):
                    more_details[i] = "Beacon with QR code";
                    break;
                case (3):
                    more_details[i] = "GPS location";
                    break;
                case (4):
                    more_details[i] = "GPS location with QR code";
                    break;
            }
        }
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.challenge_quiz_single_question, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.question_num);
        TextView txtDetails = (TextView) rowView.findViewById(R.id.more_details);

        txtTitle.setText("Question " + question_nums[position]);
        txtDetails.setText("Type:  " + more_details[position]);
        return rowView;
    }
}
