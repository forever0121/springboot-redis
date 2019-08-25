package com.forever.config;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.Set;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.sentinel.nodes}")
    private Set<String> nodes;

    @Value("${spring.redis.sentinel.master}")
    private String master;

    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.lettuce.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.lettuce.pool.min-idle}")
    private int minIdle;
    @Value("${spring.redis.lettuce.pool.max-wait}")
    private long maxWait;
    @Value("${spring.redis.lettuce.pool.max-active}")
    private int maxActive;

    @Bean
    public RedisConnectionFactory lettuceConnectionFactory(){
        RedisSentinelConfiguration configuration = new RedisSentinelConfiguration(master, nodes);
        configuration.setPassword(RedisPassword.of(password.toCharArray()));
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxWaitMillis(maxWait);
        LettucePoolingClientConfiguration lettucePoolingClientConfiguration =
                LettucePoolingClientConfiguration.builder().poolConfig(genericObjectPoolConfig).build();
        return new LettuceConnectionFactory(configuration,lettucePoolingClientConfiguration);
    }
    @Bean
    public RedisTemplate<String,Object> redisTemplate(){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory());
        redisTemplate.setKeySerializer(keySerializer());//设置key的序列化器
        redisTemplate.setValueSerializer(valueSerializer());//设置value的序列化器
        redisTemplate.setHashKeySerializer(keySerializer());
        redisTemplate.setHashValueSerializer(valueSerializer());
        return redisTemplate;
    }

    @Bean
    @Primary//优先选择这个bean
    public CacheManager cacheManager(){
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(30))//设置超时时间30分钟
        .disableCachingNullValues()//如果是空值，不缓存
        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()));
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(lettuceConnectionFactory()))
                .cacheDefaults(redisCacheConfiguration).build();
    }

    private RedisSerializer<Object> valueSerializer() {
        return new Jackson2JsonRedisSerializer(Object.class);
    }

    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }
}
