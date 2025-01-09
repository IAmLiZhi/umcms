package com.uoumei.mwebsite.entity;

import java.util.Date;

import com.uoumei.base.entity.BaseEntity;
import com.uoumei.mwebsite.constant.e.WebsiteLogEnum;

/**
 * 
 * 日志基础
 */
public class WebsiteLogEntity extends BaseEntity {

	private int logId;

	private int logAppId;

	private Date logDatetime;

	private String logIp;

	private int logIsMobile;

	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public int getLogAppId() {
		return logAppId;
	}

	public void setLogAppId(int logAppId) {
		this.logAppId = logAppId;
	}

	public Date getLogDatetime() {
		return logDatetime;
	}

	public void setLogDatetime(Date logDatetime) {
		this.logDatetime = logDatetime;
	}

	public String getLogIp() {
		return logIp;
	}

	public void setLogIp(String logIp) {
		this.logIp = logIp;
	}

	public int getLogIsMobile() {
		return logIsMobile;
	}

	public void setLogIsMobile(int logIsMobile) {
		this.logIsMobile = logIsMobile;
	}
	
	public void setLogIsMobile(WebsiteLogEnum logIsMobile) {
		this.logIsMobile = logIsMobile.toInt();
	}
	
}
