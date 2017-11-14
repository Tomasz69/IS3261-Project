package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.LoginButton;

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
import java.util.Arrays;

public class MainActivity extends Activity {
    CallbackManager callbackManager;
    ProfileTracker profileTracker;
    private static final String FB_SHAREDPREF_FOR_APP = "FbSharedPrefForApp";
    private static final String PROGRESS_PREFS = "progress_state";
    private SharedPreferences appPreferences;
    private SharedPreferences progressPreferences;

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
    Button setInternetAccessButton;

    public void onClick_ToggleInternetAccess(View view) {
        SharedPreferences.Editor editor = appPreferences.edit();
        if (appPreferences.getBoolean("no_internet_access", false)) {
            //pressed enable button
            editor.putBoolean("no_internet_access", false);
            editor.commit();
            setInternetAccessButton.setText(getResources().getString(R.string.disable_internet));
            if (appPreferences.getString("email", null) == null) {
                Toast.makeText(this, "You need to log in to facebook to enable internet access for the app.",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            //pressed disable button
            editor.putBoolean("no_internet_access", true);
            editor.commit();
            setInternetAccessButton.setText(getResources().getString(R.string.enable_internet));
            Toast.makeText(this, "Enable internet access and log in to facebook to sync your progress to the cloud.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle(R.string.main_title);
        appPreferences = getSharedPreferences(FB_SHAREDPREF_FOR_APP, MODE_PRIVATE);
        progressPreferences = getSharedPreferences(PROGRESS_PREFS, MODE_PRIVATE);
        setInternetAccessButton = findViewById(R.id.setInternetAccess);

        if (appPreferences.getBoolean("no_internet_access", false)) {
            //show enable button
            setInternetAccessButton.setText(getResources().getString(R.string.enable_internet));
            Toast.makeText(this, "Enable internet access and log in to facebook to sync your progress to the cloud.",
                    Toast.LENGTH_LONG).show();
        } else {
            //show disable button
            setInternetAccessButton.setText(getResources().getString(R.string.disable_internet));
        }
        // Set up button for log out.
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                // App code
                //when user clicks on log out
                if (currentProfile == null) {
                    Toast.makeText(MainActivity.this, "FB Logout success!", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = getSharedPreferences(FB_SHAREDPREF_FOR_APP, MODE_PRIVATE).edit();
                    editor.putString("email", null);
                    editor.commit();
                    Intent loginPage = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginPage);
                }
            }
        };
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String BSSID = wifiInfo.getBSSID();
        //test again in school
        //setInternetAccessButton.setText(BSSID);
        if (BSSID.equals("e6:a7:a0:b9:28:50")) {
            syncLessonProgress();
            syncRevisionProgress();
        }
    }

    private void syncLessonProgress() {
        Toast.makeText(this, "Sync lessons called",Toast.LENGTH_SHORT).show(); //testing
        JSONArray local_lesson_progress_list = new JSONArray();

        int i;
        int user_id = appPreferences.getInt("user_id", 0);
        System.out.println("User ID at lesson list:" + user_id);
        for (i = 0; i < filesList.length; i++) {
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
        SyncLessonProgressAsyncTask syncLessonProgressAsyncTask = new SyncLessonProgressAsyncTask();
        syncLessonProgressAsyncTask.execute("http://192.168.137.1:3000/api/user/UpdateLessonProgress", local_lesson_progress_list.toString());
    }

    private class SyncLessonProgressAsyncTask extends AsyncTask<String, Void, String> {

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
                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
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
                //Toast.makeText(getApplicationContext(), "Warning: Connect to the internet to save your progress and high scores to the cloud!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                assert httpURLConnection != null;
                httpURLConnection.disconnect();
            }

            return stringBuilder.toString();
        }

        public void onPostExecute(String result) {
            try {
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void syncRevisionProgress() {
        Toast.makeText(this, "Sync revision called",Toast.LENGTH_SHORT).show(); //testing
        JSONArray local_revision_progress_list = new JSONArray();

        int i;
        int user_id = appPreferences.getInt("user_id", 0);
        for (i = 0; i < filesList.length; i++) {
            String title = filesList[i];
            int highScore = progressPreferences.getInt(title + "_highscore", 0);
            JSONObject latest_revision_progress = new JSONObject();
            try {
                latest_revision_progress.put("id_revision_progress", 1);
                latest_revision_progress.put("title", title);
                latest_revision_progress.put("user_id", user_id);
                latest_revision_progress.put("high_score", highScore);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("Revision Progress: " + latest_revision_progress.toString());
            local_revision_progress_list.put(latest_revision_progress);
        }
        System.out.println("revision Progress List: " + local_revision_progress_list.toString());
        SyncRevisionProgressAsyncTask syncRevisionProgressAsyncTask = new SyncRevisionProgressAsyncTask();
        syncRevisionProgressAsyncTask.execute("http://192.168.137.1:3000/api/user/UpdateRevisionProgress", local_revision_progress_list.toString());
    }

    private class SyncRevisionProgressAsyncTask extends AsyncTask<String, Void, String> {

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
                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
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
                //Toast.makeText(getApplicationContext(),"Warning: Connect to the internet to save your progress and high scores to the cloud!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                assert httpURLConnection != null;
                httpURLConnection.disconnect();
            }

            return stringBuilder.toString();
        }

        public void onPostExecute(String result) {
            try {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //to request permissions from user to enable beacon tracking
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    public void onClick_GoToLessonList(View view) {
        Intent lessonListIntent = new Intent(this, PDFLessonsListActivity.class);
        startActivity(lessonListIntent);
    }

    public void onClick_GoToChallengeQuizCodeActivity(View view) {
        Intent challengeCode = new Intent(this, ChallengeQuizCodeActivity.class);
        startActivity(challengeCode);
    }

    public void onClick_GoToPastChallengeResultsActivity(View view) {
        Intent challengeResults = new Intent(this, PastChallengeResultsActivity.class);
        startActivity(challengeResults);
    }

    public void onClick_GoToRevisionListActivity(View view) {
        Intent revisionIntent = new Intent(this, RevisionListActivity.class);
        startActivity(revisionIntent);
    }

    public void onClick_GoToLinks(View view) {
        Intent linksIntent = new Intent(this, UsefulLinksActivity.class);
        startActivity(linksIntent);
    }
}
