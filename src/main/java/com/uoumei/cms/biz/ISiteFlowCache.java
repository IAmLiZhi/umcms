package com.uoumei.cms.biz;

import javax.servlet.http.HttpServletRequest;

/**
 * 站点流量缓存接口
 */
public interface ISiteFlowCache {
	public boolean  flow(HttpServletRequest request, String page,String referrer);
}
