package com.harshit.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis Configuration
 * 
 * Redis is used for:
 * - Token blacklisting (fast lookup, auto-expiration)
 * - Caching (optional)
 * - Rate limiting (optional)
 * 
 * Note: For this demo, Redis connection will fail if Redis is not running.
 * In production, handle connection failures gracefully.
 */
@Configuration
public class RedisConfig {
    
    /**
     * Redis Connection Factory
     * 
     * Lettuce is a non-blocking Redis client
     * Alternative: Jedis (blocking)
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }
    
    /**
     * Redis Template
     * 
     * Used for Redis operations
     * Configured with String serializers for simple key-value operations
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}

