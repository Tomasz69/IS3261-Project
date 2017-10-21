package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;
import java.util.UUID;

public class QuizBeaconActivity extends Activity {

    private BeaconManager beaconManager;
    private BeaconRegion beaconRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_beacon);
        TextView hint = findViewById(R.id.hint);
        hint.setText("Location Hint: COM1 SR3");

        beaconManager = new BeaconManager(getApplicationContext());
        beaconRegion = new BeaconRegion(
                "monitored region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                17850, 43121);
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
                int receivedRSSI = beaconForQuiz.getRssi();
                TextView hint = findViewById(R.id.hint);
                hint.setText("Location Hint: COM1 SR3 " + receivedRSSI);

                if (receivedRSSI >= -70){
                    String question = "What is Cordova originally called?";
                    String[] answers = {"GapPhone","PhoneGap", "PhoneGag", "9Gag"};
                    assignFragments(question, answers);
                }
            }
        });
    }

    protected void assignFragments(String _question, String[] _answers){
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
        beaconManager.stopMonitoring(beaconRegion.getIdentifier());
        beaconManager.stopRanging(beaconRegion);

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        beaconManager.stopMonitoring(beaconRegion.getIdentifier());
        beaconManager.stopRanging(beaconRegion);

        super.onDestroy();
    }
}
