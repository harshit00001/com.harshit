# CompletableFuture Exception Handling Notes

## The three handlers

There are three ways to deal with a failure in an asynchronous chain.

The exceptionally method takes a Function from Throwable to a value. It runs only on the failure path
and it recovers, so it is the right choice for supplying a fallback value.

The handle method takes a BiFunction of value and Throwable. It runs on both the success and failure
paths, it can recover, and it can change the result type, so it suits mapping an outcome to a
response object.

The whenComplete method takes a BiConsumer of value and Throwable. It runs on both paths but cannot
recover: the failure keeps propagating downstream. Use it for logging, metrics and cleanup.

## Exceptions arrive wrapped

An exception thrown inside a lambda is wrapped in a CompletionException before the handler sees it,
even when the handler is attached directly to the stage that failed. This means a type check such as
testing whether the throwable is a TimeoutException is always false unless the cause is unwrapped
first with getCause().

There is one exception to this rule. When a future is failed manually by calling
completeExceptionally, the exception is stored unwrapped and the handler receives it directly. A
handler can therefore see either shape, so always unwrap defensively.

## join versus get

The join method throws an unchecked CompletionException. The get method throws the checked
ExecutionException along with InterruptedException, and it is interruptible. Both wrap the real
cause, so the cause must be unwrapped before it is inspected.

## Silent failures

A failure skips every thenApply, thenAccept and thenCompose stage in the chain without running them.
If the chain has no terminal handler and nobody calls join or get, the exception is stored in a
future that nobody reads: there is no stack trace, no log line, and no thread death. Every chain
should therefore end in exceptionally, handle, or a whenComplete that logs.

## Combining futures

The allOf method waits for every future but completes exceptionally if any of them fails, which
discards the successful results. When partial success is acceptable, attach an exceptionally fallback
to each individual future before combining them.

The anyOf method completes with the first future to finish either successfully or exceptionally, so a
fast failure beats a slow success.

## Timeouts and retries

The orTimeout method, added in Java 9, completes the future exceptionally with a TimeoutException,
while completeOnTimeout supplies a default value instead. Neither interrupts the running task, so
socket and read timeouts must still be configured on the underlying client.

The exceptionallyCompose method, added in Java 12, recovers with another future rather than a plain
value, which makes it the natural way to fall back to a backup service or to implement retries
recursively.

## Executors

Always pass an explicit executor. The default is ForkJoinPool.commonPool(), which is shared across
the whole process and sized to the number of CPUs minus one, so blocking input and output on it
starves parallel streams and every library that relies on it. Remember also that ThreadLocal context
such as the security context or the logging MDC does not cross thread boundaries.
