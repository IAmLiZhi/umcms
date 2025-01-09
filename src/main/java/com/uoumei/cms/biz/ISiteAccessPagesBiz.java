package com.uoumei.cms.biz;

import java.util.Date;

import com.uoumei.base.biz.IBaseBiz;
import com.uoumei.cms.entity.SiteAccessPages;


/**
 * 文章推送
 */
public interface ISiteAccessPagesBiz extends IBaseBiz{
	public int save(SiteAccessPages access);
	
	public int update(SiteAccessPages access);

	public SiteAccessPages findAccessPage(String sessionId, Integer pageIndex);
	
	public void clearByDate(Date date);

}
