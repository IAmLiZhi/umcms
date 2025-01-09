
package com.uoumei.parser.impl.general;

import com.uoumei.parser.IParser;

/**
 * 网站主机地址
 * 
 */
public class GlobalHostParser extends IParser {
	
	/**
	 * 网站路径标签，单标签，主要用于提交列表等HTML的路径到相应的action中 网站全局标签 {ms:global.url/}
	 */
	private final static String GLOBAL_HOST="\\{ms:global.host/\\}";
	
	/**
	 * 构造标签的属性
	 * @param htmlContent原HTML代码
	 * @param newContent替换的内容
	 */
	public GlobalHostParser(String htmlContent,String newContent){
		super.htmlCotent = htmlContent;
		super.newCotent = newContent;
	}
	
	@Override
	public String parse() {
		return super.replaceAll(GLOBAL_HOST);
	}

}