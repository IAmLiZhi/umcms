
package com.uoumei.cms.parser.impl;

import com.uoumei.parser.IParser;

/**
 * 文章内容(单标签)
 */
public class ArticleContentParser extends IParser {
	
	/**
	 * 文章内容标签
	 */
	private final static String ARTICLE_CONTENT_FIELD="\\{ms:field.content/\\}";
	
	/**
	 * 构造标签的属性
	 * @param htmlContent原HTML代码
	 * @param newContent替换的内容
	 */
	public ArticleContentParser(String htmlContent,String newContent){
		super.htmlCotent = htmlContent;
		super.newCotent = newContent;
	}
	
	@Override
	public String parse() {
		// TODO Auto-generated method stub
		return super.replaceAll(ARTICLE_CONTENT_FIELD);
	}

}