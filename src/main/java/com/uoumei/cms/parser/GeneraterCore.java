package com.uoumei.cms.parser;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.uoumei.basic.biz.IAppBiz;
import com.uoumei.basic.biz.IColumnBiz;
import com.uoumei.basic.entity.AppEntity;
import com.uoumei.basic.entity.ColumnEntity;
import com.uoumei.cms.biz.IArticleBiz;
import com.uoumei.cms.entity.ArticleEntity;
import com.uoumei.parser.IParserRegexConstant;
import com.uoumei.util.FileUtil;
import com.uoumei.util.StringUtil;


/**
 * 生成器
 */
@Component
@Scope("prototype")
public class GeneraterCore {

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
	 * 文章解析器
	 */
	@Autowired
	private CmsParser cmsParser;
	

	//--zzq 生成一个主页
	public void generateOneIndex(AppEntity app, String generatePath, String tmpPath, String generateFileName, String tmpFileName){
		String writePath = generatePath + File.separator + generateFileName; 
		// PC端生成
		String htmlContent = FileUtil.readFile(tmpPath + File.separator + tmpFileName); // 读取模版文件内容
		if (!StringUtil.isBlank(htmlContent)) {
			//进行html的解析
			htmlContent = cmsParser.parse(htmlContent,app);
			// 解析HTML上的标签
			FileUtil.writeFile(htmlContent, writePath, FileUtil.URF8);
		}
		
		//手机端生成
		String mobileStyle = app.getAppMobileStyle(); 
		if (!StringUtil.isBlank(mobileStyle)) { // 手机端
			String mobileTmpContent = FileUtil.readFile(tmpPath + File.separator + mobileStyle + File.separator + tmpFileName);// 读取手机端文章模版地址
			if (!StringUtil.isBlank(mobileTmpContent)) {
				FileUtil.createFolder(generatePath + File.separator + mobileStyle);
				writePath = generatePath + File.separator + mobileStyle + File.separator + generateFileName;
				//进行html的解析
				Map map = new HashMap();
				map.put(CmsParser.MOBILE,IParserRegexConstant.MOBILE);
				String mobileHtmlContent = cmsParser.parse(mobileTmpContent,app,map);
				// 解析HTML上的标签
				FileUtil.writeFile(mobileHtmlContent, writePath, FileUtil.URF8);
			}
		}
	}
	
