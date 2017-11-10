package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ChallengeQuizCodeActivity extends Activity {

    private String[] question_nums;
    private int[] question_ids; //same as location hint ids
    private String[] question_titles;
    private String[] question_answers;
    private String[] question_corrects;
    private int[] question_types; //0 - normal; 1 - beacon; 2 - beacon with QR; 3 - gps; 4 - gps with QR
    private String challengeCode;
    private static final String FB_SHAREDPREF_FOR_APP = "FbSharedPrefForApp";
    private SharedPreferences appPreferences;
    private int user_id;
    TextView quizStatusUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challenge_quiz_code);
        this.setTitle(R.string.challenge_title);
        appPreferences = getSharedPreferences(FB_SHAREDPREF_FOR_APP, MODE_PRIVATE);
        user_id = appPreferences.getInt("user_id",0);
        quizStatusUpdate = findViewById(R.id.codeTextView);
    }

    public void onClick_GoToChallengeQuizList(View view) {
        EditText challengeCodeField = findViewById(R.id.challengeCodeText);
        challengeCode = challengeCodeField.getText().toString();
        GetQuestionsAsyncTask getQuestionsAsyncTask = new GetQuestionsAsyncTask();
        getQuestionsAsyncTask.execute("http://192.168.137.1:3000/api/Questions/GetAllQuestionsByCode?_question_code="
                + challengeCode + "&user_id=" + user_id);
    }

    private void GoToList() {
        Intent intent = new Intent(this, ChallengeQuizQuestionListActivity.class);
        intent.putExtra("question_nums", question_nums);
        intent.putExtra("question_ids", question_ids);
        intent.putExtra("question_titles", question_titles);
        intent.putExtra("question_answers", question_answers);
        intent.putExtra("question_corrects", question_corrects);
        intent.putExtra("question_types", question_types);
        intent.putExtra("challengeQuizCode", challengeCode);
        System.out.println("Challenge Code in Code: " + challengeCode);
        startActivity(intent);
    }

    private class GetQuestionsAsyncTask extends AsyncTask<String, Void, String> {

        public String doInBackground(String... str) {
            URL url = convertToUrl(str[0]);
            HttpURLConnection httpURLConnection = null;
            int responseCode;
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                responseCode = httpURLConnection.getResponseCode();
                if (responseCode == httpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    inputStream.close();
                }
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
                e.printStackTrace();
            } finally {
                assert httpURLConnection != null;
                httpURLConnection.disconnect();
            }
            return stringBuilder.toString();
        }

        public void onPostExecute(String result) {
            try {
                JSONArray listOfQuestions = new JSONArray(result);
                int numOfQuestions = listOfQuestions.length();
                question_nums = new String[numOfQuestions];
                question_ids = new int[numOfQuestions];
                question_titles = new String[numOfQuestions];
                question_answers = new String[numOfQuestions];
                question_corrects = new String[numOfQuestions];
                question_types = new int[numOfQuestions];

                for (int i = 0; i < listOfQuestions.length(); i++) {
                    final JSONObject questionDetails = listOfQuestions.getJSONObject(i);
                    question_nums[i] = i + 1 + "";
                    question_ids[i] = questionDetails.optInt("id_question");
                    question_titles[i] = questionDetails.optString("question_title");
                    question_answers[i] = questionDetails.optString("question_answers");
                    question_corrects[i] = questionDetails.optString("question_correct_answer");
                    question_types[i] = questionDetails.optInt("question_type");
                }
                final String CHALLENGE_PREFS = "challenge_state";
                SharedPreferences challengePreferences = getSharedPreferences(CHALLENGE_PREFS, MODE_PRIVATE);
                int currentScore = challengePreferences.getInt(challengeCode + "SCORE", 0);
                System.out.println("Current score upon joining or rejoining quiz: " + currentScore);
                if (currentScore == 0) {
                    SharedPreferences.Editor editor = challengePreferences.edit();
                    editor.putInt(challengeCode + "SCORE", 0);
                    editor.commit();
                }
                GoToList();
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
                e.printStackTrace();
                quizStatusUpdate.setText(getResources().getString(R.string.challenge_code_error));
            }
        }
    }

    // method from internet to handle url stuff
    private URL convertToUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(),
                    url.getHost(), url.getPort(), url.getPath(),
                    url.getQuery(), url.getRef());
            url = uri.toURL();
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
