package com.uoumei.cms.biz;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.uoumei.base.biz.IBaseBiz;
import com.uoumei.cms.entity.SiteAccessStatistic;

/**
 * 文章推送
 */
public interface ISiteAccessStatisticBiz extends IBaseBiz {

	public List<Map<String, Object>> statistic(Date begin, Date end, Integer siteId, String statisticType,String statisticValue);
	
	public List<Map<String, Object>> statisticByColumnValue(Date begin, Date end, Integer siteId, Integer target, String statisticType,
			Integer count, Integer sort);
	
	public List<String> findStatisticColumnValues(Date begin, Date end,Integer siteId, String statisticType);
	
	public List<Map<String, Object>> statisticByYear(Integer year,Integer siteId, String statisticType,String statisticValue,boolean groupByMonth,Integer orderBy);

	public List<Object[]> statisticByYearByTarget(Integer year,Integer siteId, Integer target,String statisticType,String statisticValue);
	
	public int save(SiteAccessStatistic statistic);
	
	public Map<String, Object> statisticTotal(Integer siteId);
}
