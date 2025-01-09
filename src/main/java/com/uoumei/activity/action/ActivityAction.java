package com.uoumei.activity.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.runtime.ProcessInstance;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uoumei.activity.biz.impl.ActivityBizImpl;
import com.uoumei.activity.entity.ProcColumnEntity;
import com.uoumei.activity.entity.ProcColumnStepsEntity;
import com.uoumei.basic.action.BaseAction;
import com.uoumei.basic.biz.IColumnBiz;
import com.uoumei.basic.entity.ColumnEntity;
import com.uoumei.basic.entity.ManagerSessionEntity;
import com.uoumei.cms.constant.ModelCode;

import cn.hutool.core.lang.Console;

import com.uoumei.base.util.bean.EUListBean;

/**
 * 工作流
 */
@Controller("ActivitiAction")
@RequestMapping("/${managerPath}/activity")
public class ActivityAction extends BaseAction {

	/**
	 * 栏目业务层
	 */
	@Autowired
	private IColumnBiz columnBiz;

	@Autowired
	private ActivityBizImpl activityBizImpl;

	/**
	 * 返回主界面index
	 */
	@RequestMapping("/index")
	@RequiresPermissions("activity:view")
	public String index(HttpServletResponse response, HttpServletRequest request, ModelMap model) {
		// ManagerSessionEntity managerSession = (ManagerSessionEntity)
		// getManagerBySession(request);
		// int appId = managerSession.getBasicId();
		// List<ColumnEntity> list = columnBiz.queryAll(appId,
		// this.getModelCodeId(request, ModelCode.CMS_COLUMN));
		// System.out.println(list);
		// request.setAttribute("listColumn", JSONArray.toJSONString(list));
		model.addAttribute("model", "activity");
		return view("/activity/index");
	}

	/**
	 * 工作流添加跳转页面
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	public String add(HttpServletRequest request, ModelMap model) {
		// // 站点ID
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		int appId = managerSession.getBasicId();

		List<ColumnEntity> list = columnBiz.queryAll(appId, this.getModelCodeId(request));
		System.out.println(list);
		ColumnEntity columnSuper = new ColumnEntity();
		model.addAttribute("appId", appId);
		model.addAttribute("columnSuper", columnSuper);
		model.addAttribute("column", new ColumnEntity());
		model.addAttribute("listColumn", JSONArray.toJSONString(list));
		model.addAttribute("model", "activity");
		return view("/activity/form");
	}

	/**
	 * 保存并启动流程
	 * 
	 * @return
	 */
	@RequestMapping("/save")
	@RequiresPermissions("activity:save")
	public void save(@RequestBody Map<String, Object> inParam, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			JSONObject inJson = new JSONObject(inParam);
			ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
			int appId = managerSession.getBasicId();

			inJson.put("appId", appId);
			activityBizImpl.saveProc(inJson);

			// int managerId = managerSession.getManagerId();
			// System.out.println("管理员" + managerId);
			// Map<String, Object> variables = new HashMap<String, Object>();
			// variables.put("articleid", managerId);
			// ProcessInstance instance = activityBizImpl.startWork(inJson,
			// managerId, variables);
			// // System.out.println("流程" + instance.getId() + "启动成功");
			//// this.outJson(response, true, null,
			// JSONArray.toJSONString(instance.getId()));
			this.outJson(response, true, null, JSONArray.toJSONString("success"));
		} catch (Exception e) {
			this.outJson(response, false, null, this.redirectBack(request, false));
		}

	}

	/**
	 * 查询工作流程
	 */
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		int appId = managerSession.getBasicId();

		List list = activityBizImpl.queryByAppId(appId);
		EUListBean _list = new EUListBean(list, list.size());
		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(_list));
	}

	/**
	 * 删除流程
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("activity:del")
	public void delete(@RequestBody Map<String, Object> inParam, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			JSONObject inJson = new JSONObject(inParam);
			activityBizImpl.deleteById(inJson);
			this.outJson(response, true, null, JSONArray.toJSONString("success"));
		} catch (Exception e) {
			this.outJson(response, false, null, this.redirectBack(request, false));
		}

	}

	/**
	 * 根据id查询工作流详情
	 */
	@RequestMapping("/show")
	public void show(@RequestBody Map<String, Object> inParam, ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject inJson = new JSONObject(inParam);
		ProcColumnEntity procColumn = activityBizImpl.queryById(inJson);
		this.outJson(response, JSONObject.toJSONString(procColumn));
	}

	@RequestMapping("/edit")
	public void edit(@RequestBody Map<String, Object> inParam, ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject inJson = new JSONObject(inParam);
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		int appId = managerSession.getBasicId();
		inJson.put("appId", appId);
		activityBizImpl.update(inJson);
		this.outJson(response, true, null, JSONArray.toJSONString("success"));
	}

}