	//--zzq 生成一个栏目
	public void generateOneColumn(ColumnEntity column, AppEntity app, String generatePath, String tmpPath, String url) {

		String mobileStyle = app.getAppMobileStyle();
		String columnPath = null;// pc端
		String mobilePath = null;// 手机端
		
		// 生成列表保存路径
		columnPath = generatePath + File.separator + column.getColumnPath();
		FileUtil.createFolder(columnPath);
		if (!StringUtil.isBlank(mobileStyle)) {
			mobilePath = generatePath + File.separator + mobileStyle + File.separator + column.getColumnPath();
			FileUtil.createFolder(mobilePath);
		}

		// 判断列表类型
		ArticleEntity articleWhere = new ArticleEntity();
		articleWhere.setArticleState(com.uoumei.cms.constant.Const.ARTICLE_STATE_PUB); 
		switch (column.getColumnType()) {
		case ColumnEntity.COLUMN_TYPE_LIST: // 列表
			// 手机列表模版
			if (!StringUtil.isBlank(mobileStyle)) {
				String mobileListTtmpContent = FileUtil.readFile(tmpPath + File.separator + mobileStyle + File.separator + column.getColumnListUrl());
				// 如果模版不为空就进行标签替换
				if (!StringUtil.isBlank(mobileListTtmpContent)) {
					// 生成手机端模版
					// 要生成手机的静态页面数
					int mobilePageSize = cmsParser.getPageSize(app, mobileListTtmpContent, column);
					// 根据页面数,循环生成静态页面个数在
					Map map = new HashMap();
					for (int i = 0; i < mobilePageSize; i++) {
						String writePath = mobilePath + File.separator + IParserRegexConstant.PAGE_LIST + (i + 1) + IParserRegexConstant.HTML_SUFFIX;
						if (i == 0) {
							writePath = mobilePath + File.separator + IParserRegexConstant.HTML_INDEX;
						}
						String pagePath = url + File.separator + mobileStyle + File.separator + column.getColumnPath() + File.separator + IParserRegexConstant.PAGE_LIST ;
						map.put(CmsParser.LIST_LINK_PATH, pagePath);
						map.put(CmsParser.CUR_PAGE_NO, i + 1);
						map.put(CmsParser.MOBILE,IParserRegexConstant.MOBILE);
						String pageContent = cmsParser.parse(mobileListTtmpContent,app,column,map);
						FileUtil.writeFile(pageContent, writePath, FileUtil.URF8);// 写文件
					}
				}
			}

			// 读取列表模版地址
			String listTtmpContent = FileUtil.readFile(tmpPath + File.separator + column.getColumnListUrl());
			// 要生成的静态页面数
			int pageSize = cmsParser.getPageSize(app, listTtmpContent, column);// generaterFactory.getPageSize(app, listTtmpContent, column);
			// 根据页面数,循环生成静态页面个数在
			Map map = new HashMap();
			for (int i = 0; i < pageSize; i++) {
				String writePath = columnPath + File.separator + IParserRegexConstant.PAGE_LIST + (i + 1) + IParserRegexConstant.HTML_SUFFIX;
				if (i == 0) {
					writePath = columnPath + File.separator + IParserRegexConstant.HTML_INDEX;
				}
				//--zzq
//				String pagePath = app.getAppHostUrl() + File.separator + IParserRegexConstant.HTML_SAVE_PATH + File.separator + app.getAppId() + File.separator + column.getColumnPath() + File.separator + "list";
				String pagePath = url + column.getColumnPath() + File.separator + "list";
				map.put(CmsParser.LIST_LINK_PATH, pagePath);
				map.put(CmsParser.CUR_PAGE_NO, i + 1);
				String pageContent = cmsParser.parse(listTtmpContent,app, column,map);
				FileUtil.writeFile(pageContent, writePath, FileUtil.URF8);// 写文件
			}
			break;
		case ColumnEntity.COLUMN_TYPE_COVER:// 单页
			// 取该栏目的最后一篇新闻作为显示内容
			List<ArticleEntity> list = articleBiz.queryListByColumnId(column.getCategoryId(),articleWhere);
			// 手机端
			if (!StringUtil.isBlank(mobileStyle)) {
				String writePath = mobilePath + File.separator + IParserRegexConstant.HTML_INDEX;
				// 读取封面模板内容
				String coverTtmpContent = FileUtil.readFile(tmpPath + File.separator + mobileStyle + File.separator + column.getColumnUrl());
				// 如果模版不为空就进行标签替换
				if (!StringUtil.isBlank(coverTtmpContent)) {
					map = new HashMap();
					map.put(CmsParser.MOBILE,IParserRegexConstant.MOBILE);
					// 文章地址前缀
					// 表示该栏目下面没有文章
					if (list == null || list.size() == 0) {
						String coverContent = cmsParser.parse(coverTtmpContent,app,column,map); //generaterFactory.builder(app, column, coverTtmpContent, tmpPath, mobileStyle); // 解析标签
						FileUtil.writeFile(coverContent, writePath, FileUtil.URF8);// 写文件
						break;
					}
					ArticleEntity article = list.get(0);// 取一篇文章作为封面栏目的内容
					article.setArticleLinkURL(url + File.separator + mobileStyle + column.getColumnPath() + File.separator + IParserRegexConstant.HTML_INDEX);

					String coverContent =  cmsParser.parse(coverTtmpContent,app,column,article,map);//generaterFactory.builderArticle(app, column, article, coverTtmpContent, tmpPath, null, null, mobileStyle); // 解析标签
					// 取最后一篇文章作为栏目内容
					FileUtil.writeFile(coverContent, writePath, FileUtil.URF8);// 写文件
				}
			}

			String writePath = columnPath + File.separator + IParserRegexConstant.HTML_INDEX;
			// 读取封面模板内容
			String coverTtmpContent = FileUtil.readFile(tmpPath + File.separator + column.getColumnUrl());
			// 文章地址前缀
			// 表示该栏目下面没有文章
			if (list == null || list.size() == 0) {
				String coverContent = cmsParser.parse(coverTtmpContent,app,column);//generaterFactory.builder(app, column, coverTtmpContent, tmpPath); // 解析标签
				FileUtil.writeFile(coverContent, writePath, FileUtil.URF8);// 写文件
				break;
			}
			ArticleEntity article = list.get(0);// 取一篇文章作为封面栏目的内容
			article.setArticleLinkURL(url + File.separator + column.getColumnPath() + File.separator + IParserRegexConstant.HTML_INDEX);

			String coverContent = cmsParser.parse(coverTtmpContent,app,column,article);//generaterFactory.builderArticle(app, column, article, coverTtmpContent, tmpPath, null, null); // 解析标签
																																// 取最后一篇文章作为栏目内容
			FileUtil.writeFile(coverContent, writePath, FileUtil.URF8);// 写文件
			break;
		}
	}

