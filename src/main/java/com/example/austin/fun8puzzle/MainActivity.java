package com.example.austin.fun8puzzle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import java.util.Random;

/**
 * This is a puzzle game made by Junyi Chen
 * the game board is a 3*3 grids. Each tile contains a unique number from 1-8 and a blank tile with nothing.
 * user will be swiping the screen to move the blank tile up down left right in order to solve the puzzle with order shown below
 *              [1 2 3]
 *              [4 5 6]
 *              [7 8  ]
 * the time will start counting once user first swipe the screen
 * steps will be counted
 * hint button is used to show user next move to the solution
 * answer button will animate the best solution(lowest steps count)
 * Users are allowed to select difficulty level, different level will contain different states.
 */
public class MainActivity extends Activity {


    //private Puzzle myPuzzle;
    private Puzzle gameState;//current state of the game
    private Algorithm algorithm; //Algorithm object which will implement A* algorithm to get solution

    private MyGridView myGameBoard; //Gridview used to gameboard

    private AlertDialog.Builder builder; //AlertDialog when the game is finished
    private long timeUsed;  //used to count how long it took to finished the game
    private int steps; //used to count how many steps user used

    private Button restartButton; //button will restart the game when click it
    private Button hintButton; //button will show user next move to the solution
    private Button answerButton; //button will animate the solution
    private Chronometer timeCount; //Chronometer used to count up the time
    private TextView stepsCount; //Textview used to show the steps used

    private boolean isStarted; //a boolean variable to check wether the game is started or not
    private String solution; //Solution, U - means move the blank tile U, D - move Down, L - move left R - move right

    private GestureDetector gestureDetector; //My own gesture detector for the game board swipe detection

    //Predefined initial states
    final String[] easyLevel = new String[]{
            "152043786", "162043758", "123760548", "235140786"
    };

    final String[] normalLevel = new String[]{
            "152743860", "264507183", "435210786", "325406718",
    };

    final String[] hardLevel = new String[]{
            "145802367", "210435678", "321654078", "218407635"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myGameBoard = findViewById(R.id.gameBoard);
        init();
        builder = new AlertDialog.Builder(this);
        restartButton = (Button) findViewById(R.id.restartButton);
        hintButton = (Button) findViewById(R.id.hintButton);
        answerButton = (Button) findViewById(R.id.answerButton);
        timeCount = (Chronometer)findViewById(R.id.timeCount);
        stepsCount = (TextView) findViewById(R.id.steps);
        isStarted = false;
        steps = 0;
        MyGestureListener listener = new MyGestureListener(this, gameState, myGameBoard);
        myGameBoard.setGestureListener(listener);
        setListeners(this);
    }


    public void init(){
        //Getting the difficulty selected by the user
        Intent myIntent = getIntent();
        Difficulty.DifficultyLevel difficultyLevel = (Difficulty.DifficultyLevel) myIntent.getSerializableExtra("difficulty");
        String initialState = "";
        //Get a random number
        Random rand = new Random();
        int index = rand.nextInt(4) ;
        switch (difficultyLevel){
            case easy:
                initialState = easyLevel[index];
                break;
            case normal:
                initialState = normalLevel[index];
                break;
            case hard:
                initialState = hardLevel[index];
                break;
        }
        gameState = new Puzzle(initialState);
        //gameState = myPuzzle;
        setAdapter(new MyAdapter(gameState, this, myGameBoard));
    }

    private void setListeners(final Activity act){


        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                algorithm = new Algorithm(gameState);
                solution = algorithm.getSolution();
                if(!solution.equals("")) {
                    if(!isGameStarted()){
                        startTime();
                    }
                    getNextPuzzle();
                    setAdapter(new MyAdapter(gameState, act, myGameBoard));
                    stepIncrease();
                } else{
                    stopTime();
                    goalMatched();
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
                setAdapter(new MyAdapter(gameState, act, myGameBoard));
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
                    stepIncrease();
                    handleAnswerButton(handler, act);
                }
                else {
                    stopTime();
                    goalMatched();
                    restartButton.setEnabled(true);
                }
            }
        }, 500);
    }

    public void setGameState(Puzzle puzzle){
        gameState = puzzle;
    }

    public void stepIncrease(){
        steps++;
        stepsCount.setText(Integer.toString(steps));
    }
    public Puzzle getGameState(){
        return gameState;
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

    public void startTime(){
        isStarted = true;
        timeCount.setBase(SystemClock.elapsedRealtime());
        timeCount.start();
    }

    public void stopTime(){
        isStarted = false;
        timeUsed = SystemClock.elapsedRealtime() - timeCount.getBase();
        timeCount.stop();

    }

    public boolean isGameStarted(){
        return isStarted;
    }

    public void goalMatched(){
        hintButton.setEnabled(false);
        answerButton.setEnabled(false);
        long second = timeUsed / 1000;
        long mins = (timeUsed / 1000) / 60;
        if(builder == null) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Puzzle Solved!");
            builder.setCancelable(true);
            builder.setPositiveButton("GOODJOB!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("GOODJOB!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
        builder.setMessage("You solved the puzzle in " + mins + " mins:" + second + " sec" );
        builder.show();
    }

}
