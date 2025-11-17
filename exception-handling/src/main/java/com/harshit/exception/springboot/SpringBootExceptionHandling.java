package com.harshit.exception.springboot;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * SPRING BOOT EXCEPTION HANDLING - Interview Explanation:
 * 
 * Spring Boot provides several ways to handle exceptions:
 * 
 * 1. @ExceptionHandler: Handle exceptions in a controller
 * 2. @ControllerAdvice: Global exception handling
 * 3. @ResponseStatus: Set HTTP status for exceptions
 * 4. ResponseEntity: Return custom error responses
 * 
 * Simple Explanation:
 * - Instead of try-catch everywhere, handle exceptions globally
 * - Return proper HTTP status codes and error messages
 * - Better API design and user experience
 */
@RestController
@RequestMapping("/api/users")
public class SpringBootExceptionHandling {

    // In-memory storage for demo
    private Map<Long, User> users = new HashMap<>();
    private Long nextId = 1L;

    /**
     * Interview Point: @ExceptionHandler in Controller
     * Handles exceptions thrown by methods in this controller
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e) {
        ErrorResponse error = new ErrorResponse(
            "USER_NOT_FOUND",
            e.getMessage(),
            System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Interview Point: GET endpoint that might throw exception
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = users.get(id);
        if (user == null) {
            // Interview Point: Throw custom exception
            throw new UserNotFoundException("User with ID " + id + " not found");
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Interview Point: POST endpoint with validation
     */
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        user.setId(nextId++);
        users.put(user.getId(), user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Interview Point: PUT endpoint
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found");
        }
        user.setId(id);
        users.put(id, user);
        return ResponseEntity.ok(user);
    }

    /**
     * Interview Point: DELETE endpoint
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found");
        }
        users.remove(id);
        return ResponseEntity.noContent().build();
    }
}

// ==================== GLOBAL EXCEPTION HANDLER ====================

/**
 * Interview Point: @ControllerAdvice
 * Handles exceptions globally for all controllers
 * 
 * Technical: Intercepts exceptions from any controller
 * Simple: One place to handle all errors in your application
 */
@ControllerAdvice
class GlobalExceptionHandler {

    /**
     * Interview Point: Handle specific exception globally
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e) {
        ErrorResponse error = new ErrorResponse(
            "USER_NOT_FOUND",
            e.getMessage(),
            System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Interview Point: Handle validation exceptions
     * @Valid triggers this when validation fails
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            javax.validation.ConstraintViolationException e) {
        ErrorResponse error = new ErrorResponse(
            "VALIDATION_ERROR",
            "Validation failed: " + e.getMessage(),
            System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Interview Point: Handle method argument exceptions
     */
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            org.springframework.web.bind.MethodArgumentNotValidException e) {
        ErrorResponse error = new ErrorResponse(
            "VALIDATION_ERROR",
            "Invalid request data: " + e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
            System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Interview Point: Handle all other exceptions (catch-all)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        ErrorResponse error = new ErrorResponse(
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred: " + e.getMessage(),
            System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

// ==================== CUSTOM EXCEPTION ====================

/**
 * Interview Point: Custom Exception with @ResponseStatus
 * Automatically sets HTTP status when thrown
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

// ==================== ERROR RESPONSE DTO ====================

/**
 * Interview Point: Standard Error Response Format
 * Consistent error structure for API consumers
 */
class ErrorResponse {
    private String errorCode;
    private String message;
    private long timestamp;

    public ErrorResponse(String errorCode, String message, long timestamp) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters
    public String getErrorCode() { return errorCode; }
    public String getMessage() { return message; }
    public long getTimestamp() { return timestamp; }
}

// ==================== USER MODEL ====================

class User {
    private Long id;
    
    @NotNull(message = "Name is required")
    private String name;
    
    @NotNull(message = "Email is required")
    private String email;
    
    public User() {}
    
    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

/**
 * INTERVIEW SUMMARY: Spring Boot Exception Handling
 * 
 * Key Annotations:
 * 1. @ExceptionHandler: Handle exception in controller
 * 2. @ControllerAdvice: Global exception handling
 * 3. @ResponseStatus: Set HTTP status on exception
 * 4. @Valid: Enable validation
 * 
 * Best Practices:
 * - Use @ControllerAdvice for global handling
 * - Return consistent error response format
 * - Use appropriate HTTP status codes
 * - Provide meaningful error messages
 * - Log exceptions for debugging
 * - Don't expose internal details to clients
 * 
 * HTTP Status Codes:
 * - 400: Bad Request (validation errors)
 * - 404: Not Found (resource doesn't exist)
 * - 500: Internal Server Error (unexpected errors)
 * - 401: Unauthorized (authentication required)
 * - 403: Forbidden (not authorized)
 */

