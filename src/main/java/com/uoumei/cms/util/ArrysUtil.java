
package com.uoumei.cms.util;

import java.util.Arrays;

import cn.hutool.core.util.ArrayUtil;


/**
 * 数组工具类
 */
public class ArrysUtil {
	/**
	 * 降序排序
	 * @param str
	 * @param delimiter
	 * 			分隔符
	 * @return
	 */
	public static String sort(String str,String delimiter){
		String[] articleTypeArrays = str.split(delimiter);
		Arrays.sort(articleTypeArrays);
		return ArrayUtil.join(articleTypeArrays, delimiter);
	}
}
