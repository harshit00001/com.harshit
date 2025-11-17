package com.rest.annotations.controller;

import com.rest.annotations.model.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * EMPLOYEE CONTROLLER - Interview Explanation:
 * 
 * This controller demonstrates all HTTP methods and Spring annotations:
 * - GET: Retrieve data
 * - POST: Create new resource
 * - PUT: Update entire resource
 * - PATCH: Partial update
 * - DELETE: Remove resource
 * 
 * Annotations covered:
 * - @PathVariable: Extract values from URL path
 * - @RequestParam: Extract query parameters
 * - @RequestBody: Extract JSON/XML from request body
 * - @RequestHeader: Extract values from HTTP headers
 * - @RequestAttribute: Extract attributes from request
 * - @ModelAttribute: Bind form data
 * - @Valid: Enable validation
 */
@RestController
@RequestMapping("/api/employees")
@Validated
public class EmployeeController {

    // Interview Point: In-memory storage for demo purposes
    // In real application, this would be a service layer calling a database
    private Map<Long, Employee> employees = new HashMap<>();
    private Long nextId = 1L;

    // Initialize with some sample data
    public EmployeeController() {
        employees.put(1L, new Employee(1L, "John Doe", "john.doe@example.com", 
            "Engineering", 75000.0, LocalDate.of(2020, 1, 15), "Software Engineer"));
        employees.put(2L, new Employee(2L, "Jane Smith", "jane.smith@example.com", 
            "Marketing", 65000.0, LocalDate.of(2019, 6, 20), "Marketing Manager"));
        nextId = 3L;
    }

    // ==================== GET METHODS ====================

