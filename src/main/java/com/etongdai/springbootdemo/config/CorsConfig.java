package com.etongdai.springbootdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 跨域设置
 *
 * <p>仅与前端(JS)交互时才需要设置</p>
 *
 * @author hotleave
 * @see org.springframework.web.bind.annotation.CrossOrigin
 */
@Configuration
public class CorsConfig {
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new CorsWebMvcConfigurerAdapter();
  }

  private static class CorsWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
      registry.addMapping("/api/**");
    }
  }
}
