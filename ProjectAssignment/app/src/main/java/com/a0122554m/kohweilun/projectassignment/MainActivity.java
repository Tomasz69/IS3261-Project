package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class MainActivity extends Activity {
    CallbackManager callbackManager;
    ProfileTracker profileTracker;
    private static final String FB_SHAREDPREF_FOR_APP = "FbSharedPrefForApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Login Button.
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                // App code
                //when user clicks on log out
                if (currentProfile == null){
                    Toast.makeText(MainActivity.this, "FB Logout success!", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = getSharedPreferences(FB_SHAREDPREF_FOR_APP, MODE_PRIVATE).edit();
                    editor.putString("email", null);
                    editor.commit();
                    Intent loginPage = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(loginPage);

                }
            }
        };

//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        // App code
//                        Toast.makeText(MainActivity.this, "FB Login success!", Toast.LENGTH_SHORT).show();
//                        GraphRequest request = GraphRequest.newMeRequest(
//                                loginResult.getAccessToken(),
//                                new GraphRequest.GraphJSONObjectCallback() {
//                                    @Override
//                                    public void onCompleted(JSONObject object, GraphResponse response) {
//                                        Log.v("LoginActivity", response.toString());
//
//                                        // Application code
//                                        try {
//                                            String email = object.getString("email");
//                                            if (email != null)
//                                                Toast.makeText(MainActivity.this, "Email = " + email, Toast.LENGTH_SHORT).show();
//                                            String birthday = object.getString("birthday"); // 01/31/1980 format
//                                            if (birthday != null)
//                                                Toast.makeText(MainActivity.this, "Birthday = " + birthday, Toast.LENGTH_SHORT).show();
//                                        } catch (JSONException exception) {
//                                            Toast.makeText(MainActivity.this, "There is an error accessing the attributes from the JSON object", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                        Bundle parameters = new Bundle();
//                        parameters.putString("fields", "id,name,email,gender,birthday");
//                        request.setParameters(parameters);
//                        request.executeAsync();
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                        Toast.makeText(MainActivity.this, "FB Login cancel!", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                        Toast.makeText(MainActivity.this, "FB Login error!", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        profileTracker.stopTracking();
//    }

    @Override
    protected void onResume() {
        super.onResume();

        //to request permissions from user to enable beacon tracking
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    public void onClick_GoToLessonList(View view){
        Intent lessonListIntent = new Intent(this, PDFLessonsList.class);
        startActivity(lessonListIntent);
    }

    public void onClick_GoToChallengeQuizCodeActivity(View view){
        Intent challengeCode = new Intent(this, ChallengeQuizCodeActivity.class);
        startActivity(challengeCode);
    }

    public void onClick_GoToPastChallengeResultsActivity(View view){
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
