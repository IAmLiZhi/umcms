
package com.uoumei.cms.parser.impl;

import com.uoumei.parser.IParser;

/**
 * 内容标题(单标签)   
 */
public class HitParser extends IParser {
	
	/**
	 * 文章点击量标签
	 */
	private final static String ARTICLE_HIT_FIELD="\\{ms:field.hit/\\}";
	
	
	/**
	 * 构造标签的属性
	 * @param htmlContent原HTML代码
	 * @param newContent替换的内容
	 */
	public HitParser(String htmlContent,String newContent){
		super.htmlCotent = htmlContent;
		super.newCotent = newContent;
	}
	
	@Override
	public String parse() {
		// TODO Auto-generated method stub
		return super.replaceAll(ARTICLE_HIT_FIELD);
	}

}