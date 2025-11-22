package com.harshit.springboot.advanced;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * WEBCLIENT - Modern Non-Blocking HTTP Client
 * 
 * Spring WebClient is a non-blocking, reactive HTTP client introduced in Spring 5
 * as part of the Spring WebFlux module. It was designed to replace RestTemplate
 * for modern, asynchronous web communication. WebClient solves several problems that
 * RestTemplate had, particularly around scalability and performance in high-load
 * scenarios.
 * 
 * WebClient enables asynchronous, non-blocking calls, which means that instead of
 * blocking a thread while waiting for an HTTP response, it uses an event-loop model
 * that can handle many concurrent requests with fewer threads. This makes it much
 * more efficient for applications that need to make many HTTP calls, especially in
 * microservices architectures where services communicate frequently.
 * 
 * WebClient also supports backpressure and streaming, which means it can handle
 * large responses efficiently by processing data as it arrives rather than loading
 * everything into memory. It integrates seamlessly with Spring WebFlux for reactive
 * pipelines, allowing you to chain multiple asynchronous operations together in a
 * functional programming style.
 */
@Service
public class WebClientExample {
    
    private final WebClient webClient;
    
    /**
     * CONSTRUCTOR - Creating WebClient Instance
     * 
     * WebClient is created using a builder pattern. You can configure the base URL,
     * default headers, timeouts, and other settings. Once created, a WebClient
     * instance is thread-safe and can be reused for multiple requests.
     */
    public WebClientExample() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.example.com")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
    
    /**
     * GET REQUEST EXAMPLE
     * 
     * This method demonstrates how to make a GET request using WebClient. The
     * get() method returns a request builder, and you can chain methods to
     * configure the request. The retrieve() method executes the request and
     * returns a Mono or Flux depending on whether you expect a single response
     * or a stream of responses.
     */
    public Mono<String> fetchUserData(Long userId) {
        // Interview Point: Non-blocking GET request
        // Returns Mono<String> - reactive type that represents a single value
        return webClient.get()
                .uri("/users/{id}", userId)  // Path variable
                .retrieve()  // Execute request
                .bodyToMono(String.class);  // Convert response to String
    }
    
    /**
     * POST REQUEST EXAMPLE
     * 
     * This method demonstrates how to make a POST request with a request body.
     * The body() method is used to set the request body, and you can specify
     * the content type. WebClient automatically handles serialization of objects
     * to JSON using HttpMessageConverters.
     */
    public Mono<String> createUser(User user) {
        // Interview Point: Non-blocking POST request with body
        return webClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)  // Automatically serializes to JSON
                .retrieve()
                .bodyToMono(String.class);
    }
    
    /**
     * STREAMING EXAMPLE
     * 
     * WebClient supports Server-Sent Events (SSE) and streaming responses.
     * Instead of waiting for the entire response, you can process data as it
     * arrives. This is useful for real-time updates or large responses.
     */
    public Flux<String> streamEvents() {
        // Interview Point: Streaming response - processes data as it arrives
        return webClient.get()
                .uri("/events")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)  // Flux for stream of data
                .delayElements(Duration.ofSeconds(1));  // Process each element with delay
    }
    
    /**
     * ERROR HANDLING EXAMPLE
     * 
     * WebClient provides reactive error handling using onErrorResume, onErrorReturn,
     * and other reactive operators. This allows you to handle errors in a functional
     * programming style without try-catch blocks.
     */
    public Mono<String> fetchWithErrorHandling(Long userId) {
        return webClient.get()
                .uri("/users/{id}", userId)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(error -> {
                    // Interview Point: Handle errors reactively
                    System.out.println("Error occurred: " + error.getMessage());
                    return Mono.just("Default user data");  // Return fallback
                });
    }
    
    /**
     * CHAINING MULTIPLE REQUESTS
     * 
     * One of the powerful features of WebClient is the ability to chain multiple
     * asynchronous operations together. You can use flatMap to make a second request
     * based on the result of the first request, all in a non-blocking way.
     */
    public Mono<String> fetchUserAndOrders(Long userId) {
        // Interview Point: Chain multiple async operations
        return webClient.get()
                .uri("/users/{id}", userId)
                .retrieve()
                .bodyToMono(User.class)
                .flatMap(user -> {
                    // Make second request based on first response
                    return webClient.get()
                            .uri("/users/{id}/orders", user.getId())
                            .retrieve()
                            .bodyToMono(String.class);
                });
    }
}

/**
 * Helper class for demonstration
 */
class User {
    private Long id;
    private String name;
    private String email;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}

