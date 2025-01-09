package com.uoumei.mwebsite.constant;

import java.util.ResourceBundle;
import org.springframework.context.ApplicationContext;

/**
 * 基础枚举类
 */
public final class Const {

	/**
	 * spring资源文件加载上下文对象
	 */
	public static ApplicationContext CONTEXT;

	/**
	 * action层对应的国际化资源文件
	 */
	public final static ResourceBundle RESOURCES = ResourceBundle
			.getBundle("com.uoumei.mwebsite.resources.resources");

}
