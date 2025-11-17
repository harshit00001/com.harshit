# Spring REST Annotations - Complete Guide

A comprehensive Spring Boot project demonstrating all HTTP methods (GET, POST, PUT, PATCH, DELETE) and Spring MVC annotations with practical examples using an Employee model.

## 📚 What's Covered

### HTTP Methods
- ✅ **GET**: Retrieve data
- ✅ **POST**: Create new resources
- ✅ **PUT**: Update entire resource
- ✅ **PATCH**: Partial updates
- ✅ **DELETE**: Remove resources

### Spring Annotations
- ✅ **@PathVariable**: Extract values from URL path
- ✅ **@RequestParam**: Extract query parameters
- ✅ **@RequestBody**: Extract JSON/XML from request body
- ✅ **@RequestHeader**: Extract HTTP headers
- ✅ **@RequestAttribute**: Extract request attributes
- ✅ **@ModelAttribute**: Bind form data
- ✅ **@Valid**: Enable validation

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6+

### Running the Application

1. **Navigate to project directory:**
   ```bash
   cd spring-rest-annotations
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Application will start on:**
   ```
   http://localhost:8080
   ```

## 📖 API Endpoints

### Base URL: `http://localhost:8080/api/employees`

### GET Endpoints

#### 1. Get All Employees
```http
GET /api/employees
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "department": "Engineering",
    "salary": 75000.0,
    "hireDate": "2020-01-15",
    "position": "Software Engineer"
  }
]
```

#### 2. Get Employee by ID (@PathVariable)
```http
GET /api/employees/1
```

**Interview Point:** `@PathVariable` extracts `1` from URL and maps it to `Long id` parameter.

#### 3. Search Employees (@RequestParam)
```http
GET /api/employees/search?department=Engineering&minSalary=50000&name=John
```

**Query Parameters:**
- `department` (optional): Filter by department
- `minSalary` (optional): Minimum salary filter
- `name` (optional): Search by name (contains)

**Interview Point:** `@RequestParam` extracts values from `?key=value` in URL.

#### 4. Get Employee Details with Headers (@RequestHeader)
```http
GET /api/employees/1/details
Headers:
  X-Client-Id: mobile-app
  User-Agent: Mozilla/5.0
```

**Interview Point:** `@RequestHeader` extracts values from HTTP headers.

### POST Endpoints

#### 5. Create Employee (@RequestBody)
```http
POST /api/employees
Content-Type: application/json

{
  "name": "Bob Wilson",
  "email": "bob.wilson@example.com",
  "department": "Sales",
  "salary": 60000.0,
  "hireDate": "2023-01-10",
  "position": "Sales Representative"
}
```

**Interview Point:** `@RequestBody` converts JSON to Employee object automatically.

**Response:** `201 Created` with the created employee.

#### 6. Create Employee from Form (@ModelAttribute)
```http
POST /api/employees/form
Content-Type: application/x-www-form-urlencoded

name=Bob Wilson&email=bob@example.com&department=Sales&salary=60000
```

**Interview Point:** `@ModelAttribute` binds form-encoded data to object.

### PUT Endpoint

#### 7. Update Entire Employee
```http
PUT /api/employees/1
Content-Type: application/json

{
  "id": 1,
  "name": "John Updated",
  "email": "john.updated@example.com",
  "department": "Engineering",
  "salary": 85000.0,
  "hireDate": "2020-01-15",
  "position": "Senior Software Engineer"
}
```

**Interview Point:** PUT replaces the entire resource. All fields must be provided.

### PATCH Endpoints

#### 8. Partial Update (@RequestBody with Map)
```http
PATCH /api/employees/1
Content-Type: application/json

{
  "salary": 80000,
  "department": "Engineering"
}
```

**Interview Point:** PATCH updates only provided fields. Uses `Map<String, Object>` for flexibility.

#### 9. Update Salary Only (@RequestParam)
```http
PATCH /api/employees/1/salary?newSalary=85000
```

**Interview Point:** Can use `@RequestParam` with PATCH for simple updates.

