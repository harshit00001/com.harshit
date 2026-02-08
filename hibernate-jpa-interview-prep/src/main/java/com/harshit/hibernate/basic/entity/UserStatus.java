package com.harshit.hibernate.basic.entity;

/**
 * User Status Enumeration
 * <p>
 * Represents the possible states of a user account:
 * - ACTIVE: User can log in and use the system
 * - INACTIVE: User account is disabled but not deleted
 * - SUSPENDED: User account is temporarily suspended
 * <p>
 * Stored as STRING in database for readability and maintainability.
 * Example: "ACTIVE" instead of 0, "INACTIVE" instead of 1
 */
public enum UserStatus {
    ACTIVE,      // User account is active and can be used
    INACTIVE,    // User account is inactive (disabled)
    SUSPENDED    // User account is suspended (temporarily disabled)
}
