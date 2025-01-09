
package com.uoumei.basic.constant.e;

import com.uoumei.base.constant.e.BaseCookieEnum;

/**
 * cookie枚举类
 */
public enum CookieConstEnum implements BaseCookieEnum{
	
	/**
	 * 分页cookie
	 */
	PAGENO_COOKIE("pageno_cookie"),
	
	/**
	 *上次访问地址
	 */
	BACK_COOKIE("back_cookie"),
	
	/**
	 * QQ登录保存当前用户点击地址的session
	 */
	BASIC_HIT("basic_hit");
	
	/**
	 * 设置CookieConst的常量
	 * @param attr 常量
	 */
	CookieConstEnum(String attr) {
		this.attr = attr;
	}
	
	private String attr;

	/**
	 * 返回该CookieConst常量的字符串表示
	 * @return 字符串
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return attr;
	}
}