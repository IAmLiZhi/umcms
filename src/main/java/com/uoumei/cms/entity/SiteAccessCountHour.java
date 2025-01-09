package com.uoumei.cms.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 小时数据统计表
 */
public class SiteAccessCountHour implements Serializable {
    private Integer accessCountHourId;

    private Long hourPv;

    private Long hourIp;

    private Long hourUv;

    private Date accessDate;

    private Integer accessHour;

    private Integer appId;

    public Integer getAccessCountHourId() {
        return accessCountHourId;
    }

    public void setAccessCountHourId(Integer accessCountHourId) {
        this.accessCountHourId = accessCountHourId;
    }

    public Long getHourPv() {
		return hourPv;
	}

	public void setHourPv(Long hourPv) {
		this.hourPv = hourPv;
	}

	public Long getHourIp() {
		return hourIp;
	}

	public void setHourIp(Long hourIp) {
		this.hourIp = hourIp;
	}

	public Long getHourUv() {
		return hourUv;
	}

	public void setHourUv(Long hourUv) {
		this.hourUv = hourUv;
	}

	public Date getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(Date accessDate) {
        this.accessDate = accessDate;
    }

    public Integer getAccessHour() {
        return accessHour;
    }

    public void setAccessHour(Integer accessHour) {
        this.accessHour = accessHour;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }
}