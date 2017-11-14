package com.a0122554m.kohweilun.projectassignment;

/**
 * Created by weilu on 14/11/2017.
 */

public class HardCodedQuestionBank {

    private String[] question_nums;
    private int[] question_ids; //same as location hint ids
    private String[] question_titles;
    private String[] question_answers;
    private String[] question_corrects;
    private int[] question_types; //0 - normal; 1 - beacon; 2 - beacon with QR; 3 - gps; 4 - gps with QR

    public HardCodedQuestionBank(){
        String[] _question_titles = {
                "What is Cordova originally known as?",
                "What is a good quick-and-dirty way to test your codes?",
                "What is the name of a common browser rendering engine?",
                "Identify the wrong design principle for clarity.",
                "What is Cordova originally known as?",
                "What is a good quick-and-dirty way to test your codes?",
                "What is the name of a common browser rendering engine?",
                "Identify the wrong design principle for clarity.",
                "What is Cordova originally known as?",
                "What is a good quick-and-dirty way to test your codes?"
        };
        String[] _question_answers ={
                "GapPhone-You are close!;PhoneGap-See lesson 1 slide 21.;PhoneGag-Nothing to do with memes!;9Gag-Seriously?!",
                "Bread-Cook it more!;Sandwich-Have less filings.;Toast-Toasts are indeed very useful in coding!;Burger-Don't cook it so much.",
                "Webkit-See lesson 1 slide20;Webkid-You are close! Spell better!;Webkat-No food involved!;KitKat-Go eat. You are hungry.",
                "Text is legible at every font size-That's a correct principle!;Icons are precise and easy to understand.-That's a correct principle!;Decorations are subtle and appropriate.-That's a correct principle!;Use long phrases to be detailed.-You should keep it brief and use short phrases.",
                "GapPhone-You are close!;PhoneGap-See lesson 1 slide 21.;PhoneGag-Nothing to do with memes!;9Gag-Seriously?!",
                "Bread-Cook it more!;Sandwich-Have less filings.;Toast-Toasts are indeed very useful in coding!;Burger-Don't cook it so much.",
                "Webkit-See lesson 1 slide20;Webkid-You are close! Spell better!;Webkat-No food involved!;KitKat-Go eat. You are hungry.",
                "Text is legible at every font size-That's a correct principle!;Icons are precise and easy to understand.-That's a correct principle!;Decorations are subtle and appropriate.-That's a correct principle!;Use long phrases to be detailed.-You should keep it brief and use short phrases.",
                "GapPhone-You are close!;PhoneGap-See lesson 1 slide 21.;PhoneGag-Nothing to do with memes!;9Gag-Seriously?!",
                "Bread-Cook it more!;Sandwich-Have less filings.;Toast-Toasts are indeed very useful in coding!;Burger-Don't cook it so much."
        };
        String[] _question_corrects={
                "PhoneGap",
                "Toast",
                "Webkit",
                "Use long phrases to be detailed.",
                "PhoneGap",
                "Toast",
                "Webkit",
                "Use long phrases to be detailed.",
                "PhoneGap",
                "Toast"
        };
        String[] _question_nums = {"1", "2", "3", "4", "5","6","7","8","9","10"};
        int[] _question_ids = {1,2,3,4,5,6,7,8,9,10};
        int[] _question_types = {0,1,2,1,0,1,0,1,2,1};
        question_titles = _question_titles;
        question_answers = _question_answers;
        question_corrects = _question_corrects;
        question_nums = _question_nums;
        question_ids = _question_ids;
        question_types = _question_types;
    }

    public String[] getQuestion_nums() {
        return question_nums;
    }

    public int[] getQuestion_ids() {
        return question_ids;
    }

    public String[] getQuestion_titles() {
        return question_titles;
    }

    public String[] getQuestion_answers() {
        return question_answers;
    }

    public String[] getQuestion_corrects() {
        return question_corrects;
    }

    public int[] getQuestion_types() {
        return question_types;
    }
}
