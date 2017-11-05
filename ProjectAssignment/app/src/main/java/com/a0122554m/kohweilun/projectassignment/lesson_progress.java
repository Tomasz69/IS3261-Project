package com.a0122554m.kohweilun.projectassignment;

/**
 * Created by weilu on 5/11/2017.
 */

public class lesson_progress {
    private int id_lesson_progress;
    private String title;
    private int user_id;
    private int last_seen_page;
    private int furthest_page;

    public lesson_progress(String _title, int _user_id, int _last_seen_page, int _furthest_page){
        id_lesson_progress = 1; //dummy id
        title = _title;
        user_id = _user_id;
        last_seen_page = _last_seen_page;
        furthest_page = _furthest_page;
    }

}
