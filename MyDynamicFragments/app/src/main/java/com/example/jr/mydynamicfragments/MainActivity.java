package com.example.jr.mydynamicfragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick_GoToDynamicFragments(View view) {
        Intent myIntent = new Intent(this, Activity1.class);
        startActivity(myIntent);
    }

    public void onClick_GoToFragmentWithInterface(View view) {
        Intent myIntent = new Intent(this, Activity2.class);
        startActivity(myIntent);
    }

    public void onClick_GoToBroadcastReceiver(View view) {
        Intent myIntent = new Intent(this, Activity3.class);
        startActivity(myIntent);
    }
}
