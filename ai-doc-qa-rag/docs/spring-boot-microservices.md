# Spring Boot Microservices Notes

## Transaction propagation

The default propagation level is REQUIRED, which joins an existing transaction or starts a new one.
REQUIRES_NEW always suspends the caller's transaction and starts an independent one, which is how an
audit record can be committed even when the calling business transaction rolls back.

A self-invocation does not go through the proxy, so calling one @Transactional method from another
method of the same bean silently ignores the annotation. Extracting the method into a separate bean
is the usual fix.

By default a rollback happens only for unchecked exceptions. A checked exception commits unless the
rollbackFor attribute names it explicitly.

## Idempotency in event-driven flows

Kafka consumers deliver at least once, so the same message can arrive twice after a rebalance or a
retry. Consumers therefore need idempotency, usually implemented with a unique business key and a
database constraint, or with an inbox table recording processed message identifiers.

Manual acknowledgement combined with committing offsets only after the database write completes
prevents message loss when a consumer crashes mid-processing.

## Resilience patterns

A circuit breaker stops calling a failing dependency after a threshold of failures, giving it room to
recover instead of piling on load. A bulkhead limits the concurrent calls to one dependency so that a
single slow service cannot exhaust the whole thread pool.

Retries need exponential backoff and jitter, and they must only be applied to idempotent operations,
otherwise a retry duplicates the side effect.

## API versioning and contracts

Backwards-compatible changes such as adding an optional field do not require a new version. Breaking
changes such as removing a field or changing its type require a new major version, exposed as a URI
prefix like /v2 or as a content-type header.

## Database performance

The N plus 1 select problem occurs when a lazy association is accessed inside a loop, producing one
query per parent row. It is solved with a join fetch, an entity graph, or a batch size setting.

Connection pool sizing follows from concurrency rather than optimism: a pool much larger than the
database can service simply moves the queue from the application into the database. Indexes should
match the query predicates and their column order should match the leading columns of composite
filters.
