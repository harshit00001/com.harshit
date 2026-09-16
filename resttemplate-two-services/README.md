# Two microservices + RestTemplate

`order-service` (port **8082**) calls `inventory-service` (port **8081**) over HTTP using **RestTemplate**.
No Kafka, no discovery server, no database — so you can explain the call path in an interview in two minutes.

```
You (browser / curl)
        |
        |  POST /api/orders
        v
+------------------+         RestTemplate          +---------------------+
|  order-service   |  GET  /api/inventory/{sku}    | inventory-service   |
|  :8082           | ----------------------------> | :8081               |
|                  |  POST /api/inventory/{sku}/   |  in-memory stock    |
|                  |       reserve                 |                     |
+------------------+ <---------------------------- +---------------------+
```

---

## Prerequisites

- JDK 17
- Maven

If `JAVA_HOME` still points at Java 8:

```powershell
$env:JAVA_HOME = "C:\Users\harshraj\OneDrive - AMDOCS\Bell Canada\Java"
```

---

## Steps to run

Open **two** terminals. Start **inventory first** — order-service will 503 if it is down.

### Terminal 1 — inventory-service

```powershell
cd "C:\Users\harshraj\OneDrive - AMDOCS\harshit pc\java code\com.harshit\resttemplate-two-services\inventory-service"
mvn spring-boot:run
```

Wait for: `Tomcat started on port 8081`

Sanity check:

```powershell
curl http://localhost:8081/api/inventory
```

You should see three SKUs (`SKU-PHONE` qty 10, `SKU-LAPTOP` qty 4, `SKU-CABLE` qty 50).

### Terminal 2 — order-service

```powershell
cd "C:\Users\harshraj\OneDrive - AMDOCS\harshit pc\java code\com.harshit\resttemplate-two-services\order-service"
mvn spring-boot:run
```

Wait for: `Tomcat started on port 8082`

### Terminal 3 — place an order (this is the RestTemplate hop)

```powershell
curl -X POST http://localhost:8082/api/orders `
  -H "Content-Type: application/json" `
  -d "{\"sku\":\"SKU-PHONE\",\"qty\":2}"
```

Example response:

```json
{
  "orderId": "ORD-A1B2C3D4",
  "sku": "SKU-PHONE",
  "qty": 2,
  "status": "CONFIRMED",
  "remainingStock": 8
}
```

Watch **both** logs:

- order-service: `RestTemplate GET http://localhost:8081/api/inventory/SKU-PHONE`
- inventory-service: `GET stock sku=SKU-PHONE`
- order-service: `RestTemplate POST .../reserve`
- inventory-service: `RESERVE sku=SKU-PHONE qty=2`

List orders:

```powershell
curl http://localhost:8082/api/orders
```

Stock after the order:

```powershell
curl http://localhost:8081/api/inventory/SKU-PHONE
```

`availableQty` should now be **8**.

---

## What to try after the happy path

| Call | What you prove |
|---|---|
| `POST` qty `99` for `SKU-PHONE` | inventory returns **409**; order-service maps it back (no fake success) |
| `POST` sku `SKU-UNKNOWN` | inventory **404** travels through RestTemplate as `HttpStatusCodeException` |
| Stop inventory, then `POST` an order | order-service returns **503** (`ResourceAccessException` / connection refused) |

---

## Communication process (step by step)

Read **[COMMUNICATION.md](COMMUNICATION.md)** for the full sequence, RestTemplate methods, and interview talking points.

Short version of one `POST /api/orders`:

1. Your curl hits **order-service only**. You never call inventory yourself.
2. `OrderController` → `OrderService.place`.
3. `InventoryClient.getStock` → `restTemplate.getForObject("http://localhost:8081/api/inventory/{sku}", ...)`.
4. inventory returns JSON → Jackson maps it to `StockItem`.
5. If qty is OK, `InventoryClient.reserve` → `restTemplate.exchange(..., POST, ...)`.
6. inventory deducts stock and returns remaining qty.
7. order-service saves the order in memory and returns `OrderResponse` to you.

JSON field names must match on both sides (`sku`, `availableQty`, `qty`, …). RestTemplate does **not** share Java classes across JVMs — each service has its own DTO records.

---

## Layout

```
resttemplate-two-services/
  inventory-service/     # callee, port 8081
  order-service/         # caller, port 8082, RestTemplate bean
  COMMUNICATION.md
  README.md
```

Base URL is `inventory.base-url` in `order-service` `application.yml`. Point it at another host without code changes.
