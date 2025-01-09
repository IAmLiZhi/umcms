package com.uoumei.mdiy.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.uoumei.base.dao.IBaseDao;
import com.uoumei.mdiy.entity.SearchEntity;

/**
 * 搜索持久化层，继承IBaseDao持久化层
 * 
 */
public interface ISearchDao extends IBaseDao{
	
	/**
	 * 通过数字区域来查询数据集合
	 * @param cmTableName 表名
	 * @param fieldFieldName 字段名
	 * @param preNum 第一个数
	 * @param nextNum 第二个数
	 * @return 返回数据集合
	 */
	@SuppressWarnings("rawtypes")
	public Map queryMapByNumArea(@Param("cmTableName") String cmTableName,@Param("fieldFieldName") String fieldFieldName,@Param("preNum") int preNum,@Param("nextNum") int nextNum);

	/**
	 * 通过id与应用id获取搜索
	 * @param id　主键
	 * @param appId　应用编号
	 * @return　搜索实体
	 */
	public SearchEntity getByIdAndAppId(@Param("id") int id,@Param("appId") int appId);

	
	/**
	 * 查询列表
	 * @param appId　应用编号
	 * @param start　分页
	 * @param pageSize　显示数量
	 */
	public List query(@Param("appId")int appId, @Param("start")int start, @Param("pageSize")int pageSize);
	
	/**
	 * 根据站点id查询数据表中记录集合总数
	 * @param appId 站点id
	 * @return
	 */
	int queryCount(@Param("appId")int appId);
}
