
package com.uoumei.basic.dao;

import java.util.List;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.basic.entity.RoleColumnEntity;

/**
 * 角色栏目关联持久化层，接口，继承IBaseDao
 */
public interface IRoleColumnDao extends IBaseDao{
	
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
	 * 根据角色编号删除对应功能
	 * @param id 角色编号
	 */
	public void deleteByRoleId(int id);
	
	/**
	 * 通过角色获取所有关联的栏目id
	 * @param roleId
	 */
	public List<RoleColumnEntity> queryByRoleId(int roleId);

}