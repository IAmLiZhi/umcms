
package com.uoumei.parser.impl.general;

import com.uoumei.parser.IParser;

/**
 * 网站版权信息,网站标签(单标签)
 * 网站全局标签
 *  {ms: global.copyright/}
 */
public class GlobalCopyrightParser extends IParser {
	
	/**
	 *  网站版权信息,网站标签 网站全局标签 {ms: global.copyright/}
	 */
	private final static String GLOBAL_COPYRIGHT="\\{ms:global.copyright/\\}";
	
	/**
	 * 构造标签的属性
	 * @param htmlContent原HTML代码
	 * @param newContent替换的内容
	 */
	public GlobalCopyrightParser(String htmlContent,String newContent){
		super.htmlCotent = htmlContent;
		super.newCotent = newContent;
	}
	
	@Override
	public String parse() {
		// TODO Auto-generated method stub
		return super.replaceAll(GLOBAL_COPYRIGHT);
	}

}