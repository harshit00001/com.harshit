package Practise;

public class SecondSmallest {
    public static int secondSmallest(int[] arr, int n) {
        // Edge case: if the array has fewer than 2 elements
        if (n < 2)
            return -1;

        int small = Integer.MAX_VALUE;
        int second_small = Integer.MAX_VALUE;

        // Loop through the array to find the second smallest element
        for (int i = 0; i < n; i++) {
            // Update the smallest and second smallest values
            if (arr[i] < small) {
                second_small = small;
                small = arr[i];
            }
            else if (arr[i] < second_small && arr[i] != small) {
                second_small = arr[i];
            }
        }
        return second_small; // Return the second smallest element
    }
    public static void main(String[] args) {
        // Array of elements
        int[] arr = {1, 2, 4, 7, 7, 5};

        // Calculate the size of the array
        int n = arr.length;

        // Find the second smallest and second largest elements
        int sS = SecondSmallest.secondSmallest(arr, n);
//        int sL = Solution.secondLargest(arr, n);

        // Output the results
        System.out.println("Second smallest is " + sS);
        //System.out.println("Second largest is " + sL);
    }

}
