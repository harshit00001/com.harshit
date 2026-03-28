# Spring `@Transactional` — scripted interview answers (with project pointers)

Run the project to see console demos: `mvn spring-boot:run` from `spring-transaction-interview`.

**If one-line answers feel too short:** read **`docs/DEEP_DIVE.md`** — same topics with full paragraphs. **In IntelliJ:** open any class under `com.harshit.transaction` and read the **class Javadoc** (not only inline `//` comments) — that is where the long interview Q&A lives.

Package map:

| Topic | Code |
|-------|------|
| Atomic transfer | `account.TransferService` |
| `REQUIRES_NEW` audit | `audit.AuditService`, `facade.AuditedTransferFacade` |
| Rollback rules | `rollback.RollbackDemoService` |
| Self-invocation | `selfinvocation.ReportingServiceBad`, `ReportingServiceGood` |
| Programmatic TX | `programmatic.ProgrammaticTransactionService` |
| Proxies (see `proxy-target-class`) | `proxy.ProxyShowcase`, `PaymentGatewayImpl`, `LegacyBatchProcessor` |
| Internals (`afterCommit`) | `internal.TransactionInternalsDemo` |

---

## The six “pillar” questions (quick script)

### 1) What does `@Transactional` do?

**Answer:** It declares that a method (or class) should run inside a database transaction. Spring’s **transaction interceptor** opens a transaction before the method, and **commits** on success or **rolls back** on failure (per rollback rules). The business code does not call `commit()` manually.

**Code:** `TransferService.transfer` — debit + credit in one boundary.

---

### 2) Default propagation and rollback?

**Answer:** Default propagation is **`REQUIRED`**: join the caller’s transaction if one exists; otherwise start a new one. Default rollback is for **`RuntimeException`** and **`Error`**; **checked exceptions do not roll back** unless you set `rollbackFor`.

**Code:** `RollbackDemoService` — compare `throwsChecked` vs `throwsCheckedWithRollback`.

---

### 3) `REQUIRED` vs `REQUIRES_NEW`?

**Answer:** **`REQUIRED`** joins the existing transaction. **`REQUIRES_NEW`** **suspends** the current transaction, opens a **new** one, commits or rolls back **independently**, then resumes the outer. Used when something must persist even if the outer business transaction rolls back (audit, outbox).

**Code:** `AuditedTransferFacade.transferWithAuditTrail` + `AuditService.record(REQUIRES_NEW)`.

---

### 4) Self-invocation problem?

**Answer:** Calling `this.transactionalMethod()` **inside the same class** does **not** go through the Spring **proxy**, so `@Transactional` on the callee may **not** apply. Fix: call through an **injected self** (`@Lazy` same type), move logic to **another bean**, or use **`TransactionTemplate`** / AspectJ compile-time weaving.

**Code:** `ReportingServiceBad` (fails TX check) vs `ReportingServiceGood` (injected `self`).

---

### 5) How do Spring proxies relate to transactions?

**Answer:** `@Transactional` is implemented with **AOP**. The container wraps your bean in a **proxy**. With **Spring Boot 3’s default** (`proxy-target-class=true`), that is usually **CGLIB** even for interface beans; **JDK dynamic proxies** appear when you set `spring.aop.proxy-target-class=false` and inject by interface. Only **external** calls through the proxy get the interceptor.

**Code:** `ProxyShowcase.describeProxies()` prints actual runtime class names.

---

### 6) Transaction internals — what happens under the hood?

**Short answer:** Spring opens a transaction before your method, binds the persistence resources to the **current thread**, runs your code, then either **commits** or **rolls back**. Side effects that must only happen if data really persisted should use **`afterCommit`** (or transactional events / outbox).

**Longer answer (what to say in a senior interview):** Think in layers. The **proxy** delegates to `TransactionInterceptor`. That asks **`PlatformTransactionManager`** (for JPA, usually **`JpaTransactionManager`**) to obtain a **logical** transaction: it may map to a JDBC connection and an **`EntityManager`** pinned to the thread. Your repositories participate in that same unit of work. Hibernate may **flush** pending SQL before commit. If the method ends successfully and nothing set **rollback-only**, the manager **commits**; otherwise it **rolls back**. **`TransactionSynchronization`** callbacks fire around that boundary — notably **`afterCommit()`** only on success, which is why we use it for “publish event / send message only if DB committed.” Afterward, resources are **unbound** from the thread (which is why lazy collections may fail if you access them outside the session/transaction unless you use fetch strategies or `OpenEntityManagerInView` deliberately).

