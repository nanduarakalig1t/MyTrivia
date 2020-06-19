package com.example.mytrivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytrivia.data.AnswerListAsyncResponse;
import com.example.mytrivia.data.QuestionBank;
import com.example.mytrivia.model.Question;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvCounter , tvStatement;
    private ImageButton btnPrev, btnNext;
    private Button btnTrue, btnFalse;
    private int currentQuestionIndex = 0;
    private ArrayList<Question> questionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCounter = findViewById(R.id.tvCounter);
        tvStatement = findViewById(R.id.tvStatement);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnTrue = findViewById(R.id.btnTrue);
        btnFalse = findViewById(R.id.btnFalse);

        btnPrev.setOnClickListener(MainActivity.this);
        btnNext.setOnClickListener(MainActivity.this);
        btnTrue.setOnClickListener(MainActivity.this);
        btnFalse.setOnClickListener(MainActivity.this);


        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                Log.d("MAIN2", "processFinished: " +questionArrayList);
                updateStatement();
            }
        });

        Log.d("MAIN1", "onCreate: " + questionList);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPrev:
                if (currentQuestionIndex > 0){
                    currentQuestionIndex--;
                }
                updateStatement();
                break;
            case R.id.btnNext:
                if(currentQuestionIndex + 1 < questionList.size()){
                    currentQuestionIndex++;
                }
                updateStatement();
                break;
            case R.id.btnTrue:
                checkAnswer(true);
                updateStatement();
                break;
            case R.id.btnFalse:
                checkAnswer(false);
                updateStatement();
                break;
        }

    }

    private void checkAnswer(boolean userResponse) {
        int toastID = 0;
        if (userResponse == questionList.get(currentQuestionIndex).isAnswer()) {
            fadeView();
            toastID = R.string.answer_correct;
        }else{
            shakeAnimation();
            toastID = R.string.answer_wrong;
        }
        Toast.makeText(MainActivity.this,toastID,Toast.LENGTH_SHORT).show();
            
    }

    private void updateStatement() {
        tvStatement.setText(questionList.get(currentQuestionIndex).getStatement());
        tvCounter.setText((currentQuestionIndex + 1) + " of " + (questionList.size()+1));
    }

    private void fadeView(){
        final CardView cardView = findViewById(R.id.cvStatement);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(AlphaAnimation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,
                            R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cvStatement);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}