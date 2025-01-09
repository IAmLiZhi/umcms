
package com.uoumei.basic.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.basic.entity.CategoryEntity;
import com.uoumei.util.PageUtil;

/**
 * 类别数据持久层，继承IBaseDao
 */
public interface ICategoryDao extends IBaseDao {

	/**
     * 分页查询</br>
     * 查询分类集合</br>
	 * @param category 分类实体
	 * @param page pageUtil实体
	 * @param orderBy 排序字段
	 * @param order 排序方式true:asc false:desc
	 * @return 返回分类集合
	 * @deprecated   推荐query
	 */
	public List<CategoryEntity> queryByPageList(@Param("category")CategoryEntity category,@Param("page")PageUtil page,@Param("orderBy")String orderBy,@Param("order") boolean order);
	
    /**
     * 根据分类ID查询子分类</br>
     * @param category 分类实体
     */
	@Deprecated
    public List<CategoryEntity> queryChilds(@Param("category")CategoryEntity category);
    
    
    /**
     * 根据分类ID查询子分类总数</br>
     * @param category 分类实体
     */
	@Deprecated
    public int count(@Param("category")CategoryEntity category);
    
    
    /**
     * 根据ID批量查询分类实体
     * @param listId ID集合
     * @return 返回分类实体
     */
    @Deprecated
    public List<CategoryEntity> queryBatchCategoryById(@Param("listId")List<Integer> listId);

	/**
	 * 根据应用编号与模块编号查询分类
	 * 
	 * @param appId 应用编号
	 * @param modelId 模块编号
	 * @return 返回分类集合
	 */
    @Deprecated
	public List<CategoryEntity> queryByAppIdOrModelId(@Param("appId")Integer appId,@Param("modelId") Integer modelId);

    
	
	/**
	 * 查询当前分类下面的所有子分类
	 * @param category 必须存在categoryId categoryParentId
	 * @return
	 */
	public List<CategoryEntity> queryChildren(CategoryEntity category);
	
	/**
	 * 根据字典查询机构
	 * @param category
	 * @return
	 */
	public List queryByDictId(CategoryEntity category);
}