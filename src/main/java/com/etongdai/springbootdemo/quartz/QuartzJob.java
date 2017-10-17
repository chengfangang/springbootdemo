package com.etongdai.springbootdemo.quartz;

import org.quartz.Job;
import org.quartz.Scheduler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定时任务
 *
 * <p>必须实现{@link Job}接口</p>
 *
 * <p>如果{@link QuartzJob}中即指定了{@code cron}又指定了{@code fixRating}，则优先使用{@code cron}</p>
 *
 * @author hotleave
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface QuartzJob {
  /**
   * cron表达式
   */
  String cron() default "";

  /**
   * 距上一次执行开始的间隔
   *
   * <p>单位：秒</p>
   */
  int fixRating() default -1;

  /**
   * 任务名称
   */
  String name() default "";

  /**
   * 任务组
   */
  String group() default Scheduler.DEFAULT_GROUP;

  /**
   * 定时器恢复时是否执行失败的任务
   */
  boolean requestsRecovery() default false;
}
