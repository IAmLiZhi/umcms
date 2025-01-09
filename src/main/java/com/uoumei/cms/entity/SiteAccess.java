package com.uoumei.cms.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 站点访问表
 */
public class SiteAccess implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String ENGINE_BAIDU=".baidu.";
	public static final String ENGINE_GOOGLE=".google.";
	public static final String ENGINE_YAHOO=".yahoo.";
	public static final String ENGINE_BING=".bing.";
	public static final String ENGINE_SOGOU=".sogou.";
	public static final String ENGINE_SOSO=".soso.";
	public static final String ENGINE_SO=".so.";
	
	public static final String ENGINE_BAIDU_MESSAGE="百度";
	public static final String ENGINE_GOOGLE_MESSAGE="谷歌";
	public static final String ENGINE_YAHOO_MESSAGE="雅虎";
	public static final String ENGINE_BING_MESSAGE="必应";
	public static final String ENGINE_SOGOU_MESSAGE="搜狗";
	public static final String ENGINE_SOSO_MESSAGE="搜搜";
	public static final String ENGINE_SO_MESSAGE="搜";
	
	public static final String SOURCE_DIRECT_MESSAGE="直接访问";
	public static final String SOURCE_ENGINE_MESSAGE="搜索引擎";
	public static final String SOURCE_EXTERNAL_MESSAGE="外部链接";
	
    private Integer accessId;

    private String sessionId;

    private Integer appId;

    private Date accessTime;

    private Date accessDate;

    private String ip;

    private String area;

    private String accessSource;

    private String externalLink;

    private String engine;

    private String entryPage;

    private String lastStopPage;

    private Integer visitSecond;

    private Integer visitPageCount;

    private String operatingSystem;

    private String browser;

    private String keyword;

    public Integer getAccessId() {
        return accessId;
    }

    public void setAccessId(Integer accessId) {
        this.accessId = accessId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId == null ? null : sessionId.trim();
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    public Date getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(Date accessDate) {
        this.accessDate = accessDate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getAccessSource() {
        return accessSource;
    }

    public void setAccessSource(String accessSource) {
        this.accessSource = accessSource == null ? null : accessSource.trim();
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink == null ? null : externalLink.trim();
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine == null ? null : engine.trim();
    }

    public String getEntryPage() {
        return entryPage;
    }

    public void setEntryPage(String entryPage) {
        this.entryPage = entryPage == null ? null : entryPage.trim();
    }

    public String getLastStopPage() {
        return lastStopPage;
    }

    public void setLastStopPage(String lastStopPage) {
        this.lastStopPage = lastStopPage == null ? null : lastStopPage.trim();
    }

    public Integer getVisitSecond() {
        return visitSecond;
    }

    public void setVisitSecond(Integer visitSecond) {
        this.visitSecond = visitSecond;
    }

    public Integer getVisitPageCount() {
		return visitPageCount;
	}

	public void setVisitPageCount(Integer visitPageCount) {
		this.visitPageCount = visitPageCount;
	}

	public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem == null ? null : operatingSystem.trim();
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser == null ? null : browser.trim();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }
}