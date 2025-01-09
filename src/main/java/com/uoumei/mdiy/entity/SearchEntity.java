package com.uoumei.mdiy.entity;
import com.uoumei.basic.entity.BaseEntity;

/**
 * 搜索实体，继承BasicEntity
 * 
 */
public class SearchEntity extends BaseEntity{
	
	/**
	 * 自增长ID
	 */
	private int searchId;
	
	/**
	 * 搜索名称
	 */
	private String searchName;
	
	/**
	 * 搜索结果模板
	 */
	private String searchTemplets;
	
	
	private String searchType;

	/**
	 * 获取searchId
	 * @return searchId
	 */
	public int getSearchId() {
		return searchId;
	}

	/**
	 * 设置searchId
	 * @param searchId
	 */
	public void setSearchId(int searchId) {
		this.searchId = searchId;
	}

	/**
	 * 获取searchName
	 * @return searchName
	 */
	public String getSearchName() {
		return searchName;
	}

	/**
	 * 设置searchName
	 * @param searchName
	 */
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	/**
	 * 获取searchTemplets
	 * @return searchTemplets
	 */
	public String getSearchTemplets() {
		return searchTemplets;
	}

	/**
	 * 设置searchTemplets
	 * @param searchTemplets
	 */
	public void setSearchTemplets(String searchTemplets) {
		this.searchTemplets = searchTemplets;
	}


	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	
	
}
