package com.harshit.springboot.advanced;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * SPRING MVC REQUEST FLOW - End-to-End Explanation
 * 
 * Understanding the complete flow of a request through a Spring MVC application
 * is fundamental for Spring Boot interviews. This flow demonstrates how Spring
 * handles HTTP requests from the moment they arrive until a response is sent back
 * to the client.
 * 
 * The flow involves several components working together: DispatcherServlet acts
 * as the front controller, HandlerMapping determines which controller should handle
 * the request, the Controller processes the request and delegates to the Service
 * layer, the Service layer performs business logic and interacts with the Repository
 * layer, and finally the response is serialized and sent back to the client.
 */
@RestController
@RequestMapping("/api/users")
public class SpringMvcRequestFlow {
    
    /**
     * STEP-BY-STEP REQUEST FLOW DEMONSTRATION
     * 
     * This controller method demonstrates the complete request flow. When a client
     * sends a GET request to /api/users/101, here's what happens step by step:
     * 
     * Step 1: The HTTP request arrives at the DispatcherServlet, which is the front
     * controller in Spring MVC. The DispatcherServlet is like a receptionist that
     * receives all incoming requests and routes them to the appropriate handler.
     * 
     * Step 2: The DispatcherServlet consults the HandlerMapping to determine which
     * controller method should handle this request. The HandlerMapping looks at the
     * URL pattern, HTTP method, and other factors to find the matching @GetMapping
     * method. In this case, it matches the getUser method because the URL pattern
     * /api/users/{id} matches and the HTTP method is GET.
     * 
     * Step 3: Once the correct controller method is identified, Spring invokes it and
     * passes in any required parameters. The @PathVariable annotation tells Spring
     * to extract the id value from the URL and pass it as a parameter. Spring also
     * handles parameter binding, validation, and type conversion automatically.
     * 
     * Step 4: The controller method processes the request. In a well-designed
     * application, the controller doesn't contain business logic. Instead, it delegates
     * to the service layer, which acts like the operations team that knows how to
     * fetch and process data. The controller's job is to handle HTTP concerns like
     * request parsing and response formatting.
     * 
     * Step 5: The service layer performs the business logic. It might validate the
     * request, apply business rules, and coordinate with other services. In this
     * example, the service calls the repository to fetch data from the database.
     * 
     * Step 6: The repository layer interacts with the database using JPA or Hibernate.
     * It translates the method call into a SQL query, executes it, and maps the
     * results back to Java objects. The repository abstracts away the database
     * implementation details.
     * 
     * Step 7: The data flows back through the layers. The repository returns a domain
     * object, the service might transform it into a DTO (Data Transfer Object), and
     * the controller wraps it in a ResponseEntity.
     * 
     * Step 8: Spring uses HttpMessageConverters (like Jackson for JSON) to serialize
     * the response object into the appropriate format (JSON, XML, etc.). The
     * DispatcherServlet then sends the serialized response back to the client.
     * 
     * This entire flow happens automatically thanks to Spring's configuration, but
     * understanding each step is crucial for debugging, performance optimization,
     * and customizing the behavior when needed.
     */
    private final UserService userService;
    
    public SpringMvcRequestFlow(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * EXAMPLE: GET Request Flow
     * 
     * This method demonstrates the complete flow for a GET request. The flow is:
     * Client → DispatcherServlet → HandlerMapping → Controller → Service → Repository → Database
     * Then back: Database → Repository → Service → Controller → HttpMessageConverter → DispatcherServlet → Client
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        // Interview Point: @PathVariable extracts id from URL
        // Controller delegates to service layer
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    /**
     * EXAMPLE: POST Request Flow
     * 
     * This method demonstrates the flow for a POST request with a request body.
     * The @RequestBody annotation tells Spring to deserialize the JSON request body
     * into a UserDTO object using HttpMessageConverters.
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        // Interview Point: @RequestBody deserializes JSON to UserDTO
        // Validation happens automatically if @Valid is used
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(201).body(createdUser);
    }
}

/**
 * Service layer - handles business logic
 */
class UserService {
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public UserDTO getUserById(Long id) {
        // Interview Point: Service layer performs business logic
        // Might include validation, transformation, coordination
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        
        // Convert domain object to DTO
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }
    
    public UserDTO createUser(UserDTO userDTO) {
        // Business logic for creating user
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        
        User savedUser = userRepository.save(user);
        return new UserDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }
}

/**
 * Repository layer - handles data access
 */
interface UserRepository {
    User findById(Long id);
    User save(User user);
}

/**
 * Domain and DTO classes
 */
class User {
    private Long id;
    private String name;
    private String email;
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

class UserDTO {
    private Long id;
    private String name;
    private String email;
    
    public UserDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

