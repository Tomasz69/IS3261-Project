package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;

public class RevisionListActivity extends Activity {
    public static final String HIGHSCORE_PREFS = "progress_state";
    SharedPreferences sharedPreferences;

    private String[] filesList = {
            "lesson01_introduction.pdf",
            "lesson02_android_intro.pdf",
            "lesson03_sqlite.pdf",
            "lesson04_shared_preferences.pdf",
            "lesson05_activity_fragment.pdf",
            "lesson06_broadcast_receiver_and_battery.pdf",
            "lesson07_dangerous_permissions.pdf",
            "lesson08_android_sensors_and_location_v3.pdf",
            "lesson09_internet.pdf",
            "lesson10_location_and_map.pdf",
            "lesson11_qr_codes.pdf"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.revision_list);
        this.setTitle(R.string.revision_title);

        sharedPreferences = getSharedPreferences(HIGHSCORE_PREFS, Context.MODE_PRIVATE);

        String[] titlesList = {
                getResources().getString(R.string.lesson_button1),
                getResources().getString(R.string.lesson_button2),
                getResources().getString(R.string.lesson_button3),
                getResources().getString(R.string.lesson_button4),
                getResources().getString(R.string.lesson_button5),
                getResources().getString(R.string.lesson_button6),
                getResources().getString(R.string.lesson_button7),
                getResources().getString(R.string.lesson_button8),
                getResources().getString(R.string.lesson_button9),
                getResources().getString(R.string.lesson_button10),
                getResources().getString(R.string.lesson_button11)
        };
        Button[] buttons= {
                findViewById(R.id.lessonButton1),
                findViewById(R.id.lessonButton2),
                findViewById(R.id.lessonButton3),
                findViewById(R.id.lessonButton4),
                findViewById(R.id.lessonButton5),
                findViewById(R.id.lessonButton6),
                findViewById(R.id.lessonButton7),
                findViewById(R.id.lessonButton8),
                findViewById(R.id.lessonButton9),
                findViewById(R.id.lessonButton10),
                findViewById(R.id.lessonButton11)
        };

        int i;
        for (i = 0; i < filesList.length; i++){
            Button button = buttons[i];
            String title = titlesList[i];
            int high_score = sharedPreferences.getInt(filesList[i] + "_highscore", 0);
            button.setText(Html.fromHtml(title + "<br/><small>Highscore : " + high_score + "</small>"));
            final Intent intent = new Intent(this, BothTypesQuestionActivity.class);

            intent.putExtra("fileName", filesList[i]);
            intent.putExtra("title", titlesList[i]);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
