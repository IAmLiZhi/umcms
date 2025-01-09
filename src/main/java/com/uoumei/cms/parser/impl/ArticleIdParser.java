
package com.uoumei.cms.parser.impl;

import com.uoumei.parser.IParser;

/**
 * 内容标题(单标签)
 */
public class ArticleIdParser extends IParser {
	
	/**
	 * 文章id标签
	 */
	private final static String ARTICLE_TITLE_FIELD="\\{ms:field.id/\\}";
	
	/**
	 * 构造标签的属性
	 * @param htmlContent原HTML代码
	 * @param newContent替换的内容
	 */
	public ArticleIdParser(String htmlContent,String newContent){
		super.htmlCotent = htmlContent;
		super.newCotent = newContent;
	}
	
	@Override
	public String parse() {
		// TODO Auto-generated method stub
		return super.replaceAll(ARTICLE_TITLE_FIELD);
	}

}