package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends Activity {
//    CallbackManager callbackManager;
//    ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Login Button.
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
//        loginButton.setReadPermissions(Arrays.asList(
//                "public_profile", "email", "user_birthday"));
//
//        FacebookSdk.sdkInitialize(this.getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//
//        profileTracker = new ProfileTracker() {
//            @Override
//            protected void onCurrentProfileChanged(
//                    Profile oldProfile,
//                    Profile currentProfile) {
//                // App code
//                if (oldProfile != null)
//                    Toast.makeText(MainActivity.this, "oldProfile (name) = " + oldProfile.getName(), Toast.LENGTH_SHORT).show();
//                if (currentProfile != null)
//                    Toast.makeText(MainActivity.this, "currentProfile (name) = " + currentProfile.getName(), Toast.LENGTH_SHORT).show();
//            }
//        };

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

    /*start of temporary buttons*/
    public void onClick_GoToLessonList(View view){
        Intent lessonListIntent = new Intent(this, StaticLessonsList.class);
        startActivity(lessonListIntent);
    }

    //temporary testing
    public void onClick_GoToChallengeQuizActivity(View view){
        Intent beaconIntent = new Intent(this, QuizWebAPIActivity.class);
        beaconIntent.putExtra("challengeQuizCode", "B34C0N");
        startActivity(beaconIntent);
    }

    public void onClick_GoToRevisionListActivity(View view) {
        Intent revisionIntent = new Intent(this, RevisionListActivity.class);
        startActivity(revisionIntent);
    }

    public void onClick_GoToLinks(View view) {
        Intent linksIntent = new Intent(this, LinksActivity.class);
        startActivity(linksIntent);
    }
}
