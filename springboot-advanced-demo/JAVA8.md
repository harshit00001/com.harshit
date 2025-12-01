# Java 8 Complete Guide - Notes and Interview Questions

## 📋 Table of Contents
1. [Java 8 Features Overview](#1-java-8-features-overview)
2. [Functional Interfaces](#2-functional-interfaces)
3. [Lambda Expressions](#3-lambda-expressions)
4. [Default Methods in Interfaces](#4-default-methods-in-interfaces)
5. [Static Methods in Interfaces](#5-static-methods-in-interfaces)
6. [Streams API](#6-streams-api)
7. [Stream Operations](#7-stream-operations)
8. [Optional Class](#8-optional-class)
9. [Coding Interview Questions - Strings](#9-coding-interview-questions---strings)
10. [Coding Interview Questions - Numbers](#10-coding-interview-questions---numbers)
11. [Coding Interview Questions - Employee Objects](#11-coding-interview-questions---employee-objects)
12. [Advanced Stream Operations](#12-advanced-stream-operations)

---

## 1. Java 8 Features Overview

### Key Features Introduced in Java 8:

1. **Default and Static Methods** in Interfaces
2. **Functional Interfaces**
3. **Lambda Expressions**
4. **Date and Time API** (java.time package)
5. **Stream API**
6. **Optional Class**

---

## 2. Functional Interfaces

### What is a Functional Interface?

A **Functional Interface** is an interface that has:
- **Exactly one abstract method**
- Can have multiple static and default methods
- May or may not be annotated with `@FunctionalInterface`
- Can be implemented using Lambda Expressions

### Common Functional Interfaces in Java 8:

| Interface | Purpose | Method Signature |
|-----------|---------|------------------|
| **Runnable** | Executes a task without parameters and return value | `void run()` |
| **Callable** | Executes a task that returns a value | `V call() throws Exception` |
| **Consumer<T>** | Accepts an input but returns nothing | `void accept(T t)` |
| **Supplier<T>** | Returns values without taking input | `T get()` |
| **Predicate<T>** | Returns a boolean value | `boolean test(T t)` |
| **Function<T,R>** | Takes input of one type, returns output of another/same type | `R apply(T t)` |
| **Comparator<T>** | Compares two objects | `int compare(T o1, T o2)` |

### Example:

```java
@FunctionalInterface
public interface BonusCalculator {
    void calculate(int amount);
    // Can have default and static methods
    default void policyType() {
        System.out.println("Policy for bonus calculation");
    }
}
```

---

## 3. Lambda Expressions

### What is a Lambda Expression?

A **Lambda Expression** is:
- An **anonymous function** that helps implement functional programming
- A method defined **without name, access specifier, or return type**
- Can be used **only with Functional Interfaces**
- Makes code writing **simple, short, and readable**

### Syntax:

```java
(parameters) -> expression
// or
(parameters) -> { statements; }
```

### Examples:

```java
// Old way (Anonymous inner class)
Runnable r = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello");
    }
};

// Lambda way
Runnable r = () -> System.out.println("Hello");

// With parameters
Predicate<Integer> isEven = (num) -> num % 2 == 0;

// With multiple statements
Function<String, String> upperCase = (str) -> {
    return str.toUpperCase();
};
```

### Important Rule: Effectively Final Variables

**Question: What happens if you modify a local variable inside a Lambda? (Tricky)**

**Answer:**
- Lambda expressions can access local variables, but they must be **effectively final** (i.e., not modified after assignment)
- If you try to modify it, **compilation will fail**

```java
// ❌ WRONG - Compilation Error
String message = "Hello";
IGreater greater = (String username) -> {
    message = "Welcome"; // ❌ Error: local variables are by default final, can't modify
    System.out.println("Great Day " + username);
};

// ✅ CORRECT - Effectively final
String message = "Hello"; // Not modified after this
IGreater greater = (String username) -> {
    System.out.println(message + " " + username); // ✅ OK - just reading
};
```

---

## 4. Default Methods in Interfaces

### What is the use of default methods in interfaces?

**Answer:**
- Helps to **add new functionality** to existing interfaces in an application
- Adding a default method to an existing interface **does not break the contract**
- Default methods are **implicitly public**
- Helps to add a **common behavior** across all implementing classes of the interface
- **Default method can be overridden** by implementing classes

### Example:

```java
public interface BonusCalculator {
    void calculate(int amount);
    
    // Default method - provides common implementation
    default void policyType() {
        System.out.println("Policy for bonus calculation");
    }
    
    default void greet() {
        System.out.println("Welcome");
    }
}

// Implementing class can use default method or override it
public class EmployeeBonus implements BonusCalculator {
    @Override
    public void calculate(int amount) {
        // Implementation
    }
    
    // Optional: Override default method
    @Override
    public void policyType() {
        System.out.println("Custom policy");
    }
}
```

### What will happen if two interfaces have the same default method?

**Question: What will happen if two interfaces have same default method and a class implements it? (Tricky)**

**Answer:**
- If two interfaces have the same default method and a class implements both, then **compilation fails**
- **Solution:** The implementation class **must override** the conflicting method

### Example:

```java
// Interface 1
public interface BonusCalculator {
    void calculate(int amount);
    default void policyType() {
        System.out.println("Policy for bonus");
    }
}

// Interface 2
@FunctionalInterface
public interface AllowanceCalculator {
    void calculate(int amount);
    default void policyType() {
        System.out.println("Policy for allowance");
    }
}

// Implementation class - MUST override
public class EmployeeDetails implements BonusCalculator, AllowanceCalculator {
    @Override
    public void calculate(int amount) {
        // Implementation
    }
    
    // ✅ MUST override to resolve conflict
    @Override
    public void policyType() {
        System.out.println("Policy for employees...");
        // Can call specific interface's default method
        AllowanceCalculator.super.policyType();
        BonusCalculator.super.policyType();
    }
}
```

---

## 5. Static Methods in Interfaces

### What is the use of static methods in interfaces?

**Answer:**
- Can be used to provide a **common functionality** for all implementation classes
- Static methods can be called using the **interface name only**
- **Cannot be overridden** by implementing classes

### Example:

```java
public interface BonusCalculator {
    void calculate(int amount);
    
    default void policyType() {
        System.out.println("Policy for bonus calculation");
    }
    
    // Static method - called using interface name
    static void call() {
        System.out.println("Can be called only using interface");
    }
}

// Usage
BonusCalculator.call(); // ✅ Call using interface name
```

### Difference between Static and Default Methods:

| Feature | Static Methods | Default Methods |
|---------|---------------|-----------------|
| **Purpose** | Provide common functionality for all implementation classes | Add new functionality to existing interface |
| **How to call** | Using interface name only | By object of implementation class |
| **Can be overridden?** | ❌ Cannot be overridden | ✅ Can be overridden |
| **Backward compatibility** | Yes | Yes (for backward compatibility) |

---

## 6. Streams API

### What are Streams in Java 8?

**Answer:**
- A **Stream represents a sequence of elements** and supports parallel and aggregate operations
- Streams are an **abstraction for processing collections** of values
- Streams can be created from **collections, arrays, or iterators**
- A stream **does not store its elements**
- Stream operations **don't change their source**
- Have **Specialized Streams** for primitive data types (IntStream, LongStream, DoubleStream)

### Key Characteristics:

1. **Non-storing**: Streams don't store elements
2. **Functional**: Operations don't modify the source
3. **Lazy**: Operations are executed only when needed
4. **Potentially unbounded**: Can work with infinite sequences
5. **Consumable**: Elements are visited only once

---

## 7. Stream Operations

### How to Create Streams?

#### 1. From an Array:

```java
String[] names = new String[] {"Ram", "John", "Sri"};

// Method 1: Using Stream.of()
Stream.of(names).forEach(name -> System.out.println(name));

// Method 2: Using Arrays.stream()
Arrays.stream(names).forEach(name -> System.out.println(name));
```

#### 2. From a List:

```java
List<String> courses = Arrays.asList("Java", "Angular", "Node");

// Convert to a stream
courses.stream().forEach(System.out::println);
```

#### 3. From Primitive Arrays:

```java
int[] nums = new int[]{1, 2, 3, 4, 5};

// Create IntStream
IntStream stream = Arrays.stream(nums);
```

### Difference between Intermediate & Terminal Operations:

| Feature | Intermediate Operations | Terminal Operations |
|---------|------------------------|---------------------|
| **Return Type** | Returns a new Stream | Consumes the Stream and returns result |
| **Methods** | `map()`, `filter()`, `sorted()`, `limit()`, `skip()`, `distinct()` | `forEach()`, `collect()`, `count()`, `findFirst()`, `findAny()` |
| **Execution** | Lazy operations - executed only when terminal operation is invoked | Eager operations - trigger execution |
| **Chaining** | Can be chained | Ends the stream pipeline |

### Example:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Intermediate operations (lazy)
Stream<Integer> stream = numbers.stream()
    .filter(n -> n > 5)        // Intermediate
    .map(n -> n * 2)           // Intermediate
    .sorted();                  // Intermediate

// Terminal operation (triggers execution)
stream.forEach(System.out::println); // Terminal
```

### Key Stream Operations:

#### 1. map() vs flatMap()

**Question: What is the difference between map() and flatMap() in Streams?**

**Answer:**
- **map()**: Transforms elements of one type into same/another type **individually**
- **flatMap()**: **Flattens multiple streams into a single stream**. Useful when dealing with nested collections

**Example - map():**

```java
String[] courses = {"Java", "Angular", "Node"};

// Transform each string to lowercase
Arrays.stream(courses)
    .map(str -> str.toLowerCase())
    .forEach(System.out::println);
```

**Example - flatMap():**

```java
// List of List of employees
List<List<Employee>> employees = Arrays.asList(
    Arrays.asList(employee1, employee2),
    Arrays.asList(employee3, employee4)
);

// Returns a stream of list of employees as blocks
Stream<List<Employee>> empListStream = employees.stream();

// Returns a stream of employees (flattened)
Stream<Employee> employeeStream = empListStream
    .flatMap(employeeList -> employeeList.stream());

employeeStream.forEach(System.out::println);
```

#### 2. findFirst() vs findAny()

**Question: What is the difference between findFirst() and findAny() in Streams?**

**Answer:**
- **findFirst()**: Returns the **first element in sequential order**. Guarantees order but might be slower in parallel execution
- **findAny()**: Returns **any element**, especially useful for parallel streams. Optimized for performance in parallel streams and does not guarantee order

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// Sequential - both return first element
Optional<Integer> first = numbers.stream().findFirst();
Optional<Integer> any = numbers.stream().findAny();

// Parallel - findFirst() maintains order, findAny() may return any
Optional<Integer> firstParallel = numbers.parallelStream().findFirst();
Optional<Integer> anyParallel = numbers.parallelStream().findAny();
```

#### 3. limit() vs skip()

**Question: What is the difference between limit() and skip()?**

**Answer:**
- **limit(n)**: Returns **first n elements** from a stream
- **skip(n)**: **Skips first n elements** and returns the rest

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

numbers.stream().limit(3).forEach(System.out::print); // Output: 123
numbers.stream().skip(3).forEach(System.out::print);  // Output: 45
```

#### 4. distinct()

**Question: How to remove duplicates from a List using Java 8? (Tricky)**

**Answer:** Using `distinct()` method

```java
List<Integer> numbers = Arrays.asList(10, 12, 27, 33, 54, 44, 54, 33);

List<Integer> uniqueNumbers = numbers.stream()
    .distinct()
    .collect(Collectors.toList());

System.out.println(uniqueNumbers); // [10, 12, 27, 33, 54, 44]
```

#### 5. sorted()

**Question: How do you sort the elements in a List using Java 8?**

**Answer:** Using `sorted()` method

```java
// Ascending order
Arrays.asList("Java", "Angular", "CSS", "Html")
    .stream()
    .sorted()
    .forEach(System.out::println);

// Descending order
Arrays.asList("Java", "Angular", "CSS", "Html")
    .stream()
    .sorted(Comparator.reverseOrder())
    .forEach(System.out::println);
```

#### 6. forEach() vs forEachOrdered()

**Question: How does forEachOrdered() behave differently from forEach() in parallel streams? (Tricky)**

**Answer:**
- **forEach()** in parallel streams does **not guarantee order**
- **forEachOrdered()** maintains the **original order**, even in parallel streams

```java
// Using forEach (unordered in parallel)
IntStream.range(1, 10).parallel().forEach(n -> System.out.print(n + " "));
// Output: 7 3 5 6 8 2 4 9 1 (unordered)

// Using forEachOrdered (ordered even in parallel)
IntStream.range(1, 10).parallel().forEachOrdered(n -> System.out.print(n + " "));
// Output: 1 2 3 4 5 6 7 8 9 (ordered)
```

#### 7. Infinite Streams

**Question: How can you create an infinite Stream in Java 8? (Tricky)**

**Answer:** Using `generate()` method

```java
// Create an infinite stream
Stream.generate(() -> "Hello")
    .limit(10)  // Limit to prevent infinite loop
    .forEach(num -> System.out.println(num));
```

#### 8. reduce()

**Question: What is reduce() and how does it work in Streams?**

**Answer:**
- `reduce()` combines elements of a stream into a **single value**
- It takes an **identity**, an **accumulator function**, and a **combiner** (for parallel execution)

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// Sum of all numbers
int sum = numbers.stream()
    .reduce(0, (a, b) -> a + b);

// Product of all numbers
int product = numbers.stream()
    .reduce(1, (a, b) -> a * b);
```

#### 9. Collectors.groupingBy()

**Question: How to group elements in a collection?**

**Answer:** `Collectors.groupingBy()` is used to group data

```java
// Grouping by author
Map<String, List<Book>> booksByAuthor = books.stream()
    .collect(Collectors.groupingBy(Book::getAuthor));

System.out.println(booksByAuthor);
```

#### 10. Collectors.toMap() vs Collectors.groupingBy()

**Question: What is the difference between Collectors.toMap() and Collectors.groupingBy()?**

**Answer:**
- **Collectors.toMap()**: Converts a stream into a **single map**, but throws an exception if there are **duplicate keys**
- **Collectors.groupingBy()**: Groups elements by a key and **allows multiple values per key**

```java
// toMap() - fails with duplicate keys
Map<String, Integer> map = list.stream()
    .collect(Collectors.toMap(Item::getName, Item::getPrice));
// Throws IllegalStateException if duplicate keys exist

// groupingBy() - handles duplicates
Map<String, List<Item>> grouped = list.stream()
    .collect(Collectors.groupingBy(Item::getName));
// Groups all items with same name
```

---

## 8. Optional Class

### What is Optional in Java 8 and why is it useful?

**Answer:**
- **Optional** is a container object that **may or may not contain a value**
- Helps to **avoid NullPointerException**
- Always **check the optional** before printing the value

### Key Methods of Optional:

| Method | Purpose |
|--------|---------|
| `get()` | Returns the value if present, throws NoSuchElementException if not |
| `isEmpty()` | Returns true if value is not present |
| `isPresent()` | Returns true if value is present |
| `orElse(T other)` | Returns value if present, otherwise returns other |
| `orElseGet(Supplier<T> other)` | Returns value if present, otherwise invokes supplier |
| `orElseThrow()` | Returns value if present, otherwise throws NoSuchElementException |

### Example:

```java
Optional<String> name = Optional.ofNullable("Great day");
System.out.println(name.orElse("Welcome"));

// Safe access
Optional<String> optional = Optional.ofNullable(getName());
if (optional.isPresent()) {
    String value = optional.get();
    System.out.println(value);
}
```

### orElse() vs orElseGet() - Important Difference!

**Question: What is the difference between orElse() and orElseGet()? (Tricky)**

**Answer:**
- **orElse()**: **Always evaluates** the fallback value, even if it's not needed
- **orElseGet()**: **Only evaluates** when the value is missing, avoiding unnecessary computation

```java
public static void main(String[] args) {
    Optional<String> opt = Optional.of("Great Day");
    
    System.out.println("Using orElse");
    String value1 = opt.orElse(printDefault());  // ❌ printDefault() is ALWAYS called
    System.out.println(value1);
    // Output:
    // Processing (always executed)
    // Great Day
    
    System.out.println("Using orElseGet");
    String value2 = opt.orElseGet(() -> printDefault());  // ✅ printDefault() only called if needed
    System.out.println(value2);
    // Output:
    // Great Day (printDefault() not called)
}

static String printDefault() {
    System.out.println("Processing");
    return "welcome";
}
```

**Key Takeaway:** Use `orElseGet()` when the fallback value is expensive to compute!

---

## 9. Coding Interview Questions - Strings

### Basics Questions:

#### 1. Convert a list of strings to uppercase, sort them and print

```java
List<String> fruit = Arrays.asList("Apple", "Orange", "PineApple");

fruit.stream()
    .map(str -> str.toUpperCase())
    .sorted()
    .forEach(System.out::println);
```

#### 2. Get the list of strings where the length of each element is > 5 and get the count

```java
List<String> fruit = Arrays.asList("Apple", "Orange", "PineApple");

long count = fruit.stream()
    .filter(word -> word.length() > 5)
    .count();

System.out.println(count);
```

#### 3. Remove duplicates from a list

```java
List<String> fruit = Arrays.asList("Apple", "Orange", "Apple", "PineApple");

fruit.stream()
    .distinct()
    .forEach(System.out::println);
```

#### 4. Get the list of strings having a particular word

```java
List<String> fruit = Arrays.asList("Apple", "Orange", "PineApple");

fruit.stream()
    .filter(str -> str.contains("Apple"))
    .forEach(System.out::println);
```

#### 5. Remove strings that start with a specific word from a list

```java
List<String> fruit = Arrays.asList("Apple", "Orange", "PineApple");

fruit.stream()
    .filter(str -> !str.startsWith("A"))
    .forEach(System.out::println);
```

#### 6. Find the first element in a list that starts with a particular letter

```java
List<String> fruit = Arrays.asList("Apple", "Orange", "PineApple");

Optional<String> op = fruit.stream()
    .filter(str -> str.startsWith("O"))
    .findFirst();

if (op.isPresent()) {
    String st = op.get();
    System.out.println(st);
}
```

#### 7. Get the length of each name in a list

```java
List<String> fruit = Arrays.asList("Apple", "Orange", "PineApple");

fruit.stream()
    .map(str -> str.length())
    .forEach(System.out::println);
```

### Intermediate Questions:

#### 8. Sort the list of strings based on their lengths (ascending/descending)

```java
List<String> fruit = Arrays.asList("Apple", "Orange", "PineApple");

// Ascending order
fruit.stream()
    .sorted(Comparator.comparing(String::length))
    .forEach(System.out::println);

// Descending order
fruit.stream()
    .sorted(Comparator.comparing(String::length).reversed())
    .forEach(System.out::println);

// Alternative way
fruit.stream()
    .sorted((o1, o2) -> Integer.compare(o1.length(), o2.length()))
    .forEach(System.out::println);
```

#### 9. Find the longest string in the list

```java
List<String> fruit = Arrays.asList("Apple", "Orange", "PineApple");

Optional<String> op = fruit.stream()
    .max(Comparator.comparing(String::length));

op.ifPresent(str -> System.out.println(str.toUpperCase()));
```

#### 10. Get the list of strings having vowels

```java
List<String> fruit = Arrays.asList("Apple", "Orange", "PineApple");

fruit.stream()
    .filter(str -> str.matches(".*[aeiou].*"))
    .forEach(System.out::println);
```

#### 11. Convert a list of strings into a single string, separated by commas

```java
List<String> fruit = Arrays.asList("Apple", "Orange", "PineApple");

String result = fruit.stream()
    .collect(Collectors.joining(","));

System.out.println(result); // Apple,Orange,PineApple
```

#### 12. Create a new list of strings, where each string is reversed

```java
List<String> fruit = Arrays.asList("Apple", "Orange", "PineApple");

fruit.stream()
    .map(str -> new StringBuffer(str).reverse().toString())
    .forEach(System.out::println);
```

#### 13. Remove all strings from the list that are empty or null

```java
List<String> fruit = Arrays.asList("Apple", "", null, "Orange", "PineApple");

fruit.stream()
    .filter(str -> str != null && !str.isEmpty())
    .forEach(System.out::println);
```

### Advanced Questions:

#### 14. Group a list of strings by the first letter of each string

```java
List<String> fruit = Arrays.asList("Apple", "Orange", "PineApple", "Apricot");

Map<Character, List<String>> grouped = fruit.stream()
    .collect(Collectors.groupingBy(str -> str.charAt(0)));

System.out.println(grouped);
```

#### 15. Group a list of strings by the length of the string

```java
List<String> fruit = Arrays.asList("Apple", "Orange", "PineApple");

Map<Integer, List<String>> grouped = fruit.stream()
    .collect(Collectors.groupingBy(String::length));

System.out.println(grouped);
```

#### 16. Create a map where key is first letter, value is list of strings starting with that letter

```java
List<String> fruit = Arrays.asList("Apple", "Orange", "PineApple", "Apricot");

Map<Character, List<String>> map = fruit.stream()
    .collect(Collectors.groupingBy(str -> str.charAt(0)));

System.out.println(map);
```

#### 17. Group strings with vowels and no vowels into two lists

```java
List<String> fruit = Arrays.asList("Apple", "Orange", "PineApple", "xyz");

Map<Boolean, List<String>> partitioned = fruit.stream()
    .collect(Collectors.partitioningBy(str -> str.matches(".*[aeiou].*")));

System.out.println(partitioned);
```

#### 18. Find frequency of a character in a string

```java
String input = "avinash";

// Count frequency of 's'
long count = input.chars()
    .filter(ch -> ch == 's')
    .count();

System.out.println("Frequency of 's': " + count);
```

#### 19. Find frequency of each character in a string

```java
String input = "avinash";

Map<Character, Long> frequencyMap = input.chars()
    .mapToObj(ch -> (char) ch)
    .collect(Collectors.groupingBy(
        Function.identity(),
        Collectors.counting()
    ));

System.out.println(frequencyMap);
```

---

## 10. Coding Interview Questions - Numbers

### Basics Questions:

#### 1. Get even/odd numbers from array/list

```java
// From List
List<Integer> nums = Arrays.asList(12, 67, 86, 53, 11, 90);
nums.stream()
    .filter(num -> num % 2 == 0)
    .forEach(System.out::println);

// From Array
int[] arr = new int[]{1, 2, 3, 4, 5, 6, 7};
Arrays.stream(arr)
    .filter(num -> num % 2 == 0)
    .forEach(System.out::println);

// Even numbers in a range
IntStream.rangeClosed(10, 100)
    .filter(num -> num % 2 == 0)
    .forEach(System.out::println);
```

#### 2. Sort a list of integers in ascending/descending order

```java
List<Integer> ls = Arrays.asList(2, 3, 4, 5, 112, 71, 70);

// Ascending
ls.stream()
    .sorted()
    .forEach(System.out::println);

// Descending
ls.stream()
    .sorted((a, b) -> b - a)
    .forEach(System.out::println);

// Alternative way for descending
ls.stream()
    .sorted(Comparator.comparingInt(Integer::intValue).reversed())
    .forEach(System.out::println);

// For arrays
int[] nums = new int[]{2, 4, 21, 8, 6, 10};
Arrays.stream(nums)
    .boxed()
    .sorted((a, b) -> b - a)
    .forEach(System.out::println);
```

#### 3. Calculate sum and average of numbers

```java
List<Integer> nums = Arrays.asList(12, 4, 6, 8, 10);

// Sum
int sum = nums.stream()
    .mapToInt(num -> num.intValue())
    .sum();
System.out.println("Sum: " + sum);

// Average
OptionalDouble average = nums.stream()
    .mapToDouble(num -> num.doubleValue())
    .average();
average.ifPresent(avg -> System.out.println("Average: " + avg));
```

#### 4. Find maximum/minimum number

```java
List<Integer> nums = Arrays.asList(12, 67, 86, 53, 11, 90);

// Maximum
Optional<Integer> max = nums.stream()
    .max(Comparator.comparing(Integer::valueOf));
System.out.println("Max: " + max.get());

// Using IntStream
int maxInt = nums.stream()
    .mapToInt(Integer::intValue)
    .max()
    .getAsInt();

// Minimum
Optional<Integer> min = nums.stream()
    .min(Comparator.comparing(Integer::valueOf));
System.out.println("Min: " + min.get());
```

### Intermediate Questions:

#### 5. Remove duplicate elements

```java
List<Integer> numbers = Arrays.asList(10, 12, 27, 33, 54, 44, 54, 33);

List<Integer> unique = numbers.stream()
    .distinct()
    .collect(Collectors.toList());

System.out.println(unique);
```

#### 6. Convert to squares and print

```java
List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);

// From List
nums.stream()
    .map(num -> num * num)
    .forEach(System.out::println);

// From Array
int[] numsarr = new int[]{1, 2, 3, 4, 5};
Arrays.stream(numsarr)
    .mapToDouble(num -> Math.pow(num, 2))
    .mapToInt(doubleval -> (int) doubleval)
    .forEach(System.out::println);
```

#### 7. Find sum of squares

```java
List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);

// Using map and reduce
int squareSum = nums.stream()
    .map(num -> num * num)
    .reduce(0, (a, b) -> a + b);
System.out.println("Square Sum: " + squareSum);

// From Array
int[] numsarr = new int[]{1, 2, 3, 4, 5};
double sum = Arrays.stream(numsarr)
    .mapToDouble(num -> Math.pow(num, 2))
    .sum();
System.out.println("Sum: " + sum);
```

#### 8. Get second largest and second smallest number

```java
List<Integer> nums = Arrays.asList(12, 67, 86, 53, 11, 90, 82, 86, 25);

// Second largest
Optional<Integer> secondLargest = nums.stream()
    .sorted((a, b) -> b - a)
    .skip(1)
    .findFirst();
System.out.println("Second Largest: " + secondLargest.get());

// Second smallest
Optional<Integer> secondSmallest = nums.stream()
    .sorted()
    .skip(1)
    .findFirst();
System.out.println("Second Smallest: " + secondSmallest.get());
```

#### 9. Get first number greater than 50

```java
List<Integer> nums = Arrays.asList(12, 67, 86, 53, 11, 90);

Optional<Integer> first = nums.stream()
    .filter(num -> num > 50)
    .findFirst();

first.ifPresent(System.out::println);
```

### Advanced Questions:

#### 10. Sum of numbers in a two-dimensional array

```java
int[][] twoDArray = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};

int sum = Arrays.stream(twoDArray)
    .flatMapToInt(Arrays::stream)
    .sum();

System.out.println("Sum: " + sum);
```

#### 11. Convert array to map (key: number, value: square)

```java
int[] nums = new int[]{1, 2, 3, 4, 5};

Map<Integer, Integer> map = Arrays.stream(nums)
    .boxed()
    .collect(Collectors.toMap(
        num -> num,
        num -> num * num
    ));

System.out.println(map);
```

#### 12. Partition into even and odd groups

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

Map<Boolean, List<Integer>> partitioned = numbers.stream()
    .collect(Collectors.partitioningBy(num -> num % 2 == 0));

System.out.println("Even: " + partitioned.get(true));
System.out.println("Odd: " + partitioned.get(false));
```

---

## 11. Coding Interview Questions - Employee Objects

### Employee Class Structure:

```java
public class Employee {
    private String empName;
    private int empId;
    private String city;
    private double salary;
    private String department;
    
    // Constructors, getters, setters
    public Employee(String empName, int empId, double salary, String city, String department) {
        this.empName = empName;
        this.empId = empId;
        this.salary = salary;
        this.city = city;
        this.department = department;
    }
    
    // Getters
    public String getEmpName() { return empName; }
    public int getEmpId() { return empId; }
    public String getCity() { return city; }
    public double getSalary() { return salary; }
    public String getDepartment() { return department; }
}
```

### Sample Data:

```java
List<Employee> employees = Arrays.asList(
    new Employee("Harini", 12888, 60000, "Cochin", "Accounts"),
    new Employee("Raj", 12889, 70000, "Bengaluru", "Testing"),
    new Employee("Priya", 12890, 80000, "Chennai", "HR"),
    new Employee("Kumar", 12891, 90000, "Bengaluru", "IT"),
    new Employee("Sita", 12892, 75000, "Cochin", "Accounts")
);
```

### Basics Questions:

#### 1. Find all employees from a specific city and get the count

```java
// Filter by city
employees.stream()
    .filter(emp -> emp.getCity().equals("Bengaluru"))
    .forEach(System.out::println);

// Get count
long totalCount = employees.stream()
    .filter(emp -> emp.getCity().equals("Bengaluru"))
    .count();
System.out.println("Total count: " + totalCount);
```

#### 2. Find all employees where salary is greater than a particular value

```java
employees.stream()
    .filter(emp -> emp.getSalary() > 70000)
    .forEach(System.out::println);
```

#### 3. Print all employee names in uppercase and alphabetical order

```java
employees.stream()
    .map(emp -> emp.getEmpName().toUpperCase())
    .sorted()
    .forEach(System.out::println);
```

#### 4. Get the first employee where dept is "HR" and if not available throw exception

```java
Employee hrEmployee = employees.stream()
    .filter(emp -> emp.getDepartment().equals("HR"))
    .findFirst()
    .orElseThrow(() -> new RuntimeException("No HR employee found"));

System.out.println(hrEmployee);
```

#### 5. Get the total salary of all employees in a specific department

```java
// Method 1: Using mapToDouble and sum
double totalSalary = employees.stream()
    .filter(emp -> emp.getDepartment().equals("Accounts"))
    .mapToDouble(Employee::getSalary)
    .sum();
System.out.println("Total Salary: " + totalSalary);

// Method 2: Using Collectors.summingDouble
double total = employees.stream()
    .filter(emp -> emp.getDepartment().equals("Testing"))
    .collect(Collectors.summingDouble(Employee::getSalary));
System.out.println("Total Salary: " + total);
```

### Intermediate Questions:

#### 6. Check if all employees have salary greater than a specific value

```java
boolean allHighSalary = employees.stream()
    .allMatch(emp -> emp.getSalary() > 50000);
System.out.println("All have salary > 50000: " + allHighSalary);
```

#### 7. Get the sum and average of salaries of all employees

```java
// Sum
double totalSalary = employees.stream()
    .mapToDouble(Employee::getSalary)
    .sum();
System.out.println("Total Salary: " + totalSalary);

// Average
OptionalDouble average = employees.stream()
    .mapToDouble(Employee::getSalary)
    .average();
average.ifPresent(avg -> System.out.println("Average Salary: " + avg));
```

#### 8. Get a Map of Employee Names and their salaries

```java
Map<String, Double> empSalaryMap = employees.stream()
    .collect(Collectors.toMap(
        Employee::getEmpName,
        Employee::getSalary
    ));

System.out.println(empSalaryMap);
```

#### 9. Group employees by department

```java
Map<String, List<Employee>> employeesByDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment));

System.out.println(employeesByDept);
```

#### 10. Get employees with maximum and minimum salary

```java
// Maximum salary
Optional<Employee> maxSalaryEmp = employees.stream()
    .max(Comparator.comparing(Employee::getSalary));
maxSalaryEmp.ifPresent(emp -> System.out.println("Max Salary: " + emp));

// Minimum salary
Optional<Employee> minSalaryEmp = employees.stream()
    .min(Comparator.comparing(Employee::getSalary));
minSalaryEmp.ifPresent(emp -> System.out.println("Min Salary: " + emp));
```

---

## 12. Advanced Stream Operations

### Parallel Streams

**Question: When to use parallel streams? (Tricky)**

**Answer:**
- Use for **large datasets** that require parallel execution
- Avoid for **small datasets** or cases with high overhead of thread management

```java
int sum = IntStream.range(100, 2000)
    .parallel()
    .sum();
System.out.println(sum);
```

### Primitive Streams

**Question: What are primitive streams?**

**Answer:**
- **Special streams** for working with primitive data types: `int`, `long`, and `double`
- Types: **IntStream**, **LongStream**, **DoubleStream**
- Uses **specialized lambda expressions** - e.g., `IntFunction`, `IntPredicate`
- Supports terminal aggregate operations: `sum()` and `average()`

```java
IntStream stream = Arrays.stream(new int[] { 40, 20, 30, 91, 16, 7 });
int sum = stream.filter(x -> x > 20).sum();
System.out.println(sum);
```

### Important Notes:

1. **Streams don't allow modification** of source while iterating - throws `ConcurrentModificationException`
2. **forEach()** doesn't guarantee order in parallel streams
3. **forEachOrdered()** maintains order even in parallel streams
4. Use **orElseGet()** instead of **orElse()** when fallback computation is expensive
5. **distinct()** removes duplicates based on `equals()` method
6. **sorted()** requires elements to be `Comparable` or provide a `Comparator`

---

## 🎯 Summary - Key Points to Remember

### Functional Interfaces:
- Must have exactly one abstract method
- Can be implemented using Lambda expressions
- Common ones: Consumer, Supplier, Predicate, Function

### Lambda Expressions:
- Anonymous functions
- Can only access effectively final local variables
- Syntax: `(parameters) -> expression`

### Default Methods:
- Add functionality to existing interfaces
- Can be overridden
- Resolve conflicts by overriding in implementing class

### Static Methods:
- Called using interface name
- Cannot be overridden

### Streams:
- Lazy evaluation for intermediate operations
- Terminal operations trigger execution
- Don't modify source
- Can be parallel or sequential

### Optional:
- Prevents NullPointerException
- Use `orElseGet()` for expensive computations
- Always check before accessing value

---

## 📚 Practice Tips

1. **Understand the difference** between intermediate and terminal operations
2. **Know when to use** `map()` vs `flatMap()`
3. **Remember** `findFirst()` vs `findAny()` for parallel streams
4. **Practice** grouping and partitioning operations
5. **Master** Optional to avoid null checks
6. **Understand** the difference between `orElse()` and `orElseGet()`

---

**Happy Learning! 🚀**

