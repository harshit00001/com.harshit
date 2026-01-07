# Kafka Interview Practical Questions & Answers

A comprehensive collection of practical Kafka interview questions with working code examples and step-by-step reproduction guides.

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [Setup Instructions](#setup-instructions)
3. [Interview Questions](#interview-questions)
4. [How to Run Examples](#how-to-run-examples)
5. [Common Kafka Commands](#common-kafka-commands)

---

## Prerequisites

- **Java 17+** installed
- **Maven 3.6+** installed
- **Apache Kafka** installed and running
- **Zookeeper** (if using Kafka < 2.8, otherwise Kafka uses KRaft mode)

### Installing Kafka

**Windows:**
```bash
# Download Kafka from https://kafka.apache.org/downloads
# Extract to a folder, e.g., C:\kafka
```

**Linux/Mac:**
```bash
# Using Homebrew (Mac)
brew install kafka

# Or download from https://kafka.apache.org/downloads
```

---

## Setup Instructions

### Step 1: Start Zookeeper (if needed)

**For Kafka < 2.8:**
```bash
# Navigate to Kafka directory
cd C:\kafka  # or your Kafka installation path

# Start Zookeeper
bin\windows\zookeeper-server-start.bat config\zookeeper.properties
```

**For Kafka 2.8+ (KRaft mode):**
```bash
# No Zookeeper needed - Kafka uses KRaft
```

### Step 2: Start Kafka Server

```bash
# Navigate to Kafka directory
cd C:\kafka

# Start Kafka (Windows)
bin\windows\kafka-server-start.bat config\server.properties

# Start Kafka (Linux/Mac)
bin/kafka-server-start.sh config/server.properties
```

### Step 3: Verify Kafka is Running

Open a new terminal and run:
```bash
# List topics (should return empty or existing topics)
bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092
```

### Step 4: Build the Project

```bash
# Navigate to project directory
cd com.harshit/kafka-interview-practical

# Build with Maven
mvn clean install
```

---

## Interview Questions

### Question 1: How do you create a basic Kafka Producer in Java?

**Location:** `src/main/java/com/harshit/kafka/interview/q1/BasicProducerExample.java`

**Key Concepts:**
- Producer configuration (bootstrap servers, serializers)
- Creating ProducerRecord
- Sending messages (synchronous/asynchronous)
- Callbacks for success/failure handling

**Steps to Reproduce:**

1. **Create a topic:**
```bash
bin\windows\kafka-topics.bat --create --topic basic-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
```

2. **Run the Producer:**
```bash
# Compile and run
mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q1.BasicProducerExample"
```

3. **Verify messages were sent:**
```bash
# In another terminal, consume messages
bin\windows\kafka-console-consumer.bat --topic basic-topic --from-beginning --bootstrap-server localhost:9092
```

**Expected Output:**
- Producer sends 10 messages
- Each message shows partition, offset, and timestamp
- Console consumer displays all messages

---

### Question 2: How do you create a basic Kafka Consumer in Java?

**Location:** `src/main/java/com/harshit/kafka/interview/q2/BasicConsumerExample.java`

**Key Concepts:**
- Consumer configuration (group ID, deserializers)
- Subscribing to topics
- Polling for messages
- Manual offset commit

**Steps to Reproduce:**

1. **Ensure topic exists and has messages:**
   - Run Question 1's producer first, or send messages manually

2. **Run the Consumer:**
```bash
mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q2.BasicConsumerExample"
```

3. **Send messages to test (in another terminal):**
```bash
# Using console producer
bin\windows\kafka-console-producer.bat --topic basic-topic --bootstrap-server localhost:9092
# Type messages and press Enter
```

**Expected Output:**
- Consumer displays all messages with metadata
- Shows partition, offset, key, value, and timestamp
- Commits offsets after processing

---

### Question 3: How do Consumer Groups work in Kafka?

**Location:** `src/main/java/com/harshit/kafka/interview/q3/`

**Key Concepts:**
- Consumer groups and partition distribution
- Multiple consumers in same group
- Different groups consuming independently
- Partition assignment

**Steps to Reproduce:**

1. **Create topic with multiple partitions:**
```bash
bin\windows\kafka-topics.bat --create --topic orders-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
```

2. **Run Producer:**
```bash
mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q3.ProducerForConsumerGroup"
```

3. **Run Consumer Instance 1 (Terminal 1):**
```bash
mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q3.ConsumerGroupExample" -Dexec.args="consumer-1"
```

4. **Run Consumer Instance 2 (Terminal 2):**
```bash
mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q3.ConsumerGroupExample" -Dexec.args="consumer-2"
```

5. **Run Consumer Instance 3 (Terminal 3):**
```bash
mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q3.ConsumerGroupExample" -Dexec.args="consumer-3"
```

**Expected Behavior:**
- All 3 consumers share partitions (each gets 1 partition)
- Messages are distributed among consumers
- If you stop one consumer, its partitions are reassigned to others

**Test Different Consumer Groups:**
- Modify `GROUP_ID` in code to create a new group
- Run consumers with different group IDs
- Observe that each group consumes all messages independently

---

### Question 4: How do you handle errors in Kafka Consumer?

**Location:** `src/main/java/com/harshit/kafka/interview/q4/`

**Key Concepts:**
- Error handling strategies
- Retry logic with exponential backoff
- Dead Letter Queue (DLQ)
- Deserialization error handling
- Commit strategies on errors

**Steps to Reproduce:**

1. **Create topics:**
```bash
bin\windows\kafka-topics.bat --create --topic events-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
bin\windows\kafka-topics.bat --create --topic events-dlq --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

2. **Run Producer (sends mix of valid/invalid messages):**
```bash
mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q4.ErrorHandlingProducer"
```

3. **Run Consumer with Error Handling:**
```bash
mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q4.ErrorHandlingExample"
```

**Expected Behavior:**
- Valid messages are processed successfully
- Failed messages are retried (up to 3 times)
- Messages that fail after retries are sent to DLQ
- Deserialization errors are caught and sent to DLQ
- Offsets are committed appropriately

4. **Check DLQ:**
```bash
bin\windows\kafka-console-consumer.bat --topic events-dlq --from-beginning --bootstrap-server localhost:9092
```

---

### Question 5: How do you implement Kafka Producer/Consumer in Spring Boot?

**Location:** `src/main/java/com/harshit/kafka/interview/q5/`

**Key Concepts:**
- Spring Boot auto-configuration
- KafkaTemplate for producers
- @KafkaListener for consumers
- Manual acknowledgment
- Accessing metadata with @Header

**Steps to Reproduce:**

1. **Start Spring Boot Application:**
```bash
mvn spring-boot:run
```

2. **Send Messages via REST API:**

   **Simple message:**
   ```bash
   curl http://localhost:8080/api/kafka/send?message=Hello%20Kafka
   ```

   **Message with key:**
   ```bash
   curl http://localhost:8080/api/kafka/send-with-key?key=user-1&message=Hello
   ```

   **Message with callback:**
   ```bash
   curl -X POST http://localhost:8080/api/kafka/send-callback -H "Content-Type: text/plain" -d "Hello with Callback"
   ```

   **Synchronous send:**
   ```bash
   curl -X POST http://localhost:8080/api/kafka/send-sync -H "Content-Type: text/plain" -d "Hello Synchronous"
   ```

3. **Observe Consumer Logs:**
   - Check console output for consumer messages
   - Multiple listeners will consume the same messages (different groups)
   - See metadata (partition, offset, key) in logs

**Expected Behavior:**
- Messages are sent via REST API
- Multiple Spring Boot consumers receive messages
- Different consumer groups show different consumption patterns
- Manual acknowledgment consumer processes and commits

---

## Common Kafka Commands

### Topic Management

```bash
# List all topics
bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092

# Create topic
bin\windows\kafka-topics.bat --create --topic my-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

# Describe topic
bin\windows\kafka-topics.bat --describe --topic my-topic --bootstrap-server localhost:9092

# Delete topic
bin\windows\kafka-topics.bat --delete --topic my-topic --bootstrap-server localhost:9092
```

### Producer (Console)

```bash
# Send messages via console
bin\windows\kafka-console-producer.bat --topic my-topic --bootstrap-server localhost:9092
```

### Consumer (Console)

```bash
# Consume from beginning
bin\windows\kafka-console-consumer.bat --topic my-topic --from-beginning --bootstrap-server localhost:9092

# Consume with group
bin\windows\kafka-console-consumer.bat --topic my-topic --bootstrap-server localhost:9092 --group my-group

# Consume with key and value
bin\windows\kafka-console-consumer.bat --topic my-topic --from-beginning --bootstrap-server localhost:9092 --property print.key=true --property print.value=true
```

### Consumer Groups

```bash
# List consumer groups
bin\windows\kafka-consumer-groups.bat --bootstrap-server localhost:9092 --list

# Describe consumer group
bin\windows\kafka-consumer-groups.bat --bootstrap-server localhost:9092 --group my-group --describe

# Reset offsets
bin\windows\kafka-consumer-groups.bat --bootstrap-server localhost:9092 --group my-group --topic my-topic --reset-offsets --to-earliest --execute
```

---

## Project Structure

```
kafka-interview-practical/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── harshit/
        │           └── kafka/
        │               └── interview/
        │                   ├── KafkaInterviewApplication.java
        │                   ├── q1/          # Basic Producer
        │                   ├── q2/          # Basic Consumer
        │                   ├── q3/          # Consumer Groups
        │                   ├── q4/          # Error Handling
        │                   └── q5/          # Spring Boot
        └── resources/
            └── application.properties
```

---

## Troubleshooting

### Kafka not starting
- Check if port 9092 is available
- Verify Zookeeper is running (if needed)
- Check logs in `logs/` directory

### Connection refused
- Ensure Kafka server is running
- Verify bootstrap server address: `localhost:9092`
- Check firewall settings

### Topic not found
- Create topic before running producers/consumers
- Verify topic name matches in code

### Consumer not receiving messages
- Check consumer group ID
- Verify `auto-offset-reset` setting (earliest/latest)
- Ensure producer sent messages successfully
- Check if messages were already consumed (offset committed)

---

## Interview Tips

1. **Understand the basics first:** Producer, Consumer, Topics, Partitions
2. **Know Consumer Groups:** How they work, partition distribution
3. **Error Handling:** Retry logic, DLQ, commit strategies
4. **Spring Boot Integration:** KafkaTemplate, @KafkaListener
5. **Performance:** Batching, compression, partitioning strategies
6. **Reliability:** Acks, replication, exactly-once semantics

---

## Additional Resources

- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Spring Kafka Documentation](https://docs.spring.io/spring-kafka/docs/current/reference/html/)
- [Kafka Best Practices](https://kafka.apache.org/documentation/#best_practices)

---

## License

This project is for educational purposes and interview preparation.

