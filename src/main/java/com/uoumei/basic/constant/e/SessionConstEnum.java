
package com.uoumei.basic.constant.e;

import com.uoumei.base.constant.e.BaseSessionEnum;

/**
 * session枚举类
 * 
 */
public enum SessionConstEnum implements BaseSessionEnum {

	/**
	 * 模块idsession
	 */
	MODEL_ID_SESSION("model_id_session"),

	/**
	 * 模块名称
	 */
	MODEL_TITLE_SESSION("model_title_session"),

	/**
	 * 模块英文名称
	 */
	MODEL_NAME_SESSION("model_name_session"),

	/**
	 * 普通管理员的sesison
	 */
	MANAGER_SESSION("manager_session"),

	/**
	 * 验证码session
	 */
	CODE_SESSION("rand_code"),

	/**
	 * 普通管理员角色菜单的sesison
	 */
	MANAGER_ROLE_MODEL_ESSION("manager_role_model_session"),

	/**
	 * 模块编号
	 */
	MANAGER_MODEL_CODE("manager_model_code"),
	/**
	 * 错误信息
	 */
	EXCEPTOIN("ms_exception");

	/**
	 * 设置session常量
	 * 
	 * @param attr
	 *            常量
	 */
	SessionConstEnum(String attr) {
		this.attr = attr;
	}

	private String attr;

	/**
	 * 返回SessionConst常量的字符串表示
	 * 
	 * @return 字符串
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return attr;
	}

}