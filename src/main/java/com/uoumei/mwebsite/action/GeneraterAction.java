package com.uoumei.mwebsite.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.uoumei.basic.action.BaseAction;
import com.uoumei.basic.biz.IAppBiz;
import com.uoumei.basic.biz.IColumnBiz;
import com.uoumei.basic.biz.IModelBiz;
import com.uoumei.basic.entity.AppEntity;
import com.uoumei.basic.entity.CategoryEntity;
import com.uoumei.basic.entity.ColumnEntity;
import com.uoumei.cms.biz.IArticleBiz;
import com.uoumei.cms.constant.ModelCode;
import com.uoumei.cms.entity.ArticleEntity;
import com.uoumei.cms.parser.CmsParser;
import com.uoumei.cms.parser.GeneraterCore;
import com.uoumei.cms.util.PathUtil;
import com.uoumei.parser.IParserRegexConstant;
import com.uoumei.util.FileUtil;
import com.uoumei.util.StringUtil;
import com.uoumei.base.util.BasicUtil;

/**
 * 生成器
 */
@Controller("mwebsiteGenerater")
@RequestMapping("/${managerPath}/mwebsite/generate")
public class GeneraterAction extends BaseAction {

	/**
	 * 文章管理业务层
	 */
	@Autowired
	private IArticleBiz articleBiz;

	/**
	 * 栏目管理业务层
	 */
	@Autowired
	private IColumnBiz columnBiz;

	/**
	 * 站点管理业务层
	 */
	@Autowired
	private IAppBiz appBiz;
	
	/**
	 * 模块管理业务层
	 */
	@Autowired
	private IModelBiz modelBiz;

	/**
	 * 文章解析器
	 */
	@Autowired
	private CmsParser cmsParser;
	
	@Autowired
	private GeneraterCore generaterCore;
	
	@Value("${managerPath}")
	private String managerPath;
	

	private boolean folderExists(String path){
		File file = new File(path);
		return file.exists();
	}
	
	@RequestMapping("/syncFolder")
	@ResponseBody
	public void syncFolder(HttpServletRequest request, HttpServletResponse response){
		try {
			// 获取站点信息
			String websiteIdParam = request.getParameter("websiteId");
			if(StringUtil.isBlank(websiteIdParam)){
				return;
			}
			int websiteId = Integer.parseInt(websiteIdParam);
			AppEntity app = (AppEntity) appBiz.getEntity(websiteId);
			
			if(!StringUtil.isBlank(app.getAppStyle())){
				PathUtil.syncFolder(app.getAppId(), app.getAppStyle());
			}
			
			this.outJson(response, true);
		}catch (Exception e){
			System.out.println(e.getMessage());
			this.outJson(response, false);
		}

	}

