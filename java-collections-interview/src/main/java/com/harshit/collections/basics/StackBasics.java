package com.harshit.collections.basics;

import java.util.*;

/**
 * STACK BASICS - Understanding Stack Data Structure
 * 
 * A Stack is a Last-In-First-Out (LIFO) data structure, like a stack of plates where you can only
 * add or remove plates from the top. The last plate you put on the stack is the first one you can
 * take off. This is the opposite of a queue, which follows First-In-First-Out (FIFO) principle.
 * 
 * In Java, the Stack class extends Vector and is a legacy class. While it's still available and
 * functional, it's generally recommended to use ArrayDeque instead when you need stack functionality,
 * because ArrayDeque is more efficient and doesn't have the synchronization overhead of Vector.
 * 
 * Stack operations include push() to add an element to the top, pop() to remove and return the top
 * element, peek() to examine the top element without removing it, and empty() to check if the stack
 * is empty. These operations are fundamental to many algorithms, including expression evaluation,
 * backtracking, and parsing.
 */
public class StackBasics {
    
    public static void main(String[] args) {
        demonstrateStack();
        demonstrateArrayDequeAsStack();
        demonstrateStackApplications();
        compareStackVsArrayDeque();
    }
    
    /**
     * STACK CLASS DEMONSTRATION
     * 
     * This method demonstrates the legacy Stack class. While Stack is still available in Java,
     * it's not recommended for new code because it extends Vector, which has synchronization
     * overhead even when you don't need thread safety. However, understanding Stack is important
     * for interviews and legacy code maintenance.
     */
    public static void demonstrateStack() {
        System.out.println("=== STACK CLASS DEMONSTRATION ===");
        
        // Create Stack
        Stack<String> stack = new Stack<>();
        
        // Push elements (add to top)
        stack.push("First");
        stack.push("Second");
        stack.push("Third");
        
        System.out.println("Stack: " + stack);
        System.out.println("Top element (peek): " + stack.peek());
        System.out.println("Is empty: " + stack.empty());
        
        // Pop elements (remove from top)
        System.out.println("\nPopping elements:");
        while (!stack.empty()) {
            System.out.println("  Popped: " + stack.pop());
        }
        
        System.out.println("Stack after popping all: " + stack);
        System.out.println("Is empty: " + stack.empty());
        
        // Search operation (returns distance from top, -1 if not found)
        stack.push("A");
        stack.push("B");
        stack.push("C");
        System.out.println("\nSearch 'B': " + stack.search("B")); // Returns 2 (2 positions from top)
        System.out.println("Search 'A': " + stack.search("A")); // Returns 3 (3 positions from top)
        System.out.println("Search 'X': " + stack.search("X")); // Returns -1 (not found)
        
        System.out.println();
    }
    
    /**
     * ARRAYDEQUE AS STACK DEMONSTRATION
     * 
     * ArrayDeque is the recommended way to implement a stack in modern Java. It provides the
     * same functionality as Stack but with better performance because it doesn't have the
     * synchronization overhead of Vector. ArrayDeque uses push() and pop() methods just like
     * Stack, making it a drop-in replacement.
     */
    public static void demonstrateArrayDequeAsStack() {
        System.out.println("=== ARRAYDEQUE AS STACK (RECOMMENDED) ===");
        
        // Using ArrayDeque as Stack
        Deque<String> stack = new ArrayDeque<>();
        
        // Push elements
        stack.push("First");
        stack.push("Second");
        stack.push("Third");
        
        System.out.println("Stack: " + stack);
        System.out.println("Top element (peek): " + stack.peek());
        System.out.println("Is empty: " + stack.isEmpty());
        
        // Pop elements
        System.out.println("\nPopping elements:");
        while (!stack.isEmpty()) {
            System.out.println("  Popped: " + stack.pop());
        }
        
        System.out.println("Stack after popping all: " + stack);
        System.out.println();
    }
    
