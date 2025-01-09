package com.uoumei.cms.biz.impl;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.uoumei.basic.biz.IAppBiz;
import com.uoumei.basic.entity.AppEntity;
import com.uoumei.cms.biz.ISiteAccessBiz;
import com.uoumei.cms.biz.ISiteAccessPagesBiz;
import com.uoumei.cms.biz.ISiteFlowCache;
import com.uoumei.cms.entity.SiteAccess;
import com.uoumei.cms.entity.SiteAccessPages;
import com.uoumei.cms.util.DateFormatUtils;
import com.uoumei.cms.util.DateUtils;
import com.uoumei.cms.util.RequestUtils;
import com.uoumei.cms.util.UserAgentUtils;
import com.uoumei.cms.util.ParseURLKeyword;;


@Service
public class SiteFlowCacheImpl implements ISiteFlowCache {
	
	@Autowired
	private IAppBiz appBiz;
	
	@Autowired
	private ISiteAccessBiz siteAccessBiz;
	@Autowired
	private ISiteAccessPagesBiz siteAccessPagesBiz;

	@Autowired 
	@Qualifier("cmsAccessCache")
	private Ehcache accessCache;
	@Autowired 
	@Qualifier("cmsLastAccessCache")
	private Ehcache lastAccessCache;
	@Autowired 
	@Qualifier("cmsAccessPageCache")
	private Ehcache accessPageCache;
	
	private final String VISIT_COUNT="visitCount";
	private final String LAST_VISIT_TIME="lastVisitTime";

	private Logger log = LoggerFactory.getLogger(SiteFlowCacheImpl.class);

		
	public boolean flow(HttpServletRequest request,  String page, String referer) {
		String ip = RequestUtils.getIpAddr(request);
		AppEntity app = this.getWebsite(request);
		if(null == app){
			return false;
		}
		
		Date nowTime = DateFormatUtils.parseTime(Calendar.getInstance().getTime());
		Date nowDate = DateUtils.getStartDate(Calendar.getInstance().getTime());
		HttpSession session=request.getSession();
		String sessionId =session.getId();
		Integer visitCount=(Integer) session.getAttribute(VISIT_COUNT);
		Date lastVisitTime=(Date) session.getAttribute(LAST_VISIT_TIME);
		SiteAccess access = null;
		SiteAccess lastAccess = findLastAccess(app.getAppId());
		SiteAccessPages accessPage;
		boolean firstVisitToday=false;
		boolean newVisitor=false;
		if(visitCount==null){
			visitCount=0;
			lastVisitTime=Calendar.getInstance().getTime();
			access=visitAccess(app,request, ip, sessionId, page, referer);
			//最新访问的时间比当前日期要早
			if(lastAccess==null||
					(lastAccess.getAccessDate()!=null&&
					lastAccess.getAccessDate().before(nowDate))){
				firstVisitToday=true;
			}
			newVisitor=true;
		}else{
			access=findAccess(sessionId);
			if(access==null){
				access=visitAccess(app,request, ip, sessionId, page, referer);
				newVisitor=true;
			}
			access=updateAccess(access, page, visitCount+1, DateUtils.getSecondBetweenDate(access.getAccessTime(), nowTime));
		}
		accessPage=visitPages(app,page, sessionId, visitCount, lastVisitTime);
		visitCount+=1;
		session.setAttribute(VISIT_COUNT, visitCount);
		session.setAttribute(LAST_VISIT_TIME, Calendar.getInstance().getTime());
		accessCache.put(new Element(sessionId, access));
		accessPageCache.put(new Element(sessionId+visitCount, accessPage));
		lastAccessCache.put(new Element(app.getAppId(),access));

		return true;
	}

