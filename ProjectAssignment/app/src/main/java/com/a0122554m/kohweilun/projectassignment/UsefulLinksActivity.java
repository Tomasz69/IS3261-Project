package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class UsefulLinksActivity extends Activity {

    ListView list;
    String[] itemname = {
            "Google Android Developer Page",
            "Vogella's Android Tutorials",
            "CodePath Android Cliffnotes",
            "TheNewBoston's Video Tutorials",
            "Derek Banas' Video Tutorials"
    };

    Integer[] imgid = {
            R.drawable.android_logo,
            R.drawable.vogella_logo,
            R.drawable.codepath_logo,
            R.drawable.thenewboston_logo,
            R.drawable.derekbanas_logo

    };

    String[] description = {
            "The central website where you will find all the resources to get you started with android development. The website includes many resources for learning the fundamentals, a complete API reference and the tools that you will need to start developing.",
            "Contains many tutorials for the beginner and the advanced android programmer. The articles by Lars Vogel are usually expertly written and will provide you with a deeper understanding of the Android architecture.",
            "Welcome to the open-source CodePath Android Cliffnotes! Our goal is to become the central crowdsourced resource for complete and up-to-date practical Android developer guides for any topic.",
            "200 video tutorials that focus on individual aspects of Android development, starting with downloading and installing the SDK. Most videos are around 5 minutes long so they can be enjoyed during a short break.",
            "Derek has posted video tutorials on a number of subjects. Currently there are 26 tutorials on Android development.",
    };

    String[] url = {
            "http://developer.android.com/develop/index.html",
            "http://www.vogella.com/tutorials/android.html",
            "https://guides.codepath.com/android",
            "https://www.youtube.com/playlist?list=PL2F07DBCDCC01493A",
            "https://www.youtube.com/playlist?list=PLGLfVvz_LVvQUjiCc8lUT9aO0GsWA4uNe",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.useful_links_list);

        UsefulLinksListAdapter adapter = new UsefulLinksListAdapter(this, itemname, imgid, description);
        list = (ListView) findViewById(R.id.links_list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Selecteditem = itemname[+position];
                Toast.makeText(getApplicationContext(), "Linking to " + Selecteditem + " ...", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(url[+position]);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });
    }
}
