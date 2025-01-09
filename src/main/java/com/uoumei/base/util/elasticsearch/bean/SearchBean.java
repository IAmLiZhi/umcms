package com.uoumei.base.util.elasticsearch.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uoumei.util.StringUtil;

/**
 * 搜索引擎mapping类
 */
public  class SearchBean {


	private int pageNo = 1;
	
	private int pageSize = 20; 
	
	private String orderBy = "id";
	
	private String order = "desc";
	
	private String keyword;


	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		if(pageNo<=1) {
			pageNo = 1;
		}
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		if(StringUtil.isBlank(orderBy)) {
			orderBy = "id";
		}
		this.orderBy = orderBy;
	}

	public String getOrder() {
		return order;
	}
	

	public void setOrder(String order) {
		if(StringUtil.isBlank(order)) {
			order = "desc";
		}
		this.order = order;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	
	
}
