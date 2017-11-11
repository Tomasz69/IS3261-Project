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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.useful_links_list);
        this.setTitle(R.string.useful_title);

        final String[] itemname = {
                getResources().getString(R.string.useful_item1),
                getResources().getString(R.string.useful_item2),
                getResources().getString(R.string.useful_item3),
                getResources().getString(R.string.useful_item4),
                getResources().getString(R.string.useful_item5)
        };

        final Integer[] imgid = {
                R.drawable.android_logo,
                R.drawable.vogella_logo,
                R.drawable.codepath_logo,
                R.drawable.thenewboston_logo,
                R.drawable.derekbanas_logo
        };

        final String[] description = {
                getResources().getString(R.string.useful_desc1),
                getResources().getString(R.string.useful_desc2),
                getResources().getString(R.string.useful_desc3),
                getResources().getString(R.string.useful_desc4),
                getResources().getString(R.string.useful_desc5)
        };

        final String[] url = {
                "http://developer.android.com/develop/index.html",
                "http://www.vogella.com/tutorials/android.html",
                "https://guides.codepath.com/android",
                "https://www.youtube.com/playlist?list=PL2F07DBCDCC01493A",
                "https://www.youtube.com/playlist?list=PLGLfVvz_LVvQUjiCc8lUT9aO0GsWA4uNe",
        };
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
