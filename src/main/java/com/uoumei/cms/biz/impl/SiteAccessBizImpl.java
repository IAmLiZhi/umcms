package com.uoumei.cms.biz.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uoumei.base.biz.impl.BaseBizImpl;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.cms.biz.ISiteAccessBiz;
import com.uoumei.cms.biz.ISiteAccessStatisticBiz;
import com.uoumei.cms.dao.ISiteAccessDao;
import com.uoumei.cms.entity.SiteAccess;
import com.uoumei.cms.entity.SiteAccessStatistic;
import com.uoumei.cms.util.ValueUtil;

@Service
public class SiteAccessBizImpl extends BaseBizImpl implements ISiteAccessBiz{

	@Autowired
	ISiteAccessDao siteAccessDao;
	@Autowired
	private ISiteAccessStatisticBiz accessStatisticBiz;

	@Override
	protected IBaseDao getDao() {
		return siteAccessDao;
	}

	@Override
	public Map<String, Object> statisticToday(Integer siteId, Date date) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = siteAccessDao.statisticToday(siteId, date);
		if(null == list || list.size() == 0){
			map.put("pv", 0L);
			map.put("ip", 0L);
			map.put("visitors", 0L);
			map.put("visitSecond", 0L);
			return map;
		}
		map = list.get(0);
		Long pv = ValueUtil.getLongValue(map.get("pv"));
		Long ip = ValueUtil.getLongValue(map.get("ip"));
		Long visitors = ValueUtil.getLongValue(map.get("visitors"));
		Long visitSecond = ValueUtil.getLongValue(map.get("visitSecond"));
		map.put("pv", pv);
		map.put("ip", ip);
		map.put("visitors", visitors);
		map.put("visitSecond", visitSecond);
		return map;
	}

	@Override
	public List<Object[]> statisticTodayByTarget(Integer siteId, Integer target, String statisticColumn,
			String statisticValue) {
		return siteAccessDao.statisticTodayByTarget(siteId, target, statisticColumn, statisticValue);
	}

	@Override
	public List<String> findPropertyValues(String property, Integer siteId) {
		return siteAccessDao.findPropertyValues(property, siteId);
	}
	
	@Override
	public int findCountByDate(Date date, Integer siteId) {
		return siteAccessDao.findCountByDate(date, siteId);
	}
	
	@Override
	public SiteAccess findRecentAccess(Date date, Integer siteId) {
		return siteAccessDao.findRecentAccess(date, siteId);
	}

	@Override
	public SiteAccess findAccessBySessionId(String sessionId) {
		return siteAccessDao.findAccessBySessionId(sessionId);
	}

	@Override
	public int saveOrUpdate(SiteAccess access) {
		return siteAccessDao.saveOrUpdate(access);
	}

	@Override
	public void clearByDate(Date date) {
		siteAccessDao.clearByDate(date);
	}

	@Override
	public void statisticByProperty(String property, Date date, Integer siteId) {
		List<Map<String, Object>> resultes=new ArrayList<Map<String, Object>>();
		if(StringUtils.isBlank(property)){
			property=SiteAccessStatistic.STATISTIC_ALL;
		}
		if(property.equals(SiteAccessStatistic.STATISTIC_ALL)){
			resultes=siteAccessDao.statisticByDay(date,siteId);
		}else if(property.equals(SiteAccessStatistic.STATISTIC_SOURCE)){
			resultes=siteAccessDao.statisticBySource(date,siteId);
		}else if(property.equals(SiteAccessStatistic.STATISTIC_ENGINE)){
			resultes=siteAccessDao.statisticByEngine(date,siteId);
		}else if(property.equals(SiteAccessStatistic.STATISTIC_LINK)){
			resultes=siteAccessDao.statisticByLink(date,siteId);
		}else if(property.equals(SiteAccessStatistic.STATISTIC_KEYWORD)){
			resultes=siteAccessDao.statisticByKeyword(date,siteId);
		}
		for(Map<String, Object> map : resultes){
			SiteAccessStatistic sas = new SiteAccessStatistic();
//			Long pv = Long.parseLong(map.get("pv").toString());
//			Long ip = Long.parseLong(map.get("ip").toString());
//			Long visitors = Long.parseLong(map.get("visitors").toString());
//			Long visitSecond = Long.parseLong(map.get("visitSecond").toString());
			
			Long pv = ValueUtil.getLongValue(map.get("pv"));
			Long ip = ValueUtil.getLongValue(map.get("ip"));
			Long visitors = ValueUtil.getLongValue(map.get("visitors"));
			Long visitSecond = ValueUtil.getLongValue(map.get("visitSecond"));
			
			String statisticColumnValue = (String)map.get("statisticColumnValue");
			sas.setAppId(siteId);
			sas.setPv(pv);
			sas.setIp(ip);
			sas.setVisitors(visitors);
			sas.setStatisitcType(property);
			sas.setStatisticColumnValue(statisticColumnValue);
			sas.setStatisticDate(date);
			if(visitors != null && visitors >0){
				sas.setPagesAver(pv/visitors);
				sas.setVisitSecondAver(visitSecond/visitors);
			}
			accessStatisticBiz.save(sas);
			
		}
	}
}
