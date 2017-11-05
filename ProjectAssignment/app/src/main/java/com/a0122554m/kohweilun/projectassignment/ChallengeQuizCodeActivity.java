package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ChallengeQuizCodeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_quiz_code);

    }

    public void onClick_GoToChallengeQuizList(View view){
        Intent intent = new Intent(this, ChallengeQuizQuestionListActivity.class);
        EditText challengeCodeField = findViewById(R.id.challengeCodeText);
        String challengeCode = challengeCodeField.getText().toString();
        intent.putExtra("challengeQuizCode", challengeCode );
        System.out.println("Challenge Code in Code: " + challengeCode);
        startActivity(intent);
    }
}
