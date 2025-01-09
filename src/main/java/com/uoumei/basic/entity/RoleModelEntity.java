
package com.uoumei.basic.entity;

import com.uoumei.base.entity.BaseEntity;

/**
 * 角色与模块关联表
 */
public class RoleModelEntity extends BaseEntity {

	/**
	 * 模块编号
	 */
	private int modelId;
	
	/**
	 * 角色编号
	 */
	private int roleId;

	/**
	 *获取 modelId
	 * @return modelId
	 */
	public int getModelId() {
		return modelId;
	}

	/**
	 *设置modelId
	 * @param modelId
	 */
	public void setModelId(int modelId) {
		this.modelId = modelId;
	}

	/**
	 *获取 roleId
	 * @return roleId
	 */
	public int getRoleId() {
		return roleId;
	}

	/**
	 *设置roleId
	 * @param roleId
	 */
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
}