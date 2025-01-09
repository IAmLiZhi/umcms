
package com.uoumei.basic.biz;

import java.util.List;
import com.uoumei.base.biz.IBaseBiz;
import com.uoumei.basic.entity.RoleColumnEntity;

/**
 * 角色栏目关联业务层接口
 */
public interface IRoleColumnBiz extends IBaseBiz{
	
	/**
	 * 保存该角色对应的栏目集合
	 * @param roleColumnList 集合
	 */
	public void saveEntity(List<RoleColumnEntity> roleColumnList);
	
	/**
	 * 更新该角色对应的栏目集合
	 * @param roleColumnList 集合
	 */
	public void updateEntity(List<RoleColumnEntity> roleColumnList);
	
	/**
	 * 通过角色获取所有关联的栏目id
	 * @param roleId
	 */
	public List<RoleColumnEntity> queryByRoleId(int roleId);
}