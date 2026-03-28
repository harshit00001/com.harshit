# Spring transactions — deep dive (readable explanations)

This document repeats **nothing** from the one-line comments: it is meant to be read when the code comments still feel too short. Pair it with the Javadoc on each class in `com.harshit.transaction`.

---

## 1. Why “after commit” exists (`TransactionInternalsDemo`)

**Problem you are solving:** The database and the rest of the world (message broker, email provider, another microservice) are separate systems. You want a rule like: “Only tell the warehouse to ship **after** we know the payment row is really stored.”

**Wrong approach:** Call Kafka or REST at the beginning of the service method. If the method throws later, or if the transaction rolls back, you have already told another system to act — **dual-write inconsistency**.

**What Spring offers:** While a transaction is open, you can register a `TransactionSynchronization`. The method `afterCommit()` is invoked **only after** the transaction manager successfully commits. If the transaction rolls back, `afterCommit()` is **not** called. So “notify external world” code belongs there (or in patterns built on the same idea: `@TransactionalEventListener(phase = AFTER_COMMIT)`, transactional outbox, etc.).

**Lifecycle detail:** There is also `afterCompletion(int status)` which runs whether you committed or rolled back — use it for freeing resources, not for business notifications that must only happen on success.

**Threading:** Default callbacks run on the **same thread** that committed. If you need async work, schedule it **inside** the callback explicitly and understand ordering guarantees.

---

## 2. How `@Transactional` actually runs (internals, interview style)

1. **Proxy call:** The client holds a reference to a **proxy**, not always to your raw object.
2. **Interceptor:** `TransactionInterceptor` asks `PlatformTransactionManager` to `getTransaction` (begin or join).
3. **Resource binding:** For JPA, an `EntityManager` is associated with the thread via `TransactionSynchronizationManager` so all repository calls in that thread share one persistence context within the transaction.
4. **Your code:** Business logic and repository calls run; Hibernate may flush SQL before commit.
5. **Outcome:** If nothing marked rollback-only and no rollback exception rule triggers → **commit**. Otherwise → **rollback**.
6. **Callbacks:** Registered synchronizations run in order (`beforeCommit`, then commit, then `afterCommit` for successful commits).
7. **Cleanup:** Unbind resources; return connection to pool.

**Why this matters in interviews:** Explains **lazy-loading** issues (session often scoped to the transaction), **read-only** hints, and why **async** methods need their own transaction configuration.

---

## 3. Proxies: JDK vs CGLIB (`ProxyShowcase`)

Spring does **not** rewrite your bytecode at compile time by default. At **runtime** it builds a **proxy object**:

- **JDK dynamic proxy:** Implements the same interfaces as your bean; requires an interface-based injection style to be natural.
- **CGLIB:** Subclasses your class (or a generated subclass) so it can intercept `public` methods on concrete classes.

**Spring Boot default:** Often `spring.aop.proxy-target-class=true`, so you frequently see class names containing `EnhancerBySpringCGLIB` even when your bean implements an interface. That is **normal** — it does not mean `@Transactional` is broken.

**Self-invocation:** Internal `this.foo()` calls **do not** pass through the proxy, so the interceptor (and thus `@Transactional`) does not run for `foo()` unless you use a workaround (injected self, another bean, `TransactionTemplate`, or AspectJ weaving).

---

## 4. Propagation `REQUIRES_NEW` (`AuditService`, `AuditedTransferFacade`)

**Story:** Compliance requires a row “payment attempted” even when the payment transaction rolls back.

**Mechanism:** `REQUIRES_NEW` **suspends** the outer transaction, starts a **new** one, runs the audit insert, **commits** that new transaction, then **resumes** the outer one. When the outer transaction fails, only outer changes roll back — the audit row that already committed stays.

**Caution:** This is powerful but adds complexity (ordering, duplicate handling, performance). Interviews reward mentioning **trade-offs**, not only the keyword.

---

## 5. Rollback rules (`RollbackDemoService`)

Default: rollback on **unchecked** (`RuntimeException`, `Error`). **Checked** exceptions do **not** roll back by default. Many production bugs come from assuming checked exceptions always roll back — say explicitly that you use `rollbackFor = Exception.class` when needed.

---

## 6. Programmatic API (`ProgrammaticTransactionService`)

Same transaction manager, different style: you pass a callback to `TransactionTemplate` and control boundaries manually. Prefer when the transactional scope is **not** a single clean method — e.g. per-iteration transactions in a loop, or third-party APIs that cannot be annotated.

---

## How to study this repository

1. Open `internal/TransactionInternalsDemo` and read the **class-level** Javadoc (full interview Q&A).
2. Run `mvn spring-boot:run` and read console sections in order — they match `InterviewDemoRunner`.
3. Use your debugger: set a breakpoint in `TransferService.transfer` and step **into** Spring’s interceptor once to see the stack (optional but memorable).
