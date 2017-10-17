package com.etongdai.springbootdemo.async;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Future;

/**
 * @author hotleave
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AsyncTest {
  private final Logger log = LoggerFactory.getLogger(AsyncTest.class);

  @Autowired
  private FooService fooService;

  /**
   * 两个任务串行执行，总时间为各任务所用时间之和
   */
  @Test
  public void sync() throws Exception {
    long start = System.currentTimeMillis();
    fooService.doSomething();
    fooService.doAnotherThing();

    long elipse = System.currentTimeMillis() - start;
    log.info("time elipse: {}", elipse);

    // 串行时间一定大于或等于两个任务的时间之和
    Assert.assertTrue(elipse >= 5000);
  }

  /**
   * 并行执行，不关心执行结果，快速返回
   */
  @Test
  public void async() throws Exception {
    long start = System.currentTimeMillis();
    fooService.doSomethingAsync();
    fooService.doAnotherThingAsync();

    long elipse = System.currentTimeMillis() - start;
    log.info("time elipse: {}", elipse);

    // 100毫秒内就能返回，与机器配置有关
    Assert.assertTrue(elipse < 100);
  }

  /**
   * 并行执行并等待结果，所用时间取决于各任务中执行时间最长的
   */
  @Test
  public void paralla() throws Exception {
    long start = System.currentTimeMillis();

    Future<Long> someResult = fooService.doSomethingAsyncAndReturn();
    Future<Long> anotherResult = fooService.doAnotherThingAsyncAndReturn();

    long elipse = System.currentTimeMillis() - start;

    log.info("time elipsed: {}", elipse);

    // Furure的get方法会等待异步方法执行完成
    log.info("doSomethingAsyncAndReturn use: {}", someResult.get());
    log.info("doAnotherThingAsyncAndReturn use: {}", anotherResult.get());
    log.info("2 tasks total usage: {}", someResult.get() + anotherResult.get());

    long wating = System.currentTimeMillis() - start;
    log.info("paralla time: {}", wating);
  }
}
