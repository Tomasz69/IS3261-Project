package com.example.jr.mydynamicfragments;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

public class Activity3 extends Activity {
    MyBroadcastReceiver myReceiver;
    IntentFilter myIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        myReceiver = new MyBroadcastReceiver();
        myIntentFilter = new IntentFilter("SOC_Broadcast");

    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(myReceiver, myIntentFilter);
    }

    @Override
    public void onPause() {
        unregisterReceiver(myReceiver);
        super.onPause();
    }

    public void onClick_SendBroadcast(View view) {
        Intent i = new Intent("some value from intent");
        i.putExtra("key", i);
    }
}
