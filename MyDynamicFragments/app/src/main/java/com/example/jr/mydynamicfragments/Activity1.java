package com.example.jr.mydynamicfragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;

public class Activity1 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment1 = new Fragment1();
        fragmentTransaction.add(R.id.framelayout11, fragment1);
        fragmentTransaction.commit();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment2 = new Fragment2();
            fragmentTransaction.add(R.id.framelayout12, fragment2);
            fragmentTransaction.commit();
        }
    }


}
