package com.a0122554m.kohweilun.projectassignment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class OldDynamicLessonsList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_activity_dynamic_lessons_list);

        CustomAdapter adapter = new CustomAdapter(this);
        ListView lessonsListView = findViewById(R.id.lessonsListView);
        lessonsListView.setAdapter(adapter);

        LoadFeedData loadFeedData = new LoadFeedData(adapter);
        loadFeedData.execute();

        lessonsListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView parentView, View childView, int position, long id) {

            }

            public void onNothingSelected(AdapterView parentView) {

            }
        });
    }

    private class CustomAdapter extends BaseAdapter {

        private Context mContext;

        private LayoutInflater mLayoutInflater;

        private ArrayList<String> lessonsList = new ArrayList<String>();

        public CustomAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return lessonsList.size();
        }

        @Override
        public Object getItem(int position) {
            return lessonsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            return null;
        }

        public void updateEntries(ArrayList<String> entries) {
            lessonsList = entries;
            notifyDataSetChanged();
        }
    }

    private class LoadFeedData extends AsyncTask<Void, Void, ArrayList<String>> {

        //put url here
        private final String mUrl = "";

        private final CustomAdapter mAdapter;

        public LoadFeedData(CustomAdapter adapter) {
            mAdapter = adapter;
        }


        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            return null;
        }

        protected void onPostExecute(ArrayList<String> entries) {
            mAdapter.updateEntries(entries);
        }
    }
}
