
package com.uoumei.basic.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uoumei.basic.biz.IAppBiz;
import com.uoumei.basic.constant.ModelCode;
import com.uoumei.basic.constant.e.CookieConstEnum;
import com.uoumei.basic.entity.AppEntity;
import com.uoumei.basic.entity.ManagerEntity;
import com.uoumei.basic.entity.ManagerSessionEntity;
import com.uoumei.util.StringUtil;

import com.uoumei.base.util.BasicUtil;

/**
 * 网站基本信息控制层
 * 
 */
@Controller
@RequestMapping("/${managerPath}/app/")
public class AppAction extends BaseAction {

	/**
	 * appBiz业务层的注入
	 */
	@Autowired
	private IAppBiz appBiz;

	/**
	 * 跳转到修改页面
	 * 
	 * @param mode
	 *            ModelMap实体对象
	 * @param appId
	 *            站点id
	 * @param request
	 *            请求对象
	 * @return 站点修改页面
	 */
	@RequestMapping(value = "/{appId}/edit")
	public String edit(ModelMap mode, @PathVariable int appId, HttpServletRequest request) {
		// 获取Session值 --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		
		AppEntity app = null;
		//若有appid直接根据appId查询
		if (appId < 0) {
			appId = managerSession.getBasicId();
			app = (AppEntity) appBiz.getEntity(appId);
			if(app!=null) {
				//防止session再次压入appId
				if(BasicUtil.getSession("appId")==null){
					BasicUtil.setSession("appId",app.getAppId());
				}
			} else {
				appId = (int) BasicUtil.getSession("appId");
				app = (AppEntity) appBiz.getEntity(appId);
			}
		} else {
			app = (AppEntity) appBiz.getEntity(appId);
		}

		// 判断否是超级管理员,是的话不显示站点风格
		if (this.isSystemManager(request)) {
			mode.addAttribute("SystemManager", true);
		} else {
			mode.addAttribute("SystemManager", false);
		}

		mode.addAttribute("app", app);
		return view("/app/app");

	}

	/**
	 * 更新站点信息
	 * 
	 * @param mode
	 *            ModelMap实体对象
	 * @param app
	 *            站点对象
	 * @param request
	 *            请求对象
	 * @param response
	 *            相应对象
	 */
	@RequestMapping("/update")
	@RequiresPermissions("app:update")
	public void update(ModelMap mode, @ModelAttribute AppEntity app, HttpServletRequest request,
			HttpServletResponse response) {
		mode.clear();
		// 获取Session值
		ManagerEntity managerSession = (ManagerEntity) getManagerBySession(request);
		if (managerSession == null) {
			return;
		}
		mode.addAttribute("managerSession", managerSession);

		// 判断否是超级管理员,不是则不修改应用续费时间和清单
		if (!this.isSystemManager(request)) {
			app.setAppPayDate(null);
			app.setAppPay(null);
		}
		int managerRoleID = managerSession.getManagerRoleID();
		// 判断站点数据的合法性
		// 获取cookie
		String cookie = this.getCookie(request, CookieConstEnum.PAGENO_COOKIE);
		int pageNo = 1;
		// 判断cookies是否为空
		if (!StringUtil.isBlank(cookie) && Integer.valueOf(cookie) > 0) {
			pageNo = Integer.valueOf(cookie);
		}
		mode.addAttribute("pageNo", pageNo);
		if (!checkForm(app, response)) {
			return;
		}
		if (!StringUtil.isBlank(app.getAppLogo())) {
			app.setAppLogo(app.getAppLogo().replace("|", ""));
		}

		// 过滤地址后面的/\符号
		String url = app.getAppUrl();
		String _url[] = url.split("\r\n");
		StringBuffer sb = new StringBuffer();
		for (String temp : _url) {
			String lastChar = temp.trim().substring(temp.length() - 1);
			if (lastChar.equals("/") || lastChar.equals("\\")) {
				sb.append(temp.substring(0, temp.trim().length() - 1));
			} else {
				sb.append(temp);
			}
			sb.append("\r\n");
		}
		app.setAppUrl(sb.toString());
		appBiz.updateEntity(app);
		this.outJson(response, ModelCode.APP, true, String.valueOf(pageNo), String.valueOf(managerRoleID));
	}

	/**
	 * 判断站点域名的合法性
	 * 
	 * @param app
	 *            要验证的站点信息
	 * @param response
	 *            response对象
	 */
	public boolean checkForm(AppEntity app, HttpServletResponse response) {

		/*
		 * 判断数据的合法性
		 */
		if (!StringUtil.checkLength(app.getAppKeyword(), 0, 1000)) {
			this.outJson(response, ModelCode.APP, false,
					getResString("err.length", this.getResString("appKeyword"), "0", "1000"));
			return false;
		}
		if (!StringUtil.checkLength(app.getAppCopyright(), 0, 1000)) {
			this.outJson(response, ModelCode.APP, false,
					getResString("err.length", this.getResString("appCopyright"), "0", "1000"));
			return false;
		}
		if (!StringUtil.checkLength(app.getAppDescription(), 0, 1000)) {
			this.outJson(response, ModelCode.APP, false,
					getResString("err.length", this.getResString("appDescrip"), "0", "1000"));
			return false;
		}
		if (!StringUtil.checkLength(app.getAppName(), 1, 50)) {
			this.outJson(response, ModelCode.APP, false,
					getResString("err.length", this.getResString("appTitle"), "1", "50"));
			return false;
		}
		if (!StringUtil.isBlank(app.getAppStyle()) && !StringUtil.checkLength(app.getAppStyle(), 1, 30)) {
			this.outJson(response, ModelCode.APP, false,
					getResString("err.length", this.getResString("appStyle"), "1", "30"));
			return false;
		}
		if (!StringUtil.checkLength(app.getAppHostUrl(), 10, 150)) {
			this.outJson(response, ModelCode.APP, false,
					getResString("err.length", this.getResString("appUrl"), "10", "150"));
			return false;
		}
		return true;
	}

	/**
	 * 判断是否有重复的域名
	 * 
	 * @param request
	 *            请求对象
	 * @return true:重复,false:不重复
	 */
	@RequestMapping("/checkUrl")
	@ResponseBody
	public boolean checkUrl(HttpServletRequest request) {
		if (request.getParameter("appUrl") != null) {
			if (appBiz.countByUrl(request.getParameter("appUrl")) > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}
}