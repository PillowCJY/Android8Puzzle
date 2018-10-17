package com.example.austin.fun8puzzle;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

/**
 *  My own adapter used for game board gridview
 *  @author Junyi Chen
 */
public class MyAdapter extends BaseAdapter {

    private Puzzle myPuzzle; //game state
    private GridView tiles; //gridview, used to calculate the length of the game board
    //color sets
    private String[] color = {"#bf80ff", "#cc33ff", "#ff66cc",
                              "#00e6e6", "#00cc99", "#29a3a3",
                              "#ff3333", "#ff3300", "#ff6600"};

    private Activity myActivity; //game activity
    private Button tileButton; //each button inside gridview

    //constructor
    public MyAdapter(Puzzle puzzle, Activity act, GridView board){
        myPuzzle = puzzle;
        myActivity = act;
        tiles = board;
    }

    @Override
    public int getCount() {
        // 3 * 3 grid
        return 9;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = myActivity.getLayoutInflater().inflate(R.layout.tile, parent, false);
        }
        //set the game board to square
        convertView.setMinimumHeight(tiles.getWidth() / 3);
        tileButton = (Button)convertView.findViewById(R.id.tileButton);
        tileButton.setBackgroundColor(Color.parseColor(color[position]));
        int number = getNumber(position);
        if(number != 0) {
            tileButton.setText(Integer.toString(number));
            tileButton.setTextSize(28);
        }
        return convertView;
    }

    //get the number for each tile according to the index
    public int getNumber(int position){
        if(position <= 2){
            return myPuzzle.gameBoard[0][position];
        } else if (position > 2 && position <= 5){
            return myPuzzle.gameBoard[1][position-3];
        } else {
            return myPuzzle.gameBoard[2][position-6];
        }
    }
}
