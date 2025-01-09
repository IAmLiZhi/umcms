package com.uoumei.cms.biz.impl;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uoumei.base.biz.impl.BaseBizImpl;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.cms.biz.ISiteAccessPagesBiz;
import com.uoumei.cms.dao.ISiteAccessPagesDao;
import com.uoumei.cms.entity.SiteAccessPages;

@Service
public class SiteAccessPagesBizImpl extends BaseBizImpl implements ISiteAccessPagesBiz{

	@Autowired
	ISiteAccessPagesDao siteAccessPagesDao;

	@Override
	protected IBaseDao getDao() {
		return siteAccessPagesDao;
	}

	@Override
	public int save(SiteAccessPages access) {
		return siteAccessPagesDao.save(access);
	}

	@Override
	public int update(SiteAccessPages access) {
		// TODO Auto-generated method stub
		return siteAccessPagesDao.updateByUpdater(access);
	}

	@Override
	public SiteAccessPages findAccessPage(String sessionId, Integer pageIndex) {
		return siteAccessPagesDao.findAccessPage(sessionId, pageIndex);
	}

	@Override
	public void clearByDate(Date date) {
		siteAccessPagesDao.clearByDate(date);
	}

}
