package com.uoumei.cms.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 访问统计表
 */
public class SiteAccessStatistic implements Serializable {

	public static final String STATISTIC_ALL = "all";
	public static final String STATISTIC_SOURCE = "source";
	public static final String STATISTIC_ENGINE = "engine";
	public static final String STATISTIC_LINK = "link";
	public static final String STATISTIC_KEYWORD = "keyword";
	public static final String STATISTIC_AREA = "area";
	public static final String STATISTIC_BROWER = "brower";
	public static final String STATISTIC_SYSTEM = "system";
	public static final long serialVersionUID = 1L;
	public static final int STATISTIC_TARGET_PV = 0;
	public static final int STATISTIC_TARGET_IP = 1;
	public static final int STATISTIC_TARGET_VISITORS= 2;
	public static final int STATISTIC_TARGET_VISITSECOND = 3;
	public static final int STATISTIC_TARGET_ALL = 4;
	
	public static final Integer SORT_ASC = 0;
	public static final Integer SORT_DESC = 1;
	

    private Integer accessStatisticId;

    private Date statisticDate;

    private Long pv;

    private Long ip;

    private Long visitors;

    private Long pagesAver;

    private Long visitSecondAver;

    private String statisitcType;

    private String statisticColumnValue;

    private Integer appId;

    public Integer getAccessStatisticId() {
        return accessStatisticId;
    }

    public void setAccessStatisticId(Integer accessStatisticId) {
        this.accessStatisticId = accessStatisticId;
    }

    public Date getStatisticDate() {
        return statisticDate;
    }

    public void setStatisticDate(Date statisticDate) {
        this.statisticDate = statisticDate;
    }

    public Long getPv() {
		return pv;
	}

	public void setPv(Long pv) {
		this.pv = pv;
	}

	public Long getIp() {
		return ip;
	}

	public void setIp(Long ip) {
		this.ip = ip;
	}

	public Long getVisitors() {
		return visitors;
	}

	public void setVisitors(Long visitors) {
		this.visitors = visitors;
	}

	public Long getPagesAver() {
		return pagesAver;
	}

	public void setPagesAver(Long pagesAver) {
		this.pagesAver = pagesAver;
	}

	public Long getVisitSecondAver() {
		return visitSecondAver;
	}

	public void setVisitSecondAver(Long visitSecondAver) {
		this.visitSecondAver = visitSecondAver;
	}

	public String getStatisitcType() {
        return statisitcType;
    }

    public void setStatisitcType(String statisitcType) {
        this.statisitcType = statisitcType == null ? null : statisitcType.trim();
    }

    public String getStatisticColumnValue() {
        return statisticColumnValue;
    }

    public void setStatisticColumnValue(String statisticColumnValue) {
        this.statisticColumnValue = statisticColumnValue == null ? null : statisticColumnValue.trim();
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }
}