package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;

public class LoginActivity extends Activity {
    CallbackManager callbackManager;
    ProfileTracker profileTracker;

    private static final String FB_SHAREDPREF_FOR_APP = "FbSharedPrefForApp";
    private static final String PROGRESS_PREFS = "progress_state";
    private SharedPreferences appPreferences;
    private SharedPreferences progressPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle(R.string.login_title);
        appPreferences = getSharedPreferences(FB_SHAREDPREF_FOR_APP, MODE_PRIVATE);
        progressPreferences = getSharedPreferences(PROGRESS_PREFS, MODE_PRIVATE);
        String fb_email = appPreferences.getString("email", null);

        // Set up Login Button.
        LoginButton loginButton = findViewById(R.id.login_page_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                // App code
                //if user is logged in, redirect to main page
                if (currentProfile != null){
                    Intent mainPage = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(mainPage);
                }
            }
        };

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.fb_login_success), Toast.LENGTH_SHORT).show();
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());

                                        // Application code
                                        try {
                                            String email = object.getString("email");
                                            String name = object.getString("name");
                                            GetUserAsyncTask getUserAsyncTask = new GetUserAsyncTask();
                                            getUserAsyncTask.execute("http://192.168.137.1:3000/api/user/login?email=" + email + "&name=" + name, email);
                                        } catch (JSONException exception) {
                                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.fb_error_attributes), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.fb_login_cancel), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.fb_login_error), Toast.LENGTH_SHORT).show();
                    }
                });

        if (fb_email != null){
            Intent mainPage = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(mainPage);
        }
    }

    public void onClick_NoInternetAccess(View view){
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putBoolean("no_internet_access", true);
        editor.commit();
        Intent mainPage = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(mainPage);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    private class GetUserAsyncTask extends AsyncTask<String, Void, String> {

        public String doInBackground(String... str) {
            SharedPreferences.Editor editor = getSharedPreferences(FB_SHAREDPREF_FOR_APP, MODE_PRIVATE).edit();
            editor.putString("email", str[1]);
            editor.commit();
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
                JSONArray both_lists = new JSONArray(result);
                JSONArray listOfLessonProgress = both_lists.getJSONArray(0);
                JSONArray listOfRevisionProgress = both_lists.getJSONArray(1);
                SharedPreferences.Editor editor1 = progressPreferences.edit();
                JSONObject lessonProgressDetails = listOfLessonProgress.getJSONObject(0); //dummy initialization
                for (int i = 0; i < listOfLessonProgress.length(); i++) {
                    lessonProgressDetails = listOfLessonProgress.getJSONObject(i);
                    JSONObject revisionProgressDetails = listOfRevisionProgress.getJSONObject(i);
                    String fileName = lessonProgressDetails.optString("title");

                    //lesson stuff
                    int lastSeen = lessonProgressDetails.optInt("last_seen_page");
                    int furthest = lessonProgressDetails.optInt("furthest_page");
                    System.out.println("File Name: " + fileName + "Last Seen: " + lastSeen +", Furthest: " + furthest);
                    editor1.putInt(fileName + "_LASTSEEN", lastSeen);
                    editor1.commit();
                    editor1.putInt(fileName + "_FURTHEST", furthest);
                    editor1.commit();

                    //revision stuff
                    int highScore = revisionProgressDetails.optInt(("high_score"));
                    editor1.putInt(fileName + "_highscore", highScore);
                    editor1.commit();
                }
                //get user_id from last lesson progress pulled
                int user_id = lessonProgressDetails.optInt("user_id");
                System.out.println("User ID upon login:" + user_id);
                SharedPreferences.Editor editor2 = appPreferences.edit();
                editor2.putInt("user_id", user_id);
                editor2.commit();
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

    public void onClick_GoToMainActivity(View view) {
        if (Profile.getCurrentProfile() != null){
            Intent mainPage = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(mainPage);
        }
    }
}
