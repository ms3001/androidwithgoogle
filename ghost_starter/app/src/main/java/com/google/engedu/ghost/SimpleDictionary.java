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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.TreeSet;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private Random random = new Random();


    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if (prefix.equals("")) {                        //If prefix empty return random word
            return words.get(random.nextInt(words.size()));
        }                                               //Else search for a word

        int low = 0;
        int high = words.size() - 1;

        while (high >= low) {
            int index = (low + high) / 2;
            if (prefix.compareTo(words.get(index)) < 0) { //word at index is closer to z
                if(words.get(index).length() > prefix.length()) {
                    if(words.get(index).substring(0,prefix.length()).equals(prefix)) {
                        return words.get(index);
                    }
                }
                high = index - 1;
            } else if (prefix.compareTo(words.get(index)) > 0) { //word at index is closer to a
                low = index + 1;
            } else {                                       //the two are the same
                low++;
            }
        }

        return null;
    }



    @Override
    public String getGoodWordStartingWith(String prefix) {
        if (prefix.equals("")) {                        //If prefix empty return random word
            return words.get(random.nextInt(words.size()));
        }

        String selected = null;
        TreeSet<String> output = new TreeSet<>();

        int low = 0;
        int high = words.size() - 1;

        while (high >= low) {
            int index = (low + high) / 2;
            if (prefix.compareTo(words.get(index)) < 0) { //word at index is closer to z
                if(words.get(index).length() > prefix.length()) {
                    if(words.get(index).substring(0,prefix.length()).equals(prefix)) {
                        output.add(words.get(index));
                        low++;
                    } else {
                        high = index - 1;
                    }
                } else {
                    high = index - 1;
                }
            } else if (prefix.compareTo(words.get(index)) > 0) { //word at index is closer to a
                low = index + 1;
            } else {                                       //the two are the same
                low++;
            }
        }

        if (output.size() != 0) {               //if we found some words
            ArrayList<String> goodOnes = new ArrayList<>();
            ArrayList<String> badOnes = new ArrayList<>();

            for (int i=0; i<output.size(); i++) {
                String temp = output.pollFirst();
                if ((temp.length() - prefix.length()) % 2 == 0) {
                    goodOnes.add(temp);
                } else {
                    badOnes.add(temp);
                }
            }
            if (goodOnes.size() != 0) {
                selected = goodOnes.get(random.nextInt(goodOnes.size()));
            } else {
                selected = badOnes.get(random.nextInt(badOnes.size()));
            }
        }

        return selected;
    }
}
