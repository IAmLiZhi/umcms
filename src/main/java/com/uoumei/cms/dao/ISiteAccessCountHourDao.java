package com.uoumei.cms.dao;

import java.util.Date;
import java.util.List;

import com.uoumei.base.dao.IBaseDao;
import com.uoumei.cms.entity.SiteAccessCountHour;

public interface ISiteAccessCountHourDao extends IBaseDao {
	
	public List<SiteAccessCountHour> getList(Date date);
	
	public int save(SiteAccessCountHour bean);

}
