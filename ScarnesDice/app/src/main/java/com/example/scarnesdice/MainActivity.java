package com.example.scarnesdice;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.util.Random;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.drawable.TransitionDrawable;
import android.graphics.drawable.Drawable;


public class MainActivity extends AppCompatActivity {

    private static final int NUM_DICE_FACES = 6;
    private static final int WIN_SCORE = 50;

    private int userScore;                  //global variables for scores used in game
    private int userTurnScore;
    private int compScore;
    private int compTurnScore;
    private Random random = new Random();
    private Handler handler = new Handler();
    private int [] imgs;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            userScore = 0;
            userTurnScore = 0;
            compScore = 0;
            compTurnScore = 0;
            imgs = new int[]{R.drawable.dice0, R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};
        }

    /**
     * Causes a delay when computer plays the game.
     */
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {

            if (compScore + compTurnScore >= WIN_SCORE) {
                handler.postDelayed(timerRunnable2, 1000);
                handler.removeCallbacks(timerRunnable);
                return;
            }

            if (compTurnScore < 20) {
                handler.postDelayed(this, 1000);
                computerTurn();
            } else {
                endComputerTurn();
            }
        }
    };

    /**
     * Called by postDelay when computer turn ends
     */
    Runnable timerRunnable2 = new Runnable() {
        @Override
        public void run() {
            endComputerTurn();
        }
    };



    /**
     * Called when user clicks on 'roll' button.
     * @param view the view that was clicked
     */
    public void Roll(View view) {
        int roll = RollTheDice();

        if (roll > 1) {                     //turnScore updated
            userTurnScore += roll;
            updateScore(userTurnScore);
        } else {                            //turn ends when a 1 is rolled
            userTurnScore = 0;
            updateScore(userTurnScore);
            startComputerTurn();
        }
    }

    /**
     * Called when user clicks on 'hold' button.
     * @param view the view that was clicked
     */
    public void Hold(View view) {
        userScore += userTurnScore;         //userScore increased
        userTurnScore = 0;                  //turnScore set to 0
        updateScore(0);
        if (!checkForWin()) {               //if user did not win after hold, computer's turn starts
            startComputerTurn();
        }
    }

    /**
     * Called when user clicks on 'reset' button.
     * Resets game state to starting values (0 for all)
     * @param view the view that was clicked
     */
    public void Reset(View view) {
        userScore = 0; userTurnScore = 0; compScore = 0; compTurnScore = 0;
        handler.removeCallbacks(timerRunnable);
        handler.removeCallbacks(timerRunnable2);
        updateScore(userTurnScore);
        Button one = (Button) findViewById(R.id.button1);
        Button two = (Button) findViewById(R.id.button2);
        one.setClickable(true);             //enable these buttons while user plays
        two.setClickable(true);
        one.setText("Roll");
        two.setText("Hold");
        ImageView imageView = (ImageView) findViewById(R.id.imageView1);
        imageView.setImageResource(R.drawable.dice1);
        TextView textView = (TextView) findViewById(R.id.textView3);
        textView.setVisibility(TextView.INVISIBLE);
    }

    /**
     * Function to check if either the user of the computer has won the game.
     * @return true if the user has won the game, else false
     */
    public boolean checkForWin() {
        TextView textView = (TextView) findViewById(R.id.textView3);
        if (userScore >= WIN_SCORE) {
            textView.setText("User Wins!");
            textView.setVisibility(TextView.VISIBLE);
            Button one = (Button) findViewById(R.id.button1);
            Button two = (Button) findViewById(R.id.button2);
            one.setClickable(false);            //disable these buttons while computer plays
            two.setClickable(false);
            one.setText("Game");
            two.setText("over");
            return true;
        } else if (compScore >= WIN_SCORE) {
            textView.setText("Computer Wins!");
            textView.setVisibility(TextView.VISIBLE);
            Button one = (Button) findViewById(R.id.button1);
            Button two = (Button) findViewById(R.id.button2);
            one.setClickable(false);            //disable these buttons while computer plays
            two.setClickable(false);
            one.setText("Game");
            two.setText("over");
        }
        return false;
    }

    /**
     * Helper function to roll dice and display output.
     * @return the dice roll value
     */
    public int RollTheDice() {
        int diceRoll = random.nextInt(NUM_DICE_FACES) + 1;
        TransitionDrawable td = new TransitionDrawable(new Drawable[] {getResources().getDrawable(imgs[0]),getResources().getDrawable(imgs[diceRoll])});
        ImageView imageView = (ImageView) findViewById(R.id.imageView1);
        imageView.setImageDrawable(td);
        td.startTransition(100);
        return diceRoll;
    }

    /**
     * Function to edit textViews to display appropriate scores.
     * @param c the temporary sum
     */
    public void updateScore(int c) {
        String first = "Your score: " + Integer.toString(userScore) +
                " Computer score: " + Integer.toString(compScore);
        String second = "Hold value: " + Integer.toString(c);
        TextView score = (TextView) findViewById(R.id.textView1);
        TextView tempScore = (TextView) findViewById(R.id.textView2);
        score.setText(first);
        tempScore.setText(second);
    }

    /**
     * Function to handle logic when a computer plays.
     */
    public void computerTurn() {
        int diceRoll = RollTheDice();
        if (diceRoll > 1) {
            compTurnScore += diceRoll;
            updateScore(compTurnScore);
        } else {                            //rolled a 1, turn over for computer
            compTurnScore = 0;
            endComputerTurn();
        }
    }

    /**
     * Function to do some housekeeping before computer turn.
     */
    public void startComputerTurn() {
        Button one = (Button) findViewById(R.id.button1);
        Button two = (Button) findViewById(R.id.button2);
        one.setClickable(false);            //disable these buttons while computer plays
        two.setClickable(false);
        one.setText("Computer");
        two.setText("turn");
        handler.postDelayed(timerRunnable,1500);
    }


    /**
     * Function to do some housekeeping after the computer turn.
     */
    public void endComputerTurn() {
        Button one = (Button) findViewById(R.id.button1);
        Button two = (Button) findViewById(R.id.button2);
        one.setClickable(true);             //enable these buttons while user plays
        two.setClickable(true);
        one.setText("Roll");
        two.setText("Hold");
        handler.removeCallbacks(timerRunnable);
        handler.removeCallbacks(timerRunnable2);
        compScore += compTurnScore;
        compTurnScore = 0;
        updateScore(compTurnScore);
        checkForWin();
    }
}
