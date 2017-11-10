package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ChallengeQuizQuestionListActivity extends Activity {

    private String[] question_nums;
    private int[] question_ids; //same as location hint ids
    private String[] question_titles;
    private String[] question_answers;
    private String[] question_corrects;
    private int[] question_types; //0 - normal; 1 - beacon; 2 - beacon with QR; 3 - gps; 4 - gps with QR
    private String challengeQuizCode;

    private static final String FB_SHAREDPREF_FOR_APP = "FbSharedPrefForApp";
    private SharedPreferences appPreferences;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challenge_quiz_question_list);
        appPreferences = getSharedPreferences(FB_SHAREDPREF_FOR_APP, MODE_PRIVATE);
        user_id = appPreferences.getInt("user_id",0);

        question_nums = getIntent().getStringArrayExtra("question_nums");
        question_ids = getIntent().getIntArrayExtra("question_ids");
        question_titles = getIntent().getStringArrayExtra("question_titles");
        question_answers = getIntent().getStringArrayExtra("question_answers");
        question_corrects = getIntent().getStringArrayExtra("question_corrects");
        question_types = getIntent().getIntArrayExtra("question_types");
        challengeQuizCode = getIntent().getStringExtra("challengeQuizCode");
        setUpList();
    }

    public void onClick_endParticipation(View view){
        EndParticipationAsyncTask endParticipationAsyncTask = new EndParticipationAsyncTask();
        endParticipationAsyncTask.execute("http://192.168.137.1:3000/api/Questions/endChallengeParticipation?" +
                "_question_code=" + challengeQuizCode + "&user_id=" + user_id);
    }

    private class EndParticipationAsyncTask extends AsyncTask<String, Void, String> {

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
                Boolean success = Boolean.valueOf(result);
                if (success) {
                    Toast.makeText(getApplicationContext(), "You have successfully ended your participation in this challenge.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Error! There is an error in ending your participation.", Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
                e.printStackTrace();
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

    private void setUpList(){
        ChallengeQuestionListAdapter adapter = new ChallengeQuestionListAdapter(this, question_nums);
        ListView list = findViewById(R.id.questions_list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent hintIntent = new Intent(getApplicationContext(), ChallengeQuizHintActivity.class);
                hintIntent.putExtra("id", question_ids[position]);
                hintIntent.putExtra("title", question_titles[position]);
                hintIntent.putExtra("answers", question_answers[position]);
                hintIntent.putExtra("correct", question_corrects[position]);
                hintIntent.putExtra("type", question_types[position]);
                hintIntent.putExtra("code", challengeQuizCode);
                startActivity(hintIntent);
            }
        });
    }
}
