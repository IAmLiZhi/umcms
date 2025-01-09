package com.uoumei.mwebsite.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.uoumei.base.util.DateUtils;
import com.uoumei.basic.action.BaseAction;
import com.uoumei.basic.biz.IAppBiz;
import com.uoumei.basic.entity.AppEntity;
import com.uoumei.basic.entity.ManagerSessionEntity;
import com.uoumei.cms.biz.IArticleBiz;
import com.uoumei.cms.biz.ISiteAccessBiz;
import com.uoumei.cms.biz.ISiteAccessCountHourBiz;
import com.uoumei.cms.biz.ISiteAccessStatisticBiz;
import com.uoumei.cms.constant.Const;
import com.uoumei.cms.entity.ArticleEntity;
import com.uoumei.cms.entity.SiteAccessStatistic;
import com.uoumei.util.StringUtil;

/**
 * 站群站点流量统计
 */
@Controller("mweSiteStatsAction")
@RequestMapping("/${managerPath}/mwebsite/stats")
public class SiteStatsAction extends BaseAction {
	
	@Autowired
	ISiteAccessCountHourBiz siteAccessCountHourBiz;

	@Autowired
	ISiteAccessStatisticBiz siteAccessStatisticBiz;

	@Autowired
	ISiteAccessBiz siteAccessBiz;
	
	@Autowired
	IArticleBiz articleBiz;
	
