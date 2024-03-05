package com.example.cachetest.repository;

import com.google.common.collect.ImmutableMap;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import java.time.Duration;
import java.util.Map;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Repository;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Mono;

@Repository
public class CacheRepository {
  private static final Logger log = LoggerFactory.getLogger(CacheRepository.class);
  private final ReactiveValueOperations<String, Object> reactiveValueOperations;
  private final ReactiveListOperations<String, Object> reactiveListOperations;
  private final ReactiveRedisTemplate<String, Object> template;
  public <E> Mono<Boolean> save(String mapName, String key, E obj, Duration duration) {
    return this.reactiveValueOperations.set(this.keyBuilder(mapName, key), obj, duration);
  }
  public Mono<Object> getObject(String mapName, String key) {
    return this.reactiveValueOperations.get(this.keyBuilder(mapName, key));
  }
  private String keyBuilder(String mapName, String key) {
    return mapName.concat(":").concat(key);
  }
  @Generated
  public CacheRepository(
      ReactiveValueOperations<String, Object> reactiveValueOperations,
      ReactiveListOperations<String, Object> reactiveListOperations,
      ReactiveRedisTemplate<String, Object> template) {
    this.reactiveValueOperations = reactiveValueOperations;
    this.reactiveListOperations = reactiveListOperations;
    this.template = template;
  }
}