**Code:** `TransactionInternalsDemo` — read the **full class Javadoc** in the IDE; the method `scheduleAfterCommit` registers a callback that runs **only after** a successful commit (see console demo in `InterviewDemoRunner`).

---

## Internal working (deeper script + code)

**Q: Walk through Spring’s transactional flow.**

**A (expanded):**

1. **Who is calling?** Another bean (or test) calls a method on a **proxy** reference, not on your raw object.
2. **Begin or join:** `TransactionInterceptor` calls `PlatformTransactionManager.getTransaction(...)` with the propagation rule (default `REQUIRED` = join or create).
3. **Bind resources:** `EntityManager`/JDBC connection are tied to the thread through `TransactionSynchronizationManager` so all ORM work in that call stack shares one transaction.
4. **Your code:** Services and repositories execute; Hibernate may flush to the DB before commit depending on flush mode and operation.
5. **Commit vs rollback:** Unchecked exception (by default) → rollback. Success path → commit. Checked exceptions need explicit `rollbackFor` if you want rollback.
6. **Synchronizations:** `beforeCommit`, then commit, then `afterCommit` (success only); `afterCompletion` always runs — use for cleanup.
7. **Release:** Unbind thread state; return connection to the pool.

**Why it matters in interviews:** Explaining **thread-bound** resources explains **`LazyInitializationException`** (session closed after TX) and why **async** methods need explicit TX propagation. See **`docs/DEEP_DIVE.md`** for the same ideas in prose form.

---

## Proxy questions (script)

**Q: JDK proxy vs CGLIB?**

**A:** **JDK dynamic proxy** — only interfaces, built with `java.lang.reflect.Proxy`. **CGLIB** — subclass of your concrete class at runtime. **Spring Boot 3** still defaults to **`spring.aop.proxy-target-class=true`**, so you usually see **CGLIB** (`...EnhancerBySpringCGLIB...`) even for beans that implement interfaces. Set **`spring.aop.proxy-target-class=false`** to prefer JDK proxies for interface-based beans. Both mechanisms apply `@Transactional` via AOP; interviews often want you to mention **proxy-target-class** and **self-invocation**.

**Q: Why don’t private methods get `@Transactional`?**

**A:** Proxies can only intercept **public** methods they override (CGLIB cannot override `private`). Design keeps AOP predictable.

**Q: Does `@Transactional` work on the controller?**

**A:** It can, but **service layer** is preferred: keeps web concerns separate and transactions aligned with domain operations.

---

## Real-world examples (one-liners for interviews)

| Scenario | Pattern |
|----------|---------|
| Bank transfer | Single `@Transactional` method; rollback on any failure |
| Audit must survive failure | `REQUIRES_NEW` on audit service |
| Read-heavy reporting | `@Transactional(readOnly = true)` on query services |
| Batch with partial logic | `TransactionTemplate` in a loop |
| Publish event after DB commit | `TransactionSynchronization.afterCommit()` |

---

## Extra rapid-fire Q&A

**Isolation levels?** Read uncommitted / read committed / repeatable read / serializable — trade **consistency vs throughput**; defaults are DB-specific.

**Lazy loading after TX?** Session often closed → use fetch join, DTO projections, or `OpenEntityManagerInView` (know trade-offs).

**Two databases?** Two `DataSource`s → two transaction managers; **no single** `@Transactional` spanning both without distributed transaction patterns (often avoided; use **Saga** / **outbox**).

---

## How to study this repo

1. Read `InterviewDemoRunner` — order of demos matches concepts above.
2. Step through in debugger: place breakpoint in `TransferService.transfer`.
3. Temporarily set `logging.level.org.springframework.orm.jpa: DEBUG` to see SQL and TX boundaries.
