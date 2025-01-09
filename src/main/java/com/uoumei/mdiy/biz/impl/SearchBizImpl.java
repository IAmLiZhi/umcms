package com.uoumei.mdiy.biz.impl;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.basic.biz.impl.BasicBizImpl;
import com.uoumei.mdiy.biz.ISearchBiz;
import com.uoumei.mdiy.dao.ISearchDao;
import com.uoumei.mdiy.entity.SearchEntity;
import com.uoumei.util.PageUtil;

import com.uoumei.base.util.BasicUtil;

/**
 * 搜索业务层实现类，继承BasicBizImpl，实现ISearchBiz
 * 
 */
@Service("searchBiz")
public class SearchBizImpl extends BasicBizImpl implements ISearchBiz{

	/**
	 * 搜索持久化层
	 */
	@Autowired
	private ISearchDao searchDao;
	
	/**
	 * 获取searchDao
	 */
	@Override
	protected IBaseDao getDao() {
		return searchDao;
	}
/*
	@Override
	public SearchEntity getById(int searchId) {
		// TODO Auto-generated method stub
		SearchEntity search = new SearchEntity();
		search.setAppId(BasicUtil.getAppId());
		search.setSearchId(searchId);
		Object obj = searchDao.getByEntity(search);
		return obj!=null?(SearchEntity)obj:null;
	}
	*/
}
