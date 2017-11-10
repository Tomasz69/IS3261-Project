package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by JR on 28/10/17.
 */

public class UsefulLinksListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;
    private final String[] description;

    public UsefulLinksListAdapter(Activity context, String[] itemname, Integer[] imgid, String[] description) {
        super(context, R.layout.useful_links_single_link, itemname);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemname = itemname;
        this.imgid = imgid;
        this.description = description;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.useful_links_single_link, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.text_title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        TextView extraText = (TextView) rowView.findViewById(R.id.text_description);

        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid[position]);
        extraText.setText(description[position]);
        return rowView;


    }
}
