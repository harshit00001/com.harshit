# Java Collections Interview Prep (`com.harshit`)

Accenture-style **Java Collections** interview preparation for **4+ years experience**.

Each class is a self-contained lesson: interview questions, deep-dive comments, and runnable code.

## Requirements

- Java 17+
- Maven 3.9+

## Run

```bash
# Run the menu (lists all demos)
mvn -q exec:java

# Run a specific demo
mvn -q exec:java -Dexec.mainClass=com.harshit.map.HashMapInternalsDemo
```

## Topics

| Class | Interview focus |
|-------|-----------------|
| `CollectionsHierarchyDemo` | List vs Set vs Map vs Queue |
| `ArrayListVsLinkedListDemo` | Performance, when to use what |
| `HashMapInternalsDemo` | Buckets, collisions, treeify, resize |
| `HashMapEqualsHashCodeDemo` | equals/hashCode contract, mutable keys |
| `SetVariantsDemo` | HashSet, TreeSet, LinkedHashSet |
| `ConcurrentCollectionsDemo` | CHM, CopyOnWrite, atomic ops |
| `FailFastVsFailSafeDemo` | CME, safe iteration & removal |
| `ComparableVsComparatorDemo` | Sorting, TreeSet consistency trap |
| `QueueAndDequeDemo` | PriorityQueue, ArrayDeque |
| `LruCacheDemo` | LinkedHashMap access-order |
| `InterviewScenariosDemo` | Real-world Accenture-style scenarios |

## Package

All code lives under **`com.harshit`**.
