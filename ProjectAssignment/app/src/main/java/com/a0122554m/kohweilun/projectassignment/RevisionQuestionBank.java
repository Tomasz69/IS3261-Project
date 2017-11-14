package com.a0122554m.kohweilun.projectassignment;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JR on 28/10/17.
 */

public class RevisionQuestionBank {
    List<RevisionQuestion> questionList = new ArrayList<>();
    RevisionDBHelper revisionDBHelper;
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
        revisionDBHelper = new RevisionDBHelper(context);
        revisionDBHelper.deleteTable();
        revisionDBHelper.createTable();
        //questionList = revisionDBHelper.getAllQuestionsList(null);

        //if (questionList.isEmpty()) {
            // Lesson 1
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is Cordova originally known as?",
                    new String[]{
                            "GapPhone-You are close!",
                            "PhoneGap-See lesson 1 slide 21.",
                            "PhoneGag-Nothing to do with memes!",
                            "9Gag-Seriously?!"
                    },
                    "PhoneGap"), titlesList[0]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is a good quick-and-dirty way to test your codes?",
                    new String[]{
                            "Bread-Cook it more!",
                            "Sandwich-Have less filings.",
                            "Toast-Toasts are indeed very useful in coding!",
                            "Burger-Don't cook it so much."
                    },
                    "Toast"), titlesList[0]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is the name of a common browser rendering engine?",
                    new String[]{
                            "Webkit-See lesson 1 slide 20",
                            "Webkid-You are close! Spell better!",
                            "Webkat-No food involved!",
                            "KitKat-Go eat. You are hungry."
                    },
                    "Webkit"), titlesList[0]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("Identify the wrong design principle for clarity.",
                    new String[]{
                            "Text is legible at every font size-That's a correct principle!",
                            "Icons are precise and easy to understand.-That's a correct principle!",
                            "Decorations are subtle and appropriate.-That's a correct principle!",
                            "Use long phrases to be detailed.-You should keep it brief and use short phrases."
                    },
                    "Use long phrases to be detailed."), titlesList[0]);

            // Lesson 2
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is Cordova originally known as?",
                    new String[]{
                            "GapPhone-You are close!",
                            "PhoneGap-See lesson 1 slide 21.",
                            "PhoneGag-Nothing to do with memes!",
                            "9Gag-Seriously?!"
                    },
                    "PhoneGap"), titlesList[1]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is a good quick-and-dirty way to test your codes?",
                    new String[]{
                            "Bread-Cook it more!",
                            "Sandwich-Have less filings.",
                            "Toast-Toasts are indeed very useful in coding!",
                            "Burger-Don't cook it so much."
                    },
                    "Toast"), titlesList[1]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is the name of a common browser rendering engine?",
                    new String[]{
                            "Webkit-See lesson 1 slide 20",
                            "Webkid-You are close! Spell better!",
                            "Webkat-No food involved!",
                            "KitKat-Go eat. You are hungry."
                    },
                    "Webkit"), titlesList[1]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("Identify the wrong design principle for clarity.",
                    new String[]{
                            "Text is legible at every font size-That's a correct principle!",
                            "Icons are precise and easy to understand.-That's a correct principle!",
                            "Decorations are subtle and appropriate.-That's a correct principle!",
                            "Use long phrases to be detailed.-You should keep it brief and use short phrases."
                    },
                    "Use long phrases to be detailed."), titlesList[1]);

            // Lesson 3
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is Cordova originally known as?",
                    new String[]{
                            "GapPhone-You are close!",
                            "PhoneGap-See lesson 1 slide 21.",
                            "PhoneGag-Nothing to do with memes!",
                            "9Gag-Seriously?!"
                    },
                    "PhoneGap"), titlesList[2]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is a good quick-and-dirty way to test your codes?",
                    new String[]{
                            "Bread-Cook it more!",
                            "Sandwich-Have less filings.",
                            "Toast-Toasts are indeed very useful in coding!",
                            "Burger-Don't cook it so much."
                    },
                    "Toast"), titlesList[2]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is the name of a common browser rendering engine?",
                    new String[]{
                            "Webkit-See lesson 1 slide 20",
                            "Webkid-You are close! Spell better!",
                            "Webkat-No food involved!",
                            "KitKat-Go eat. You are hungry."
                    },
                    "Webkit"), titlesList[2]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("Identify the wrong design principle for clarity.",
                    new String[]{
                            "Text is legible at every font size-That's a correct principle!",
                            "Icons are precise and easy to understand.-That's a correct principle!",
                            "Decorations are subtle and appropriate.-That's a correct principle!",
                            "Use long phrases to be detailed.-You should keep it brief and use short phrases."
                    },
                    "Use long phrases to be detailed."), titlesList[2]);

            // Lesson 4
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is Cordova originally known as?",
                    new String[]{
                            "GapPhone-You are close!",
                            "PhoneGap-See lesson 1 slide 21.",
                            "PhoneGag-Nothing to do with memes!",
                            "9Gag-Seriously?!"
                    },
                    "PhoneGap"), titlesList[3]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is a good quick-and-dirty way to test your codes?",
                    new String[]{
                            "Bread-Cook it more!",
                            "Sandwich-Have less filings.",
                            "Toast-Toasts are indeed very useful in coding!",
                            "Burger-Don't cook it so much."
                    },
                    "Toast"), titlesList[3]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is the name of a common browser rendering engine?",
                    new String[]{
                            "Webkit-See lesson 1 slide 20",
                            "Webkid-You are close! Spell better!",
                            "Webkat-No food involved!",
                            "KitKat-Go eat. You are hungry."
                    },
                    "Webkit"), titlesList[3]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("Identify the wrong design principle for clarity.",
                    new String[]{
                            "Text is legible at every font size-That's a correct principle!",
                            "Icons are precise and easy to understand.-That's a correct principle!",
                            "Decorations are subtle and appropriate.-That's a correct principle!",
                            "Use long phrases to be detailed.-You should keep it brief and use short phrases."
                    },
                    "Use long phrases to be detailed."), titlesList[3]);

            // Lesson 5
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is Cordova originally known as?",
                    new String[]{
                            "GapPhone-You are close!",
                            "PhoneGap-See lesson 1 slide 21.",
                            "PhoneGag-Nothing to do with memes!",
                            "9Gag-Seriously?!"
                    },
                    "PhoneGap"), titlesList[4]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is a good quick-and-dirty way to test your codes?",
                    new String[]{
                            "Bread-Cook it more!",
                            "Sandwich-Have less filings.",
                            "Toast-Toasts are indeed very useful in coding!",
                            "Burger-Don't cook it so much."
                    },
                    "Toast"), titlesList[4]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is the name of a common browser rendering engine?",
                    new String[]{
                            "Webkit-See lesson 1 slide 20",
                            "Webkid-You are close! Spell better!",
                            "Webkat-No food involved!",
                            "KitKat-Go eat. You are hungry."
                    },
                    "Webkit"), titlesList[4]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("Identify the wrong design principle for clarity.",
                    new String[]{
                            "Text is legible at every font size-That's a correct principle!",
                            "Icons are precise and easy to understand.-That's a correct principle!",
                            "Decorations are subtle and appropriate.-That's a correct principle!",
                            "Use long phrases to be detailed.-You should keep it brief and use short phrases."
                    },
                    "Use long phrases to be detailed."), titlesList[4]);

            // Lesson 6
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is Cordova originally known as?",
                    new String[]{
                            "GapPhone-You are close!",
                            "PhoneGap-See lesson 1 slide 21.",
                            "PhoneGag-Nothing to do with memes!",
                            "9Gag-Seriously?!"
                    },
                    "PhoneGap"), titlesList[5]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is a good quick-and-dirty way to test your codes?",
                    new String[]{
                            "Bread-Cook it more!",
                            "Sandwich-Have less filings.",
                            "Toast-Toasts are indeed very useful in coding!",
                            "Burger-Don't cook it so much."
                    },
                    "Toast"), titlesList[5]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is the name of a common browser rendering engine?",
                    new String[]{
                            "Webkit-See lesson 1 slide 20",
                            "Webkid-You are close! Spell better!",
                            "Webkat-No food involved!",
                            "KitKat-Go eat. You are hungry."
                    },
                    "Webkit"), titlesList[5]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("Identify the wrong design principle for clarity.",
                    new String[]{
                            "Text is legible at every font size-That's a correct principle!",
                            "Icons are precise and easy to understand.-That's a correct principle!",
                            "Decorations are subtle and appropriate.-That's a correct principle!",
                            "Use long phrases to be detailed.-You should keep it brief and use short phrases."
                    },
                    "Use long phrases to be detailed."), titlesList[5]);

            // Lesson 7
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is Cordova originally known as?",
                    new String[]{
                            "GapPhone-You are close!",
                            "PhoneGap-See lesson 1 slide 21.",
                            "PhoneGag-Nothing to do with memes!",
                            "9Gag-Seriously?!"
                    },
                    "PhoneGap"), titlesList[6]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is a good quick-and-dirty way to test your codes?",
                    new String[]{
                            "Bread-Cook it more!",
                            "Sandwich-Have less filings.",
                            "Toast-Toasts are indeed very useful in coding!",
                            "Burger-Don't cook it so much."
                    },
                    "Toast"), titlesList[6]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is the name of a common browser rendering engine?",
                    new String[]{
                            "Webkit-See lesson 1 slide 20",
                            "Webkid-You are close! Spell better!",
                            "Webkat-No food involved!",
                            "KitKat-Go eat. You are hungry."
                    },
                    "Webkit"), titlesList[6]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("Identify the wrong design principle for clarity.",
                    new String[]{
                            "Text is legible at every font size-That's a correct principle!",
                            "Icons are precise and easy to understand.-That's a correct principle!",
                            "Decorations are subtle and appropriate.-That's a correct principle!",
                            "Use long phrases to be detailed.-You should keep it brief and use short phrases."
                    },
                    "Use long phrases to be detailed."), titlesList[6]);

            // Lesson 8
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is Cordova originally known as?",
                    new String[]{
                            "GapPhone-You are close!",
                            "PhoneGap-See lesson 1 slide 21.",
                            "PhoneGag-Nothing to do with memes!",
                            "9Gag-Seriously?!"
                    },
                    "PhoneGap"), titlesList[7]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is a good quick-and-dirty way to test your codes?",
                    new String[]{
                            "Bread-Cook it more!",
                            "Sandwich-Have less filings.",
                            "Toast-Toasts are indeed very useful in coding!",
                            "Burger-Don't cook it so much."
                    },
                    "Toast"), titlesList[7]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is the name of a common browser rendering engine?",
                    new String[]{
                            "Webkit-See lesson 1 slide 20",
                            "Webkid-You are close! Spell better!",
                            "Webkat-No food involved!",
                            "KitKat-Go eat. You are hungry."
                    },
                    "Webkit"), titlesList[7]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("Identify the wrong design principle for clarity.",
                    new String[]{
                            "Text is legible at every font size-That's a correct principle!",
                            "Icons are precise and easy to understand.-That's a correct principle!",
                            "Decorations are subtle and appropriate.-That's a correct principle!",
                            "Use long phrases to be detailed.-You should keep it brief and use short phrases."
                    },
                    "Use long phrases to be detailed."), titlesList[7]);

            // Lesson 9
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is Cordova originally known as?",
                    new String[]{
                            "GapPhone-You are close!",
                            "PhoneGap-See lesson 1 slide 21.",
                            "PhoneGag-Nothing to do with memes!",
                            "9Gag-Seriously?!"
                    },
                    "PhoneGap"), titlesList[8]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is a good quick-and-dirty way to test your codes?",
                    new String[]{
                            "Bread-Cook it more!",
                            "Sandwich-Have less filings.",
                            "Toast-Toasts are indeed very useful in coding!",
                            "Burger-Don't cook it so much."
                    },
                    "Toast"), titlesList[8]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is the name of a common browser rendering engine?",
                    new String[]{
                            "Webkit-See lesson 1 slide 20",
                            "Webkid-You are close! Spell better!",
                            "Webkat-No food involved!",
                            "KitKat-Go eat. You are hungry."
                    },
                    "Webkit"), titlesList[8]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("Identify the wrong design principle for clarity.",
                    new String[]{
                            "Text is legible at every font size-That's a correct principle!",
                            "Icons are precise and easy to understand.-That's a correct principle!",
                            "Decorations are subtle and appropriate.-That's a correct principle!",
                            "Use long phrases to be detailed.-You should keep it brief and use short phrases."
                    },
                    "Use long phrases to be detailed."), titlesList[8]);

            // Lesson 10
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is Cordova originally known as?",
                    new String[]{
                            "GapPhone-You are close!",
                            "PhoneGap-See lesson 1 slide 21.",
                            "PhoneGag-Nothing to do with memes!",
                            "9Gag-Seriously?!"
                    },
                    "PhoneGap"), titlesList[9]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is a good quick-and-dirty way to test your codes?",
                    new String[]{
                            "Bread-Cook it more!",
                            "Sandwich-Have less filings.",
                            "Toast-Toasts are indeed very useful in coding!",
                            "Burger-Don't cook it so much."
                    },
                    "Toast"), titlesList[9]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is the name of a common browser rendering engine?",
                    new String[]{
                            "Webkit-See lesson 1 slide 20",
                            "Webkid-You are close! Spell better!",
                            "Webkat-No food involved!",
                            "KitKat-Go eat. You are hungry."
                    },
                    "Webkit"), titlesList[9]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("Identify the wrong design principle for clarity.",
                    new String[]{
                            "Text is legible at every font size-That's a correct principle!",
                            "Icons are precise and easy to understand.-That's a correct principle!",
                            "Decorations are subtle and appropriate.-That's a correct principle!",
                            "Use long phrases to be detailed.-You should keep it brief and use short phrases."
                    },
                    "Use long phrases to be detailed."), titlesList[9]);

            // Lesson 11
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is Cordova originally known as?",
                    new String[]{
                            "GapPhone-You are close!",
                            "PhoneGap-See lesson 1 slide 21.",
                            "PhoneGag-Nothing to do with memes!",
                            "9Gag-Seriously?!"
                    },
                    "PhoneGap"), titlesList[10]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is a good quick-and-dirty way to test your codes?",
                    new String[]{
                            "Bread-Cook it more!",
                            "Sandwich-Have less filings.",
                            "Toast-Toasts are indeed very useful in coding!",
                            "Burger-Don't cook it so much."
                    },
                    "Toast"), titlesList[10]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("What is the name of a common browser rendering engine?",
                    new String[]{
                            "Webkit-See lesson 1 slide 20",
                            "Webkid-You are close! Spell better!",
                            "Webkat-No food involved!",
                            "KitKat-Go eat. You are hungry."
                    },
                    "Webkit"), titlesList[10]);
            revisionDBHelper.insertQuestion(new RevisionQuestion("Identify the wrong design principle for clarity.",
                    new String[]{
                            "Text is legible at every font size-That's a correct principle!",
                            "Icons are precise and easy to understand.-That's a correct principle!",
                            "Decorations are subtle and appropriate.-That's a correct principle!",
                            "Use long phrases to be detailed.-You should keep it brief and use short phrases."
                    },
                    "Use long phrases to be detailed."), titlesList[10]);

            questionList = revisionDBHelper.getAllQuestionsList(null);
        //}

        questionList = revisionDBHelper.getAllQuestionsList(lesson);
    }
}
