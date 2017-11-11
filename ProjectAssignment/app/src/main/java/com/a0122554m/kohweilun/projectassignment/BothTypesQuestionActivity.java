package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

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

    private ProgressBar progressBarCircle;
    private TextView textViewTime;
    private CountDownTimer countDownTimer;
    private long timeCountInMilliSeconds = 11 * 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.both_types_question);

        scoreView = (TextView) findViewById(R.id.score);
        scoreTextView = (TextView) findViewById(R.id.score_text);
        questionView = (TextView) findViewById(R.id.question);
        buttonChoice1 = (Button) findViewById(R.id.choice1);
        buttonChoice2 = (Button) findViewById(R.id.choice2);
        buttonChoice3 = (Button) findViewById(R.id.choice3);
        buttonChoice4 = (Button) findViewById(R.id.choice4);

        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        textViewTime = (TextView) findViewById(R.id.textViewTime);

        challengeQuiz = getIntent().getBooleanExtra("challenge", false);

        if (challengeQuiz) {
            this.setTitle(R.string.challenge_title);
            QUESTIONID = getIntent().getStringExtra("id");
            QUESTION = getIntent().getStringExtra("question");
            ANSWERS = getIntent().getStringArrayExtra("answers");
            CORRECT = getIntent().getStringExtra("correct");
            CHALLENGE_CODE = getIntent().getStringExtra("code");
            scoreTextView.setText("Question:");
            scoreView.setText("");
            updateQuestion();
        } else {
            this.setTitle(R.string.revision_title);
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

        buttonChoice1.setBackgroundResource(R.color.light_grey);
        buttonChoice2.setBackgroundResource(R.color.light_grey);
        buttonChoice3.setBackgroundResource(R.color.light_grey);
        buttonChoice4.setBackgroundResource(R.color.light_grey);

        if (challengeQuiz) {
            questionView.setText(QUESTION);
            buttonChoice1.setText(ANSWERS[randomOptions.get(0)]);
            buttonChoice2.setText(ANSWERS[randomOptions.get(1)]);
            buttonChoice3.setText(ANSWERS[randomOptions.get(2)]);
            buttonChoice4.setText(ANSWERS[randomOptions.get(3)]);
        } else {
            if (questionNumber < revisionQuestionBank.getNumQuestions()) {
                // call to initialize the progress bar values
                setProgressBarValues();
                // call to start the count down timer
                startCountDownTimer();

                questionView.setText(revisionQuestionBank.getQuestion(randomQuestions.get(questionNumber)));
                buttonChoice1.setText(revisionQuestionBank.getChoice(randomQuestions.get(questionNumber), randomOptions.get(0)));
                buttonChoice2.setText(revisionQuestionBank.getChoice(randomQuestions.get(questionNumber), randomOptions.get(1)));
                buttonChoice3.setText(revisionQuestionBank.getChoice(randomQuestions.get(questionNumber), randomOptions.get(2)));
                buttonChoice4.setText(revisionQuestionBank.getChoice(randomQuestions.get(questionNumber), randomOptions.get(3)));
                answer = revisionQuestionBank.getCorrectAnswer(randomQuestions.get(questionNumber));
                questionNumber++;
            } else {
                stopCountDownTimer();
                Toast.makeText(this, getResources().getString(R.string.both_type_revision_complete_question), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, RevisionHighscoreActivity.class);
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
        // call to start the count down timer
        stopCountDownTimer();

        Button chosenChoice = (Button) view;
        if (challengeQuiz) {
            final String CHALLENGE_PREFS = "challenge_state";
            SharedPreferences challengePreferences = getSharedPreferences(CHALLENGE_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = challengePreferences.edit();
            editor.putBoolean(CHALLENGE_CODE + QUESTIONID + "DONE", true);
            editor.commit();
            int currentScore = challengePreferences.getInt(CHALLENGE_CODE + "SCORE", 0);
            System.out.println("Current score before marking question: " + currentScore);
            if (chosenChoice.getText().equals(CORRECT)) {
                view.setBackgroundResource(R.color.green);
                Toast.makeText(this, getResources().getString(R.string.question_correct), Toast.LENGTH_SHORT).show();
                currentScore++;
                editor.putInt(CHALLENGE_CODE + "SCORE", currentScore);
                editor.commit();
                System.out.println("Current score after marking question correct: " + currentScore);
            } else {
                view.setBackgroundColor(Color.RED);
                Toast.makeText(this, getResources().getString(R.string.question_wrong), Toast.LENGTH_SHORT).show();
                System.out.println("Current score after marking question wrong: " + currentScore);
            }
//            Intent challengeQuizList = new Intent(this, ChallengeQuizQuestionListActivity.class);
//            challengeQuizList.putExtra("challengeQuizCode", CHALLENGE_CODE);
//            startActivity(challengeQuizList);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after specified wait time
                    finish();
                }
            }, 2200);
        } else {
            if (chosenChoice.getText().equals(answer)) {
                view.setBackgroundResource(R.color.green);
                score++;
                Toast.makeText(this, getResources().getString(R.string.question_correct), Toast.LENGTH_SHORT).show();
            } else {
                view.setBackgroundColor(Color.RED);
                Toast.makeText(this, getResources().getString(R.string.question_wrong), Toast.LENGTH_SHORT).show();
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after specified wait time
                    updateScore(score);
                    updateQuestion();
                }
            }, 2200);
        }
    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                stopCountDownTimer();
                Toast.makeText(BothTypesQuestionActivity.this, "Time's up! Skipping to next question..", Toast.LENGTH_SHORT).show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        updateScore(score);
                        updateQuestion();
                    }
                }, 2200);
            }

        }.start();
    }

    /**
     * method to stop count down timer
     */
    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }

    /**
     * method to set circular progress bar values
     */
    private void setProgressBarValues() {
        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }

    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {
        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;
    }
}