	//--zzq 生成一个文章
	public void generateOneArticle(ArticleEntity article, ColumnEntity column, AppEntity app, String generatePath, String tmpPath, String url) {

		String mobileStyle = app.getAppMobileStyle();
		
		//读取模版内容
		String tmpContent = FileUtil.readFile(tmpPath + File.separator + column.getColumnUrl());// 读取文章模版地址
		String mobileTmpContent = null;
		if (!StringUtil.isBlank(mobileStyle)) {
			mobileTmpContent = FileUtil.readFile(tmpPath + File.separator + mobileStyle + File.separator + column.getColumnUrl());// 读取手机端文章模版地址
		}
		//生成文章所在路径
		FileUtil.createFolder(generatePath + column.getColumnPath());
		if (!StringUtil.isBlank(mobileTmpContent)) {
			FileUtil.createFolder(generatePath + File.separator + mobileStyle + File.separator + column.getColumnPath());
		}
		
		String writePath = null;
		
		// 生成文档
		switch (column.getColumnType()) {
		case ColumnEntity.COLUMN_TYPE_LIST: // 列表
			writePath = generatePath + column.getColumnPath() + File.separator + article.getArticleID() + IParserRegexConstant.HTML_SUFFIX;
			article.setArticleLinkURL(url + column.getColumnPath() + File.separator + article.getArticleID() + IParserRegexConstant.HTML_SUFFIX);

			ArticleEntity previous = articleBiz.getPrevious(app.getAppId(), article.getArticleID(),article.getBasicCategoryId());// 上一篇文章
			ArticleEntity next = articleBiz.getNext(app.getAppId(), article.getArticleID(),article.getBasicCategoryId());// 下一篇文章
			if(article.getColumn()!=null){
				if (previous != null) {
					previous.setArticleLinkURL(url + article.getColumn().getColumnPath() + File.separator + previous.getArticleID() + IParserRegexConstant.HTML_SUFFIX);
				}
				if (next != null) {
					next.setArticleLinkURL(url +  article.getColumn().getColumnPath() + File.separator + next.getArticleID() + IParserRegexConstant.HTML_SUFFIX);
				}
			}
			Map map = new HashMap();
			map.put(CmsParser.PREVIOUS, previous);
			map.put(CmsParser.NEXT, next);
			
			String content =  cmsParser.parse(tmpContent,app,column,article,map);
			FileUtil.writeFile(content, writePath, FileUtil.URF8);// 写文件

			// 手机端
			if (!StringUtil.isBlank(mobileTmpContent)) {
				
				writePath = generatePath + File.separator + mobileStyle + File.separator + column.getColumnPath() + File.separator + article.getArticleID() + IParserRegexConstant.HTML_SUFFIX;
				article.setArticleLinkURL(url + File.separator + mobileStyle + File.separator + column.getColumnPath() + File.separator + article.getArticleID() + IParserRegexConstant.HTML_SUFFIX);

				if(article.getColumn()!=null){
					if (previous != null) {
						previous.setArticleLinkURL(url + File.separator + mobileStyle + File.separator + article.getColumn().getColumnPath() + File.separator + previous.getArticleID() + IParserRegexConstant.HTML_SUFFIX);
					}
					if (next != null) {
						next.setArticleLinkURL(url + File.separator + mobileStyle + File.separator + article.getColumn().getColumnPath() + File.separator + next.getArticleID() + IParserRegexConstant.HTML_SUFFIX);
					}
				}
				map.put(CmsParser.MOBILE,IParserRegexConstant.MOBILE);
				String tmp = cmsParser.parse(mobileTmpContent,app,column,article,map);//;generaterFactory.builderArticle(app, tempColumn, article, mobileTmpContent, tmpPath, previous, next, mobileStyle); // 解析标签
				FileUtil.writeFile(tmp, writePath, FileUtil.URF8);// 写文件
			}
			break;
		case ColumnEntity.COLUMN_TYPE_COVER:// 单页
			writePath = generatePath + column.getColumnPath() + File.separator + IParserRegexConstant.HTML_INDEX;
			article.setArticleLinkURL(url + column.getColumnPath() + File.separator + IParserRegexConstant.HTML_INDEX);

			String coverContent = cmsParser.parse(tmpContent,app,column,article);//generaterFactory.builderArticle(app, column, article, coverTtmpContent, tmpPath, null, null); // 解析标签
			FileUtil.writeFile(coverContent, writePath, FileUtil.URF8);// 写文件
			
			// 手机端
			if (!StringUtil.isBlank(mobileTmpContent)) {
				writePath = generatePath + File.separator + mobileStyle + File.separator + column.getColumnPath() + File.separator + IParserRegexConstant.HTML_INDEX;
				
				Map covermap = new HashMap();
				covermap.put(CmsParser.MOBILE,IParserRegexConstant.MOBILE);

				article.setArticleLinkURL(url + File.separator + mobileStyle + column.getColumnPath() + File.separator + IParserRegexConstant.HTML_INDEX);

				String mobileCoverContent =  cmsParser.parse(mobileTmpContent,app,column,article,covermap);//generaterFactory.builderArticle(app, column, article, coverTtmpContent, tmpPath, null, null, mobileStyle); // 解析标签
				// 取最后一篇文章作为栏目内容
				FileUtil.writeFile(mobileCoverContent, writePath, FileUtil.URF8);// 写文件
			}
			break;
		}
	}
}