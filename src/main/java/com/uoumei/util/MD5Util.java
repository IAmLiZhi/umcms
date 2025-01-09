
package com.uoumei.util;

import java.security.MessageDigest;

/**
 * MD5通用加密解密类
 */
public class MD5Util {
	/**
	 * 将字节数字转换成十六进制字符串
	 * @param b byte数组
	 * @return 返回转换后的字符串
	 */
	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));
		return resultSb.toString();
	}

	/**
	 * 将字节转换成十六进制字符串
	 * @param b 字节
	 * @return 返回转换后的字符串
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * MD5加密
	 * @param origin 来源
	 * @param charsetname 字符集名称
	 * @return 返回加密后的字符串
	 */
	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	/**
	 * 所有十六进制数的字符串数组
	 */
	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
}