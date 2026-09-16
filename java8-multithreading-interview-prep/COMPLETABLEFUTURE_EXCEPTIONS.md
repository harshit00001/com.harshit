# Exception Handling in CompletableFuture

Every output block below is **real output** from running the examples on JDK 17 — including two
behaviours that almost everyone gets wrong in interviews.

---

## The short answer

There are three handlers, and choosing between them is the whole question:

| Method | Signature | Runs when | Can it recover? | Use it for |
|---|---|---|---|---|
| `exceptionally` | `Function<Throwable, T>` | **Failure only** | **Yes** — returns a fallback value | "On error, use a default" |
| `handle` | `BiFunction<T, Throwable, R>` | **Always** | **Yes** — and can change the type | One place for both paths, or mapping to a response object |
| `whenComplete` | `BiConsumer<T, Throwable>` | **Always** | **No** — the failure keeps propagating | Logging, metrics, cleanup |

```java
// 1. Recover with a fallback value
orderService.fetchAsync(id)
    .exceptionally(ex -> Order.empty());

// 2. Handle both outcomes and map to one type
orderService.fetchAsync(id)
    .handle((order, ex) -> ex == null
            ? ResponseEntity.ok(order)
            : ResponseEntity.status(503).build());

// 3. Observe without changing the outcome (note: does NOT swallow)
orderService.fetchAsync(id)
    .whenComplete((order, ex) -> { if (ex != null) log.error("fetch failed", ex); });
```

Each has an `...Async` variant (`exceptionallyAsync`, `handleAsync`, `whenCompleteAsync`) that runs the
handler on a different thread — use them if the handler does anything slow.

---

## Gotcha 1: your handler receives a CompletionException, not your exception

This is the single most common bug. The exception thrown inside a lambda is **wrapped**, even when the
handler is attached directly to the stage that failed.

```java
CompletableFuture<String> failing = CompletableFuture.supplyAsync(() -> {
    throw new IllegalStateException("boom");
});

failing.exceptionally(ex -> describe(ex)).join();
failing.handle((v, ex) -> describe(ex)).join();
failing.thenApply(v -> v).exceptionally(ex -> describe(ex)).join();
```

**Real output:**

```
exceptionally directly on failing stage -> CompletionException("...IllegalStateException: boom")
                                             caused by  IllegalStateException("boom")
handle directly on failing stage        -> CompletionException("...IllegalStateException: boom")
                                             caused by  IllegalStateException("boom")
deeper in chain (after a thenApply)     -> CompletionException("...IllegalStateException: boom")
                                             caused by  IllegalStateException("boom")
```

So this very natural code **never matches**:

```java
.exceptionally(ex -> {
    if (ex instanceof TimeoutException) {   // FALSE — ex is a CompletionException
        return cached();
    }
    return fallback();
})
```

### But there is an exception to the rule

If the future was failed manually with `completeExceptionally`, the exception is stored **unwrapped**:

```java
CompletableFuture<String> manual = new CompletableFuture<>();
manual.completeExceptionally(new IllegalStateException("manual"));
manual.exceptionally(ex -> describe(ex)).join();
```

```
exceptionally sees -> IllegalStateException("manual")        <- no wrapper this time
```

So a handler can receive **either** shape depending on how the future failed. Never branch on the
type you were given — unwrap first. Keep this helper around:

```java
public static Throwable unwrap(Throwable ex) {
    return (ex instanceof CompletionException || ex instanceof ExecutionException)
            && ex.getCause() != null
            ? ex.getCause()
            : ex;
}

// now type checks actually work:
.exceptionally(ex -> unwrap(ex) instanceof TimeoutException ? cached() : fallback())
```

---

## Gotcha 2: `join()` and `get()` throw different exceptions

```java
try { f.join(); } catch (Throwable t) { ... }
try { f.get();  } catch (Throwable t) { ... }
```

```
join threw -> CompletionException("...IllegalStateException: boom")  caused by  IllegalStateException("boom")
get threw  -> ExecutionException("...IllegalStateException: boom")   caused by  IllegalStateException("boom")
```

| | `join()` | `get()` |
|---|---|---|
| Throws | `CompletionException` (unchecked) | `ExecutionException` + `InterruptedException` (both checked) |
| Interruptible | No | Yes |
| Use in | Lambdas and stream pipelines, where checked exceptions are painful | Code that must react to interruption |

Both wrap the real cause, so `catch (CompletionException e) { handle(e.getCause()); }`.

---

## Step by step: how a failure travels down a chain

### Step 1 — a failure skips every normal stage

