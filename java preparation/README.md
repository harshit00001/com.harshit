# Java interview preparation

## Layout

- **`pom.xml`** (root) — single Maven project, **Java 17**, compiles all topic sources.
- **`01-...` through `19-...`** — one folder per topic.
- Each topic has: **`src/main/java/com/harshit/preparation/topicNN/TopicNNQa.java`**
  - Block comments use **`SCRIPT:`** — short **spoken-style** paragraphs (how you answer in the panel), not bullet lists. Some blocks add **`REAL LIFE:`** for a one-line analogy. **Code** stays below the script where it helps.

## Build

```bash
cd "java preparation"
mvn -q compile
```

Use **JDK 17+**. Older JDKs will fail on `--release 17`.

## Run demos

`InterviewPrepRunner` runs each topic’s **`demo()`** (or **`examples()`** for topic 01). Every **`TopicNNQa`** also has **`main`** so you can run that class alone in the IDE.

```bash
mvn -q compile exec:java
mvn -q exec:java -Dexec.args="04"
```

From the IDE, run **`com.harshit.preparation.InterviewPrepRunner`** with program arguments `all` or `01` … `19`, or run any **`TopicNNQa`** directly.

## Topic → file

| Folder | Class |
|--------|--------|
| `01-hashmap-collections` | `Topic01Qa` |
| `02-oop-basics` | `Topic02Qa` |
| … | … |
| `19-agile` | `Topic19Qa` |

## Reverted / removed

- Old **`java-interview-lab`** and **`spring-interview-lab`** projects removed.
- **`INTERVIEW.md`** / **`MASTER_QUESTIONS.md`** removed — content lives in **`.java`** files now.
