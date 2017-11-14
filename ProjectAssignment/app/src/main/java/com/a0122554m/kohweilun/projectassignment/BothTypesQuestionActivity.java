package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class BothTypesQuestionActivity extends Activity {
    private final long WAIT_DELAY = 2100;
    private RevisionQuestionBank revisionQuestionBank = new RevisionQuestionBank();

    private Button buttonChoice1;
    private Button buttonChoice2;
    private Button buttonChoice3;
    private Button buttonChoice4;
    private TextView scoreView;
    private TextView scoreTextView;
    private TextView questionText_tv;
    private TextView questionNum_tv;
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

        questionText_tv = (TextView) findViewById(R.id.question_text);
        questionNum_tv = (TextView) findViewById(R.id.question_number);
        scoreTextView = (TextView) findViewById(R.id.score_text);
        scoreView = (TextView) findViewById(R.id.score);
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
            progressBarCircle.setVisibility(View.INVISIBLE);
            textViewTime.setVisibility(View.INVISIBLE);
            questionText_tv.setText("");
            questionNum_tv.setText("Question:");
            questionNum_tv.setTextSize(22);
            scoreTextView.setText("");
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

    @Override
    protected void onDestroy() {
        if (!challengeQuiz) {
            stopCountDownTimer();
        }
        super.onDestroy();
    }

    private void updateQuestion() {
        ArrayList<Integer> randomOptions = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            randomOptions.add(i);
        }
        Collections.shuffle(randomOptions);

        buttonChoice1.setBackgroundResource(R.drawable.drawable_rectangle_round_both);
        buttonChoice2.setBackgroundResource(R.drawable.drawable_rectangle_round_both);
        buttonChoice3.setBackgroundResource(R.drawable.drawable_rectangle_round_both);
        buttonChoice4.setBackgroundResource(R.drawable.drawable_rectangle_round_both);

        if (challengeQuiz) {
            questionView.setText(QUESTION);
            final Button[] buttonChoices = {buttonChoice1, buttonChoice2, buttonChoice3, buttonChoice4};

            for (int i=0; i < buttonChoices.length; i++){
                final Button buttonChoice = buttonChoices[i];
                final String answer = ANSWERS[randomOptions.get(i)].split("-")[0];
                final String explanation = ANSWERS[randomOptions.get(i)].split("-")[1];
                buttonChoice.setText(answer);
                buttonChoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClick_SubmitAnswer(buttonChoice, answer, explanation);
                    }
                });
            }
        } else {
            if (questionNumber < revisionQuestionBank.getNumQuestions()) {
                // call to initialize the progress bar values
                setProgressBarValues();
                // call to start the count down timer
                startCountDownTimer();

                updateQuestionCount(questionNumber+1);
                questionView.setText(revisionQuestionBank.getQuestion(randomQuestions.get(questionNumber)));

                final Button[] buttonChoices = {buttonChoice1, buttonChoice2, buttonChoice3, buttonChoice4};
                answer = revisionQuestionBank.getCorrectAnswer(randomQuestions.get(questionNumber));
                for (int i=0; i < buttonChoices.length; i++){
                    final Button buttonChoice = buttonChoices[i];
                    final String chosenAnswer = revisionQuestionBank.getChoice(randomQuestions.get(questionNumber), i);
                    final String explanation = chosenAnswer.split("-")[1];
                    buttonChoice.setText(chosenAnswer.split("-")[0]);
                    buttonChoice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onClick_SubmitAnswer(buttonChoice, chosenAnswer.split("-")[0], explanation);
                        }
                    });
                }
                questionNumber++;
            } else {
                stopCountDownTimer();
                Toast.makeText(this, getResources().getString(R.string.both_type_revision_complete_question), Toast.LENGTH_LONG).show();

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
        scoreView.setText("" + score);
    }

    private void updateQuestionCount(int question) {
        questionNum_tv.setText("" + question + " / " + revisionQuestionBank.getNumQuestions());
    }

    public void onClick_SubmitAnswer(Button _button, String _answer, String _explanation) {
        if (challengeQuiz) {
            final String CHALLENGE_PREFS = "challenge_state";
            SharedPreferences challengePreferences = getSharedPreferences(CHALLENGE_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = challengePreferences.edit();
            editor.putBoolean(CHALLENGE_CODE + QUESTIONID + "DONE", true);
            editor.commit();
            int currentScore = challengePreferences.getInt(CHALLENGE_CODE + "SCORE", 0);
            System.out.println("Current score before marking question: " + currentScore);
            if (_answer.equals(CORRECT)) {
                _button.setBackgroundResource(R.drawable.drawable_rectangle_round_both_green);
                currentScore++;
                editor.putInt(CHALLENGE_CODE + "SCORE", currentScore);
                editor.commit();
                System.out.println("Current score after marking question correct: " + currentScore);

                showMyDialog(this, "Correct!\n\n" + _explanation);
            } else {
                _button.setBackgroundResource(R.drawable.drawable_rectangle_round_both_red);
                System.out.println("Current score after marking question wrong: " + currentScore);

                showMyDialog(this, "Wrong!\n\n" + _explanation);
            }
        } else {
            stopCountDownTimer();

            if (_answer.equals(answer)) {
                _button.setBackgroundResource(R.drawable.drawable_rectangle_round_both_green);
                score++;
                showMyDialog(this, "Correct!\n\n" + _explanation);
            } else {
                _button.setBackgroundResource(R.drawable.drawable_rectangle_round_both_red);
                showMyDialog(this, "Wrong!\n\n" + _explanation);
            }
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
                }, WAIT_DELAY);
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

    private void showMyDialog(Context context, String text) {
        final Dialog dialog = new Dialog(context) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                // Tap anywhere to close dialog.
                this.cancel();
                return true;
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.revision_quiz_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView textView = (TextView) dialog.findViewById(R.id.quiz_dialog);
        textView.setText(text);

        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (!challengeQuiz) {
                    updateScore(score);
                    updateQuestion();
                } else {
                    finish();
                }
            }
        });
    }
}
