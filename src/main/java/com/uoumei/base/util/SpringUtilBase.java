package com.uoumei.base.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class SpringUtilBase {

	private static ThreadLocal<HttpServletRequest> requestLocal = new ThreadLocal<HttpServletRequest>();
	private static ThreadLocal<HttpServletResponse> responseLocal = new ThreadLocal<HttpServletResponse>();
 
	/**
	 * 获取当前请求对象
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
//		if(requestLocal.get()==null) {
			try {
				return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			} catch (Exception e) {
				return null;
			}
			
//		} else {
//			return (HttpServletRequest) requestLocal.get();
//		}
		
	}

	/**
	 * 通过spring的webapplicationcontext上下文对象读取bean对象
	 * 
	 * @param sc
	 *            上下文servletConext对象
	 * @param beanName
	 *            要读取的bean的名称
	 * @return 返回获取到的对象。获取不到返回null
	 */
	public static Object getBean(ServletContext sc, String beanName) {
		return WebApplicationContextUtils.getWebApplicationContext(sc).getBean(beanName);
	}

	/**
	 * 通过spring的webapplicationcontext上下文对象读取bean对象
	 * 
	 * @param sc
	 *            上下文servletConext对象
	 * @param beanName
	 *            要读取的bean的名称
	 * @return 返回获取到的对象。获取不到返回null
	 */
	public static Object getBean(String beanName) {
		return WebApplicationContextUtils.getWebApplicationContext(SpringUtil.getRequest().getServletContext())
				.getBean(beanName);
	}

	/**
	 * 通过spring的webapplicationcontext上下文对象读取bean对象
	 * 
	 * @param sc
	 *            上下文servletConext对象
	 * @param class
	 *            要读取的类名称
	 * @return 返回获取到的对象。获取不到返回null
	 */
	public static <T> T getBean(Class<T> cls) {
		return WebApplicationContextUtils.getWebApplicationContext(SpringUtil.getRequest().getServletContext())
				.getBean(cls);
	}

	public static HttpServletResponse getResponse() {
		return (HttpServletResponse) responseLocal.get();
	}

	public static void setResponse(HttpServletResponse response) {
		responseLocal.set(response); 
	}

	public static void setResquest(HttpServletRequest request) {
		requestLocal.set(request);
	}
	
	public static HttpSession getSession() {
		return SpringUtil.getRequest().getSession();
	}
	
	
}
