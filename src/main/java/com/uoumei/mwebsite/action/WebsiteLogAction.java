package com.uoumei.mwebsite.action;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uoumei.base.entity.BaseEntity;
import com.uoumei.basic.biz.IManagerBiz;
import com.uoumei.basic.constant.Const;
import com.uoumei.basic.constant.ModelCode;
import com.uoumei.basic.constant.e.CookieConstEnum;
import com.uoumei.basic.constant.e.SessionConstEnum;
import com.uoumei.basic.entity.ManagerEntity;
import com.uoumei.mwebsite.biz.IWebsiteBiz;
import com.uoumei.mwebsite.biz.IWebsiteLogBiz;
import com.uoumei.mwebsite.entity.WebsiteEntity;
import com.uoumei.parser.IParserRegexConstant;
import com.uoumei.util.PageUtil;
import com.uoumei.util.StringUtil;

/**
 * 网站基本信息控制层
 */
@Controller
@RequestMapping("/${managerPath}/website/log")
public class WebsiteLogAction extends BaseAction{
	
	@Autowired
	private IWebsiteLogBiz websiteLogBiz;
	
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request,ModelMap mode,HttpServletResponse response){
		return view("/mwebsite/log/website_log_list");
	}
	
	
}
