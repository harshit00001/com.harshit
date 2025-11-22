# Kafka Spring Boot Interview - Quick Start Guide

## Prerequisites

1. **Java 17+** installed
2. **Maven 3.6+** installed
3. **Apache Kafka** running on localhost:9092

## Setting Up Kafka

### Option 1: Using Docker (Recommended)

```bash
# Start Zookeeper and Kafka using Docker Compose
docker-compose up -d
```

### Option 2: Local Installation

1. Download Kafka from https://kafka.apache.org/downloads
2. Start Zookeeper:
   ```bash
   bin/zookeeper-server-start.sh config/zookeeper.properties
   ```
3. Start Kafka:
   ```bash
   bin/kafka-server-start.sh config/server.properties
   ```

## Running the Application

1. **Clone/Navigate to the project directory**
   ```bash
   cd kafka-springboot-interview
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Verify it's running**
   - You should see: "Kafka Interview Application Started!"
   - Server running on: http://localhost:8080

## Testing the Application

### 1. Send a Message via REST API

```bash
# Send a simple message
curl -X POST "http://localhost:8080/api/kafka/send?topic=order-created-topic&message=Hello%20Kafka"

# Send a message with key
curl -X POST "http://localhost:8080/api/kafka/send-with-key?topic=order-created-topic&key=user-123&message=Order%20created"

# Create an order (publishes to Kafka)
curl -X POST "http://localhost:8080/api/kafka/orders" \
  -H "Content-Type: application/json" \
  -d "{\"orderId\":\"123\",\"userId\":\"user-456\",\"amount\":100.50}"
```

### 2. Check Consumer Logs

Watch the console output - you should see messages being consumed by the `@KafkaListener` methods in `KafkaConsumerService`.

## Project Structure

```
kafka-springboot-interview/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/harshit/kafka/
│       │       ├── KafkaInterviewApplication.java
│       │       ├── basics/
│       │       │   ├── BasicKafkaProducer.java      # Standard Java API
│       │       │   └── BasicKafkaConsumer.java      # Standard Java API
│       │       ├── spring/
│       │       │   ├── KafkaProducerService.java    # Spring KafkaTemplate
│       │       │   └── KafkaConsumerService.java    # @KafkaListener
│       │       ├── controller/
│       │       │   └── KafkaController.java         # REST API endpoints
│       │       └── advanced/
│       │           ├── ErrorHandlingConsumer.java   # Retry & DLT
│       │           └── PartitioningExample.java     # Partitioning concepts
│       └── resources/
│           └── application.properties               # Kafka configuration
├── README.md                                        # Complete Q&A guide
└── pom.xml                                          # Maven dependencies
```

## Key Topics Covered

### Basic Level
- What is Kafka and why it was introduced
- Kafka architecture and components
- Topics, partitions, and offsets
- Producers and consumers
- Consumer groups

### Intermediate Level
- Spring Boot integration
- @KafkaListener annotation
- KafkaTemplate usage
- Error handling strategies
- Message partitioning
- Offset management

### Advanced Level
- Fault tolerance and high availability
- Consumer group rebalancing
- Duplicate message handling
- Delivery semantics (at-least-once, exactly-once)
- Performance monitoring and tuning
- Real-world use cases

## Learning Path

1. **Start with Basics**: Read `BasicKafkaProducer.java` and `BasicKafkaConsumer.java` to understand core concepts
2. **Spring Integration**: Study `KafkaProducerService.java` and `KafkaConsumerService.java` for Spring Boot patterns
3. **Advanced Concepts**: Explore `ErrorHandlingConsumer.java` and `PartitioningExample.java`
4. **Interview Prep**: Read through `README.md` for comprehensive Q&A in narrative format

## Common Issues

### Kafka not running
- **Error**: Connection refused on localhost:9092
- **Solution**: Make sure Kafka is running and accessible on port 9092

### Topics not found
- **Error**: Topic does not exist
- **Solution**: Topics are auto-created by default. If disabled, create topics manually:
  ```bash
  kafka-topics.sh --create --topic order-created-topic --bootstrap-server localhost:9092
  ```

### Port already in use
- **Error**: Port 8080 already in use
- **Solution**: Change `server.port` in `application.properties` or stop the conflicting service

## Next Steps

1. Read the complete `README.md` for all interview questions and answers
2. Practice explaining concepts out loud using the narrative format
3. Experiment with the code examples
4. Try implementing your own producer/consumer scenarios

---

**Happy Learning! 🚀**

