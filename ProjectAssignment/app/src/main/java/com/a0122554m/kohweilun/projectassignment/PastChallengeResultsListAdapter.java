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

public class PastChallengeResultsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] challenge_codes;
    private final String[] challenge_results;

    public PastChallengeResultsListAdapter(Activity context, String[] _challenge_codes, String[] _challenge_results) {
        super(context, R.layout.useful_links_single_link, _challenge_codes);

        this.context = context;
        this.challenge_codes = _challenge_codes;
        this.challenge_results = _challenge_results;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.past_challenge_single_result, null, true);

        TextView challengeCodeTV = (TextView) rowView.findViewById(R.id.challenge_code);
        TextView challengeResultTV= (TextView) rowView.findViewById(R.id.challenge_result);

        challengeCodeTV.setText(challenge_codes[position]);
        challengeResultTV.setText(challenge_results[position]);
        return rowView;
    }
}