	@Autowired
	private IAppBiz appBiz;
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request, ModelMap model, HttpServletResponse response) {
		List<AppEntity> websites = appBiz.queryAll();
		model.addAttribute("websites", websites);
		
		String websiteIdParam = request.getParameter("websiteId");
		int websiteId = 0;
		if (!StringUtil.isBlank(websiteIdParam)) {
			websiteId = Integer.parseInt(request.getParameter("websiteId"));
		}else{
			if(websites != null && websites.size()>0){
				AppEntity app = websites.get(0);
				websiteId = app.getAppId();
			}
		}
		model.addAttribute("websiteId", websiteId);
		
		// 返回路径
		return view("/mwebsite/stats/index");
	}
	/**
	 * 
	 * @param begin
	 * @param end
	 */
	@RequestMapping("/pv/list")
	public void pageViewList(Integer flag, Date begin, Date end, HttpServletRequest request,
			HttpServletResponse response) {
		
		String websiteIdParam = request.getParameter("websiteId");
		if(StringUtil.isBlank(websiteIdParam)){
			return;
		}
		int siteId = Integer.parseInt(websiteIdParam);
		
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		
		if(begin==null){
			begin=DateUtils.getSpecficWeekStart(now, 0);
		}else{
			begin = DateUtils.getStartDate(begin);
		}
		if(end==null){
			end=DateUtils.getFinallyDate(now);
		}else{
			end = DateUtils.getFinallyDate(end);
		}

		int days = DateUtils.getDaysBetweenDate(begin, end);
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = siteAccessStatisticBiz.statistic(begin, end, siteId, SiteAccessStatistic.STATISTIC_ALL, null);
		// for(int i=days+2;i>1;i--){
		for (int i = days; i >= 0; i--) {
			Date d = DateUtils.getSpecficDateStart(end, -i);
			boolean includeInQueryList = false;
			for (Map<String, Object> map : list) {				
				Date statisticDate = (Date) map.get("statisticDate");
				if (DateUtils.isInDate(d, statisticDate)) {
					map.put("statisticDate", statisticDate);
					data.add(map);
					includeInQueryList = true;
					break;
				}
			}
			if (!includeInQueryList) {
				Map<String, Object>  placeMap = new HashMap<String, Object>();
				placeMap.put("pv", 0L);
				placeMap.put("ip", 0L);
				placeMap.put("visitors", 0L);
				placeMap.put("visitSecondAver", 0L);
				placeMap.put("statisticDate", d);
				data.add(placeMap);
			}
		}
		//转换日期
		for(Map<String, Object> map:data){
			Date d = (Date)map.get("statisticDate");
			map.put("statisticDate", DateUtils.getMonthDayStr(d));
		}
		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(data));
	}

	/**
	 * @param type = "all","source","engine","link","keyword","area","brower","system"
	 * @param target
	 *            展示指标(0-pv,1-IP,2-访客数 3-访问时长)
	 * @param begin
	 * @param end
	 * @param sort
	 *            排序 0时间升序 1指标值倒序 ，默认0
	 * @param count 个数           
	 */
	@RequestMapping("/source/list")
	public void sourceList(String type, Integer target, Date begin, Date end, Integer sort,
			Integer count, HttpServletRequest request, HttpServletResponse response) {
		
		String websiteIdParam = request.getParameter("websiteId");
		if(StringUtil.isBlank(websiteIdParam)){
			return;
		}
		int siteId = Integer.parseInt(websiteIdParam);
		
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		
		// 默认一个月
		if (begin == null) {
			begin = DateUtils.getSpecficMonthStart(now, -1);
		}else{
			begin = DateUtils.getStartDate(begin);
		}
		if (end == null) {
			end = DateUtils.getFinallyDate(now);
		}else{
			end = DateUtils.getFinallyDate(end);
		}
		//默认 来源
		if (StringUtils.isBlank(type)) {
			type = SiteAccessStatistic.STATISTIC_SOURCE;
		}
		//默认10条
		if (count == null) {
			count = 10;
		}
		// 默认0 (0-pv,1-IP,2-访客数 3-访问时长 )
		if (target == null) {
			target = 0;
		}
		if (sort == null) {
			sort = SiteAccessStatistic.SORT_DESC;
		}
		
		List<Map<String, Object>> list = siteAccessStatisticBiz.statisticByColumnValue(begin, end, siteId, target, type, count, sort);
		
		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(list));
	}

	/**
	 * 获得总流量
	 * @param request
	 * @param response
	 */
	@RequestMapping("/pv/count")
	public void countPV(HttpServletRequest request, HttpServletResponse response) {
		String websiteIdParam = request.getParameter("websiteId");
		if(StringUtil.isBlank(websiteIdParam)){
			return;
		}
		int siteId = Integer.parseInt(websiteIdParam);
		
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		Date accessDate = DateUtils.getStartDate(now);
		Map<String, Object> beforeMap = siteAccessStatisticBiz.statisticTotal(siteId);
		Map<String, Object> todayMap = siteAccessBiz.statisticToday(siteId, accessDate);
		Long beforePv = (Long)beforeMap.get("pv");
		Long beforeIp = (Long)beforeMap.get("ip");
		Long beforeVisitors = (Long)beforeMap.get("visitors");
		
		Long todayPv = (Long)todayMap.get("pv");
		Long todayIp = (Long)todayMap.get("ip");
		Long todayVisitors = (Long)todayMap.get("visitors");
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("pvTotal", beforePv+todayPv);
		retMap.put("ipTotal", beforeIp+todayIp);
		retMap.put("visitorsTotal", beforeVisitors+todayVisitors);
		retMap.put("pvToday", todayPv);
		retMap.put("ipToday", todayIp);
		retMap.put("visitorsToday", todayVisitors);

		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(retMap));
	}
	
	@RequestMapping("/article/count")
	public void countArticle(HttpServletRequest request, HttpServletResponse response) {
		String websiteIdParam = request.getParameter("websiteId");
		if(StringUtil.isBlank(websiteIdParam)){
			return;
		}
		int siteId = Integer.parseInt(websiteIdParam);
		
		ArticleEntity article = new ArticleEntity();
		article.setArticleState(Const.ARTICLE_STATE_PUB);
		int total = articleBiz.count(siteId, null, null, null, article);
		

		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		Date tomorrow = cal.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateTime = format.format(now);
		String endDateTime = format.format(tomorrow);
		
		int today = articleBiz.countToday(siteId, dateTime, endDateTime, article);
		
		JSONObject json = new JSONObject();
		json.put("total", total);
		json.put("today", today);
		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(json));
	}
	
	@RequestMapping("/article/list")
	public void listArticle(HttpServletRequest request, HttpServletResponse response) {
		String websiteIdParam = request.getParameter("websiteId");
		if(StringUtil.isBlank(websiteIdParam)){
			return;
		}
		int siteId = Integer.parseInt(websiteIdParam);
		
		List<ArticleEntity> list = articleBiz.queryTopHit(siteId, 10);

		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(list));
	}

}
