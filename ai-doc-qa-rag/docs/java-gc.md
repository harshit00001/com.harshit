# Java Garbage Collection Notes

## Generations and minor collections

The heap is split into a young generation and an old generation. New objects are allocated in Eden,
inside a thread-local allocation buffer, so allocation is a pointer bump that needs no locking.

When Eden fills, a minor collection copies the few surviving objects into a survivor space and
declares the whole of Eden free. The cost of a minor collection scales with the number of survivors,
not with the amount of garbage, which is why generating short-lived garbage is cheap.

## Promotion and tenuring

Each young collection increments the age of every survivor. Once an object survives more collections
than MaxTenuringThreshold, which defaults to 15, it is promoted into the old generation. Old
generation growth is normal for request-spanning state such as cache entries or open sessions.

If the survivor space is too small, objects are promoted before they have aged properly. This is
called premature promotion and it makes old-generation collections more frequent.

## Full collections

A full collection walks the young generation, the old generation and Metaspace, and usually compacts
the heap. Its cost tracks the size of the live set rather than the volume of garbage, so it takes a
few milliseconds on a small heap and can take seconds on a multi-gigabyte heap.

System.gc() is only a hint to the JVM. It can be disabled entirely with -XX:+DisableExplicitGC, so
calling it from application code is considered a code smell.

## Detecting a memory leak

Used heap is a poor signal because it includes garbage and moves up and down constantly. The number
to read is the live set after each collection, available from
MemoryPoolMXBean.getCollectionUsage(). A live set that climbs monotonically across collections and
never returns is a leak. A flat live set with a high collection count is allocation pressure, which
is a different problem solved by allocating less.

Always run production services with -XX:+HeapDumpOnOutOfMemoryError, because without the dump you
get only one chance to reproduce the incident.

## Choosing a collector

Java 8 defaults to the Parallel collector and Java 9 onwards defaults to G1 on server-class
machines. A container with less than about 1792 MB of memory or fewer than two CPUs silently falls
back to Serial GC, which produces much longer pauses.

G1 divides the heap into regions and aims for the pause target set by MaxGCPauseMillis, which
defaults to 200 milliseconds. ZGC and Shenandoah mark and compact concurrently, trading a little
throughput for pauses that barely grow as the heap grows.

## Container memory and OOMKilled

An OutOfMemoryError comes from the JVM refusing an allocation. An OOMKilled with exit code 137 comes
from the kernel enforcing the container memory limit, and it produces no Java stack trace at all.

Process memory is more than the heap: Metaspace, the code cache, thread stacks of roughly 1 MB each,
direct NIO byte buffers and garbage collector bookkeeping all count towards the resident set size.
Sizing the heap at 100 percent of the pod limit therefore guarantees an OOMKill. Leave 25 to 30
percent headroom, typically with -XX:MaxRAMPercentage=70, and investigate native growth with
Native Memory Tracking rather than with a heap dump.
