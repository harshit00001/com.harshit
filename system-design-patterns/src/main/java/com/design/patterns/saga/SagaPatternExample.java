package com.design.patterns.saga;

import java.util.ArrayList;
import java.util.List;

/**
 * SAGA PATTERN - Interview Explanation:
 * 
 * Problem: In distributed systems, when you need to perform a transaction
 * across multiple services, traditional ACID transactions don't work
 * because each service has its own database.
 * 
 * Solution: Saga Pattern - A sequence of local transactions where each
 * transaction updates data and publishes an event. If a step fails,
 * compensating transactions are executed to undo previous steps.
 * 
 * Two Approaches:
 * 1. Choreography: Each service knows what to do next (decentralized)
 * 2. Orchestration: A central orchestrator coordinates all steps (centralized)
 * 
 * Interview Tip: Saga is used for distributed transactions in microservices.
 */
public class SagaPatternExample {

    public static void main(String[] args) {
        System.out.println("=== SAGA PATTERN: Orchestration Approach ===\n");
        demonstrateOrchestrationSaga();
        
        System.out.println("\n=== SAGA PATTERN: Choreography Approach ===\n");
        demonstrateChoreographySaga();
    }
    
    /**
     * ORCHESTRATION SAGA
     * 
     * Interview Explanation:
     * - Central orchestrator coordinates all steps
     * - Orchestrator knows the entire workflow
     * - If any step fails, orchestrator triggers compensation
     * - Pros: Centralized control, easier to understand
     * - Cons: Single point of failure, orchestrator can become complex
     */
    private static void demonstrateOrchestrationSaga() {
        System.out.println("Example: E-commerce Order Processing");
        System.out.println("Steps: Validate Payment -> Reserve Inventory -> Create Order -> Send Notification\n");
        
        OrderOrchestrator orchestrator = new OrderOrchestrator();
        
        // Simulate order creation
        OrderRequest request = new OrderRequest("order-123", "user-456", 100.0, "item-789");
        
        try {
            orchestrator.processOrder(request);
            System.out.println("\n✓ Order processed successfully!");
        } catch (SagaException e) {
            System.out.println("\n✗ Order failed: " + e.getMessage());
            System.out.println("Compensating transactions executed to rollback changes.");
        }
    }
    
    /**
     * CHOREOGRAPHY SAGA
     * 
     * Interview Explanation:
     * - No central coordinator
     * - Each service listens to events and decides what to do next
     * - Services communicate through events
     * - Pros: Decentralized, no single point of failure, scalable
     * - Cons: Harder to understand flow, difficult to debug
     */
    private static void demonstrateChoreographySaga() {
        System.out.println("Example: Order Processing with Event-Driven Architecture");
        System.out.println("Services react to events and trigger next steps\n");
        
        // Create services
        PaymentService paymentService = new PaymentService();
        InventoryService inventoryService = new InventoryService();
        OrderService orderService = new OrderService();
        NotificationService notificationService = new NotificationService();
        
        // Subscribe to events
        EventBus eventBus = new EventBus();
        eventBus.subscribe("PaymentValidated", inventoryService);
        eventBus.subscribe("InventoryReserved", orderService);
        eventBus.subscribe("OrderCreated", notificationService);
        eventBus.subscribe("PaymentFailed", paymentService); // Compensation
        eventBus.subscribe("InventoryUnavailable", inventoryService); // Compensation
        
        // Start the saga by validating payment
        OrderRequest request = new OrderRequest("order-456", "user-789", 200.0, "item-101");
        paymentService.validatePayment(request, eventBus);
    }
}

// ==================== ORCHESTRATION SAGA ====================

/**
 * Interview Point: Order Orchestrator
 * This is the central coordinator that manages the entire workflow.
 * It knows all the steps and their order.
 */
class OrderOrchestrator {
    private PaymentService paymentService = new PaymentService();
    private InventoryService inventoryService = new InventoryService();
    private OrderService orderService = new OrderService();
    private NotificationService notificationService = new NotificationService();
    
    /**
     * Interview Point: Main orchestration method
     * Executes steps in sequence, and if any fails, executes compensations
     */
    public void processOrder(OrderRequest request) throws SagaException {
        List<CompensationAction> compensations = new ArrayList<>();
        
        try {
            // Step 1: Validate Payment
            System.out.println("Step 1: Validating payment...");
            paymentService.validatePayment(request);
            compensations.add(() -> paymentService.refundPayment(request));
            
            // Step 2: Reserve Inventory
            System.out.println("Step 2: Reserving inventory...");
            inventoryService.reserveInventory(request);
            compensations.add(() -> inventoryService.releaseInventory(request));
            
            // Step 3: Create Order
            System.out.println("Step 3: Creating order...");
            orderService.createOrder(request);
            compensations.add(() -> orderService.cancelOrder(request));
            
            // Step 4: Send Notification
            System.out.println("Step 4: Sending notification...");
            notificationService.sendNotification(request);
            // No compensation needed for notification
            
        } catch (Exception e) {
            // Interview Point: If any step fails, execute compensations in reverse order
            System.out.println("\nError occurred: " + e.getMessage());
            System.out.println("Executing compensating transactions...");
            
            // Execute compensations in reverse order
            for (int i = compensations.size() - 1; i >= 0; i--) {
                try {
                    compensations.get(i).execute();
                } catch (Exception compEx) {
                    System.err.println("Compensation failed: " + compEx.getMessage());
                }
            }
            
            throw new SagaException("Order processing failed: " + e.getMessage());
        }
    }
}

