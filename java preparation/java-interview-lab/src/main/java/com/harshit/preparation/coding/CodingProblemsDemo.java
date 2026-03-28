package com.harshit.preparation.coding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <h2>Common coding patterns for interviews</h2>
 * <p>
 * Duplicates: HashMap count or HashSet single pass for "seen".
 * Common elements: HashSet + iteration.
 * Sliding window: add incoming, subtract outgoing when window full.
 * Palindrome: two pointers after filtering alphanumerics.
 */
public final class CodingProblemsDemo {

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 2, 4, 1, 5};
        System.out.println("duplicates: " + duplicates(nums));

        List<Integer> a = List.of(1, 2, 3, 4);
        List<Integer> b = List.of(3, 4, 5, 6);
        System.out.println("common: " + common(a, b));

        int[] arr = {1, 2, 3, 4, 5};
        System.out.println("window sums k=3: " + slidingSum(arr, 3));

        System.out.println("palindrome? " + isPalindromeClean("A man, a plan, a canal: Panama"));
    }

    /** O(n) duplicate values using frequency map. */
    static List<Integer> duplicates(int[] arr) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int x : arr) {
            freq.merge(x, 1, Integer::sum);
        }
        List<Integer> out = new ArrayList<>();
        for (Map.Entry<Integer, Integer> e : freq.entrySet()) {
            if (e.getValue() > 1) {
                out.add(e.getKey());
            }
        }
        return out;
    }

    static List<Integer> common(List<Integer> a, List<Integer> b) {
        Set<Integer> set = new HashSet<>(a);
        List<Integer> out = new ArrayList<>();
        for (Integer x : b) {
            if (set.contains(x)) {
                out.add(x);
            }
        }
        return out;
    }

    static List<Integer> slidingSum(int[] arr, int k) {
        List<Integer> sums = new ArrayList<>();
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            if (i >= k) {
                sum -= arr[i - k];
            }
            if (i >= k - 1) {
                sums.add(sum);
            }
        }
        return sums;
    }

    static boolean isPalindromeClean(String s) {
        int l = 0;
        int r = s.length() - 1;
        while (l < r) {
            while (l < r && !Character.isLetterOrDigit(s.charAt(l))) {
                l++;
            }
            while (l < r && !Character.isLetterOrDigit(s.charAt(r))) {
                r--;
            }
            if (Character.toLowerCase(s.charAt(l)) != Character.toLowerCase(s.charAt(r))) {
                return false;
            }
            l++;
            r--;
        }
        return true;
    }

    private CodingProblemsDemo() {
    }
}
