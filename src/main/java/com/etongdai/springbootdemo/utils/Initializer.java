package com.etongdai.springbootdemo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author hotleave
 */
@Component
@Order
@Slf4j
public class Initializer implements ApplicationRunner {
  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("初始化系统");
    log.info("系统参数：{}", new ObjectMapper().writeValueAsString(args));
    log.info("系统初始化完成");
  }
}
