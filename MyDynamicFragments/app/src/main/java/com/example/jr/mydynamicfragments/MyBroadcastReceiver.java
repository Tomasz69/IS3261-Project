package com.example.jr.mydynamicfragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by JR on 20/9/17.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent i) {
        Toast.makeText(context, "Broadcast message = " + i.getStringExtra("key"), Toast.LENGTH_SHORT).show();
    }
}
