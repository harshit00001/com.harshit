# How Java GC Actually Works — 12 Steps, Each Proved by Measurement

A step-by-step walkthrough of garbage collection for a Java backend interview. Every code snippet
here was actually run on **JDK 17.0.12, G1 collector**, and every output block is the real console
output — not an illustration of what it might look like.

Read it top to bottom once: the steps are ordered as an object's life story, then as a production
incident.

---

## The mental model first

```
HEAP
  YOUNG GENERATION                              OLD GENERATION
  +----------+   +--------+   +--------+        +--------------------+
  |   EDEN   |-->|   S0   |<->|   S1   |------->|    OLD / TENURED   |
  | new objs |   | surv 1 |   | surv 2 |        |  long-lived / big  |
  +----------+   +--------+   +--------+        +--------------------+
   allocate       copied on each young GC,       promoted after ~15
   here (TLAB)    age increments every time      young collections
   \____________________ minor GC ____________/  \___ full GC ____/
     frequent, cheap, cost = survivors             rare, expensive,
                                                   cost = live set

NON-HEAP  (GC tuning will not shrink these, but the container counts them)
  +-----------+  +------------+  +---------------+  +------------------+
  | Metaspace |  | Code cache |  | Thread stacks |  | Direct buffers   |
  +-----------+  +------------+  +---------------+  +------------------+
```

Two sentences that carry most of the theory:

1. **The weak generational hypothesis** — most objects die very young. So the JVM makes allocation
   trivially cheap in Eden and reclaims it in bulk, paying only for the few survivors.
2. **Reachability, not reference counting** — an object lives if a path exists from a *GC root*
   (live thread stacks, static fields, JNI handles, active monitors). Cycles are collected fine.

### The three numbers that matter

Almost all GC confusion comes from watching the wrong number.

| Number | Where it comes from | What it tells you |
|---|---|---|
| Used heap | `MemoryMXBean.getHeapMemoryUsage()` | Very little on its own — it includes garbage and saws up and down |
| **Live set after collection** | `MemoryPoolMXBean.getCollectionUsage()` | The number that answers "is this a leak?" |
| Per-collector count and time | `GarbageCollectorMXBean` | *Which* collector ran, so you can separate a young GC from a full GC |

---

## Step 1 — Which collector am I even on?

Never guess this in an interview; the JVM will tell you.

```java
for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
    System.out.println(gc.getName() + " manages " + gc.getMemoryPoolNames());
}
// And flags exactly as the JVM resolved them:
HotSpotDiagnosticMXBean diag = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);
System.out.println(diag.getVMOption("MaxGCPauseMillis").getValue());
```

**Real output** (`-Xms256m -Xmx256m`):

```
java 17.0.12 (Java HotSpot(TM) 64-Bit Server VM)
active collector: G1GC
G1 Young Generation            manages pools G1 Eden Space, G1 Survivor Space, G1 Old Gen
G1 Old Generation              manages pools G1 Eden Space, G1 Survivor Space, G1 Old Gen

HEAP POOL                      USED NOW LIVE AFTER GC
G1 Eden Space                    2.0 MB          0 B
G1 Old Gen                          0 B          0 B
G1 Survivor Space                   0 B          0 B
Metaspace                     1007.7 KB          <- non-heap
Compressed Class Space         105.3 KB          <- non-heap

MaxHeapSize          = 268435456     (-Xmx)
MaxRAMPercentage     = 25.0          (heap as a share of the container limit)
MaxGCPauseMillis     = 200           (G1's soft pause goal)
G1HeapRegionSize     = 1048576       (1 MB; humongous = bigger than half of this)
MaxTenuringThreshold = 15            (young GCs survived before promotion)
```

**Say this:** Java 8 defaults to Parallel, Java 9+ defaults to G1 on server-class machines. A small
container (under ~1792 MB or fewer than 2 CPUs) silently falls back to **Serial GC** — which is worth
knowing, because Step 10 shows what that costs.

---

## Step 2 — Allocation and the minor GC

```java
// 300 MB of garbage, 64 KB at a time, inside a 64 MB heap
for (int i = 0; i < 300 * 16; i++) {
    sink = new byte[64 * 1024];   // allocated in Eden, abandoned immediately
}
```

**Real output** (`-Xms64m -Xmx64m`):

