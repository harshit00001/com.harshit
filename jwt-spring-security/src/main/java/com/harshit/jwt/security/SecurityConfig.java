package com.harshit.jwt.security;

import com.harshit.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security Configuration - Main security setup for Spring Security
 * 
 * This class configures:
 * - Authentication (how users are authenticated)
 * - Authorization (what users can access)
 * - Password encoding
 * - JWT filter integration
 * - Session management (stateless for JWT)
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  // Enables @PreAuthorize annotation
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    /**
     * Configure authentication
     * 
     * Tells Spring Security:
     * - Use our UserService to load users
     * - Use BCryptPasswordEncoder for password matching
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
            .passwordEncoder(passwordEncoder());
    }
    
    /**
     * Configure HTTP security
     * 
     * This is where we define:
     * - Which endpoints are public
     * - Which endpoints require authentication
     * - Which endpoints require specific roles
     * - Session management (stateless for JWT)
     * - CORS configuration
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (Cross-Site Request Forgery) for stateless JWT
            // In production, you might want to enable it for stateful apps
            .csrf().disable()
            
            // Configure authorization rules
            .authorizeRequests()
                // Public endpoints (no authentication required)
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()  // H2 console for testing
                
                // User endpoints (require authentication)
                .antMatchers("/api/user/**").authenticated()
                
                // Admin endpoints (require ADMIN role)
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                
                // All other requests require authentication
                .anyRequest().authenticated()
            
            .and()
            
            // Make session stateless (JWT is stateless)
            // No session will be created or used by Spring Security
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            
            .and()
            
            // Add JWT filter before UsernamePasswordAuthenticationFilter
            // This ensures JWT tokens are processed first
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            
            // Allow H2 console frames (for testing)
            .headers().frameOptions().sameOrigin();
    }
    
    /**
     * Password Encoder Bean
     * 
     * BCrypt is a strong password hashing algorithm
     * - Automatically generates salt
     * - One-way hashing (can't reverse)
     * - Slow by design (prevents brute force)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Authentication Manager Bean
     * 
     * Required for programmatic authentication
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