	/**
	 * 更新主页
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request,ModelMap model) {
		List<AppEntity> websites = appBiz.queryAll();
		model.addAttribute("websites", websites);
		
		String websiteIdParam = request.getParameter("websiteId");
		int websiteId = 0;
		if (!StringUtil.isBlank(websiteIdParam)) {
			websiteId = Integer.parseInt(request.getParameter("websiteId"));
		}else{
			if(websites != null && websites.size()>0){
				AppEntity app = websites.get(0);
				websiteId = app.getAppId();
			}
		}
		model.addAttribute("websiteId", websiteId);
		
		Integer modelId = modelBiz.getEntityByModelCode(ModelCode.CMS_COLUMN).getModelId(); // 查询当前模块编号
		//获取所有的内容管理栏目
		List<ColumnEntity> list  = columnBiz.queryAll(websiteId,modelId);
		model.addAttribute("list",  JSONArray.toJSONString(list));
		model.addAttribute("now", new Date());
		
		// 返回路径
		return view("/mwebsite/generate/index");
	}

	/**
	 * 生成主页
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/generateIndex")
	@ResponseBody
	public void generateIndex(HttpServletRequest request, HttpServletResponse response) {
		// 获取站点信息
		String websiteIdParam = request.getParameter("websiteId");
		if(StringUtil.isBlank(websiteIdParam)){
			return;
		}
		int websiteId = Integer.parseInt(websiteIdParam);
		AppEntity app = (AppEntity) appBiz.getEntity(websiteId);
		
		String tmpFileName = request.getParameter("url"); // 模版文件名称
		if(StringUtil.isBlank(tmpFileName)){
			tmpFileName = "index.htm";
		}
		String generateFileName = request.getParameter("position");// 生成后的文件名称
		if(StringUtil.isBlank(generateFileName)){
			generateFileName = "index.html";
		}
		
		//生成保存htm页面地址
		String generatePath = PathUtil.getGeneratePath(app);	
		FileUtil.createFolder(generatePath);
		//网站风格路径
		String tmpPath = PathUtil.getTmpPath(app);
		
		//同步静态文件
//		if(!this.syncFolder(webSiteTmpPath, generateRoot)){
//			this.outJson(response, false,"文件同步失败");
//		}
		
		generaterCore.generateOneIndex(app, generatePath, tmpPath, generateFileName, tmpFileName);

		this.outJson(response, true);
	}

	/**
	 * 生成列表的静态页面
	 * 
	 * @param request
	 * @param response
	 * @param columnId
	 */
	@RequestMapping("/{columnId}/genernateColumn")
	@ResponseBody
	public void genernateColumn(HttpServletRequest request, HttpServletResponse response, @PathVariable int columnId) {
		// 获取站点信息
		String websiteIdParam = request.getParameter("websiteId");
		if(StringUtil.isBlank(websiteIdParam)){
			return;
		}
		int websiteId = Integer.parseInt(websiteIdParam);
		AppEntity app = (AppEntity) appBiz.getEntity(websiteId);
		
		// --zzq
//		String url = app.getAppHostUrl() + File.separator + IParserRegexConstant.HTML_SAVE_PATH + File.separator + app.getAppId();
		String url = app.getAppHostUrl();
		
		//生成保存htm页面地址
		String generatePath = PathUtil.getGeneratePath(app);	
		FileUtil.createFolder(generatePath);
		//网站风格路径
		String tmpPath = PathUtil.getTmpPath(app);
		
		List<ColumnEntity> columns = new ArrayList<ColumnEntity>();
		// 如果栏目id小于0则更新所有的栏目，否则只更新选中的栏目
		int modelId = BasicUtil.getModelCodeId(ModelCode.CMS_COLUMN); // 查询当前模块编号
		if (columnId > 0) {
			List<CategoryEntity> categorys = columnBiz.queryChildrenCategory(columnId, app.getAppId(),modelId);
			for (CategoryEntity c : categorys) {
				columns.add((ColumnEntity) columnBiz.getEntity(c.getCategoryId()));
			}
		} else {
			//获取所有的内容管理栏目
			columns = columnBiz.queryAll(app.getAppId(),modelId);
		}
		// 获取栏目列表模版
		for (ColumnEntity column : columns) {
			generaterCore.generateOneColumn(column, app, generatePath, tmpPath, url);
		}
		this.outJson(response, true);
	}

