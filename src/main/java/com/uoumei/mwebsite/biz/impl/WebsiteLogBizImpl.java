package com.uoumei.mwebsite.biz.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uoumei.base.biz.impl.BaseBizImpl;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.mwebsite.biz.IWebsiteLogBiz;
import com.uoumei.mwebsite.dao.IWebsiteLogDao;

@Service("logBiz")
public class WebsiteLogBizImpl extends BaseBizImpl implements IWebsiteLogBiz {

	/**
	 * 声明IAppDao持久化层
	 */
	@Autowired
	private IWebsiteLogDao logDao;

	@Override
	protected IBaseDao getDao() {
		return logDao;
	}


}
