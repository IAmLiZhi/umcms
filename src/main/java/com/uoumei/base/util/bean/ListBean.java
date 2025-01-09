
package com.uoumei.base.util.bean;

import java.util.List;

import com.github.pagehelper.PageInfo;

/**
 * 给返回json对象，支持分页
 * 
 */
public class ListBean {

	private PageInfo page;

	private List list;

	public ListBean(List list) {
		this.list = list;
	}

	public ListBean(List list, PageInfo page) {
		this.page = page;
		this.list = list;
	}

	public PageInfo getPage() {
		return page;
	}

	public void setPage(PageInfo page) {
		this.page = page;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

}