```java
CompletableFuture.<String>supplyAsync(() -> { throw new IllegalStateException("db down"); })
    .thenApply(v -> v + "!")                       // SKIPPED
    .exceptionally(ex -> "fallback")
    .join();
```

```
result -> fallback[CompletionException("...IllegalStateException: db down") caused by IllegalStateException("db down")]
```

`thenApply`, `thenAccept`, `thenCompose`, `thenCombine` all pass the failure straight through without
running. This is why a broken chain looks "silent" — nothing ran, nothing logged.

### Step 2 — `whenComplete` sees the error but does not stop it

```java
CompletableFuture<String> observed = failing
    .whenComplete((v, ex) -> log("saw " + ex));
observed.join();   // still throws!
```

```
whenComplete saw        -> value=null, ex=CompletionException
downstream still failed -> CompletionException(...) caused by IllegalStateException("boom")
```

Use `whenComplete` for logging, and `exceptionally`/`handle` when you actually want to recover.

### Step 3 — position in the chain matters

A handler only covers the stages **above** it.

```java
CompletableFuture.supplyAsync(() -> "ok")
    .exceptionally(ex -> "recovered")                                   // guards nothing yet
    .thenApply(v -> { throw new IllegalStateException("fails AFTER"); })// fails below the handler
    .handle((v, ex) -> ex == null ? "value:" + v : "handle caught: " + ex)
    .join();
```

```
-> handle caught: CompletionException("...IllegalStateException: fails AFTER the handler")
```

**Rule:** put the recovery handler at the **end** of the chain, or immediately after the specific
stage whose failure you want to treat differently.

### Step 4 — the silent swallow, the real production bug

```java
CompletableFuture.supplyAsync(() -> { throw new IllegalStateException("nobody will see me"); });
// nothing joins it, nothing handles it
```

```
main continues, nothing logged -> exception vanished
```

No stack trace, no log line, no thread death — the exception is simply stored in a future nobody
reads. A fire-and-forget `CompletableFuture` with no terminal handler is how failures disappear in
production.

**Rule: every chain ends in `exceptionally`, `handle`, or `whenComplete` that logs.** Treat a chain
without one as a code-review defect.

---

## Multiple futures: `allOf` and one failing call

```java
CompletableFuture<String> inventory = ...;                    // ok
CompletableFuture<String> pricing   = ...;                    // throws
CompletableFuture<String> shipping  = ...;                    // ok

CompletableFuture.allOf(inventory, pricing, shipping).join();
```

```
allOf().join() threw            -> CompletionException(...) caused by IllegalStateException("pricing down")
inventory isCompletedExceptionally -> false
pricing   isCompletedExceptionally -> true
```

`allOf` still **waits for all** of them; it just completes exceptionally with one of the failures, and
you lose the successful results. If partial success is acceptable — which for a product page it
usually is — attach a fallback to each future **before** combining:

```java
var results = List.of(
    inventory.exceptionally(ex -> "n/a"),
    pricing.exceptionally(ex -> "DEFAULT-PRICE"),
    shipping.exceptionally(ex -> "n/a"));
CompletableFuture.allOf(results.toArray(CompletableFuture[]::new)).join();
```

```
per-future recovery -> [inventory-ok, DEFAULT-PRICE, shipping-ok]
```

`anyOf` completes with the first future to finish **either way** — so a fast failure wins over a slow
success. If you want the first *success*, put `exceptionally` on each input first.

---

## Timeouts (Java 9+)

A hung dependency is the failure mode that hurts most, and neither handler helps unless something
actually completes the future.

```java
slow.orTimeout(100, TimeUnit.MILLISECONDS).join();                     // fail fast
slow.completeOnTimeout("cached-value", 100, TimeUnit.MILLISECONDS);    // degrade gracefully
```

```
orTimeout threw   -> CompletionException("...TimeoutException") caused by TimeoutException
completeOnTimeout -> cached-value
```

Note `orTimeout` does **not** interrupt the running task — it only completes the future. The work
keeps burning a thread, so also set real socket/read timeouts on the client underneath.

---

## Fallback to another service, and retries

`exceptionallyCompose` (Java 12+) is the one to know: it recovers with **another future** instead of a
plain value.

```java
primaryService.fetchAsync()
    .exceptionallyCompose(ex -> backupService.fetchAsync());
```

```
result -> from-backup-service
```

Retry is the same idea, applied recursively:

```java
CompletableFuture<String> withRetry(int remaining) {
    return callFlakyService()
        .exceptionallyCompose(ex -> remaining <= 1
                ? CompletableFuture.failedFuture(ex)      // Java 9+
                : withRetry(remaining - 1));
}
```

```
result -> succeeded on attempt 3
```

