package com.harshit.jpa.intermediate.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * JPA Entity: Order (One-to-Many relationship)
 * 
 * INTERVIEW QUESTION: How do you map relationships in JPA?
 * 
 * ANSWER:
 * JPA supports the same relationship types as Hibernate:
 * - @OneToMany: One parent to many children
 * - @ManyToOne: Many children to one parent
 * - @OneToOne: One-to-one relationship
 * - @ManyToMany: Many-to-many relationship
 * 
 * KEY ANNOTATIONS:
 * - @JoinColumn: Specifies foreign key column
 * - mappedBy: Used on inverse side (non-owning side)
 * - cascade: Defines cascade operations
 * - fetch: LAZY (default for @OneToMany) or EAGER (default for @ManyToOne)
 * 
 * REAL-WORLD SCENARIO:
 * Order entity in e-commerce system:
 * - One order has many order items
 * - Order has customer information
 * - Order has total amount calculated from items
 */
@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_number", unique = true, nullable = false)
    private String orderNumber;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date", nullable = false)
    private Date orderDate;
    
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;
    
    // One-to-Many: One order has many order items
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();
    
    // Many-to-One: Many orders belong to one customer
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    public Order() {
        this.orderDate = new Date();
        this.status = OrderStatus.PENDING;
    }
    
    public Order(String orderNumber) {
        this();
        this.orderNumber = orderNumber;
    }
    
    // Helper method to maintain bidirectional relationship
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
        calculateTotal();
    }
    
    private void calculateTotal() {
        totalAmount = orderItems.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getOrderNumber() {
        return orderNumber;
    }
    
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    public Date getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderNumber='" + orderNumber + '\'' +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", itemsCount=" + (orderItems != null ? orderItems.size() : 0) +
                '}';
    }
}

enum OrderStatus {
    PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
}

