package com.uoumei.cms.action.web;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.uoumei.basic.action.BaseAction;
import com.uoumei.cms.biz.IArticleBiz;
import com.uoumei.cms.biz.ISiteFlowCache;

@Controller
@RequestMapping("/app/site")
public class SiteFlowAction extends BaseAction {
	@Autowired
	private ISiteFlowCache siteFlowCache;
	
	@Autowired
	private IArticleBiz articleBiz;
	
	@RequestMapping(path="/flow", method=RequestMethod.POST)
	public void flow(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String pages) throws UnsupportedEncodingException{
//		String page = pages.toString();
		String enU2 = URLDecoder.decode(pages,"utf-8");
		System.out.println(enU2);
		JSONObject url = null;
		if (enU2.endsWith("=")) enU2 = enU2.substring(0, enU2.length() - 1);
		url = (JSONObject) JSONObject.parse(enU2);
		String page = url.getString("page");
		String referer = url.getString("referer");
		if(StringUtils.isBlank(page)){
			this.outJson(response, false, "page parameter");
			return;
		}
//		String referer = request.getParameter("referer");
		if(null == referer){
			referer = "";
		}
		boolean bRtn = siteFlowCache.flow(request, page, referer);
		if(bRtn){
			this.outJson(response, true, "success");
		}else{
			this.outJson(response, false, "error");
		}
		return;

	}
	@RequestMapping("/hit")
	public void hit(HttpServletRequest request,
			HttpServletResponse response, String id){
		
		if(StringUtils.isBlank(id)){
			this.outJson(response, false, "id parameter");
			return;
		}
		
		int nId;
		try {
			nId = Integer.parseInt(id);
		}catch(Exception e){
			this.outJson(response, false, "id parameter");
			return;
		}
		articleBiz.updateHit(nId, null);
		
		this.outJson(response, true, "success");
		return;

	}
}
