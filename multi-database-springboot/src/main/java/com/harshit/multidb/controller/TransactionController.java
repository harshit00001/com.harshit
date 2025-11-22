package com.harshit.multidb.controller;

import com.harshit.multidb.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Transaction Controller - Handles operations across both databases
 */
@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    /**
     * Create user and order in both databases
     * POST /api/transactions/user-and-order
     */
    @PostMapping("/user-and-order")
    public ResponseEntity<?> createUserAndOrder(@RequestBody Map<String, Object> request) {
        try {
            String username = request.get("username").toString();
            String email = request.get("email").toString();
            String fullName = request.get("fullName").toString();
            String productName = request.get("productName").toString();
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            Integer quantity = Integer.parseInt(request.get("quantity").toString());
            
            transactionService.createUserAndOrder(username, email, fullName, productName, amount, quantity);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "User and order created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Create user and order with rollback handling
     * POST /api/transactions/user-and-order-safe
     */
    @PostMapping("/user-and-order-safe")
    public ResponseEntity<?> createUserAndOrderSafe(@RequestBody Map<String, Object> request) {
        try {
            String username = request.get("username").toString();
            String email = request.get("email").toString();
            String fullName = request.get("fullName").toString();
            String productName = request.get("productName").toString();
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            Integer quantity = Integer.parseInt(request.get("quantity").toString());
            
            transactionService.createUserAndOrderWithRollback(username, email, fullName, productName, amount, quantity);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "User and order created successfully with rollback protection");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Get user with their orders
     * GET /api/transactions/user/{userId}/orders
     */
    @GetMapping("/user/{userId}/orders")
    public ResponseEntity<?> getUserWithOrders(@PathVariable Long userId) {
        try {
            var userOrderInfo = transactionService.getUserWithOrders(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("user", userOrderInfo.getUser());
            response.put("orders", userOrderInfo.getOrders());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}

