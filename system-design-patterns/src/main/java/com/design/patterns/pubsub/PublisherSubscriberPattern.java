package com.design.patterns.pubsub;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * PUBLISHER-SUBSCRIBER PATTERN - Interview Explanation:
 * 
 * Problem: In tightly coupled systems, components need to know about
 * each other. When one component changes, others break.
 * 
 * Solution: Publisher-Subscriber (Pub-Sub) decouples components.
 * Publishers send messages without knowing who receives them.
 * Subscribers receive messages without knowing who sent them.
 * 
 * Simple Explanation:
 * - Like a radio station: Station broadcasts (publishes), 
 *   listeners tune in (subscribe)
 * - Publisher doesn't know who's listening
 * - Subscriber doesn't know who's broadcasting
 * - They communicate through a message broker/topic
 * 
 * Key Components:
 * 1. Publisher: Sends messages to topics
 * 2. Subscriber: Receives messages from topics
 * 3. Message Broker: Routes messages to subscribers
 * 4. Topic: Category/channel for messages
 * 
 * Interview Tip: Foundation for event-driven architecture.
 */
public class PublisherSubscriberPattern {

    public static void main(String[] args) {
        System.out.println("=== PUBLISHER-SUBSCRIBER PATTERN ===\n");
        
        // Interview Point: Create message broker
        MessageBroker broker = new MessageBroker();
        
        // Interview Point: Create publishers
        Publisher orderPublisher = new Publisher("OrderService", broker);
        Publisher paymentPublisher = new Publisher("PaymentService", broker);
        
        // Interview Point: Create subscribers
        Subscriber emailService = new Subscriber("EmailService");
        Subscriber inventoryService = new Subscriber("InventoryService");
        Subscriber analyticsService = new Subscriber("AnalyticsService");
        
        // Interview Point: Subscribe to topics
        broker.subscribe("order.created", emailService);
        broker.subscribe("order.created", inventoryService);
        broker.subscribe("order.created", analyticsService);
        
        broker.subscribe("payment.completed", emailService);
        broker.subscribe("payment.completed", analyticsService);
        
        System.out.println("--- Publishing Messages ---\n");
        
        // Interview Point: Publishers publish messages
        orderPublisher.publish("order.created", new OrderCreatedEvent("order-123", "user-456", 99.99));
        
        System.out.println();
        
        paymentPublisher.publish("payment.completed", new PaymentCompletedEvent("payment-789", "order-123", 99.99));
        
        System.out.println("\n--- Multiple Subscribers to Same Topic ---");
        // Interview Point: Multiple subscribers can receive same message
        broker.subscribe("order.cancelled", emailService);
        broker.subscribe("order.cancelled", inventoryService);
        orderPublisher.publish("order.cancelled", new OrderCancelledEvent("order-123"));
    }
}

// ==================== MESSAGE BROKER ====================

/**
 * Interview Point: Message Broker
 * Routes messages from publishers to subscribers
 * In real systems: Kafka, RabbitMQ, AWS SNS/SQS, etc.
 */
class MessageBroker {
    // Interview Point: Map of topic -> list of subscribers
    // CopyOnWriteArrayList for thread safety
    private Map<String, List<Subscriber>> topicSubscribers = new ConcurrentHashMap<>();
    
    /**
     * Interview Point: Subscribe to a topic
     * When messages are published to this topic, subscriber will receive them
     */
    public void subscribe(String topic, Subscriber subscriber) {
        topicSubscribers.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(subscriber);
        System.out.println("  → " + subscriber.getName() + " subscribed to topic: " + topic);
    }
    
    /**
     * Interview Point: Unsubscribe from a topic
     */
    public void unsubscribe(String topic, Subscriber subscriber) {
        List<Subscriber> subscribers = topicSubscribers.get(topic);
        if (subscribers != null) {
            subscribers.remove(subscriber);
            System.out.println("  → " + subscriber.getName() + " unsubscribed from topic: " + topic);
        }
    }
    
    /**
     * Interview Point: Publish message to topic
     * All subscribers of this topic will receive the message
     */
    public void publish(String topic, Object message) {
        System.out.println("Publishing to topic '" + topic + "': " + message);
        
        List<Subscriber> subscribers = topicSubscribers.get(topic);
        if (subscribers != null) {
            // Interview Point: Notify all subscribers
            for (Subscriber subscriber : subscribers) {
                subscriber.receive(topic, message);
            }
        } else {
            System.out.println("  (No subscribers for topic: " + topic + ")");
        }
    }
}