	/**
	 * 根据栏目id更新所有的文章
	 * 
	 * @param request
	 * @param response
	 * @param columnId
	 */
	@RequestMapping("/{columnId}/generateArticle")
	@ResponseBody
	public void generateArticle(HttpServletRequest request, HttpServletResponse response, @PathVariable int columnId) {
		// 获取站点信息
		String websiteIdParam = request.getParameter("websiteId");
		if(StringUtil.isBlank(websiteIdParam)){
			return;
		}
		int websiteId = Integer.parseInt(websiteIdParam);
		AppEntity app = (AppEntity) appBiz.getEntity(websiteId);
		
		String dateTime = request.getParameter("dateTime");

		//String url = app.getAppHostUrl() + File.separator + IParserRegexConstant.HTML_SAVE_PATH + File.separator + app.getAppId() + File.separator; // 文章地址前缀
		String url = app.getAppHostUrl(); // 文章地址前缀
		
		//生成保存htm页面地址
		String generatePath = PathUtil.getGeneratePath(app);	
		FileUtil.createFolder(generatePath);
		//网站风格路径
		String tmpPath = PathUtil.getTmpPath(app);
		
		List<ColumnEntity> columns = new ArrayList<ColumnEntity>();
		Integer modelId = modelBiz.getEntityByModelCode(ModelCode.CMS_COLUMN).getModelId(); // 查询当前模块编号
		if (columnId > 0) {
			List<CategoryEntity> categorys = columnBiz.queryChildrenCategory(columnId, app.getAppId(),modelId);
			for (CategoryEntity c : categorys) {
				columns.add((ColumnEntity) columnBiz.getEntity(c.getCategoryId()));
			}
		} else {
			columns = columnBiz.queryColumnListByWebsiteId(app.getAppId()); // 读取所有栏目
		}
		
		// 如果没有选择栏目，生成规则
		// 1先读取所有的栏目,从最低级的分类取
		ArticleEntity articleWhere = new ArticleEntity();
		articleWhere.setArticleState(com.uoumei.cms.constant.Const.ARTICLE_STATE_PUB); 
		for (ColumnEntity tempColumn : columns) {// 循环分类
			// 生成文档
			switch (tempColumn.getColumnType()) {
			case ColumnEntity.COLUMN_TYPE_LIST: // 列表
				List<ArticleEntity> articleList = articleBiz.query(tempColumn.getCategoryId(), dateTime, app.getAppId(),articleWhere);// .queryListByColumnId(tempColumn.getCategoryId());
				if (articleList == null || articleList.size() == 0) {
					break;
				}

				for (int ai = 0; ai < articleList.size();) {
					ArticleEntity article = articleList.get(ai);
					generaterCore.generateOneArticle(article, tempColumn, app, generatePath, tmpPath, url);
					ai++;
				}
				break;
			case ColumnEntity.COLUMN_TYPE_COVER:// 单页
				List<ArticleEntity> list = articleBiz.queryListByColumnId(tempColumn.getCategoryId(),articleWhere);
				// 表示该栏目下面没有文章
				if (list == null || list.size() == 0) {
					String writePath = null;
					String mobileStyle = app.getAppMobileStyle(); 
					if (!StringUtil.isBlank(mobileStyle)) { // 手机端
						String mobileTmpContent = FileUtil.readFile(tmpPath + File.separator + mobileStyle + File.separator + tempColumn.getColumnUrl());// 读取手机端文章模版地址
						if (!StringUtil.isBlank(mobileTmpContent)) {
							FileUtil.createFolder(generatePath + File.separator + mobileStyle + File.separator + tempColumn.getColumnPath());
							writePath = generatePath + File.separator + mobileStyle + File.separator + tempColumn.getColumnPath() + File.separator + IParserRegexConstant.HTML_INDEX;
							
							Map map = new HashMap();
							map.put(CmsParser.MOBILE,IParserRegexConstant.MOBILE);
							String coverContent = cmsParser.parse(mobileTmpContent,app,tempColumn,map); //generaterFactory.builder(app, column, coverTtmpContent, tmpPath, mobileStyle); // 解析标签
							FileUtil.writeFile(coverContent, writePath, FileUtil.URF8);// 写文件
						}
					}

					//PC端
					String tmpContent = FileUtil.readFile(tmpPath + File.separator + tempColumn.getColumnUrl());// 读取文章模版地址
					FileUtil.createFolder(generatePath + tempColumn.getColumnPath());
					writePath = generatePath + tempColumn.getColumnPath() + File.separator + IParserRegexConstant.HTML_INDEX;
					String coverContent = cmsParser.parse(tmpContent,app,tempColumn);//generaterFactory.builder(app, column, coverTtmpContent, tmpPath); // 解析标签
					FileUtil.writeFile(coverContent, writePath, FileUtil.URF8);// 写文件
					break;
				}
				
				// 取一篇文章作为封面栏目的内容
				ArticleEntity article = list.get(0);
				generaterCore.generateOneArticle(article, tempColumn, app, generatePath, tmpPath, url);

				break;
			}
		}
		this.outJson(response, true);
	}

