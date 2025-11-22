package com.harshit.multidb.secondary.repository;

import com.harshit.multidb.secondary.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Order Repository - Secondary Database
 * 
 * This repository is automatically configured to use the secondary database
 * because it's in the package specified in SecondaryDatabaseConfig.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * Find all orders by user ID
     */
    List<Order> findByUserId(Long userId);
    
    /**
     * Find orders by status
     */
    List<Order> findByStatus(String status);
    
    /**
     * Find orders by user ID and status
     */
    List<Order> findByUserIdAndStatus(Long userId, String status);
}

