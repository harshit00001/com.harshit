# java-interview-lab (Java 17)

Plain Java **17** project — runnable demos with **elaborative Javadoc** matching `../01-..19-*/INTERVIEW.md` topics (core Java + patterns + coding).

## Build & run

```bash
cd java-interview-lab
mvn -q compile
mvn -q exec:java
```

Requires **JDK 17+** on `PATH`.

## Packages

| Package | Topics |
|---------|--------|
| `collections` | HashMap, HashSet, custom key |
| `string` | Pool, equals, StringBuilder |
| `serialization` | Immutability, serialVersionUID |
| `compare` | Comparable vs Comparator |
| `jvm` | Heap/stack snapshot via `Runtime` |
| `concurrency` | synchronized, Callable vs Runnable |
| `streams` | filter, flatMap, Optional |
| `oop.polymorphism` | Strategy / polymorphism |
| `oop.defaults` | Default method diamond conflict |
| `oop` | Static hiding |
| `patterns` | Strategy + factory map |
| `coding` | Duplicates, common elements, sliding window, palindrome |

Spring / Kafka / JPA remain in Markdown under `../10-..16-` (add a Spring Boot module later if you want).
