# spring-transaction-interview

Spring Boot **3.2**, Java **17**, H2 in-memory — demonstrates `@Transactional`, propagation, rollback rules, self-invocation, CGLIB vs JDK (via `spring.aop.proxy-target-class`), `TransactionTemplate`, and `afterCommit`.

Requires **JDK 17+** to build and run (`mvn` must use Java 17).

## Run

```bash
cd spring-transaction-interview
mvn spring-boot:run
```

Console output walks through each scenario. **Interview Q&A script:** [docs/INTERVIEW_SCRIPT.md](docs/INTERVIEW_SCRIPT.md).

## H2 console

[http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
JDBC URL: `jdbc:h2:mem:txdemo` — user `sa`, empty password (see `application.yml`).

## Key classes

- `demo.InterviewDemoRunner` — startup demos
- `docs/INTERVIEW_SCRIPT.md` — scripted answers aligned with code
- `docs/DEEP_DIVE.md` — longer explanations (after commit, proxy chain, propagation) when short comments are not enough

**Tip:** In IntelliJ, press **Quick Documentation** (Ctrl+Q) on a class name — the expanded Javadoc on `TransferService`, `TransactionInternalsDemo`, `ReportingServiceBad`, etc. is written for interview prep.
