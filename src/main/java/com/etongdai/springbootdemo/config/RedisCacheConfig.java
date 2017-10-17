package com.etongdai.springbootdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author hotleave
 */
@Configuration
public class RedisCacheConfig {
  @Bean
  public RedisCacheManager getRedisCacheManage(RedisTemplate redisTemplate) {
    return new RedisCacheManager(redisTemplate);
  }
}
