
package com.uoumei.cms.parser.impl;

import com.uoumei.parser.IParser;

/**
 * 解析文章关键字标签{ms:field.keyword/}
 */
public class ArticleKeywordParser extends IParser{
	
	/**
	 * 文章关键字标签
	 */
	private final static String ARTICLE_KEYWORD_FIELD="\\{ms:field.keyword/\\}";
	
	/**
	 * 构造标签的属性
	 * @param htmlContent原HTML代码
	 * @param newContent替换的内容
	 */
	public ArticleKeywordParser(String htmlContent,String newContent){
		super.htmlCotent = htmlContent;
		super.newCotent = newContent;
	}
	
	@Override
	public String parse() {
		// TODO Auto-generated method stub
		return super.replaceAll(ARTICLE_KEYWORD_FIELD);
	}
}