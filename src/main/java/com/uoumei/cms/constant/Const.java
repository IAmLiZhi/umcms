
package com.uoumei.cms.constant;

import java.util.ResourceBundle;


/**
 * 常量定义
 */
public final class Const {
	//--zzq
	public static final int ARTICLE_STATE_EDIT = 0;
	public static final int ARTICLE_STATE_AUDIT = 1;
	public static final int ARTICLE_STATE_PASS = 2;
	public static final int ARTICLE_STATE_NOPASS = 3;
	public static final int ARTICLE_STATE_WAIT = 4;
	public static final int ARTICLE_STATE_PUB = 5;
	
	/**
	 * 文章类型属性
	 */
	public static final ResourceBundle ARTICLE_ATTRIBUTE_RESOURCE = ResourceBundle.getBundle("com.uoumei.cms.resources.article_attribute");
	
	public static final String LOGIN_URL = "login_url";
}