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

    public ChallengeQuestionListAdapter(Activity context, String[] question_nums) {
        super(context, R.layout.question_in_list, question_nums);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.question_nums = question_nums;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.question_in_list, null, true);

        TextView txtTitle = rowView.findViewById(R.id.question_num);
        txtTitle.setText("Question " + question_nums[position]);
        return rowView;


    }
}
