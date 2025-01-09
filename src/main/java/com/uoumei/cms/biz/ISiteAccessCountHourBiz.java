package com.uoumei.cms.biz;

import java.util.Date;
import java.util.List;

import com.uoumei.base.biz.IBaseBiz;
import com.uoumei.cms.entity.SiteAccessCountHour;


/**
 * 文章推送
 */
public interface ISiteAccessCountHourBiz extends IBaseBiz{
	
	public List<SiteAccessCountHour> getList(Date date);
	public void statisticCount(Date date, Integer siteId);
	public int save(SiteAccessCountHour bean);

}
