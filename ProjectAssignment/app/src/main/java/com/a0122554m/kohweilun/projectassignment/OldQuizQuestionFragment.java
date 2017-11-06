package com.a0122554m.kohweilun.projectassignment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class OldQuizQuestionFragment extends Fragment {

    private String QUESTION;
    private String[] ANSWERS;
    private String TEXT;

    public OldQuizQuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Bundle args = getArguments();
        QUESTION = args.getString("question");
        ANSWERS = args.getStringArray("answers");
        TEXT = QUESTION;
        
        for (int i = 0; i < ANSWERS.length; i++){
            int questionNum = i + 1;
            TEXT += "\n" + questionNum + ") " + ANSWERS[i];
        }
        return inflater.inflate(R.layout.old_fragment_quiz_question, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView quizQuestionText = view.findViewById(R.id.QuizQuestionText);
        quizQuestionText.setText(TEXT);
    }

}
