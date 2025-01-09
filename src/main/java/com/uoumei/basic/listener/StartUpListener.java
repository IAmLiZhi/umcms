
package com.uoumei.basic.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.uoumei.base.constant.Const;

@WebListener("startUpListener")
public class StartUpListener implements ServletContextListener {

	/*
	 * log4j日志记录
	 */
	protected final Logger LOG = Logger.getLogger(this.getClass());

	/**
	 * 
	 * 监听项目启动，进行初始化
	 * 
	 * @param sce
	 *            ServletContextEvent对象
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		LOG.debug("启动初始化开始");
		Const.CONTEXT = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		Const.PROJECT_PATH = sce.getServletContext().getRealPath(Const.SEPARATOR);
		if (!Const.PROJECT_PATH.endsWith(java.io.File.separator)) {
			Const.PROJECT_PATH = Const.PROJECT_PATH + java.io.File.separator;
		}
		Const.WEB_INF_PATH =sce.getServletContext().getRealPath("WEB-INF");
		if (!Const.WEB_INF_PATH.endsWith(java.io.File.separator)) {
			Const.WEB_INF_PATH = Const.WEB_INF_PATH + java.io.File.separator;
		}
		Const.BASE = sce.getServletContext().getContextPath();
		LOG.debug("启动初始化结束");
	}

	/**
	 * 监听项目终止，进行销毁
	 * 
	 * @param sce
	 *            ServletContextEvent对象
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	}

}