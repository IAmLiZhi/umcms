package com.uoumei.cms.biz.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uoumei.base.biz.impl.BaseBizImpl;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.cms.biz.ISiteAttrBiz;
import com.uoumei.cms.dao.ISiteAttrDao;

@Service
public class SiteAttrBizImpl extends BaseBizImpl implements ISiteAttrBiz{

	@Autowired
	ISiteAttrDao siteAttrDao;

	@Override
	protected IBaseDao getDao() {
		return siteAttrDao;
	}

}
