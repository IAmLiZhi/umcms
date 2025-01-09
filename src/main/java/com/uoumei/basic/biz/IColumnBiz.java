
package com.uoumei.basic.biz;
import java.util.List;

import com.uoumei.basic.biz.ICategoryBiz;
import com.uoumei.basic.entity.BasicEntity;
import com.uoumei.basic.entity.CategoryEntity;
import com.uoumei.basic.entity.ColumnEntity;

/**
 * 栏目业务层接口，继承ICategoryBiz接口
 * 
 */
public interface IColumnBiz extends ICategoryBiz {

	
	
	/**
	 * 根据站点ID查询该站点下的栏目集合
	 * @param columnWebsiteId 站点Id
	 * @return 栏目集合
	 */
	public List<ColumnEntity> queryColumnListByWebsiteId(int columnWebsiteId);
	
	
	
	/**
	 * 根据站点Id查询该站点下的栏目的父栏目Id为categoryCategoryId的子栏目集合
	 * @param categoryCategoryId 父栏目ID
	 * @param columnWebsiteId 站点Id
	 * @param modelId 模块编号
	 * @return 栏目集合
	 */
	public List<ColumnEntity> queryChild(int categoryCategoryId,int columnWebsiteId,Integer modelId,Integer size);

	/**
	 * 获取当前应用下面对应模块的所以栏目分类
	 * @param appId 站点信息
	 * @param modelId 模块信息
	 * @return 记录集合
	 */
	@Deprecated
	public List<ColumnEntity> queryAll(int appId,int modelId);
	
	//use this
	public List<ColumnEntity> queryByColumnIds(int appId,int[] columnIds);

	/**
	 * 通过栏目ID查询该栏目同级栏目
	 * @param columnId 栏目ID
	 * @return 同级栏目集合
	 */
	public List<ColumnEntity> querySibling(int columnId,Integer size);
	
	/**
	 * 通过栏目ID查询顶级栏目的同级栏目
	 * @param columnId 栏目ID
	 * @return 顶级同级栏目集合
	 */
	public List<ColumnEntity> queryTopSiblingListByColumnId(int columnId,Integer size);
	
	/**
	 * 根据栏目Id查询栏目的子栏目集
	 * @param columnId 栏目ID
	 * @return 子栏目集合
	 */
	public List<ColumnEntity> queryChildListByColumnId(int columnId,Integer size);
	
	
	/**
	 * 根据栏目ID查询其子栏目ID集合
	 * @param columnId 栏目ID
	 * @param appId 应用ID
	 * @return 子栏目ID集合
	 */
	public int[] queryChildIdsByColumnId(int columnId,int appId);
	
	
	/**
	 * 通过栏目ID查询栏目对应节点路径上的父级栏目集合
	 * @param columnId 栏目ID
	 * @return 栏目及其父级栏目集合
	 */
	public List<ColumnEntity> queryParentColumnByColumnId(int columnId);
	
	/**
	 * 根据站点Id查询该站点下的栏目的父栏目Id为categoryCategoryId的子栏目集合数目统计
	 * @param categoryCategoryId 父栏目ID
	 * @param columnWebsiteId 站点ID
	 * @return 子栏目统计数目
	 */
	public int queryColumnChildListCountByWebsiteId(int categoryCategoryId,int columnWebsiteId);
	
	/**
	 * @Title: save  
	 * @Description: TODO(通用栏目保存)  
	 * @param @param columnEntity
	 * @param @param modelCode参数  
	 * @return void返回类型  
	 * @throws
	 */
	public void save(ColumnEntity column,int modelCode,int CategoryManagerId,String file,int appId);
	
	/**
	 * 
	 * @Title: delete  
	 * @Description: TODO(通用栏目删除)  
	 * @param @param columns参数  
	 * @return void返回类型  
	 * @throws
	 */
	public void delete(int[] columns, int appId);
	/**
	 * 
	 * @Title: update  
	 * @Description: TODO(通用栏目uapdate)  
	 * @param @param column参数  
	 * @return void返回类型  
	 * @throws
	 */
	public void update(ColumnEntity column,int modelCode,int managerId,String file, int appId);
}