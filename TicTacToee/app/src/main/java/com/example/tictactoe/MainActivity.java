package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView player1Score, player2Score, gameStatus;
    private Button [] buttons = new Button[9];
    private Button resetGame;

    private int score1, score2, rountCount;
    boolean turnX;

    // p1 => 0
    // p2 => 1
    // empty => 2
    int [] gameState = {2,2,2,2,2,2,2,2,2};

    int [][] winningPositions = {
            {0,1,2}, {3,4,5}, {6,7,8}, // row
            {0,3,6}, {1,4,7}, {2,5,8}, // column
            {0,4,8}, {2,4,6}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player1Score = findViewById(R.id.score_1);
        player2Score = findViewById(R.id.score_2);
        gameStatus = findViewById(R.id.textView5);
        gameStatus.setText("Tic Tac Toe");
        resetGame = findViewById(R.id.resetBtn);

        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rountCount = 0;
                score1 = 0;
                score2 = 0;
                turnX = true;
                player1Score.setText("0");
                player2Score.setText("0");
                gameStatus.setText("Tic Tac Toe");
                playAgain();
            }
        });


        rountCount = 0;
        score1 = 0;
        score2 = 0;
        turnX = true;



        for (int i=1; i<=buttons.length; i++) {
            String btnId = "button" + i;
            int resourceID = getResources().getIdentifier(btnId, "id", getPackageName());
            buttons[i-1] = findViewById(resourceID);
            buttons[i-1].setOnClickListener(this);
            Log.i("BUTTON", "BUTTON " + i + ": " + Integer.toString(resourceID)  + " \n");
        }

    }


    @Override
    public void onClick(View view) {
        if (!((Button) view).getText().toString().equals("")) {
            return;
        }

        String btnID = view.getResources().getResourceEntryName(view.getId());
        int gameStatePointer = Integer.parseInt(btnID.substring(btnID.length()-1, btnID.length())) - 1;

        if (turnX) {
            ((Button) view).setText("X");
            ((Button) view).setTextColor(Color.parseColor("#FFC34A"));
            gameState[gameStatePointer] = 0;
        } else {
            ((Button) view).setText("O");
            ((Button) view).setTextColor(Color.parseColor("#70FFEA"));
            gameState[gameStatePointer] = 1;
        }
        rountCount++;

        if (checkWinner()) {
            if (turnX) {
                score1++;
                updatePlayerScore();
                Toast.makeText(this, "Player One Won", Toast.LENGTH_LONG).show();
                playAgain();
            } else {
                score2++;
                updatePlayerScore();
                Toast.makeText(this, "Player Two Won", Toast.LENGTH_LONG).show();
                playAgain();
            }
        } else if (rountCount == 9) {
            playAgain();
            Toast.makeText(this, "Draw", Toast.LENGTH_LONG).show();
        } else {
            turnX = !turnX;
        }

        if (score1 > score2) {
            gameStatus.setText("Player 1 is winning");
        } else if (score1 < score2) {
            gameStatus.setText("Player 2 is winning");
        } else {
            gameStatus.setText("Draw");
        }

    }

    private boolean checkWinner() {
        boolean winnerResult = false;

        for (int [] winningPosition: winningPositions) {
            if (gameState[winningPosition[0]] == gameState[winningPosition[1]]
                    && gameState[winningPosition[1]] == gameState[winningPosition[2]]
                    && gameState[winningPosition[0]] != 2) {
                winnerResult = true;
            }
        }
        return winnerResult;
    }

    public void updatePlayerScore() {
        player1Score.setText(Integer.toString(score1));
        player2Score.setText(Integer.toString(score2));

    }

    public void playAgain() {
        rountCount = 0;
        turnX = true;

        for (int i = 0; i<buttons.length;i++) {
            gameState[i] = 2;
            buttons[i].setText("");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadDataInfo();

    }

    private void loadDataInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPreferences", Activity.MODE_PRIVATE);

        if (sharedPreferences != null) {
            if (sharedPreferences.contains("score1")) {
                String s1 = sharedPreferences.getString("score1", "11");
                player1Score = findViewById(R.id.score_1);
                player1Score.setText(s1);
            }
            if (sharedPreferences.contains("score2")) {
                String s2 = sharedPreferences.getString("score2", "22");
                player2Score = findViewById(R.id.score_2);
                player2Score.setText(s2);
            }
            if (sharedPreferences.contains("roundCount")) {
                rountCount = Integer.parseInt(sharedPreferences.getString("roundCount", "7"));
            }
            if (sharedPreferences.contains("turnX")) {
                turnX = Boolean.parseBoolean(sharedPreferences.getString("turnX", "false"));
            }

            for (int i = 0; i<gameState.length;i++) {
                if (sharedPreferences.contains("cell" + i)) {
                    gameState[i] = Integer.parseInt(sharedPreferences.getString("cell" + i, Integer.toString(gameState[i])));
                    int value = gameState[i];
                    switch (value) {
                        case 2:
                            buttons[i].setText("");
                            break;
                        case 0:
                            buttons[i].setText("X");
                            buttons[i].setTextColor(Color.parseColor("#FFC34A"));
                            break;
                        case 1:
                            buttons[i].setText("O");
                            buttons[i].setTextColor(Color.parseColor("#70FFEA"));
                            break;
                    }
                }
            }


        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        saveDataInfo();
    }

    private void saveDataInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPreferences", Activity.MODE_PRIVATE);

        //pair <key,value> to be stored represents our 'important' data
        SharedPreferences.Editor preEditor = sharedPreferences.edit();

        preEditor.putString("score1", Integer.toString(score1));
        preEditor.putString("score2", Integer.toString(score2));
        preEditor.putString("turnX", Boolean.toString(turnX));
        preEditor.putString("roundCount", Integer.toString(rountCount));

        for (int i = 0; i<gameState.length;i++) {
            preEditor.putString("cell" + i, Integer.toString(gameState[i]));
        }

        preEditor.commit();
    }






}