    /**
     * STACK APPLICATIONS DEMONSTRATION
     * 
     * Stacks are used in many real-world applications. This method demonstrates some common
     * use cases like checking balanced parentheses, reversing a string, and evaluating expressions.
     * Understanding these applications helps you recognize when to use a stack in your code.
     */
    public static void demonstrateStackApplications() {
        System.out.println("=== STACK APPLICATIONS ===");
        
        // Application 1: Check balanced parentheses
        System.out.println("1. Checking Balanced Parentheses:");
        System.out.println("   '()' is balanced: " + isBalanced("()"));
        System.out.println("   '()[]{}' is balanced: " + isBalanced("()[]{}"));
        System.out.println("   '([)]' is balanced: " + isBalanced("([)]"));
        System.out.println("   '((()))' is balanced: " + isBalanced("((()))"));
        
        // Application 2: Reverse a string
        System.out.println("\n2. Reversing String using Stack:");
        String original = "Hello World";
        String reversed = reverseString(original);
        System.out.println("   Original: " + original);
        System.out.println("   Reversed: " + reversed);
        
        // Application 3: Decimal to binary conversion
        System.out.println("\n3. Decimal to Binary using Stack:");
        int decimal = 42;
        String binary = decimalToBinary(decimal);
        System.out.println("   Decimal: " + decimal);
        System.out.println("   Binary: " + binary);
        
        System.out.println();
    }
    
    /**
     * Check if parentheses are balanced using stack
     */
    public static boolean isBalanced(String expression) {
        Deque<Character> stack = new ArrayDeque<>();
        
        for (char ch : expression.toCharArray()) {
            if (ch == '(' || ch == '[' || ch == '{') {
                stack.push(ch);
            } else if (ch == ')' || ch == ']' || ch == '}') {
                if (stack.isEmpty()) {
                    return false;
                }
                char top = stack.pop();
                if ((ch == ')' && top != '(') ||
                    (ch == ']' && top != '[') ||
                    (ch == '}' && top != '{')) {
                    return false;
                }
            }
        }
        
        return stack.isEmpty();
    }
    
    /**
     * Reverse a string using stack
     */
    public static String reverseString(String str) {
        Deque<Character> stack = new ArrayDeque<>();
        
        // Push all characters onto stack
        for (char ch : str.toCharArray()) {
            stack.push(ch);
        }
        
        // Pop all characters to build reversed string
        StringBuilder reversed = new StringBuilder();
        while (!stack.isEmpty()) {
            reversed.append(stack.pop());
        }
        
        return reversed.toString();
    }
    
    /**
     * Convert decimal to binary using stack
     */
    public static String decimalToBinary(int decimal) {
        if (decimal == 0) {
            return "0";
        }
        
        Deque<Integer> stack = new ArrayDeque<>();
        
        while (decimal > 0) {
            stack.push(decimal % 2);
            decimal = decimal / 2;
        }
        
        StringBuilder binary = new StringBuilder();
        while (!stack.isEmpty()) {
            binary.append(stack.pop());
        }
        
        return binary.toString();
    }
    
    /**
     * COMPARISON: Stack vs ArrayDeque
     */
    public static void compareStackVsArrayDeque() {
        System.out.println("=== STACK vs ARRAYDEQUE COMPARISON ===");
        
        System.out.println("\nFeature              | Stack        | ArrayDeque");
        System.out.println("---------------------|--------------|-------------");
        System.out.println("Thread-Safe          | Yes (Vector) | No");
        System.out.println("Performance          | Slower       | Faster");
        System.out.println("Null Allowed         | Yes          | No");
        System.out.println("Legacy Class         | Yes          | No");
        System.out.println("Recommended          | No           | Yes");
        
        System.out.println("\nRecommendation: Use ArrayDeque instead of Stack for new code");
        System.out.println("Reason: Better performance, no unnecessary synchronization");
        System.out.println();
    }
}

