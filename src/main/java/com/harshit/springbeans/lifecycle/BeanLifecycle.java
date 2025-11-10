package com.harshit.springbeans.lifecycle;

/**
 * ============================================
 * BEAN LIFECYCLE - STEP BY STEP
 * ============================================
 * 
 * Bean lifecycle is the process from creation to destruction.
 * 
 * LIFECYCLE STEPS:
 * 
 * 1. INSTANTIATION: Spring creates the object (constructor called)
 * 2. POPULATE PROPERTIES: Spring injects dependencies
 * 3. BeanNameAware: Sets bean name (if implements BeanNameAware)
 * 4. BeanFactoryAware: Sets BeanFactory (if implements BeanFactoryAware)
 * 5. ApplicationContextAware: Sets ApplicationContext (if implements)
 * 6. @PostConstruct: Initialization method (if annotated)
 * 7. InitializingBean: afterPropertiesSet() (if implements)
 * 8. Custom init method: (if specified)
 * 9. Bean is READY to use
 * 
 * DESTRUCTION (when container shuts down):
 * 10. @PreDestroy: Cleanup method (if annotated)
 * 11. DisposableBean: destroy() (if implements)
 * 12. Custom destroy method: (if specified)
 * 
 * See LifecycleBean.java for complete implementation with all steps
 */

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

/**
 * ============================================
 * SIMPLE LIFECYCLE EXAMPLE
 * ============================================
 * 
 * Most common approach: Use @PostConstruct and @PreDestroy
 * This is simpler and more commonly used in practice
 */
@Component
class SimpleLifecycleBean {
    
    public SimpleLifecycleBean() {
        System.out.println("SimpleLifecycleBean: Constructor called");
    }
    
    @PostConstruct
    public void init() {
        System.out.println("SimpleLifecycleBean: @PostConstruct - Initializing");
        // Initialize resources, connect to database, etc.
    }
    
    public void doSomething() {
        System.out.println("SimpleLifecycleBean: Doing something");
    }
    
    @PreDestroy
    public void cleanup() {
        System.out.println("SimpleLifecycleBean: @PreDestroy - Cleaning up");
        // Close connections, release resources, etc.
    }
}
