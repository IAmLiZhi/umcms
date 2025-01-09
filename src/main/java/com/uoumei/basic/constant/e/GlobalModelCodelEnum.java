package com.uoumei.basic.constant.e;


import com.uoumei.base.constant.e.BaseEnum;

/**
 * 全局枚举类
 */
public enum GlobalModelCodelEnum implements BaseEnum{
	
	CITY("10990000");
	
	
	/**
	 * 设置CookieConst的常量
	 * @param attr 常量
	 */
	GlobalModelCodelEnum(String code) {
		this.code = code;
	}
	
	private String code;
	

	/**
	 * 返回该CookieConst常量的字符串表示
	 * @return 字符串
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return code;
	}


	@Override
	public int toInt() {
		return 0;
	}

	
}
