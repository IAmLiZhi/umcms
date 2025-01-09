package com.uoumei.util;

public class UseTimeUtil {
	public static long getTimeMillis(long begin){
		long now = System.currentTimeMillis();
		return now - begin;
	}
	
}
