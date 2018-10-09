package com.example.austin.fun8puzzle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;

public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

    private MainActivity myActivity;
    private Puzzle gameState;
    private GridView myGameBoard;

    public MyGestureListener(MainActivity act, Puzzle state, GridView grid){
        myActivity = act;
        gameState = state;
        myGameBoard = grid;
    }

    @Override
    public boolean onDown(MotionEvent e1){
        return true;
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){

        if(e1.getY() - e2.getY() > 50){
            //moving up
            if(gameState.canMoveUp()){
                gameState = gameState.moveUp();
            }
            return true;
        } else if (e2.getY() - e1.getY() > 50){
            //moving down
            if(gameState.canMoveDown()){
                gameState = gameState.moveDown();
            }
            return true;
        } else if (e1.getX() - e2.getX() > 50){
            //moving left
            if(gameState.canMoveLeft()){
                gameState = gameState.moveLeft();
            }
            return true;
        } else if (e2.getX() - e1.getX() > 50){
            //moving right
            if(gameState.canMoveRigh()){
                gameState = gameState.moveRight();
            }
            return true;
        }
        myActivity.setAdapter(new MyAdapter(gameState, myActivity, myGameBoard));
        return false;
    }
}
