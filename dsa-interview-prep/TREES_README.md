# 🌳 Trees Data Structure - Complete Interview Guide

A comprehensive guide to Tree data structures from **Basic to Advanced** with detailed explanations, algorithms, and interview-focused problems.

---

## 📚 Table of Contents

1. [Introduction to Trees](#introduction-to-trees)
2. [Basic Terminology](#basic-terminology)
3. [Types of Trees](#types-of-trees)
4. [Tree Traversals](#tree-traversals)
5. [Binary Search Tree (BST)](#binary-search-tree-bst)
6. [Common Tree Operations](#common-tree-operations)
7. [Most Asked Interview Problems](#most-asked-interview-problems)
8. [Advanced Tree Structures](#advanced-tree-structures)
9. [Time & Space Complexity](#time--space-complexity)
10. [Interview Tips & Patterns](#interview-tips--patterns)

---

## Introduction to Trees

### What is a Tree?

A **tree** is a hierarchical data structure that consists of nodes connected by edges. Unlike arrays or linked lists which are linear, trees are non-linear and represent relationships in a hierarchical manner.

**Simple Explanation**: Think of a family tree or a company's organizational chart. You have a root (top person), and each person can have children (subordinates), who can have their own children, and so on.

**Technical Definition**: A tree is a connected acyclic graph (no cycles) with exactly one path between any two nodes.

### Key Characteristics

- **Root Node**: The topmost node (no parent)
- **Leaf Nodes**: Nodes with no children (terminal nodes)
- **Parent-Child Relationship**: Each node (except root) has exactly one parent
- **Siblings**: Nodes with the same parent
- **Path**: Sequence of nodes from one node to another
- **Height**: Length of longest path from root to leaf
- **Depth**: Length of path from root to a specific node

---

## Basic Terminology

```
                    A (Root, Level 0)
                   / \
                  B   C (Siblings, Level 1)
                 / \   \
                D   E   F (Level 2)
               /
              G (Leaf, Level 3)

- Node A: Root node (depth = 0, height = 3)
- Node B, C: Children of A, siblings to each other
- Node D, E, F: Children of B and C
- Node G: Leaf node (no children)
- Height of tree: 3 (longest path: A → B → D → G)
- Depth of G: 3 (path length from A to G)
```

### Important Terms

| Term | Definition | Example |
|------|-----------|---------|
| **Root** | Topmost node with no parent | Node A |
| **Leaf** | Node with no children | Node G, E, F |
| **Internal Node** | Node with at least one child | Node B, C, D |
| **Parent** | Node that has children | A is parent of B and C |
| **Child** | Node directly below another | B and C are children of A |
| **Sibling** | Nodes with same parent | B and C are siblings |
| **Ancestor** | Any node on path from root to current | A, B, D are ancestors of G |
| **Descendant** | Any node in subtree of current | D, G are descendants of B |
| **Degree** | Number of children | B has degree 2, C has degree 1 |
| **Height** | Longest path from node to leaf | Height of A is 3 |
| **Depth** | Path length from root to node | Depth of G is 3 |
| **Level** | Set of nodes at same depth | Level 1: B, C |

---

## Types of Trees

### 1. Binary Tree

A tree where each node has **at most 2 children** (left and right).

```
       1
      / \
     2   3
    / \
   4   5
```

**Properties**:
- Maximum nodes at level `i`: 2^i
- Maximum nodes in tree of height `h`: 2^(h+1) - 1
- Minimum height for `n` nodes: ⌈log₂(n+1)⌉ - 1

### 2. Binary Search Tree (BST)

A binary tree with ordering property:
- **Left subtree** of a node contains only nodes with values **less than** the node's value
- **Right subtree** of a node contains only nodes with values **greater than** the node's value
- Both left and right subtrees must also be BSTs

```
        4
       / \
      2   6
     / \ / \
    1  3 5  7
```

**Key Property**: Inorder traversal of BST gives **sorted sequence** (1, 2, 3, 4, 5, 6, 7)

### 3. Complete Binary Tree

All levels are completely filled except possibly the last level, which is filled from left to right.

```
       1
      / \
     2   3
    / \
   4   5
```

### 4. Full Binary Tree

Every node has either 0 or 2 children (no node has exactly 1 child).

```
       1
      / \
     2   3
    / \
   4   5
```

### 5. Perfect Binary Tree

All internal nodes have 2 children and all leaves are at the same level.

```
       1
      / \
     2   3
    / \ / \
   4  5 6  7
```

### 6. Balanced Binary Tree

Height difference between left and right subtrees is at most 1 for every node.

**Examples**: AVL Tree, Red-Black Tree

---

## Tree Traversals

Tree traversal means visiting every node in the tree exactly once. There are four main types:

### 1. Inorder Traversal (Left → Root → Right)

**Simple Explanation**: Visit left child first, then current node, then right child.

**Use Cases**:
- For BST: Produces sorted sequence
- Used in expression trees to get infix notation

**Algorithm**:
```
1. Traverse left subtree
2. Visit root
3. Traverse right subtree
```

**Example**:
```
Tree:       1
          /   \
         2     3
        / \   / \
       4   5 6   7

Inorder: 4 → 2 → 5 → 1 → 6 → 3 → 7
```

**Time Complexity**: O(n) - visit each node once  
**Space Complexity**: O(h) - recursion stack (h = height)

### 2. Preorder Traversal (Root → Left → Right)

**Simple Explanation**: Visit current node first, then left child, then right child.

**Use Cases**:
- Copying a tree
- Getting prefix expression from expression tree
- Creating a copy of the tree

**Algorithm**:
```
1. Visit root
2. Traverse left subtree
3. Traverse right subtree
```

**Example**:
```
Tree:       1
          /   \
         2     3
        / \   / \
       4   5 6   7

Preorder: 1 → 2 → 4 → 5 → 3 → 6 → 7
```

**Time Complexity**: O(n)  
**Space Complexity**: O(h)

### 3. Postorder Traversal (Left → Right → Root)

**Simple Explanation**: Visit left child first, then right child, then current node.

**Use Cases**:
- Deleting a tree
- Getting postfix expression from expression tree
- Calculating directory sizes

**Algorithm**:
```
1. Traverse left subtree
2. Traverse right subtree
3. Visit root
```

**Example**:
```
Tree:       1
          /   \
         2     3
        / \   / \
       4   5 6   7

Postorder: 4 → 5 → 2 → 6 → 7 → 3 → 1
```

**Time Complexity**: O(n)  
**Space Complexity**: O(h)

### 4. Level Order Traversal (BFS - Breadth First Search)

**Simple Explanation**: Visit nodes level by level, from top to bottom, left to right.

**Use Cases**:
- Printing tree level by level
- Finding minimum depth
- Serialization of binary tree

**Algorithm**:
```
1. Use a queue
2. Start with root
3. For each node, add its children to queue
4. Process nodes in queue order
```

**Example**:
```
Tree:       1
          /   \
         2     3
        / \   / \
       4   5 6   7

Level Order: 1 → 2 → 3 → 4 → 5 → 6 → 7
```

**Time Complexity**: O(n)  
**Space Complexity**: O(w) where w is maximum width (worst case O(n))

---

## Binary Search Tree (BST)

### BST Properties

1. **Ordering Property**: 
   - All nodes in left subtree < root
   - All nodes in right subtree > root
   - This property holds recursively

2. **No Duplicates**: Typically, BSTs don't allow duplicate values (or handle them consistently)

3. **Inorder Traversal**: Always produces sorted sequence

### BST Operations

#### 1. Search in BST

**Time Complexity**: 
- Average: O(log n) for balanced tree
- Worst: O(n) for skewed tree (like linked list)

**Algorithm**:
```
1. Start at root
2. If value == root.val, found
3. If value < root.val, search left subtree
4. If value > root.val, search right subtree
5. If reach null, not found
```

**Why O(log n)?** Each comparison eliminates half the tree (in balanced case).

#### 2. Insert in BST

**Time Complexity**: O(h) where h is height

**Algorithm**:
```
1. Start at root
2. If value < root.val, go left
3. If value > root.val, go right
4. If null found, insert new node there
5. Maintain BST property
```

**Key Point**: Always insert at leaf position to maintain BST property.

#### 3. Delete in BST

**Time Complexity**: O(h)

**Three Cases**:

**Case 1: Node has no children (Leaf)**
- Simply remove the node

**Case 2: Node has one child**
- Replace node with its child

**Case 3: Node has two children**
- Find inorder successor (smallest in right subtree) OR
- Find inorder predecessor (largest in left subtree)
- Replace node's value with successor/predecessor
- Delete successor/predecessor (which will be Case 1 or 2)

**Why Inorder Successor/Predecessor?**
- They maintain BST property when swapped
- Successor is the next larger value
- Predecessor is the next smaller value

#### 4. Validate BST

**Problem**: Check if a binary tree is a valid BST.

**Common Mistake**: Just checking `left.val < root.val < right.val` is **NOT sufficient**!

**Correct Approach**: 
- Use range-based validation
- Each node must be within valid range (min, max)
- Left subtree: (min, root.val)
- Right subtree: (root.val, max)

**Time Complexity**: O(n)

---

## Common Tree Operations

### 1. Find Maximum Depth (Height)

**Problem**: Find the height of a binary tree.

**Approach**: Recursive DFS
```
height(node) = 1 + max(height(left), height(right))
```

**Base Case**: If node is null, return 0

**Time Complexity**: O(n)  
**Space Complexity**: O(h) for recursion stack

### 2. Find Minimum Depth

**Problem**: Find the minimum depth (shortest path from root to leaf).

**Key Difference**: Must reach a **leaf** (node with no children).

**Time Complexity**: O(n)  
**Space Complexity**: O(h)

### 3. Check if Two Trees are Same

**Problem**: Determine if two binary trees are identical.

**Approach**: 
- Both null → same
- One null, one not → different
- Values match AND left subtrees same AND right subtrees same

**Time Complexity**: O(n)  
**Space Complexity**: O(h)

### 4. Check if Tree is Symmetric

**Problem**: Check if tree is mirror of itself.

**Approach**: Compare left and right subtrees as mirrors.

**Time Complexity**: O(n)  
**Space Complexity**: O(h)

### 5. Invert Binary Tree (Mirror Tree)

**Problem**: Swap left and right children for every node.

**Approach**: 
- Swap left and right
- Recursively invert left subtree
- Recursively invert right subtree

**Time Complexity**: O(n)  
**Space Complexity**: O(h)

---

## Most Asked Interview Problems

### Easy Level

#### 1. Maximum Depth of Binary Tree
**LeetCode**: #104  
**Approach**: Recursive DFS  
**Time**: O(n), **Space**: O(h)

#### 2. Same Tree
**LeetCode**: #100  
**Approach**: Recursive comparison  
**Time**: O(n), **Space**: O(h)

#### 3. Symmetric Tree
**LeetCode**: #101  
**Approach**: Compare left and right as mirrors  
**Time**: O(n), **Space**: O(h)

#### 4. Invert Binary Tree
**LeetCode**: #226  
**Approach**: Swap children recursively  
**Time**: O(n), **Space**: O(h)

#### 5. Path Sum
**LeetCode**: #112  
**Problem**: Check if there exists a root-to-leaf path with given sum  
**Approach**: DFS with sum tracking  
**Time**: O(n), **Space**: O(h)

### Medium Level

#### 6. Binary Tree Level Order Traversal
**LeetCode**: #102  
**Approach**: BFS using queue  
**Time**: O(n), **Space**: O(w) where w is max width

#### 7. Construct Binary Tree from Preorder and Inorder
**LeetCode**: #105  
**Approach**: 
- First element of preorder is root
- Find root in inorder → left and right subtrees
- Recursively build subtrees  
**Time**: O(n), **Space**: O(n)

#### 8. Validate Binary Search Tree
**LeetCode**: #98  
**Approach**: Range-based validation  
**Time**: O(n), **Space**: O(h)

#### 9. Lowest Common Ancestor (LCA)
**LeetCode**: #236  
**Problem**: Find the lowest common ancestor of two nodes  
**Approach**: 
- If both nodes in left subtree → LCA in left
- If both nodes in right subtree → LCA in right
- Otherwise, current node is LCA  
**Time**: O(n), **Space**: O(h)

#### 10. Binary Tree Right Side View
**LeetCode**: #199  
**Problem**: Return values of nodes visible from right side  
**Approach**: BFS, take last node of each level  
**Time**: O(n), **Space**: O(w)

#### 11. Count Complete Tree Nodes
**LeetCode**: #222  
**Problem**: Count nodes in complete binary tree (optimize)  
**Approach**: Use complete tree property to skip subtrees  
**Time**: O(log²n), **Space**: O(log n)

#### 12. Kth Smallest Element in BST
**LeetCode**: #230  
**Approach**: Inorder traversal (gives sorted order)  
**Time**: O(h + k), **Space**: O(h)

#### 13. Serialize and Deserialize Binary Tree
**LeetCode**: #297  
**Approach**: Preorder traversal with null markers  
**Time**: O(n), **Space**: O(n)

### Hard Level

#### 14. Binary Tree Maximum Path Sum
**LeetCode**: #124  
**Problem**: Find maximum path sum (path can start/end anywhere)  
**Approach**: 
- For each node, calculate max path through it
- Max path = node.val + max(left, 0) + max(right, 0)
- Track global maximum  
**Time**: O(n), **Space**: O(h)

#### 15. Recover Binary Search Tree
**LeetCode**: #99  
**Problem**: Two nodes are swapped, recover the BST  
**Approach**: Inorder traversal to find swapped nodes  
**Time**: O(n), **Space**: O(h)

#### 16. Binary Tree Postorder Traversal (Iterative)
**LeetCode**: #145  
**Approach**: Use two stacks or reverse preorder  
**Time**: O(n), **Space**: O(h)

---

## Advanced Tree Structures

### 1. AVL Tree (Self-Balancing BST)

**What is it?** A BST that automatically maintains balance.

**Balance Factor**: `height(left) - height(right)` must be -1, 0, or 1

**Rotations**:
- **Left Rotation**: When right subtree is too heavy
- **Right Rotation**: When left subtree is too heavy
- **Left-Right Rotation**: Double rotation
- **Right-Left Rotation**: Double rotation

**Time Complexity**: 
- All operations: O(log n) guaranteed
- Better worst-case than regular BST

**Use Cases**: When you need guaranteed O(log n) performance

### 2. Red-Black Tree

**Properties**:
1. Every node is either red or black
2. Root is always black
3. No two consecutive red nodes
4. Every path from root to null has same number of black nodes

**Time Complexity**: O(log n) for all operations

**Use Cases**: Used in Java's TreeMap, C++'s std::map

### 3. Trie (Prefix Tree)

**What is it?** A tree-like structure for storing strings.

**Use Cases**:
- Autocomplete
- Spell checker
- IP routing
- Longest prefix matching

**Example**:
```
        root
       / | \
      t  a  i
     /   |   \
    o    n    s
   /     |
  p      d
```

Stores: "top", "an", "and", "is"

**Time Complexity**:
- Insert: O(m) where m is string length
- Search: O(m)
- Space: O(ALPHABET_SIZE * N * M) where N is number of strings

### 4. Segment Tree

**What is it?** Used for range queries and updates.

**Use Cases**:
- Range sum queries
- Range minimum/maximum queries
- Range updates

**Time Complexity**:
- Build: O(n)
- Query: O(log n)
- Update: O(log n)

### 5. Fenwick Tree (Binary Indexed Tree)

**What is it?** Efficient data structure for prefix sums.

**Use Cases**: Range sum queries, point updates

**Time Complexity**:
- Query: O(log n)
- Update: O(log n)
- Space: O(n)

---

## Time & Space Complexity

### Common Operations Complexity

| Operation | Binary Tree | BST (Balanced) | BST (Skewed) | AVL Tree |
|-----------|-------------|----------------|--------------|----------|
| **Search** | O(n) | O(log n) | O(n) | O(log n) |
| **Insert** | O(1)* | O(log n) | O(n) | O(log n) |
| **Delete** | O(1)* | O(log n) | O(n) | O(log n) |
| **Traversal** | O(n) | O(n) | O(n) | O(n) |
| **Space** | O(n) | O(n) | O(n) | O(n) |

*Assuming you know the position

### Traversal Complexities

| Traversal | Time | Space (Recursive) | Space (Iterative) |
|-----------|------|-------------------|-------------------|
| **Inorder** | O(n) | O(h) | O(h) |
| **Preorder** | O(n) | O(h) | O(h) |
| **Postorder** | O(n) | O(h) | O(h) |
| **Level Order** | O(n) | O(w) | O(w) |

Where:
- `n` = number of nodes
- `h` = height of tree
- `w` = maximum width of tree

### Height Analysis

- **Balanced Tree**: h = O(log n)
- **Skewed Tree**: h = O(n)
- **Complete Tree**: h = ⌊log₂n⌋

---

## Interview Tips & Patterns

### 1. Recursive Pattern

Most tree problems use recursion:
```java
public TreeNode solve(TreeNode root) {
    // Base case
    if (root == null) {
        return null; // or appropriate value
    }
    
    // Recursive calls
    TreeNode left = solve(root.left);
    TreeNode right = solve(root.right);
    
    // Process and return
    return process(root, left, right);
}
```

### 2. DFS vs BFS

**Use DFS (Recursion/Stack)** when:
- Need to explore deep paths
- Finding paths, sums, depths
- Most tree problems

**Use BFS (Queue)** when:
- Need level-by-level processing
- Finding shortest path
- Level order traversal

### 3. Helper Functions

Many problems need helper functions with additional parameters:
```java
// Public method
public boolean isValidBST(TreeNode root) {
    return isValidBST(root, Long.MIN_VALUE, Long.MAX_VALUE);
}

// Helper with bounds
private boolean isValidBST(TreeNode root, long min, long max) {
    // Implementation
}
```

### 4. Common Patterns

**Pattern 1: Bottom-Up**
- Process children first, then parent
- Example: Max depth, diameter

**Pattern 2: Top-Down**
- Process parent first, then children
- Example: Path sum, validate BST

**Pattern 3: Inorder for BST**
- Inorder traversal gives sorted order
- Use for: Kth smallest, validate BST

**Pattern 4: Level Order**
- Use queue for BFS
- Process level by level

### 5. Edge Cases to Consider

1. **Empty tree** (root == null)
2. **Single node** tree
3. **Skewed tree** (like linked list)
4. **Duplicate values** (for BST)
5. **Integer overflow** (use long for bounds)

### 6. Optimization Tips

1. **Early Termination**: Return as soon as condition fails
2. **Memoization**: Cache results for repeated subproblems
3. **Iterative Solutions**: Avoid stack overflow for deep trees
4. **Morris Traversal**: O(1) space for inorder (advanced)

### 7. Problem-Solving Steps

1. **Understand**: Draw examples, understand requirements
2. **Approach**: Choose recursive/iterative, DFS/BFS
3. **Code**: Write clean, readable code
4. **Test**: Test with edge cases
5. **Optimize**: Analyze time/space, optimize if needed

---

## Practice Problems by Difficulty

### Easy (Start Here)
- Maximum Depth of Binary Tree
- Same Tree
- Symmetric Tree
- Invert Binary Tree
- Path Sum
- Balanced Binary Tree

### Medium (Most Common in Interviews)
- Binary Tree Level Order Traversal
- Construct Binary Tree from Preorder and Inorder
- Validate Binary Search Tree
- Lowest Common Ancestor
- Binary Tree Right Side View
- Kth Smallest Element in BST
- Serialize and Deserialize Binary Tree
- Binary Tree Zigzag Level Order Traversal

### Hard (Advanced)
- Binary Tree Maximum Path Sum
- Recover Binary Search Tree
- Serialize and Deserialize N-ary Tree
- Binary Tree Postorder Traversal (Iterative)

---

## Key Takeaways

1. **Trees are hierarchical** - think recursively
2. **BST property** - left < root < right
3. **Traversals** - Inorder (sorted for BST), Preorder (copy), Postorder (delete), Level Order (BFS)
4. **Time Complexity** - Usually O(n) for traversal, O(log n) for balanced BST operations
5. **Space Complexity** - O(h) for recursion, O(w) for BFS
6. **Most problems use recursion** - master the recursive pattern
7. **Edge cases matter** - null checks, single node, empty tree
8. **Practice makes perfect** - solve problems regularly

---

## Resources

- **LeetCode**: Tree tag has 200+ problems
- **Visualization**: Use visualgo.net for tree animations
- **Practice**: Start with easy, move to medium, tackle hard

---

**Happy Learning! 🌳**

*Remember: Every expert was once a beginner. Keep practicing!*