```
 ALLOCED      EDEN USED      HEAP USED      YOUNG GCs
    30 MB        10.0 MB        11.0 MB              1
    60 MB        11.0 MB        12.0 MB              2
   120 MB         1.0 MB         2.0 MB              4     <- Eden just got emptied
   150 MB        33.0 MB        34.0 MB              4     <- G1 grew Eden on its own
   300 MB         9.0 MB        10.0 MB              9

Allocating 300 MB inside a 64.0 MB heap (wall clock 91 ms)
  G1 Young Generation      9 collections      16 ms total
  G1 Old Generation        0 collections       0 ms
  heap used: 2.0 MB -> 10.0 MB
```

**What it proves:** 300 MB of traffic flowed through a 64 MB heap using 9 collections and 16 ms of
total pause. Eden fills, gets emptied wholesale, and heap usage returns to its baseline.

**Say this:** Allocation is a pointer bump inside a thread-local allocation buffer (TLAB), so it needs
no locking. A minor GC copies the few survivors out and declares all of Eden free — **the cost scales
with survivors, not with garbage**. That is why generating a lot of short-lived garbage is not
automatically a problem.

---

## Step 3 — GC roots, and why cycles are not a problem

A `WeakReference` is the measuring instrument: after a collection it reads `null` only if the referent
was truly unreachable.

```java
static Node staticRoot;                       // a static field IS a GC root

Node a = new Node(), b = new Node();
a.peer = b;  b.peer = a;                      // they reference each other
WeakReference<Node> watch = new WeakReference<>(a);
a = null;  b = null;                          // no root reaches either one now
System.gc();
System.out.println(watch.get());              // null -> both were collected
```

**Real output:**

```
Case 1: dropped local variable                               -> COLLECTED
Case 2: still referenced by static field                     -> STILL ALIVE
        after clearing the static field                      -> COLLECTED
Case 3: mutually referencing pair, unreachable from any root  -> COLLECTED
        reference queue delivered a notification: true
```

**Say this:** Java is reachability-based, not reference-counted, so an "island of isolation" is
collected normally. The flip side is Case 2: static fields are GC roots, which is the mechanism behind
most real leaks — a `static Map` keeps everything it holds alive for the entire process lifetime.

---

## Step 4 — Survivor spaces, tenuring, and promotion

```java
Deque<byte[]> window = new ArrayDeque<>();     // a rolling "live" set, like open sessions
for (int round = 0; round < 12; round++) {
    for (int i = 0; i < 60; i++) {
        window.addLast(new byte[256 * 1024]);
        if (window.size() > 60) window.removeFirst();
        churn(8, 1_048_576);                  // garbage around the live set
    }
}
```

**Real output** (`-Xms128m -Xmx128m`, `MaxTenuringThreshold = 15`):

```
 ROUND       SURVIVOR        OLD GEN   OLD AFTER GC    YOUNG GCs
     1         4.3 MB        51.8 MB            0 B           19
     3         4.0 MB        58.8 MB            0 B          117
     4         3.8 MB        55.3 MB        27.5 MB          159   <- first old-gen collection
     8         2.5 MB        56.8 MB        27.5 MB          411
    12         3.3 MB        72.3 MB        26.8 MB          743   <- plateau, not a climb
```

**What it proves:** "OLD GEN" bounces around because it contains garbage. "OLD AFTER GC" — the live
set — settles at ~27 MB and **stays there**. That is a healthy application, not a leak.

**Say this:** Each young GC copies survivors between the two survivor spaces and increments their age;
past `MaxTenuringThreshold` (15 here) they are promoted to old gen. Old gen growing is normal for
anything request-spanning, like a cache entry or an open session. If survivor space is too small,
objects get promoted early — *premature promotion* — and old collections become more frequent. You
diagnose that with `-Xlog:gc+age=trace`.

---

## Step 5 — Minor vs major vs full GC, in milliseconds

```java
churn(400 * 4, 256 * 1024);                   // Phase 1: pure short-lived garbage
List<byte[]> live = retain(40);               // Phase 2: 40 MB that survives
System.gc();                                  // ask for a full collection
live.clear();
System.gc();                                  // Phase 3: same call, nothing live
```

**Real output** (`-Xms128m -Xmx128m`):

