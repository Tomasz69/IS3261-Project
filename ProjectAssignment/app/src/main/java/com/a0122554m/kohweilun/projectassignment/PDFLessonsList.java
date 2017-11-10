package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class PDFLessonsList extends Activity {

    private String[] filesList = {
            "lesson01_introduction.pdf",
            "lesson02_android_intro.pdf",
            "lesson03_sqlite.pdf",
            "lesson04_shared_preferences.pdf",
            "lesson05_activity_fragment.pdf",
            "lesson06_broadcast_receiver_and_battery.pdf",
            "lesson07_dangerous_permissions.pdf",
            "lesson08_android_sensors_and_location_v3.pdf",
            "lesson09_internet.pdf",
            "lesson10_location_and_map.pdf",
            "lesson11_qr_codes.pdf"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_lessons_list);
        String[] titlesList = {
                getResources().getString(R.string.lesson_button1),
                getResources().getString(R.string.lesson_button2),
                getResources().getString(R.string.lesson_button3),
                getResources().getString(R.string.lesson_button4),
                getResources().getString(R.string.lesson_button5),
                getResources().getString(R.string.lesson_button6),
                getResources().getString(R.string.lesson_button7),
                getResources().getString(R.string.lesson_button8),
                getResources().getString(R.string.lesson_button9),
                getResources().getString(R.string.lesson_button10),
                getResources().getString(R.string.lesson_button11)
        };
        Button[] buttons= {
                findViewById(R.id.lessonButton1),
                findViewById(R.id.lessonButton2),
                findViewById(R.id.lessonButton3),
                findViewById(R.id.lessonButton4),
                findViewById(R.id.lessonButton5),
                findViewById(R.id.lessonButton6),
                findViewById(R.id.lessonButton7),
                findViewById(R.id.lessonButton8),
                findViewById(R.id.lessonButton9),
                findViewById(R.id.lessonButton10),
                findViewById(R.id.lessonButton11)
        };


        int i;
        for (i = 0; i < filesList.length; i++){
            Button button = buttons[i];
            final Intent intent = new Intent(this, PDFActivity.class);
            intent.putExtra("fileName", filesList[i]);
            intent.putExtra("title", titlesList[i]);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String FB_SHAREDPREF_FOR_APP = "FbSharedPrefForApp";
        String PROGRESS_PREFS = "progress_state";
        SharedPreferences appPreferences = getSharedPreferences(FB_SHAREDPREF_FOR_APP, MODE_PRIVATE);
        SharedPreferences progressPreferences = getSharedPreferences(PROGRESS_PREFS, MODE_PRIVATE);
        JSONArray local_lesson_progress_list = new JSONArray();

        int i;
        int user_id = appPreferences.getInt("user_id", 0);
        System.out.println("User ID at lesson list:" + user_id);
        for (i = 0; i < filesList.length; i++){
            String title = filesList[i];
            int lastSeen = progressPreferences.getInt(title + "_LASTSEEN", 0);
            int furthest = progressPreferences.getInt(title + "_FURTHEST", 0);
            JSONObject latest_lesson_progress = new JSONObject();
            try {
                latest_lesson_progress.put("id_lesson_progress", 1);
                latest_lesson_progress.put("title", title);
                latest_lesson_progress.put("user_id", user_id);
                latest_lesson_progress.put("last_seen_page", lastSeen);
                latest_lesson_progress.put("furthest_page", furthest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("Lesson Progress: " + latest_lesson_progress.toString());
            local_lesson_progress_list.put(latest_lesson_progress);
        }
        System.out.println("Lesson Progress List: " + local_lesson_progress_list.toString());
        SyncProgressAsyncTask syncProgressAsyncTask = new SyncProgressAsyncTask();
        syncProgressAsyncTask.execute("http://192.168.137.1:3000/api/user/UpdateLessonProgress", local_lesson_progress_list.toString());
    }

    private class SyncProgressAsyncTask extends AsyncTask<String, Void, String> {

        public String doInBackground(String... str) {
            URL url = convertToUrl(str[0]);
            HttpURLConnection httpURLConnection = null;
            int responseCode;
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty( "Content-Type", "application/json; charset=UTF-8" );
                //httpURLConnection.setRequestProperty("Accept", "application/json");
                OutputStream os = httpURLConnection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(str[1]);
                osw.flush();
                osw.close();
                //httpURLConnection.connect();
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
                Toast.makeText(getApplicationContext(),
                        "Warning: Connect to the internet to save your progress and high scores to the cloud!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                assert httpURLConnection != null;
                httpURLConnection.disconnect();
            }

            return stringBuilder.toString();
        }

        public void onPostExecute(String result) {
            try {
                System.out.println("Raw result: " +result);
                Boolean success = Boolean.valueOf(result);
                if (success){
                    System.out.println("Processing result possible.");
                } else {
                    System.out.println("Gibberish produced.");
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
}
