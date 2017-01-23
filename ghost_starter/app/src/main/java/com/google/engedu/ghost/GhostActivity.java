/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private static final String FRAG_IS_WORD = "You lose, you entered a word";
    private static final String FRAG_IS_WORD_COMP = "You win, comp entered a word";
    private static final String NO_OTHER_WORDS = "You lose, you entered a bad prefix";
    private static final String OTHER_WORD = "You lose, challenge failed";

    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private String fragment = "";
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            dictionary = new SimpleDictionary(getAssets().open("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        fragment = "";
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    public void challenge(View view) {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView word = (TextView) findViewById(R.id.ghostText);
        if (dictionary.isWord(fragment)) {
            if(fragment.length() > 3) {
                label.setText(FRAG_IS_WORD_COMP);
                return;
            }
        } else {
            word.setText(dictionary.getAnyWordStartingWith(fragment));
            label.setText(OTHER_WORD);
        }
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        label.setText(COMPUTER_TURN);
        if (fragment.length() > 3) {
            if (dictionary.isWord(fragment)) {      //If the fragment is a word, call it out
                label.setText(FRAG_IS_WORD);
                return;
            }
        }
                                                    //Else, look for a longer word
        String guess = dictionary.getAnyWordStartingWith(fragment);
        if (guess == null) {                        //If no longer words exist, call it out
            label.setText(NO_OTHER_WORDS);
            return;
        } else {                                    //Else, guess fragment + one more letter
            int len = fragment.length();
            updateFragment(guess.substring(0, len + 1));
        }
        userTurn = true;
        label.setText(USER_TURN);
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode >= 29 && keyCode <= 54) {
            updateFragment(fragment + (char) (keyCode + 68));
            userTurn = false;
            computerTurn();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * Function to update the fragment shown on the UI.
     * @param newFrag the new fragment to be shown
     */
    public void updateFragment(String newFrag) {
        fragment = newFrag;
        TextView view = (TextView) findViewById(R.id.ghostText);
        view.setText(fragment);
    }
}
