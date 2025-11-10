package com.harshit.springbeans.scopes;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * ============================================
 * SCOPE 2: PROTOTYPE
 * ============================================
 * 
 * NEW instance every time!
 * 
 * Characteristics:
 * - NEW instance created every time bean is requested
 * - Created on-demand (lazy initialization)
 * - Spring doesn't manage lifecycle after creation
 * - Use when bean has state that shouldn't be shared
 * - More memory usage (but better isolation)
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrototypeBean {
    
    private int id;
    private static int instanceCounter = 0;
    
    public PrototypeBean() {
        instanceCounter++;
        this.id = instanceCounter;
        System.out.println("=== PROTOTYPE BEAN CREATED #" + id + " ===");
        System.out.println("NEW instance created! Total instances: " + instanceCounter);
    }
    
    public int getId() {
        return id;
    }
    
    public void doSomething() {
        System.out.println("PrototypeBean instance #" + id + " doing something");
    }
}


