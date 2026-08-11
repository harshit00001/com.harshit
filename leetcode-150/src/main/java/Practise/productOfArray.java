package Practise;

import java.util.*;

public class productOfArray {

        public boolean wordBreak(String s, List<String> wordDict) {
            Set<String> set = new HashSet<>(wordDict);

            boolean[] dp = new boolean[s.length() + 1];
            dp[0] = true; // empty string is always valid

            for (int i = 1; i <= s.length(); i++) {
                for (int j = 0; j < i; j++) {

                    // check if left part is valid and right part exists in dictionary
                    if (dp[j] && set.contains(s.substring(j, i))) {
                        dp[i] = true;
                        break;
                    }
                }
            }

            return dp[s.length()];
        }

        public static void main(String[] args) {
            productOfArray sol = new productOfArray();

            String s = "leetcode";
            List<String> wordDict = Arrays.asList("leet", "code");

            boolean result = sol.wordBreak(s, wordDict);

            System.out.println("Can be segmented: " + result);
        }
    }