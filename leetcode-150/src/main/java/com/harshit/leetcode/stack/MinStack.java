package com.harshit.leetcode.stack;

import java.util.*;

/**
 * Problem: Min Stack
 * 
 * Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.
 * 
 * Implement the MinStack class:
 * - MinStack() initializes the stack object.
 * - void push(int val) pushes the element val onto the stack.
 * - void pop() removes the element on the top of the stack.
 * - int top() gets the top element of the stack.
 * - int getMin() retrieves the minimum element in the stack.
 * 
 * You must implement a solution with O(1) time complexity for each function.
 * 
 * Example 1:
 * Input
 * ["MinStack","push","push","push","getMin","pop","top","getMin"]
 * [[],[-2],[0],[-3],[],[],[],[]]
 * 
 * Output
 * [null,null,null,null,-3,null,0,-2]
 * 
 * Explanation
 * MinStack minStack = new MinStack();
 * minStack.push(-2);
 * minStack.push(0);
 * minStack.push(-3);
 * minStack.getMin(); // return -3
 * minStack.pop();
 * minStack.top();    // return 0
 * minStack.getMin(); // return -2
 * 
 * Constraints:
 * - -2^31 <= val <= 2^31 - 1
 * - Methods pop, top and getMin operations will always be called on non-empty stacks.
 * - At most 3 * 10^4 calls will be made to push, pop, top, and getMin.
 */
public class MinStack {
    
    /**
     * MinStack class implementation
     */
    public static class MinStackImpl {
        
        public MinStackImpl() {
            // Write your logic here
            // Initialize your data structures
            // Hint: Use two stacks or one stack with pairs
        }
        
        public void push(int val) {
            // Write your logic here
            // Hint: Push value and current minimum
        }
        
        public void pop() {
            // Write your logic here
            // Hint: Pop from stack
        }
        
        public int top() {
            // Write your logic here
            // Hint: Return top element
            return 0;
        }
        
        public int getMin() {
            // Write your logic here
            // Hint: Return minimum element
            return 0;
        }
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        MinStackImpl minStack = new MinStackImpl();
        
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        
        int min1 = minStack.getMin();
        System.out.println("getMin(): " + min1 + " (Expected: -3)");
        
        minStack.pop();
        int top = minStack.top();
        System.out.println("top(): " + top + " (Expected: 0)");
        
        int min2 = minStack.getMin();
        System.out.println("getMin(): " + min2 + " (Expected: -2)");
    }
}