### DELETE Endpoints

#### 10. Delete Employee by ID
```http
DELETE /api/employees/1
```

**Response:** `204 No Content` if successful, `404 Not Found` if employee doesn't exist.

#### 11. Delete by Department (@RequestParam)
```http
DELETE /api/employees?department=Engineering
```

**Response:**
```json
{
  "deletedCount": 2,
  "deletedIds": [1, 3],
  "department": "Engineering"
}
```

## 🎯 Annotation Explanations

### @PathVariable

**Technical:** Extracts values from URL path segments. The value in `{id}` maps to the method parameter.

**Simple:** When URL is `/employees/123`, the `123` becomes the `id` parameter.

**Example:**
```java
@GetMapping("/{id}")
public Employee getEmployee(@PathVariable Long id) {
    // id = 123 from /employees/123
}
```

**URL:** `/api/employees/123` → `id = 123`

---

### @RequestParam

**Technical:** Extracts query parameters from URL (`?key=value`). Can be optional or required, with default values.

**Simple:** Gets values from the `?` part of URL. Like `?name=John&age=30`.

**Example:**
```java
@GetMapping("/search")
public List<Employee> search(@RequestParam String name,
                             @RequestParam(required = false) String department) {
    // name from ?name=John
    // department from ?department=Engineering (optional)
}
```

**URL:** `/api/employees/search?name=John&department=Engineering`

---

### @RequestBody

**Technical:** Binds HTTP request body (JSON/XML) to Java object. Spring uses Jackson to deserialize JSON automatically.

**Simple:** The JSON you send in the request body gets converted to a Java object.

**Example:**
```java
@PostMapping
public Employee create(@RequestBody Employee employee) {
    // JSON {"name": "John"} becomes Employee object
}
```

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com"
}
```

---

### @RequestHeader

**Technical:** Extracts values from HTTP headers. Common for authentication tokens, client info, etc.

**Simple:** Gets information from HTTP headers (like `Authorization: Bearer token`).

**Example:**
```java
@GetMapping("/details")
public Employee getDetails(@RequestHeader("Authorization") String authToken) {
    // Gets value from Authorization header
}
```

**Headers:**
```
Authorization: Bearer abc123xyz
X-Client-Id: mobile-app
```

---

### @ModelAttribute

**Technical:** Binds form data (`application/x-www-form-urlencoded`) or query parameters to object. Used for HTML forms.

**Simple:** Used when submitting HTML forms (not JSON). Converts form fields to object.

**Example:**
```java
@PostMapping("/form")
public Employee createFromForm(@ModelAttribute Employee employee) {
    // Form fields become Employee object
}
```

**Form Data:**
```
name=John Doe&email=john@example.com&department=Engineering
```

---

### @Valid

**Technical:** Enables Bean Validation (JSR-303). Validates object fields using annotations like `@NotNull`, `@Email`, etc.

**Simple:** Automatically checks if the data is valid (not null, correct format, etc.) before processing.

**Example:**
```java
@PostMapping
public Employee create(@Valid @RequestBody Employee employee) {
    // Validates employee before this method runs
    // Returns 400 Bad Request if validation fails
}
```

**Validation Annotations:**
- `@NotNull`: Field cannot be null
- `@NotBlank`: String cannot be blank
- `@Email`: Must be valid email format
- `@Min(0)`: Number must be >= 0
- `@Size(min=2, max=50)`: String length constraint

---

## 💡 Interview Questions & Answers

### Q: What's the difference between @PathVariable and @RequestParam?

**Technical:**
- `@PathVariable`: Extracts values from URL path segments (`/employees/{id}`)
- `@RequestParam`: Extracts values from query string (`?key=value`)

**Simple:**
- Path variable: Part of the URL path itself (`/employees/123`)
- Request param: Added after `?` (`/employees?name=John`)

**Example:**
```java
// PathVariable: /api/employees/123
@GetMapping("/{id}")
public Employee get(@PathVariable Long id) { }