```
Phase 1: 400 MB of short-lived allocation
  average young pause: 1.63 ms over 8 collections

Phase 2: System.gc() with 40 MB live
  G1 Old Generation        1 collection       6 ms
  live set retained on purpose        : 40.0 MB
  old gen still used after the full GC: 41.1 MB   <- matches what we deliberately kept
  average full pause: 6.00 ms over 1 collections

Phase 3: System.gc() after dropping the references
  heap used: 41.1 MB -> 1.1 MB                    <- now it can actually be reclaimed
```

**Say this:** A minor GC touches young gen only — frequent, short, cheap. A full GC walks young, old
and Metaspace and usually compacts, so its cost tracks the **live set**: 6 ms here on a 128 MB heap,
but seconds on a multi-gigabyte one. Phase 2 versus Phase 3 makes the key point — the collector cannot
reclaim what you are still referencing, so "the GC is slow" is usually "your live set is large".

And: `System.gc()` is only a *hint*. It can be disabled outright with `-XX:+DisableExplicitGC`, so
calling it from application code is a code smell.

---

## Step 6 — Strong, soft, weak, phantom

```java
WeakReference<Object>   weak   = new WeakReference<>(target);
SoftReference<byte[]>   soft   = new SoftReference<>(new byte[4 << 20]);
PhantomReference<Object> phantom = new PhantomReference<>(target, queue);
```

**Real output** (`-Xms64m -Xmx64m`):

```
Weak:    with a strong reference still held  : present
         after dropping the strong reference : null (cleared)
Soft:    after a full GC with free heap      : present
         cleared under pressure              : CLEARED after 29 MB of pressure
Phantom: get() while the object is alive     : null (always null, by design)
         enqueued after collection           : true
WeakHashMap: entries left after GC: 1 (the one whose key we still hold)
```

| Type | Cleared when | Use it for |
|---|---|---|
| Strong | Never, while reachable | Everything normal |
| Soft | Only under memory pressure | Memory-sensitive caches — but you cannot predict *when* |
| Weak | At the next GC once only weakly reachable | Canonical maps, metadata keyed on another object's life (`WeakHashMap`, `ThreadLocalMap` keys) |
| Phantom | Enqueued *after* collection; `get()` always null | Native resource cleanup — the modern API on top of it is `Cleaner` |

**Say this:** `finalize()` is deprecated for good reasons — unpredictable timing, it can resurrect
objects, and it delays reclamation by an extra GC cycle. Use `Cleaner` or plain
try-with-resources. And in a Spring service I would reach for a bounded Caffeine cache before soft
references, because eviction you control beats eviction the GC guesses at.

---

## Step 7 — The production question: leak or allocation pressure?

Same program, same total bytes allocated. Only retention differs.

```java
static final List<byte[]> CACHE = new ArrayList<>();   // the accidental static cache

byte[] payload = new byte[1 << 20];
if (leaking) CACHE.add(payload);                        // leak mode retains it
// then read the live set AFTER each collection:
pool.getCollectionUsage().getUsed();
```

**Real output — churn mode** (`-Xmx96m`):

```
 ROUND    HEAP AFTER GC  LIVE SET AFTER GC    GC PAUSE ms
     1         828.0 KB           828.0 KB              4
     5         830.2 KB           830.2 KB             14
    10         830.4 KB           830.4 KB             26     <- flat. healthy.
```

**Real output — leak mode:**

```
 ROUND    HEAP AFTER GC  LIVE SET AFTER GC    GC PAUSE ms
     1          16.8 MB            16.8 MB              3
     2          32.8 MB            32.8 MB              6
     3          48.8 MB            48.8 MB              9
     4          64.8 MB            64.8 MB             13
     5          80.8 MB            80.8 MB             16     <- monotonic climb
java.lang.OutOfMemoryError: Java heap space
```

**This is the whole diagnostic skill in one table:**

| What the numbers do | Diagnosis | Next action |
|---|---|---|
| Live set climbs every collection, never returns | **Leak** | Heap dump -> Eclipse MAT -> Leak Suspects -> dominator tree -> find the retaining root |
| Live set flat, GC count high | **Allocation pressure** | JFR or async-profiler allocation profile; allocate less |
| Pause time rising while reclaimed bytes shrink | Sliding into `GC overhead limit exceeded` | It will not recover on its own — fix the leak or raise the heap |

**Say this:** I never diagnose from used heap, because it saws up and down and tells me nothing. I read
the live set after each collection. And I always ship with `-XX:+HeapDumpOnOutOfMemoryError` — without
the dump you get exactly one chance to reproduce the incident.

