
package com.uoumei.cms.parser.impl;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.uoumei.base.entity.BaseEntity;
import com.uoumei.basic.entity.AppEntity;
import com.uoumei.basic.entity.ColumnEntity;
import com.uoumei.cms.constant.e.ColumnTypeEnum;
import com.uoumei.cms.entity.ArticleEntity;
import com.uoumei.mdiy.biz.IContentModelBiz;
import com.uoumei.mdiy.biz.IContentModelFieldBiz;
import com.uoumei.mdiy.entity.ContentModelEntity;
import com.uoumei.mdiy.entity.ContentModelFieldEntity;
import com.uoumei.parser.IParserRegexConstant;
import com.uoumei.parser.impl.general.DateParser;
import com.uoumei.util.RegexUtil;
import com.uoumei.util.StringUtil;

/**
 * 
 * 解析列表标签, {ms:arclist typeid= size= titlelen= flag = }:列表头标签,<br/>
 * {/ms:arclist}:列表尾标签,<br/>
 * 列表中的属性：<br/>
 * typeid 类型 int栏目ID,在列表模板和档案模板中一般不需要指定，在首页模板中允许用","分开表示多个栏目,<br/>
 * size 类型 int 返回文档列表总数,默认为20条全部返回，也可以配合分页使用,<br/>
 * titlelen 类型 int 标题长度,等同于titlelength。默认40个汉字,<br/>
 * flag 类型 String flag = c 自定义属性值：推荐[c]幻灯[f],<br/>
 * [field.index/]:序号,根据显示条数显示的序号1 2 …..10,<br/>
 * [field.id/]:编号,对应文章在数据库里的自动编号,<br/>
 * [field.title/]:标题,标题长度根据titlelen的属性值指定，默认40个汉字,<br/>
 * [field.fulltitle/]:全部标题,显示完整的标题,<br/>
 * [field.author/]:作者,<br/>
 * [field.source/]:来源,<br/>
 * [field.date fmt=”yyyy-mm-dd”/]:时间(非必填),<br/>
 * [field.content length=/]:内容,length=:内容的长度,指定获取文章长度(非必填),<br/>
 * [field.typename/]:分类名称，文章所属分类的名称,<br/>
 * [field.typeid/]:分类编号,文章所属分类的编号,<br/>
 * [field.typelink/]:分类连接,点击连接连接到当前分类的列表,<br/>
 * [field.link/]:内容链接,点击显示文章具体的内容地址,<br/>
 * [field.hit/]:信息点击预览数,<br/>
 * 
 */
public class ListParser extends com.uoumei.mdiy.parser.ListParser {
	
	protected static final String FIELD_HIT = "\\[field.hit/\\]";
	
	/**
	 * 文章关键字,文章所属分类的编号 文章列表子标签 [field.keyword/]
	 */
	protected final static String FIELD_KEY_WORD="\\[field.keyword/\\]";
	
	/**
	 * 文章flag标签,显示文章的属性
	 */
	
	protected final static String FIELD_FLAG="\\[field.flag/\\]";
	
	
	/**
	 * 新增字段业务层
	 */
	protected IContentModelFieldBiz fieldBiz;
	
	
	protected IContentModelBiz contentBiz;
	
	protected AppEntity app;
	
	/**
	 * 列表解析构造,
	 * 
	 * @param htmlCotent
	 *            　原始内容
	 * @param articles
	 *            　文章列表
	 * @param websiteUrl
	 *            　网站地址
	 * @param isPaging
	 *            ture:分页
	 * @param listProperty
	 *            　当前标签属性
	 */
	public ListParser(AppEntity app,String htmlCotent, List<ArticleEntity> articles,String websiteUrl, Map<String, String> listProperty, boolean isPaging,IContentModelFieldBiz fieldBiz,IContentModelBiz contentBiz) {
		this.app = app;
		String tabTmpContent = "";
		if (isPaging) {
			// 在HTML模版中标记出要用内容替换的标签
			tabTmpContent = replaceStartAndEnd(htmlCotent,LIST_PAGING);
		} else {
			tabTmpContent = replaceStartAndEnd(htmlCotent,LIST_NOPAGING);
		}
		this.listProperty = listProperty;
		this.fieldBiz = fieldBiz;
		this.contentBiz = contentBiz;
		// 经过遍历后的数组标签
		super.newCotent = list(tabTmpContent, htmlCotent, articles, websiteUrl);
		super.htmlCotent = tabTmpContent;
		
	}

	
	
