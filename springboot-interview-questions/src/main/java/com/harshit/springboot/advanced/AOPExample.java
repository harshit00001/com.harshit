package com.harshit.springboot.advanced;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * AOP (ASPECT-ORIENTED PROGRAMMING) - Complete Example
 * 
 * AOP stands for Aspect-Oriented Programming. It's a way to separate cross-cutting
 * concerns from the main business logic. Cross-cutting concerns are functionalities
 * that affect multiple parts of the application but aren't part of the core logic.
 * Examples include logging, security checks, performance monitoring, transaction
 * management, and exception handling.
 * 
 * Instead of writing logging code in every service method, you can write it once
 * in an aspect and apply it wherever needed. This keeps the code clean and focused
 * on business logic, while cross-cutting concerns are handled separately. This
 * separation makes the code more maintainable, testable, and follows the Single
 * Responsibility Principle.
 * 
 * Spring AOP uses proxies to implement aspects. When you call a method on a Spring
 * bean, the call goes through a proxy that can intercept the method call and apply
 * the aspect logic before, after, or around the method execution. If the class
 * implements an interface, Spring uses JDK dynamic proxy. Otherwise, it uses CGLIB
 * to create a subclass proxy.
 */
@Aspect
@Component
public class AOPExample {
    
    /**
     * POINTCUT DEFINITION
     * 
     * A pointcut is an expression that selects which methods should have the aspect
     * applied. This pointcut selects all methods in the ServicePackage that have
     * any return type, any method name, and any parameters. Pointcuts use AspectJ
     * expression language to define matching rules.
     */
    @Pointcut("execution(* com.harshit.springboot.advanced.ServicePackage.*.*(..))")
    public void serviceMethods() {
        // Pointcut definition - no implementation needed
    }
    
    /**
     * BEFORE ADVICE
     * 
     * Before advice executes before the target method is called. This is useful for
     * logging method entry, performing security checks, or validating parameters.
     * The JoinPoint parameter gives you access to information about the method being
     * called, such as method name, arguments, and target object.
     */
    @Before("serviceMethods()")
    public void beforeMethod(JoinPoint joinPoint) {
        // Interview Point: Executes before target method
        // Can access method information via JoinPoint
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        System.out.println("Before executing method: " + methodName);
        System.out.println("Arguments: " + java.util.Arrays.toString(args));
    }
    
    /**
     * AFTER ADVICE
     * 
     * After advice executes after the target method completes, regardless of whether
     * it returns normally or throws an exception. This is useful for cleanup operations
     * or logging that should happen regardless of the outcome.
     */
    @After("serviceMethods()")
    public void afterMethod(JoinPoint joinPoint) {
        // Interview Point: Executes after method (even if exception thrown)
        System.out.println("After executing method: " + joinPoint.getSignature().getName());
    }
    
    /**
     * AFTER RETURNING ADVICE
     * 
     * After returning advice executes only when the method returns normally (without
     * throwing an exception). You can access the return value and perform operations
     * based on it, such as logging the result or transforming the return value.
     */
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        // Interview Point: Executes only on successful return
        // Can access return value
        System.out.println("Method returned: " + result);
    }
    
    /**
     * AFTER THROWING ADVICE
     * 
     * After throwing advice executes only when the method throws an exception. This
     * is useful for logging exceptions, sending alerts, or handling exceptions in a
     * centralized way. You can access the exception object and perform recovery or
     * logging operations.
     */
    @AfterThrowing(pointcut = "serviceMethods()", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        // Interview Point: Executes only when exception is thrown
        // Can access exception object
        System.out.println("Exception in method " + joinPoint.getSignature().getName() + 
            ": " + exception.getMessage());
    }
    
    /**
     * AROUND ADVICE
     * 
     * Around advice is the most powerful type of advice. It can execute code before
     * and after the method, and it can control whether the method is actually called.
     * It can also modify the return value or handle exceptions. This is useful for
     * performance monitoring, transaction management, or caching.
     */
    @Around("serviceMethods()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        // Interview Point: Most powerful - can control method execution
        long startTime = System.currentTimeMillis();
        
        try {
            // Can decide whether to proceed with method execution
            Object result = joinPoint.proceed();  // Calls the actual method
            
            long executionTime = System.currentTimeMillis() - startTime;
            System.out.println("Method executed in " + executionTime + " ms");
            
            return result;  // Can modify return value
        } catch (Exception e) {
            // Can handle exceptions
            System.out.println("Exception handled in aspect: " + e.getMessage());
            throw e;  // Or return default value
        }
    }
}

/**
 * Example service class for AOP demonstration
 */
@Component
class ServicePackage {
    public String processData(String data) {
        System.out.println("Processing data: " + data);
        return "Processed: " + data;
    }
    
    public void performOperation() {
        System.out.println("Performing operation");
    }
}