// RequestParam: /api/employees?id=123
@GetMapping
public Employee get(@RequestParam Long id) { }
```

---

### Q: What's the difference between PUT and PATCH?

**Technical:**
- PUT: Replaces entire resource. All fields must be provided. Idempotent.
- PATCH: Partial update. Only provided fields are updated. Idempotent.

**Simple:**
- PUT: Like overwriting a file completely
- PATCH: Like editing specific parts of a document

**Example:**
```java
// PUT: Must send all fields
PUT /api/employees/1
{ "id": 1, "name": "John", "email": "john@example.com", "salary": 50000 }

// PATCH: Only send fields to update
PATCH /api/employees/1
{ "salary": 60000 }  // Only updates salary
```

---

### Q: When to use @RequestBody vs @ModelAttribute?

**Technical:**
- `@RequestBody`: For JSON/XML (REST APIs). Content-Type: `application/json`
- `@ModelAttribute`: For form data (HTML forms). Content-Type: `application/x-www-form-urlencoded`

**Simple:**
- RequestBody: When sending JSON from JavaScript/frontend
- ModelAttribute: When submitting HTML forms

**Example:**
```java
// JSON API
@PostMapping
public Employee create(@RequestBody Employee emp) { }

// HTML Form
@PostMapping("/form")
public Employee create(@ModelAttribute Employee emp) { }
```

---

### Q: How does @Valid work?

**Technical:**
- Enables Bean Validation (JSR-303)
- Validates object before method execution
- Returns `400 Bad Request` if validation fails
- Requires `@Valid` annotation and validation annotations on model

**Simple:**
- Automatically checks if data is correct
- If email is invalid or required field is missing, returns error
- No need to manually check each field

**Example:**
```java
@PostMapping
public Employee create(@Valid @RequestBody Employee emp) {
    // If emp.email is not valid email format,
    // Spring returns 400 Bad Request automatically
}
```

---

## 🧪 Testing with cURL

### Get All Employees
```bash
curl http://localhost:8080/api/employees
```

### Get Employee by ID
```bash
curl http://localhost:8080/api/employees/1
```

### Search Employees
```bash
curl "http://localhost:8080/api/employees/search?department=Engineering&minSalary=50000"
```

### Create Employee
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Johnson",
    "email": "alice@example.com",
    "department": "HR",
    "salary": 55000.0,
    "position": "HR Manager"
  }'
```

### Update Employee (PUT)
```bash
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "name": "John Updated",
    "email": "john.updated@example.com",
    "department": "Engineering",
    "salary": 85000.0,
    "position": "Senior Engineer"
  }'
```

### Partial Update (PATCH)
```bash
curl -X PATCH http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "salary": 90000,
    "department": "Engineering"
  }'
```

### Delete Employee
```bash
curl -X DELETE http://localhost:8080/api/employees/1
```

### Get with Headers
```bash
curl -X GET http://localhost:8080/api/employees/1/details \
  -H "X-Client-Id: mobile-app" \
  -H "User-Agent: MyApp/1.0"
```

---

## 📝 Project Structure

```
spring-rest-annotations/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── rest/
│       │           └── annotations/
│       │               ├── SpringRestAnnotationsApplication.java
│       │               ├── controller/
│       │               │   └── EmployeeController.java
│       │               └── model/
│       │                   └── Employee.java
│       └── resources/
│           └── application.properties
├── pom.xml
└── README.md
```

---

## 🎓 Key Takeaways

1. **@PathVariable**: For URL path segments (`/employees/{id}`)
2. **@RequestParam**: For query parameters (`?name=John`)
3. **@RequestBody**: For JSON/XML in request body
4. **@RequestHeader**: For HTTP headers
5. **@ModelAttribute**: For form data
6. **@Valid**: Enables automatic validation
7. **PUT**: Replace entire resource
8. **PATCH**: Partial update
9. **POST**: Create new resource
10. **DELETE**: Remove resource

---

**Happy Learning! 🚀**

Practice these endpoints and annotations to master Spring REST APIs!



