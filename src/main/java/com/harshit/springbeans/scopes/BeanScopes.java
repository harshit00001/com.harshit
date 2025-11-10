package com.harshit.springbeans.scopes;

/**
 * ============================================
 * BEAN SCOPES - TYPES OF BEANS
 * ============================================
 * 
 * Scope determines how many instances Spring creates and how they're shared.
 * 
 * SCOPES (Most Important for Interviews):
 * 
 * 1. SINGLETON (Default):
 *    - ONE instance per Spring container
 *    - Shared by all requests
 *    - Created when container starts
 *    - Thread-safe if bean is stateless
 * 
 * 2. PROTOTYPE:
 *    - NEW instance every time bean is requested
 *    - Created on-demand (lazy)
 *    - NOT managed by Spring after creation
 *    - Use for stateful beans
 * 
 * 3. REQUEST (Web only):
 *    - ONE instance per HTTP request
 *    - Only in web applications
 * 
 * 4. SESSION (Web only):
 *    - ONE instance per HTTP session
 *    - Only in web applications
 * 
 * 5. APPLICATION (Web only):
 *    - ONE instance per ServletContext
 * 
 * 6. WEBSOCKET (Web only):
 *    - ONE instance per WebSocket session
 */

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
// SingletonBean and PrototypeBean moved to their own files:
// See SingletonBean.java and PrototypeBean.java

/**
 * ============================================
 * SCOPE 3: Using @Bean with Scope
 * ============================================
 * 
 * You can also specify scope in @Bean methods
 */
@Component
class ScopeExampleConfig {
    
    // Singleton scope (default)
    @org.springframework.context.annotation.Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public SharedResource sharedResource() {
        System.out.println("Creating SINGLETON SharedResource");
        return new SharedResource("Shared for all");
    }
    
    // Prototype scope
    @org.springframework.context.annotation.Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public UserSession userSession() {
        System.out.println("Creating PROTOTYPE UserSession");
        return new UserSession();
    }
}

// Supporting classes
class SharedResource {
    private String name;
    
    public SharedResource(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}

class UserSession {
    private String sessionId;
    
    public UserSession() {
        this.sessionId = "Session-" + System.currentTimeMillis();
    }
    
    public String getSessionId() {
        return sessionId;
    }
}

