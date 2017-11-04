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

    public void initQuestions(Context context) {
        myDBHelper = new MyDBHelper(context);
        questionList = myDBHelper.getAllQuestionsList();

        if (questionList.isEmpty()) {
            myDBHelper.insertQuestion(new RevisionQuestion("Question 1: ",
                    new String[]{"1", "2", "3", "4"}, "1"));
            myDBHelper.insertQuestion(new RevisionQuestion("Question 2: ",
                    new String[]{"1", "2", "3", "4"}, "2"));
            myDBHelper.insertQuestion(new RevisionQuestion("Question 3: ",
                    new String[]{"1", "2", "3", "4"}, "3"));
            myDBHelper.insertQuestion(new RevisionQuestion("Question 4: ",
                    new String[]{"1", "2", "3", "4"}, "4"));

            questionList = myDBHelper.getAllQuestionsList();
        }
    }
}
