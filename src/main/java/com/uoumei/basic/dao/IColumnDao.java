
package com.uoumei.basic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uoumei.base.dao.IBaseDao;
import com.uoumei.basic.entity.BasicEntity;
import com.uoumei.basic.entity.ColumnEntity;

/**
 * 栏目持久化层接口，继承IBaseDao接口
 * 
 */
public interface IColumnDao extends IBaseDao {

	
	/**
	 * 根据站点ID查询该站点下的栏目集合
	 * @param appId 站点Id
	 * @return 栏目集合
	 */
	public List<ColumnEntity> queryColumnListByWebsiteId(@Param("appId")int appId);
	
	/**
	 * 根据站点Id查询该站点下的栏目的父栏目Id为categoryCategoryId的子栏目集合
	 * @param categoryCategoryId 父栏目ID
	 * @param appId 站点Id
	 * @return 栏目集合
	 */
	public List<ColumnEntity> queryColumnByCategoryIdAndWebsiteIdAndModelId(@Param("categoryCategoryId")int categoryCategoryId,@Param("appId")int appId,@Param("modelId")Integer modelId,@Param("size")Integer size);
	
	/**
	 * 根据栏目ID查询其子栏目ID集合
	 * @param categoryId 栏目ID
	 * @param appId 栏目ID
	 * @return 子栏目ID集合
	 */
	public List<Integer> queryColumnChildIdList(@Param("categoryId")int categoryId,@Param("appId")int appId);
	
	
	/**
	 * 根据站点Id查询该站点下的栏目的父栏目Id为categoryCategoryId的子栏目集合数目统计
	 * @param categoryCategoryId 父栏目ID
	 * @param appId 站点ID
	 * @return 子栏目统计数目
	 */
	public int queryColumnChildListCountByWebsiteId(@Param("categoryCategoryId")int categoryCategoryId,@Param("appId")int appId);
	
	

	/**
	 * 获取当前应用下面对应模块的所以栏目分类
	 * @param columnWebsiteId 站点信息
	 * @param modelId 模块信息
	 * @return 记录集合
	 */
	public List<ColumnEntity> queryByAppIdAndModelId(@Param("appId")int appId, @Param("modelId")int modelId);
	//use this
	public List<ColumnEntity> queryByAppIdAndColumnIds(@Param("appId")int appId, @Param("columnIds") int[] columnIds);

}