// ==================== PUBLISHER ====================

/**
 * Interview Point: Publisher
 * Publishes messages to topics without knowing who receives them
 */
class Publisher {
    private String name;
    private MessageBroker broker;
    
    public Publisher(String name, MessageBroker broker) {
        this.name = name;
        this.broker = broker;
    }
    
    /**
     * Interview Point: Publish message to a topic
     * Publisher doesn't know who will receive it
     */
    public void publish(String topic, Object message) {
        System.out.println("[" + name + "] Publishing to topic: " + topic);
        broker.publish(topic, message);
    }
}

// ==================== SUBSCRIBER ====================

/**
 * Interview Point: Subscriber
 * Receives messages from topics it subscribed to
 */
class Subscriber {
    private String name;
    
    public Subscriber(String name) {
        this.name = name;
    }
    
    /**
     * Interview Point: Receive message from broker
     * Subscriber doesn't know who published it
     */
    public void receive(String topic, Object message) {
        System.out.println("  [" + name + "] Received from topic '" + topic + "': " + message);
        
        // Interview Point: Subscriber processes the message
        processMessage(topic, message);
    }
    
    /**
     * Interview Point: Process the received message
     * Each subscriber can handle messages differently
     */
    private void processMessage(String topic, Object message) {
        if (name.equals("EmailService")) {
            System.out.println("    → Sending email notification...");
        } else if (name.equals("InventoryService")) {
            System.out.println("    → Updating inventory...");
        } else if (name.equals("AnalyticsService")) {
            System.out.println("    → Recording analytics event...");
        }
    }
    
    public String getName() {
        return name;
    }
}

// ==================== MESSAGE TYPES ====================

/**
 * Interview Point: Domain Events
 * Messages that represent something that happened
 */
class OrderCreatedEvent {
    private String orderId;
    private String userId;
    private double amount;
    
    public OrderCreatedEvent(String orderId, String userId, double amount) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
    }
    
    @Override
    public String toString() {
        return "OrderCreated{orderId='" + orderId + "', userId='" + userId + "', amount=" + amount + "}";
    }
}

class PaymentCompletedEvent {
    private String paymentId;
    private String orderId;
    private double amount;
    
    public PaymentCompletedEvent(String paymentId, String orderId, double amount) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
    }
    
    @Override
    public String toString() {
        return "PaymentCompleted{paymentId='" + paymentId + "', orderId='" + orderId + "', amount=" + amount + "}";
    }
}

class OrderCancelledEvent {
    private String orderId;
    
    public OrderCancelledEvent(String orderId) {
        this.orderId = orderId;
    }
    
    @Override
    public String toString() {
        return "OrderCancelled{orderId='" + orderId + "'}";
    }
}

/**
 * INTERVIEW SUMMARY: Publisher-Subscriber Pattern
 * 
 * When to use:
 * - Decouple components
 * - Event-driven architecture
 * - Multiple components need to react to same event
 * - Scalability (can add more subscribers easily)
 * - Asynchronous processing
 * 
 * Benefits:
 * - Loose coupling (publisher doesn't know subscribers)
 * - Scalability (easy to add more subscribers)
 * - Flexibility (subscribers can be added/removed dynamically)
 * - Asynchronous (non-blocking)
 * 
 * Challenges:
 * - Message delivery guarantees (at-least-once, at-most-once, exactly-once)
 * - Message ordering (maintain order or not?)
 * - Error handling (what if subscriber fails?)
 * - Message persistence (what if subscriber is down?)
 * 
 * Real-world examples:
 * - Message queues: Kafka, RabbitMQ, AWS SNS/SQS
 * - Event streaming: Apache Kafka
 * - Notification systems
 * - Microservices communication
 * - Real-time analytics
 * 
 * Variations:
 * - Topic-based: Messages go to all subscribers of a topic
 * - Queue-based: Messages go to one subscriber (load balancing)
 * - Fan-out: One message to many subscribers
 * - Fan-in: Many messages to one subscriber
 */



