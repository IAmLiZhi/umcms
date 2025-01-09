package com.uoumei.cms.action.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uoumei.basic.action.BaseAction;
import com.uoumei.cms.biz.IArticleBiz;
import com.uoumei.cms.biz.IInteractionPublishBiz;
import com.uoumei.cms.entity.ArticleEntity;
import com.uoumei.cms.entity.InteractionPublishEntity;
import com.uoumei.base.filter.DateValueFilter;
import com.uoumei.base.filter.DoubleValueFilter;
import com.uoumei.base.util.bean.EUListBean;
import com.uoumei.base.util.BasicUtil;

@Controller
@RequestMapping("/app")
public class InteractionAction extends BaseAction {
	/**
	 * 文章管理业务处理层
	 */
	@Autowired
	private IArticleBiz articleBiz;

	/**
	 * 互动交流(发布)业务处理层
	 */
	@Autowired
	private IInteractionPublishBiz interactionPublishBiz;

	/**
	 * 搜索所有文章
	 */
	@RequestMapping(value = "/{searchId}/search", method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=UTF-8")
	public void search(@PathVariable int searchId, @RequestBody Map<String, Object> inObject,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject inJson = new JSONObject(inObject);
		JSONObject outJson = articleBiz.queryByKey(inJson);
		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(outJson, new DoubleValueFilter(),
				new DateValueFilter("yyyy-MM-dd HH:mm:ss")));

	}

	/**
	 * 互动交流发布
	 */
	@RequestMapping(value = "/interaction/save", method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=UTF-8")
	public void save(@RequestBody Map<String, Object> inObject, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			JSONObject inJson = new JSONObject(inObject);
			boolean info = interactionPublishBiz.save(inJson);
			this.outJson(response, true, null, JSONArray.toJSONString(info));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 互动交流列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/interaction/list", method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=UTF-8")
	public void list(@RequestBody Map<String, Object> inObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject inJson = new JSONObject(inObject);
		JSONObject outJson = interactionPublishBiz.queryByState(inJson);
		
//		this.outJson(response, JSONArray.toJSON(outJson));
		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(outJson, new DoubleValueFilter(),
				new DateValueFilter("yyyy-MM-dd")));

	}
	
	/**
	 * 互动交流详情
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/interaction/show", method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=UTF-8")
	public void show(@RequestBody Map<String, Object> inObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject inJson = new JSONObject(inObject);
		InteractionPublishEntity Interaction = interactionPublishBiz.show(inJson);
		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(Interaction, new DoubleValueFilter(),
				new DateValueFilter("yyyy-MM-dd HH:mm:ss")));

	}
	

}
