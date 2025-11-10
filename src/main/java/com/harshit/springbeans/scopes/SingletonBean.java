package com.harshit.springbeans.scopes;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * ============================================
 * SCOPE 1: SINGLETON (DEFAULT)
 * ============================================
 * 
 * This is the DEFAULT scope - you don't need to specify it
 * 
 * Characteristics:
 * - Only ONE instance exists in entire Spring container
 * - Created when container starts (eager initialization)
 * - Same instance returned every time you request it
 * - Memory efficient
 * - Must be thread-safe for concurrent access
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SingletonBean {
    
    private int counter = 0;
    
    public SingletonBean() {
        System.out.println("=== SINGLETON BEAN CREATED ===");
        System.out.println("Singleton bean created once when container starts");
        System.out.println("This instance will be reused for all requests");
    }
    
    public void increment() {
        counter++;
        System.out.println("Singleton counter: " + counter);
    }
    
    public int getCounter() {
        return counter;
    }
}


