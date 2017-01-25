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

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        if (s.length() == 1) {
            if (this.children.containsKey(s)) {
                TrieNode child = this.children.get(s);
                child.isWord = true;
            } else {
                TrieNode child = new TrieNode();
                child.isWord = true;
                this.children.put(s, child);
            }
        } else {
            if (this.children.containsKey(s.substring(0,1))) {
                TrieNode child = this.children.get(s.substring(0,1));
                child.add(s.substring(1));
                this.children.put(s.substring(0,1), child);
            } else {
                TrieNode child = new TrieNode();
                child.add(s.substring(1));
                this.children.put(s.substring(0,1), child);
            }
        }
    }

    public boolean isWord(String s) {
        if (s.length() == 1) {
            if (this.children.containsKey(s)) {
                return this.children.get(s).isWord;
            } else {
                return false;
            }
        } else {
            if (this.children.containsKey(s.substring(0,1))) {
                return this.children.get(s.substring(0,1)).isWord(s.substring(1));
            } else {
                return false;
            }
        }
    }

    public String getAnyWordStartingWith(String s) {

        String out = findWordRecursive(this, s, 0);
        System.out.println(s+out);
        if (out == null) {
            return null;
        } else {
            return s + out;
        }

    }

    private String findWordRecursive(TrieNode curr, String s, int index) {
        String chosen = "";
        if (index < s.length()) {      //if looking for frag parts

            if (curr.children.containsKey(s.substring(index,index+1))) {
                curr = curr.children.get(s.substring(index,index+1));
                index++;

            } else {
                return null;
            }

        } else {                        //traversing tree
            if (curr.children.isEmpty()) {
                return "";
            } else {
                Random rand = new Random();
                Object [] possiblePaths = curr.children.keySet().toArray();
                chosen = (String) possiblePaths[rand.nextInt(possiblePaths.length)];
                curr = curr.children.get(chosen);
            }
        }

        String part2 = findWordRecursive(curr, s, index);
        if (part2 == null) {
            return null;
        } else {
            return chosen + part2;
        }
    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }

    @Override
    public String toString() {
        return this.children.keySet() + this.children.toString();
    }
}
