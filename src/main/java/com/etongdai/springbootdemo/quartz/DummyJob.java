package com.etongdai.springbootdemo.quartz;

import lombok.extern.slf4j.Slf4j;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Dummpy定时任务
 *
 * @author hotleave
 */
@Component
@Slf4j
@QuartzJob(cron = "0/5 * * * * ?"/* fixRating = 3*/, name = "fooJob")
@DisallowConcurrentExecution
public class DummyJob implements Job {
  @Autowired
  private DataSource dataSource;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    log.info("Job {} executed, autowired: {}", context.getFireInstanceId(), dataSource.getClass());
  }
}
