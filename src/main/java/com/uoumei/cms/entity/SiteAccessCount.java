package com.uoumei.cms.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 日统计页数访问情况表
 */
public class SiteAccessCount implements Serializable {
    private Integer accessCountId;

    private Integer pageCount;

    private Integer visitors;

    private Date statisticDate;

    private Integer appId;

    public Integer getAccessCountId() {
        return accessCountId;
    }

    public void setAccessCountId(Integer accessCountId) {
        this.accessCountId = accessCountId;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getVisitors() {
        return visitors;
    }

    public void setVisitors(Integer visitors) {
        this.visitors = visitors;
    }

    public Date getStatisticDate() {
        return statisticDate;
    }

    public void setStatisticDate(Date statisticDate) {
        this.statisticDate = statisticDate;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }
}