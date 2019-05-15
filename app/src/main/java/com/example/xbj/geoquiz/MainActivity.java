package com.example.xbj.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private boolean mIsCheater;

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mNextTextView;
    private TextView mQuestionTextView;
    private Button mCheatButton;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_anstralia,true),
            new Question(R.string.question_oceans,false),
            new Question(R.string.question_mideast,true),
            new Question(R.string.question_myself,true)
    };

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate(bundle) called");
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        /*int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);*/

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(MainActivity.this,R.string.correct_toast,Toast.LENGTH_SHORT).show();*/
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               /* Toast.makeText(MainActivity.this,R.string.incorect_toast,Toast.LENGTH_SHORT).show();*/
                checkAnswer(false);
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                /*int question = mQuestionBank[mCurrentIndex].getTextResId();
                mQuestionTextView.setText(question);*/
                updateQuestion();
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + mQuestionBank.length - 1) % mQuestionBank.length;
                /*int question = mQuestionBank[mCurrentIndex].getTextResId();
                mQuestionTextView.setText(question);*/
                updateQuestion();
            }
        });

        mNextTextView = (TextView) findViewById(R.id.question_text_view);
        mNextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                /*int question = mQuestionBank[mCurrentIndex].getTextResId();
                mQuestionTextView.setText(question);*/
                mIsCheater = false;
                updateQuestion();
            }
        });

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start CheatActivity 启动CheatActivity活动
                //Intent intent = new Intent(MainActivity.this,CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this,answerIsTrue);
                //startActivity(intent);
                startActivityForResult(intent,REQUEST_CODE_CHEAT);
            }
        });
        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        mIsCheater = CheatActivity.wasAnswerShown(data);
    }

    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (mIsCheater){
            messageResId = R.string.judgment_toast;
        }else{
            if (userPressedTrue ==answerIsTrue){
                messageResId = R.string.correct_toast;
            }else{
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"onSaveInstancState");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
    }
}
