package com.harshit.springboot.advanced;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * RESTTEMPLATE vs WEBCLIENT - Complete Comparison
 * 
 * This is a common interview question as Spring Boot applications transition from
 * RestTemplate to WebClient. Understanding the differences, advantages, and when
 * to use each is crucial for modern Spring Boot development.
 * 
 * RestTemplate is great for simple, synchronous calls, but it doesn't scale well
 * under high load because it uses one thread per request. When you make an HTTP
 * call with RestTemplate, the thread blocks until the response is received,
 * which means that thread cannot be used for anything else during that time. In
 * high-concurrency scenarios, this can lead to thread pool exhaustion and poor
 * performance.
 * 
 * WebClient, being non-blocking and reactive, is ideal for microservices and
 * event-driven systems. It uses an event-loop model where a small number of
 * threads can handle many concurrent requests. Instead of blocking while waiting
 * for a response, the thread can move on to handle other requests, and when the
 * response arrives, it's processed by the same thread pool. This makes WebClient
 * much more efficient for applications that need to make many HTTP calls.
 * 
 * WebClient also integrates seamlessly with Spring WebFlux and supports streaming
 * and backpressure, making it the preferred choice for modern applications. It
 * supports Server-Sent Events, WebSockets, and can handle large responses
 * efficiently by processing data as it arrives.
 */
@Service
public class RestTemplateVsWebClient {
    
    private final RestTemplate restTemplate;
    private final WebClient webClient;
    
    public RestTemplateVsWebClient() {
        this.restTemplate = new RestTemplate();
        this.webClient = WebClient.builder()
                .baseUrl("https://api.example.com")
                .build();
    }
    
    /**
     * RESTTEMPLATE EXAMPLE - Synchronous Blocking Call
     * 
     * This method demonstrates how RestTemplate works. When you call getForObject,
     * the current thread blocks until the HTTP response is received. This means
     * the thread cannot do anything else during this time, which is inefficient
     * when you have many concurrent requests.
     */
    public String fetchDataWithRestTemplate(String url) {
        // Interview Point: This call blocks the thread until response is received
        // Thread cannot be used for anything else during this time
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }
    
    /**
     * WEBCLIENT EXAMPLE - Asynchronous Non-Blocking Call
     * 
     * This method demonstrates how WebClient works. When you call get() and retrieve(),
     * it returns immediately with a Mono. The actual HTTP call happens asynchronously,
     * and the thread is free to handle other requests. When the response arrives,
     * it's processed by the reactive framework.
     */
    public Mono<String> fetchDataWithWebClient(String url) {
        // Interview Point: This call is non-blocking
        // Thread is free to handle other requests immediately
        // Response is processed asynchronously when it arrives
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class);
    }
    
    /**
     * COMPARISON DEMONSTRATION
     * 
     * This method shows the performance difference between RestTemplate and WebClient.
     * With RestTemplate, making 100 requests would require 100 threads (one per request).
     * With WebClient, the same 100 requests can be handled by a small number of threads
     * because threads are not blocked waiting for responses.
     */
    public void demonstrateDifference() {
        System.out.println("=== RESTTEMPLATE vs WEBCLIENT ===");
        
        // RestTemplate: Blocking - one thread per request
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            // Each call blocks a thread
            // In high concurrency, this can exhaust thread pool
        }
        long restTemplateTime = System.currentTimeMillis() - start;
        
        // WebClient: Non-blocking - few threads handle many requests
        start = System.currentTimeMillis();
        Mono<String>[] requests = new Mono[10];
        for (int i = 0; i < 10; i++) {
            requests[i] = webClient.get()
                    .uri("/data")
                    .retrieve()
                    .bodyToMono(String.class);
        }
        // All requests can be handled concurrently with fewer threads
        Mono.when(requests).block();  // Wait for all to complete
        long webClientTime = System.currentTimeMillis() - start;
        
        System.out.println("RestTemplate approach: " + restTemplateTime + " ms");
        System.out.println("WebClient approach: " + webClientTime + " ms");
        System.out.println("WebClient is more efficient for concurrent requests");
    }
}

