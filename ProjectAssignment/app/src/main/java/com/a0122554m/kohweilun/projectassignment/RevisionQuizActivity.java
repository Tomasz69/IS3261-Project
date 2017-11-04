package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RevisionQuizActivity extends Activity {
    private RevisionQuestionBank revisionQuestionBank = new RevisionQuestionBank();

    private TextView scoreView;
    private TextView questionView;
    private Button buttonChoice1;
    private Button buttonChoice2;
    private Button buttonChoice3;
    private Button buttonChoice4;
    private String answer;

    private String lesson;
    private int max_score;
    private int score = 0;
    private int questionNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision_quiz);

        scoreView = (TextView)findViewById(R.id.score);
        questionView = (TextView)findViewById(R.id.question);
        buttonChoice1 = (Button)findViewById(R.id.choice1);
        buttonChoice2 = (Button)findViewById(R.id.choice2);
        buttonChoice3 = (Button)findViewById(R.id.choice3);
        buttonChoice4 = (Button)findViewById(R.id.choice4);

        lesson = getIntent().getStringExtra("title");

        revisionQuestionBank.initQuestions(getApplicationContext(), lesson);

        max_score = revisionQuestionBank.getNumQuestions();

        updateQuestion();
        updateScore(score);
    }

    private void updateQuestion() {
        if (questionNumber < revisionQuestionBank.getNumQuestions()) {
            questionView.setText(revisionQuestionBank.getQuestion(questionNumber));
            buttonChoice1.setText(revisionQuestionBank.getChoice(questionNumber, 0));
            buttonChoice2.setText(revisionQuestionBank.getChoice(questionNumber, 1));
            buttonChoice3.setText(revisionQuestionBank.getChoice(questionNumber, 2));
            buttonChoice4.setText(revisionQuestionBank.getChoice(questionNumber, 3));
            answer = revisionQuestionBank.getCorrectAnswer(questionNumber);
            questionNumber++;
        } else {
            Toast.makeText(this, "You have successfully completed this quiz!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, HighscoreActivity.class);
            intent.putExtra("lesson", lesson);
            intent.putExtra("max_score", max_score);
            intent.putExtra("current_score", score);

            startActivity(intent);
        }
    }

    private void updateScore(int score) {
        scoreView.setText("" + score + " / " + revisionQuestionBank.getNumQuestions());
    }

    public void onClick_SubmitAnswer(View view) {
        Button chosenChoice = (Button)view;

        if (chosenChoice.getText().equals(answer)) {
            score++;
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
        }

        updateScore(score);
        updateQuestion();
    }
}
