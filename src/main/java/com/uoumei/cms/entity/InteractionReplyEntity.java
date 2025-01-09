package com.uoumei.cms.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 互动交流回复实体类
 */
public class InteractionReplyEntity implements Serializable{
	private int id;
	private int publishId;
	private String replyContent;
	private Date replyTime;
	private int isOpen;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPublishId() {
		return publishId;
	}
	public void setPublishId(int publishId) {
		this.publishId = publishId;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public Date getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}
	public int getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}
	
	
	
	
	
	
}
