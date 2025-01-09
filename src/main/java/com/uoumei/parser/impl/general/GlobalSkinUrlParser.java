
package com.uoumei.parser.impl.general;

import com.uoumei.parser.IParser;

/**
 * 模版路径标签 ,单标签，主要用语引入CSS,js等文件(单标签)
 * 网站全局标签
 *  {ms: globalskin.url /}
 */
public class GlobalSkinUrlParser extends IParser {
	
	/**
	 * 模版路径标签 ,单标签，主要用语引入CSS,js等文件4 网站全局标签 {ms:globalskin.url /}
	 */
	private final static String GLOBAL_SKIN="\\{ms:globalskin.url/\\}";
	
	/**
	 * 构造标签的属性
	 * @param htmlContent原HTML代码
	 * @param newContent替换的内容
	 */
	public GlobalSkinUrlParser(String htmlContent,String newContent){
		super.htmlCotent = htmlContent;
		super.newCotent = newContent;
	}
	
	@Override
	public String parse() {
		// TODO Auto-generated method stub
		return super.replaceAll(GLOBAL_SKIN);
	}

}