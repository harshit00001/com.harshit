package com.design.patterns.cqrs;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CQRS PATTERN - Interview Explanation:
 * 
 * CQRS = Command Query Responsibility Segregation
 * 
 * Problem: In traditional systems, the same data model is used for
 * reading and writing, which can cause performance issues and complexity.
 * 
 * Solution: Separate read and write operations into different models.
 * - Commands: Write operations (create, update, delete) - optimized for writes
 * - Queries: Read operations (get, list, search) - optimized for reads
 * 
 * Simple Explanation:
 * - Instead of one database for everything, you have:
 *   * Write database: Optimized for fast writes
 *   * Read database: Optimized for fast reads (can be denormalized)
 * - They can be different databases, different schemas, or different services
 * 
 * Interview Tip: Used when read and write workloads are very different.
 */
public class CQRSPattern {

    public static void main(String[] args) {
        System.out.println("=== CQRS PATTERN DEMONSTRATION ===\n");
        
        // Interview Point: Separate command and query handlers
        UserCommandHandler commandHandler = new UserCommandHandler();
        UserQueryHandler queryHandler = new UserQueryHandler();
        
        // Interview Point: Commands for write operations
        System.out.println("--- WRITE OPERATIONS (Commands) ---");
        commandHandler.handleCreateUser(new CreateUserCommand("user1", "John Doe", "john@example.com"));
        commandHandler.handleCreateUser(new CreateUserCommand("user2", "Jane Smith", "jane@example.com"));
        commandHandler.handleUpdateUser(new UpdateUserCommand("user1", "John Updated"));
        
        // Interview Point: Queries for read operations
        System.out.println("\n--- READ OPERATIONS (Queries) ---");
        UserDTO user = queryHandler.handleGetUser(new GetUserQuery("user1"));
        System.out.println("User: " + user);
        
        List<UserDTO> users = queryHandler.handleListUsers(new ListUsersQuery());
        System.out.println("All users: " + users);
        
        // Interview Point: Write and read models can be different
        System.out.println("\n--- DIFFERENT MODELS ---");
        System.out.println("Write model: User (normalized, for writes)");
        System.out.println("Read model: UserDTO (denormalized, optimized for reads)");
    }
}

// ==================== COMMAND SIDE (Write) ====================

/**
 * Interview Point: Command Handler
 * Handles write operations (create, update, delete)
 * Writes to write-optimized store
 */
class UserCommandHandler {
    // Interview Point: Write store - optimized for writes
    // In real system, this could be a database optimized for writes
    private Map<String, User> writeStore = new ConcurrentHashMap<>();
    private EventBus eventBus = new EventBus();
    
    public void handleCreateUser(CreateUserCommand command) {
        System.out.println("Command: Creating user " + command.getUserId());
        
        // Interview Point: Write to write store
        User user = new User(command.getUserId(), command.getName(), command.getEmail());
        writeStore.put(command.getUserId(), user);
        
        // Interview Point: Publish event to update read store
        // This is how read and write stores stay in sync
        eventBus.publish(new UserCreatedEvent(command.getUserId(), command.getName(), command.getEmail()));
        
        System.out.println("  ✓ User created in write store");
    }
    
    public void handleUpdateUser(UpdateUserCommand command) {
        System.out.println("Command: Updating user " + command.getUserId());
        
        User user = writeStore.get(command.getUserId());
        if (user != null) {
            user.setName(command.getNewName());
            writeStore.put(command.getUserId(), user);
            
            // Interview Point: Publish update event
            eventBus.publish(new UserUpdatedEvent(command.getUserId(), command.getNewName()));
            System.out.println("  ✓ User updated in write store");
        }
    }
    
    public void handleDeleteUser(DeleteUserCommand command) {
        System.out.println("Command: Deleting user " + command.getUserId());
        writeStore.remove(command.getUserId());
        eventBus.publish(new UserDeletedEvent(command.getUserId()));
        System.out.println("  ✓ User deleted from write store");
    }
}

/**
 * Interview Point: Commands - represent write operations
 * They are immutable and contain all data needed for the operation
 */
class CreateUserCommand {
    private String userId;
    private String name;
    private String email;
    
    public CreateUserCommand(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
    
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}

class UpdateUserCommand {
    private String userId;
    private String newName;
    
    public UpdateUserCommand(String userId, String newName) {
        this.userId = userId;
        this.newName = newName;
    }
    
    public String getUserId() { return userId; }
    public String getNewName() { return newName; }
}

class DeleteUserCommand {
    private String userId;
    
    public DeleteUserCommand(String userId) {
        this.userId = userId;
    }
    
    public String getUserId() { return userId; }
}

// ==================== QUERY SIDE (Read) ====================

/**
 * Interview Point: Query Handler
 * Handles read operations (get, list, search)
 * Reads from read-optimized store (can be denormalized, cached, etc.)
 */
class UserQueryHandler {
    // Interview Point: Read store - optimized for reads
    // Can be denormalized, have indexes, be a different database, etc.
    private Map<String, UserDTO> readStore = new ConcurrentHashMap<>();
    
