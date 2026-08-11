# README2 — Learn Kafka in practice (hands-on lab)

Use this **after** Docker + Kafka UI + `KafkaMicroserviceLabApplication` are running (Dashboard shows **1 broker**, topics appear after the app starts).

| Tool | URL / port |
|------|------------|
| **Kafka UI** | http://localhost:8080 |
| **Order API (Producer)** | http://localhost:8085 |
| **Broker (from your PC)** | `localhost:9092` |

---

## Big picture (one sentence per box)

```text
You (curl/Postman)  →  Order API (Producer)  →  Broker stores log on a Partition
                                                      ↓
                              Consumer Group A (billing) reads + commits offset
                              Consumer Group B (inventory) reads + commits offset
```

| Term | What it is in this lab | Where you see it |
|------|------------------------|------------------|
| **Producer** | `OrderProducerService` sends `OrderEvent` | App logs `PRODUCER ack ...` |
| **Broker** | Kafka server in Docker (`kafka-lab`) | UI → **Brokers** → count 1 |
| **Topic** | Named stream `order-events` | UI → **Topics** |
| **Partition** | 3 shards of the topic (0, 1, 2) | UI → topic → **Partitions** |
| **Offset** | Index of message inside a partition | UI → Messages; API response; debugger |
| **Consumer group** | Team name sharing work | UI → **Consumers** → `billing-service`, `inventory-service` |
| **Consumer** | Running listener thread in your app | UI → consumer group → members |

---

## Before you start (checklist)

1. `docker compose up -d` in `kafka-microservice-lab` folder.
2. Kafka UI Dashboard: **Online 1**, **Brokers count 1**, version not "Unknown".
3. Run **`KafkaMicroserviceLabApplication`** in **Debug** (port 8085).
4. In UI → **Topics**: you should see **`order-events`** (3 partitions). If missing, restart the Spring app once Kafka is up.

---

## Lab 1 — Producer → broker → partition → offset

**Goal:** Publish one event and tie **API response** to **Kafka UI** to **logs**.

### Step 1 — Publish (Producer)

PowerShell:

```powershell
curl -X POST http://localhost:8085/api/orders `
  -H "Content-Type: application/json" `
  -d "{\"customerId\":\"lab1\",\"item\":\"SIM\",\"amount\":19.99}"
```

Note the JSON response:

- `kafkaTopic` → should be `order-events`
- `kafkaPartition` → e.g. `1`
- `kafkaOffset` → e.g. `0` (first message on that partition)
- `orderId` → message **key**

### Step 2 — App log (Producer ack)

In the IDE console, find a line like:

```text
PRODUCER ack topic=order-events partition=1 offset=0 timestamp=... key=...
```

**Check:** partition/offset match the HTTP response.

### Step 3 — Kafka UI (Broker + Topic + Partition + Message)

1. Open http://localhost:8080
2. **Topics** → click **`order-events`**
3. Open **Messages** (or browse by partition)
4. Select the **partition** from step 1 (e.g. Partition **1**)
5. Find your message; confirm **Offset** matches step 1

You have just traced: **Producer → Broker → Partition log → Offset**.

### Step 4 — Debugger (optional)

Breakpoint in `OrderProducerService.logSendResult` → inspect `meta.partition()`, `meta.offset()`.

---

## Lab 2 — Consumer group vs consumer (two groups, same event)

**Goal:** One published message is read **twice** — once per **consumer group** (not once per consumer in the same group).

### Step 1 — Publish again

```powershell
curl -X POST http://localhost:8085/api/orders `
  -H "Content-Type: application/json" `
  -d "{\"orderId\":\"lab2-order\",\"customerId\":\"c2\",\"item\":\"Roaming\",\"amount\":99}"
```

### Step 2 — App logs (two consumers)

Look for **two** log blocks for the same order:

```text
BillingConsumer | group=billing-service ... partition=... offset=...
InventoryConsumer | group=inventory-service ... partition=... offset=...
```

**Same** topic, partition, offset, key — **different** group id.

### Step 3 — Kafka UI → Consumers

1. **Consumers** (left menu)
2. Open group **`billing-service`**
   - See **members** (often 2 — billing uses `concurrency = "2"`)
   - See **lag** (should be 0 if processing keeps up)
3. Open group **`inventory-service`**
   - Separate member(s), separate committed offsets for the **same** topic

**Rule:** *N consumer groups = N independent copies of the stream.*  
Within **one** group, each partition is handled by **one** consumer at a time.

### Step 4 — Debugger

Breakpoints:

- `BillingConsumer.onOrder`
- `InventoryConsumer.onOrder`

Publish once; debugger hits **both** (order may vary). Inspect `record.group()` is not on record — use logs for group; on record use `record.partition()`, `record.offset()`, `record.key()`.

---

## Lab 3 — Partition choice (message key)

**Goal:** Same **key** (`orderId`) → same **partition**; different keys may go to different partitions.

### Step 1 — Fixed orderId (twice)

```powershell
curl -X POST http://localhost:8085/api/orders -H "Content-Type: application/json" -d "{\"orderId\":\"FIXED-KEY-1\",\"customerId\":\"c1\",\"item\":\"A\",\"amount\":1}"
curl -X POST http://localhost:8085/api/orders -H "Content-Type: application/json" -d "{\"orderId\":\"FIXED-KEY-1\",\"customerId\":\"c1\",\"item\":\"B\",\"amount\":2}"
```

