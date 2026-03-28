package Practise;

import java.util.*;
import java.util.*;

// Class containing Pascal's Triangle generation logic
class Solution {
    // Function to generate Pascal's Triangle up to numRows
    public List<List<Integer>> generate(int numRows) {
        // Result list to hold all rows
        List<List<Integer>> triangle = new ArrayList<>();

        // Loop for each row
        for (int i = 0; i < numRows; i++) {
            // Create a row with size (i+1)
            List<Integer> row = new ArrayList<>(Collections.nCopies(i + 1, 1));

            // Fill elements from index 1 to i-1 (middle values)
            for (int j = 1; j < i; j++) {
                // Each element = sum of two elements above it
                row.set(j, triangle.get(i - 1).get(j - 1) +
                        triangle.get(i - 1).get(j));
            }

            // Add current row to the triangle
            triangle.add(row);
        }
        return triangle;
    }
}

public class ZeroMatrix {
    public static void main(String[] args) {
        Solution obj = new Solution();
        int n = 5;

        // Generate and print Pascal's Triangle
        List<List<Integer>> result = obj.generate(n);
        for (List<Integer> row : result) {
            for (Integer val : row) System.out.print(val + " ");
            System.out.println();
        }
    }
}

