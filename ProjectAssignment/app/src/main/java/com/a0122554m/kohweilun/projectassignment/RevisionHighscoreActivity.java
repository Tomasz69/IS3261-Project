package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RevisionHighscoreActivity extends Activity {
    public static final String HIGHSCORE_PREFS = "progress_state";//changed to progress state..use same one as lessons
    TextView tv_highscore;
    TextView tv_currentScore;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.revision_highscore);
        this.setTitle(R.string.revision_title);

        tv_highscore = (TextView)findViewById(R.id.highscore);
        tv_currentScore = (TextView)findViewById(R.id.currentScore);

        String lesson = getIntent().getStringExtra("lesson");
        String fileName = getIntent().getStringExtra("fileName");//new
        int max_score = getIntent().getIntExtra("max_score", 0);
        int current_score = getIntent().getIntExtra("current_score", 0);

        sharedPreferences = getSharedPreferences(HIGHSCORE_PREFS, Context.MODE_PRIVATE);

        updateHighscore(fileName, current_score, max_score);
        tv_currentScore.setText(getResources().getString(R.string.rev_current_score) + " " + current_score + " !");
    }

    private void updateHighscore(String fileName, int current_score, int max_score) {
        int high_score = sharedPreferences.getInt(fileName + "_highscore", 0);//changed to fileName

        if (current_score > high_score) {
            tv_highscore.setText(getResources().getString(R.string.rev_new_high_score) + "\n" + "" + current_score + " / " + max_score);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(fileName + "_highscore", current_score); //changed to fileName
            editor.commit();
        } else {
            tv_highscore.setText(getResources().getString(R.string.rev_high_score) + "\n" + "" + high_score + " / " + max_score);
        }
    }

    public void onClick_GoBackToRevisionListActivity(View view) {
        Intent intent = new Intent(this, RevisionListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        finish();
        startActivity(intent);
    }
}