    public UserQueryHandler() {
        // Interview Point: In real system, read store is updated via events
        // For demo, we'll manually sync
        EventBus.getInstance().subscribe(UserCreatedEvent.class, this::onUserCreated);
        EventBus.getInstance().subscribe(UserUpdatedEvent.class, this::onUserUpdated);
        EventBus.getInstance().subscribe(UserDeletedEvent.class, this::onUserDeleted);
    }
    
    public UserDTO handleGetUser(GetUserQuery query) {
        System.out.println("Query: Getting user " + query.getUserId());
        // Interview Point: Read from read-optimized store
        UserDTO user = readStore.get(query.getUserId());
        System.out.println("  ✓ User retrieved from read store");
        return user;
    }
    
    public List<UserDTO> handleListUsers(ListUsersQuery query) {
        System.out.println("Query: Listing all users");
        // Interview Point: Read store can have pre-computed views
        List<UserDTO> users = new ArrayList<>(readStore.values());
        System.out.println("  ✓ Users retrieved from read store");
        return users;
    }
    
    // Interview Point: Event handlers to keep read store in sync
    private void onUserCreated(UserCreatedEvent event) {
        // Interview Point: Read model can be different - denormalized, optimized
        UserDTO dto = new UserDTO(event.getUserId(), event.getName(), event.getEmail());
        readStore.put(event.getUserId(), dto);
        System.out.println("  → Read store updated via event");
    }
    
    private void onUserUpdated(UserUpdatedEvent event) {
        UserDTO dto = readStore.get(event.getUserId());
        if (dto != null) {
            dto.setName(event.getNewName());
            System.out.println("  → Read store updated via event");
        }
    }
    
    private void onUserDeleted(UserDeletedEvent event) {
        readStore.remove(event.getUserId());
        System.out.println("  → Read store updated via event");
    }
}

/**
 * Interview Point: Queries - represent read operations
 */
class GetUserQuery {
    private String userId;
    
    public GetUserQuery(String userId) {
        this.userId = userId;
    }
    
    public String getUserId() { return userId; }
}

class ListUsersQuery {
    // Can have filters, pagination, etc.
}

// ==================== MODELS ====================

/**
 * Interview Point: Write Model
 * Optimized for writes - normalized, can have complex validation
 */
class User {
    private String userId;
    private String name;
    private String email;
    
    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
    
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
}

/**
 * Interview Point: Read Model (DTO)
 * Optimized for reads - can be denormalized, flattened, have computed fields
 * This is what clients see when they query
 */
class UserDTO {
    private String userId;
    private String name;
    private String email;
    // Interview Point: Can have additional fields not in write model
    private String displayName; // Computed field
    
    public UserDTO(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.displayName = name.toUpperCase(); // Example of computed field
    }
    
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public void setName(String name) { 
        this.name = name;
        this.displayName = name.toUpperCase();
    }
    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() {
        return "UserDTO{id='" + userId + "', name='" + name + "', email='" + email + "'}";
    }
}

// ==================== EVENTS ====================

/**
 * Interview Point: Events keep read and write stores in sync
 */
interface DomainEvent {}

class UserCreatedEvent implements DomainEvent {
    private String userId;
    private String name;
    private String email;
    
    public UserCreatedEvent(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
    
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}

class UserUpdatedEvent implements DomainEvent {
    private String userId;
    private String newName;
    
    public UserUpdatedEvent(String userId, String newName) {
        this.userId = userId;
        this.newName = newName;
    }
    
    public String getUserId() { return userId; }
    public String getNewName() { return newName; }
}

class UserDeletedEvent implements DomainEvent {
    private String userId;
    
    public UserDeletedEvent(String userId) {
        this.userId = userId;
    }
    
    public String getUserId() { return userId; }
}

// ==================== EVENT BUS ====================

class EventBus {
    private static EventBus instance = new EventBus();
    private Map<Class<?>, List<EventHandler>> subscribers = new ConcurrentHashMap<>();
    
    public static EventBus getInstance() {
        return instance;
    }
    
    public <T extends DomainEvent> void subscribe(Class<T> eventType, EventHandler<T> handler) {
        subscribers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
    }
    
    public void publish(DomainEvent event) {
        List<EventHandler> handlers = subscribers.get(event.getClass());
        if (handlers != null) {
            handlers.forEach(handler -> handler.handle(event));
        }
    }
}

@FunctionalInterface
interface EventHandler<T extends DomainEvent> {
    void handle(T event);
}

/**
 * INTERVIEW SUMMARY: CQRS Pattern
 * 
 * When to use:
 * - Read and write workloads are very different
 * - Need to scale reads and writes independently
 * - Complex read queries that would slow down writes
 * - Different teams working on read vs write
 * 
 * Benefits:
 * - Independent scaling of read and write
 * - Optimize each side for its purpose
 * - Can use different databases (SQL for writes, NoSQL for reads)
 * - Simpler models (no need to support both read and write)
 * 
 * Challenges:
 * - Eventual consistency (read and write stores can be out of sync temporarily)
 * - More complex architecture
 * - Need event handling to keep stores in sync
 * 
 * Real-world examples:
 * - E-commerce: Write to order DB, read from search index
 * - Social media: Write posts to DB, read from timeline cache
 * - Analytics: Write to transactional DB, read from data warehouse
 */


