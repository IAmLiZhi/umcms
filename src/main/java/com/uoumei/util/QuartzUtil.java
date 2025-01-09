
package com.uoumei.util;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 定时任务管理类
 */
public class QuartzUtil {
	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();


	private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
	private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";

	/**
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 * 
	 * @param jobName
	 *            任务名
	 * @param cls
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void addJob(String jobName, Class cls, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName, JOB_GROUP_NAME).build();// 任务名，任务组，任务执行类
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(new TriggerKey(jobName, TRIGGER_GROUP_NAME)).startNow().withSchedule(CronScheduleBuilder.cronSchedule(time)).build();
			sched.scheduleJob(jobDetail, trigger);
			// 启动
			if (!sched.isShutdown()) {
				sched.start();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 添加一个定时任务
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 * @param jobClass
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class jobClass, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();// 任务名，任务组，任务执行类
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(new TriggerKey(triggerName, triggerGroupName)).startNow().withSchedule(CronScheduleBuilder.cronSchedule(time)).build();
			sched.scheduleJob(jobDetail, trigger);			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName 任务名
	 * @param time 触发时间
	 */
	@SuppressWarnings("rawtypes")
	public static void modifyJobTime(String jobName, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName,TRIGGER_GROUP_NAME);
			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				JobKey job = JobKey.jobKey(jobName);
				JobDetail jobDetail = sched.getJobDetail(job);
				Class objJobClass = jobDetail.getJobClass();
				removeJob(jobName);
				addJob(jobName, objJobClass, time);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 修改一个任务的触发时间
	 * 
	 * @param triggerName 触发器名称
	 * @param triggerGroupName 触发组名称
	 * @param time 触发时间
	 */
	public static void modifyJobTime(String triggerName, String triggerGroupName, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName);
			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				trigger.getTriggerBuilder().startNow().withSchedule(CronScheduleBuilder.cronSchedule(time));
				// 重启触发器
				sched.resumeTrigger(triggerKey);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
	 *
	 * @param jobName 任务名称
	 */
	public static void removeJob(String jobName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobKey job = JobKey.jobKey(jobName,JOB_GROUP_NAME);
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName,TRIGGER_GROUP_NAME);
			sched.pauseTrigger(triggerKey);// 停止触发器
			sched.unscheduleJob(triggerKey);// 移除触发器
			sched.deleteJob(job);// 删除任务
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * 移除一个任务
	 * @param jobName 任务名
	 * @param jobGroupName 任务组名
	 * @param triggerName 触发器名
	 * @param triggerGroupName 触发器组名
	 */
	public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobKey job = JobKey.jobKey(jobName, jobGroupName);
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
			sched.pauseTrigger(triggerKey);// 停止触发器
			sched.unscheduleJob(triggerKey);// 移除触发器
			sched.deleteJob(job);// 删除任务
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 启动所有定时任务
	 */
	public static void startJobs() {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * 关闭所有定时任务
	 */
	public static void shutdownJobs() {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			if (!sched.isShutdown()) {
				sched.shutdown();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}