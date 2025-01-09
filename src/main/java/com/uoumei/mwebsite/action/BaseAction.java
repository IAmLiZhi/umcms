package com.uoumei.mwebsite.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;

import com.uoumei.basic.constant.Const;
import com.uoumei.basic.entity.ManagerSessionEntity;
import com.uoumei.mwebsite.biz.IWebsiteBiz;
import com.uoumei.mwebsite.entity.WebsiteEntity;


/**
 * 站点基础控制类
 */
public class BaseAction extends com.uoumei.basic.action.BaseAction{
	
	/**
	 * 
	 * 根据用户请求获取网站实体
	 * @param request HttpServletRequest
	 * @return  网站实体
	 */
	protected WebsiteEntity getWebsite(HttpServletRequest request) {
		WebsiteEntity website = new WebsiteEntity();
		
		IWebsiteBiz websiteBiz = (IWebsiteBiz) getBean(request.getServletContext(), "webisteBiz");
		// 根据用户所请求的域名地址获取网站实体
		WebsiteEntity curWebsite = websiteBiz.getByUrl(this.getDomain(request));
		if(curWebsite!=null){
			BeanUtils.copyProperties(curWebsite, website);
			return website;
		}
		return null;
		
	}
	
	/**
	 * 判断当前管理员是否是系统平台管理员
	 * @param request 请求对象
	 * @return true:是系统平台管理员，false:不是系统平台管理员
	 */
	protected boolean isSystemManager(HttpServletRequest request) {
		ManagerSessionEntity manager = (ManagerSessionEntity) getManagerBySession(request);
		if (manager.getManagerRoleID() == Const.DEFAULT_SYSTEM_MANGER_ROLE_ID) {
			return true;
		} else {
			return false;
		}
	}	
}
