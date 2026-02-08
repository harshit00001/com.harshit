package com.harshit.hibernate.basic.controller;

import com.harshit.hibernate.basic.entity.User;
import com.harshit.hibernate.basic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * User REST Controller - Spring Boot REST API Example
 * 
 * ====================================================================================
 * INTERVIEW QUESTION: What is @RestController and how does it differ from @Controller?
 * ====================================================================================
 * 
 * DETAILED ANSWER:
 * 
 * @RestController is a specialized version of @Controller that combines:
 * - @Controller: Marks class as Spring MVC controller
 * - @ResponseBody: Automatically serializes return values to HTTP response body
 * 
 * DIFFERENCES:
 * 
 * @Controller:
 * - Used for traditional Spring MVC (returns view names)
 * - Returns String = view name (e.g., "users", "index")
 * - Used with @ResponseBody for REST APIs
 * - Example: return "users" -> renders users.html template
 * 
 * @RestController:
 * - Used for REST APIs (returns JSON/XML)
 * - Automatically converts return values to JSON/XML
 * - No need for @ResponseBody on each method
 * - Example: return user -> JSON: {"id": 1, "username": "john"}
 * 
 * HOW IT WORKS:
 * 
 * 1. REQUEST MAPPING:
 *    - @RequestMapping maps HTTP requests to controller methods
 *    - Can specify URL path, HTTP method, headers, etc.
 *    - Example: @RequestMapping("/api/users") maps to /api/users
 * 
 * 2. HTTP METHOD MAPPINGS:
 *    - @GetMapping: Handles GET requests
 *    - @PostMapping: Handles POST requests
 *    - @PutMapping: Handles PUT requests
 *    - @DeleteMapping: Handles DELETE requests
 *    - @PatchMapping: Handles PATCH requests
 * 
 * 3. REQUEST PARAMETERS:
 *    - @PathVariable: Extracts path variables (/users/{id})
 *    - @RequestParam: Extracts query parameters (?name=value)
 *    - @RequestBody: Extracts JSON/XML from request body
 *    - @RequestHeader: Extracts HTTP headers
 * 
 * 4. RESPONSE:
 *    - Return value is automatically serialized to JSON (using Jackson)
 *    - ResponseEntity provides more control (status code, headers)
 *    - @ResponseStatus sets default HTTP status code
 * 
 * ====================================================================================
 * REST API BEST PRACTICES:
 * ====================================================================================
 * 
 * 1. URL DESIGN:
 *    - Use nouns, not verbs: /users not /getUsers
 *    - Use plural: /users not /user
 *    - Use hierarchical: /users/{id}/orders
 * 
 * 2. HTTP METHODS:
 *    - GET: Retrieve data (idempotent, safe)
 *    - POST: Create new resource (not idempotent)
 *    - PUT: Update entire resource (idempotent)
 *    - PATCH: Partial update (idempotent)
 *    - DELETE: Delete resource (idempotent)
 * 
 * 3. HTTP STATUS CODES:
 *    - 200 OK: Success
 *    - 201 Created: Resource created
 *    - 204 No Content: Success, no response body
 *    - 400 Bad Request: Invalid input
 *    - 404 Not Found: Resource not found
 *    - 500 Internal Server Error: Server error
 * 
 * 4. REQUEST/RESPONSE BODIES:
 *    - Use JSON for data exchange
 *    - Validate input with @Valid and Bean Validation
 *    - Use DTOs (Data Transfer Objects) instead of entities
 * 
 * ====================================================================================
 * REAL-WORLD SCENARIO:
 * ====================================================================================
 * 
 * This controller provides REST API for user management:
 * - POST /api/users - Create user
 * - GET /api/users - List all users
 * - GET /api/users/{id} - Get user by ID
 * - PUT /api/users/{id} - Update user
 * - DELETE /api/users/{id} - Delete user
 * 
 * Clients can use these endpoints from:
 * - Web applications (JavaScript/Angular/React)
 * - Mobile applications (Android/iOS)
 * - Other microservices
 * - Postman/curl for testing
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    /**
     * User Service - Injected by Spring
     * 
     * Controller delegates business logic to Service layer
     * Controller only handles HTTP concerns (request/response)
     */
    private final UserService userService;
    
    /**
     * Constructor Injection
     * 
     * Spring automatically injects UserService
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Create User - POST /api/users
     * 
     * HTTP Method: POST
     * Request Body: JSON with user data
     * Response: 201 Created with created user
     * 
     * Example Request:
     * POST /api/users
     * Content-Type: application/json
     * {
     *   "username": "john_doe",
     *   "email": "john@example.com",
     *   "firstName": "John",
     *   "lastName": "Doe",
     *   "age": 30
     * }
     * 
     * Example Response:
     * HTTP 201 Created
     * {
     *   "id": 1,
     *   "username": "john_doe",
     *   "email": "john@example.com",
     *   ...
     * }
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreateRequest request) {
        logger.info("POST /api/users - Creating user: {}", request.getUsername());
        
        try {
            User user = userService.createUser(
                    request.getUsername(),
                    request.getEmail(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getAge()
            );
            
            // 201 Created - Resource successfully created
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
            
        } catch (IllegalArgumentException e) {
            // 400 Bad Request - Invalid input
            logger.error("Error creating user: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get All Users - GET /api/users
     * 
     * HTTP Method: GET
     * Response: 200 OK with list of users
     * 
     * Example Response:
     * HTTP 200 OK
     * [
     *   {"id": 1, "username": "john_doe", ...},
     *   {"id": 2, "username": "jane_smith", ...}
     * ]
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("GET /api/users - Getting all users");
        
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get User by ID - GET /api/users/{id}
     * 
     * HTTP Method: GET
     * Path Variable: id (user ID)
     * Response: 200 OK with user, or 404 Not Found
     * 
     * Example Request:
     * GET /api/users/1
     * 
     * Example Response:
     * HTTP 200 OK
     * {"id": 1, "username": "john_doe", ...}
     * 
     * Or if not found:
     * HTTP 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.info("GET /api/users/{} - Getting user by ID", id);
        
        Optional<User> user = userService.findUserById(id);
        
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            // 404 Not Found - Resource doesn't exist
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get User by Username - GET /api/users/username/{username}
     * 
     * Example Request:
     * GET /api/users/username/john_doe
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        logger.info("GET /api/users/username/{} - Getting user by username", username);
        
        Optional<User> user = userService.findUserByUsername(username);
        
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get Active Users - GET /api/users/active
     * 
     * Returns only users with ACTIVE status
     */
    @GetMapping("/active")
    public ResponseEntity<List<User>> getActiveUsers() {
        logger.info("GET /api/users/active - Getting active users");
        
        List<User> activeUsers = userService.findActiveUsers();
        return ResponseEntity.ok(activeUsers);
    }
    
    /**
     * Update User - PUT /api/users/{id}
     * 
     * HTTP Method: PUT
     * Path Variable: id (user ID)
     * Request Body: JSON with updated user data
     * Response: 200 OK with updated user, or 404 Not Found
     * 
     * Example Request:
     * PUT /api/users/1
     * {
     *   "firstName": "John Updated",
     *   "lastName": "Doe Updated",
     *   "age": 31
     * }
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request) {
        logger.info("PUT /api/users/{} - Updating user", id);
        
        try {
            User updatedUser = userService.updateUser(
                    id,
                    request.getFirstName(),
                    request.getLastName(),
                    request.getAge()
            );
            
            return ResponseEntity.ok(updatedUser);
            
        } catch (IllegalArgumentException e) {
            logger.error("Error updating user: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Deactivate User - PUT /api/users/{id}/deactivate
     * 
     * Soft delete - sets status to INACTIVE
     */
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        logger.info("PUT /api/users/{}/deactivate - Deactivating user", id);
        
        try {
            userService.deactivateUser(id);
            // 204 No Content - Success, no response body
            return ResponseEntity.noContent().build();
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Delete User - DELETE /api/users/{id}
     * 
     * HTTP Method: DELETE
     * Path Variable: id (user ID)
     * Response: 204 No Content, or 404 Not Found
     * 
     * Example Request:
     * DELETE /api/users/1
     * 
     * Example Response:
     * HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("DELETE /api/users/{} - Deleting user", id);
        
        try {
            userService.deleteUser(id);
            // 204 No Content - Success, no response body
            return ResponseEntity.noContent().build();
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get User Statistics - GET /api/users/stats
     * 
     * Returns statistics about users
     */
    @GetMapping("/stats")
    public ResponseEntity<UserStats> getUserStats() {
        logger.info("GET /api/users/stats - Getting user statistics");
        
        long activeUserCount = userService.getActiveUserCount();
        long totalUserCount = userService.findAllUsers().size();
        
        UserStats stats = new UserStats();
        stats.setTotalUsers(totalUserCount);
        stats.setActiveUsers(activeUserCount);
        stats.setInactiveUsers(totalUserCount - activeUserCount);
        
        return ResponseEntity.ok(stats);
    }
    
    /**
     * DTO (Data Transfer Object) for creating users
     * 
     * Why use DTOs instead of Entity?
     * - Separates API contract from database structure
     * - Can have different validation rules
     * - Prevents exposing internal entity structure
     * - Can combine data from multiple entities
     */
    public static class UserCreateRequest {
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private Integer age;
        
        // Getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
    }
    
    /**
     * DTO for updating users
     */
    public static class UserUpdateRequest {
        private String firstName;
        private String lastName;
        private Integer age;
        
        // Getters and setters
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
    }
    
    /**
     * DTO for user statistics
     */
    public static class UserStats {
        private long totalUsers;
        private long activeUsers;
        private long inactiveUsers;
        
        // Getters and setters
        public long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
        
        public long getActiveUsers() { return activeUsers; }
        public void setActiveUsers(long activeUsers) { this.activeUsers = activeUsers; }
        
        public long getInactiveUsers() { return inactiveUsers; }
        public void setInactiveUsers(long inactiveUsers) { this.inactiveUsers = inactiveUsers; }
    }
}

