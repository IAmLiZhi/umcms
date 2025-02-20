package com.hjh.file.sync.util;

/**
 * 日志工具
 */
public class LogHelper {

	public static void info(String msg) {
		System.out.println("[INFO]" + msg);
	}

	public static void warn(String msg) {
		System.out.println("[WARN]" + msg);
	}

	public static void error(Exception e) {
		e.printStackTrace();
	}

}
