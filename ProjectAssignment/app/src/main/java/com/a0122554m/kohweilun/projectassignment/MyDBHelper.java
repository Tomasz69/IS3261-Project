package com.a0122554m.kohweilun.projectassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JR on 30/10/17.
 */

public class MyDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RevisionQuestionBankDB";

    public static final String TABLE_NAME = "RevisionQuestionBank";
    public static final String KEY_ID = "_id";
    public static final String QUESTION = "question";
    public static final String CHOICE1 = "choice1";
    public static final String CHOICE2 = "choice2";
    public static final String CHOICE3 = "choice3";
    public static final String CHOICE4 = "choice4";
    public static final String ANSWER = "answer";

    private static final String SQLite_CREATE = "CREATE TABLE "
            + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + QUESTION + " TEXT NOT NULL, "
            + CHOICE1 + " TEXT NOT NULL, "
            + CHOICE2 + " TEXT NOT NULL, "
            + CHOICE3 + " TEXT NOT NULL, "
            + CHOICE4 + " TEXT NOT NULL, "
            + ANSWER + " TEXT NOT NULL);";

    private static final String SQLite_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQLite_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQLite_DELETE);
        onCreate(sqLiteDatabase);
    }

    public long insertQuestion(RevisionQuestion revisionQuestion) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUESTION, revisionQuestion.getQuestion());
        values.put(CHOICE1, revisionQuestion.getChoice(0));
        values.put(CHOICE2, revisionQuestion.getChoice(1));
        values.put(CHOICE3, revisionQuestion.getChoice(2));
        values.put(CHOICE4, revisionQuestion.getChoice(3));
        values.put(ANSWER, revisionQuestion.getAnswer());

        return db.insert(TABLE_NAME, null, values);
    }

    public List<RevisionQuestion> getAllQuestionsList() {
        List<RevisionQuestion> questionList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                RevisionQuestion question = new RevisionQuestion();

                String questionText = c.getString(c.getColumnIndex(QUESTION));
                question.setQuestion(questionText);
                String choice1Text = c.getString(c.getColumnIndex(CHOICE1));
                question.setChoice(0, choice1Text);
                String choice2Text = c.getString(c.getColumnIndex(CHOICE2));
                question.setChoice(1, choice2Text);
                String choice3Text = c.getString(c.getColumnIndex(CHOICE3));
                question.setChoice(2, choice3Text);
                String choice4Text = c.getString(c.getColumnIndex(CHOICE4));
                question.setChoice(3, choice4Text);
                String answerText = c.getString(c.getColumnIndex(ANSWER));
                question.setAnswer(answerText);

                questionList.add(question);
            } while (c.moveToNext());
        }

        return questionList;
    }
}
