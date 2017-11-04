package com.a0122554m.kohweilun.projectassignment;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JR on 28/10/17.
 */

public class RevisionQuestionBank {
    List<RevisionQuestion> questionList = new ArrayList<>();
    MyDBHelper myDBHelper;
    String[] titlesList = {
            "Course Introduction",
            "Introduction to Android",
            "SQLite",
            "Shared Preferences",
            "Activity and Fragment",
            "Broadcast Receiver and Battery",
            "Dangerous Permission",
            "Android Sensors and Location",
            "Internet",
            "Location and Map",
            "QR Codes"
    };

    public int getNumQuestions() {
        return questionList.size();
    }

    public String getQuestion(int index) {
        return questionList.get(index).getQuestion();
    }

    public String getChoice(int qIndex, int cIndex) {
        return questionList.get(qIndex).getChoice(cIndex);
    }

    public String getCorrectAnswer(int index) {
        return questionList.get(index).getAnswer();
    }

    public void initQuestions(Context context, String lesson) {
        myDBHelper = new MyDBHelper(context);
        myDBHelper.deleteTable();
        myDBHelper.createTable();
        questionList = myDBHelper.getAllQuestionsList(null);

        if (questionList.isEmpty()) {
            // Lesson 1
            myDBHelper.insertQuestion(new RevisionQuestion("Question 1.1: ",
                    new String[]{"1", "2", "3", "4"}, "1"), titlesList[0]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 1.2: ",
                    new String[]{"1", "2", "3", "4"}, "2"), titlesList[0]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 1.3: ",
                    new String[]{"1", "2", "3", "4"}, "3"), titlesList[0]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 1.4: ",
                    new String[]{"1", "2", "3", "4"}, "4"), titlesList[0]);

            // Lesson 2
            myDBHelper.insertQuestion(new RevisionQuestion("Question 2.1: ",
                    new String[]{"1", "2", "3", "4"}, "1"), titlesList[1]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 2.2: ",
                    new String[]{"1", "2", "3", "4"}, "2"), titlesList[1]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 2.3: ",
                    new String[]{"1", "2", "3", "4"}, "3"), titlesList[1]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 2.4: ",
                    new String[]{"1", "2", "3", "4"}, "4"), titlesList[1]);

            // Lesson 3
            myDBHelper.insertQuestion(new RevisionQuestion("Question 3.1: ",
                    new String[]{"1", "2", "3", "4"}, "1"), titlesList[2]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 3.2: ",
                    new String[]{"1", "2", "3", "4"}, "2"), titlesList[2]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 3.3: ",
                    new String[]{"1", "2", "3", "4"}, "3"), titlesList[2]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 3.4: ",
                    new String[]{"1", "2", "3", "4"}, "4"), titlesList[2]);

            // Lesson 4
            myDBHelper.insertQuestion(new RevisionQuestion("Question 4.1: ",
                    new String[]{"1", "2", "3", "4"}, "1"), titlesList[3]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 4.2: ",
                    new String[]{"1", "2", "3", "4"}, "2"), titlesList[3]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 4.3: ",
                    new String[]{"1", "2", "3", "4"}, "3"), titlesList[3]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 4.4: ",
                    new String[]{"1", "2", "3", "4"}, "4"), titlesList[3]);

            // Lesson 5
            myDBHelper.insertQuestion(new RevisionQuestion("Question 5.1: ",
                    new String[]{"1", "2", "3", "4"}, "1"), titlesList[4]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 5.2: ",
                    new String[]{"1", "2", "3", "4"}, "2"), titlesList[4]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 5.3: ",
                    new String[]{"1", "2", "3", "4"}, "3"), titlesList[4]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 5.4: ",
                    new String[]{"1", "2", "3", "4"}, "4"), titlesList[4]);

            // Lesson 6
            myDBHelper.insertQuestion(new RevisionQuestion("Question 6.1: ",
                    new String[]{"1", "2", "3", "4"}, "1"), titlesList[5]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 6.2: ",
                    new String[]{"1", "2", "3", "4"}, "2"), titlesList[5]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 6.3: ",
                    new String[]{"1", "2", "3", "4"}, "3"), titlesList[5]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 6.4: ",
                    new String[]{"1", "2", "3", "4"}, "4"), titlesList[5]);

            // Lesson 7
            myDBHelper.insertQuestion(new RevisionQuestion("Question 7.1: ",
                    new String[]{"1", "2", "3", "4"}, "1"), titlesList[6]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 7.2: ",
                    new String[]{"1", "2", "3", "4"}, "2"), titlesList[6]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 7.3: ",
                    new String[]{"1", "2", "3", "4"}, "3"), titlesList[6]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 7.4: ",
                    new String[]{"1", "2", "3", "4"}, "4"), titlesList[6]);

            // Lesson 8
            myDBHelper.insertQuestion(new RevisionQuestion("Question 8.1: ",
                    new String[]{"1", "2", "3", "4"}, "1"), titlesList[7]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 8.2: ",
                    new String[]{"1", "2", "3", "4"}, "2"), titlesList[7]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 8.3: ",
                    new String[]{"1", "2", "3", "4"}, "3"), titlesList[7]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 8.4: ",
                    new String[]{"1", "2", "3", "4"}, "4"), titlesList[7]);

            // Lesson 9
            myDBHelper.insertQuestion(new RevisionQuestion("Question 9.1: ",
                    new String[]{"1", "2", "3", "4"}, "1"), titlesList[8]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 9.2: ",
                    new String[]{"1", "2", "3", "4"}, "2"), titlesList[8]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 9.3: ",
                    new String[]{"1", "2", "3", "4"}, "3"), titlesList[8]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 9.44: ",
                    new String[]{"1", "2", "3", "4"}, "4"), titlesList[8]);

            // Lesson 10
            myDBHelper.insertQuestion(new RevisionQuestion("Question 10.1: ",
                    new String[]{"1", "2", "3", "4"}, "1"), titlesList[9]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 10.2: ",
                    new String[]{"1", "2", "3", "4"}, "2"), titlesList[9]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 10.3: ",
                    new String[]{"1", "2", "3", "4"}, "3"), titlesList[9]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 10.4: ",
                    new String[]{"1", "2", "3", "4"}, "4"), titlesList[9]);

            // Lesson 11
            myDBHelper.insertQuestion(new RevisionQuestion("Question 11.1: ",
                    new String[]{"1", "2", "3", "4"}, "1"), titlesList[10]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 11.2: ",
                    new String[]{"1", "2", "3", "4"}, "2"), titlesList[10]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 11.3: ",
                    new String[]{"1", "2", "3", "4"}, "3"), titlesList[10]);
            myDBHelper.insertQuestion(new RevisionQuestion("Question 11.4: ",
                    new String[]{"1", "2", "3", "4"}, "4"), titlesList[10]);

            questionList = myDBHelper.getAllQuestionsList(null);
        }

        questionList = myDBHelper.getAllQuestionsList(lesson);
    }
}
