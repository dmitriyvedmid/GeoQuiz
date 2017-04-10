package com.example.dmitriyvedmid.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String KEY_INDEX = "index";
    private static final String KEY_CORRECT = "correct";
    private static final String KEY_CHEATS_USED = "cheats";
    private static final int REQUEST_CODE_CHEAT = 0;
    Button mTrueButton;
    Button mFalseButton;
    Button mNextQuestionButton;
    Button mCheatButton;
    TextView mQuestionText;
    private Question[] questions;
    int iterator;
    int mCorrectCount;
    int mCheatsUsed;
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mIsCheater = false;

        mTrueButton = (Button) findViewById(R.id.positive_button);
        mFalseButton = (Button) findViewById(R.id.negative_button);
        mNextQuestionButton = (Button) findViewById(R.id.next_question_Button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mQuestionText = (TextView) findViewById(R.id.question_text);

        mTrueButton.setOnClickListener(this);
        mFalseButton.setOnClickListener(this);
        mCheatButton.setOnClickListener(this);
        mNextQuestionButton.setOnClickListener(this);

        questions = new Question[]{
                new Question(R.string.question1, true),
                new Question(R.string.question2, false),
                new Question(R.string.question3, true),
                new Question(R.string.question4, false),
                new Question(R.string.question5, true)
        };

        if (savedInstanceState != null) {
            iterator = savedInstanceState.getInt(KEY_INDEX, 0);
            mCorrectCount = savedInstanceState.getInt(KEY_CORRECT, 0);
            mCheatsUsed = savedInstanceState.getInt(KEY_CHEATS_USED, 0);
        }
        updateQuestion();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positive_button:
                mCheatButton.setClickable(false);
                checkAnswer(true, mTrueButton);
                break;
            case R.id.negative_button:
                mCheatButton.setClickable(false);
                checkAnswer(false, mFalseButton);
                break;
            case R.id.next_question_Button:
                iterator++;
                updateQuestion();
                break;
            case R.id.cheat_button:
                mCheatsUsed++;
                boolean answerIsTrue = questions[iterator].isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(i, REQUEST_CODE_CHEAT);
                break;
        }

    }

    public void updateQuestion() {
        mTrueButton.setBackgroundColor(Color.WHITE);
        mFalseButton.setBackgroundColor(Color.WHITE);
        if (iterator == questions.length - 1) {
            mQuestionText.setText(questions[iterator].getTextResId());
            mFalseButton.setClickable(true);
            mTrueButton.setClickable(true);
            mCheatButton.setClickable(true);
            mNextQuestionButton.setText(R.string.end_test_btn_text);
        } else if (iterator < questions.length) {
            mQuestionText.setText(questions[iterator].getTextResId());
            mFalseButton.setClickable(true);
            mTrueButton.setClickable(true);
            mCheatButton.setClickable(true);
        } else {
            mNextQuestionButton.setClickable(false);
            mNextQuestionButton.setAlpha(0);
            mFalseButton.setClickable(false);
            mFalseButton.setAlpha(0);
            mTrueButton.setClickable(false);
            mTrueButton.setAlpha(0);
            mCheatButton.setClickable(false);
            mCheatButton.setAlpha(0);
            mQuestionText.setText(getResources().getString(R.string.congratulations_toast) + mCorrectCount + "\nПодсказок использованно: " + mCheatsUsed);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    public void checkAnswer(boolean userPressed, Button pressedButton) {
        mFalseButton.setClickable(false);
        mTrueButton.setClickable(false);
        if (mIsCheater) {
            pressedButton.setBackgroundColor(Color.BLUE);
        } else {
            if (questions[iterator].isAnswerTrue() == userPressed) {
                pressedButton.setBackgroundColor(Color.GREEN);
                mCorrectCount++;
            } else pressedButton.setBackgroundColor(Color.RED);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_INDEX, iterator);
        savedInstanceState.putInt(KEY_CORRECT, mCorrectCount);
        savedInstanceState.putInt(KEY_CHEATS_USED, mCheatsUsed);
    }
}