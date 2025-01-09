
package com.uoumei.basic.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;

import com.uoumei.base.constant.Const;
import com.uoumei.basic.constant.e.SessionConstEnum;
import com.uoumei.util.StringUtil;
import com.uoumei.util.UseTimeUtil;
import com.uoumei.base.util.SpringUtil;

/**
 * 所有action的拦截器，主要是设置base与basepath
 */
public class ActionInterceptor extends BaseInterceptor {


	@Value("${managerPath}")
	private String managerPath;

	@Value("${managerViewPath}")
	private String managerViewPath;
	
	@Value("${upload.floder.path}")
	private String uploadFloderPath;
	
	@Value("${upload.server.url}")
	private String uploadServerUrl;

	/**
	 * 所有action的拦截,主要拦截base与basepath
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param response
	 *            HttpServletResponse 对象
	 * @param handler
	 *            处理器
	 * @throws Exception
	 *             异常处理
	 * @return 处理后返回true
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		long begin = System.currentTimeMillis();
		
		String modelId = request.getParameter(MODEL_ID); // 获取模块编号
		if (StringUtil.isInteger(modelId)) {
			request.getSession().setAttribute(SessionConstEnum.MODEL_ID_SESSION.toString(), modelId);
			request.getSession().setAttribute(SessionConstEnum.MODEL_TITLE_SESSION.toString(),request.getParameter("modelTitle"));
			request.getSession().setAttribute(SessionConstEnum.MODEL_NAME_SESSION.toString(), request.getParameter("modelName"));
		}
		
		com.uoumei.basic.constant.Const.VIEW = this.managerViewPath;
		com.uoumei.basic.constant.Const.UPLOAD_PATH = this.uploadFloderPath;
		String base = request.getScheme() + "://" + request.getServerName()
				+ (request.getServerPort() == 80 ? "" : ":" + request.getServerPort());
		com.uoumei.basic.constant.Const.UPLOAD_SERVER_URL = this.uploadServerUrl;
		
		request.setAttribute(BASE_MANAGER_PATH, managerPath);
		request.setAttribute(BASE, Const.BASE);
		request.setAttribute(MANAGER_PATH,Const.BASE+managerPath);
		request.setAttribute(MANAGER_VIEW_PATH,managerViewPath);
		request.setAttribute(BASE_PATH, base + Const.BASE);
		request.setAttribute(BASE_URL, base + request.getContextPath() + request.getServletPath()
				+ (request.getQueryString() == null ? "" : "?" + request.getQueryString()));
		
		request.setAttribute(PARAMS, assemblyRequestUrlParams(request));
		
		SpringUtil.setResponse(response);
		SpringUtil.setResquest(request);
		
		logger.debug("use total = " + UseTimeUtil.getTimeMillis(begin));
		
		return true;
	}

}