**Check:** both responses show the **same** `kafkaPartition` (offsets differ: 0, 1, 2… on that partition).

### Step 2 — UI

Topic **`order-events`** → Messages on that partition → two messages, same key, consecutive offsets.

### Step 3 — Different keys

Send 5 requests **without** `orderId` (random id each time). Compare `kafkaPartition` in responses — often spread across 0, 1, 2.

**Code link:** key is set in `OrderProducerService.publish` → `kafkaTemplate.send(ORDER_TOPIC, key, event)`.

---

## Lab 4 — Offset grows; lag means “behind”

**Goal:** Understand **offset** as a bookmark per partition per consumer group.

### Step 1

Publish 3 messages (any JSON).

### Step 2 — UI

**Consumers** → `billing-service` → inspect **current offset** / **end offset** / **lag** (labels vary by UI version).

- **Lag 0:** consumer caught up.
- **Lag > 0:** messages waiting (slow consumer or app stopped).

### Step 3 — Experiment

1. **Stop** the Spring app.
2. Publish 2 more messages via curl (they still land in Kafka).
3. UI: lag for both groups should **increase**.
4. **Start** app again → consumers catch up → lag back to **0**.

This shows: messages **persist on broker**; consumers **pull** when ready.

---

## Lab 5 — Broker and internal topics

**Goal:** See the broker as storage, not “the app”.

1. UI → **Brokers** → 1 broker, Kafka **3.8**.
2. **Topics** → enable **Show internal topics** if you want to see `__consumer_offsets` (where committed offsets are stored).  
   Do **not** delete internal topics.

Your app topic: **`order-events`** only — **3 partitions**, replication **1** (local Docker).

---

## Lab 6 — Map UI dashboard to your screenshot

When Dashboard shows:

| Field | Meaning |
|-------|---------|
| **Brokers count 1** | Single-node cluster (fine for learning) |
| **Topics 2** | Usually `order-events` + maybe `__consumer_offsets` counted or another auto topic |
| **Partitions 53** | Sum of partitions on all topics (internal topics have many partitions) |
| **Production / Consumption 0 Bytes** | UI metric; can stay 0 briefly — trust **Messages** tab and app logs for activity |

---

## Quick reference — curl commands

```powershell
# Simple order
curl -X POST http://localhost:8085/api/orders -H "Content-Type: application/json" -d "{\"customerId\":\"x\",\"item\":\"Plan\",\"amount\":10}"

# Help
curl http://localhost:8085/api/orders/help
```

---

## Troubleshooting

| Problem | Fix |
|---------|-----|
| UI loading forever | `docker compose down && docker compose up -d`, wait 30s, refresh UI |
| No `order-events` topic | Restart Spring app after Kafka is healthy |
| curl connection refused | App not running on 8085 |
| No consumer logs | Check `@KafkaListener` started; no errors on startup |

---

## Code map (what to read while labs run)

| File | Role |
|------|------|
| `order/OrderController.java` | REST → trigger producer |
| `order/OrderProducerService.java` | **Producer**, partition via key |
| `config/KafkaConfig.java` | Topic **3 partitions** |
| `billing/BillingConsumer.java` | **Consumer group** `billing-service` |
| `inventory/InventoryConsumer.java` | **Consumer group** `inventory-service` |
| `support/KafkaRecordDebug.java` | Logs partition / offset / key |

---

## Suggested learning order

1. Lab 1 — producer path + UI message  
2. Lab 2 — two consumer groups  
3. Lab 3 — keys and partitions  
4. Lab 4 — stop app, lag, restart  
5. Lab 5 — brokers / internal topics  

When comfortable, debug **`BillingConsumer.onOrder`** with **Evaluate** on `record.offset()` and compare to Kafka UI for the same partition.

---

## One-page memory

```text
POST /api/orders  →  Producer  →  Topic.order-events  →  Partition[P]  @ offset O
                                                      ↘
                        billing-service     reads P@O, commits offset
                        inventory-service   reads P@O, commits offset (separate group)
```

Happy labbing.

---

## Troubleshooting: `InvalidReceiveException` (size = 1195725856)

**Symptom:** Kafka broker logs show `Invalid receive (size = 1195725856 larger than 104857600)` and closes connections from `172.18.0.1`.

**Cause:** Something sent **HTTP** to port **9092** (Kafka’s binary protocol port). The number `1195725856` is not a real message size — it is the ASCII bytes of **`GET `** (a browser HTTP request). Kafka reads the first 4 bytes as length and rejects it.

**Common triggers:**

- Opening `http://localhost:9092` in a browser (9092 is **not** a web UI)
- Health checks or tools probing 9092 with HTTP
- Confusing Kafka UI port with broker port

**Fix:**

1. **Do not** browse to `http://localhost:9092`. Use **Kafka UI** at http://localhost:8080 instead.
2. Spring Boot app should use `bootstrap-servers: localhost:9092` (Kafka **client** protocol — this is correct).
3. Kafka UI inside Docker must use `kafka:29092` (already set in `docker-compose.yml`).
4. After fixing `docker-compose` port mapping, recreate UI: `docker compose up -d --force-recreate kafka-ui`

**Do not** “fix” by raising `socket.request.max.bytes` — that only hides the misdirected HTTP traffic.

This WARN is usually **harmless** if the app still publishes/consumes; stop hitting 9092 with HTTP and the log noise goes away.
