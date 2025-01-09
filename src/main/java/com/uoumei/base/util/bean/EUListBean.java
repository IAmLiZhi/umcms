
package com.uoumei.base.util.bean;

import java.util.List;

import com.github.pagehelper.PageInfo;

/**
 * 给返回json对象，支持分页
 */
public class EUListBean {

	private int total;

	private List rows;


	public EUListBean(List rows, int total) {
		this.total = total;
		this.rows = rows;
	}


	public int getTotal() {
		return total;
	}


	public void setTotal(int total) {
		this.total = total;
	}


	public List getRows() {
		return rows;
	}


	public void setRows(List rows) {
		this.rows = rows;
	}

}
