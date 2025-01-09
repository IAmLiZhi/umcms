
package com.uoumei.basic.biz;

import java.util.List;
import com.uoumei.base.biz.IBaseBiz;
import com.uoumei.basic.entity.RoleModelEntity;

/**
 * 角色模块关联业务层接口
 */
public interface IRoleModelBiz extends IBaseBiz{
	
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
	 * 通过角色获取所有关联的模块id
	 * @param roleId
	 */
	public List<RoleModelEntity> queryByRoleId(int roleId);
}