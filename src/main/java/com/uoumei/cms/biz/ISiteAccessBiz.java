package com.uoumei.cms.biz;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.uoumei.base.biz.IBaseBiz;
import com.uoumei.cms.entity.SiteAccess;


/**
 * 文章推送
 */
public interface ISiteAccessBiz extends IBaseBiz{
	
	public List<String> findPropertyValues(String property,Integer siteId);
	
	public Map<String, Object> statisticToday(Integer siteId,Date date);
	
	public List<Object[]> statisticTodayByTarget(Integer siteId,Integer target,String statisticColumn,String statisticValue);
	
	public int findCountByDate(Date date, Integer siteId);
	
	public SiteAccess findRecentAccess(Date date, Integer siteId);
	
	public SiteAccess findAccessBySessionId(String sessionId);
	
	public int saveOrUpdate(SiteAccess access);
	
	public void clearByDate(Date date);
	
	public void statisticByProperty(String property,Date date,Integer siteId);
	
}
