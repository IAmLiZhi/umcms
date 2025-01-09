package com.uoumei.cms.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 互动交流发布实体类
 */
public class InteractionPublishEntity extends InteractionReplyEntity implements Serializable {
	private int id;
	private String name;
	private String phone;
	private String cardNo;
	private String title;
	private String publishContent;
	private String fileUrl;
	private int type;
	private int state;
	private Date createTime;
	private int appId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPublishContent() {
		return publishContent;
	}
	public void setPublishContent(String publishContent) {
		this.publishContent = publishContent;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	
	
	
	
	
	

}
