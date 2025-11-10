package com.harshit.springbeans.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * ============================================
 * COMPLETE LIFECYCLE EXAMPLE
 * ============================================
 * 
 * This class demonstrates the complete bean lifecycle
 * by implementing multiple interfaces and annotations
 */
@Component
public class LifecycleBean implements 
        BeanNameAware,           // Step 3
        BeanFactoryAware,        // Step 4
        ApplicationContextAware, // Step 5
        InitializingBean,        // Step 7
        DisposableBean {         // Step 11
    
    private String beanName;
    private String data;
    
    /**
     * STEP 1: INSTANTIATION
     * 
     * Constructor is called first
     * Bean object is created in memory
     */
    public LifecycleBean() {
        System.out.println("========================================");
        System.out.println("STEP 1: INSTANTIATION");
        System.out.println("  -> Constructor called");
        System.out.println("  -> Object created in memory");
        System.out.println("========================================");
    }
    
    /**
     * STEP 2: POPULATE PROPERTIES
     * 
     * Spring injects dependencies here
     * (Setter injection, field injection, etc.)
     */
    public void setData(String data) {
        this.data = data;
        System.out.println("STEP 2: POPULATE PROPERTIES");
        System.out.println("  -> Dependencies injected: " + data);
    }
    
    /**
     * STEP 3: BEAN NAME AWARE
     * 
     * Spring tells the bean its name in the container
     */
    @Override
    public void setBeanName(String name) {
        this.beanName = name;
        System.out.println("STEP 3: BEAN NAME AWARE");
        System.out.println("  -> Bean name set: " + name);
    }
    
    /**
     * STEP 4: BEAN FACTORY AWARE
     * 
     * Spring provides access to BeanFactory
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("STEP 4: BEAN FACTORY AWARE");
        System.out.println("  -> BeanFactory provided");
    }
    
    /**
     * STEP 5: APPLICATION CONTEXT AWARE
     * 
     * Spring provides access to ApplicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("STEP 5: APPLICATION CONTEXT AWARE");
        System.out.println("  -> ApplicationContext provided");
    }
    
    /**
     * STEP 6: @POSTCONSTRUCT
     * 
     * Called AFTER all dependencies are injected
     * This is the FIRST initialization callback
     * Most commonly used for initialization
     */
    @PostConstruct
    public void postConstruct() {
        System.out.println("STEP 6: @POSTCONSTRUCT");
        System.out.println("  -> @PostConstruct method called");
        System.out.println("  -> Bean is initialized and ready!");
    }
    
    /**
     * STEP 7: INITIALIZING BEAN
     * 
     * Called after @PostConstruct
     * Part of InitializingBean interface
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("STEP 7: INITIALIZING BEAN");
        System.out.println("  -> afterPropertiesSet() called");
        System.out.println("  -> All properties set, bean fully initialized");
    }
    
    /**
     * STEP 8: CUSTOM INIT METHOD
     * 
     * Can be specified in @Bean(initMethod = "customInit")
     * or in XML configuration
     * 
     * (Not shown here, but can be added)
     */
    
    /**
     * STEP 9: BEAN IS READY
     * 
     * Bean is now fully initialized and ready to use
     */
    public void doWork() {
        System.out.println("STEP 9: BEAN IS READY");
        System.out.println("  -> Bean name: " + beanName);
        System.out.println("  -> Bean doing work: " + data);
    }
    
    /**
     * STEP 10: @PREDESTROY
     * 
     * Called when container is shutting down
     * This is the FIRST destruction callback
     * Most commonly used for cleanup
     */
    @PreDestroy
    public void preDestroy() {
        System.out.println("========================================");
        System.out.println("STEP 10: @PREDESTROY");
        System.out.println("  -> @PreDestroy method called");
        System.out.println("  -> Container shutting down, cleaning up...");
    }
    
    /**
     * STEP 11: DISPOSABLE BEAN
     * 
     * Called after @PreDestroy
     * Part of DisposableBean interface
     */
    @Override
    public void destroy() throws Exception {
        System.out.println("STEP 11: DISPOSABLE BEAN");
        System.out.println("  -> destroy() called");
        System.out.println("  -> Bean is being destroyed");
        System.out.println("========================================");
    }
}


