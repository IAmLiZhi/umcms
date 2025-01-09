package com.uoumei.cms.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 访问详细页面表
 */
public class SiteAccessPages implements Serializable {
    private Integer accessPagesId;

    private String accessPage;

    private String sessionId;

    private Date accessDate;

    private Date accessTime;

    private Integer visitSecond;

    private Integer pageIndex;

    private Integer appId;

    public Integer getAccessPagesId() {
        return accessPagesId;
    }

    public void setAccessPagesId(Integer accessPagesId) {
        this.accessPagesId = accessPagesId;
    }

    public String getAccessPage() {
        return accessPage;
    }

    public void setAccessPage(String accessPage) {
        this.accessPage = accessPage == null ? null : accessPage.trim();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId == null ? null : sessionId.trim();
    }

    public Date getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(Date accessDate) {
        this.accessDate = accessDate;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    public Integer getVisitSecond() {
        return visitSecond;
    }

    public void setVisitSecond(Integer visitSecond) {
        this.visitSecond = visitSecond;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }
}