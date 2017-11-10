package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class PastChallengeResultsActivity extends Activity {

    ListView list;
    String[] challenge_codes;
    String[] challenge_results;

    private static final String FB_SHAREDPREF_FOR_APP = "FbSharedPrefForApp";
    private SharedPreferences appPreferences;
    private int user_id;

    TextView pastChallengeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.past_challenge_result_list);
        appPreferences = getSharedPreferences(FB_SHAREDPREF_FOR_APP, MODE_PRIVATE);
        user_id = appPreferences.getInt("user_id",0);
        pastChallengeTV = findViewById(R.id.pastChallengeTextView);
        GetQuestionsAsyncTask getQuestionsAsyncTask = new GetQuestionsAsyncTask();
        getQuestionsAsyncTask.execute("http://192.168.137.1:3000/api/Questions/getPastChallengeResults?user_id=" + user_id);
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
                JSONArray listOfResults = new JSONArray(result);
                int numOfResults =  listOfResults.length();
                challenge_codes = new String[numOfResults];
                challenge_results = new String[numOfResults];

                for (int i = 0; i <  listOfResults.length(); i++) {
                    final JSONObject resultDetails =  listOfResults.getJSONObject(i);
                    challenge_codes[i] = resultDetails.optString("code");
                    challenge_results[i] = "Score: " + resultDetails.optInt("score") + ", Position: " +
                                                resultDetails.optInt("position");
                }
                setUpList();
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
                e.printStackTrace();
                pastChallengeTV.setText("No past results are found.");
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
        PastChallengeResultsListAdapter adapter = new PastChallengeResultsListAdapter(this, challenge_codes, challenge_results);
        list = (ListView) findViewById(R.id.results_list);
        list.setAdapter(adapter);
    }


}
