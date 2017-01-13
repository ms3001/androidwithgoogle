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

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private int wordLength = DEFAULT_WORD_LENGTH;

    private List<String> wordList = new ArrayList<>(); //AL that will contain given dict.
    private HashSet<String> wordSet = new HashSet<>(); //HS that contains words only once
    private HashMap<String, List<String>> lettersToWord = new HashMap<>(); //HM alpha order to word
    private HashMap<Integer, List<String>> sizeToWord = new HashMap<>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word); //add the word we just read to wordList
            wordSet.add(word); //add the word to the wordSet
            String key = sortLetters(word); //find key of word
            if (lettersToWord.containsKey(key)) { //key already exists
                List<String> val = lettersToWord.get(key); //use existing AL
                val.add(word);
                lettersToWord.put(key, val); //add the word to letterToWord
            } else {                              //key does not exist
                List<String> val = new ArrayList<String>(); //create new AL
                val.add(word);
                lettersToWord.put(key, val);
            }
            int size = word.length(); //find size of word
            if (sizeToWord.containsKey(size)) { //key already exists
                List<String> val = sizeToWord.get(size); //use existing AL
                val.add(word);
                sizeToWord.put(size,val); //add the word to sizeToWord
            } else {                              //key does not exist
                List<String> val = new ArrayList<String>(); //create new AL
                val.add(word);
                sizeToWord.put(size, val);
            }
        }

        in.close(); //Close reader
    }

    public boolean isGoodWord(String word, String base) {
        if (wordSet.contains(word)) { //word is in dict
            if (word.contains(base)) { //word contains base
                return false;
            }
            return true;
        }
        return false;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        int len = targetWord.length();
        String sorted = sortLetters(targetWord);
        for (int i = 0; i < wordList.size(); i++) { //check if each word in dict. is anagram
            if(wordList.get(i).length() == len) { //if same length
                if(sorted.equals(sortLetters(wordList.get(i)))) {
                    result.add(wordList.get(i));
                }
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> alpha = new ArrayList<String>(Arrays.asList("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"));
        for (int i = 0; i < alpha.size(); i++) { //for each letter of the alphabet
            String key = sortLetters(word.concat(alpha.get(i))); //add the letter to the word
            List<String> eachLetter = lettersToWord.get(key);
            if (eachLetter != null) {
                for (int j = 0; j < eachLetter.size(); j++) {
                    if (isGoodWord(eachLetter.get(j), word)) { //if good word, then add
                        result.add(eachLetter.get(j));
                    }
                }
            }
        }
        return result;
    }

    public String sortLetters(String word) { //function to sort string chars alphabetically
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public String pickGoodStarterWord() {
        List<String> myList = sizeToWord.get(wordLength);
        int i = random.nextInt(myList.size());
        String interestWord = myList.get(i);
        int numAnagrams = getAnagramsWithOneMoreLetter(interestWord).size();
        while (numAnagrams < MIN_NUM_ANAGRAMS) { //while not enough anagrams
            i++;
            interestWord = myList.get(i);
            numAnagrams = getAnagramsWithOneMoreLetter(interestWord).size();
        }
        if (wordLength == MAX_WORD_LENGTH) {
            //do nothing
        }
        wordLength++;
        return interestWord;


    }
}