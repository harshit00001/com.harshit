package com.harshit.jwt.security;

import com.harshit.jwt.service.TokenBlacklistService;
import com.harshit.jwt.service.UserService;
import com.harshit.jwt.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT Authentication Filter - Intercepts requests and validates JWT tokens
 * 
 * This is a CRITICAL component for JWT authentication.
 * 
 * How it works:
 * 1. Intercepts every HTTP request
 * 2. Extracts JWT token from Authorization header
 * 3. Validates token (signature, expiration, blacklist)
 * 4. If valid, sets authentication in SecurityContext
 * 5. Allows request to proceed
 * 
 * Filter Chain:
 * Request → JwtAuthenticationFilter → Other Filters → Controller
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    
    /**
     * This method is called for every HTTP request
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain chain)
            throws ServletException, IOException {
        
        // Extract token from request
        final String requestTokenHeader = request.getHeader("Authorization");
        
        String username = null;
        String jwtToken = null;
        
        // JWT Token is in the form "Bearer <token>"
        // Remove "Bearer " prefix to get actual token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            
            try {
                // Extract username from token
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.warn("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.warn("JWT Token has expired");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        
        // Once we get the token, validate it
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Check if token is blacklisted (advanced feature)
            if (tokenBlacklistService.isTokenBlacklisted(jwtToken)) {
                logger.warn("Token is blacklisted");
                chain.doFilter(request, response);
                return;
            }
            
            // Load user details
            UserDetails userDetails = userService.loadUserByUsername(username);
            
            // Validate token
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                
                // Create authentication object
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, 
                                null, 
                                userDetails.getAuthorities());
                
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Set authentication in SecurityContext
                // This tells Spring Security that user is authenticated
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        
        // Continue filter chain
        chain.doFilter(request, response);
    }
}

