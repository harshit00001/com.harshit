package com.harshit.springboot.basics;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Value ANNOTATION - Injecting Properties
 * 
 * The @Value annotation allows you to inject values from application.properties or
 * application.yml into fields. This is useful for configuration values that might
 * change between environments (development, staging, production).
 */
@Component
public class ValueAnnotationExample {
    
    /**
     * INJECTING PROPERTY VALUE
     * 
     * To inject a value from application.properties into a field, you use
     * @Value("${property.name}"). Spring reads the property file and injects
     * the value when creating the bean.
     */
    @Value("${employee.name}")
    private String employeeName;
    
    /**
     * INJECTING WITH DEFAULT VALUE
     * 
     * You can provide a default value using the syntax ${property.name:defaultValue}.
     * If the property is not found, the default value is used.
     */
    @Value("${app.version:1.0.0}")
    private String appVersion;
    
    /**
     * INJECTING MULTIPLE VALUES
     * 
     * You can inject lists or arrays from properties using SpEL (Spring Expression Language).
     */
    @Value("${app.features:feature1,feature2}")
    private String[] features;
    
    public String getEmployeeName() {
        return employeeName;
    }
    
    public String getAppVersion() {
        return appVersion;
    }
    
    public String[] getFeatures() {
        return features;
    }
}

