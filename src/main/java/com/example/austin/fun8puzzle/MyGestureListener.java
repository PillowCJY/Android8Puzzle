package com.example.austin.fun8puzzle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 *  My own gesturelistener which is used to detect user swiping
 *  @author Junyi Chen
 */
public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

    private MainActivity myActivity;//game activity
    private Puzzle gameState; //current game state
    private GridView myGameBoard; //game board within game activity

    //constructor
    public MyGestureListener(MainActivity act, Puzzle state, GridView grid){
        myActivity = act;
        gameState = state;
        myGameBoard = grid;
    }

    @Override
    public boolean onDown(MotionEvent e){
        return true;
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){


        if(myActivity.isGameStarted()==false){
            myActivity.startTime();
        }

        gameState = myActivity.getGameState();

        if(e1.getY() - e2.getY() > 120){
            //moving up
            if(gameState.canMoveUp()){
                gameState = gameState.moveUp();
            }

        } else if (e2.getY() - e1.getY() > 120){
            //moving down
            if(gameState.canMoveDown()){
                gameState = gameState.moveDown();
            }

        } else if (e1.getX() - e2.getX() > 120){
            //moving left
            if(gameState.canMoveLeft()){
                gameState = gameState.moveLeft();
            }

        } else if (e2.getX() - e1.getX() > 120){
            //moving right
            if(gameState.canMoveRigh()){
                gameState = gameState.moveRight();
            }

        }
        //set the new state back to activity
        myActivity.setGameState(gameState);
        //redraw the game board
        myActivity.setAdapter(new MyAdapter(gameState, myActivity, myGameBoard));
        //increase the step by one
        myActivity.stepIncrease();
        //check if goal matching
        if(gameState.goalMatch()){
            myActivity.stopTime();
            myActivity.goalMatched();
        }
        return true;
    }
}
