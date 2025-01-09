package com.uoumei.cms.dao;

import com.uoumei.base.dao.IBaseDao;
import com.uoumei.cms.entity.SiteAccessCount;

public interface ISiteAccessCountDao extends IBaseDao{
	public int save(SiteAccessCount bean);
}
