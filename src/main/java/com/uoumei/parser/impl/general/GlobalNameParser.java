
package com.uoumei.parser.impl.general;

import com.uoumei.parser.IParser;
/**
 * 解析网站名称标签(单标签)
 * 网站全局标签
 *  {ms:global.name /}
 */
public class GlobalNameParser extends IParser {
	
	/**
	 * 网站名称 网站全局标签 {ms:global.name /}
	 */
	private final static String GLOBAL_NAME="\\{ms:global.name/\\}";

	/**
	 * 构造标签的属性
	 * @param htmlContent原HTML代码
	 * @param newContent替换的内容
	 */
	public GlobalNameParser(String htmlContent,String newContent){
		super.htmlCotent = htmlContent;
		super.newCotent = newContent;
	}
	
	@Override
	public String parse() {
		// TODO Auto-generated method stub
		return super.replaceAll(GLOBAL_NAME);
	}

}