package com.harshit.springbeans.basic;

/**
 * ============================================
 * CUSTOM BEAN NAMES
 * ============================================
 * 
 * By default, Spring uses class name with lowercase first letter as bean name.
 * You can specify a custom name using the value parameter.
 */

import org.springframework.stereotype.Component;

/**
 * STEP 1: Specify custom bean name
 * 
 * Bean Name: "myCustomService" (custom name)
 * 
 * Without value: Bean name would be "customBeanName"
 * With value="myCustomService": Bean name is "myCustomService"
 */
@Component("myCustomService")
public class CustomBeanName {
    
    public CustomBeanName() {
        System.out.println("STEP 2: CustomBeanName created with custom name: 'myCustomService'");
    }
    
    public void doSomething() {
        System.out.println("Custom bean doing something!");
    }
}


