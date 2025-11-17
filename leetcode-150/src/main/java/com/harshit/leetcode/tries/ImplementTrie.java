package com.harshit.leetcode.tries;

import java.util.*;

/**
 * Problem: Implement Trie (Prefix Tree)
 * 
 * A trie (pronounced as "try") or prefix tree is a tree data structure used to efficiently
 * store and retrieve keys in a dataset of strings. There are various applications of this
 * data structure, such as autocomplete and spellchecker.
 * 
 * Implement the Trie class:
 * - Trie() Initializes the trie object.
 * - void insert(String word) Inserts the string word into the trie.
 * - boolean search(String word) Returns true if the string word is in the trie (i.e., was inserted before), and false otherwise.
 * - boolean startsWith(String prefix) Returns true if there is a previously inserted string word that has the prefix prefix, and false otherwise.
 * 
 * Example:
 * Input
 * ["Trie", "insert", "search", "search", "startsWith", "insert", "search"]
 * [[], ["apple"], ["apple"], ["app"], ["app"], ["app"], ["app"]]
 * Output
 * [null, null, true, false, true, null, true]
 * 
 * Explanation
 * Trie trie = new Trie();
 * trie.insert("apple");
 * trie.search("apple");   // return True
 * trie.search("app");     // return False
 * trie.startsWith("app"); // return True
 * trie.insert("app");
 * trie.search("app");     // return True
 * 
 * Constraints:
 * - 1 <= word.length, prefix.length <= 2000
 * - word and prefix consist only of lowercase English letters.
 * - At most 3 * 10^4 calls in total will be made to insert, search, and startsWith.
 */
public class ImplementTrie {
    
    /**
     * TrieNode class
     */
    public static class TrieNode {
        // Write your logic here
        // Hint: Each node should have:
        // - Array or HashMap of children (26 for lowercase letters)
        // - Boolean flag to mark end of word
    }
    
    /**
     * Trie class implementation
     */
    public static class Trie {
        private TrieNode root;
        
        public Trie() {
            // Write your logic here
            // Initialize root node
        }
        
        public void insert(String word) {
            // Write your logic here
            // Hint: Traverse/create path for each character
            // Mark last node as end of word
        }
        
        public boolean search(String word) {
            // Write your logic here
            // Hint: Traverse path, check if exists and is end of word
            return false;
        }
        
        public boolean startsWith(String prefix) {
            // Write your logic here
            // Hint: Traverse path, just check if path exists
            return false;
        }
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        Trie trie = new Trie();
        
        trie.insert("apple");
        System.out.println("Inserted: apple");
        
        boolean result1 = trie.search("apple");
        System.out.println("Search 'apple': " + result1 + " (Expected: true)");
        
        boolean result2 = trie.search("app");
        System.out.println("Search 'app': " + result2 + " (Expected: false)");
        
        boolean result3 = trie.startsWith("app");
        System.out.println("StartsWith 'app': " + result3 + " (Expected: true)");
        
        trie.insert("app");
        boolean result4 = trie.search("app");
        System.out.println("Search 'app' after insert: " + result4 + " (Expected: true)");
    }
}

