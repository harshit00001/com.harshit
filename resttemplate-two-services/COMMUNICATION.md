# How the two services talk (RestTemplate)

This is the file to open before an interview. Every hop below is what actually runs when you
`POST http://localhost:8082/api/orders`.

---

## 1. Who is who

| Service | Port | Role | HTTP client |
|---|---|---|---|
| `inventory-service` | 8081 | Callee. Owns stock. | none |
| `order-service` | 8082 | Caller. Owns orders. | **RestTemplate** |

They are two JVMs. They share nothing except JSON over HTTP.

```
┌─────────────┐   JSON over HTTP    ┌──────────────────┐
│   You       │                     │                  │
│   curl      │──── POST /orders ──►│  order-service   │
└─────────────┘                     │  RestTemplate    │
                                    │        │         │
                                    │        │ GET /api/inventory/{sku}
                                    │        │ POST /api/inventory/{sku}/reserve
                                    │        ▼         │
                                    └────────┬─────────┘
                                             │
                                             ▼
                                    ┌──────────────────┐
                                    │ inventory-service│
                                    │  ConcurrentHashMap
                                    └──────────────────┘
```

---

## 2. Sequence of one confirmed order

```
You                 OrderController       InventoryClient        InventoryController
 |                        |                      |                        |
 | POST /api/orders       |                      |                        |
 | {sku, qty}             |                      |                        |
 |----------------------->|                      |                        |
 |                        | place(sku, qty)      |                        |
 |                        |--------------------->|                        |
 |                        |                      | getForObject GET       |
 |                        |                      | /api/inventory/SKU-PHONE
 |                        |                      |----------------------->|
 |                        |                      | 200 StockItem          |
 |                        |                      |<-----------------------|
 |                        |                      | exchange POST          |
 |                        |                      | /api/inventory/SKU-PHONE/reserve
 |                        |                      | { "qty": 2 }           |
 |                        |                      |----------------------->|
 |                        |                      | 200 remainingQty=8     |
 |                        |                      |<-----------------------|
 |                        | OrderResponse        |                        |
 | 200 CONFIRMED          |<---------------------|                        |
 |<-----------------------|                      |                        |
```

Two RestTemplate calls on purpose:

1. **GET** — read. Safe to retry. Confirms the SKU exists.
2. **POST reserve** — write. Deducts stock. This is the side effect.

In production you might skip GET and only POST (one network round-trip). The GET is here so you can **see** `getForObject` and `exchange` in the logs.

---

## 3. What RestTemplate actually does

Bean (timeouts are the interview detail):

```java
@Bean
public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
            .setConnectTimeout(Duration.ofSeconds(2))
            .setReadTimeout(Duration.ofSeconds(3))
            .build();
}
```

| Method in `InventoryClient` | RestTemplate API | HTTP |
|---|---|---|
| `getStock` | `getForObject(url, StockItem.class, sku)` | GET |
| `reserve` | `exchange(url, POST, HttpEntity, ReserveResponse.class, sku)` | POST |

`{sku}` in the URL is a URI template. RestTemplate substitutes it — you do not concatenate the path by hand.

Jackson maps JSON ↔ records because field names match:

```json
{ "sku": "SKU-PHONE", "name": "...", "availableQty": 10 }
```

↔ `record StockItem(String sku, String name, int availableQty)`

---

## 4. Errors (map HTTP, do not swallow)

| Inventory returns | RestTemplate throws | order-service returns to you |
|---|---|---|
| 404 unknown SKU | `HttpClientErrorException` | 404 |
| 409 not enough stock | `HttpClientErrorException` | 409 |
| process down / timeout | `ResourceAccessException` | **503** Service Unavailable |

That last row is why timeouts exist. Without them, a dead inventory-service holds a Tomcat worker until the OS TCP timeout (can be minutes).

---

## 5. What RestTemplate is **not**

- Not async (`WebClient` / `RestClient` are the newer APIs; RestTemplate is blocking and still asked in interviews).
- Not a service registry. URL is `inventory.base-url=http://localhost:8081`. With Eureka you would still use RestTemplate (or a load-balanced `RestTemplate` with `@LoadBalanced`).
- Not a transaction across services. If POST reserve succeeds and order-service then crashes, stock is already gone. That is the distributed-transaction problem; this demo does not pretend to solve it (saga / outbox would).

---

## 6. Interview answers (short)

**Why RestTemplate as a `@Bean`?**  
One shared client with timeouts. `new RestTemplate()` in every method forgets timeouts and wastes connections.

**Why a separate `InventoryClient`?**  
So `OrderService` does not know URLs. You can stub the client in a unit test without starting inventory.

**Synchronous?**  
Yes. The order HTTP request waits until inventory answers. That is simpler than Kafka, and it couples availability: if inventory is down, orders fail (503).

**How would you harden this?**  
Retries with backoff on GET only, circuit breaker (Resilience4j), idempotency key on reserve, replace RestTemplate with `RestClient` (Spring 6.1) using the same URLs.