---

## Step 8 — Why ThreadLocal leaks in a Spring Boot app

`ThreadLocalMap` holds its **key weakly** but its **value strongly**. Pool threads live for the whole
process, so nothing releases that value until someone calls `remove()`.

```java
static final ThreadLocal<byte[]> CONTEXT = new ThreadLocal<>();

void handleRequest(boolean cleanUp) {
    try {
        CONTEXT.set(new byte[4 << 20]);   // 4 MB "request context"
    } finally {
        if (cleanUp) CONTEXT.remove();    // the one line that fixes it
    }
}
```

**Real output** (4-thread pool, 40 tasks, `-Xmx128m`):

```
                                        LEAK MODE          FIXED MODE (remove())
live set before any request           :  818.6 KB            818.7 KB
live set after the requests           :   20.8 MB            854.7 KB
still retained by the pool            :   20.0 MB             36.0 KB
verdict                               :  LEAKING              CLEAN
live set once the pool threads died   :  854.1 KB                   -
```

**The subtle part worth mentioning:** notice the last row. Killing the threads released the 20 MB
instantly. That is *why this bug is so hard to reproduce in a test* — a test that shuts the executor
down before asserting will always pass. You have to measure while the pool is still alive.

**Say this:** Tomcat and executor threads are pooled for the process lifetime, so "one leak per thread"
is permanent, not transient. Always clear in a `finally` block — that is exactly what Spring's
`RequestContextHolder` and SLF4J's `MDC.clear()` do for you. Same shape of bug: unclosed resources,
listeners never deregistered, static caches with no eviction.

---

## Step 9 — Humongous objects in G1 (and a trap in `new byte[1MB]`)

A **humongous** allocation is anything larger than **half a G1 region**. It skips Eden and goes
straight into contiguous humongous regions.

```java
long region = diag.getVMOption("G1HeapRegionSize").getValue();  // 1 MB here
byte[] humongous = new byte[(int) (region * 6 / 10)];            // 614 KB -> humongous
byte[] ordinary  = new byte[(int) (region / 16)];                // 64 KB  -> Eden
```

**Real output** (`-Xmx256m -XX:G1HeapRegionSize=1m`):

```
Phase A, 240 MB in 614.4 KB chunks:  eden peak 2.0 MB      <- Eden never even moves
         3 young collections, 5 ms
Phase B, 240 MB in 64.0 KB chunks:   eden peak 147.0 MB    <- normal Eden flow
         1 young collection, 2 ms

Phase C, half the heap pinned in humongous regions, then churn:
         young collections: 38  |  old-generation collections: 0
         (vs 1-3 collections for the same churn in phases A/B)

Phase D, region rounding:
  20 arrays of exactly the region size (1.0 MB) cost 40.0 MB of heap
  20 arrays 1 KB smaller                        cost 20.0 MB of heap
```

**Three findings, in order of interview value:**

1. **Eden staying flat while the heap grows is the fingerprint of humongous allocation.** That is how
   you recognise it from MXBean readings or a GC log.
2. **`new byte[1024 * 1024]` can cost 2 MB.** A humongous object occupies whole regions, and the
   16-byte object header pushes a 1 MB payload into a second 1 MB region — wasting nearly half the
   space. Size buffers *just under* a power of two, never exactly on it.
3. **Phases A and B cost about the same, and honesty here scores points.** Since JDK 8u60 G1 *eagerly
   reclaims* short-lived humongous regions during young collections, so the old "humongous is always
   expensive" line is outdated. The real cost is Phase C: pinning half the heap leaves less room for
   Eden, so young collections became roughly **ten times more frequent**.

**Say this:** Real triggers on my stack are large Kafka batches, big JSON/XML payloads and unpaginated
JPA result sets. Fixes in order of preference: lower `max.poll.records` or page the query, stream
instead of buffering, and only then consider a larger `-XX:G1HeapRegionSize`. When you see
`to-space exhausted` or `Evacuation failure` in the log, this is the same story escalating — G1 had
nowhere to copy survivors.

---

## Step 10 — Stop-the-world pauses, measured as user-visible latency

Trick worth knowing: you can detect STW pauses without parsing a GC log. Run a thread that only wants
to sleep 1 ms and record how much later it actually woke up. Application threads cannot run during a
pause, so the missing time shows up as latency.

