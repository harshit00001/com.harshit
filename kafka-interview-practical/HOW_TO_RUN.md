# How to Run and Verify the Project

## ✅ Step-by-Step Verification Guide

### Prerequisites Check

First, verify you have everything installed:

```bash
# Check Java version (should be 17+)
java -version

# Check Maven
mvn -version

# Check if Kafka is installed (navigate to Kafka directory)
cd C:\kafka  # or your Kafka installation path
dir  # should see bin, config folders
```

---

## 🚀 Complete Test Flow

### Step 1: Start Kafka Server

**Open Terminal 1 (PowerShell/Command Prompt):**

```bash
# Navigate to Kafka directory
cd C:\kafka  # Replace with your Kafka path

# Start Zookeeper (if Kafka version < 2.8)
bin\windows\zookeeper-server-start.bat config\zookeeper.properties
```

**Open Terminal 2 (PowerShell/Command Prompt):**

```bash
# Navigate to Kafka directory
cd C:\kafka

# Start Kafka Server
bin\windows\kafka-server-start.bat config\server.properties
```

**Wait for:** You should see `[KafkaServer id=0] started` in Terminal 2

**✅ Verification:** Kafka is running if you see "started" message and no errors

---

### Step 2: Verify Kafka is Running

**Open Terminal 3 (PowerShell/Command Prompt):**

```bash
# Navigate to Kafka directory
cd C:\kafka

# List topics (should work without errors)
bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092
```

**✅ Expected Output:** Either empty list `[]` or list of existing topics (no errors)

**❌ If Error:** 
- Check if Kafka is running (Step 1)
- Check if port 9092 is available
- Verify bootstrap-server address

---

### Step 3: Build the Project

**In Terminal 3 (or new terminal):**

```bash
# Navigate to project directory
cd "C:\Java code\com.harshit\kafka-interview-practical"

# Build the project
mvn clean install
```

**✅ Expected Output:** `BUILD SUCCESS` at the end

**❌ If Error:**
- Check Java version (must be 17+)
- Check Maven installation
- Check internet connection (Maven downloads dependencies)

---

### Step 4: Create Topic for Testing

**In Terminal 3:**

```bash
# Navigate to Kafka directory
cd C:\kafka

# Create topic
bin\windows\kafka-topics.bat --create --topic basic-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
```

**✅ Expected Output:** `Created topic basic-topic.`

**Verify topic exists:**
```bash
bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092
```

**✅ Should see:** `basic-topic` in the list

---

### Step 5: Run Producer (Send Messages)

**In Terminal 3 (or new terminal):**

```bash
# Navigate to project directory
cd "C:\Java code\com.harshit\kafka-interview-practical"

# Run the producer
mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q1.BasicProducerExample"
```

**✅ Expected Output:**
```
Message sent successfully!
Topic: basic-topic
Partition: X
Offset: Y
Timestamp: Z
...
All messages sent successfully!
```

**✅ Verification Checklist:**
- [ ] No connection errors
- [ ] Messages show "sent successfully"
- [ ] Shows partition numbers (0, 1, or 2)
- [ ] Shows offset numbers
- [ ] Final message: "All messages sent successfully!"

**❌ If Error:**
- `Connection refused`: Kafka not running (go back to Step 1)
- `Topic not found`: Topic not created (go back to Step 4)
- `ClassNotFoundException`: Project not built (go back to Step 3)

---

### Step 6: Verify Messages Were Sent (Console Consumer)

**Open Terminal 4 (new PowerShell/Command Prompt):**

```bash
# Navigate to Kafka directory
cd C:\kafka

# Consume messages from beginning
bin\windows\kafka-console-consumer.bat --topic basic-topic --from-beginning --bootstrap-server localhost:9092
```

**✅ Expected Output:**
```
Message 1 from Kafka Producer
Message 2 from Kafka Producer
Message 3 from Kafka Producer
...
Message 10 from Kafka Producer
```

**✅ Verification:** You should see all 10 messages

**Press Ctrl+C to stop the consumer**

---

### Step 7: Run Consumer (Java Code)

**In Terminal 3 (stop producer if still running with Ctrl+C):**

```bash
# Navigate to project directory
cd "C:\Java code\com.harshit\kafka-interview-practical"

# Run the consumer
mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q2.BasicConsumerExample"
```

