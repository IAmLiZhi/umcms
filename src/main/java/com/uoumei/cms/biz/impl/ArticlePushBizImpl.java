package com.uoumei.cms.biz.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uoumei.base.biz.impl.BaseBizImpl;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.cms.biz.IArticlePushBiz;
import com.uoumei.cms.dao.IArticlePushDao;

@Service
public class ArticlePushBizImpl extends BaseBizImpl implements IArticlePushBiz{

	@Autowired
	IArticlePushDao articlePushDao;

	@Override
	protected IBaseDao getDao() {
		return articlePushDao;
	}

}
