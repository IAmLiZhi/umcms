
package com.uoumei.base.filter;

import java.math.BigDecimal;

import com.alibaba.fastjson.serializer.ValueFilter;


public class DoubleValueFilter implements ValueFilter {

	private int digits=2;
	
	public DoubleValueFilter(){}
	
	/**
	 *  指定保留小数个数
	 * @param digits 默认2位
	 */
	public DoubleValueFilter(int digits){
		this.digits = digits;
	}
	
	@Override
	public Object process(Object object, String name, Object value) {
		// TODO Auto-generated method stub
		if (value instanceof BigDecimal || value instanceof Double || value instanceof Float) {
			return new BigDecimal(value.toString()).setScale(digits, BigDecimal.ROUND_HALF_UP);
		}
		return value;
	}

}
