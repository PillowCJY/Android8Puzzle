package com.example.austin.fun8puzzle;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Button;

public class MyAdapter extends BaseAdapter {

    private Puzzle myPuzzle;

    private GridView tiles;
    private String[] color = {"#bf80ff", "#cc33ff", "#ff66cc",
                              "#00e6e6", "#00cc99", "#29a3a3",
                              "#ff3333", "#ff3300", "#ff6600"};

    private Activity myActivity;
    private Button tileButton;

    public MyAdapter(Puzzle puzzle, Activity act, GridView board){
        myPuzzle = puzzle;
        myActivity = act;
        tiles = board;
    }

    @Override
    public int getCount() {
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
