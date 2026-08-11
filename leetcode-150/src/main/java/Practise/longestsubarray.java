package Practise;

import java.util.HashMap;

public class longestsubarray {
    public static int longestSubarray(int[] arr, int K) {
        HashMap<Integer, Integer> map = new HashMap<>();

        int sum = 0;
        int maxLen = 0;

        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];

            // Case 1: sum itself equals K
            if (sum == K) {
                maxLen = i + 1;
            }

            // Case 2: (sum - K) exists
            if (map.containsKey(sum - K)) {
                int len = i - map.get(sum - K);
                maxLen = Math.max(maxLen, len);
            }

            // Store prefix sum (only first occurrence)
            if (!map.containsKey(sum)) {
                map.put(sum, i);
            }
        }

        return maxLen;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 1, 1, 1, 1};
        int K = 3;

        System.out.println(longestSubarray(arr, K));
    }
}
