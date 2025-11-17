# System Design Patterns - Interview Preparation

A comprehensive Java project covering essential system design patterns commonly asked in software engineering interviews, with simple code examples and easy-to-understand explanations.

## 📚 Patterns Covered

### 1. **Saga Pattern** (`saga/`)
- **File**: `SagaPatternExample.java`
- **Concepts**: Orchestration vs Choreography, Distributed Transactions, Compensation
- **Use Cases**: E-commerce orders, Payment processing, Multi-step workflows

### 2. **Circuit Breaker Pattern** (`circuitbreaker/`)
- **File**: `CircuitBreakerPattern.java`
- **Concepts**: Failure handling, Cascading failures, Service resilience
- **Use Cases**: External API calls, Microservices communication, Fault tolerance

### 3. **CQRS Pattern** (`cqrs/`)
- **File**: `CQRSPattern.java`
- **Concepts**: Command Query Separation, Read/Write models, Event-driven updates
- **Use Cases**: High-read systems, Complex queries, Independent scaling

### 4. **Event Sourcing** (`eventsourcing/`)
- **File**: `EventSourcingPattern.java`
- **Concepts**: Event store, State reconstruction, Audit trail, Time travel
- **Use Cases**: Banking systems, Audit requirements, Debugging complex systems

### 5. **Rate Limiting** (`ratelimit/`)
- **File**: `RateLimitingPattern.java`
- **Concepts**: Token Bucket, Sliding Window, API protection
- **Use Cases**: API gateways, DDoS protection, Fair resource allocation

### 6. **Retry Pattern** (`retry/`)
- **File**: `RetryPattern.java`
- **Concepts**: Exponential backoff, Jitter, Transient failures
- **Use Cases**: Network calls, External services, Database connections

### 7. **Publisher-Subscriber** (`pubsub/`)
- **File**: `PublisherSubscriberPattern.java`
- **Concepts**: Decoupling, Event-driven architecture, Message routing
- **Use Cases**: Microservices communication, Real-time systems, Notifications

## 🎯 Interview Topics

### Basic Concepts
- ✅ Distributed transactions
- ✅ Service resilience patterns
- ✅ Event-driven architecture
- ✅ API protection
- ✅ Fault tolerance

### Advanced Concepts
- ✅ Saga orchestration vs choreography
- ✅ Circuit breaker states (CLOSED, OPEN, HALF_OPEN)
- ✅ CQRS with event sourcing
- ✅ Rate limiting algorithms
- ✅ Retry strategies

## 🚀 How to Use This Project

1. **Start with Basics**: Begin with `SagaPatternExample.java` to understand distributed transactions
2. **Learn Resilience**: Study `CircuitBreakerPattern.java` for fault tolerance
3. **Understand CQRS**: Explore `CQRSPattern.java` for read/write separation
4. **Master Events**: Go through `EventSourcingPattern.java` for event-driven design
5. **Protect APIs**: Learn `RateLimitingPattern.java` for API security
6. **Handle Failures**: Study `RetryPattern.java` for resilient systems
7. **Decouple Systems**: Understand `PublisherSubscriberPattern.java` for loose coupling

## 💡 Interview Tips

### When Explaining Patterns:

1. **Start with the Problem**: Always explain why we need this pattern
   - "In distributed systems, we can't use traditional transactions because..."

2. **Explain the Solution**: Describe how the pattern solves it
   - "Saga pattern solves this by using a sequence of local transactions..."

3. **Give Examples**: Provide real-world use cases
   - "This is used in e-commerce when processing orders across multiple services"

4. **Compare Approaches**: Show you understand trade-offs
   - "Orchestration is centralized but simpler, choreography is decentralized but harder to debug"

5. **Mention Challenges**: Show depth of knowledge
   - "The main challenge is eventual consistency and handling partial failures"

### Common Interview Questions:

**Q: What is the Saga pattern?**
- **Technical**: A pattern for managing distributed transactions by breaking them into a sequence of local transactions, each with a compensating transaction for rollback.
- **Simple**: Instead of one big transaction across services, we do small transactions one by one. If any fails, we undo the previous ones.

**Q: When would you use CQRS?**
- **Technical**: When read and write workloads are significantly different, requiring independent scaling and optimization of read and write models.
- **Simple**: When you read data much more than you write it, or when your read queries are very complex and would slow down writes.

**Q: What's the difference between Circuit Breaker and Retry?**
- **Technical**: Retry handles transient failures by attempting operations again, while Circuit Breaker prevents calls to failing services to avoid resource waste and cascading failures.
- **Simple**: Retry tries again when something fails temporarily. Circuit Breaker stops trying when a service is clearly down, to avoid wasting time and resources.

**Q: Explain Event Sourcing.**
- **Technical**: Instead of storing current state, store all events that led to that state. State is reconstructed by replaying events.
- **Simple**: Like a bank statement - instead of just storing your current balance, store every deposit and withdrawal. You can calculate your balance at any time by adding them up.

**Q: How does Rate Limiting work?**
- **Technical**: Restricts the number of requests a client can make in a time period using algorithms like Token Bucket or Sliding Window.
- **Simple**: Like a speed limit - you can only make so many requests per minute. If you exceed it, your requests are rejected.

## 📝 Pattern Comparison

| Pattern | Problem Solved | Key Benefit |
|---------|----------------|-------------|
| **Saga** | Distributed transactions | Handles multi-service transactions |
| **Circuit Breaker** | Cascading failures | Prevents calling failing services |
| **CQRS** | Read/write conflicts | Independent scaling of reads/writes |
| **Event Sourcing** | Lost history | Complete audit trail and time travel |
| **Rate Limiting** | Resource abuse | Protects APIs from overload |
| **Retry** | Transient failures | Handles temporary failures gracefully |
| **Pub-Sub** | Tight coupling | Decouples components via events |

## 🔍 Key Concepts to Remember

- **Distributed Transactions**: Transactions across multiple services/databases
- **Compensation**: Undoing a completed operation (opposite of rollback)
- **Eventual Consistency**: Data will be consistent eventually, not immediately
- **Idempotency**: Operation can be repeated safely without side effects
- **Backpressure**: Slowing down producers when consumers can't keep up
- **Thundering Herd**: Many clients retrying at the same time

## 🎓 Practice Exercises

After studying each pattern:

1. **Modify the code**: Change parameters, add features, experiment
2. **Create variations**: Implement different strategies or algorithms
3. **Combine patterns**: Use multiple patterns together (e.g., Retry + Circuit Breaker)
4. **Explain out loud**: Practice explaining as if in an interview
5. **Design systems**: Apply patterns to design real systems

## 📖 Additional Resources

- **Books**: 
  - "Designing Data-Intensive Applications" by Martin Kleppmann
  - "Microservices Patterns" by Chris Richardson
- **Online**: 
  - AWS Architecture Patterns
  - Martin Fowler's Blog
  - System Design Interview resources

## ⚠️ Important Notes

- Patterns are tools, not solutions - use them when appropriate
- Understand trade-offs (consistency vs availability, complexity vs flexibility)
- Consider your specific use case before applying patterns
- Start simple, add patterns as needed
- Test thoroughly, especially failure scenarios

---

**Happy Learning! 🚀**

Practice explaining each pattern in both technical and simple terms. The code examples are designed to help you understand and explain these concepts during interviews.



