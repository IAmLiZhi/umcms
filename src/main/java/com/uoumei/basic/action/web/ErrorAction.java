
package com.uoumei.basic.action.web;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uoumei.basic.action.BaseAction;
import com.uoumei.basic.biz.IAppBiz;
import com.uoumei.basic.constant.e.SessionConstEnum;
import com.uoumei.basic.entity.AppEntity;
import com.uoumei.basic.entity.ManagerSessionEntity;
import com.uoumei.basic.parser.BaseParser;
import com.uoumei.basic.parser.IGeneralParser;
import com.uoumei.parser.IParserRegexConstant;
import com.uoumei.util.FileUtil;
import com.uoumei.util.StringUtil;
import com.uoumei.base.constant.Const;
import com.uoumei.base.util.BasicUtil;
import com.uoumei.cms.util.PathUtil;

/**
 * 错误页面定义
 * 
 */
@Controller("errorAction")
@RequestMapping("/error")
public class ErrorAction extends  BaseAction{
	
	
	/**
	 *文章解析器
	 */
	@Autowired
	private BaseParser baseParser;
	
	@Autowired
	private IAppBiz appBiz;

	/**
	 * 返回404页面
	 */
	@RequestMapping("/{code}")
	@ResponseBody
	public void code(@PathVariable("code") String code, HttpServletRequest req, HttpServletResponse resp,Exception ex){
		String tmpFilePath = this.getTemplatePath(req) + File.separator + code+".htm";
		String content = 	FileUtil.readFile(tmpFilePath);
		if (StringUtil.isBlank(content)) {
			 content = FileUtil.readFile(this.getRealPath(req,"/error/"+code+".htm"));
			 if (StringUtil.isBlank(content)) {
				 content = FileUtil.readFile(this.getRealPath(req,"/error/error.htm"));
			 }
			 content = content.replace("{code/}", code);
		} else {
			content = this.parserMsTag(content,baseParser, req);
		}
		Object obj = BasicUtil.getSession(SessionConstEnum.EXCEPTOIN);
		if(obj!=null) {
			Exception e = (Exception)obj;
			StringWriter sw = new StringWriter();    
			PrintWriter pw = new PrintWriter(sw);    
			e.printStackTrace(pw);    
			content = content.replace("{"+SessionConstEnum.EXCEPTOIN.toString()+"/}", sw.toString());
		}
		this.outString(resp, content);
	}
	private String parserMsTag(String html, IGeneralParser parser, HttpServletRequest req) {
		if (StringUtil.isBlank(html)) {
			return "";
		}
		// --zzq
		AppEntity app = null;
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(req);
		if(null != managerSession  && managerSession.getBasicId()>0){
			app = (AppEntity) appBiz.getEntity(managerSession.getBasicId());
		}
		if(null == app){
			return "";
		}
		Map map = new HashMap();
		if (this.isMobileDevice(req)) {
			map.put(IGeneralParser.MOBILE, IParserRegexConstant.MOBILE);
		}
		return parser.parse(html, app, map);
	}
	private String getTemplatePath(HttpServletRequest req) {
		// --zzq
		AppEntity app = null;
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(req);
		if(null != managerSession  && managerSession.getBasicId()>0){
			app = (AppEntity) appBiz.getEntity(managerSession.getBasicId());
		}
		if(null == app){
			return "";
		}

		//--zzq
//		String tmpName = app.getAppStyle();// 获取模版名称
//		String tmpPath = getRealPath(req, IParserRegexConstant.REGEX_SAVE_TEMPLATE); // 获取系统模版存放物理路径
//		String webSiteTmpPath = tmpPath + File.separator + app.getAppId() + File.separator + tmpName;// 根据站点id组装站点信息路径 // 格式：templets／站点ID/模版风格
		String webSiteTmpPath = PathUtil.getTmpPath(app);
		
		if (this.isMobileDevice(req)) {
			webSiteTmpPath += File.separator + app.getAppMobileStyle(); // 应用移动模板存放路径
		}
		return webSiteTmpPath;
	}
}
