package com.uoumei.cms.entity;

import java.io.Serializable;
import java.util.Date;

import com.uoumei.base.entity.BaseEntity;

public class ArticlePushEntity extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer pushId;
    private Integer appId;
    private Integer srcAppId;
    private String srcAppName;
    private Integer srcColumnId;
    private String srcColumnName;
    private String linkTitle;
    private String linkUrl;
    private String linkThumbnails;
    private Integer destColumnId;
    private String destColumnName;
    
    private Integer state;
    private Date createTime;
    private Date pubTime;
    

	public Integer getPushId() {
		return pushId;
	}
	public void setPushId(Integer pushId) {
		this.pushId = pushId;
	}
	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
	}
	public Integer getSrcAppId() {
		return srcAppId;
	}
	public void setSrcAppId(Integer srcAppId) {
		this.srcAppId = srcAppId;
	}
	public String getSrcAppName() {
		return srcAppName;
	}
	public void setSrcAppName(String srcAppName) {
		this.srcAppName = srcAppName;
	}
	public Integer getSrcColumnId() {
		return srcColumnId;
	}
	public void setSrcColumnId(Integer srcColumnId) {
		this.srcColumnId = srcColumnId;
	}
	public String getSrcColumnName() {
		return srcColumnName;
	}
	public void setSrcColumnName(String srcColumnName) {
		this.srcColumnName = srcColumnName;
	}
	public String getLinkTitle() {
		return linkTitle;
	}
	public void setLinkTitle(String linkTitle) {
		this.linkTitle = linkTitle;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getLinkThumbnails() {
		return linkThumbnails;
	}
	public void setLinkThumbnails(String linkThumbnails) {
		this.linkThumbnails = linkThumbnails;
	}
	public Integer getDestColumnId() {
		return destColumnId;
	}
	public void setDestColumnId(Integer destColumnId) {
		this.destColumnId = destColumnId;
	}
	public String getDestColumnName() {
		return destColumnName;
	}
	public void setDestColumnName(String destColumnName) {
		this.destColumnName = destColumnName;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getPubTime() {
		return pubTime;
	}
	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}

}