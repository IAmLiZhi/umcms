
package com.uoumei.basic.constant;

import java.util.ResourceBundle;
import org.springframework.context.ApplicationContext;

/**
 * 基础枚举类
 * 
 */
public final class Const {
	/**
	 * action层对应的国际化资源文件
	 */
	public final static ResourceBundle RESOURCES = ResourceBundle.getBundle("com.uoumei.basic.resources.resources");

	/**
	 * 默认系统管理员所对应的角色ID为1
	 */
	public final static int DEFAULT_SYSTEM_MANGER_ROLE_ID = 1;

	/**
	 * 默认站点管理员所对应的角色ID为2
	 */
	public final static int DEFAULT_WEBSITE_MANGER_ROLE_ID = 2;

	/**
	 * 默认CMS所对应的模块ID为1
	 */
	public final static int DEFAULT_CMS_MODEL_ID = 1;

	/**
	 * 顶级栏目的父栏目ID为0
	 */
	public final static int COLUMN_TOP_CATEGORY_ID = 0;

	/**
	 * 后台文件存放文件夹，通过拦截器赋值
	 * @see com.uoumei.basic.interceptor.ActionInterceptor
	 */
	public  static String VIEW = "";
	
	/**
	 * 上传路径地址，通过拦截器赋值
	 * @see com.uoumei.basic.interceptor.ActionInterceptor
	 */
	public  static String UPLOAD_PATH = "";
	//--zzq
	public  static String UPLOAD_SERVER_URL = "";
	
}