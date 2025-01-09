package com.uoumei.cms.biz;

import java.util.Date;
import java.util.List;

import com.uoumei.base.biz.IBaseBiz;
import com.uoumei.cms.entity.SiteAccessCount;


/**
 * 文章推送
 */
public interface ISiteAccessCountBiz extends IBaseBiz{
	public List<Object[]> statisticVisitorCountByDate(Integer siteId,Date begin, Date end);

	public List<Object[]> statisticVisitorCountByYear(Integer siteId,Integer year);

	public int save(SiteAccessCount count);

	public void statisticCount(Date date, Integer siteId);
}
