package com.harshit.springboot.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

/**

/**
 * JPA AND SPRING DATA JPA EXAMPLES
 * 
 * Spring Data JPA is a framework that abstracts away the boilerplate of JPA and Hibernate.
 * It allows you to define repository interfaces and automatically generates queries,
 * making data access cleaner, faster, and more maintainable.
 * 
 * This class demonstrates various JPA annotations and concepts that are commonly asked
 * in Spring Boot interviews, including @Id, @GeneratedValue, @Entity, @Table, @Transient,
 * @ElementCollection, @OneToOne, @OneToMany, FetchType.LAZY vs EAGER, and more.
 */
@Entity
@Table(name = "product_details")  // Custom table name
public class Product {
    
    /**
     * @Id ANNOTATION
     * 
     * Every JPA entity must have a primary key, and @Id marks the field as the primary key.
     * If you don't define an @Id field, Hibernate throws an exception because every entity
     * must have a primary key to uniquely identify records in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generate ID
    private Long id;
    
    private String name;
    private Double price;
    
    /**
     * @Transient ANNOTATION
     * 
     * If you don't want to save a property or instance variable in the database, you use
     * @Transient. This field will not be persisted to the database. It's useful for
     * calculated fields or temporary data that doesn't need to be stored.
     */
    @Transient
    private double totalPrice;  // Not saved in database
    
    /**
     * @ElementCollection ANNOTATION
     * 
     * To store a list of String values in the database using Spring Data JPA, you use
     * @ElementCollection. This creates a separate table to store the collection elements.
     * You can specify the table name and join column using @CollectionTable.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "delivery", joinColumns = @JoinColumn(name = "product_id"))
    private List<String> deliveryType;  // Stores: free, prime, standard
    
    /**
     * @OneToOne WITH CASCADE
     * 
     * To save the child entity automatically while saving the parent, you use cascade
     * operations. @OneToOne(cascade = CascadeType.ALL) means that when you save the
     * parent, the child is automatically saved as well. This is useful for related
     * entities that should always be saved together.
     */
    @OneToOne(cascade = CascadeType.ALL)  // Save features before saving product
    private Features features;
    
    /**
     * @OneToMany WITH FETCH TYPE
     * 
     * FetchType.LAZY loads related entities only when accessed, which is better for
     * performance. FetchType.EAGER loads all related entities immediately, which can
     * cause performance issues with large datasets but ensures data is available.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Offer> offers;
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public List<String> getDeliveryType() { return deliveryType; }
    public void setDeliveryType(List<String> deliveryType) { this.deliveryType = deliveryType; }
    public Features getFeatures() { return features; }
    public void setFeatures(Features features) { this.features = features; }
    public List<Offer> getOffers() { return offers; }
    public void setOffers(List<Offer> offers) { this.offers = offers; }
}

/**
 * Related entity classes
 */
@Entity
class Features {
    @Id
    @GeneratedValue
    private Long id;
    private String color;
    private String size;
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
}

@Entity
class Offer {
    @Id
    @GeneratedValue
    private Long id;
    private String description;
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

/**
 * Spring Data JPA Repository
 * 
 * Spring Data JPA automatically provides implementations for common operations.
 * You just define the interface, and Spring creates the implementation at runtime.
 */
@Repository
interface ProductRepository extends JpaRepository<Product, Long> {
    // Spring Data JPA automatically generates implementation
    // You can add custom query methods here
}

