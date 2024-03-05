package com.example.cachetest.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class RedissonProperties {
  @Value("spring.data.redis.host")
  private String host;

  @Value("spring.data.redis.port")
  private Integer port;

  @Value("spring.data.redis.password")
  private String password;

  @Value("spring.data.redis.timeoutInMilliseconds")
  private Integer timeoutInMilliseconds;

  @Value("spring.data.redis.ssl")
  private Boolean ssl;

}
