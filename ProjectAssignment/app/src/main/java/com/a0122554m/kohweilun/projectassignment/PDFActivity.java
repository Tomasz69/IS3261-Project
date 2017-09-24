package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class PDFActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_activity);
        assignFragments();
    }

    protected void assignFragments(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment pdfFragment = new PDFFragment();
        fragmentTransaction.add(R.id.fragmentforpdf, pdfFragment);
        fragmentTransaction.commit();
    }
}
