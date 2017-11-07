package com.a0122554m.kohweilun.projectassignment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class ChallengeQuizHintActivity extends Activity implements
        com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private String question_id;
    private String question_title;
    private String[] question_answers;
    private String question_correct;
    private int question_type = 0; //initial dummy value 0 [0 - normal; 1 - beacon; 2 - gps]
    private String code;
    private String location_description = ""; //dummy initialization
    private String location_coordinates = ""; //dummy initialization
    private String qrCodeScannedResult = ""; //dummy initialization
    private BeaconManager beaconManager;
    private BeaconRegion beaconRegion;
    private int receivedRSSI = -1000; //dummy initialization
    ImageView imageView;
    TextView addInfo;
    private Boolean questionDone;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    private String hint_lat;
    private String hint_lng;

    private static final int REQUEST_ACCESS_FINE_LOCATION_PERMISSION = 200;
    private boolean permissionToAccessFineLocationAccepted = false;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 1) {
            permissionToAccessFineLocationAccepted = grantResults[1] ==
                    PackageManager.PERMISSION_GRANTED;
        }

        if (!permissionToAccessFineLocationAccepted)
            finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_quiz_hint);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, permissions,
                    REQUEST_ACCESS_FINE_LOCATION_PERMISSION);
        }

        question_id = getIntent().getIntExtra("id",0) + "";
        question_title = getIntent().getStringExtra("title");
        question_answers = getIntent().getStringExtra("answers").split(";");
        question_correct = getIntent().getStringExtra("correct");
        question_type = getIntent().getIntExtra("type",0);
        code = getIntent().getStringExtra("code");
        imageView = findViewById(R.id.imageHint);
        addInfo = findViewById(R.id.additionalInfo);

        final String CHALLENGE_PREFS = "challenge_state";
        SharedPreferences challengePreferences = getSharedPreferences(CHALLENGE_PREFS, MODE_PRIVATE);
        questionDone = challengePreferences.getBoolean(code + question_id + "DONE", false);

        if (questionDone){
            addInfo.setText("You have already completed this question.");
        }else {
            //normal question no hints
            if (question_type == 0) {
                GoToQuestionPage(question_id, question_title, question_answers, question_correct, code);
            }
            //beacon hint
            if (question_type == 1) {
                GetHintAsyncTask getHintAsyncTask = new GetHintAsyncTask();
                getHintAsyncTask.execute("http://192.168.137.1:3000/api/Questions/GetHint?_question_id=" + question_id);
            }
            //gps hint
            if (question_type == 2) {
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(10000);
                mLocationRequest.setFastestInterval(5000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();

                GetHintAsyncTask getHintAsyncTask = new GetHintAsyncTask();
                getHintAsyncTask.execute("http://192.168.137.1:3000/api/Questions/GetHint?_question_id=" + question_id);
            }
        }
    }

    private class GetHintAsyncTask extends AsyncTask<String, Void, String> {

        public String doInBackground(String... str){
            URL url = convertToUrl(str[0]);
            HttpURLConnection httpURLConnection = null;
            int responseCode;
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
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
            try {
                JSONObject location = new JSONObject(result);
                location_description = location.optString("description");
                location_coordinates = location.optString("coordinates");
                new ImageLoadTask("http://192.168.137.1:3000/img/" + location_description, imageView).execute();
                setUpBeaconOrGPSIfNeeded();
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String urlString;
        private ImageView imageView;

        public ImageLoadTask(String _url, ImageView _imageView) {
            this.urlString = _url;
            this.imageView = _imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            URL url = convertToUrl(urlString);
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }

    // method to handle url stuff
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

    private void setUpBeaconOrGPSIfNeeded(){
        if (question_type == 1) {
            String beaconCoordinates[] = location_coordinates.split(";");
            setUpBeacon(beaconCoordinates[0], beaconCoordinates[1], beaconCoordinates[2]);
        }
        if (question_type == 2) {
            String gpsCoordinates[] = location_coordinates.split(";");
            hint_lat = gpsCoordinates[0];
            hint_lng = gpsCoordinates[1];
        }
    }

    public void onClick_scanQR_or_goQuestion(View view){
        if (questionDone) {
            System.out.println("Happily doing nothing.");
        }else {
            if (qrCodeScannedResult.equals("")) {
                Intent qrCodeIntent = new Intent(this, ChallengeQuizQRCodeActivity.class);
                int requestCode = 5678;
                startActivityForResult(qrCodeIntent, requestCode);
            } else {
                GoToQuestionPage(question_id, question_title, question_answers, question_correct, code);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("Hint Activity Location:" + location_coordinates);
        if (requestCode == 5678) {
            if (resultCode == RESULT_OK) {
                qrCodeScannedResult = data.getStringExtra("scannedResult");
                System.out.println("Hint Activity Scanned: " + qrCodeScannedResult);
                System.out.println("Hint Activity Boolean: " + location_coordinates.equals(qrCodeScannedResult));
                if (!location_coordinates.equals(qrCodeScannedResult)) {
                    addInfo.setText("You have scanned the wrong QR code! Try again!");
                    qrCodeScannedResult = "";
                }
                setUpBeaconOrGPSIfNeeded();
            }
        }
    }

    private void setUpBeacon(String uuid, String major, String minor){
        beaconManager = new BeaconManager(getApplicationContext());
        beaconRegion = new BeaconRegion(
                "monitored region",
                UUID.fromString(uuid), Integer.parseInt(major), Integer.parseInt(minor));
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(beaconRegion);
                beaconManager.startRanging(beaconRegion);
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {

            @Override
            public void onEnteredRegion(BeaconRegion beaconRegion, List<Beacon> beacons) {
                //addInfo.setText("You are close!");
            }

            @Override
            public void onExitedRegion(BeaconRegion region) {
                //addInfo.setText("You are getting further!");
            }
        });

        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion region, List<Beacon> beacons) {
                if (!beacons.isEmpty()) {
                    Beacon beaconForQuiz = beacons.get(0);

                    receivedRSSI = beaconForQuiz.getRssi();
                    System.out.println("Hint Activity Beacon Boolean: " + location_coordinates.equals(qrCodeScannedResult));
                    if (location_coordinates.equals(qrCodeScannedResult)) {
                        if (receivedRSSI >= -70) {
                            addInfo.setText("Verified! Proceed to question.");
                            Button buttonInHintActivity = findViewById(R.id.scanQRorGoQuestionButton);
                            buttonInHintActivity.setText(getResources().getString(R.string.go_question_button));
                        } else {
                            addInfo.setText("Walk closer to beacon to view question.");
                        }
                    }
                }
            }
        });
    }

    private void GoToQuestionPage(String _question_id, String _question, String[] _answers, String _correct, String _code){
        Intent questionIntent = new Intent(getApplicationContext(), BothTypesQuestionActivity.class);
        questionIntent.putExtra("challenge", true);
        questionIntent.putExtra("id", _question_id);
        questionIntent.putExtra("question", _question);
        questionIntent.putExtra("answers", _answers);
        questionIntent.putExtra("correct", _correct);
        questionIntent.putExtra("code", _code);
        startActivity(questionIntent);
    }

    @Override
    protected void onPause() {
        if(!questionDone) {
            if (question_type == 1) {
                beaconManager.stopMonitoring(beaconRegion.getIdentifier());
                beaconManager.stopRanging(beaconRegion);
            }
            if (question_type == 2) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (!questionDone) {
            if (question_type == 1) {
                beaconManager.stopMonitoring(beaconRegion.getIdentifier());
                beaconManager.stopRanging(beaconRegion);
            }
        }
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (question_type == 2) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (question_type == 2) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (question_type == 2) {
            if (mGoogleApiClient.isConnected()) {
                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                                    mGoogleApiClient, mLocationRequest, this);
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if (mCurrentLocation != null) {
            if (location_coordinates != "" && location_coordinates.equals(qrCodeScannedResult)) {
                String lat = String.valueOf(mCurrentLocation.getLatitude());
                String lng = String.valueOf(mCurrentLocation.getLongitude());
                double lat_diff = Double.parseDouble(lat) - Double.parseDouble(hint_lat);
                double lng_diff = Double.parseDouble(lng) - Double.parseDouble(hint_lng);

                if (lat_diff <= 0.0001 && lat_diff >= -0.0001 && lng_diff <= 0.0001 && lng_diff >= -0.0001) {
                    addInfo.setText("Verified! Proceed to question.");
                    Button buttonInHintActivity = findViewById(R.id.scanQRorGoQuestionButton);
                    buttonInHintActivity.setText(getResources().getString(R.string.go_question_button));
                } else {
                    addInfo.setText("Walk closer to view question." + " Latitude = " + lat + ", Longitude = " + lng);
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                            mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

}
