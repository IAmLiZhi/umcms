package com.uoumei.task.job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.uoumei.basic.biz.IAppBiz;
import com.uoumei.basic.entity.AppEntity;
import com.uoumei.cms.biz.ISiteAccessBiz;
import com.uoumei.cms.biz.ISiteAccessCountBiz;
import com.uoumei.cms.biz.ISiteAccessCountHourBiz;
import com.uoumei.cms.biz.ISiteAccessPagesBiz;
import com.uoumei.cms.entity.SiteAccessStatistic;
import com.uoumei.cms.util.DateUtils;
import com.uoumei.util.UseTimeUtil;

public class SiteDayJob {

	@Autowired
	private IAppBiz appBiz;
	
	@Autowired
	private ISiteAccessBiz siteAccessBiz;
	
	@Autowired
	private ISiteAccessPagesBiz siteAccessPagesBiz;
	
	@Autowired
	private ISiteAccessCountBiz siteAccessCountBiz;
	
	@Autowired
	private ISiteAccessCountHourBiz siteAccessCountHourBiz;
	
	private Logger log = LoggerFactory.getLogger(SiteDayJob.class);
	
	public void execute() {
		log.debug("SiteDayJob enter");
		long begin = System.currentTimeMillis();
		
		Date now = new Date();
		
		//for test!!!
//		now = this.getNextDayNow(now);
		//for test end
		
		List<AppEntity> websites = appBiz.queryAll();
		for(AppEntity website : websites){
			statisticPerSite(now, website.getAppId());
		}
		
		//清除以往数据
		clearBeforeDayData(now);
		log.debug("SiteDayJob USE TOTAL TIME= " + UseTimeUtil.getTimeMillis(begin));
	}
	
	private void statisticPerSite(Date now, Integer siteId){
		
		Date beforeDay = getBeforeDay(now);
		int count = siteAccessBiz.findCountByDate(beforeDay, siteId);
		if(count == 0){
			return;
		}
		//每日总流量统计
		siteAccessBiz.statisticByProperty(SiteAccessStatistic.STATISTIC_ALL, beforeDay, siteId);
		//来源统计
		siteAccessBiz.statisticByProperty(SiteAccessStatistic.STATISTIC_SOURCE, beforeDay, siteId);
		//搜索引擎统计
		siteAccessBiz.statisticByProperty(SiteAccessStatistic.STATISTIC_ENGINE, beforeDay, siteId);
		//外部链接统计
		siteAccessBiz.statisticByProperty(SiteAccessStatistic.STATISTIC_LINK, beforeDay, siteId);
		//关键词统计
		siteAccessBiz.statisticByProperty(SiteAccessStatistic.STATISTIC_KEYWORD, beforeDay, siteId);
		//访问页数情况统计
		siteAccessCountBiz.statisticCount(beforeDay, siteId);
		//统计时间段数据
		siteAccessCountHourBiz.statisticCount(beforeDay, siteId);
	}
	
	private void clearBeforeDayData(Date now){
		Date d=DateUtils.getStartDate(now);
		siteAccessBiz.clearByDate(d);
		siteAccessPagesBiz.clearByDate(d);
	}
	
	private Date getBeforeDay(Date now){
		Calendar ca = Calendar.getInstance();
		ca.setTime(now);
		ca.add(Calendar.DATE, -1);
		Date beforeDay = ca.getTime();
		Date d=DateUtils.getStartDate(beforeDay);
		
		return d;
	}
	private Date getNextDayNow(Date now){
		Calendar ca = Calendar.getInstance();
		ca.setTime(now);
		ca.add(Calendar.DATE, 1);
		return ca.getTime();
	}
}
