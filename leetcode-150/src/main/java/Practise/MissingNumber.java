package Practise;

public class MissingNumber {
    public static int missingNumber(int[] arr, int n) {
        int xor1 = 0; // XOR of 1 to n
        int xor2 = 0; // XOR of array

        for (int i = 1; i <= n; i++) {
            xor1 ^= i;
            System.out.println(xor1);
        }

        for (int num : arr) {
            xor2 ^= num;
            System.out.println(xor2);
        }

        return xor1 ^ xor2;
    }
    public static void main(String[] args) {
        int[] arr = {1, 2, 4, 5}; // Missing 3
        int n = 5;

        int result = missingNumber(arr, n);
        System.out.println("Missing number is: " + result);
    }
}
