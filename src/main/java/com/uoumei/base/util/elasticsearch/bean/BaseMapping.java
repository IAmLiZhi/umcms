package com.uoumei.base.util.elasticsearch.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 搜索引擎mapping类
 */
public abstract class BaseMapping implements Serializable{

	private String id;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	
}
