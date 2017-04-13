package com.example.dmitriyvedmid.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CheatActivity extends AppCompatActivity {

    TextView mAnswerTextView;
    Button mShowAnswer;
    Boolean mAnswerIsTrue;
    public static final String EXTRA_ANSWER_IS_TRUE = "com.dmitr.geoquiz.answer_is_true";
    public static final String EXTRA_ANSWER_SHOWN = "com.dmitr.geoquiz.answer_shown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerTextView =(TextView)findViewById(R.id.answer_textview);
        mShowAnswer =(Button)findViewById(R.id.show_answer_btn);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        setAnswerShownResult(true);

        mShowAnswer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue)
                    Toast.makeText(getApplicationContext(), "Это правда", Toast.LENGTH_LONG).show();
                else Toast.makeText(getApplicationContext(), "Это ложь", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}
