package com.uoumei.cms.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.uoumei.base.dao.IBaseDao;
import com.uoumei.cms.entity.SiteAccess;

public interface ISiteAccessDao extends IBaseDao {

	public int saveOrUpdate(SiteAccess access);

	public SiteAccess findAccessBySessionId(@Param("sessionId") String sessionId);

	/**
	 * 查询date之前最近的访问记录
	 * 
	 * @param date
	 * @return
	 */
	public SiteAccess findRecentAccess(@Param("date") Date date, @Param("siteId") Integer siteId);
	
	/**
	 * 查询date当天的访问数量
	 * 
	 * @param date
	 * @return
	 */
	public Integer findCountByDate(@Param("date") Date date, @Param("siteId") Integer siteId);

	/**
	 * 统计日期站点流量
	 * 
	 * @param date
	 * @param siteId
	 * @return
	 */
	public List<Map<String, Object>> statisticByDay(@Param("date") Date date, @Param("siteId") Integer siteId);

	/**
	 * 统计来源站点流量
	 * 
	 * @param date
	 * @param siteId
	 * @return
	 */
	public List<Map<String, Object>> statisticBySource(@Param("date") Date date, @Param("siteId") Integer siteId);

	/**
	 * 统计搜索引擎站点流量
	 * 
	 * @param date
	 * @param siteId
	 * @return
	 */
	public List<Map<String, Object>> statisticByEngine(@Param("date") Date date, @Param("siteId") Integer siteId);

	/**
	 * 统计外部链接站点流量
	 * 
	 * @param date
	 * @param siteId
	 * @return
	 */
	public List<Map<String, Object>> statisticByLink(@Param("date") Date date, @Param("siteId") Integer siteId);

	/**
	 * 统计关键词站点流量
	 * 
	 * @param date
	 * @param siteId
	 * @return
	 */
	public List<Map<String, Object>> statisticByKeyword(@Param("date") Date date, @Param("siteId") Integer siteId);

	/**
	 * 统计用户访问页数
	 * 
	 * @param date
	 * @param siteId
	 * @return
	 */
	public List<Map<String, Object>> statisticByPageCount(@Param("date") Date date, @Param("siteId") Integer siteId);

	/**
	 * 统计日期站点流量（按小时分组）
	 * 
	 * @param date
	 * @param siteId
	 * @return
	 */
	public List<Map<String, Object>> statisticByDayGroupByHour(@Param("date") Date date, @Param("siteId") Integer siteId);

	/**
	 * 查询property列值
	 * 
	 * @param property
	 * @param siteId
	 * @return
	 */
	public List<String> findPropertyValues(@Param("property") String property, @Param("siteId") Integer siteId);

	public List<Map<String, Object>> statisticToday(@Param("siteId") Integer siteId, @Param("date") Date date);

	public List<Object[]> statisticTodayByTarget(@Param("siteId") Integer siteId, @Param("target") Integer target,
			@Param("statisticColumn") String statisticColumn, @Param("statisticValue") String statisticValue);

	public void clearByDate(@Param("date") Date date);

}
