
package com.uoumei.base.biz;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.uoumei.base.constant.e.TableEnum;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.util.PageUtil;

import com.uoumei.base.util.elasticsearch.bean.BaseMapping;

public interface IBaseBiz<E> {

	/**
	 * 修改表
	 * 
	 * @param table
	 *            表名称
	 * @param fileds
	 *            key:字段名称 list[0] 类型 list[1]长度 list[2]默认值 list[3]是否不填
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	void alterTable(String table, Map fileds, String type);

	/**
	 * 修改表
	 * 
	 * @param table
	 *            表名
	 * @param fileds
	 *            修改字段
	 * @param type
	 *            修改类型 @see TableEnum
	 */
	void alterTable(String table, Map fileds, TableEnum type);

	/**
	 * 查询表中记录总数
	 * 
	 * @param table
	 *            表名称
	 * @param wheres
	 *            条件 都是key-value对应
	 * @return 返回查询总数
	 */
	int countBySQL(String table, Map wheres);

	/**
	 * 创建表
	 * 
	 * @param table
	 *            表名称
	 * @param fileds
	 *            key:字段名称 list[0] 类型 list[1]长度 list[2]默认值 list[3]是否不填
	 */
	@SuppressWarnings("rawtypes")
	void createTable(String table, Map<Object, List> fileds);

	/**
	 * 根据id集合实现批量的删除
	 * 
	 * @param ids
	 *            id集合
	 */
	void delete(int[] ids);

	/**
	 * 动态SQL删除
	 * 
	 * @param table
	 *            表名称
	 * @param wheres
	 *            條件 都是key-value对应
	 */
	@SuppressWarnings("rawtypes")
	void deleteBySQL(String table, Map wheres);

	/**
	 * 根据id删除实体
	 * 
	 * @param ene
	 *            要删除的主键id
	 */
	void deleteEntity(BaseEntity entity);

	/**
	 * 根据id删除实体
	 * 
	 * @param id
	 *            要删除的主键id
	 */
	void deleteEntity(int id);

	/**
	 * 删除表
	 * 
	 * @param table
	 *            表名称
	 */
	void dropTable(String table);

	/**
	 * 导入执行数据
	 * 
	 * @param sql
	 *            sql语句
	 */
	Object excuteSql(String sql);

	/**
	 * 更具ID查询实体信息
	 * 
	 * @param id
	 *            实体ID
	 * @return 返回实体
	 */
	<E>E getEntity(BaseEntity entity);

	/**
	 * 更具ID查询实体信息
	 * 
	 * @param id
	 *            实体ID
	 * @return 返回实体
	 */
	<E>BaseEntity getEntity(int id);

	/**
	 * 添加记录
	 * 
	 * @param table
	 *            表名称
	 * @param fields
	 *            编号
	 */
	@SuppressWarnings("rawtypes")
	void insertBySQL(String table, Map fields);


	/**
	 * 查询
	 */
	List<E> query(BaseEntity entity);

	/**
	 * 查询所有
	 * 
	 * @return 返回list实体数组
	 */
	List<E> queryAll();

	/**
	 * 分页查询
	 * 
	 * @param page
	 *            PageUtil对象，主要封装分页的方法
	 * @param orderBy
	 *            排序字段
	 * @param order
	 *            排序方式true:asc false:desc
	 * @return 返回list实体数组
	 */
	@Deprecated
	List<E> queryByPage(PageUtil page, String orderBy, boolean order);

	/**
	 * 动态sql查询
	 * 
	 * @param table
	 *            表名称
	 * @param fields
	 *            list集合
	 * @param wheres
	 *            条件 都是key-value对应
	 * @return 返回list实体数组
	 */
	@SuppressWarnings("rawtypes")
	List queryBySQL(String table, List<String> fields, Map wheres);

	/**
	 * 动态sql查询
	 * 
	 * @param table
	 *            表名称
	 * @param fields
	 *            list集合
	 * @param wheres
	 *            条件 都是key-value对应
	 * @param begin
	 *            开始
	 * @param end
	 *            结束
	 * @return 返回list实体数组
	 */
	@SuppressWarnings("rawtypes")
	List queryBySQL(String table, List<String> fields, Map wheres, Integer begin, Integer end);

	/**
	 * 查询数据表中记录集合总数</br>
	 * 可配合分页使用</br>
	 * 
	 * @return 返回集合总数
	 */
	@Deprecated
	int queryCount();

	/**
	 * 批量新增
	 * 
	 * @param list
	 *            新增数据
	 */
	void saveBatch(List list);

	/**
	 * 保存
	 * 
	 * @param entity
	 *            实体
	 * @return 返回保存后的id
	 */
	int saveEntity(BaseEntity entity);

	/**
	 * 动态SQL更新
	 * 
	 * @param table
	 *            表名称
	 * @param fields
	 *            list集合每个map都是key-value对应
	 * @param wheres
	 *            条件 都是key-value对应
	 */
	@SuppressWarnings("rawtypes")
	void updateBySQL(String table, Map fields, Map wheres);

	/**
	 * 更新实体
	 * 
	 * @param entity
	 */
	void updateEntity(BaseEntity entity);
	
	/**
	 * 查询，只提供给搜索引擎做数据同步使用
	 * @param base mapping实体
	 * @return 单条或多条数据
	 */
	List<BaseMapping> queryForSearchMapping(BaseMapping base);

}