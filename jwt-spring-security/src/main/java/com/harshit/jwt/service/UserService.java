package com.harshit.jwt.service;

import com.harshit.jwt.entity.Role;
import com.harshit.jwt.entity.User;
import com.harshit.jwt.repository.RoleRepository;
import com.harshit.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User Service - Business logic for user operations
 * 
 * Implements UserDetailsService for Spring Security integration
 */
@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Load user by username for Spring Security
     * 
     * This method is called by Spring Security during authentication
     * to load user details
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(getAuthorities(user))
                .build();
    }
    
    /**
     * Get authorities (roles) for user
     * 
     * Converts Role entities to Spring Security GrantedAuthority objects
     */
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
    
    /**
     * Register new user
     * 
     * Steps:
     * 1. Check if username/email already exists
     * 2. Encode password (NEVER store plain passwords!)
     * 3. Assign default role (ROLE_USER)
     * 4. Save user
     */
    public User registerUser(String username, String password, String email) {
        // Check if user already exists
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        // Create new user
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        // IMPORTANT: Always encode password before storing
        user.setPassword(passwordEncoder.encode(password));
        
        // Assign default role
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role role = new Role("ROLE_USER");
                    return roleRepository.save(role);
                });
        user.addRole(userRole);
        
        return userRepository.save(user);
    }
    
    /**
     * Get user by username
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
    
    /**
     * Get all users (for admin)
     */
    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

