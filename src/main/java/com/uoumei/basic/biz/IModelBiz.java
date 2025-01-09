
package com.uoumei.basic.biz;

import java.util.List;
import com.uoumei.base.biz.IBaseBiz;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.base.constant.e.BaseEnum;
import com.uoumei.basic.constant.e.ModelEnum;
import com.uoumei.basic.entity.ModelEntity;

/**
 * 模块业务接口
 * 
 */
public interface IModelBiz extends IBaseBiz {
	
	/**
	 * 通用分类
	 */
	String CATEGORY_MODEL = "99";
	
	/**
	 * 通用文章
	 */
	String BASIC_MODEL = "98";
	/**
	 * 通用订单
	 */
	String ORDER_MODEL = "97";
	 /**
	  * 通用订单状态
	  */
	String ORDER_STATUS_MODEL = "96";
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
	ModelEntity getEntityByModelCode(BaseEnum modelCode);	
	
	/**
	 * 根据模块编号查询模块实体
	 * @param modelCode 模块编号
	 * @return 返回模块实体
	 */
	ModelEntity getEntityByModelCode(String modelCode);	
	
	/**
	 * 根据模块id获取当前项目中的分类模块id，规则根据modelcode定。**99******,只用是第３位与第４位９９
	 * @param modelType 模块类型　
	 * @param modelId 模块ID
	 * @return 返回模块实体
	 */
	ModelEntity getModel(String modelType,int modelId);
}