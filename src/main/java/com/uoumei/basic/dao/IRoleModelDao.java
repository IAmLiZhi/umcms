
package com.uoumei.basic.dao;

import java.util.List;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.basic.entity.RoleModelEntity;

/**
 * 角色模块关联持久化层，接口，继承IBaseDao
 */
public interface IRoleModelDao extends IBaseDao{
	
	/**
	 * 保存该角色对应的模块集合
	 * @param roleModelList 集合
	 */
	public void saveEntity(List<RoleModelEntity> roleModelList);
	
	/**
	 * 更新该角色对应的模块集合
	 * @param roleModelList 集合
	 */
	public void updateEntity(List<RoleModelEntity> roleModelList);
	
	/**
	 * 根据角色编号删除对应功能
	 * @param id 角色编号
	 */
	public void deleteByRoleId(int id);
	
	/**
	 * 通过角色获取所有关联的模块id
	 * @param roleId
	 */
	public List<RoleModelEntity> queryByRoleId(int roleId);

}