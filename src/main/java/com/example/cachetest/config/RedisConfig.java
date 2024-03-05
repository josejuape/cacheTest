package com.example.cachetest.config;

import com.example.cachetest.repository.CacheRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

  @Autowired
  RedissonProperties redissonProperties;
  @Bean("customSharedMemoryService")
  public CacheRepository customSharedMemoryService() {
    return createCustomClient(redissonProperties.getSsl(), redissonProperties.getHost(), redissonProperties.getPort(),
        redissonProperties.getPassword(), redissonProperties.getTimeoutInMilliseconds());
  }

  private CacheRepository createCustomClient(
      Boolean isConnectionSsl, String host, Integer port, String password, Integer timeoutInMilliseconds) {

    RedissonClient redissonClient = redissonClient(isConnectionSsl, host, port, password, timeoutInMilliseconds);
    ReactiveRedisConnectionFactory connectionFactory = new RedissonConnectionFactory(redissonClient);
    ReactiveRedisTemplate<String, Object> redisTemplate = customRedisTemplate(connectionFactory);
    ReactiveValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
    ReactiveListOperations<String, Object> listOperations = redisTemplate.opsForList();
    return new CacheRepository(valueOperations, listOperations, redisTemplate);
  }

  private RedissonClient redissonClient(
      Boolean isConnectionSsl, String host, Integer port,
      String password, Integer timeoutInMilliseconds) {
    Config config = new Config();
    SingleServerConfig singleServerConfig = config.useSingleServer();
    singleServerConfig.setAddress("rediss://" + host + ":" + port);
    singleServerConfig.setPassword(password);
    singleServerConfig.setTimeout(timeoutInMilliseconds);
    singleServerConfig.setConnectionPoolSize(4);
    singleServerConfig.setConnectionMinimumIdleSize(2);
    return Redisson.create(config);
  }

  private ReactiveRedisTemplate<String, Object> customRedisTemplate(
      ReactiveRedisConnectionFactory connectionFactory) {
    return new ReactiveRedisTemplate<>(
        connectionFactory, RedisSerializationContext
        .<String, Object>newSerializationContext()
        .key(RedisSerializer.string())
        .hashKey(RedisSerializer.string())
        .value(RedisSerializer.json())
        .hashValue(RedisSerializer.json())
        .build());
  }

}

