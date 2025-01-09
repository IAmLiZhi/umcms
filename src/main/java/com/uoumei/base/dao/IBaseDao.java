
package com.uoumei.base.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.uoumei.base.entity.BaseEntity;

import com.uoumei.base.util.elasticsearch.bean.BaseMapping;


public interface IBaseDao<E extends Serializable> {

	/**
	 * SQL修改表
	 * 
	 * @param table
	 *            表名称
	 * @param fileds
	 *            key:字段名称 list[0] 类型 list[1]长度 list[2]默认值 list[3]是否不填
	 */
	void alterTable(@Param("table") String table, @Param("fileds") Map fileds, @Param("type") String type);

	/**
	 * SQL总数
	 * 
	 * @param table
	 *            表名称
	 * @param wheres
	 *            条件 都是key-value对应
	 * @return 总数
	 */
	int countBySQL(@Param("table") String table, @Param("wheres") Map wheres);

	/**
	 * SQL创建表
	 * 
	 * @param table
	 *            表名称
	 * @param fileds
	 *            key:字段名称 list[0] 类型 list[1]长度 list[2]默认值 list[3]是否不填
	 */
	void createTable(@Param("table") String table, @Param("fileds") Map<Object, List> fileds);

	/**
	 * SQL动态SQL删除
	 * 
	 * @param table
	 *            表名称
	 * @param wheres
	 *            條件 都是key-value对应
	 */

	void deleteBySQL(@Param("table") String table, @Param("wheres") Map wheres);

	/**
	 * SQL删除表
	 * 
	 * @param table
	 *            表名称
	 */
	void dropTable(@Param("table") String table);

	/**
	 * SQL导入执行数据
	 * 
	 * @param sql
	 *            sql语句
	 */
	List excuteSql(@Param("sql") String sql);

	/**
	 * SQL添加记录
	 * 
	 * @param table
	 *            表名称
	 * @param fields
	 *            编号
	 */
	void insertBySQL(@Param("table") String table, @Param("fields") Map fields);

	/**
	 * SQL动态sql查询
	 * 
	 * @param table
	 *            表名称
	 * @param fields
	 *            list集合
	 * @param wheres
	 *            条件 都是key-value对应
	 * @param begin
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @param order
	 *            排序方式,true:asc;fales:desc
	 * @return 返回查询结果
	 */
	@SuppressWarnings("rawtypes")
	List queryBySQL(@Param("table") String table, @Param("fields") List<String> fields, @Param("wheres") Map wheres,
			@Param("begin") Integer begin, @Param("end") Integer end, @Param("order") String order);

	/**
	 * SQL动态SQL更新
	 * 
	 * @param table
	 *            表名称
	 * @param fields
	 *            list集合每个map都是key-value对应
	 * @param wheres
	 *            条件 都是key-value对应
	 */
	void updateBySQL(@Param("table") String table, @Param("fields") Map fields, @Param("wheres") Map wheres);

	/**
	 * 根据id集合实现批量的删除
	 * 
	 * @param ids
	 *            id集合
	 */
	void delete(@Param("ids") int[] ids);

	/**
	 * 根据id删除实体 推荐使用delete(int[] ids)
	 * 
	 * @param id
	 *            要删除的主键id
	 */
	@Deprecated
	void deleteEntity(int id);
	
	/**
	 * 通过entity条件删除对应entity
	 * @param entity
	 */
	void deleteByEntity(BaseEntity entity);

	/**
	 * 根据ID查询实体信息
	 * 
	 * @param id
	 *            实体ID
	 * @return 返回base实体
	 */
	BaseEntity getEntity(Integer id);
	/**
	 * 根据entity查询实体信息
	 * 
	 * @param entity 
	 *            实体
	 * @return 返回base实体
	 */
	<E>E getByEntity(BaseEntity entity);

	/**
	 * 查询
	 */
	List<E> query(BaseEntity entity);

	/**
	 * 查询所有
	 * 
	 * @return 返回list数组
	 */
	List<E> queryAll();

	/**
	 * 分页查询,4.5.8版本之后推荐使用query方法查询
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            显示条数
	 * @param orderBy
	 *            排序字段
	 * @param order
	 *            order 排序方式,true:asc;fales:desc
	 * @return 返回list数组
	 */
	@Deprecated
	List<E> queryByPage(@Param("pageNo") int pageNo, @Param("pageSize") int pageSize,
			@Param("orderBy") String orderBy, @Param("order") boolean order);

	/**
	 * 查询数据表中记录集合总数
	 * 
	 * @return 返回查询总条数
	 */
	@Deprecated
	int queryCount();

	/**
	 * 批量新增
	 * 
	 * @param list
	 *            新增数据
	 */
	void saveBatch(@Param("list") List list);

	/**
	 * 保存
	 * 
	 * @param entity
	 *            实体
	 * @return 返回保存后的id
	 */
	int saveEntity(BaseEntity entity);

	/**
	 * 更新实体
	 * 
	 * @param entity
	 *            实体
	 */
	void updateEntity(BaseEntity entity);
	
	/**
	 * 查询，只提供给搜索引擎做数据同步使用
	 * @param base mapping实体
	 * @return 单条或多条数据
	 */
	List<BaseMapping> queryForSearchMapping(BaseMapping base);
	
}