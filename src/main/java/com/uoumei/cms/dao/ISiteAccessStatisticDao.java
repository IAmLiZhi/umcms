package com.uoumei.cms.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.uoumei.base.dao.IBaseDao;
import com.uoumei.cms.entity.SiteAccessStatistic;

public interface ISiteAccessStatisticDao extends IBaseDao {

	public int save(SiteAccessStatistic statistic);

	public List<Map<String, Object>> statistic(@Param("begin") Date begin, @Param("end") Date end, @Param("siteId") Integer siteId,
			@Param("statisticType") String statisticType, @Param("statisticValue") String statisticValue);

	public List<Map<String, Object>> statisticByColumnValue(@Param("begin") Date begin, @Param("end") Date end,
			@Param("siteId") Integer siteId, @Param("target") Integer target,
			@Param("statisticType") String statisticType, 
			@Param("count") Integer count, @Param("sort") Integer sort);

	public List<String> findStatisticColumnValues(@Param("begin") Date begin, @Param("end") Date end,
			@Param("siteId") Integer siteId, @Param("statisticType") String statisticType);

	public List<Map<String, Object>> statisticTotal(@Param("siteId") Integer siteId);

	public List<Map<String, Object>> statisticByYear(@Param("year") Integer year, @Param("siteId") Integer siteId,
			@Param("statisticType") String statisticType, @Param("statisticValue") String statisticValue,
			@Param("groupByMonth") boolean groupByMonth, @Param("orderBy") Integer orderBy);

	public List<Object[]> statisticByYearByTarget(@Param("year") Integer year, @Param("siteId") Integer siteId,
			@Param("target") Integer target, @Param("statisticType") String statisticType,
			@Param("statisticValue") String statisticValue);
	
}
