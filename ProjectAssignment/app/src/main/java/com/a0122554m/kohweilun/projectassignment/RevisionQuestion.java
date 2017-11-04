package com.a0122554m.kohweilun.projectassignment;

/**
 * Created by JR on 28/10/17.
 */

public class RevisionQuestion {
    private String question;
    private String[] choices = new String[4];
    private String answer;

    public RevisionQuestion() {

    }

    public RevisionQuestion(String question, String[] choices, String answer) {
        this.question = question;
        this.choices[0] = choices[0];
        this.choices[1] = choices[1];
        this.choices[2] = choices[2];
        this.choices[3] = choices[3];
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getChoices() {
        return choices;
    }

    public void setChoices(String[] choices) {
        this.choices = choices;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getChoice(int i) {
        return choices[i];
    }

    public void setChoice(int i, String choice) {
        this.choices[i] = choice;
    }
}