    /**
     * GET - Retrieve all employees
     * 
     * Interview Point: @GetMapping
     * - Maps to HTTP GET requests
     * - No parameters needed
     * - Returns list of all resources
     * 
     * URL: GET /api/employees
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        System.out.println("GET /api/employees - Retrieving all employees");
        List<Employee> employeeList = new ArrayList<>(employees.values());
        return ResponseEntity.ok(employeeList);
    }

    /**
     * GET - Retrieve employee by ID using @PathVariable
     * 
     * Interview Point: @PathVariable
     * - Extracts value from URL path
     * - {id} in URL maps to Long id parameter
     * - Can specify name if different: @PathVariable("id")
     * 
     * URL: GET /api/employees/1
     * 
     * Simple Explanation: The number in the URL (like /employees/1) 
     * gets passed to the method as the 'id' parameter
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        System.out.println("GET /api/employees/" + id + " - Retrieving employee by ID");
        
        Employee employee = employees.get(id);
        if (employee != null) {
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET - Search employees using @RequestParam
     * 
     * Interview Point: @RequestParam
     * - Extracts query parameters from URL (?key=value)
     * - Optional by default (can be required = true)
     * - Can have default value: defaultValue = "all"
     * - Can specify name: @RequestParam("dept")
     * 
     * URL: GET /api/employees/search?department=Engineering
     * URL: GET /api/employees/search?department=Engineering&minSalary=50000
     * 
     * Simple Explanation: The ?department=Engineering part in URL
     * gets passed as the 'department' parameter
     */
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) @Min(0) Double minSalary,
            @RequestParam(required = false) String name) {
        
        System.out.println("GET /api/employees/search - Searching employees");
        System.out.println("  Parameters - department: " + department + 
                         ", minSalary: " + minSalary + ", name: " + name);
        
        List<Employee> results = employees.values().stream()
            .filter(emp -> department == null || emp.getDepartment().equalsIgnoreCase(department))
            .filter(emp -> minSalary == null || emp.getSalary() >= minSalary)
            .filter(emp -> name == null || emp.getName().toLowerCase().contains(name.toLowerCase()))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(results);
    }

    /**
     * GET - Get employee with @RequestHeader
     * 
     * Interview Point: @RequestHeader
     * - Extracts values from HTTP headers
     * - Common headers: Authorization, User-Agent, Accept, etc.
     * - Can be optional or required
     * 
     * URL: GET /api/employees/{id}/details
     * Header: X-Client-Id: mobile-app
     * 
     * Simple Explanation: Gets information from HTTP headers
     * (like authentication tokens, client type, etc.)
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<Map<String, Object>> getEmployeeDetails(
            @PathVariable Long id,
            @RequestHeader(value = "X-Client-Id", required = false) String clientId,
            @RequestHeader(value = "User-Agent", required = false) String userAgent) {
        
        System.out.println("GET /api/employees/" + id + "/details");
        System.out.println("  Headers - X-Client-Id: " + clientId + ", User-Agent: " + userAgent);
        
        Employee employee = employees.get(id);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, Object> details = new HashMap<>();
        details.put("employee", employee);
        details.put("requestedBy", clientId != null ? clientId : "unknown");
        details.put("userAgent", userAgent);
        
        return ResponseEntity.ok(details);
    }

    // ==================== POST METHOD ====================

    /**
     * POST - Create new employee using @RequestBody
     * 
     * Interview Point: @RequestBody
     * - Binds HTTP request body (JSON/XML) to Java object
     * - Spring automatically converts JSON to object (needs Jackson)
     * - @Valid enables validation on the object
     * 
     * URL: POST /api/employees
     * Body: { "name": "Bob Wilson", "email": "bob@example.com", ... }
     * 
     * Simple Explanation: The JSON data in the request body
     * gets converted to an Employee object automatically
     */
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        System.out.println("POST /api/employees - Creating new employee");
        System.out.println("  Employee: " + employee);
        
        // Interview Point: Generate ID if not provided
        if (employee.getId() == null) {
            employee.setId(nextId++);
        }
        
        // Interview Point: Set hire date if not provided
        if (employee.getHireDate() == null) {
            employee.setHireDate(LocalDate.now());
        }
        
        employees.put(employee.getId(), employee);
        
        // Interview Point: Return 201 Created status with location header
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    /**
     * POST - Create employee using @ModelAttribute (form data)
     * 
     * Interview Point: @ModelAttribute
     * - Binds form data (application/x-www-form-urlencoded) to object
     * - Used for HTML forms
     * - Can also bind query parameters
     * 
     * URL: POST /api/employees/form
     * Content-Type: application/x-www-form-urlencoded
     * Body: name=Bob&email=bob@example.com&department=Sales
     * 
     * Simple Explanation: Used when submitting HTML forms
     * (not JSON, but form-encoded data)
     */
    @PostMapping("/form")
    public ResponseEntity<Employee> createEmployeeFromForm(@Valid @ModelAttribute Employee employee) {
        System.out.println("POST /api/employees/form - Creating employee from form data");
        System.out.println("  Employee: " + employee);
        
        if (employee.getId() == null) {
            employee.setId(nextId++);
        }
        if (employee.getHireDate() == null) {
            employee.setHireDate(LocalDate.now());
        }
        
        employees.put(employee.getId(), employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    // ==================== PUT METHOD ====================

    /**
     * PUT - Update entire employee resource
     * 
     * Interview Point: PUT vs PATCH
     * - PUT: Replace entire resource (all fields must be provided)
     * - PATCH: Partial update (only provided fields are updated)
     * 
     * URL: PUT /api/employees/1
     * Body: Complete employee object with all fields
     * 
     * Simple Explanation: PUT replaces the entire employee record
     * (like overwriting a file completely)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody Employee employee) {
        
        System.out.println("PUT /api/employees/" + id + " - Updating entire employee");
        
        if (!employees.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        
        // Interview Point: PUT replaces entire resource
        employee.setId(id); // Ensure ID matches path variable
        employees.put(id, employee);
        
        return ResponseEntity.ok(employee);
    }

    // ==================== PATCH METHOD ====================

    /**
     * PATCH - Partial update of employee
     * 
     * Interview Point: @RequestBody with Map
     * - For PATCH, we often use Map<String, Object> to handle partial updates
     * - Only update fields that are provided
     * - More flexible than requiring full object
     * 
     * URL: PATCH /api/employees/1
     * Body: { "salary": 80000, "department": "Engineering" }
     * 
     * Simple Explanation: PATCH updates only the fields you send
     * (like editing specific parts of a document)
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Employee> partialUpdateEmployee(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        
        System.out.println("PATCH /api/employees/" + id + " - Partially updating employee");
        System.out.println("  Updates: " + updates);
        
        Employee employee = employees.get(id);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Interview Point: Update only provided fields
        if (updates.containsKey("name")) {
            employee.setName((String) updates.get("name"));
        }
        if (updates.containsKey("email")) {
            employee.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("department")) {
            employee.setDepartment((String) updates.get("department"));
        }
        if (updates.containsKey("salary")) {
            employee.setSalary(((Number) updates.get("salary")).doubleValue());
        }
        if (updates.containsKey("position")) {
            employee.setPosition((String) updates.get("position"));
        }
        if (updates.containsKey("hireDate")) {
            employee.setHireDate(LocalDate.parse((String) updates.get("hireDate")));
        }
        
        employees.put(id, employee);
        return ResponseEntity.ok(employee);
    }

    /**
     * PATCH - Update salary with @RequestParam
     * 
     * Interview Point: Multiple annotations in one method
     * - Can combine @PathVariable, @RequestParam, @RequestHeader, etc.
     * - Each serves a different purpose
     * 
     * URL: PATCH /api/employees/1/salary?newSalary=85000
     */
    @PatchMapping("/{id}/salary")
    public ResponseEntity<Employee> updateSalary(
            @PathVariable Long id,
            @RequestParam @Min(0) Double newSalary) {
        
        System.out.println("PATCH /api/employees/" + id + "/salary?newSalary=" + newSalary);
        
        Employee employee = employees.get(id);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        
        employee.setSalary(newSalary);
        employees.put(id, employee);
        return ResponseEntity.ok(employee);
    }

    // ==================== DELETE METHOD ====================

    /**
     * DELETE - Remove employee
     * 
     * Interview Point: DELETE method
     * - Removes resource
     * - Usually returns 204 No Content or 200 OK
     * - Can use @PathVariable to identify resource
     * 
     * URL: DELETE /api/employees/1
     * 
     * Simple Explanation: Deletes the employee with the given ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        System.out.println("DELETE /api/employees/" + id + " - Deleting employee");
        
        if (employees.remove(id) != null) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    /**
     * DELETE - Delete by department using @RequestParam
     * 
     * Interview Point: DELETE with query parameters
     * - Can use @RequestParam with DELETE
     * - Useful for bulk operations
     * 
     * URL: DELETE /api/employees?department=Engineering
     */
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteEmployeesByDepartment(
            @RequestParam @NotBlank String department) {
        
        System.out.println("DELETE /api/employees?department=" + department);
        
        List<Long> deletedIds = employees.entrySet().stream()
            .filter(entry -> entry.getValue().getDepartment().equalsIgnoreCase(department))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        
        deletedIds.forEach(employees::remove);
        
        Map<String, Object> response = new HashMap<>();
        response.put("deletedCount", deletedIds.size());
        response.put("deletedIds", deletedIds);
        response.put("department", department);
        
        return ResponseEntity.ok(response);
    }

    // ==================== ADVANCED EXAMPLES ====================

    /**
     * GET - Complex example with multiple annotations
     * 
     * Interview Point: Combining multiple annotations
     * - @PathVariable: ID from URL
     * - @RequestParam: Query parameters
     * - @RequestHeader: HTTP headers
     * - @RequestAttribute: Request-scoped attributes (set by filters/interceptors)
     * 
     * URL: GET /api/employees/1/advanced?includeSalary=true
     * Headers: X-Request-Id: req-123, Authorization: Bearer token
     */
    @GetMapping("/{id}/advanced")
    public ResponseEntity<Map<String, Object>> getEmployeeAdvanced(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") Boolean includeSalary,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId,
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestAttribute(value = "requestStartTime", required = false) Long startTime) {
        
        System.out.println("GET /api/employees/" + id + "/advanced");
        System.out.println("  includeSalary: " + includeSalary);
        System.out.println("  X-Request-Id: " + requestId);
        System.out.println("  Authorization: " + (authorization != null ? "Present" : "Missing"));
        
        Employee employee = employees.get(id);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", employee.getId());
        response.put("name", employee.getName());
        response.put("email", employee.getEmail());
        response.put("department", employee.getDepartment());
        
        if (includeSalary) {
            response.put("salary", employee.getSalary());
        }
        
        if (requestId != null) {
            response.put("requestId", requestId);
        }
        
        if (startTime != null) {
            response.put("processingTime", System.currentTimeMillis() - startTime);
        }
        
        return ResponseEntity.ok(response);
    }
}


