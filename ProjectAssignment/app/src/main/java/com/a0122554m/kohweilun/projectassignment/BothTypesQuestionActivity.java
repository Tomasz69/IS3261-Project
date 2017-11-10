package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class BothTypesQuestionActivity extends Activity {
    private RevisionQuestionBank revisionQuestionBank = new RevisionQuestionBank();

    private Button buttonChoice1;
    private Button buttonChoice2;
    private Button buttonChoice3;
    private Button buttonChoice4;
    private TextView scoreView;
    private TextView scoreTextView;
    private TextView questionView;

    //challenge quiz
    private boolean challengeQuiz; //new
    private String QUESTIONID;
    private String QUESTION;
    private String[] ANSWERS;
    private String CORRECT;
    private String CHALLENGE_CODE;

    //revision quiz
    private String lesson;
    private String fileName; //new
    private int max_score;
    private int score = 0;
    private int questionNumber = 0;
    private String answer;

    private ArrayList<Integer> randomQuestions = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_both_types_question);

        scoreView = (TextView)findViewById(R.id.score);
        scoreTextView = (TextView)findViewById(R.id.score_text);
        questionView = (TextView)findViewById(R.id.question);
        buttonChoice1 = (Button)findViewById(R.id.choice1);
        buttonChoice2 = (Button)findViewById(R.id.choice2);
        buttonChoice3 = (Button)findViewById(R.id.choice3);
        buttonChoice4 = (Button)findViewById(R.id.choice4);

        challengeQuiz = getIntent().getBooleanExtra("challenge", false);

        if (challengeQuiz) {
            QUESTIONID = getIntent().getStringExtra("id");
            QUESTION = getIntent().getStringExtra("question");
            ANSWERS = getIntent().getStringArrayExtra("answers");
            CORRECT = getIntent().getStringExtra("correct");
            CHALLENGE_CODE = getIntent().getStringExtra("code");
            scoreTextView.setText("Question:");
            scoreView.setText("");
            updateQuestion();
        } else {
            lesson = getIntent().getStringExtra("title");
            fileName = getIntent().getStringExtra("fileName");
            revisionQuestionBank.initQuestions(getApplicationContext(), lesson);

            max_score = revisionQuestionBank.getNumQuestions();

            for (int i = 0; i < max_score; i++) {
                randomQuestions.add(i);
            }
            Collections.shuffle(randomQuestions);

            updateQuestion();
            updateScore(score);
        }
    }

    private void updateQuestion() {
        ArrayList<Integer> randomOptions = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            randomOptions.add(i);
        }
        Collections.shuffle(randomOptions);

        if (challengeQuiz) {
            questionView.setText(QUESTION);
            buttonChoice1.setText(ANSWERS[randomOptions.get(0)]);
            buttonChoice2.setText(ANSWERS[randomOptions.get(1)]);
            buttonChoice3.setText(ANSWERS[randomOptions.get(2)]);
            buttonChoice4.setText(ANSWERS[randomOptions.get(3)]);
        } else {
            if (questionNumber < revisionQuestionBank.getNumQuestions()) {
                questionView.setText(revisionQuestionBank.getQuestion(randomQuestions.get(questionNumber)));
                buttonChoice1.setText(revisionQuestionBank.getChoice(randomQuestions.get(questionNumber), randomOptions.get(0)));
                buttonChoice2.setText(revisionQuestionBank.getChoice(randomQuestions.get(questionNumber), randomOptions.get(1)));
                buttonChoice3.setText(revisionQuestionBank.getChoice(randomQuestions.get(questionNumber), randomOptions.get(2)));
                buttonChoice4.setText(revisionQuestionBank.getChoice(randomQuestions.get(questionNumber), randomOptions.get(3)));
                answer = revisionQuestionBank.getCorrectAnswer(randomQuestions.get(questionNumber));
                questionNumber++;
            } else {
                Toast.makeText(this, "You have successfully completed this quiz!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, HighscoreActivity.class);
                intent.putExtra("lesson", lesson);
                intent.putExtra("fileName", fileName); //new
                intent.putExtra("max_score", max_score);
                intent.putExtra("current_score", score);

                finish();
                startActivity(intent);
            }
        }
    }

    private void updateScore(int score) {
        scoreView.setText("" + score + " / " + revisionQuestionBank.getNumQuestions());
    }

    public void onClick_SubmitAnswer(View view) {
        Button chosenChoice = (Button)view;
        if (challengeQuiz) {
            final String CHALLENGE_PREFS = "challenge_state";
            SharedPreferences challengePreferences = getSharedPreferences(CHALLENGE_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = challengePreferences.edit();
            editor.putBoolean(CHALLENGE_CODE + QUESTIONID + "DONE", true);
            editor.commit();
            int currentScore = challengePreferences.getInt(CHALLENGE_CODE + "SCORE", 0);
            System.out.println("Current score before marking question: " + currentScore);
            if (chosenChoice.getText().equals(CORRECT)) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                currentScore++;
                editor.putInt(CHALLENGE_CODE + "SCORE", currentScore);
                editor.commit();
                System.out.println("Current score after marking question correct: " + currentScore);
            } else {
                Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
                System.out.println("Current score after marking question wrong: " + currentScore);
            }
//            Intent challengeQuizList = new Intent(this, ChallengeQuizQuestionListActivity.class);
//            challengeQuizList.putExtra("challengeQuizCode", CHALLENGE_CODE);
//            startActivity(challengeQuizList);
            finish();
        } else {
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
}
