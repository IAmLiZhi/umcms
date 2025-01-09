package com.uoumei.cms.biz.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uoumei.base.biz.impl.BaseBizImpl;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.cms.biz.ISiteAccessCountHourBiz;
import com.uoumei.cms.dao.ISiteAccessCountHourDao;
import com.uoumei.cms.dao.ISiteAccessDao;
import com.uoumei.cms.entity.SiteAccessCountHour;

@Service
public class SiteAccessCountHourBizImpl extends BaseBizImpl implements ISiteAccessCountHourBiz{

	@Autowired
	private ISiteAccessDao  accessDao;
	
	@Autowired
	ISiteAccessCountHourDao siteAccessCountHourDao;

	@Override
	protected IBaseDao getDao() {
		return siteAccessCountHourDao;
	}

	@Override
	public List<SiteAccessCountHour> getList(Date date) {
		return siteAccessCountHourDao.getList(date);
	}

	@Override
	public int save(SiteAccessCountHour bean) {
		return siteAccessCountHourDao.save(bean);
	}
	
	@Override
	public void statisticCount(Date date, Integer siteId) {
		List<Map<String, Object>> list = accessDao.statisticByDayGroupByHour(date, siteId);
		for (Map<String, Object>  map : list) {
			SiteAccessCountHour bean = new SiteAccessCountHour();
			Long pv = Long.parseLong(map.get("pv").toString());
			Long ip = Long.parseLong(map.get("ip").toString());
			Long visitors = Long.parseLong(map.get("visitors").toString());
			Integer accessHour = Integer.parseInt(map.get("accessHour").toString());
			
			bean.setAppId(siteId);
			bean.setAccessDate(date);
			bean.setHourUv(visitors);
			bean.setHourPv(pv);
			bean.setHourIp(ip);
			bean.setAccessHour(accessHour);
			save(bean);
		}
	}

}
