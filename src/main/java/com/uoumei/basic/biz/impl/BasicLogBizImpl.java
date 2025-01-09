package com.uoumei.basic.biz.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uoumei.base.biz.impl.BaseBizImpl;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.basic.biz.IBasicLogBiz;
import com.uoumei.basic.dao.IBasicLogDao;
import com.uoumei.basic.entity.BasicLogEntity;
import com.uoumei.util.PageUtil;

@Service("basicLogBiz")
public class BasicLogBizImpl extends BaseBizImpl implements IBasicLogBiz {

	/**
	 * 声明IAppDao持久化层
	 */
	@Autowired
	private IBasicLogDao basicLogDao;

	@Override
	protected IBaseDao getDao() {
		return basicLogDao;
	}

	@Override
	public List<BaseEntity> query(BasicLogEntity basicLog,PageUtil page,String orderBy,boolean order) {
		// TODO Auto-generated method stub
		return basicLogDao.query(basicLog, page.getPageNo(), page.getPageSize(), orderBy, order);
	}

	@Override
	public int count(BasicLogEntity basicLog) {
		// TODO Auto-generated method stub
		return basicLogDao.count(basicLog);
	}


}
