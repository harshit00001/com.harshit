# Java 8 Features - Complete Guide

A comprehensive Java project covering all Java 8 features from basics to advanced, with interview-friendly explanations and practice questions.

## 📚 Topics Covered

### 1. **Lambda Expressions** (`basics/`)
- What are lambda expressions
- Syntax and usage
- Functional interfaces
- Method references

### 2. **Stream API** (`streams/`)
- Stream creation
- Intermediate operations (filter, map, sorted, etc.)
- Terminal operations (collect, forEach, reduce, etc.)
- Collectors
- Parallel streams

### 3. **Optional Class** (`optional/`)
- Creating Optional
- Common methods (isPresent, orElse, map, etc.)
- Best practices

### 4. **Practice Questions** (`streams/practice/`)
- 10 common Stream API interview questions
- Solutions with explanations
- Real-world examples

## 🎯 Interview Questions & Answers

### Q: What are Lambda Expressions in Java 8?

**Technical:**
- Anonymous functions (functions without names)
- Represent instances of functional interfaces
- Concise way to write code
- Syntax: `(parameters) -> expression` or `(parameters) -> { statements; }`

**Simple:**
- Short way to write a function
- Like a mini-function you can pass around
- Makes code more readable

**Example:**
```java
// Old way
Runnable r = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello");
    }
};

// Lambda way
Runnable r = () -> System.out.println("Hello");
```

---

### Q: What is a Functional Interface?

**Technical:**
- Interface with exactly one abstract method
- Can have multiple default/static methods
- Can be annotated with @FunctionalInterface
- Examples: Predicate, Function, Consumer, Supplier

**Simple:**
- Interface with only one method to implement
- Perfect for lambda expressions

**Example:**
```java
@FunctionalInterface
interface Calculator {
    int calculate(int a, int b);
}

Calculator add = (a, b) -> a + b;
```

---

### Q: What is the Stream API?

**Technical:**
- Sequence of elements supporting functional-style operations
- Not a data structure (doesn't store data)
- Lazy evaluation (operations executed on terminal operation)
- Can be consumed only once

**Simple:**
- Like a pipeline for processing data
- You can filter, transform, and collect
- Original data is not modified

**Example:**
```java
List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());
```

---

### Q: What is the difference between Intermediate and Terminal Operations?

**Technical:**
- **Intermediate**: Return Stream, lazy (not executed until terminal)
  - Examples: filter, map, sorted, distinct, limit
- **Terminal**: Consume stream, trigger execution
  - Examples: collect, forEach, reduce, count, findFirst

**Simple:**
- Intermediate: Build the pipeline (doesn't execute)
- Terminal: Actually run the pipeline and get result

**Example:**
```java
numbers.stream()
    .filter(n -> n > 5)      // Intermediate - not executed yet
    .map(n -> n * 2)         // Intermediate - not executed yet
    .collect(toList());      // Terminal - executes everything
```

---

### Q: What is Optional in Java 8?

**Technical:**
- Container that may or may not contain a value
- Prevents NullPointerException
- Forces explicit null handling
- Methods: isPresent(), orElse(), map(), flatMap()

**Simple:**
- Wrapper around value that might be null
- Forces you to check before using
- Prevents crashes from null

**Example:**
```java
Optional<String> name = Optional.ofNullable(getName());
String result = name.orElse("Default");
```

---

### Q: How do you find duplicates using Stream API?

**Technical:**
- Use groupingBy with counting
- Filter entries with count > 1
- Extract keys

**Example:**
```java
List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 3, 3, 4);

Set<Integer> duplicates = numbers.stream()
    .collect(Collectors.groupingBy(n -> n, Collectors.counting()))
    .entrySet().stream()
    .filter(entry -> entry.getValue() > 1)
    .map(Map.Entry::getKey)
    .collect(Collectors.toSet());
```

---

### Q: What is the difference between map() and flatMap()?

**Technical:**
- **map()**: Transforms each element to one element
  - Stream<T> → Stream<R>
- **flatMap()**: Transforms each element to stream, then flattens
  - Stream<T> → Stream<Stream<R>> → Stream<R>

**Simple:**
- map: One-to-one transformation
- flatMap: One-to-many, then flatten

**Example:**
```java
// map: List<String> → List<Integer>
List<Integer> lengths = words.stream()
    .map(String::length)
    .collect(toList());

// flatMap: List<List<Integer>> → List<Integer>
List<Integer> flattened = listOfLists.stream()
    .flatMap(List::stream)
    .collect(toList());
```

---

### Q: What are Method References?

**Technical:**
- Shorthand for lambda when calling existing method
- Types: Static, Instance, Constructor
- Syntax: Class::method or instance::method

**Simple:**
- Short way to write lambda when just calling a method
- Instead of `x -> System.out.println(x)`, use `System.out::println`

**Example:**
```java
// Lambda
list.forEach(s -> System.out.println(s));

// Method reference
list.forEach(System.out::println);
```

---

### Q: How do you group elements using Stream API?

**Technical:**
- Use `Collectors.groupingBy()`
- Groups by a key (function result)
- Returns Map<K, List<T>>

**Example:**
```java
Map<String, List<Employee>> byDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment));
```

---

### Q: What is the difference between findFirst() and findAny()?

**Technical:**
- **findFirst()**: Returns first element (deterministic)
- **findAny()**: Returns any element (non-deterministic, better for parallel)

**Simple:**
- findFirst: Always returns the first one
- findAny: Returns any one (faster in parallel streams)

---

## 📝 Common Stream Operations

### Intermediate Operations:
- `filter(Predicate)`: Keep elements matching condition
- `map(Function)`: Transform each element
- `sorted(Comparator)`: Sort elements
- `distinct()`: Remove duplicates
- `limit(n)`: Take first n elements
- `skip(n)`: Skip first n elements
- `peek(Consumer)`: Perform action without modifying

### Terminal Operations:
- `collect(Collector)`: Collect to collection
- `forEach(Consumer)`: Perform action on each
- `reduce(BinaryOperator)`: Reduce to single value
- `count()`: Count elements
- `anyMatch(Predicate)`: Check if any matches
- `allMatch(Predicate)`: Check if all match
- `findFirst()`: Get first element
- `findAny()`: Get any element

### Common Collectors:
- `toList()`, `toSet()`: Collect to list/set
- `groupingBy()`: Group by key
- `partitioningBy()`: Partition by predicate
- `joining()`: Join strings
- `counting()`: Count elements
- `averagingInt()`, `summingInt()`: Aggregate numbers

---

## 🚀 Practice Questions

1. Find all even numbers
2. Find max/min
3. Sum of numbers
4. Group by department
5. Find duplicates
6. Sort employees
7. Find first matching
8. Count occurrences
9. Flatten nested lists
10. Partition by condition

See `StreamPracticeQuestions.java` for solutions!

---

**Happy Learning! 🎓**

