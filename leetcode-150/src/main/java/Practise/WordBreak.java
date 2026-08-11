package Practise;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//class Solution {
//    public boolean wordBreak(String s, List<String> wordDict) {
//        for(String str : wordDict)
//        {
//            if(str)
//        }
//    }
//}
public class WordBreak {
    public static void main(String[] args) {
        boolean result = wordBreak("catsandog", Arrays.asList("cats","dog","sand","and","cat"));
        System.out.println(result);
    }
//    public static boolean wordBreak(String s, List<String> wordDict) {
//        for(String str : wordDict)
//        {
//            if(!s.contains(str))
//            {
//                return false;
//            }
//        }
//        return true;
//    }
      public static boolean wordBreak(String s, List<String> wordDict) {
          int n = s.length();
          Set<String> words = new HashSet<>(wordDict);
          boolean[] dp = new boolean[n + 1];
          dp[0] = true;
          for (int i = 1; i <= n; i++) {
              for (int j = 0; j < i; j++) {
                  if (dp[j] && words.contains(s.substring(j, i))) {
                      dp[i] = true;
                      break;
                  }
              }
          }
            return dp[n];
      }
}
