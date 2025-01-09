
package com.uoumei.basic.entity;

import com.uoumei.base.entity.BaseEntity;

/**
 * 角色与栏目关联表
 */
public class RoleColumnEntity extends BaseEntity {

	/**
	 * 角色编号
	 */
	private int roleId;
	
	/**
	 * 栏目编号
	 */
	private int columnId;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getColumnId() {
		return columnId;
	}

	public void setColumnId(int columnId) {
		this.columnId = columnId;
	}
	

}