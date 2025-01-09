package com.uoumei.mwebsite.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.uoumei.base.constant.Const;
import com.uoumei.basic.biz.IManagerBiz;
import com.uoumei.basic.biz.IModelBiz;
import com.uoumei.basic.constant.ModelCode;
import com.uoumei.basic.entity.ManagerEntity;
import com.uoumei.mwebsite.biz.IWebsiteBiz;
import com.uoumei.mwebsite.entity.WebsiteEntity;
import com.uoumei.util.MD5Util;
import com.uoumei.util.StringUtil;

/**
 * 站群
 */
@Controller
@RequestMapping("/${managerPath}/website/manager")
public class WebsiteManagerAction extends BaseAction {

	/**
	 * appBiz业务层的注入
	 */
	@Autowired
	private IWebsiteBiz websiteBiz;

	/**
	 * managerBiz业务层的注入
	 */
	@Autowired
	private IManagerBiz managerBiz;

	@Autowired
	private IModelBiz modelBiz;

	/**
	 * 编辑站点管理员
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("{websiteId}/edit")
	@ResponseBody
	public void edit(@PathVariable int websiteId, HttpServletRequest request, HttpServletResponse response) {
		// 读取站点信息
		WebsiteEntity website = (WebsiteEntity) websiteBiz.getEntity(websiteId);
		if (website != null) {
			if (website.getWebsiteManagerId() > 0) {
				// 读取管理员信息
				ManagerEntity manager = (ManagerEntity) managerBiz.getEntity(website.getWebsiteManagerId());
				this.outJson(response, JSONObject.toJSONString(manager));

			} else {
				this.outJson(response, false);
			}
		}
	}

	/**
	 * 更新站点管理员
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("update")
	@ResponseBody
	public void update(@ModelAttribute ManagerEntity manager, HttpServletRequest request,
			HttpServletResponse response) {
		int websiteId = this.getInt(request, "managerWebsiteId");
		String modelIds = request.getParameter("modelIds");
		WebsiteEntity website = (WebsiteEntity) websiteBiz.getEntity(websiteId);
		if (website != null) {
			// 更新
			if (website.getWebsiteManagerId() > 0) {

				// 只更新昵称、密码、权限
				ManagerEntity _manager = (ManagerEntity) managerBiz.getEntity(website.getWebsiteManagerId());
				if (manager.getManagerPassword().length()>0 && !StringUtil.isBlank(manager.getManagerPassword())   && !_manager.getManagerPassword()
						.equals(MD5Util.MD5Encode(manager.getManagerPassword(), Const.UTF8))) {
					manager.setManagerPassword(MD5Util.MD5Encode(manager.getManagerPassword(), Const.UTF8));
				} else {
					manager.setManagerPassword(null);
				} 
				
				manager.setManagerId(_manager.getManagerId());
				manager.setManagerRoleID(_manager.getManagerRoleID());
				if (!StringUtil.isBlank(modelIds) && modelIds.split(",").length > 0) {
					websiteBiz.update(manager, StringUtil.stringsToInts(modelIds.split(",")));
				} else {
					websiteBiz.update(manager, null);
				}
				this.outJson(response, null, true);
			} else { // 新增
				//验证管理员用户名的值是否合法	
				if(StringUtil.isBlank(manager.getManagerName())){
					this.outJson(response, null,false,getResString("err.empty", this.getResString("manager.name")));
					return;			
				}
				if(!StringUtil.checkLength(manager.getManagerName()+"", 3, 12)){
					this.outJson(response, null, false, getResString("err.length", this.getResString("manager.name"), "3", "12"));
					return;
				}
				//验证管理员密码的值是否合法
				if(StringUtil.isBlank(manager.getManagerPassword())){
					this.outJson(response, null,false,getResString("err.empty", this.getResString("manager.password")));
					return;			
				}
				if(!StringUtil.checkLength(manager.getManagerPassword()+"", 6, 20)){
					this.outJson(response, null, false, getResString("err.length", this.getResString("manager.password"), "6", "20"));
					return;
				}
				
				ManagerEntity managerEntity = new ManagerEntity();
				managerEntity.setManagerName(manager.getManagerName());
				if (managerBiz.getEntity(managerEntity) != null) {
					this.outJson(response, ModelCode.ROLE, false, getResString("err.exist",this.getResString("managerName")));
					return;
				}
				manager.setManagerPassword(MD5Util.MD5Encode(manager.getManagerPassword(), Const.UTF8));
				if (!StringUtil.isBlank(modelIds)) {
					websiteBiz.save(website, manager, StringUtil.stringsToInts(modelIds.split(",")));
				} else {
					websiteBiz.save(website, manager, null);
				}
				this.outJson(response, null, true);
			}

		}
	}

}
