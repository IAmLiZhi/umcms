package com.uoumei.cms.biz.impl;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uoumei.base.biz.impl.BaseBizImpl;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.cms.biz.ISiteAccessCountBiz;
import com.uoumei.cms.dao.ISiteAccessCountDao;
import com.uoumei.cms.dao.ISiteAccessDao;
import com.uoumei.cms.entity.SiteAccessCount;

@Service
public class SiteAccessCountBizImpl extends BaseBizImpl implements ISiteAccessCountBiz{

	@Autowired
	private ISiteAccessDao accessDao;
	
	@Autowired
	ISiteAccessCountDao siteAccessCountDao;

	@Override
	protected IBaseDao getDao() {
		return siteAccessCountDao;
	}

	@Override
	public List<Object[]> statisticVisitorCountByDate(Integer siteId,
			Date begin, Date end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> statisticVisitorCountByYear(Integer siteId,
			Integer year) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int save(SiteAccessCount count) {
		return siteAccessCountDao.save(count);
	}

	@Override
	public void statisticCount(Date date, Integer siteId) {
		List<Map<String, Object>> list = accessDao.statisticByPageCount(date,siteId);
		for (Map<String, Object> map : list) {
			SiteAccessCount bean = new SiteAccessCount();
			bean.setAppId(siteId);
			bean.setStatisticDate(date);
			Long visitors = (Long)map.get("visitors");
			Integer pageCount = (Integer)map.get("pageCount");
			bean.setVisitors(visitors.intValue());
			bean.setPageCount(pageCount);
			save(bean);
		}
		
	}

}
