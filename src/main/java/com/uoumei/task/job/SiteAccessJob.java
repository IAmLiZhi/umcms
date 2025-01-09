package com.uoumei.task.job;

import java.util.Calendar;
import java.util.List;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.uoumei.cms.biz.ISiteAccessBiz;
import com.uoumei.cms.biz.ISiteAccessPagesBiz;
import com.uoumei.cms.entity.SiteAccess;
import com.uoumei.cms.entity.SiteAccessPages;
import com.uoumei.util.UseTimeUtil;

public class SiteAccessJob implements DisposableBean{
	
	@Autowired
	private ISiteAccessBiz siteAccessBiz;
	@Autowired
	private ISiteAccessPagesBiz siteAccessPagesBiz;
	
	@Autowired 
	@Qualifier("cmsAccessCache")
	private Ehcache accessCache;

	@Autowired 
	@Qualifier("cmsAccessPageCache")
	private Ehcache accessPageCache;
	
	private Logger log = LoggerFactory.getLogger(SiteAccessJob.class);
	
	public void execute() {
		log.debug("SiteAccessJob enter");
		long begin = System.currentTimeMillis();
		
		int accessCount = freshAccessCacheToDB(accessCache);
		int pagesCount = freshAccessPagesCacheToDB(accessPageCache);
		//freshSiteAttrCacheToDB(pvTotalCache,visitorTotalCache,dayPvTotalCache,dayVisitorTotalCache);
		
		// 清除缓存
		accessCache.removeAll();
		accessPageCache.removeAll();
		log.info("refresh cache access to DB: {}", accessCount);
		log.info("refresh cache pages to DB: {}", pagesCount);
		log.debug("SiteAccessJob USE TOTAL TIME= " + UseTimeUtil.getTimeMillis(begin));
	}
	private int freshAccessCacheToDB(Ehcache cache) {
		int count = 0;
		List<String> list = cache.getKeys();
		for (String key : list) {
			Element element = cache.get(key);
			if (element == null) {
				return count;
			}
			SiteAccess access = (SiteAccess) element.getObjectValue();
			if(access!=null){
				siteAccessBiz.saveOrUpdate(access);
			}
			count++;
		}
		return count;
	}
	
	private int freshAccessPagesCacheToDB(Ehcache cache){
		int count = 0;
		List<String> list = cache.getKeys();
		for (String key : list) {
			Element element = cache.get(key);
			if (element == null) {
				return count;
			}
			SiteAccessPages page = (SiteAccessPages) element.getObjectValue();
			if (page.getAccessPagesId() == null&& page.getSessionId() != null) {
				if(page.getAccessDate()==null){
					page.setAccessDate(Calendar.getInstance().getTime());
				}
				siteAccessPagesBiz.save(page);
			}else{
				siteAccessPagesBiz.update(page);
			}
			count++;
		}
		return count;
	}
	
	public void destroy() throws Exception {
		int accessCount = freshAccessCacheToDB(accessCache);
		int pagesCount = freshAccessPagesCacheToDB(accessPageCache);
		//freshSiteAttrCacheToDB(pvTotalCache,visitorTotalCache,dayPvTotalCache,dayVisitorTotalCache);
		log.info("Bean destroy.refresh cache access to DB: {}", accessCount);
		log.info("Bean destroy.refresh cache pages to DB: {}", pagesCount);
	}
}
