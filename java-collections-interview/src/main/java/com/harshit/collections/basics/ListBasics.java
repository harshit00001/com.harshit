package com.harshit.collections.basics;

import java.util.*;

/**
 * LIST BASICS - Understanding Java List Interface
 * 
 * The List interface represents an ordered collection of elements where each element can be accessed by its index position.
 * When we say ordered, we mean that the elements are stored in a specific sequence, and this sequence is maintained as you
 * add or remove elements. This is different from sorted, which means elements are arranged according to some comparison
 * criteria. A List maintains the order in which you insert elements, not necessarily a sorted order.
 * 
 * Lists allow duplicate elements, which means you can add the same element multiple times, and each occurrence is treated
 * as a separate element with its own index position. Lists also allow null values, though some implementations may have
 * restrictions on how many null values can be stored.
 * 
 * The most commonly used List implementations are ArrayList and LinkedList. ArrayList is backed by a dynamic array, which
 * means it uses an array internally to store elements. When you create an ArrayList, it starts with a default capacity,
 * typically ten elements. When you add more elements than the current capacity, ArrayList automatically creates a new,
 * larger array and copies all existing elements to it. This process happens automatically behind the scenes.
 * 
 * LinkedList, on the other hand, is implemented as a doubly linked list. This means each element, or node, contains the
 * actual data and references to both the next node and the previous node in the sequence. This structure makes insertion
 * and deletion operations very fast, especially at the beginning or end of the list.
 * 
 * Vector is a legacy class that is similar to ArrayList but is synchronized, making it thread-safe. However, Vector is
 * generally not recommended for new code because the synchronization overhead is usually unnecessary, and if you need
 * thread safety, there are better alternatives like CopyOnWriteArrayList or Collections.synchronizedList.
 */
public class ListBasics {
    
    public static void main(String[] args) {
        demonstrateArrayList();
        demonstrateLinkedList();
        compareArrayListVsLinkedList();
    }
    
    /**
     * ARRAYLIST DEMONSTRATION
     * 
     * This method demonstrates how ArrayList works and when to use it. ArrayList is backed by a dynamic array, which means
     * it uses an array internally to store elements. When you create an ArrayList without specifying a capacity, it starts
     * with a default initial capacity of ten elements. This capacity is not the same as the size - capacity is how many
     * elements the array can hold before it needs to resize, while size is how many elements are actually in the list.
     * 
     * When you add elements to an ArrayList and the current capacity is exceeded, ArrayList automatically creates a new,
     * larger array. The new array is typically 50% larger than the old one, though this can vary. All existing elements are
     * then copied from the old array to the new array, and the old array is discarded. This resizing operation happens
     * automatically and is transparent to you as a developer, but it does have a performance cost.
     * 
     * ArrayList is best suited for scenarios where you frequently need to read elements or access them by index. Because
     * arrays allow direct access to any element using its index, accessing an element in an ArrayList is extremely fast,
     * taking constant time on average. This makes ArrayList ideal for scenarios where you need to iterate through elements
     * or access specific elements by their position.
     * 
     * However, ArrayList is not ideal for scenarios where you frequently insert or delete elements from the middle of the
     * list. This is because inserting or removing an element requires shifting all subsequent elements to make room or fill
     * the gap. If you have a list with a thousand elements and you insert an element at position 2, all 998 elements from
     * position 2 onwards need to be shifted one position to the right, which is an expensive operation.
     */
    public static void demonstrateArrayList() {
        System.out.println("=== ARRAYLIST DEMONSTRATION ===");
        
        // Create ArrayList
        // Default initial capacity: 10
        // Grows by 50% when capacity is exceeded
        List<String> arrayList = new ArrayList<>();
        
        // Add elements
        arrayList.add("Apple");      // Index 0
        arrayList.add("Banana");     // Index 1
        arrayList.add("Cherry");     // Index 2
        arrayList.add("Apple");      // Duplicate allowed - Index 3
        arrayList.add(null);         // Null allowed - Index 4
        
        System.out.println("ArrayList: " + arrayList);
        System.out.println("Size: " + arrayList.size());
        
        // Access by index
        System.out.println("Element at index 1: " + arrayList.get(1));
        
        // Check if contains
        System.out.println("Contains 'Apple': " + arrayList.contains("Apple"));
        System.out.println("Index of 'Apple': " + arrayList.indexOf("Apple"));
        System.out.println("Last index of 'Apple': " + arrayList.lastIndexOf("Apple"));
        
        // Remove element
        arrayList.remove(0);  // Remove by index
        System.out.println("After removing index 0: " + arrayList);
        
        arrayList.remove("Banana");  // Remove by object
        System.out.println("After removing 'Banana': " + arrayList);
        
        // Iterate
        System.out.println("\nIterating ArrayList:");
        for (String fruit : arrayList) {
            System.out.println("  - " + fruit);
        }
        
        // Using Iterator
        System.out.println("\nUsing Iterator:");
        Iterator<String> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            System.out.println("  - " + iterator.next());
        }
        
