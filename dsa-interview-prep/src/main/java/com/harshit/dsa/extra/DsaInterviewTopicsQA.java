package com.harshit.dsa.extra;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DSA + coding patterns interview topics — under dsa-interview-prep.
 *
 * <p>Covers: reverse string, palindrome, duplicates, two-sum, Kadane, sorting/binary search sketches,
 * stack vs queue, word break, sliding window, simple recursion pattern.
 */
public final class DsaInterviewTopicsQA {

    private DsaInterviewTopicsQA() {
    }

    static String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    static boolean palindromeAlphaNumeric(String s) {
        int i = 0;
        int j = s.length() - 1;
        while (i < j) {
            while (i < j && !Character.isLetterOrDigit(s.charAt(i))) {
                i++;
            }
            while (i < j && !Character.isLetterOrDigit(s.charAt(j))) {
                j--;
            }
            if (Character.toLowerCase(s.charAt(i)) != Character.toLowerCase(s.charAt(j))) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }

    static List<Integer> duplicates(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        Set<Integer> dup = new HashSet<>();
        for (int n : nums) {
            if (!seen.add(n)) {
                dup.add(n);
            }
        }
        return new ArrayList<>(dup);
    }

    static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> index = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int need = target - nums[i];
            if (index.containsKey(need)) {
                return new int[]{index.get(need), i};
            }
            index.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }

    /** Maximum subarray sum (Kadane). */
    static int kadane(int[] nums) {
        int best = nums[0];
        int cur = nums[0];
        for (int i = 1; i < nums.length; i++) {
            cur = Math.max(nums[i], cur + nums[i]);
            best = Math.max(best, cur);
        }
        return best;
    }

    static int binarySearch(int[] sorted, int key) {
        int lo = 0;
        int hi = sorted.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int cmp = Integer.compare(sorted[mid], key);
            if (cmp == 0) {
                return mid;
            }
            if (cmp < 0) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return -1;
    }

    /** Word break: can s be segmented using dictionary words? (classic DP). */
    static boolean wordBreak(String s, Set<String> dict) {
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && dict.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[n];
    }

    /** Longest substring without repeating characters — sliding window. */
    static int longestUniqueSubstring(String s) {
        int[] last = new int[256];
        Arrays.fill(last, -1);
        int start = 0;
        int best = 0;
        for (int end = 0; end < s.length(); end++) {
            char c = s.charAt(end);
            if (last[c] >= start) {
                start = last[c] + 1;
            }
            last[c] = end;
            best = Math.max(best, end - start + 1);
        }
        return best;
    }

    static void stackVsQueueDemo() {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(1);
        stack.push(2);
        System.out.println("stack pop: " + stack.pop());

        Deque<Integer> queue = new ArrayDeque<>();
        queue.addLast(1);
        queue.addLast(2);
        System.out.println("queue poll: " + queue.removeFirst());
    }

    public static void main(String[] args) {
        System.out.println(reverse("interview"));
        System.out.println(palindromeAlphaNumeric("A man, a plan, a canal: Panama"));
        System.out.println("dup: " + duplicates(new int[]{1, 2, 3, 2, 1}));
        System.out.println("twoSum: " + Arrays.toString(twoSum(new int[]{2, 7, 11, 15}, 9)));
        System.out.println("kadane: " + kadane(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
        System.out.println("binarySearch idx: " + binarySearch(new int[]{1, 3, 5, 7}, 5));
        System.out.println("wordBreak: " + wordBreak("leetcode", new HashSet<>(Arrays.asList("leet", "code"))));
        System.out.println("longest unique: " + longestUniqueSubstring("abcabcbb"));
        stackVsQueueDemo();
    }
}
