package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_quiz_question_list);
        GetQuestionsAsyncTask getQuestionsAsyncTask = new GetQuestionsAsyncTask();
        String challengeQuizCode = getIntent().getStringExtra("challengeQuizCode");
        System.out.println("Challenge Code in List: " + challengeQuizCode);
        getQuestionsAsyncTask.execute("http://192.168.137.1:3000/api/Questions/GetAllQuestionsByCode?_question_code=" + challengeQuizCode);
    }

    private void setUpList(){
        ChallengeQuestionListAdapter adapter = new ChallengeQuestionListAdapter(this, question_nums);
        ListView list = findViewById(R.id.questions_list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent beaconIntent = new Intent(getApplicationContext(), ChallengeQuizHintActivity.class);
                beaconIntent.putExtra("id", question_ids[position]);
                beaconIntent.putExtra("title", question_titles[position]);
                beaconIntent.putExtra("answers", question_answers[position]);
                beaconIntent.putExtra("correct", question_corrects[position]);
                beaconIntent.putExtra("type", question_types[position]);
                startActivity(beaconIntent);
            }
        });
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
                    question_nums[i] = i+1 + "";
                    question_ids[i] = questionDetails.optInt("id_question");
                    question_titles[i] = questionDetails.optString("question_title");
                    question_answers[i] = questionDetails.optString("question_answers");
                    question_corrects[i] = questionDetails.optString("question_correct_answer");
                    question_types[i] = questionDetails.optInt("question_type");
                }
                setUpList();
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
}
