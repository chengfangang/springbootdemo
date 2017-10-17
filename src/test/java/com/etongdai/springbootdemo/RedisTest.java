package com.etongdai.springbootdemo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author hotleave
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class RedisTest {
  @Autowired
  private StringRedisTemplate redisTemplate;

  @Autowired
  private RedisCacheManager cacheManager;

  @Test
  public void redisTempate() throws Exception {
    redisTemplate.boundValueOps("foo").set("bar");
    Assert.assertEquals("bar", redisTemplate.boundValueOps("foo").get());
  }

  @Test
  public void cache() throws Exception {
    cacheManager.getCache("dictCache").put("foo", "biz");

    Assert.assertEquals("biz",
        cacheManager.getCache("dictCache").get("foo").get());
  }
}
