package com.example.jr.mydynamicfragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

public class Activity2 extends Activity implements Fragment3.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment3 = new Fragment3();
        fragmentTransaction.add(R.id.framelayout21, fragment3);
        fragmentTransaction.commit();
    }

    public void onFragmentInteraction(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }
}
