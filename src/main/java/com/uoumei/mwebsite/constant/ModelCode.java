
package com.uoumei.mwebsite.constant;

import com.uoumei.base.constant.e.BaseEnum;


public  enum ModelCode implements BaseEnum{
	
	/**
	 * 应用管理
	 */
	APP_MANAGEMENT("00000002"),
	
	/**
	 * 模块管理
	 */
	MODULE_MANAGEMENT("00000001")
	;

	ModelCode(String code) {
		this.code = code;
	}

	private String code;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return code;
	}

	public int toInt() {
		// TODO Auto-generated method stub
		return Integer.parseInt(code);
	}

}
