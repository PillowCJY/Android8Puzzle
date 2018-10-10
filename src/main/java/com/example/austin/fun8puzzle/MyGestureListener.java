package com.example.austin.fun8puzzle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;
import android.util.Log;
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
    public boolean onDown(MotionEvent e){
        return true;
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){



        if(e1.getY() - e2.getY() > 120){
            //moving up
            Log.i("getting event e1", e1.getX() + " " + e1.getY());
            Log.i("getting event e2", e2.getX() + " " + e2.getY());
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
        myActivity.setGameState(gameState);
        myActivity.setAdapter(new MyAdapter(gameState, myActivity, myGameBoard));
        return true;
    }
}
