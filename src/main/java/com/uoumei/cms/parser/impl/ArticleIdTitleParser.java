
package com.uoumei.cms.parser.impl;



import com.uoumei.parser.IParser;
import com.uoumei.util.StringUtil;
/**
 * 文章所在的栏目名称(单标签)
 */
public class ArticleIdTitleParser extends IParser{
	
	/**
	 * 文章所属栏目标题标签
	 */
	private final static String ARTICLE_TYPETITLE ="\\{ms:field.typetitle(.*)?/\\}";
	
	private  final static String TYPE = "type";
	
	private final static String TYPE_TOP = "top";
	
	/**
	 * 构造标签的属性
	 * @param htmlContent原HTML代码
	 * @param newContent替换的内容
	 */
	public ArticleIdTitleParser(String htmlContent,String newContent){
		super.htmlCotent = htmlContent;
		super.newCotent = newContent;
	}
	@Override
	public String parse() {
		// TODO Auto-generated method stub
		return super.replaceAll(ARTICLE_TYPETITLE);
	}
	
	/**
	 *  是否存在type=top的属性
	 * @return　true:存在 false:不存在
	 */
	public boolean  isTop() {
		String temp  = super.getProperty(ARTICLE_TYPETITLE).get(TYPE);
		if (StringUtil.isBlank(temp)) {
			return false;
		} else {
			return temp.equalsIgnoreCase(TYPE_TOP) ;
		}
	}
	

}