```java
while (running) {
    long start = System.nanoTime();
    Thread.sleep(1);
    long overshootMicros = (System.nanoTime() - start) / 1000 - 1000;
    if (overshootMicros > 0) hiccups.add(overshootMicros);   // the JVM stole this
}
```

**Real output — identical 12-second workload, 60 MB live, `-Xmx256m`:**

| | G1 (default) | Serial (`-XX:+UseSerialGC`) |
|---|---|---|
| p50 stall | 0.8 ms | 1.3 ms |
| p99 stall | 15.0 ms | 17.7 ms |
| **worst stall** | **16.1 ms** | **172.5 ms** |
| stalls over 20 ms | **0** | **19** |
| GC share of wall clock | **10.7 %** | **40.5 %** |
| collections | 2825 young | 1885 young + 152 full |

**Say this:** Pauses exist because moving objects requires a consistent view of the heap, so threads
are parked at safepoints while references are updated. Serial GC is not merely "slower" — it changes
the *shape* of latency, and a 172 ms stall is what breaks your p99 SLO and trips health-check
timeouts. This is the concrete argument for never letting a small container silently fall back to
Serial.

Two more points that show depth:

- **Not every pause is GC.** Deoptimisation, biased-locking revocation and heap dumps also stop the
  world. Check `-Xlog:safepoint` for time-to-safepoint before blaming the collector.
- **ZGC and Shenandoah** mark and compact concurrently using load barriers, trading a few percent of
  throughput for pauses that barely grow with heap size. Reducing allocation is still usually the
  bigger win, because pause cost tracks the live set.

---

## Step 11 — The pod was OOMKilled but there is no OutOfMemoryError

```java
List<ByteBuffer> buffers = new ArrayList<>();
buffers.add(ByteBuffer.allocateDirect(8 << 20));   // off-heap: NOT in the Java heap
```

**Real output** (`-Xmx64m -XX:MaxDirectMemorySize=64m`):

```
  DIRECT     BUFFER COUNT    OFF-HEAP USED        HEAP USED
    16 MB                2          16.0 MB           2.0 MB
    48 MB                6          48.0 MB           2.0 MB
    64 MB                8          64.0 MB           2.0 MB     <- heap never moves
OutOfMemoryError: Cannot reserve 8388608 bytes of direct buffer memory
                 (allocated: 67108864, limit: 67108864)
```

64 MB of process memory consumed while the heap sat at 2 MB. A heap dump here would look completely
innocent.

**What actually makes up container RSS:**

```
  heap (-Xmx)                +  Metaspace & code cache  +  thread stacks (~1 MB each)
  +  direct / mapped buffers +  GC bookkeeping (remembered sets, card tables)
```

**Say this:** `OutOfMemoryError` is the JVM refusing an allocation; **OOMKilled (exit 137)** is the
kernel enforcing the cgroup limit, and it produces no Java stack trace at all. So sizing the heap at
100 % of the pod limit guarantees an OOMKill — I leave 25-30 % headroom, typically
`-XX:MaxRAMPercentage=70`. Netty and NIO clients allocate direct buffers, so I bound them with
`-XX:MaxDirectMemorySize` to fail loudly instead of growing silently. Diagnose it with
`-XX:NativeMemoryTracking=summary` plus `jcmd <pid> VM.native_memory summary`, **not** a heap dump.

Know the OOM variants and what each means:

| Message | Real meaning |
|---|---|
| `Java heap space` | Live set exceeds `-Xmx` — leak or undersized heap |
| `GC overhead limit exceeded` | Over 98 % of time in GC reclaiming under 2 % — almost always a leak |
| `Metaspace` | Class metadata — classloader leak, or hot redeploys |
| `Direct buffer memory` | Off-heap NIO ceiling reached |
| `unable to create native thread` | OS/cgroup thread limit, nothing to do with the heap |
| `Requested array size exceeds VM limit` | Array bigger than `Integer.MAX_VALUE - 2` |

---

## Step 12 — Escape analysis: the allocation that never happens

Measure real allocation per thread with `com.sun.management.ThreadMXBean`.

```java
for (int i = 0; i < 10_000_000; i++) {
    Point p = new Point(i, i + 1);   // never escapes this method
    total += p.sum();
}
// measured with:
threadMXBean.getThreadAllocatedBytes(Thread.currentThread().getId());
```

**Real output:**

