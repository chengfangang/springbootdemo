package com.etongdai.springbootdemo.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Rest客户端
 *
 * @author hotleave
 */
@Configuration
public class RestTemplateConfig {
  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    builder.setConnectTimeout(10000);
    builder.setReadTimeout(30000);

    return builder.build();
  }
}
