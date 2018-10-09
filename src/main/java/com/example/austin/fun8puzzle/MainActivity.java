package com.example.austin.fun8puzzle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

public class MainActivity extends Activity {


    private Puzzle myPuzzle;
    private Puzzle gameState;
    private Algorithm algorithm;

    private GridView myGameBoard;



    private Button restartButton;
    private Button hintButton;
    private Button answerButton;

    private String solution;

    private GestureDetector gestureDetector;

    final String[] easyLevel = new String[]{
            "152043786"
    };

    final String[] normalLevel = new String[]{
            "152743860"
    };

    final String[] hardLevel = new String[]{
            "145802367"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myGameBoard = findViewById(R.id.gameBoard);
        init();

        restartButton = (Button) findViewById(R.id.restartButton);
        hintButton = (Button) findViewById(R.id.hintButton);
        answerButton = (Button) findViewById(R.id.answerButton);
        MyGestureListener listener = new MyGestureListener(this, gameState, myGameBoard);
        gestureDetector = new GestureDetector(this, listener);
        setListeners(this);
    }


    public void init(){
        Intent myIntent = getIntent();
        Difficulty.DifficultyLevel difficultyLevel = (Difficulty.DifficultyLevel) myIntent.getSerializableExtra("difficulty");
        String initialState = "";
        switch (difficultyLevel){
            case easy:
                initialState = easyLevel[0];
                break;
            case normal:
                initialState = normalLevel[0];
                break;
            case hard:
                initialState = hardLevel[0];
                break;
        }
        myPuzzle = new Puzzle(initialState);
        gameState = myPuzzle;
        setAdapter(new MyAdapter(myPuzzle, this, myGameBoard));
    }

    private void setListeners(final Activity act){
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                algorithm = new Algorithm(gameState);
                solution = algorithm.getSolution();
                if(!solution.equals("")) {
                    getNextPuzzle();
                    setAdapter(new MyAdapter(gameState, act, myGameBoard));
                }
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartButton.setEnabled(false);
                hintButton.setEnabled(false);
                answerButton.setEnabled(false);
                final Handler handler = new Handler();
                algorithm = new Algorithm(gameState);
                solution = algorithm.getSolution();
                handleAnswerButton(handler, act);
            }
        });

    }

    //Recursive function to handle answer button which will animate the solution
    public void handleAnswerButton(final Handler handler, final Activity act){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!solution.equals("")) {
                    getNextPuzzle();
                    setAdapter(new MyAdapter(gameState, act, myGameBoard));
                    handleAnswerButton(handler, act);
                }
                else {
                    restartButton.setEnabled(true);
                    hintButton.setEnabled(true);
                    answerButton.setEnabled(true);
                }
            }
        }, 500);
    }

    public void setAdapter(MyAdapter adp){
        myGameBoard.setAdapter(adp);
    }

    private void getNextPuzzle(){
        Character chr = solution.charAt(0);
        solution = solution.substring(1);
        switch(chr){
            case 'U':
                gameState = gameState.moveUp();
                break;
            case 'D':
                gameState = gameState.moveDown();
                break;
            case 'L':
                gameState = gameState.moveLeft();
                break;
            case 'R':
                gameState = gameState.moveRight();
                break;
            default:
                break;
        }
    }
}
