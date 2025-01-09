package com.uoumei.cms.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.uoumei.base.dao.IBaseDao;
import com.uoumei.cms.entity.SiteAccessPages;

public interface ISiteAccessPagesDao extends IBaseDao {
	
	public SiteAccessPages findAccessPage(@Param("sessionId")String sessionId, @Param("pageIndex")Integer pageIndex);
	
	public int save(SiteAccessPages access);
	
	public int updateByUpdater(SiteAccessPages updater);
	
	public void clearByDate(@Param("date")Date date);
}
