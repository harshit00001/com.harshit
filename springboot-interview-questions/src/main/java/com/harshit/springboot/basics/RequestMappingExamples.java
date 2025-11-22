package com.harshit.springboot.basics;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @RequestMapping EXAMPLES - Understanding Request Mapping
 * 
 * @RequestMapping is a fundamental annotation in Spring MVC that maps HTTP requests
 * to controller methods. Understanding how it works, especially when used without
 * specifying a path, is important for Spring Boot interviews.
 */
@RestController
@RequestMapping("/task-service/v1")
public class RequestMappingExamples {
    
    /**
     * @RequestMapping WITHOUT PATH
     * 
     * When you use @RequestMapping on a method without specifying a path, the method
     * handles requests to the same path as the class-level @RequestMapping. In this
     * case, the class-level path is "/task-service/v1", so this method handles
     * requests to "/task-service/v1".
     * 
     * This can lead to conflicts if multiple methods are mapped to the same path
     * without different HTTP methods. Spring will throw an error at startup if
     * two methods have the same path and HTTP method.
     */
    @RequestMapping  // No path specified - uses class-level path
    public String showDetails() {
        // Interview Point: Handles requests to /task-service/v1
        // If multiple methods have same path, Spring throws error at startup
        return "Online Task Manager";
    }
    
    /**
     * @GetMapping EXAMPLE
     * 
     * @GetMapping is a shortcut for @RequestMapping(method = RequestMethod.GET).
     * It's more concise and readable. This method handles GET requests to
     * "/task-service/v1/tasks".
     */
    @GetMapping("/tasks")
    public ResponseEntity<String> getTasks() {
        return ResponseEntity.ok("List of tasks");
    }
    
    /**
     * @PostMapping EXAMPLE
     * 
     * @PostMapping handles POST requests. If you send a POST request to a GET mapping,
     * Spring throws MethodNotAllowedException because the HTTP method doesn't match.
     */
    @PostMapping("/tasks")
    public ResponseEntity<String> createTask(@RequestBody String task) {
        return ResponseEntity.status(201).body("Task created");
    }
    
    /**
     * DUPLICATE PATH EXAMPLE - This will cause error
     * 
     * If two methods have the same path and HTTP method, Spring throws an error
     * at startup. This is a common mistake that needs to be avoided.
     */
    @GetMapping("/greet")
    public String sayHello() {
        return "Hello";
    }
    
    // This would cause error - duplicate path and method
    // @GetMapping("/greet")
    // public String welcome() {
    //     return "welcome";
    // }
    
    /**
     * NULL RETURN VALUE HANDLING
     * 
     * If a @GetMapping method returns null, Spring sends 204 No Content response.
     * If an exception is thrown, Spring returns 500 Internal Server Error.
     */
    @GetMapping("/empty")
    public String getEmpty() {
        return null;  // Returns 204 No Content
    }
}

