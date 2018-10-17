package com.example.austin.fun8puzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 *  This activity is the launcher and the main activity which enable users to select difficulty
 *  @author Junyi Chen
 */
public class Difficulty extends Activity {


    Button easyButton; //Easy Level
    Button normalButton; //Normal Level
    Button hardButton; //Hard Level

    //Enum type
    enum DifficultyLevel{
        easy, normal, hard
    }

    DifficultyLevel selectedLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.difficulty);
        easyButton = (Button) findViewById(R.id.easyButton);
        normalButton = (Button) findViewById(R.id.normalButton);
        hardButton = (Button) findViewById(R.id.hardButton);
        setListener();
    }

    //Setting listeners to different button
    private void setListener(){

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLevel = DifficultyLevel.easy;
                startGame();
            }
        });

        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLevel = DifficultyLevel.normal;
                startGame();
            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLevel = DifficultyLevel.hard;
                startGame();
            }
        });
    }

    //Start the game
    private void startGame(){
        //Create a intent which will start the game activity
        Intent it = new Intent(this, MainActivity.class);
        //passing difficulty selected by user to the game activity which the game state will be initialised according to this level
        it.putExtra("difficulty", selectedLevel);
        startActivity(it);
    }
}

