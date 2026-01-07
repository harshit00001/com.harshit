# Quick Start Guide

## 🚀 Fastest Way to Run Examples

### Prerequisites Check
```bash
# Check Java version (should be 17+)
java -version

# Check Maven
mvn -version

# Check if Kafka is installed
# Navigate to Kafka directory and verify
```

### 1. Start Kafka (5 minutes)

**Windows:**
```bash
# Terminal 1: Start Zookeeper (if Kafka < 2.8)
cd C:\kafka
bin\windows\zookeeper-server-start.bat config\zookeeper.properties

# Terminal 2: Start Kafka
cd C:\kafka
bin\windows\kafka-server-start.bat config\server.properties
```

**Linux/Mac:**
```bash
# Terminal 1: Start Zookeeper (if needed)
cd /path/to/kafka
bin/zookeeper-server-start.sh config/zookeeper.properties

# Terminal 2: Start Kafka
cd /path/to/kafka
bin/kafka-server-start.sh config/server.properties
```

### 2. Build Project (1 minute)
```bash
cd com.harshit/kafka-interview-practical
mvn clean install
```

### 3. Run Examples (Choose One)

#### Example 1: Basic Producer
```bash
# Create topic
bin\windows\kafka-topics.bat --create --topic basic-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

# Run producer
mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q1.BasicProducerExample"

# In another terminal, consume messages
bin\windows\kafka-console-consumer.bat --topic basic-topic --from-beginning --bootstrap-server localhost:9092
```

#### Example 2: Basic Consumer
```bash
# First, send some messages (run Example 1 or use console producer)
# Then run consumer
mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q2.BasicConsumerExample"
```

#### Example 3: Consumer Groups
```bash
# Create topic
bin\windows\kafka-topics.bat --create --topic orders-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

# Terminal 1: Run producer
mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q3.ProducerForConsumerGroup"

# Terminal 2: Consumer 1
mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q3.ConsumerGroupExample" -Dexec.args="consumer-1"

# Terminal 3: Consumer 2
mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q3.ConsumerGroupExample" -Dexec.args="consumer-2"
```

#### Example 4: Error Handling
```bash
# Create topics
bin\windows\kafka-topics.bat --create --topic events-topic --bootstrap-server localhost:9092 --partitions 1
bin\windows\kafka-topics.bat --create --topic events-dlq --bootstrap-server localhost:9092 --partitions 1

# Terminal 1: Producer
mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q4.ErrorHandlingProducer"

# Terminal 2: Consumer
mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q4.ErrorHandlingExample"

# Terminal 3: Check DLQ
bin\windows\kafka-console-consumer.bat --topic events-dlq --from-beginning --bootstrap-server localhost:9092
```

#### Example 5: Spring Boot
```bash
# Start Spring Boot app
mvn spring-boot:run

# In another terminal, send messages via REST API
curl http://localhost:8080/api/kafka/send?message=Hello%20Kafka
```

## ✅ Verification Checklist

- [ ] Kafka server is running (check Terminal 2)
- [ ] Zookeeper is running (if needed, check Terminal 1)
- [ ] Topic created successfully
- [ ] Producer sends messages (check console output)
- [ ] Consumer receives messages (check console output)
- [ ] No connection errors

## 🐛 Common Issues

**Issue:** "Connection refused"
- **Solution:** Ensure Kafka is running on port 9092

**Issue:** "Topic not found"
- **Solution:** Create topic before running producer/consumer

**Issue:** "ClassNotFoundException"
- **Solution:** Run `mvn clean install` first

**Issue:** Consumer not receiving messages
- **Solution:** Check if messages were already consumed, try `--from-beginning` flag

## 📚 Next Steps

1. Read the full [README.md](README.md) for detailed explanations
2. Modify code to experiment
3. Try different configurations
4. Test with multiple consumers/groups
5. Practice interview questions with code

