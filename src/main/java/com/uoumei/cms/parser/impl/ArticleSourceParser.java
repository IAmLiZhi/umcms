
package com.uoumei.cms.parser.impl;

import com.uoumei.parser.IParser;
/**
 * 内容发布来源(单标签)
 */
public class ArticleSourceParser extends IParser {
	
	/**
	 * 文章来源标签
	 */
	private final static String ARTICLE_SOURCE_FIELD="\\{ms:field.source/\\}";
	
	/**
	 * 构造标签的属性
	 * @param htmlContent原HTML代码
	 * @param newContent替换的内容
	 */
	public ArticleSourceParser(String htmlContent,String newContent){
		super.htmlCotent = htmlContent;
		super.newCotent = newContent;
	}
	
	@Override
	public String parse() {
		// TODO Auto-generated method stub
		return super.replaceAll(ARTICLE_SOURCE_FIELD);
	}

}