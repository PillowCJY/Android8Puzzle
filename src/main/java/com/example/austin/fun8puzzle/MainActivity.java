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
 * This is a puzzle game made by
 * @author Junyi Chen
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
        //instantiate my own gesture listener and set it to my adapter view
        MyGestureListener listener = new MyGestureListener(this, gameState, myGameBoard);
        myGameBoard.setGestureListener(listener);
        setListeners(this);
    }

    //This method will initialise the intial state according to the level user selected.
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
        //Instaintating new instance of Puzzle object.
        gameState = new Puzzle(initialState);
        setAdapter(new MyAdapter(gameState, this, myGameBoard));
    }

    //this method will set listeners to all views except gridview
    private void setListeners(final Activity act){

        //setting listener to hint button
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //instantiating a new algorithm object, pass the current game state to it and get the solution
                algorithm = new Algorithm(gameState);
                solution = algorithm.getSolution();

                //has solution
                if(!solution.equals("")) {
                    if(!isGameStarted()){
                        //game isnt started, start counting up the time
                        startTime();
                    }
                    //get the next state according to the solution
                    getNextPuzzle();
                    //redraw the gameboard
                    setAdapter(new MyAdapter(gameState, act, myGameBoard));
                    //increase the step by one
                    stepIncrease();
                } else{
                    //goal matched, stop counting the time and show user the time used.
                    stopTime();
                    goalMatched();
                }
            }
        });

        //setting listener to restart button
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //simple finish the current activity, which will go back to the select level activity
                finish();
            }
        });

        //setting listener to answer button
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //disable all buttons as animation will be running
                restartButton.setEnabled(false);
                hintButton.setEnabled(false);
                answerButton.setEnabled(false);
                //handler to pose delay
                final Handler handler = new Handler();
                //getting solution
                algorithm = new Algorithm(gameState);
                solution = algorithm.getSolution();
                //handle the animation
                handleAnswerButton(handler, act);
            }
        });
    }

    //Recursive function to handle answer button which will animate the solution
    public void handleAnswerButton(final Handler handler, final Activity act){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //while the puzzle is not solved
                if(!solution.equals("")) {
                    //get next state according to the solution
                    getNextPuzzle();
                    //redraw the gameboard
                    setAdapter(new MyAdapter(gameState, act, myGameBoard));
                    //increase the step
                    stepIncrease();
                    //recursive called until the solution is finished
                    handleAnswerButton(handler, act);
                }
                //puzzle is solved
                else {
                    //stop timing
                    stopTime();
                    goalMatched();
                    //enable restart button
                    restartButton.setEnabled(true);
                }
            }
        }, 500);
    }

    //setter
    public void setGameState(Puzzle puzzle){
        gameState = puzzle;
    }

    //getter
    public Puzzle getGameState(){
        return gameState;
    }

    //this method will increase the steps and display it
    public void stepIncrease(){
        steps++;
        stepsCount.setText(Integer.toString(steps));
    }

    //this method will set a new adpater to the gridview
    public void setAdapter(MyAdapter adp){
        myGameBoard.setAdapter(adp);
    }

    //this method will set game state to next best move according to the solution
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

    //start timing
    public void startTime(){
        isStarted = true;
        timeCount.setBase(SystemClock.elapsedRealtime());
        timeCount.start();
    }

    //stop timing
    public void stopTime(){
        isStarted = false;
        timeUsed = SystemClock.elapsedRealtime() - timeCount.getBase();
        timeCount.stop();

    }

    //getter
    public boolean isGameStarted(){
        return isStarted;
    }

    //this method will be called when the game is finished(goal matched)
    //it will calculate the time used to solve the puzzle and display it via alertdialog
    public void goalMatched(){
        hintButton.setEnabled(false);
        answerButton.setEnabled(false);
        long second = timeUsed / 1000;
        long mins = (timeUsed / 1000) / 60;
        if(builder == null) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Puzzle Solved!");
            builder.setCancelable(true);
            builder.setPositiveButton("GOOD JOB", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
        builder.setMessage("You solved the puzzle in\n" + mins + " mins:" + second + " sec" );
        builder.show();
    }

}
