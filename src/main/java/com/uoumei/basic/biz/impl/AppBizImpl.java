
package com.uoumei.basic.biz.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.basic.biz.IAppBiz;
import com.uoumei.basic.dao.IAppDao;
import com.uoumei.basic.entity.AppEntity; 

/**
 * 网站基本信息业务层实现类
 */
@Service("appBiz")
public class AppBizImpl extends BasicBizImpl implements IAppBiz{
	
	/**
	 * 声明IAppDao持久化层
	 */
	@Autowired
	private IAppDao appDao;
	
	
	@Override
	public AppEntity getByManagerId(int managerId) {
		// TODO Auto-generated method stub
		return (AppEntity) appDao.getByManagerId(managerId);
	}

	/**
	 * 获取应用持久化层
	 * @return appDao 返回应用持久化层
	 */
	@Override
	protected IBaseDao getDao() {
		// TODO Auto-generated method stub
		return appDao;
	}
	
	@Override
	public int countByUrl(String websiteUrl) {
		// TODO Auto-generated method stub
		return appDao.countByUrl(websiteUrl);
	}
	
	@Override
	public AppEntity getByUrl(String url) {
		// TODO Auto-generated method stub
		return (AppEntity) appDao.getByUrl(url);
	}
	
}