	private SiteAccess visitAccess(AppEntity app, HttpServletRequest request,String ip, String sessionId, String page, String referer){
		
		String browser = UserAgentUtils.getBrowserInfo(request);
		String operatingSystem = UserAgentUtils.getClientOS(request);
		String accessSource=getSource(app, request, referer);
		SiteAccess bean=new SiteAccess();
		Date now=Calendar.getInstance().getTime();
		Date time = DateFormatUtils.parseTime(now);
		Date date = DateUtils.getStartDate(now);
		bean.setAccessDate(date);
		bean.setAccessTime(time);
		
		bean.setAccessSource(accessSource);
		if(accessSource.equals(SiteAccess.SOURCE_EXTERNAL_MESSAGE)){
			bean.setExternalLink(getRefererWebSite(referer));
		}
		if(enterFromEngine(request, referer)){
			bean.setEngine(getEngine(request, referer));
			bean.setExternalLink(getRefererWebSite(referer));
		}
		
		bean.setIp(ip);
//		bean.setArea(IpSeekUtils.getIpProvinceBySina(ip));
		bean.setArea("");
		bean.setBrowser(browser);
		bean.setEntryPage(page);
		bean.setKeyword(ParseURLKeyword.getKeyword(referer));
		bean.setLastStopPage(page);
		bean.setOperatingSystem(operatingSystem);
		bean.setSessionId(sessionId);
		bean.setAppId(app.getAppId());
		bean.setVisitPageCount(1);
		bean.setVisitSecond(0);
		return bean;
	}
	
	private SiteAccess updateAccess(SiteAccess bean,String lastStopPage,int visitPageCount,Integer visitSecond){
		bean.setLastStopPage(lastStopPage);
		bean.setVisitPageCount(visitPageCount);
		bean.setVisitSecond(visitSecond);
		return bean;
	}
	
	private SiteAccess findAccess(String sessionId){
		Element accessElement=accessCache.get(sessionId);
		if(accessElement!=null){
			return (SiteAccess) accessElement.getObjectValue();
		}else{
			SiteAccess access=siteAccessBiz.findAccessBySessionId(sessionId);
			return access;
		}
	}
	

	
	private SiteAccess findLastAccess(Integer siteId){
		Element accessElement=lastAccessCache.get(siteId);
		SiteAccess lastAccess =null;
		if(accessElement!=null){
			lastAccess=(SiteAccess) accessElement.getObjectValue();
		}
		return lastAccess;
		 
	}
	
	private SiteAccessPages visitPages(AppEntity app,String page,String sessionId,Integer hasVisitCount,Date lastVisitTime) {
		SiteAccessPages bean = new SiteAccessPages();
		Date now=Calendar.getInstance().getTime();
		Date time = DateFormatUtils.parseTime(now);
		Date date = DateUtils.getStartDate(now);
		bean.setAccessPage(page);
		bean.setAccessTime(time);
		bean.setAccessDate(date);
		bean.setAppId(app.getAppId());
		bean.setSessionId(sessionId);
		//设置当前访问时间0，设置上次时间
		bean.setVisitSecond(0);
		bean.setPageIndex(hasVisitCount+1);
		//accessPageCache key为sessionid+访问页面顺序
		String prePageKey=sessionId+hasVisitCount;
		Element pageElement=accessPageCache.get(prePageKey);
		//修改上个页面的访问时间(更新缓存)
		SiteAccessPages prePage=null;
		String prePageCacheKey;
		if(pageElement==null){
			prePage=siteAccessPagesBiz.findAccessPage(sessionId, hasVisitCount);
			prePageCacheKey=sessionId+hasVisitCount;
		}else{
			prePage=(SiteAccessPages) pageElement.getObjectValue();
			prePageCacheKey=(String) pageElement.getObjectKey();
		}
		if(prePage!=null){
			prePage.setVisitSecond(DateUtils.getSecondBetweenDate(prePage.getAccessTime(), time));
			accessPageCache.put(new Element(prePageCacheKey,prePage));
		}
		return bean;
	}

