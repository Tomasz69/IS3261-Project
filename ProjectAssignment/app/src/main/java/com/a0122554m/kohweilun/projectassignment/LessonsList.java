package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LessonsList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_list);

        Button button1 = findViewById(R.id.lessonButton);
        Intent intent1 = new Intent(this, PDFActivity.class);

        Button[] buttons = {button1};
        Intent[] intents = {intent1};

        int i;
        for (i = 0; i < buttons.length; i++){
            Button button = buttons[i];
            final Intent intent = intents[i];
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    startActivity(intent);
                }
            });
        }
    }
}
