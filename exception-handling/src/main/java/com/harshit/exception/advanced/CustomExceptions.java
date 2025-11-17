package com.harshit.exception.advanced;

/**
 * CUSTOM EXCEPTIONS - Interview Explanation:
 * 
 * Problem: Built-in exceptions might not describe your specific error clearly.
 * 
 * Solution: Create custom exceptions that are specific to your domain.
 * 
 * Simple Explanation:
 * - Instead of using generic Exception, create your own
 * - Makes code more readable and maintainable
 * - Better error messages for your specific use case
 * 
 * Two Types:
 * 1. Checked Exception: Extends Exception
 * 2. Unchecked Exception: Extends RuntimeException
 */
public class CustomExceptions {

    public static void main(String[] args) {
        System.out.println("=== CUSTOM EXCEPTIONS ===\n");
        
        demonstrateCheckedCustomException();
        demonstrateUncheckedCustomException();
        demonstrateExceptionChaining();
        demonstrateExceptionWithAdditionalInfo();
    }
    
    /**
     * Interview Point: Checked Custom Exception
     * Must be handled or declared in throws
     */
    private static void demonstrateCheckedCustomException() {
        System.out.println("--- Checked Custom Exception ---");
        
        try {
            withdrawMoney(1000, 500); // Valid
            System.out.println("  ✓ Withdrawal successful");
            
            withdrawMoney(100, 500); // Will throw InsufficientFundsException
        } catch (InsufficientFundsException e) {
            System.out.println("  ✗ " + e.getMessage());
            System.out.println("  → Available: $" + e.getAvailableBalance());
            System.out.println("  → Requested: $" + e.getRequestedAmount());
        }
        
        System.out.println();
    }
    
    /**
     * Interview Point: Unchecked Custom Exception
     * Don't need to be declared in throws
     */
    private static void demonstrateUncheckedCustomException() {
        System.out.println("--- Unchecked Custom Exception ---");
        
        try {
            createUser("john@example.com", 15); // Will throw InvalidUserException
        } catch (InvalidUserException e) {
            System.out.println("  ✗ " + e.getMessage());
            System.out.println("  → Reason: " + e.getReason());
        }
        
        try {
            createUser("jane@example.com", 25); // Valid
            System.out.println("  ✓ User created successfully");
        } catch (InvalidUserException e) {
            System.out.println("  ✗ " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Interview Point: Exception Chaining
     * Wrap one exception in another to preserve stack trace
     */
    private static void demonstrateExceptionChaining() {
        System.out.println("--- Exception Chaining ---");
        
        try {
            processPayment();
        } catch (PaymentProcessingException e) {
            System.out.println("  ✗ Payment failed: " + e.getMessage());
            System.out.println("  → Root cause: " + e.getCause().getMessage());
            // Interview Point: Can access original exception
            e.printStackTrace();
        }
        
        System.out.println();
    }
    
    /**
     * Interview Point: Exception with Additional Information
     * Custom exception can store extra data
     */
    private static void demonstrateExceptionWithAdditionalInfo() {
        System.out.println("--- Exception with Additional Info ---");
        
        try {
            validateOrder(new Order("ORD-123", -10, "user-456"));
        } catch (InvalidOrderException e) {
            System.out.println("  ✗ " + e.getMessage());
            System.out.println("  → Order ID: " + e.getOrderId());
            System.out.println("  → Validation errors: " + e.getValidationErrors());
        }
        
        System.out.println();
    }
    
    // Helper methods
    private static void withdrawMoney(double balance, double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException(balance, amount);
        }
    }
    
    private static void createUser(String email, int age) {
        if (age < 18) {
            throw new InvalidUserException("User must be 18 or older", "AGE_RESTRICTION");
        }
        // Create user logic
    }
    
    private static void processPayment() throws PaymentProcessingException {
        try {
            // Simulate database error
            throw new RuntimeException("Database connection failed");
        } catch (RuntimeException e) {
            // Interview Point: Wrap exception with cause
            throw new PaymentProcessingException("Payment processing failed", e);
        }
    }
    
    private static void validateOrder(Order order) throws InvalidOrderException {
        InvalidOrderException exception = new InvalidOrderException(order.getOrderId());
        
        if (order.getQuantity() <= 0) {
            exception.addValidationError("Quantity must be positive");
        }
        if (order.getUserId() == null || order.getUserId().isEmpty()) {
            exception.addValidationError("User ID is required");
        }
        
        if (exception.hasErrors()) {
            throw exception;
        }
    }
}

// ==================== CUSTOM EXCEPTION CLASSES ====================

/**
 * Interview Point: Checked Custom Exception
 * Extends Exception - must be handled
 */
class InsufficientFundsException extends Exception {
    private double availableBalance;
    private double requestedAmount;
    
    public InsufficientFundsException(double availableBalance, double requestedAmount) {
        super(String.format("Insufficient funds. Available: $%.2f, Requested: $%.2f", 
            availableBalance, requestedAmount));
        this.availableBalance = availableBalance;
        this.requestedAmount = requestedAmount;
    }
    
    public double getAvailableBalance() {
        return availableBalance;
    }
    
    public double getRequestedAmount() {
        return requestedAmount;
    }
}

/**
 * Interview Point: Unchecked Custom Exception
 * Extends RuntimeException - don't need to handle
 */
class InvalidUserException extends RuntimeException {
    private String reason;
    
    public InvalidUserException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
    
    public String getReason() {
        return reason;
    }
}

/**
 * Interview Point: Exception with Cause (Chaining)
 */
class PaymentProcessingException extends Exception {
    public PaymentProcessingException(String message, Throwable cause) {
        super(message, cause); // Interview Point: Pass cause to parent
    }
}

/**
 * Interview Point: Exception with Additional Data
 */
class InvalidOrderException extends Exception {
    private String orderId;
    private java.util.List<String> validationErrors = new java.util.ArrayList<>();
    
    public InvalidOrderException(String orderId) {
        super("Order validation failed");
        this.orderId = orderId;
    }
    
    public void addValidationError(String error) {
        validationErrors.add(error);
    }
    
    public boolean hasErrors() {
        return !validationErrors.isEmpty();
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public java.util.List<String> getValidationErrors() {
        return validationErrors;
    }
    
    @Override
    public String getMessage() {
        return super.getMessage() + " for order: " + orderId;
    }
}

// Supporting class
class Order {
    private String orderId;
    private int quantity;
    private String userId;
    
    public Order(String orderId, int quantity, String userId) {
        this.orderId = orderId;
        this.quantity = quantity;
        this.userId = userId;
    }
    
    public String getOrderId() { return orderId; }
    public int getQuantity() { return quantity; }
    public String getUserId() { return userId; }
}

/**
 * INTERVIEW SUMMARY: Custom Exceptions
 * 
 * When to create custom exceptions:
 * - Domain-specific errors
 * - Need additional information
 * - Better error messages
 * - Clearer code intent
 * 
 * Best Practices:
 * - Use checked for recoverable errors
 * - Use unchecked for programming errors
 * - Provide meaningful messages
 * - Include relevant data
 * - Consider exception chaining
 */

