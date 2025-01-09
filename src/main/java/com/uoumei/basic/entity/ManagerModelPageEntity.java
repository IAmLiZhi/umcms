
package com.uoumei.basic.entity;

import com.uoumei.base.entity.BaseEntity;

/**
 * 管理页面实体类
 */
public class ManagerModelPageEntity extends BaseEntity {
	
	/**
	 * 管理员id
	 */
	private int managerModelPagemanagerId;
	
	/**
	 * 后台显示界面地址
	 */
	private String managerModelPageUrl;
	
	/**
	 * 模块编号
	 */
	private int managerModelPageModelId;

	public int getManagerModelPagemanagerId() {
		return managerModelPagemanagerId;
	}

	public void setManagerModelPagemanagerId(int managerModelPagemanagerId) {
		this.managerModelPagemanagerId = managerModelPagemanagerId;
	}

	public String getManagerModelPageUrl() {
		return managerModelPageUrl;
	}

	public void setManagerModelPageUrl(String managerModelPageUrl) {
		this.managerModelPageUrl = managerModelPageUrl;
	}

	public int getManagerModelPageModelId() {
		return managerModelPageModelId;
	}

	public void setManagerModelPageModelId(int managerModelPageModelId) {
		this.managerModelPageModelId = managerModelPageModelId;
	}
	
	
}