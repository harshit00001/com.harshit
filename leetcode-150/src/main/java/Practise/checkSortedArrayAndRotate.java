package Practise;

public class checkSortedArrayAndRotate {
    public static boolean check(int[] nums) {
        int count = 0;
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            if (nums[i] > nums[(i + 1) % n]) {
                count++;
            }
        }

        return count <= 1;
    }

    public static void main(String[] args) {
        int[] arr1 = {3, 4, 5, 1, 2}; // true
        int[] arr2 = {1, 2, 3, 4, 5}; // true
        int[] arr3 = {2, 1, 3, 4};    // false

        System.out.println("Array 1: " + check(arr1));
        System.out.println("Array 2: " + check(arr2));
    }
}
