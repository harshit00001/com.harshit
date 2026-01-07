# Kafka Interview Questions Summary

This document lists all the practical interview questions covered in this project with quick references.

## 📝 Question List

### Q1: How do you create a basic Kafka Producer in Java?
- **File:** `q1/BasicProducerExample.java`
- **Topics Covered:**
  - Producer configuration
  - Creating ProducerRecord
  - Sending messages (sync/async)
  - Callbacks and error handling
  - Producer lifecycle (flush, close)
- **Key Points:**
  - Bootstrap servers configuration
  - Serializers (StringSerializer)
  - ACKS configuration (all, 1, 0)
  - Retry configuration
- **Run:** `mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q1.BasicProducerExample"`

---

### Q2: How do you create a basic Kafka Consumer in Java?
- **File:** `q2/BasicConsumerExample.java`
- **Topics Covered:**
  - Consumer configuration
  - Consumer groups
  - Subscribing to topics
  - Polling for messages
  - Manual offset commit
  - Auto-offset-reset (earliest/latest)
- **Key Points:**
  - Group ID importance
  - Deserializers (StringDeserializer)
  - Poll() method behavior
  - Offset management
- **Run:** `mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q2.BasicConsumerExample"`

---

### Q3: How do Consumer Groups work in Kafka?
- **Files:** 
  - `q3/ConsumerGroupExample.java`
  - `q3/ProducerForConsumerGroup.java`
- **Topics Covered:**
  - Consumer group concept
  - Partition distribution
  - Multiple consumers in same group
  - Different groups consuming independently
  - Rebalancing
- **Key Points:**
  - Same group ID = share partitions
  - Each partition consumed by only one consumer in group
  - If consumers > partitions, some idle
  - Different groups consume same messages independently
- **Run:** 
  - Producer: `mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q3.ProducerForConsumerGroup"`
  - Consumer: `mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q3.ConsumerGroupExample" -Dexec.args="consumer-1"`

---

### Q4: How do you handle errors in Kafka Consumer?
- **Files:**
  - `q4/ErrorHandlingExample.java`
  - `q4/ErrorHandlingProducer.java`
- **Topics Covered:**
  - Deserialization error handling
  - Processing error handling
  - Retry logic with exponential backoff
  - Dead Letter Queue (DLQ)
  - Commit strategies on errors
  - Poison message handling
- **Key Points:**
  - Don't commit on error (retry)
  - Send to DLQ after max retries
  - Handle deserialization errors separately
  - Exponential backoff for retries
- **Run:**
  - Producer: `mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q4.ErrorHandlingProducer"`
  - Consumer: `mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q4.ErrorHandlingExample"`

---

### Q5: How do you implement Kafka Producer/Consumer in Spring Boot?
- **Files:**
  - `q5/SpringKafkaProducerService.java`
  - `q5/SpringKafkaConsumerService.java`
  - `q5/KafkaController.java`
- **Topics Covered:**
  - Spring Boot auto-configuration
  - KafkaTemplate usage
  - @KafkaListener annotation
  - Manual acknowledgment
  - Accessing metadata with @Header
  - REST API integration
- **Key Points:**
  - Less boilerplate than native Kafka client
  - Auto-configuration from application.properties
  - Multiple listener patterns
  - Integration with Spring ecosystem
- **Run:** `mvn spring-boot:run`
- **Test:** `curl http://localhost:8080/api/kafka/send?message=Hello`

---

## 🎯 Common Follow-up Questions

### Based on Q1 (Producer):
1. What's the difference between `acks=0`, `acks=1`, and `acks=all`?
2. How does batching work in Kafka Producer?
3. What happens if a producer fails before flush()?
4. How do you ensure exactly-once semantics?

### Based on Q2 (Consumer):
1. What's the difference between `earliest` and `latest` in auto-offset-reset?
2. When should you use manual vs automatic commit?
3. What happens if a consumer crashes before committing?
4. How does consumer rebalancing work?

### Based on Q3 (Consumer Groups):
1. What happens if you have more consumers than partitions?
2. How does Kafka distribute partitions among consumers?
3. What triggers a rebalance?
4. Can different consumer groups consume the same messages?

### Based on Q4 (Error Handling):
1. What's a Dead Letter Queue and when to use it?
2. How do you handle poison messages?
3. What's the best retry strategy?
4. How do you handle deserialization errors?

### Based on Q5 (Spring Boot):
1. How does Spring Boot auto-configure Kafka?
2. What's the difference between KafkaTemplate and native Producer?
3. How do you configure multiple Kafka clusters?
4. How do you test Kafka in Spring Boot?

---

## 📚 Additional Topics to Explore

1. **Partitioning Strategies**
   - Key-based partitioning
   - Custom partitioners
   - Round-robin partitioning

2. **Exactly-Once Semantics**
   - Idempotent producer
   - Transactional producer
   - Consumer transactions

3. **Kafka Streams**
   - Stream processing
   - Stateful operations
   - Windowing

4. **Performance Tuning**
   - Batch size optimization
   - Compression
   - Producer/Consumer tuning

5. **Monitoring & Operations**
   - Consumer lag
   - Broker metrics
   - Topic configuration

---

## 💡 Interview Tips

1. **Start with basics:** Always explain Producer/Consumer fundamentals first
2. **Show understanding:** Explain WHY you're doing something, not just HOW
3. **Discuss trade-offs:** Mention pros/cons of different approaches
4. **Think about production:** Consider error handling, monitoring, scaling
5. **Know the concepts:** Consumer groups, partitions, offsets, replication
6. **Practice coding:** Be able to write producer/consumer code from scratch

---

## 🔗 Quick Reference

| Question | Main Class | Topic Name | Group ID |
|----------|-----------|------------|----------|
| Q1 | BasicProducerExample | basic-topic | N/A |
| Q2 | BasicConsumerExample | basic-topic | basic-consumer-group |
| Q3 | ConsumerGroupExample | orders-topic | orders-consumer-group |
| Q4 | ErrorHandlingExample | events-topic | error-handling-group |
| Q5 | SpringKafkaProducerService | basic-topic | spring-consumer-group |

---

## 📖 Study Path

1. **Beginner:** Q1 → Q2 → Q5
2. **Intermediate:** Q3 → Q4
3. **Advanced:** Custom partitioners, exactly-once, Kafka Streams

---

Good luck with your interviews! 🚀

