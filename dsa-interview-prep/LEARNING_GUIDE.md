# 📚 DSA Interview Preparation - Complete Learning Guide

## 🎯 How to Use This Guide

This project is designed to take you from **basic to advanced** DSA concepts with detailed explanations perfect for interview preparation.

### 📖 Learning Path

#### **Week 1-2: Foundations**
1. Start with `arrays/BasicArrayOperations.java` - Learn basic array operations
2. Study `arrays/TwoSum.java` - Most asked interview question with detailed explanation
3. Master `arrays/MaxSubarraySum.java` - Kadane's Algorithm (very important!)
4. Practice `arrays/SlidingWindowMaximum.java` - Sliding window technique

#### **Week 3-4: Linked Lists**
1. Begin with `linkedlist/BasicLinkedListOperations.java` - Understand linked list basics
2. Study `linkedlist/DetectCycle.java` - Floyd's cycle detection (tortoise and hare)
3. Practice `linkedlist/MergeTwoSortedLists.java` - Common interview problem
4. Master `linkedlist/RemoveNthFromEnd.java` - Two pointers technique

#### **Week 5-6: Trees**
1. Start with `trees/TreeTraversals.java` - Master all traversal methods
2. Study `trees/MaxDepthBinaryTree.java` - Recursive thinking
3. Practice `trees/BinarySearchTree.java` - BST operations
4. Understand `trees/SameTree.java` - Tree comparison

#### **Week 7-8: Sorting & Searching**
1. Study `sorting/SortingAlgorithms.java` - All major sorting algorithms
2. Master `searching/BinarySearch.java` - Binary search variations
3. Practice implementing each algorithm yourself

#### **Week 9-10: Dynamic Programming**
1. Start with `dp/FibonacciDetailed.java` - Introduction to DP
2. Study `dp/ClimbingStairs.java` - Classic DP pattern
3. Master `dp/HouseRobber.java` - Decision-making DP
4. Practice `dp/CoinChange.java` - Optimization problems
5. Advanced: `dp/LongestIncreasingSubsequence.java`

#### **Week 11-12: Graphs & Advanced**
1. Study `graphs/GraphRepresentation.java` - BFS and DFS
2. Practice `graphs/NumberOfIslands.java` - Graph traversal applications
3. Master `strings/LongestSubstringWithoutRepeating.java` - Sliding window
4. Study `greedy/JumpGame.java` - Greedy algorithms

## 🔑 Key Concepts Explained

### 1. **Two Pointers Technique**
- Used in: Two Sum, Three Sum, Remove Nth from End
- When to use: When you need to find pairs or work with sorted arrays
- Pattern: One pointer at start, one at end, move based on condition

### 2. **Sliding Window**
- Used in: Longest Substring, Maximum in Window
- When to use: Finding subarrays/substrings with certain properties
- Pattern: Expand window, shrink when condition violated

### 3. **Dynamic Programming**
- Used in: Fibonacci, Climbing Stairs, House Robber
- When to use: Overlapping subproblems, optimal substructure
- Pattern: Memoization (top-down) or Tabulation (bottom-up)

### 4. **Greedy Algorithms**
- Used in: Jump Game
- When to use: When local optimal choice leads to global optimal
- Pattern: Make best choice at each step

### 5. **Graph Traversal**
- BFS: Level-order, uses queue, finds shortest path
- DFS: Deep exploration, uses stack/recursion, finds paths

## 📝 Interview Tips

### Before Coding:
1. **Clarify the problem** - Ask questions about edge cases
2. **Think out loud** - Explain your approach
3. **Start with brute force** - Then optimize
4. **Consider time/space complexity** - Always mention it

### While Coding:
1. **Use meaningful variable names**
2. **Add comments for complex logic**
3. **Handle edge cases** - Empty arrays, null values, single elements
4. **Test with examples** - Walk through your code

### After Coding:
1. **Test edge cases** - Empty, single element, duplicates
2. **Optimize if possible** - Can you reduce time or space?
3. **Explain complexity** - Time and space

## 🎓 Most Important Algorithms for Interviews

### Must Know (High Priority):
1. ✅ Two Sum (Hash Map)
2. ✅ Maximum Subarray Sum (Kadane's Algorithm)
3. ✅ Binary Search
4. ✅ Tree Traversals (Inorder, Preorder, Postorder, Level-order)
5. ✅ Detect Cycle in Linked List (Floyd's Algorithm)
6. ✅ Dynamic Programming basics (Fibonacci, Climbing Stairs)

### Should Know (Medium Priority):
1. ✅ Merge Two Sorted Lists
2. ✅ Longest Substring Without Repeating Characters
3. ✅ House Robber (DP)
4. ✅ Coin Change (DP)
5. ✅ BFS and DFS
6. ✅ Quick Sort / Merge Sort

### Good to Know (Lower Priority):
1. ✅ Sliding Window Maximum
2. ✅ Three Sum
3. ✅ Number of Islands
4. ✅ Longest Increasing Subsequence

## 💡 Practice Strategy

### Daily Practice:
- Solve 1-2 problems daily
- Focus on understanding, not memorizing
- Review solutions and understand why they work

### Weekly Review:
- Revisit problems you found difficult
- Implement solutions from scratch
- Explain solutions to someone (or yourself)

### Before Interview:
- Review all "Must Know" algorithms
- Practice explaining your thought process
- Time yourself solving problems

## 🚀 Running the Code

```bash
# Compile the project
cd com.harshit/dsa-interview-prep
mvn compile

# Run a specific class
java -cp target/classes com.harshit.dsa.arrays.TwoSum
java -cp target/classes com.harshit.dsa.dp.HouseRobber
```

## 📚 Additional Resources

- **LeetCode**: Practice problems online
- **GeeksforGeeks**: Detailed algorithm explanations
- **Cracking the Coding Interview**: Book with great explanations

## 🎯 Success Metrics

You're ready for interviews when you can:
- ✅ Explain any algorithm in your own words
- ✅ Implement solutions without looking at code
- ✅ Identify which technique to use for a problem
- ✅ Optimize solutions and explain trade-offs
- ✅ Handle edge cases confidently

---

**Remember**: Understanding > Memorization. Focus on the "why" behind each solution!

Good luck with your interviews! 🎉

