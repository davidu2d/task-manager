package com.hiveplace.task_manager.config;

import com.hiveplace.task_manager.entity.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private String port;

    @Bean
    public ReactiveRedisTemplate<String, Token> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Token> serializer = new Jackson2JsonRedisSerializer<>(Token.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Token> builder =RedisSerializationContext.newSerializationContext(new Jackson2JsonRedisSerializer<>(String.class));
        RedisSerializationContext<String, Token> context = builder.value(serializer).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }
}
