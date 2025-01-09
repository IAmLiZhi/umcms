package com.uoumei.basic.constant.e;

import com.uoumei.base.constant.e.BaseEnum;

/**
 * 是否是菜单枚举类
 */
public enum ModelIsMenuEnum implements BaseEnum{
	/**
	 * 是否是菜单1非菜单
	 */
	MODEL_NOTMENU(1,"否"),
	
	/**
	 * 是否是菜单0菜单
	 */
	MODEL_MEUN(0,"是");
	
	private String code;
	
	private int id;

	/**
	 * 构造方法
	 * @param id 默认ID
	 * @param code 传入的枚举类型
	 */
	ModelIsMenuEnum(int id,String code) {
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
