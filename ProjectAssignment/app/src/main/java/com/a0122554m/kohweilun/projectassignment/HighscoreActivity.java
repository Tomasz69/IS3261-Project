package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HighscoreActivity extends Activity {
    public static final String HIGHSCORE_PREFS = "highscore_state";
    TextView tv_highscore;
    TextView tv_currentScore;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        tv_highscore = (TextView)findViewById(R.id.highscore);
        tv_currentScore = (TextView)findViewById(R.id.currentScore);

        String lesson = getIntent().getStringExtra("lesson");
        int max_score = getIntent().getIntExtra("max_score", 0);
        int current_score = getIntent().getIntExtra("current_score", 0);

        sharedPreferences = getSharedPreferences(HIGHSCORE_PREFS, Context.MODE_PRIVATE);

        updateHighscore(lesson, current_score, max_score);
        tv_currentScore.setText("Your current score is " + current_score + " !");
    }

    private void updateHighscore(String lesson, int current_score, int max_score) {
        int high_score = sharedPreferences.getInt(lesson + "_highscore", 0);

        if (current_score > high_score) {
            tv_highscore.setText("NEW HIGHSCORE: \n" + "" + current_score + " / " + max_score);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(lesson + "_highscore", current_score);
            editor.commit();
        } else {
            tv_highscore.setText("HIGHSCORE: \n" + "" + high_score + " / " + max_score);
        }
    }

    public void onClick_GoBackToRevisionListActivity(View view) {
        Intent intent = new Intent(this, RevisionListActivity.class);
        startActivity(intent);
    }
}
