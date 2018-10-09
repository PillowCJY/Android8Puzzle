package com.example.austin.fun8puzzle;

import android.app.Activity;
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

    private void setListener(){

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLevel = DifficultyLevel.easy;
            }
        });

        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLevel = DifficultyLevel.normal;
            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLevel = DifficultyLevel.hard;
            }
        });
    }
}