| | default | `-XX:-DoEscapeAnalysis` |
|---|---|---|
| bytes allocated | 3.2 MB | 228.9 MB |
| **bytes per iteration** | **0.33** | **24.00** |
| elapsed | 11 ms | 102 ms |

24.00 bytes/iteration is exactly a 16-byte header plus two `int` fields plus padding. With the
optimisation on, the allocation essentially disappears.

**Say this:** HotSpot does not literally "allocate on the stack" — when the JIT proves an object never
escapes, it performs **scalar replacement**: the object is never created and its fields live in
registers or stack slots. Zero GC pressure. It only kicks in after the method is JIT-compiled, and
storing the object in a field, putting it in a collection or returning it defeats it. The practical
consequence: small short-lived wrappers in a hot loop are often free, so do not contort readable code
to avoid objects without measuring first.

---

## Flags I would actually set, and why

```
-Xms = -Xmx                        Predictable footprint; no repeated heap resizing
-XX:MaxRAMPercentage=70            Heap from the container limit, leaving room for non-heap
-XX:+HeapDumpOnOutOfMemoryError    The one flag every production service should carry
-XX:HeapDumpPath=/var/log/dumps    ...pointed at a volume that survives the pod
-Xlog:gc*:file=gc.log:time,uptime:filecount=5,filesize=20m
                                   Unified logging (Java 9+). Java 8 used -XX:+PrintGCDetails
-Xlog:gc+age=trace                 Tenuring distribution, for premature-promotion questions
-XX:MaxDirectMemorySize=<n>m       Bound off-heap so it fails loudly
-XX:NativeMemoryTracking=summary   Pairs with `jcmd VM.native_memory` for RSS investigations
```

Deliberately **not** set under G1: `-Xmn` / `NewRatio`, because fixing the young size stops G1 from
resizing generations to meet its pause target. Say that out loud — it shows you tune with the
collector rather than against it.

### The collectors in one table

| Collector | Pause behaviour | Use when |
|---|---|---|
| Serial | Single-threaded, longest pauses | Tiny heaps, single CPU, small containers (often by accident) |
| Parallel | Multi-threaded, best raw throughput | Batch jobs where throughput beats latency |
| G1 (default 9+) | Regionised, aims at `MaxGCPauseMillis` | Default choice for services; heaps up to tens of GB |
| ZGC | Concurrent, sub-millisecond, pauses flat vs heap size | Latency-critical, large heaps |
| Shenandoah | Concurrent compaction, similar goals to ZGC | Same, on Red Hat builds |
| CMS | **Removed in Java 14** | Never for new work — G1 replaced it |

### Diagnostic toolkit, in the order I would reach for it

1. `-Xlog:gc*` — is GC even the problem, and which collector is running?
2. `jstat -gcutil <pid> 1s` — live generation occupancy without restarting anything
3. `jcmd <pid> GC.heap_info` / `VM.native_memory summary` — heap vs native split
4. Heap dump + Eclipse MAT — dominator tree names the object retaining your memory
5. JFR (`-XX:StartFlightRecording`) or async-profiler `-e alloc` — *where* allocation comes from

---

## Ten-second answers, if you only remember this page

- **Minor GC** — young gen only, frequent, cheap, cost scales with survivors.
- **Full GC** — everything plus compaction, cost scales with the live set.
- **Leak vs pressure** — read the live set *after* collection, not used heap.
- **ThreadLocal** — weak key, strong value; pooled threads make it permanent; `remove()` in `finally`.
- **Humongous** — bigger than half a G1 region; Eden stays flat; `new byte[1MB]` costs 2 MB.
- **OOMKilled** — kernel, not JVM; heap is only part of RSS; leave 30 % headroom.
- **`System.gc()`** — a hint, disableable, a code smell in application code.
- **Escape analysis** — scalar replacement, not stack allocation; the object never exists.

---

## Want to run any of it yourself?

Every output block above is reproducible from the runnable lab in this folder:

```powershell
.\run.ps1                     # list all 12 steps
.\run.ps1 step07 leak         # leak signature
.\run.ps1 step07 churn        # allocation-pressure signature
.\run.ps1 step08 fixed        # ThreadLocal with remove()
.\run.ps1 step10 -Collector serial     # the 172 ms stall
.\run.ps1 step12 -NoEscapeAnalysis     # 24 bytes per iteration
```
