package com.uoumei.activity.entity;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

public class ProcColumnEntity implements Serializable{
    private Integer id;

    private String name;

    private String description;

    private Integer sort;

    private Integer appId;

    private Integer steps;
    
    private Integer columnId;
    
    private String columnName;
    
    private String procKey;
    
    private List<ProcColumnStepsEntity> member;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public Integer getSteps() {
		return steps;
	}

	public void setSteps(Integer steps) {
		this.steps = steps;
	}

	public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	public String getProcKey() {
		return procKey;
	}

	public void setProcKey(String procKey) {
		this.procKey = procKey;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public List<ProcColumnStepsEntity> getMember() {
		return member;
	}

	public void setMember(List<ProcColumnStepsEntity> member) {
		this.member = member;
	}

	

	
    
    
}