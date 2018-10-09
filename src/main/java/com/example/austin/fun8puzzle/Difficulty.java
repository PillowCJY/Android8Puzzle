package com.example.austin.fun8puzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Difficulty extends Activity {


    Button easyButton;
    Button normalButton;
    Button hardButton;

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
        Intent it = new Intent(this, MainActivity.class);
        it.putExtra("difficulty", selectedLevel);
        startActivity(it);
    }
}

