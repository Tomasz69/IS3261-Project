package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class HighscoreActivity extends Activity {
//    public static final HIGHSCORE_PREFS = "highscore_state";
    TextView highscore;
    TextView current_score;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        highscore = (TextView)findViewById(R.id.highscore);
        current_score = (TextView)findViewById(R.id.currentScore);

        String lesson = getIntent().getStringExtra("lesson");

        sharedPreferences = getSharedPreferences(lesson + "_highscore", Context.MODE_PRIVATE);

//        current_score.setText();
    }
}
