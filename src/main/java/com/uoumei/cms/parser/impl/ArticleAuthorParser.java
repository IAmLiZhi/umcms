package com.uoumei.cms.parser.impl;

import com.uoumei.parser.IParser;

/**
 * 内容发布作者(单标签) 文章作者标签
 */
public class ArticleAuthorParser extends IParser {
	
	/**
	 * 文章作者标签
	 */
	private final static String ARTICLE_AUTHOR_FIELD="\\{ms:field.author/\\}";
	
	/**
	 * 构造标签的属性
	 * @param htmlContent原HTML代码
	 * @param newContent替换的内容
	 */
	public ArticleAuthorParser(String htmlContent,String newContent){
		super.htmlCotent = htmlContent;
		super.newCotent = newContent;
	}
	
	@Override
	public String parse() {
		// TODO Auto-generated method stub
		return super.replaceAll(ARTICLE_AUTHOR_FIELD);
	}

}