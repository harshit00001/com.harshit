# Step-by-Step Demo Guide

## 🎯 How to Run and Demonstrate

### 1. Start the Application

```bash
mvn spring-boot:run
```

The application will automatically run a complete demonstration of all strategies.

---

## 📡 REST API Endpoints for Live Demonstration

### Strategy 3: Caching

**Endpoint:** `GET http://localhost:8080/api/demo/cache/{id}`

**Steps to demonstrate:**
1. First call: `GET http://localhost:8080/api/demo/cache/1`
   - ⏱️ **SLOW** - Hits database (check console for "Executing database query")
   - Response shows duration (e.g., 50ms)

2. Second call: `GET http://localhost:8080/api/demo/cache/1`
   - ✅ **FAST** - Returns from cache (no database query)
   - Response shows much faster duration (e.g., 2ms)

3. **Explain:** Show the difference in response times and console output

**Example:**
```bash
# First call - slow
curl http://localhost:8080/api/demo/cache/1

# Second call - fast (from cache)
curl http://localhost:8080/api/demo/cache/1
```

---

### Strategy 4: Async Processing

**Endpoint:** `POST http://localhost:8080/api/demo/async?email=test@example.com`

**Steps to demonstrate:**
1. Call the endpoint: `POST http://localhost:8080/api/demo/async?email=test@example.com`
2. **Immediately** check the response - it returns in < 5ms
3. **Then** check the console - email sending happens in background thread
4. **Explain:** Main thread didn't wait, async thread handled the email

**Example:**
```bash
curl -X POST "http://localhost:8080/api/demo/async?email=test@example.com"
```

**What to show:**
- Response returns immediately
- Console shows different thread names (main vs async-1)
- Background processing continues

---

### Strategy 5: Pagination

**Endpoint:** `GET http://localhost:8080/api/demo/pagination?page=0&size=10`

**Steps to demonstrate:**
1. Call with page 0: `GET http://localhost:8080/api/demo/pagination?page=0&size=10`
   - Shows: Loaded 10 records out of total
   
2. Call with page 1: `GET http://localhost:8080/api/demo/pagination?page=1&size=10`
   - Shows: Next 10 records
   
3. **Explain:** Only requested page is loaded, not all records

**Example:**
```bash
# Page 0 - First 10 records
curl "http://localhost:8080/api/demo/pagination?page=0&size=10"

# Page 1 - Next 10 records
curl "http://localhost:8080/api/demo/pagination?page=1&size=10"
```

---

### Strategy 1: Connection Pooling

**Endpoint:** `GET http://localhost:8080/api/demo/connection-pool`

**Steps to demonstrate:**
1. Call the endpoint
2. Check console - shows connection reuse
3. **Explain:** Connections are reused from pool, not created each time

**Example:**
```bash
curl http://localhost:8080/api/demo/connection-pool
```

---

### Strategy 2: Lazy Loading

**Endpoint:** `GET http://localhost:8080/api/demo/lazy-loading`

**Steps to demonstrate:**
1. Call the endpoint
2. Check console - shows lazy loading behavior
3. **Explain:** Related entities loaded only when accessed

**Example:**
```bash
curl http://localhost:8080/api/demo/lazy-loading
```

---

### Cache Operations

**Endpoint:** `GET http://localhost:8080/api/demo/cache-operations/{id}`

**Steps to demonstrate:**
1. Shows @Cacheable, @CachePut, and @CacheEvict
2. Check console for each operation
3. **Explain:** Different cache annotations for different scenarios

**Example:**
```bash
curl http://localhost:8080/api/demo/cache-operations/1
```

---

### All Strategies at Once

**Endpoint:** `GET http://localhost:8080/api/demo/all`

**Steps to demonstrate:**
1. Call the endpoint
2. Watch console for complete demonstration
3. **Explain:** All strategies working together

**Example:**
```bash
curl http://localhost:8080/api/demo/all
```

---

## 🎬 Presentation Flow

### Recommended Order:

1. **Start Application** - Show automatic demo
2. **Caching** - Call `/api/demo/cache/1` twice, show difference
3. **Async** - Call `/api/demo/async`, show immediate return
4. **Pagination** - Call `/api/demo/pagination`, show page loading
5. **Connection Pool** - Call `/api/demo/connection-pool`, explain reuse
6. **Lazy Loading** - Call `/api/demo/lazy-loading`, explain N+1 problem
7. **All Together** - Call `/api/demo/all` for complete demo

---

## 📊 What to Show in Console

### Caching:
- First call: "Executing database query"
- Second call: No query message (from cache)

### Async:
- Main thread name vs async thread name
- Immediate return vs background processing

### Pagination:
- Only requested page size loaded
- Total elements vs current page size

### Connection Pool:
- Connection class name (HikariCP)
- Connection reuse messages

---

## 💡 Key Points to Explain

1. **Caching:** First call slow, subsequent calls fast
2. **Async:** Non-blocking, main thread continues
3. **Pagination:** Memory efficient, loads only needed data
4. **Connection Pool:** Reuse connections, not create new
5. **Lazy Loading:** Prevents N+1 queries

---

## 🛠️ Tools to Use

- **Browser:** For GET requests
- **Postman:** For POST requests and better visualization
- **curl:** Command line testing
- **Console:** Watch detailed logs and explanations

---

## 📝 Example Script for Video

1. "Let me show you caching in action..."
   - Call endpoint first time (slow)
   - Call endpoint second time (fast)
   - Explain the difference

2. "Now let's see async processing..."
   - Call async endpoint
   - Show immediate response
   - Show console with background thread

3. "Here's pagination..."
   - Call with different page numbers
   - Show only requested data loaded

And so on for each strategy!





