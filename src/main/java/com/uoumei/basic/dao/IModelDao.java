
package com.uoumei.basic.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.basic.biz.IModelBiz;
import com.uoumei.basic.entity.ModelEntity;

/**
 * 模块持久化
 */
public interface IModelDao extends IBaseDao{
	
	/**
	 * 根据父模块id查找子id
	 * @return 返回子模块实体
	 */
	List<BaseEntity> queryChildList(int modelModelId);
	
	
	
	/**
	 * 根据角色ID查询模块集合
	 * @param roleId 角色ID
	 * @return 返回模块集合
	 */
	List<BaseEntity> queryModelByRoleId(int roleId);
	
	/**
	 * 根据模块编号查询模块实体
	 * @param modelCode 模块编号
	 * @return 返回模块实体
	 */
	ModelEntity getEntityByModelCode(@Param("modelCode")String modelCode);

	/**
	 * 根据模块id获取当前项目中的分类模块id，规则根据modelcode定。**99******,只用是第３位与第４位９９
	 * @param modelCodeRegex 规则。详细见IModelBiz
	 * @see IModelBiz
	 * @param modelId 模块根id
	 * @return 返回模块集合
	 */
	ModelEntity getModel(@Param("modelCodeRegex") String modelCodeRegex,@Param("modelId") int modelId);
	
}