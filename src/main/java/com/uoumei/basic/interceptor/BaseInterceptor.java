
package com.uoumei.basic.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.uoumei.base.constant.e.BaseCookieEnum;
import com.uoumei.base.constant.e.BaseSessionEnum;
import com.uoumei.basic.biz.IAppBiz;
import com.uoumei.basic.entity.AppEntity;
import com.uoumei.util.StringUtil;

/**
 * 基础拦截器
 */
public abstract class BaseInterceptor extends HandlerInterceptorAdapter {
	public final static String BASE = "base";

	public final static String BASE_PATH = "basePath";

	public final static String MODEL_ID = "modelId";

	public final static String BASE_URL = "baseUrl";

	public final static String BASE_MANAGER_PATH = "baseManager";
	
	public final static String MANAGER_PATH = "managerPath";
	public final static String MANAGER_VIEW_PATH = "managerViewPath";

	public final static String PARAMS = "params";
	
	

	protected Logger logger = Logger.getLogger(this.getClass());
	protected static boolean IS_WINDOWS = false;

	static {
		if (System.getProperty("os.name").toLowerCase().indexOf("windows") > 0) {
			IS_WINDOWS = true;
		}
	}

	/**
	 * 通过spring的webapplicationcontext上下文对象读取bean对象
	 * 
	 * @param sc
	 *            上下文servletConext对象
	 * @param beanName
	 *            要读取的bean的名称
	 * @return 获取到返回对象，否则返回null
	 */
	protected Object getBean(ServletContext sc, String beanName) {
		return WebApplicationContextUtils.getWebApplicationContext(sc).getBean(beanName);
	}

	/**
	 * 设置session
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param key
	 *            键SessionConst里面定义
	 * @param obj
	 *            对象
	 */
	protected void setSession(HttpServletRequest request, BaseSessionEnum key, Object obj) {
		if (StringUtil.isBlank(obj.toString())) {
			return;
		}
		request.getSession().setAttribute(key.toString(), obj);
	}

	/**
	 * 获取session
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param key
	 *            键SessionConst里面定义
	 */
	protected Object getSession(HttpServletRequest request, BaseSessionEnum key) {
		return request.getSession().getAttribute(key.toString());
	}
	

	/**
	 * 设置Cookie值
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param response
	 *            HttpServletResponse对象
	 * @param key
	 *            枚举类中的值
	 * @param value
	 *            存储对象
	 */
	protected void setCookie(HttpServletRequest request, HttpServletResponse response, BaseCookieEnum key,
			Object value) {
		request.getSession().setAttribute(key.toString(), value);
		Cookie cookie = new Cookie(key.toString(), (String) value);
		cookie.setPath("/");
		cookie.setValue((String) value);
		response.addCookie(cookie);
	}


	/**
	 * 设置Cookie值
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param response
	 *            HttpServletResponse对象
	 * @param key
	 *            枚举类中的值
	 * @param value
	 *            存储对象
	 * @param maxAge
	 *            cookie生命周期 以秒为单位
	 */
	protected void setCookie(HttpServletRequest request, HttpServletResponse response, BaseCookieEnum key, Object value,
			int maxAge) {
		request.getSession().setAttribute(key.toString(), value);
		Cookie cookie = new Cookie(key.toString(), value.toString());
		cookie.setPath("/");
		cookie.setValue(value.toString());
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}
	

	/**
	 * 获取Cookie的值
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param key
	 *            枚举类中的值
	 * @return Cookie中获取的对象
	 */
	protected String getCookie(HttpServletRequest request, BaseCookieEnum key) {
		if (request.getCookies() != null) {
			for (Cookie c : request.getCookies()) {
				if (c.getName().equals(key.toString())) {
					return c.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * 输出json数据字符串
	 * 
	 * @param response
	 *            HttpServletResponse对象
	 * @param jsonDataStr
	 *            字符串
	 */
	protected void outJson(HttpServletResponse response, Object jsonDataStr) {
		try {
			response.setContentType("application/json;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(jsonDataStr);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取当前模块对应的appid , appid主要根据用户的请求地址获得
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @return 返回appId，找不到对应app,返回0
	 */
	protected int getAppId(HttpServletRequest request) {
		return this.getApp(request).getAppId();
	}

	/**
	 * 获取当前模块对应的appid , appid主要根据用户的请求地址获得
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @return 返回appId，找不到对应app,返回0
	 */
	protected AppEntity getApp(HttpServletRequest request) {
		AppEntity app = new AppEntity();
		// 获取用户所请求的域名地址
		IAppBiz appBiz = (IAppBiz) getBean(request.getServletContext(), "appBiz");
		AppEntity website = appBiz.getByUrl(this.getDomain(request));
		if (website == null) {
			return null;
		}
		BeanUtils.copyProperties(website, app);
		return app;
	}
	
	/**
	 * 获取请求域名，域名不包括http请求协议头
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @return 返回域名地址
	 */
	private String getDomain(HttpServletRequest request) {
		String path = request.getContextPath();
		String domain = request.getServerName();
		if (request.getServerPort() == 80) {
			domain += path;
		} else {
			domain += ":" + request.getServerPort() + path;
		}
		return domain;
	}
	
	protected String assemblyRequestUrlParams(HttpServletRequest request) {
		this.logger.debug("url:"+request.getRequestURI());
		Map<String, String[]> map = request.getParameterMap();
		Iterator<String> key = map.keySet().iterator();
		StringBuffer sb = new StringBuffer();
		while (key.hasNext()) {
			String k = (String) key.next();
			String[] value = map.get(k);
			this.logger.debug(k + ":" + value[0]);
			if (value.length == 1) {
				String temp = null;
				if (!StringUtil.isBlank(value[0])) {
					temp = value[0];
				}
				sb.append(k).append("=").append(temp).append("&");
				request.setAttribute(k.replace(".", ""), temp);
			} else if (value.length == 0) {
				request.setAttribute(k.replace(".", ""), null);
			} else if (value.length > 1) {
				sb.append(k).append("=").append(value).append("&");
				request.setAttribute(k.replace(".", ""), value);
			}
		}
		if (sb.length()>0) {
			
			return sb.substring(0, sb.length()-1);
		}
		return sb.toString();
	}


}