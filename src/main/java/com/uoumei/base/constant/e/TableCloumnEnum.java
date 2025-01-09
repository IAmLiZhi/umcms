
package com.uoumei.base.constant.e;


public enum TableCloumnEnum implements BaseEnum {
	VARCHAR(1, "varchar"), TEXT(3, "text"), INT(4, "int"), DATETIME(2, "datetime"), FLOAT(5, "float");
	private String obj;
	private int id;

	TableCloumnEnum(int id, String obj) {
		this.obj = obj;
		this.id = id;
	}

	@Override
	public int toInt() {
		// TODO Auto-generated method stub
		return id;
	}

	public String toString() {
		return this.obj.toString();
	}

}