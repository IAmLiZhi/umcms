
package com.uoumei.base.job;

import org.apache.log4j.Logger;
import org.quartz.Job;


public abstract class BaseJob  implements Job {

	/*
	 * log4j日志记录
	 */
	protected final Logger LOG = Logger.getLogger(this.getClass());
	
	/**
	 * 通过spring的webapplicationcontext上下文对象读取bean对象
	 * @param beanName 要读取的bean的名称
	 * @return 返回bean对象，获取不到返回null
	 */
	protected Object getBean(String beanName) {
		return com.uoumei.base.constant.Const.CONTEXT.getBean(beanName);
	}
}