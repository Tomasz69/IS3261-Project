package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class QuizWebAPIActivity extends Activity {

    private String[] question_titles;
    private String[] question_answers;
    private String[] question_corrects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_web_api);
        Act3AsyncTask  act3AsyncTask = new Act3AsyncTask();
        //act3AsyncTask.execute("http://192.168.137.1:3000/api/Questions/GetAllQuestionsByCode?_question_code=B34C0N");
        act3AsyncTask.execute("http://10.0.2.2:3000/api/Questions/GetAllQuestionsByCode?_question_code=B34C0N");
    }

    public void onClick_GoToQuestion(int question_num){
        Intent beaconIntent = new Intent(this, QuizBeaconActivity.class);
        beaconIntent.putExtra("title", question_titles[question_num]);
        beaconIntent.putExtra("answers", question_answers[question_num]);
        beaconIntent.putExtra("correct", question_corrects[question_num]);
        startActivity(beaconIntent);
    }

    private class Act3AsyncTask extends AsyncTask<String, Void, String> {

        public String doInBackground(String... str){
            URL url = convertToUrl(str[0]);
            HttpURLConnection httpURLConnection = null;
            int responseCode;
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                assert url != null;
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                responseCode = httpURLConnection.getResponseCode();
                if (responseCode == httpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    while ((line = reader.readLine())!=null) {
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
            Button sampleButton = findViewById(R.id.SampleQuestionButton);
            try {
                JSONArray listOfQuestions = new JSONArray(result);
                for (int i = 0; i < listOfQuestions.length(); i++) {
                    JSONObject questionDetails = listOfQuestions.getJSONObject(i);
                    question_titles[i] = questionDetails.getString("question_title");
                    question_answers[i] = questionDetails.getString("question_answers");
                    question_corrects[i] = questionDetails.getString("question_correct_answer");
                }

                String text = "";
                for (int i = 0; i < listOfQuestions.length(); i++) {
                    text += question_titles[i] + "\n" + question_answers[i] + "\n" + question_corrects[i] + "\n";
                }

                TextView textView = findViewById(R.id.textView);
                textView.setText(text);
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
            //Toast.makeText(getApplicationContext(), url.toString(), Toast.LENGTH_LONG).show();
            return url;
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
