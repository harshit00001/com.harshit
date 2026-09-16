# JVM GC Interview Lab

> **Start here: [GC_STEP_BY_STEP.md](GC_STEP_BY_STEP.md)** — the full walkthrough in prose, with the
> heap diagram, short code snippets and the real measured output for all 12 steps. Read that to
> *learn* the material. This README is the reference for *running* the code.

Twelve garbage-collection interview questions, each answered by a program that **measures** the JVM
rather than describing it. Every number in these docs came from running the step on JDK 17 (G1).

The point is not to memorise definitions. It is to be the candidate who says *"I measured it, and
here is what the counters said"* — and who knows when the folklore is wrong.

## Running it

JDK 17 on this machine lives at `C:\Users\harshraj\OneDrive - AMDOCS\Bell Canada\Java`, which
`run.ps1` picks up automatically when `JAVA_HOME` is not already set.

```powershell
.\run.ps1                       # list all steps
.\run.ps1 step02                # young GC / Eden
.\run.ps1 step07 leak           # leak signature
.\run.ps1 step07 churn          # allocation-pressure signature
.\run.ps1 step08 fixed          # ThreadLocal with remove()
.\run.ps1 step10 -Collector serial    # same workload, different collector
.\run.ps1 step12 -NoEscapeAnalysis    # turn off scalar replacement
```

Each step needs its own JVM flags (small heaps, GC logging, direct-memory ceilings), which is why
they are separate runs. Without `run.ps1`:

```powershell
java -Xms96m -Xmx96m -Xlog:gc -cp target/classes com.harshit.gclab.GcLab step07 leak
```

## The 12 steps

| Step | Question it answers | What the run proves |
|------|--------------------|---------------------|
| `step01` | Which collector am I on and how is the heap laid out? | Reads collectors, pools and the flags that matter, straight from the JVM |
| `step02` | What happens between allocation and a minor GC? | 300 MB of traffic flows through a 64 MB heap in 9 young GCs |
| `step03` | What are GC roots? Does the JVM reference-count? | A mutually-referencing pair is collected; a static field keeps its object alive |
| `step04` | How does promotion and tenuring work? | Old gen grows, then the live-set-after-GC plateaus at the real live set |
| `step05` | Minor vs major vs full GC in real numbers? | Young pause averages 1.63 ms; the full GC with 40 MB live takes 6.00 ms |
| `step06` | Soft vs weak vs phantom references? | Weak clears immediately; soft survives until 29 MB of pressure; phantom only enqueues |
| `step07` | Leak or allocation pressure? | Live set flat at 830 KB (churn) vs climbing 16 → 80 MB then OOM (leak) |
| `step08` | Why does ThreadLocal leak in a pool? | 20 MB retained by 4 pool threads; 36 KB with `remove()` |
| `step09` | What is a humongous allocation? | Eden peaks at 2 MB with humongous chunks vs 147 MB with small ones; `new byte[1MB]` costs 2 MB |
| `step10` | Why are stop-the-world pauses unavoidable? | G1: worst stall 16 ms, 10.7% of wall clock. Serial: 172 ms, 40.5% |
| `step11` | Why is a pod OOMKilled with a healthy heap? | 64 MB consumed off-heap while heap stays at 2 MB, then a direct-memory OOM |
| `step12` | Does escape analysis allocate on the stack? | 0.33 bytes/iteration vs exactly 24.00 with `-XX:-DoEscapeAnalysis` |

## The three readings that matter

Most GC confusion comes from watching the wrong number.

1. **Used heap** — includes garbage not yet collected. Useless on its own; it saws up and down.
2. **Live set after collection** (`MemoryPoolMXBean.getCollectionUsage()`) — bytes still used *after*
   the pool's last collection. This is the number that answers "is it a leak?".
3. **Per-collector counts and times** (`GarbageCollectorMXBean`) — tells you *which* collector ran,
   so you can distinguish a young collection from a full one without parsing a log.

`support/GcStats.java` wraps all three, and every step prints deltas rather than absolutes.

## Diagnosis cheat sheet

| Symptom in the numbers | Diagnosis | Next action |
|---|---|---|
| Live set climbs every collection, never returns | Memory leak | Heap dump, Eclipse MAT leak suspects, dominator tree |
| Live set flat, GC count high | Allocation pressure | JFR or async-profiler allocation profile; allocate less |
| Pause time up, bytes reclaimed down | Heading for `GC overhead limit exceeded` | Fix the leak or raise the heap; it will not recover on its own |
| Eden flat while heap grows | Humongous allocations | Smaller batches/pages, or larger `G1HeapRegionSize` |
| Heap fine, container killed with exit 137 | Native memory, not heap | `jcmd <pid> VM.native_memory summary`; lower `MaxRAMPercentage` |
| Long stalls, low GC time | Not GC — safepoint or JIT | `-Xlog:safepoint` for time-to-safepoint |

## Three details that make an interviewer look up

Each of these came out of actually running the lab, not from a blog post.

1. **`new byte[1024 * 1024]` can cost 2 MB of heap.** With 1 MB G1 regions, the 16-byte object header
   pushes the array into a second region. `step09` phase D measures it: 20 arrays of exactly 1 MB
   consume 40 MB, while 20 arrays 1 KB smaller consume 20 MB. This is why buffer sizes should be
   *just under* a power of two, not exactly on it.
2. **A ThreadLocal leak vanishes the moment the thread dies**, which is why it is so hard to
   reproduce in a test. `step08` shows 20 MB retained while the pool is alive and 854 KB immediately
   after `shutdown()` — so a test that shuts the pool down before asserting will always pass.
3. **Serial GC is not just "slower"**, it changes the shape of latency. Same workload in `step10`:
   G1's worst stall was 16 ms with nothing above 20 ms; Serial's worst was 172 ms with 19 stalls over
   20 ms, and GC consumed 40.5% of wall clock instead of 10.7%. That is the argument for never
   letting a small container silently fall back to Serial.

## Two places the folklore is wrong

Worth knowing, because saying these makes you sound like you have actually looked:

- **"Humongous allocations are always expensive."** Since JDK 8u60 G1 eagerly reclaims short-lived
  humongous regions during young collections, so `step09` phase A and B cost about the same. The real
  cost appears when they *survive*: pinning half the heap in humongous regions made young collections
  roughly ten times more frequent, because Eden had nowhere to grow.
- **"Escape analysis allocates the object on the stack."** HotSpot does *scalar replacement* — the
  object never exists, its fields become registers or stack slots. `step12` measures exactly 24 bytes
  per iteration with the optimisation off and effectively zero with it on.

## Flags used, and why

```
-Xms = -Xmx                      predictable footprint; no repeated heap growth
-XX:MaxRAMPercentage=70          heap from the container limit, leaving room for non-heap
-XX:MaxGCPauseMillis=200         G1's soft pause goal (default)
-Xlog:gc                         unified logging (Java 9+); Java 8 used -XX:+PrintGCDetails
-Xlog:gc+age=trace               tenuring distribution, for premature-promotion questions
-XX:+HeapDumpOnOutOfMemoryError  the one flag every production service should carry
-XX:MaxDirectMemorySize          bound off-heap so it fails loudly instead of growing into the limit
-XX:NativeMemoryTracking=summary pairs with `jcmd VM.native_memory` for RSS investigations
```

Deliberately **not** tuned: `-Xmn` and `NewRatio` under G1, because fixing the young size stops G1
resizing generations to meet its pause target.

## Related projects here

- `garbage-collector` — the conceptual walkthrough of the same topics
- `memory-management` — heap, stack and Metaspace layout notes
