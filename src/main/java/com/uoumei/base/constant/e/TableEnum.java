
package com.uoumei.base.constant.e;


public enum TableEnum implements BaseEnum {
	ALTER_ADD("add"), ALTER_MODIFY("modify"), ALTER_DROP("drop");
	private String obj;

	TableEnum(String obj) {
		this.obj = obj;
	}

	@Override
	public int toInt() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String toString() {
		return this.obj.toString();
	}

}