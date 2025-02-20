
package com.uoumei.basic.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.basic.biz.IManagerBiz;
import com.uoumei.basic.biz.IModelBiz;
import com.uoumei.basic.constant.Const;
import com.uoumei.basic.constant.ModelCode;
import com.uoumei.basic.entity.ManagerEntity;
import com.uoumei.basic.entity.ManagerModelPageEntity;
import com.uoumei.basic.entity.ManagerSessionEntity;
import com.uoumei.basic.entity.ModelEntity;
import com.uoumei.util.StringUtil;

/**
 * 主界面控制层
 * 
 */
@Controller
@RequestMapping("/${managerPath}")
public class MainAction extends BaseAction {

	/**
	 * 模块业务层
	 */
	@Autowired
	private IModelBiz modelBiz;

	/**
	 * 管理员业务层
	 */
	@Autowired
	private IManagerBiz managerBiz;
	

	/**
	 * 加载后台主界面，并查询数据
	 * @param request 请求对象
	 * @return  主界面地址
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request) {
			
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		List<BaseEntity> modelList = new ArrayList<BaseEntity>();
		ModelEntity model = new ModelEntity();
		modelList = modelBiz.queryModelByRoleId(managerSession.getManagerRoleID());
		request.setAttribute("managerSession", managerSession);
		request.setAttribute("modelList", JSONObject.toJSONString(modelList));
		int managerId = managerSession.getManagerId();
		//根据管理员id查找管理员模块页面实体对象
		ManagerModelPageEntity managerModelPage = null;
		//如果存在管理员模块页面实体对象，则返回到页面
		if(managerModelPage!=null){
			request.setAttribute("managerModelPage", managerModelPage);
		}
		
		
		return view("/index");
	}
	
	@RequestMapping("/main")
	public String main(HttpServletRequest request) {
		return view("/main");
	}
	
	@RequestMapping("/rf")
	@ResponseBody
	public void rf(HttpServletRequest request) {
	}

	/**
	 * 查询该父模块下的子模块
	 * @param modelId 模块ID
	 * @param request 请求对象
	 * @return 子模块列表map集合
	 */
	@RequestMapping(value = "/{modelId}/queryListByModelId", method = RequestMethod.POST)
	@ResponseBody
	public Map queryListByModelId(@PathVariable int modelId, HttpServletRequest request) {
		Map modelMap = new HashMap();
		List<BaseEntity> modelList = null;
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		ModelEntity model = new ModelEntity();
		if (isSystemManager(request) && modelId == Const.DEFAULT_CMS_MODEL_ID) { // 若为系统管理员且操作CMS模块
			model.setModelManagerId(Const.DEFAULT_SYSTEM_MANGER_ROLE_ID);
			model.setModelId(modelId);
			modelList = modelBiz.query(model);
		} else if (isSystemManager(request)) { // 若为系统管理员且非操作CMS模块
			model.setModelModelId(modelId);
			modelList = modelBiz.query(model);
		} else { // 其他管理员
			modelList = modelBiz.queryModelByRoleId(managerSession.getManagerRoleID());
			for (int i = 0; i < modelList.size(); i++) {
				ModelEntity _model = (ModelEntity) modelList.get(i);
				if (_model.getModelModelId() != modelId) {
					modelList.remove(i);
					i--;
				}
			}
		}
		modelMap.put("modelList", modelList);
		return modelMap;
	}

	/**
	 * 修改登录密码
	 * @param response 响应对象
	 * @param request 请求对象
	 */
	@RequestMapping("/editPassword")
	@ResponseBody
	public void editPassword(HttpServletResponse response, HttpServletRequest request) {
		//获取管理员信息
		ManagerEntity manager = (ManagerEntity) this.getManagerBySession(request);
		this.outJson(response, ModelCode.ROLE, false,  JSONObject.toJSONString(manager.getManagerName()));
	}

	/**
	 * 修改登录密码，若不填写密码则表示不修改
	 * 
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 */
	@RequestMapping("/updatePassword")
	@ResponseBody
	public void updatePassword( HttpServletResponse response,HttpServletRequest request) {
		//获取旧的密码,MD5加密
		String oldManagerPassword = StringUtil.Md5(request.getParameter("oldManagerPassword"));
		//获取新的密码
		String newManagerPassword = request.getParameter("newManagerPassword");
		//获取管理员信息
		ManagerEntity manager = (ManagerEntity) this.getManagerBySession(request);
		// 判断新密码和旧密码是否为空
		if (StringUtil.isBlank(newManagerPassword) || StringUtil.isBlank(oldManagerPassword)) {
			this.outJson(response, ModelCode.ROLE, false, getResString("err.empty", this.getResString("managerPassword")));
			return;
		}
		
		//判断旧的密码是否正确
		if(!oldManagerPassword.equals(manager.getManagerPassword())){
			this.outJson(response, ModelCode.ROLE, false, getResString("err.password", this.getResString("managerPassword")));
			return;
		}
		// 判断新密码长度
		if (!StringUtil.checkLength(newManagerPassword, 1, 16)) {
			this.outJson(response, ModelCode.ROLE, false, getResString("err.length", this.getResString("managerPassword"), "1", "16"));
			return;
		}
		//更改密码
		manager.setManagerPassword(StringUtil.Md5(newManagerPassword));
		//更新
		managerBiz.updateUserPasswordByUserName(manager);
		this.outJson(response, ModelCode.ROLE, true, null);
	}

	/**
	 * 退出系统
	 * @param request 请求对象
	 * @return true退出成功
	 */
	@RequestMapping("/logout")
	@ResponseBody
	public boolean loginOut(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return true;
	}
	
	/**
	 * 加载UI页面
	 * @param request
	 * @return UI页面地址
	 */
	@RequestMapping("/ui")
	public String ui(HttpServletRequest request) {
		return view("/ui");
	}
	
	/**
	 * 加载UI列表界面
	 * @param request
	 * @return 列表界面地址
	 */
	@RequestMapping("/ui/list")
	public String list(HttpServletRequest request) {
		return view("/ui/list");
	}	
	
	/**
	 * 加载UI的表单页面
	 * @param request
	 * @return 表单页面地址
	 */
	@RequestMapping("/ui/form")
	public String form(HttpServletRequest request) {
		return view("/ui/from");
	}	
}