On Java 8, `exceptionallyCompose` does not exist — the equivalent is
`handle((v, ex) -> ...).thenCompose(Function.identity())`.

---

## Checked exceptions inside a lambda

`Supplier` cannot throw checked exceptions, so wrap them in a `CompletionException`, which keeps the
normal failure semantics of the chain:

```java
CompletableFuture.supplyAsync(() -> {
    try {
        return httpClient.send(request, ofString()).body();   // throws IOException
    } catch (Exception e) {
        throw new CompletionException(e);
    }
});
```

```
result -> CompletionException("java.io.IOException: socket closed") caused by IOException("socket closed")
```

Do **not** catch and return `null` — you turn a failure into a `NullPointerException` three stages
later, which is far harder to debug.

---

## In a Spring Boot service

```java
@Bean("orderExecutor")
public Executor orderExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(10);
    executor.setQueueCapacity(50);
    executor.setThreadNamePrefix("order-");
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
    return executor;
}

public CompletableFuture<OrderView> loadOrder(String id) {
    return CompletableFuture
            .supplyAsync(() -> orderRepository.find(id), orderExecutor)
            .thenCombine(
                CompletableFuture.supplyAsync(() -> pricingClient.quote(id), orderExecutor)
                                 .exceptionally(ex -> Price.unavailable()),   // degrade, not fail
                OrderView::of)
            .orTimeout(2, TimeUnit.SECONDS)
            .whenComplete((view, ex) -> {
                if (ex != null) log.error("loadOrder failed for {}", id, unwrap(ex));
            });
}
```

Four things worth saying about this in an interview:

1. **Always pass your own executor.** The default is `ForkJoinPool.commonPool()`, which is shared
   process-wide and sized to CPU count minus one — blocking I/O on it starves parallel streams and
   every other library that uses it.
2. **`RejectedExecutionException` is a failure path too.** When the queue is full the future completes
   exceptionally, so the same handlers cover it.
3. **`ThreadLocal` context does not cross threads.** Security context, MDC and tracing IDs are lost
   unless you propagate them (`DelegatingSecurityContextExecutor`, `MDCAdapter` wrappers). And clear
   them in a `finally` — pooled threads otherwise retain the value permanently, which is a real memory
   leak.
4. **`@Async` methods returning `CompletableFuture`** propagate exceptions through the future.
   `void @Async` methods do not — those need an `AsyncUncaughtExceptionHandler`, or the exception is
   lost exactly like the silent-swallow case above.

---

## Pitfalls checklist

| Pitfall | What actually happens | Fix |
|---|---|---|
| Chain with no terminal handler | Exception silently stored, never logged | End every chain with `handle`/`whenComplete` that logs |
| `if (ex instanceof MyException)` | Never true — `ex` is a `CompletionException` | Unwrap with `getCause()` first |
| `exceptionally` placed early | Does not cover later stages | Put it at the end, or after the specific risky stage |
| `whenComplete` used to recover | Failure still propagates downstream | Use `exceptionally` or `handle` |
| No executor supplied | Blocking work starves the common ForkJoinPool | Pass a dedicated executor |
| `allOf(...).join()` | Loses all successful results on one failure | Attach `exceptionally` per future first |
| No timeout | One hung dependency hangs the request | `orTimeout` / `completeOnTimeout` **plus** client-level timeouts |
| `catch (Exception e) { return null; }` | NPE several stages later | `throw new CompletionException(e)` |
| Blocking `.join()` inside a chain | Ties up a pool thread, can deadlock | `thenCompose` instead of joining |

---

## Answers if you only remember this page

- **Three handlers:** `exceptionally` (recover on failure), `handle` (both paths, can change type),
  `whenComplete` (observe only — does **not** stop the failure).
- **Your exception arrives wrapped** in `CompletionException` when it was thrown inside a lambda, but
  unwrapped when set by `completeExceptionally`, so always unwrap before type checks.
- **`join()` throws `CompletionException`; `get()` throws `ExecutionException`** — both wrap the cause.
- **A failure skips every `thenApply`/`thenCompose`** until it meets a handler; if it never meets one
  and nobody joins, it disappears entirely.
- **`exceptionallyCompose`** gives you an async fallback and, recursively, retries.
- **Timeouts are a separate concern from handlers** — `orTimeout` completes the future but does not
  cancel the work.
- **Always supply your own executor**, and remember `ThreadLocal` context does not follow the task.

---

Related in this repo: `Q06CallableFutureCompletableFuture.java`, `Q18CompletableFutureAdvanced.java`,
and the `ThreadLocal` leak measured in `../jvm-gc-interview-lab/GC_STEP_BY_STEP.md` (Step 8).
