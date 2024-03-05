package com.example.cachetest.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class CacheRedisProperties {
  @Value("spring.cache.mapa.name")
  private String name;

  @Value("spring.cache.mapa.key")
  private String key;

  @Value("spring.cache.mapa.secondsToLive")
  private Long secondsToLive;
}