	/**
	 * 遍历文章数组，将取出的内容替换标签
	 * 
	 * @param tabHtmlContent
	 *            替换过的html模版
	 * @param htmlContent
	 *            原始htlm模版内容
	 * @param articleList
	 *            文章数组
	 * @param path
	 *            项目路径
	 * @return 用内容替换标签后的HTML代码
	 */
	@Override
	protected String list(String tabHtmlContent, String htmlContent, List articleList, String path) {
		String htmlList = "";
		String tabHtml = "";
		tabHtml = tabHtml(tabHtmlContent);
		List<String> parserHtml = new ArrayList<String>();
		int countNoParser = 0;
		if (articleList != null && tabHtml != null && articleList.size() != 0 && tabHtml.length() != 0) {
			for (int i = 0; i < articleList.size(); i++) {
				
				ArticleEntity article = (ArticleEntity)articleList.get(i);
				if(article.getColumn()!=null){
					// 序号,根据显示条数显示的序号1 2 …..10。
					htmlList += tabContent(tabHtml, StringUtil.int2String((i + 1)),INDEX_FIELD_LIST);
					// 文章内容,
					htmlList =  tabContent(htmlList, contentLength(article.getArticleContent(), htmlList),CONTENT_FIELD_LIST);
					countNoParser = NoParser.countParser(htmlList);
					NoParser noParser = new NoParser(htmlList);
					
					if(countNoParser>0){
						parserHtml = noParser.getNoParserHtml(countNoParser,parserHtml);
					}
					htmlList = noParser.parse();
					// 编号,对应文章在数据库里的自动编号。
					htmlList = tabContent(htmlList, StringUtil.int2String(article.getBasicId()),ID_FIELD_LIST);
					// 标题,标题长度根据titlelen的属性值指定，默认40个汉字,
					htmlList = tabContent(htmlList, titleLength(article.getBasicTitle(), htmlContent),TITLE_FIELD_LIST);
					// 全部标题,显示完整的标题。
					htmlList = tabContent(htmlList, StringUtil.null2String(article.getBasicTitle()),FULLTITLE_FIELD_LIST);
					// 文章作者。
					htmlList = tabContent(htmlList, StringUtil.null2String(article.getArticleAuthor()),AUTHOR_FIELD_LIST);
					// 文章来源。
					htmlList = tabContent(htmlList, StringUtil.null2String(article.getArticleSource()),SOURCE_FIELD_LIST);
					// 文章点击数。
					htmlList = tabContent(htmlList, article.getBasicHit(),FIELD_HIT);
					// 文章发布时间(非必填),
					htmlList = new DateParser(htmlList,article.getBasicDateTime()).parse();//tabContent(htmlList, date(article.getBasicUpdateTime(), htmlList),DATE_FIELD_LIST);
					// 文章关键字
					htmlList = tabContent(htmlList, article.getArticleKeyword(),FIELD_KEY_WORD);
					// 文章flag属性
					String articleType = article.getArticleType();
					
					if(!StringUtils.isEmpty(articleType)){
						String[] flags = articleType.split(",");
						String flagStr = "";
						for(int j=0;j<flags.length;j++){
							try {
								String flag = com.uoumei.cms.constant.Const.ARTICLE_ATTRIBUTE_RESOURCE.getString(flags[j]);
								if(j == 0){
									flagStr = flag;
								}else{
									flagStr = flagStr + "," + flag;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						htmlList = tabContent(htmlList, flagStr,FIELD_FLAG);
					}else{
						htmlList = tabContent(htmlList, articleType,FIELD_FLAG);
					}
					// 分类名称，文章所属分类的名称,
					htmlList = tabContent(htmlList, StringUtil.null2String(article.getColumn().getCategoryTitle()),TYPENAME_FIELD_LIST);
					// 文章链接 ：[field.link/]
//					String link = path + StringUtil.null2String(article.getColumn().getColumnPath()) + File.separator + article.getBasicId() + IParserRegexConstant.HTML_SUFFIX;
//					//判断文章的类型是单页还是列表
//					if(article.getColumn().getColumnType()==ColumnTypeEnum.COLUMN_TYPE_COVER.toInt()){
//						link =path + StringUtil.null2String(article.getColumn().getColumnPath()) + File.separator + IParserRegexConstant.HTML_INDEX;
//					}
					//--zzq
					String link = StringUtil.null2String(article.getArticleUrl());
					link = link.trim();
//					if(link.length()>0){
//						char ch = link.charAt(0);
//						if(ch == '\\' || ch == '/')
//							link = path + link;
//					}
					if(!RegexUtil.isRightUrl(link)){
						link = path + link;
					}
					
					htmlList = tabContent(htmlList, link,LINK_FIELD_LIST);
					// 分类编号,文章所属分类的编号,
					htmlList = tabContent(htmlList, article.getBasicCategoryId(),TYPEID_FIELD_LIST);
					// 文章略图[field.litpic/]
					htmlList = tabContent(htmlList, StringUtil.null2String(article.getBasicThumbnails()),LITPIC_FIELD_LIST);
					// 文章描述标签[field.descrip/]
					htmlList = tabContent(htmlList, StringUtil.null2String(article.getBasicDescription()),DESCIRIP_FIELD_LIST);
					// 当前页面文章的数量[field.num]
					String numArticle = Integer.toString(articleList.size());
					htmlList = tabContent(htmlList, numArticle,NUM_ARTICLE_LIST);
					//分类连接：[field.typelink/]	点击连接连接到当前分类的列表
					String channelLink = path+StringUtil.null2String(article.getColumn().getColumnPath())+File.separator+ IParserRegexConstant.HTML_INDEX;
					htmlList = tabContent(htmlList, channelLink,TTYPELINK_FIELD_LIST);
					//对自定义字段进行替换
					htmlList = replaceField(htmlList,article.getColumn(),article.getBasicId());
				}
			}
		}
		if(countNoParser>0){
			NoParser noParser = new NoParser(htmlList);
			htmlList =noParser.parse(htmlList,parserHtml);
		}
		
		return htmlList;
	}
	

	/**
	 * 替换自定义字段
	 * @param column 栏目id
	 * @param basicId 实体id
	 * @return
	 */
	protected String  replaceField(String htmlList,ColumnEntity column,int basicId){
		
		//判断该文章是否有扩展字段
		if(column.getColumnContentModelId()!=0){
			// 根据表单类型id查找出所有的字段信息
			List<BaseEntity> listField = fieldBiz.queryListByCmid(column.getColumnContentModelId());
			//遍历所有的字段实体,得到字段名列表信息
			List<String> listFieldName = new ArrayList<String>();
			for(int j = 0;j<listField.size();j++){
				ContentModelFieldEntity field = (ContentModelFieldEntity) listField.get(j);
				listFieldName.add(field.getFieldFieldName());
			}
			ContentModelEntity contentModel = (ContentModelEntity) contentBiz.getEntity(column.getColumnContentModelId());
			// 组织where条件
			Map<String, Integer> where = new HashMap<String, Integer>();
			where.put("basicId",basicId);
			// 获取各字段的值
			List fieldLists = fieldBiz.queryBySQL(contentModel.getCmTableName(), listFieldName, where);
		
			if(fieldLists!=null && fieldLists.size()>0){
				Map fields = (Map)fieldLists.get(0);
				//计算标签的个数
				int taglibNum = count(htmlList,TAGLIB_ARTICLE_LIST);
				while(taglibNum!=0){
					//String newCotent=taglibContentParser(htmlList,fields,contentModel.getCmId());
					String newCotent= taglibContentParser(listField,htmlList,fields,contentModel.getCmId());
					// 将取出的内容替换标签
					htmlList = replaceFirst(newCotent,TAGLIB_ARTICLE_LIST,htmlList);
					taglibNum = count(htmlList,TAGLIB_ARTICLE_LIST);
				}
			}
		}else{
			Pattern patternL = Pattern.compile(TAGLIB_ARTICLE_LIST);
			Matcher matcherL = patternL.matcher(htmlList);
			if (matcherL.find()){
				//查找出用户填写的自定义标签字段名
				htmlList = tabContent(htmlList, "",TAGLIB_ARTICLE_LIST);
			}
		}
		return htmlList;
	}

}