// ==================== CHOREOGRAPHY SAGA ====================

/**
 * Interview Point: Event Bus for Choreography
 * Services publish events, and other services subscribe to react
 */
class EventBus {
    private List<EventSubscriber> subscribers = new ArrayList<>();
    
    public void subscribe(String eventType, EventSubscriber subscriber) {
        subscribers.add(subscriber);
    }
    
    public void publish(String eventType, Object data) {
        System.out.println("Event published: " + eventType);
        for (EventSubscriber subscriber : subscribers) {
            if (subscriber.handles(eventType)) {
                subscriber.handle(eventType, data);
            }
        }
    }
}

interface EventSubscriber {
    boolean handles(String eventType);
    void handle(String eventType, Object data);
}

// ==================== SERVICES ====================

/**
 * Interview Point: Payment Service
 * In real system, this would call payment gateway API
 */
class PaymentService implements EventSubscriber {
    public void validatePayment(OrderRequest request) throws Exception {
        // Simulate payment validation
        if (request.getAmount() > 1000) {
            throw new Exception("Payment validation failed: Amount too high");
        }
        System.out.println("  ✓ Payment validated for amount: $" + request.getAmount());
    }
    
    public void validatePayment(OrderRequest request, EventBus eventBus) {
        try {
            validatePayment(request);
            // Interview Point: Publish event for next step
            eventBus.publish("PaymentValidated", request);
        } catch (Exception e) {
            eventBus.publish("PaymentFailed", request);
        }
    }
    
    public void refundPayment(OrderRequest request) {
        System.out.println("  ↻ Compensating: Refunding payment for " + request.getOrderId());
    }
    
    @Override
    public boolean handles(String eventType) {
        return "PaymentFailed".equals(eventType);
    }
    
    @Override
    public void handle(String eventType, Object data) {
        if ("PaymentFailed".equals(eventType)) {
            System.out.println("  ↻ Payment service handling failure compensation");
        }
    }
}

class InventoryService implements EventSubscriber {
    public void reserveInventory(OrderRequest request) throws Exception {
        // Simulate inventory check
        if (request.getItemId().equals("item-out-of-stock")) {
            throw new Exception("Inventory unavailable");
        }
        System.out.println("  ✓ Inventory reserved for item: " + request.getItemId());
    }
    
    public void releaseInventory(OrderRequest request) {
        System.out.println("  ↻ Compensating: Releasing inventory for " + request.getItemId());
    }
    
    @Override
    public boolean handles(String eventType) {
        return "PaymentValidated".equals(eventType) || "InventoryUnavailable".equals(eventType);
    }
    
    @Override
    public void handle(String eventType, Object data) {
        if ("PaymentValidated".equals(eventType)) {
            OrderRequest request = (OrderRequest) data;
            try {
                reserveInventory(request);
                // Publish next event
                // In real implementation, we'd need access to eventBus here
                System.out.println("  → Publishing InventoryReserved event");
            } catch (Exception e) {
                System.out.println("  ✗ " + e.getMessage());
            }
        }
    }
}

class OrderService implements EventSubscriber {
    public void createOrder(OrderRequest request) throws Exception {
        System.out.println("  ✓ Order created: " + request.getOrderId());
    }
    
    public void cancelOrder(OrderRequest request) {
        System.out.println("  ↻ Compensating: Cancelling order " + request.getOrderId());
    }
    
    @Override
    public boolean handles(String eventType) {
        return "InventoryReserved".equals(eventType);
    }
    
    @Override
    public void handle(String eventType, Object data) {
        if ("InventoryReserved".equals(eventType)) {
            OrderRequest request = (OrderRequest) data;
            try {
                createOrder(request);
                System.out.println("  → Publishing OrderCreated event");
            } catch (Exception e) {
                System.out.println("  ✗ " + e.getMessage());
            }
        }
    }
}

class NotificationService implements EventSubscriber {
    public void sendNotification(OrderRequest request) {
        System.out.println("  ✓ Notification sent for order: " + request.getOrderId());
    }
    
    @Override
    public boolean handles(String eventType) {
        return "OrderCreated".equals(eventType);
    }
    
    @Override
    public void handle(String eventType, Object data) {
        if ("OrderCreated".equals(eventType)) {
            OrderRequest request = (OrderRequest) data;
            sendNotification(request);
        }
    }
}

// ==================== SUPPORTING CLASSES ====================

class OrderRequest {
    private String orderId;
    private String userId;
    private double amount;
    private String itemId;
    
    public OrderRequest(String orderId, String userId, double amount, String itemId) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.itemId = itemId;
    }
    
    public String getOrderId() { return orderId; }
    public String getUserId() { return userId; }
    public double getAmount() { return amount; }
    public String getItemId() { return itemId; }
}

@FunctionalInterface
interface CompensationAction {
    void execute() throws Exception;
}

class SagaException extends Exception {
    public SagaException(String message) {
        super(message);
    }
}

/**
 * INTERVIEW SUMMARY: Saga Pattern
 * 
 * When to use:
 * - Distributed transactions across multiple services
 * - Microservices architecture
 * - When you can't use traditional 2PC (Two-Phase Commit)
 * 
 * Orchestration vs Choreography:
 * - Use Orchestration when: You need centralized control, simpler debugging
 * - Use Choreography when: You want scalability, no single point of failure
 * 
 * Key Points:
 * - Each step has a compensating transaction
 * - Compensations run in reverse order
 * - Eventually consistent (not ACID)
 * - Can take time to complete (long-running transactions)
 */

