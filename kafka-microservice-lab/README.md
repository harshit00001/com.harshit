# Kafka Microservice Lab

**Hands-on walkthrough (Producer → partition → offset → consumer groups): see [README2.md](README2.md).**

Small **event-driven** demo: **Order** (producer) → topic **`order-events`** (3 partitions) → **Billing** + **Inventory** (two consumer groups). Built for **IntelliJ debug** on `ConsumerRecord` (partition, offset, key).

## Architecture

```text
  POST /api/orders          topic: order-events (3 partitions)
  [Order Service]  ───────►  [ Kafka broker ]
                                    │
                    ┌───────────────┴───────────────┐
                    ▼                               ▼
            group: billing-service          group: inventory-service
            (2 listener threads)            (1 listener)
            [BillingConsumer]                 [InventoryConsumer]
```

| Component | Code |
|-----------|------|
| **Producer** | `order.OrderProducerService` |
| **Topic / partitions** | `config.KafkaConfig` → 3 partitions |
| **Consumer group A** | `billing.BillingConsumer` → `billing-service` |
| **Consumer group B** | `inventory.InventoryConsumer` → `inventory-service` |

## Prerequisites

- Java 17+
- Docker Desktop (Kafka + Kafka UI)
- Maven

## 1. Start Kafka

```powershell
cd "c:\Users\harshraj\OneDrive - AMDOCS\harshit pc\java code\com.harshit\kafka-microservice-lab"
docker compose up -d
```

- **Kafka:** `localhost:9092`
- **Kafka UI:** http://localhost:8080 → Topics → `order-events` → partitions, offsets, messages

## 2. Run the app (debug)

In IntelliJ: open `KafkaMicroserviceLabApplication` → **Debug** (port **8085**).

Or:

```powershell
mvn spring-boot:run
```

## 3. Publish events

```powershell
curl -X POST http://localhost:8085/api/orders -H "Content-Type: application/json" -d "{\"customerId\":\"cust-1\",\"item\":\"RoamingPlan\",\"amount\":49.99}"
```

Response includes **kafkaPartition** and **kafkaOffset**.

**Partition demo** — same `orderId` → same partition:

```powershell
curl -X POST http://localhost:8085/api/orders -H "Content-Type: application/json" -d "{\"orderId\":\"order-100\",\"customerId\":\"c1\",\"item\":\"A\",\"amount\":10}"
```

Help: `GET http://localhost:8085/api/orders/help`

## 4. Debug breakpoints

| File | Method | Inspect |
|------|--------|---------|
| `OrderProducerService` | `publish`, `logSendResult` | `RecordMetadata` partition, offset |
| `BillingConsumer` | `onOrder` | `record.partition()`, `record.offset()`, `record.key()` |
| `InventoryConsumer` | `onOrder` | Same event, different consumer group |

## 5. Stop

```powershell
docker compose down
```