	private  String getRefererWebSite(String referer){
		if(StringUtils.isBlank(referer)){
			return "";
		}
		int start = 0, i = 0, count = 3;
		while (i < count && start != -1) {
			start = referer.indexOf('/', start + 1);
			i++;
		}
		/*
		if (start <= 0) {
			throw new IllegalStateException(
					"referer website uri not like 'http://.../...' pattern: "
							+ referer);
		}
		*/
		if (start > 0) {
			return referer.substring(0, start);
		}else{
			return "";
		}
	}
	
	private  String getSource(AppEntity app, HttpServletRequest request,String referer){
		if(StringUtils.isBlank(referer)){
			return SiteAccess.SOURCE_DIRECT_MESSAGE;
		}
		if(enterFromEngine(request, referer)){
			return SiteAccess.SOURCE_ENGINE_MESSAGE;
		}else{
			String refererWebSite=getRefererWebSite(referer);
//			String refererWebDomain="";
//			if(StringUtils.isNotBlank(refererWebDomain)){
//				refererWebDomain=refererWebSite.substring(refererWebSite.indexOf('/')+2);
//			}
//			if(refererWebDomain.indexOf(':')!=-1){
//				refererWebDomain=refererWebDomain.substring(0, refererWebDomain.indexOf(':'));
//			}
//			//本站域名直接访问
//			if(site.getDomain().equals(refererWebDomain)||(StringUtils.isNotBlank(site.getDomainAlias())&&site.getDomainAlias().contains(refererWebDomain))){
//				return SiteAccess.SOURCE_DIRECT_MESSAGE;
//			}else{
//				return SiteAccess.SOURCE_EXTERNAL_MESSAGE;
//			}
			if(app.getAppHostUrl().equals(refererWebSite)){
				return SiteAccess.SOURCE_DIRECT_MESSAGE;
			}else{
				return SiteAccess.SOURCE_EXTERNAL_MESSAGE;
			}
		}
	}
	
	/**
	 * 只支持常用的搜索引擎
	 * @param request
	 * @param referer
	 * @return
	 */
	private boolean enterFromEngine(HttpServletRequest request,String referer){
		if(StringUtils.isBlank(referer)){
			return false;
		}
		if(referer.indexOf(SiteAccess.ENGINE_BAIDU)!=-1){
			return true;
		}else if(referer.indexOf(SiteAccess.ENGINE_GOOGLE)!=-1){
			return true;
		}else if(referer.indexOf(SiteAccess.ENGINE_YAHOO)!=-1){
			return true;
		}else if(referer.indexOf(SiteAccess.ENGINE_BING)!=-1){
			return true;
		}else if(referer.indexOf(SiteAccess.ENGINE_SOGOU)!=-1){
			return true;
		}else if(referer.indexOf(SiteAccess.ENGINE_SOSO)!=-1){
			return true;
		}else if(referer.indexOf(SiteAccess.ENGINE_SO)!=-1){
			return true;
		}
		return false;
	}
	
	private  String getEngine(HttpServletRequest request,String referer){
		if(StringUtils.isBlank(referer)){
			return "";
		}
		if(referer.indexOf(SiteAccess.ENGINE_BAIDU)!=-1){
			return "百度";
		}else if(referer.indexOf(SiteAccess.ENGINE_GOOGLE)!=-1){
			return "谷歌";
		}else if(referer.indexOf(SiteAccess.ENGINE_YAHOO)!=-1){
			return "雅虎";
		}else if(referer.indexOf(SiteAccess.ENGINE_BING)!=-1){
			return "必应";
		}else if(referer.indexOf(SiteAccess.ENGINE_SOGOU)!=-1){
			return "搜狗";
		}else if(referer.indexOf(SiteAccess.ENGINE_SOSO)!=-1){
			return "搜搜";
		}else if(referer.indexOf(SiteAccess.ENGINE_SO)!=-1){
			return "搜";
		}
		return "";
	}
	

	private AppEntity getWebsite(HttpServletRequest request) {
		AppEntity website = appBiz.getByUrl(RequestUtils.getDomain(request));
		if (website == null) {
			return null;
		}
		return website;
	}


}
