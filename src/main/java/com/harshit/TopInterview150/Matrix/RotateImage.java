package com.harshit.TopInterview150.Matrix;

import java.util.Arrays;

public class RotateImage {
    public static void rotate(int[][] matrix) {
        int n =matrix.length;
        //transpose the matrix
        for(int i=0;i<n;i++)
        {
            for(int j=i+1;j<n;j++)
            {
                int temp = matrix[i][j];
                matrix[i][j]=matrix[j][i];
                matrix[j][i]= temp;
            }
        }
        //reverse the row
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n/2;j++)
            {
                int temp = matrix[i][j];
                matrix[i][j]=matrix[i][n-j-1];
                matrix[i][n-j-1]= temp;
            }
        }
        for(int[] mat:matrix)
        {
            System.out.println(Arrays.toString(mat));
        }
    }
    public static void main(String[] args) {
        int[][] num= {{1,2,3},
                      {4,5,6},
                      {7,8,9}};

        rotate(num);
    }
}