        System.out.println();
    }
    
    /**
     * LINKEDLIST DEMONSTRATION
     * 
     * This method demonstrates how LinkedList works and when to use it. LinkedList is implemented as a doubly linked list,
     * which means each element in the list is stored in a node that contains the actual data and two references: one
     * pointing to the next node and one pointing to the previous node. This structure is like a chain where each link
     * knows about both the next and previous links.
     * 
     * Because of this structure, inserting or removing elements from the beginning or end of a LinkedList is very fast,
     * taking constant time. You only need to update a few references - the new node's references, the previous node's next
     * reference, and the next node's previous reference. This makes LinkedList ideal for scenarios where you frequently add
     * or remove elements from the beginning or middle of the list.
     * 
     * LinkedList is also useful when you don't know the size of your collection in advance, because unlike ArrayList,
     * LinkedList doesn't need to resize an array when it grows. Each new element just requires creating a new node and
     * updating references, which is a constant-time operation.
     * 
     * However, accessing an element by index in a LinkedList is slower than in an ArrayList. This is because there's no
     * direct way to jump to a specific position - you have to traverse the list from the beginning or end to reach the
     * desired position. If you want to access the element at index 500 in a LinkedList, you need to start from the beginning
     * and follow the next references 500 times, which takes linear time.
     * 
     * LinkedList also has more memory overhead than ArrayList because each node needs to store not just the data, but also
     * two references. For small objects, this overhead can be significant. However, for large objects, the overhead is
     * relatively small compared to the object size itself.
     */
    public static void demonstrateLinkedList() {
        System.out.println("=== LINKEDLIST DEMONSTRATION ===");
        
        // Create LinkedList
        List<String> linkedList = new LinkedList<>();
        
        // Add elements
        linkedList.add("First");
        linkedList.add("Second");
        linkedList.add("Third");
        
        System.out.println("LinkedList: " + linkedList);
        
        // LinkedList specific methods (from Deque interface)
        LinkedList<String> deque = (LinkedList<String>) linkedList;
        
        // Add at beginning
        deque.addFirst("Zero");
        System.out.println("After addFirst('Zero'): " + deque);
        
        // Add at end
        deque.addLast("Fourth");
        System.out.println("After addLast('Fourth'): " + deque);
        
        // Get first and last
        System.out.println("First element: " + deque.getFirst());
        System.out.println("Last element: " + deque.getLast());
        
        // Remove first and last
        deque.removeFirst();
        deque.removeLast();
        System.out.println("After removing first and last: " + deque);
        
        System.out.println();
    }
    
    /**
     * COMPARISON: ArrayList vs LinkedList
     * 
     * This method demonstrates the performance differences between ArrayList and LinkedList through actual timing
     * measurements. Understanding these differences is crucial for choosing the right collection for your specific use case.
     * 
     * When it comes to getting an element by index, ArrayList is much faster because arrays allow direct access to any
     * element using its index. This operation takes constant time, O(1), meaning it takes the same amount of time
     * regardless of the list size. LinkedList, on the other hand, requires traversing the list from the beginning or end
     * to reach the desired position, which takes linear time, O(n), meaning the time increases proportionally with the
     * list size.
     * 
     * Adding elements at the end of the list is fast for both ArrayList and LinkedList, taking constant time. However, for
     * ArrayList, this is amortized constant time because occasionally the array needs to be resized, which is an expensive
     * operation. But when averaged over many operations, it's still effectively constant time.
     * 
     * Adding elements at the beginning of the list shows a significant difference. For ArrayList, this requires shifting
     * all existing elements one position to the right, which takes linear time. For LinkedList, you only need to create a
     * new node and update a few references, which takes constant time. This is where LinkedList really shines.
     * 
     * Adding elements in the middle requires finding the position first, which takes linear time for both. Then ArrayList
     * needs to shift subsequent elements, while LinkedList needs to update references. Both operations take linear time,
     * but the actual performance depends on the specific implementation details and the size of the list.
     * 
     * In practice, ArrayList is used more frequently because random access and iteration are common operations, and modern
     * hardware makes array operations very fast. The performance difference for most real-world scenarios is often
     * negligible, and ArrayList's simpler implementation and better cache locality often make it the better choice.
     */
    public static void compareArrayListVsLinkedList() {
        System.out.println("=== ARRAYLIST vs LINKEDLIST COMPARISON ===");
        
        // Performance test: Adding at beginning
        int size = 100000;
        
        // ArrayList - slow for adding at beginning
        long start = System.currentTimeMillis();
        List<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            arrayList.add(0, i);  // Add at beginning - O(n) each time
        }
        long arrayListTime = System.currentTimeMillis() - start;
        
        // LinkedList - fast for adding at beginning
        start = System.currentTimeMillis();
        List<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            linkedList.add(0, i);  // Add at beginning - O(1) each time
        }
        long linkedListTime = System.currentTimeMillis() - start;
        
        System.out.println("Adding " + size + " elements at beginning:");
        System.out.println("ArrayList time: " + arrayListTime + " ms");
        System.out.println("LinkedList time: " + linkedListTime + " ms");
        System.out.println("LinkedList is " + (arrayListTime / linkedListTime) + "x faster");
        
        // Performance test: Random access
        size = 100000;
        arrayList = new ArrayList<>();
        linkedList = new LinkedList<>();
        
        // Fill both
        for (int i = 0; i < size; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }
        
        // Random access - ArrayList is much faster
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            int index = (int) (Math.random() * size);
            arrayList.get(index);  // O(1)
        }
        arrayListTime = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            int index = (int) (Math.random() * size);
            linkedList.get(index);  // O(n)
        }
        linkedListTime = System.currentTimeMillis() - start;
        
        System.out.println("\nRandom access (10000 times):");
        System.out.println("ArrayList time: " + arrayListTime + " ms");
        System.out.println("LinkedList time: " + linkedListTime + " ms");
        System.out.println("ArrayList is " + (linkedListTime / arrayListTime) + "x faster");
        
        System.out.println();
    }
}

