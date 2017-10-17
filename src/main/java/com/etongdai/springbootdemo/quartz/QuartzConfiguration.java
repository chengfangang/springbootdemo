package com.etongdai.springbootdemo.quartz;

import lombok.extern.slf4j.Slf4j;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class QuartzConfiguration {

  public static final Trigger[] EMPTY_TRIGGERS = new Trigger[0];
  private static final Pattern JOB_NAME_PATTERN = Pattern.compile("^(.+?)Trigger");
  private static final Object EXIST = new Object();

  private Map<TriggerKey, Object> triggerKeyMap = new HashMap<>();

  @Bean
  public SchedulerFactoryBean quartzScheduler(ApplicationContext applicationContext, DataSource dataSource,
      PlatformTransactionManager transactionManager) {
    SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

    factoryBean.setQuartzProperties(quartzProperties());
    factoryBean.setDataSource(dataSource);
    factoryBean.setTransactionManager(transactionManager);
    factoryBean.setOverwriteExistingJobs(true);
    factoryBean.setStartupDelay(10);

    AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
    jobFactory.setApplicationContext(applicationContext);
    factoryBean.setJobFactory(jobFactory);

    Trigger[] triggers = getTriggers(applicationContext);

    if (triggers.length == 0) {
      factoryBean.setAutoStartup(false);
    } else {
      factoryBean.setTriggers(triggers);
    }
    return factoryBean;
  }

  private Trigger[] getTriggers(ApplicationContext applicationContext) {
    String[] quartzJobBeanNames = applicationContext.getBeanNamesForAnnotation(QuartzJob.class);

    if (quartzJobBeanNames != null && quartzJobBeanNames.length > 0) {
      List<Trigger> triggerList = Arrays.stream(quartzJobBeanNames)
          .map(beanName -> getTrigger(beanName, applicationContext.getBean(beanName, Job.class).getClass()))
          .filter(Objects::nonNull)
          .collect(Collectors.toCollection(() -> new ArrayList<>(quartzJobBeanNames.length)));

      if (!triggerList.isEmpty()) {
        return triggerList.toArray(new Trigger[triggerList.size()]);
      }
    }

    return EMPTY_TRIGGERS;
  }

  /**
   * 从SpringBean中创建Trigger
   *
   * <p>如果注解中没有指定{@link QuartzJob#name()}, 则使用beanName作为任务名称。 Trigger的名称为任务名称加上{@code Trigger}</p>
   *
   * <p>如果{@link QuartzJob}中即指定了{@code cron}又指定了{@code fixRating}，则优先使用{@code cron}</p>
   *
   * @param beanName bean名称
   * @param jobClass 任务类
   * @return 创建的Trigger
   */
  private Trigger getTrigger(String beanName, Class<? extends Job> jobClass) {
    QuartzJob quartzJob = jobClass.getAnnotation(QuartzJob.class);
    if (quartzJob == null) {
      log.info("类{}未指定@QuartzJob注解，将不会被加入到Quartz中", jobClass.getName());
      return null;
    }

    String jobName = quartzJob.name();
    if (StringUtils.isEmpty(jobName)) {
      jobName = beanName;
    }

    JobDetailImpl jobDetail = new JobDetailImpl();
    jobDetail.setKey(new JobKey(jobName, quartzJob.group()));
    jobDetail.setJobClass(jobClass);
    jobDetail.setGroup(quartzJob.group());
    jobDetail.setRequestsRecovery(quartzJob.requestsRecovery());
    jobDetail.setDurability(true);

    return buildTriggerFromQuartzJobAnnotation(quartzJob, jobDetail);
  }

  /**
   * 根据注解信息创建Trigger
   *
   * @param annotation {@link QuartzJob}注解
   * @param jobDetail  job
   * @return 触发器
   * @throws IllegalArgumentException 如果{@link QuartzJob}中没有指定{@link QuartzJob#cron()}或{@link
   *                                  QuartzJob#fixRating()}时抛出此异常
   */
  private Trigger buildTriggerFromQuartzJobAnnotation(QuartzJob annotation, JobDetail jobDetail) {
    TriggerBuilder<Trigger> builder = TriggerBuilder.newTrigger()
        .withIdentity(jobDetail.getKey().getName() + "Trigger", annotation.group())
        .forJob(jobDetail)
        .startNow();

    if (StringUtils.hasText(annotation.cron())) {
      builder.withSchedule(CronScheduleBuilder.cronSchedule(annotation.cron()));
    } else if (annotation.fixRating() > 0) {
      builder.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(annotation.fixRating()));
    } else {
      throw new IllegalArgumentException("QuartzJob必须指定cron或fixRating其中之一");
    }

    Trigger trigger = builder.build();
    trigger.getJobDataMap().put("jobDetail", jobDetail);

    triggerKeyMap.put(trigger.getKey(), EXIST);

    return trigger;
  }

  /**
   * 读取quarta.properties配置文件
   */
  private Properties quartzProperties() {
    PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
    propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
    Properties properties = null;

    try {
      propertiesFactoryBean.afterPropertiesSet();
      properties = propertiesFactoryBean.getObject();
    } catch (IOException e) {
      log.error("读取quartz.properties失败", e);
    }

    return properties;
  }

  /**
   * 清理JobStore中过期的任务
   */
  @Autowired
  public void cleanInvalidTriggers(Scheduler scheduler) {
    try {
      Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyGroup());
      if (triggerKeys != null && !triggerKeys.isEmpty()) {
        for (TriggerKey triggerKey : triggerKeys) {
          if (!triggerKeyMap.containsKey(triggerKey)) {
            log.debug("清除触发器: {}", triggerKey);

            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            String jobName = JOB_NAME_PATTERN.matcher(triggerKey.getName()).replaceAll("$1");

            JobKey jobKey = new JobKey(jobName, triggerKey.getGroup());
            log.debug("清除任务: {}", jobKey);
            scheduler.deleteJob(jobKey);
          }
        }
      }

      // 清空Map
      triggerKeyMap.clear();
      triggerKeyMap = null;
    } catch (SchedulerException e) {
      log.warn("获取定时任务分组列表失败", e);
    }
  }
}