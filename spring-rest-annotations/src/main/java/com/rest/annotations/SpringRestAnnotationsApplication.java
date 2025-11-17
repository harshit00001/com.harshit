package com.rest.annotations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringRestAnnotationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRestAnnotationsApplication.class, args);
        System.out.println("\n=== Spring REST Annotations Demo Application Started ===");
        System.out.println("API Base URL: http://localhost:8080/api/employees");
        System.out.println("Swagger UI (if configured): http://localhost:8080/swagger-ui.html\n");
    }
}