**✅ Expected Output:**
```
Subscribed to topic: basic-topic
Consumer Group: basic-consumer-group
Waiting for messages...

Received 10 message(s)
=========================================
Topic: basic-topic
Partition: X
Offset: Y
Key: key-1
Value: Message 1 from Kafka Producer
Timestamp: Z
=========================================
...
Offsets committed successfully
```

**✅ Verification Checklist:**
- [ ] Shows "Subscribed to topic"
- [ ] Receives messages
- [ ] Shows partition, offset, key, value
- [ ] Shows "Offsets committed successfully"

**Note:** Consumer runs continuously. Press Ctrl+C to stop.

---

## 🎯 Quick Test Summary

**Minimum test to verify everything works:**

1. ✅ Kafka running (Terminal 2 shows "started")
2. ✅ Topic created (`basic-topic` exists)
3. ✅ Producer sends messages (10 messages, no errors)
4. ✅ Console consumer shows messages (all 10 visible)
5. ✅ Java consumer receives messages (shows metadata)

---

## 🔧 Troubleshooting

### Problem: "Connection refused" or "Connection to node -1 could not be established"

**Solution:**
```bash
# Check if Kafka is running
# Go to Terminal 2, you should see Kafka server running
# If not, start it (Step 1)

# Check if port 9092 is in use
netstat -an | findstr 9092
```

### Problem: "Topic 'basic-topic' does not exist"

**Solution:**
```bash
# Create the topic
cd C:\kafka
bin\windows\kafka-topics.bat --create --topic basic-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

# Verify it exists
bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092
```

### Problem: "ClassNotFoundException"

**Solution:**
```bash
# Rebuild the project
cd "C:\Java code\com.harshit\kafka-interview-practical"
mvn clean install
```

### Problem: Consumer not receiving messages

**Solution:**
```bash
# Check if messages were already consumed
# Reset consumer group offset
cd C:\kafka
bin\windows\kafka-consumer-groups.bat --bootstrap-server localhost:9092 --group basic-consumer-group --reset-offsets --to-earliest --topic basic-topic --execute

# Or use different group ID in code
```

### Problem: Maven build fails

**Solution:**
```bash
# Check Java version
java -version  # Should be 17+

# Clear Maven cache and rebuild
mvn clean install -U
```

---

## 📊 Success Indicators

### ✅ Everything is Working If:

1. **Kafka Server:**
   - Terminal shows "started" message
   - No error messages
   - Can list topics without errors

2. **Producer:**
   - Sends 10 messages
   - Shows "Message sent successfully!" for each
   - Shows partition, offset, timestamp
   - Final message: "All messages sent successfully!"

3. **Consumer:**
   - Subscribes to topic
   - Receives all 10 messages
   - Shows complete metadata
   - Commits offsets successfully

4. **Console Consumer:**
   - Shows all 10 messages
   - Messages match what producer sent

---

## 🎓 Next Steps After Verification

Once everything works:

1. **Try Consumer Groups Example (Q3):**
   ```bash
   # Create topic
   bin\windows\kafka-topics.bat --create --topic orders-topic --bootstrap-server localhost:9092 --partitions 3
   
   # Run producer
   mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q3.ProducerForConsumerGroup"
   
   # Run multiple consumers in different terminals
   mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q3.ConsumerGroupExample" -Dexec.args="consumer-1"
   ```

2. **Try Spring Boot Example (Q5):**
   ```bash
   # Start Spring Boot
   mvn spring-boot:run
   
   # In another terminal, send message
   curl http://localhost:8080/api/kafka/send?message=Hello%20Kafka
   ```

3. **Try Error Handling Example (Q4):**
   ```bash
   # Create topics
   bin\windows\kafka-topics.bat --create --topic events-topic --bootstrap-server localhost:9092
   bin\windows\kafka-topics.bat --create --topic events-dlq --bootstrap-server localhost:9092
   
   # Run examples
   mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q4.ErrorHandlingProducer"
   mvn compile exec:java -Dexec.mainClass="com.harshit.kafka.interview.q4.ErrorHandlingExample"
   ```

---

## 💡 Pro Tips

1. **Keep Kafka running:** Don't close Terminal 2 (Kafka server)
2. **Use multiple terminals:** One for Kafka, one for commands, one for producer, one for consumer
3. **Check logs:** If something fails, check the terminal output for error messages
4. **Start simple:** Always test with Q1 (Basic Producer) first
5. **Verify step by step:** Don't skip verification steps

---

**🎉 If all steps work, your Kafka setup is working correctly!**

