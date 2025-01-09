
package com.uoumei.cms.util;

import java.math.BigDecimal;

public class ValueUtil {

	public static Long getLongValue(Object obj){
		if(null == obj){
			return 0L;
		}
		if(obj instanceof BigDecimal){
			return ((BigDecimal) obj).longValue();
		}else {
			return (Long)obj;
		}
	}

}
