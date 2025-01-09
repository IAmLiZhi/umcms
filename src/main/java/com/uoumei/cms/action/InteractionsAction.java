package com.uoumei.cms.action;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.uoumei.basic.action.BaseAction;
import com.uoumei.basic.entity.ManagerSessionEntity;
import com.uoumei.cms.biz.IInteractionPublishBiz;
import com.uoumei.cms.biz.IInteractionReplyBiz;
import com.uoumei.cms.entity.ArticlePushEntity;
import com.uoumei.cms.entity.InteractionPublishEntity;
import com.uoumei.util.StringUtil;
import com.uoumei.base.filter.DateValueFilter;
import com.uoumei.base.filter.DoubleValueFilter;
import com.uoumei.base.util.BasicUtil;
import com.uoumei.base.util.bean.EUListBean;

@Controller
@RequestMapping("/${managerPath}/hd")
public class InteractionsAction extends BaseAction{
	
	/**
	 * 互动交流(发布)业务层
	 */
	@Autowired
	private IInteractionPublishBiz interactionPublishBiz;
	
	/**
	 * 互动交流(回复)业务层
	 */
	@Autowired
	private IInteractionReplyBiz interactionReplyBiz;
	
	/**
	 * 返回政策咨询主界面index
	 */
	@RequestMapping("/zczx/index")
	public String consult(HttpServletResponse response, HttpServletRequest request) {
		request.setAttribute("stateList", this.getStateList());
		return view("/interaction/consult/index");
	}
	
	/**
	 * 返回意见反馈主界面index
	 */
	@RequestMapping("/yjfk/index")
	public String feedback(HttpServletResponse response, HttpServletRequest request) {
		request.setAttribute("stateList", this.getStateList());
		return view("/interaction/feedback/index");
	}
	
	/**
	 * 返回厅长信箱主界面index
	 */
	@RequestMapping("/tzxx/index")
	public String mailbox(HttpServletResponse response, HttpServletRequest request) {
		request.setAttribute("stateList", this.getStateList());
		return view("/interaction/mailbox/index");
	}
	
	/**
	 * 返回投诉举报主界面index
	 */
	@RequestMapping("/tsjb/index")
	public String index(HttpServletResponse response, HttpServletRequest request) {
		request.setAttribute("stateList", this.getStateList());
		return view("/interaction/report/index");
	}
	/**
	 * 返回监督举报主界面index
	 */
	@RequestMapping("/jdjb/index")
	public String jdjb(HttpServletResponse response, HttpServletRequest request) {
		request.setAttribute("stateList", this.getStateList());
		return view("/interaction/jdjb/index");
	}
	
	/**
	 * 根据type查询列表
	 * type==1.咨询,2.投诉,3.反馈,4.信箱
	 */
	@RequestMapping("/{type}/list")
	public void list(@PathVariable("type") int type,@ModelAttribute InteractionPublishEntity ipe,
			HttpServletResponse response, HttpServletRequest request) {
		try {
			ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
			int appId = managerSession.getBasicId();
			int state = ipe.getState();
			if(0 == state){
				String stateStr = request.getParameter("stateStr");
				if(!StringUtil.isBlank(stateStr)){
					state = Integer.parseInt(stateStr);
				}
			}
			
			JSONObject inJson = new JSONObject();
			inJson.put("appId", appId);
			inJson.put("type", type);
			inJson.put("content", ipe.getPublishContent());
			inJson.put("state", state);
			BasicUtil.startPage();
			List<InteractionPublishEntity> list = interactionPublishBiz.queryByType(inJson);
			EUListBean _list = new EUListBean(list,  (int) BasicUtil.endPage(list).getTotal());
			this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(_list, new DoubleValueFilter(),
					new DateValueFilter("yyyy-MM-dd")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询互动交流(政策咨询)信息
	 * @param response
	 * @param request
	 */
	@RequestMapping("/{id}/consult/show")
	public String consultShow(@PathVariable("id") int id,ModelMap model,HttpServletResponse response, HttpServletRequest request){
		try {
			InteractionPublishEntity interaction = interactionPublishBiz.getById(id);
			model.addAttribute("interaction",interaction);
			return view("/interaction/consult/form");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询互动交流(厅长信箱)信息
	 * @param response
	 * @param request
	 */
	@RequestMapping("/{id}/mailbox/show")
	public String mailBoxshow(@PathVariable("id") int id,ModelMap model,HttpServletResponse response, HttpServletRequest request){
		try {
			InteractionPublishEntity interaction = interactionPublishBiz.getById(id);
			model.addAttribute("interaction",interaction);
			return view("/interaction/mailbox/form");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询互动交流(意见反馈)信息
	 * @param response
	 * @param request
	 */
	@RequestMapping("/{id}/feedback/show")
	public String show(@PathVariable("id") int id,ModelMap model,HttpServletResponse response, HttpServletRequest request){
		try {
			InteractionPublishEntity interaction = interactionPublishBiz.getById(id);
			model.addAttribute("interaction",interaction);
			return view("/interaction/feedback/form");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询互动交流(投诉举报)信息
	 * @param response
	 * @param request
	 */
	@RequestMapping("/{id}/report/show")
	public String reportShow(@PathVariable("id") int id,ModelMap model,HttpServletResponse response, HttpServletRequest request){
		try {
			InteractionPublishEntity interaction = interactionPublishBiz.getById(id);
			model.addAttribute("interaction",interaction);
			return view("/interaction/report/form");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	/**
	 * 查询监督举报信息
	 * @param response
	 * @param request
	 */
	@RequestMapping("/{id}/jdjb/show")
	public String jdjbShow(@PathVariable("id") int id,ModelMap model,HttpServletResponse response, HttpServletRequest request){
		try {
			InteractionPublishEntity interaction = interactionPublishBiz.getById(id);
			model.addAttribute("interaction",interaction);
			return view("/interaction/jdjb/form");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	/**
	 * 回复
	 */
	@RequestMapping("/reply")
	public void reploy(@RequestBody Map<String, Object> inObject,HttpServletResponse response, HttpServletRequest request) {
		JSONObject inJson = new JSONObject(inObject);
		interactionReplyBiz.save(inJson);
		this.outJson(response, true, null, JSONArray.toJSONString("success"));
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/del")
	public void delete(@RequestBody List<InteractionPublishEntity> ipes,
			HttpServletResponse response, HttpServletRequest request) {
		JSONObject inJson = new JSONObject();
		JSONArray ids = new JSONArray();
		for(InteractionPublishEntity ipe:ipes){
			ids.add(ipe.getId());
		}
		inJson.put("ids", ids);
		interactionPublishBiz.del(inJson);
		this.outJson(response, true);
	}
	
	/**
	 * 修改回复
	 */
	@RequestMapping("/update")
	public void update(@RequestBody Map<String, Object> inObject,HttpServletResponse response, HttpServletRequest request) {
		JSONObject inJson = new JSONObject(inObject);
		interactionReplyBiz.update(inJson);
		this.outJson(response, true, null, JSONArray.toJSONString("success"));
	}

	List stateList = null;
	List getStateList() {
		if(null == stateList){
			Map map1 = new HashMap();
			map1.put("stateName", "已回复");
			map1.put("stateValue", 1);
			Map map2 = new HashMap();
			map2.put("stateName", "未回复");
			map2.put("stateValue", 2);
			stateList = new ArrayList();
			stateList.add(map1);
			stateList.add(map2);
			
			return stateList;
		}
		return stateList;
	}

}
