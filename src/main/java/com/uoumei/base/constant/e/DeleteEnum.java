
package com.uoumei.base.constant.e;

import com.uoumei.base.constant.e.BaseEnum;

public enum DeleteEnum implements BaseEnum{
	/**
	 * 伪删除（DEL已删除,值为1）
	 */
	DEL(1,"已删除"), 
	
	/**
	 * 伪删除（NOTDEL正常,值为0）
	 */
	NOTDEL(0,"正常");
	
	private String code;
	
	private int id;

	/**
	 * 构造方法
	 * @param id 默认ID
	 * @param code 传入的枚举类型
	 */
	DeleteEnum(int id,String code) {
		this.code = code;
		this.id = id;
	}

	@Override
	public int toInt() {
		return this.id;
	}

	@Override
	public String toString() {
		return this.code.toString();
	}
}