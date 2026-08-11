# Java 8 & Multithreading Interview Prep (Accenture ~4 YOE)

Standalone Maven project with **runnable demos**, **deep-dive Javadoc comments**, and **interview scripts** for Java 8 features and concurrency.

## Quick start

```bash
cd JavaCoding/java8-multithreading-interview-prep
mvn -q compile
mvn -q exec:java
```

Run a specific demo:

```bash
mvn -q exec:java -Dexec.args="java8 5"
mvn -q exec:java -Dexec.args="mt 6"
mvn -q exec:java -Dexec.args="all"
```

Or run any class directly from your IDE — each `Qxx*.java` has a `main` method.

## Project layout

```
src/main/java/com/harshit/interview/
├── InterviewPrepRunner.java          # Menu / run all
├── common/InterviewDemo.java
├── model/Employee.java               # Sample domain for streams
├── java8/                            # Java 8 topics
│   Q01LambdaAndFunctionalInterfaces
│   Q02MethodReferences
│   Q03OptionalDeepDive
│   Q04StreamPipelineLazyEvaluation
│   Q05CollectorsGroupingAndPartitioning
│   Q06ParallelStreamsPitfalls
│   Q07DateTimeApi
│   Q08DefaultAndStaticInterfaceMethods
│   Q09StreamsCodingInterviewQuestions
│   Q10EmployeeStreamScenarios          # Accenture Employee list classic
│   Q11ReduceMatchAndTerminalOps
│   Q12ComparatorAndSorting
│   Q13PrimitiveStreams
│   Q14FunctionalComposition
│   Q15StreamPitfallsAndBestPractices
│   Q16StringProcessingWithStreams
└── multithreading/
    Q01ThreadBasicsAndLifecycle
    Q02SynchronizedVsReentrantLock
    Q03VolatileVisibility
    Q04WaitNotifyAndBlockingQueue
    Q05ExecutorServiceAndThreadPools
    Q06CallableFutureCompletableFuture
    Q07ConcurrentCollections
    Q08AtomicVsSynchronized
    Q09CountDownLatchBarrierSemaphore
    Q10DeadlockDetectionAndPrevention
    Q11ThreadLocalDemo
    Q12StreamsOnNumbersInterviewQuestions
    Q13ReadWriteLockDemo
    Q14ForkJoinPoolDemo
    Q15DoubleCheckedLocking
    Q16InterruptHandling
    Q17ProducerConsumerBlockingQueue
    Q18CompletableFutureAdvanced
    Q19HappensBeforeRules
    Q20RaceConditionAndFix
```

## How to use for Accenture interviews

1. **Read the class-level Javadoc** — it is written as Q&A + “4 YOE angle”.
2. **Run the demo** — observe console output; explain laziness, lost updates, etc.
3. **Practice 60-second answers** using the bullet points in comments.
4. **Cross-link** with your `Java8-Streams-API-Interview-QA.md` for extra string/employee coding drills.

## Topic checklist (4 years experience)

### Java 8 (16 demos)
- [ ] Q01–Q09: Core lambda, streams, Optional, collectors, parallel, time, interfaces
- [ ] Q10: Employee stream scenarios (count, filter, group, top N) — **Accenture favorite**
- [ ] Q11: reduce, anyMatch/allMatch, findFirst vs findAny
- [ ] Q12: Comparator.comparing / thenComparing / nullsLast
- [ ] Q13: IntStream, range, summaryStatistics, boxing cost
- [ ] Q14: Predicate.and / Function.compose
- [ ] Q15: Stream reuse, side effects, when loop is better
- [ ] Q16: Reverse words, char frequency — **coding round**

### Multithreading (20 demos)
- [ ] Q01–Q12: Core concurrency toolkit
- [ ] Q13: ReadWriteLock for read-heavy cache
- [ ] Q14: ForkJoinPool & parallelStream link
- [ ] Q15: Double-checked locking + holder idiom
- [ ] Q16: Interrupt handling (restore interrupt flag)
- [ ] Q17: Producer-consumer with bounded BlockingQueue
- [ ] Q18: CompletableFuture thenCombine / applyToEither — **microservices**
- [ ] Q19: Happens-before rules (JMM)
- [ ] Q20: Race condition demo + AtomicInteger fix

## Requirements

- Java 17+
- Maven 3.8+

## Related projects in this repo

- `JavaCoding/java-collections-interview` — Collections & iteration
- `JavaCoding/java preparation/08-concurrency-multithreading` — short script Q&A
- `JavaCoding/java preparation/09-java8-streams` — short script Q&A
- `Java8-Streams-API-Interview-QA.md` — extended streams cheat sheet
