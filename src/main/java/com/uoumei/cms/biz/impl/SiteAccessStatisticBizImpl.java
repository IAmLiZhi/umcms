package com.uoumei.cms.biz.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uoumei.base.biz.impl.BaseBizImpl;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.cms.biz.ISiteAccessStatisticBiz;
import com.uoumei.cms.dao.ISiteAccessStatisticDao;
import com.uoumei.cms.entity.SiteAccessStatistic;
import com.uoumei.cms.util.ValueUtil;

@Service
public class SiteAccessStatisticBizImpl extends BaseBizImpl implements ISiteAccessStatisticBiz{

	@Autowired
	ISiteAccessStatisticDao siteAccessStatisticDao;

	@Override
	protected IBaseDao getDao() {
		return siteAccessStatisticDao;
	}

	@Override
	public List<Map<String, Object>> statistic(Date begin, Date end, Integer siteId, String statisticType,String statisticValue) {
		return siteAccessStatisticDao.statistic(begin, end, siteId, statisticType, statisticValue);
	}

	@Override
	public List<Map<String, Object>> statisticByColumnValue(Date begin, Date end, Integer siteId, Integer target, String statisticType,
			Integer count, Integer sort) {
		return siteAccessStatisticDao.statisticByColumnValue(begin, end, siteId, target, statisticType, count, sort);
	}
	
	@Override
	public List<Map<String, Object>> statisticByYear(Integer year, Integer siteId, String statisticType, String statisticValue,
			boolean groupByMonth, Integer orderBy) {
		return siteAccessStatisticDao.statisticByYear(year, siteId, statisticType, statisticValue, groupByMonth, orderBy);
	}

	@Override
	public List<Object[]> statisticByYearByTarget(Integer year, Integer siteId, Integer target, String statisticType,
			String statisticValue) {
		return siteAccessStatisticDao.statisticByYearByTarget(year, siteId, target, statisticType, statisticValue);
	}

	@Override
	public List<String> findStatisticColumnValues(Date begin, Date end, Integer siteId, String statisticType) {
		return siteAccessStatisticDao.findStatisticColumnValues(begin, end, siteId, statisticType);
	}

	@Override
	public int save(SiteAccessStatistic statistic) {
		return siteAccessStatisticDao.save(statistic);
	}

	@Override
	public Map<String, Object> statisticTotal(Integer siteId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = siteAccessStatisticDao.statisticTotal(siteId);
		if(null == list || list.size() == 0 || null == list.get(0)){
			map = new HashMap<String, Object>();
			map.put("pv", 0L);
			map.put("ip", 0L);
			map.put("visitors", 0L);
			return map;
		}
		
		map = list.get(0);
		Long pv = ValueUtil.getLongValue(map.get("pv"));
		Long ip = ValueUtil.getLongValue(map.get("ip"));
		Long visitors = ValueUtil.getLongValue(map.get("visitors"));
		map.put("pv", pv);
		map.put("ip", ip);
		map.put("visitors", visitors);
		return map;
		
	}

}
