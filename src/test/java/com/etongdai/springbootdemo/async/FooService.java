package com.etongdai.springbootdemo.async;

import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.Future;

/**
 * 测试异步方法
 *
 * @author hotleave
 */
@Service
@Slf4j
public class FooService {
  private final SecureRandom secureRandom = new SecureRandom();

  public void doSomething() {
    waiting(2000);
  }

  public void doAnotherThing() {
    waiting(3000);
  }

  @Async
  public void doSomethingAsync() {
    waiting(2000);
  }

  @Async
  public void doAnotherThingAsync() {
    waiting(3000);
  }

  @Async
  public Future<Long> doSomethingAsyncAndReturn() {
    long waiting = secureRandom.nextInt(1000) + 2000L;

    waiting(waiting);

    return AsyncResult.forValue(waiting);
  }

  @Async
  public Future<Long> doAnotherThingAsyncAndReturn() {
    long waiting = secureRandom.nextInt(1000) + 3000L;

    waiting(waiting);

    return AsyncResult.forValue(waiting);
  }

  private void waiting(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException ignore) {
      // we don't care
    }
  }
}
