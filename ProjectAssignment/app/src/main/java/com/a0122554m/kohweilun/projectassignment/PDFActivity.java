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
        String fileName = getIntent().getStringExtra("fileName");
        String title = getIntent().getStringExtra("title");
        assignFragments(fileName, title);
    }

    protected void assignFragments(String _fileName, String _title){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment pdfFragment = new PDFFragment();
        Bundle args = new Bundle();
        args.putString("fileName", _fileName);
        args.putString("title", _title);
        pdfFragment.setArguments(args);
        fragmentTransaction.add(R.id.fragmentforpdf, pdfFragment);
        fragmentTransaction.commit();
    }

}
