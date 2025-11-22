package com.harshit.multidb.service;

import com.harshit.multidb.secondary.entity.Order;
import com.harshit.multidb.secondary.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Order Service - Handles operations on SECONDARY database
 * 
 * Uses @Transactional with secondary transaction manager
 * All operations here go to the secondary database
 */
@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    /**
     * Create a new order
     * Transaction is managed by secondary TransactionManager
     */
    @Transactional(transactionManager = "secondaryTransactionManager")
    public Order createOrder(Long userId, String productName, BigDecimal amount, Integer quantity) {
        Order order = new Order(userId, productName, amount, quantity);
        return orderRepository.save(order);
    }
    
    /**
     * Get all orders
     */
    @Transactional(readOnly = true, transactionManager = "secondaryTransactionManager")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    /**
     * Get order by ID
     */
    @Transactional(readOnly = true, transactionManager = "secondaryTransactionManager")
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
    
    /**
     * Get orders by user ID
     */
    @Transactional(readOnly = true, transactionManager = "secondaryTransactionManager")
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
    
    /**
     * Update order status
     */
    @Transactional(transactionManager = "secondaryTransactionManager")
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setStatus(status);
        return orderRepository.save(order);
    }
    
    /**
     * Delete order by ID
     */
    @Transactional(transactionManager = "secondaryTransactionManager")
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}

