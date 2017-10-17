package com.etongdai.springbootdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author hotleave
 */
@Configuration
public class ThreadPoolExecutorConfig {
  @Bean("threadPoolTaskExecutor")
  public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(50);
    executor.setMaxPoolSize(50);
    executor.setQueueCapacity(200);

    return executor;
  }
}
