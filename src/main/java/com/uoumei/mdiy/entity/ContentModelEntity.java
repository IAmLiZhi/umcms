package com.uoumei.mdiy.entity;

import com.uoumei.basic.entity.BaseEntity;
import java.util.Date;

/**
* 自定义模型表实体
*/
public class ContentModelEntity extends BaseEntity{
 
	/**
	 * 自增长ID
	 */
	private int cmId;
	
	/**
	 * 表名提示文字
	 */
	private String cmTipsName;
	
	/**
	 * 表单名称
	 */
	private String cmTableName;
	
	
	/**
	 * 自定义模型模块编号
	 */
	private int cmModelId;
	

	/**
	 * 获取cmId
	 * @return cmId
	 */
	public int getCmId() {
		return cmId;
	}

	/**
	 * 设置cmId
	 * @param cmId
	 */
	public void setCmId(int cmId) {
		this.cmId = cmId;
	}

	/**
	 * 获取cmTipsName
	 * @return cmTipsName
	 */
	public String getCmTipsName() {
		return cmTipsName;
	}

	/**
	 * 设置cmTipsName
	 * @param cmTipsName
	 */
	public void setCmTipsName(String cmTipsName) {
		this.cmTipsName = cmTipsName;
	}

	/**
	 * 获取cmTableName
	 * @return cmTableName
	 */
	public String getCmTableName() {
		return cmTableName;
	}

	/**
	 * 设置cmTableName
	 * @param cmTableName
	 */
	public void setCmTableName(String cmTableName) {
		this.cmTableName = cmTableName;
	}
	

	public int getCmModelId() {
		return cmModelId;
	}

	public void setCmModelId(int cmModelId) {
		this.cmModelId = cmModelId;
	}
	
}