	/**
	 * 用户预览主页
	 * @param request 
	 * @return
	 */
	@RequestMapping("/{position}/viewIndex")
	public String viewIndex(HttpServletRequest request, @PathVariable String position, HttpServletResponse response) {
		// 获取站点信息
		String websiteIdParam = request.getParameter("websiteId");
		if(StringUtil.isBlank(websiteIdParam)){
			return "";
		}
		int websiteId = Integer.parseInt(websiteIdParam);
		AppEntity app = (AppEntity) appBiz.getEntity(websiteId);
		
		//组织主页预览地址
//		String indexPosition = app.getAppHostUrl() +  File.separator + IParserRegexConstant.HTML_SAVE_PATH + File.separator + app.getAppId() + File.separator + position;
		String indexPosition = app.getAppHostUrl() +  File.separator + position;
		return "redirect:" + indexPosition;
	}
	
	/**
	 * 提供给保存与编辑文章时使用
	 * 
	 * @param request
	 * @param response
	 * @param columnId
	 *            　栏目编号
	 */
	/*
	@RequestMapping("/{columnId}/genernateForArticle")
	@ResponseBody
	public void genernateForArticle(HttpServletResponse response, HttpServletRequest request, @PathVariable int columnId) {
		// 生成html
		// 1、更新文章
//		Map parms = new HashMap();
//		parms.put("dateTime", StringUtil.getSimpleDateStr(new Date(), "yyyy-MM-dd"));
//		Header header = new Header(this.getHost(request), com.uoumei.base.constant.Const.UTF8);
//		String cookie = "";
//		for (Cookie c : request.getCookies()) {
//			cookie += c.getName() + "=" + c.getValue() + ";";
//		}
//		header.setCookie(cookie);
//		Result re = Proxy.get(this.getUrl(request) + managerPath + "/cms/generate/" + columnId + "/generateArticle.do", header, parms);
//		ColumnEntity column = (ColumnEntity) columnBiz.getEntity(columnId);
//		if (column != null && column.getColumnType() == ColumnTypeEnum.COLUMN_TYPE_COVER.toInt()) {
//			Proxy.get(this.getUrl(request) + managerPath + "/cms/generate/" + columnId + "/genernateColumn.do", header, null);
//		}
//		// 2、更新栏目
//		// Proxy.get(this.getUrl(request)+"/manager/cms/generate/"+columnId+"/genernateColumn.do",
//		// header, null, Const.UTF8);
//
//		// 3主
//		Map map = new HashMap();
//		map.put("url", IParserRegexConstant.REGEX_INDEX_HTML);
//		map.put("position", IParserRegexConstant.HTML_INDEX);
//		Proxy.get(this.getUrl(request) + managerPath + "/cms/generate/generateIndex.do", header, map);
		Map parms = new HashMap();
		parms.put("dateTime", StringUtil.getSimpleDateStr(new Date(), "yyyy-MM-dd"));
		StringBuffer cookie = new StringBuffer();
		for (Cookie c : request.getCookies()) {
			cookie.append(c.getName()).append("=").append(c.getValue()).append(";");
		}
		HttpUtil.get(this.getUrl(request) + managerPath + "/cms/generate/" + columnId + "/generateArticle.do",parms);
		ColumnEntity column = (ColumnEntity) columnBiz.getEntity(columnId);
		if (column != null && column.getColumnType() == ColumnTypeEnum.COLUMN_TYPE_COVER.toInt()) {
			HttpUtil.get(this.getUrl(request) + managerPath + "/cms/generate/" + columnId + "/genernateColumn.do");
		}
		// 2、更新栏目
		// Proxy.get(this.getUrl(request)+"/manager/cms/generate/"+columnId+"/genernateColumn.do",
		// header, null, Const.UTF8);

		// 3主
		Map map = new HashMap();
		map.put("url", IParserRegexConstant.REGEX_INDEX_HTML);
		map.put("position", IParserRegexConstant.HTML_INDEX);
		HttpUtil.get(this.getUrl(request) + managerPath + "/cms/generate/generateIndex.do", map);

		this.outJson(response, ModelCode.CMS_GENERATE_ARTICLE, true);
	}
	 */

}