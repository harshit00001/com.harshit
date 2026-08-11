package com.harshit.exception;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point so IntelliJ / {@code mvn spring-boot:run} can start the REST exception demos
 * under {@code com.harshit.exception.springboot}.
 */
@SpringBootApplication
public class ExceptionHandlingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExceptionHandlingApplication.class, args);
    }
}
