package com.harshit.preparation.topic17;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Topic 17 — Coding (interview scripts + working methods).
 */
public final class Topic17Qa {

    private Topic17Qa() {
    }

    /*
     * Q: Common elements in two lists — optimize with HashSet?
     *
     * SCRIPT:
     * I say I put one list into a HashSet for O(1) lookup, then walk the second list and collect
     * matches. Overall time is linear in the sizes, which beats nested loops that are quadratic.
     */

    public static List<Integer> common(List<Integer> a, List<Integer> b) {
        Set<Integer> set = new HashSet<>(a);
        List<Integer> out = new ArrayList<>();
        for (Integer x : b) {
            if (set.contains(x)) {
                out.add(x);
            }
        }
        return out;
    }

    /*
     * Q: Print duplicate elements in an array?
     *
     * SCRIPT:
     * I count frequencies with a map, then output keys where count exceeds one. Alternatively sort
     * and scan adjacent pairs, but the map approach is linear on average for integer keys with a good hash.
     */

    public static List<Integer> duplicates(int[] arr) {
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

    /*
     * Q: Sliding window sum for subarrays of size k?
     *
     * SCRIPT:
     * I maintain a running sum, add the new element entering the window, and subtract the element
     * leaving when the index passes k. That avoids recomputing the whole window each time—O(n) total.
     */

    public static List<Integer> windowSums(int[] arr, int k) {
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

    /*
     * Q: Palindrome ignoring non-alphanumeric characters?
     *
     * SCRIPT:
     * I use two pointers from both ends, skip characters that are not letters or digits, compare
     * case-insensitively, and move inward. That matches the common interview constraint without
     * building a new string first.
     */

    public static boolean isPalindromeClean(String s) {
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

    public static void demo() {
        System.out.println("common: " + common(List.of(1, 2, 3), List.of(2, 3, 4)));
        System.out.println("duplicates: " + duplicates(new int[] {1, 2, 2, 3, 3}));
        System.out.println("windowSums k=2: " + windowSums(new int[] {1, 2, 3, 4}, 2));
        System.out.println("isPalindromeClean: " + isPalindromeClean("A man, a plan, a canal: Panama"));
    }

    public static void main(String[] args) {
        demo();
    }
}
