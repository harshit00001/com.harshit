package com.harshit.springboot.basics;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * EXCEPTION HANDLING IN SPRING BOOT
 * 
 * Spring Boot provides several ways to handle exceptions in REST APIs. Understanding
 * @ExceptionHandler and @ControllerAdvice is crucial for building robust applications
 * and is a common interview topic.
 */
@RestController
@RequestMapping("/api/users")
public class ExceptionHandlingExample {
    
    /**
     * METHOD-LEVEL EXCEPTION HANDLING
     * 
     * To handle exceptions that occur in a controller class, you can add a method
     * annotated with @ExceptionHandler in the same controller. This method will handle
     * exceptions thrown by any method in this controller.
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> getUser(@PathVariable Long id) {
        if (id < 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        return ResponseEntity.ok("User " + id);
    }
    
    /**
     * @ExceptionHandler IN CONTROLLER
     * 
     * This method handles IllegalArgumentException thrown by any method in this
     * controller. It's scoped to this controller only.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        // Interview Point: Handles exceptions in this controller only
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

/**
 * GLOBAL EXCEPTION HANDLING WITH @ControllerAdvice
 * 
 * To handle all exceptions that occur in REST API across all controllers, you create
 * a class annotated with @ControllerAdvice. This provides centralized exception
 * handling for the entire application.
 */
@ControllerAdvice
class GlobalExceptionHandler {
    
    /**
     * GLOBAL EXCEPTION HANDLER
     * 
     * This method handles exceptions across all controllers. It's the recommended
     * approach for production applications as it provides centralized exception handling
     * and consistent error responses.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
        // Interview Point: Handles exceptions from all controllers
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            e.getMessage(),
            System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            e.getMessage(),
            System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}

/**
 * Error response DTO
 */
class ErrorResponse {
    private int status;
    private String message;
    private long timestamp;
    
    public ErrorResponse(int status, String message, long timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }
    
    // Getters and setters
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}

