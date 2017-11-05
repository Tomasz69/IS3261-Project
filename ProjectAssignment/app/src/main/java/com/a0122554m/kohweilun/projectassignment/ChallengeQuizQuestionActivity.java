package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.UUID;

public class ChallengeQuizQuestionActivity extends Activity {

    private String question_id;
    private String question_title;
    private String[] question_answers;
    private String question_correct;
    private int question_type = 0; //initial dummy value 0 [0 - normal; 1 - beacon; 2 - beacon with QR; 3 - gps; 4 - gps with QR]
    private String location_description;
    private String location_coordinates;
    private BeaconManager beaconManager;
    private BeaconRegion beaconRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question);
        question_id = getIntent().getIntExtra("id",0) + "";
        question_title = getIntent().getStringExtra("title");
        question_answers = getIntent().getStringExtra("answers").split(";");
        question_correct = getIntent().getStringExtra("correct");
        question_type = getIntent().getIntExtra("type",0);

        //normal question no hints
        if (question_type == 0){
            assignFragments(question_title,question_answers);
        }
        //beacon hint
        if (question_type == 1 || question_type == 2) {
            GetHintAsyncTask getHintAsyncTask = new GetHintAsyncTask();
            getHintAsyncTask.execute("http://192.168.137.1:3000/api/Questions/GetHint?_question_id=" + question_id);
        }
        //gps hint
        if (question_type == 3 || question_type == 4) {

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
                setUpQuestionDisplayForBeacon();
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

    private void setUpQuestionDisplayForBeacon(){
        TextView hint = findViewById(R.id.hint);
        hint.setText(location_description);
        String beaconCoordinates[] = location_coordinates.split(";");
        setUpBeacon(beaconCoordinates[0], beaconCoordinates[1], beaconCoordinates[2]);
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
            public void onEnteredRegion(BeaconRegion beaconRegion, List<com.estimote.coresdk.recognition.packets.Beacon> beacons) {
                Toast.makeText(getApplicationContext(), "You are getting closer!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onExitedRegion(BeaconRegion region) {
                Toast.makeText(getApplicationContext(), "You are getting further!", Toast.LENGTH_LONG).show();
            }
        });

        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion region, List<com.estimote.coresdk.recognition.packets.Beacon> beacons) {
                Beacon beaconForQuiz = beacons.get(0);

                /*testing only*/
                int receivedRSSI = beaconForQuiz.getRssi();
                displayRSSI(receivedRSSI);
                /*end of testing*/

                if (receivedRSSI >= -70){
                    String question = question_title;
                    String[] answers = question_answers;
                    assignFragments(question, answers);
                }
            }
        });
    }

    private void assignFragments(String _question, String[] _answers){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment questionFragment = new QuizQuestionFragment();
        Bundle args = new Bundle();
        args.putString("question", _question);
        args.putStringArray("answers", _answers);
        questionFragment.setArguments(args);
        fragmentTransaction.add(R.id.FragmentForQuestion, questionFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onPause() {
        if (question_type == 1 || question_type == 2) {
            beaconManager.stopMonitoring(beaconRegion.getIdentifier());
            beaconManager.stopRanging(beaconRegion);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (question_type == 1 || question_type == 2) {
            beaconManager.stopMonitoring(beaconRegion.getIdentifier());
            beaconManager.stopRanging(beaconRegion);
        }
        super.onDestroy();
    }

    /*methods for testing*/
    private void displayRSSI(int _rssi){
        TextView rssi = findViewById(R.id.rssi);
        //for testing, to be changed
        rssi.setText(_rssi + "");
    }
    /*end of testing methods*/
}
