
package com.uoumei.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;


/**
 * AES加密解密
 */
public class AESUtil {
	
	/**
	 * LOG4j
	 */
	protected static final Logger LOG = Logger.getLogger(AESUtil.class.getClass());
	
	/**
	 * AES解密字符串
	 * @param decryptStr 要解密的字符串
	 * @param strKey key值
	 * @return 返回解密后的字符串
	 */
	public static String decrypt(String decryptStr, String strKey) {
		try {
			// 判断Key是否正确
			if (strKey == null) {
				LOG.debug("Key为空null");
				return null;
			}
			// 判断Key是否为16位
			if (strKey.length() != 16) {
				LOG.debug("Key长度不是16位");
				return null;
			}
			byte[] raw = strKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted1 = hex2byte(decryptStr);
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original);
				return originalString;
			} catch (Exception e) {
				System.out.println(e.toString());
				return null;
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return null;
		}
	}

	/**
	 * AES加密字符串
	 * @param encryptStr 要加密的字符串
	 * @param strKey key值
	 * @return 返回解密后的字符串
	 */
	public static String encrypt(String encryptStr, String strKey) {
		if (strKey == null) {
			LOG.debug("Key为空null");
			return null;
		}
		// 判断Key是否为16位
		if (strKey.length() != 16) {
			LOG.debug("Key长度不是16位");
			return null;
		}
		byte[] encrypted = null;
		try {
			byte[] raw = strKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			encrypted = cipher.doFinal(encryptStr.getBytes());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		} catch (NoSuchPaddingException  e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return byte2hex(encrypted).toLowerCase();
	}

	/**
	 * 字符串转二行制
	 * @param str 字符串
	 * @return 返回转换后的二进制数组
	 */
	public static byte[] hex2byte(String str) {
		if (str == null) {
			return null;
		}
		int l = str.length();
		if (l % 2 == 1) {
			return null;
		}
		byte[] bytes = new byte[l / 2];
		for (int i = 0; i != l / 2; i++) {
			bytes[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
		}
		return bytes;
	}

	/**
	 * 二行制转字符串
	 * @param bytes 二进制数组
	 * @return 返回转换后的字符串
	 */
	public static String byte2hex(byte[] bytes) {
		if (bytes == null) {
			return "";
		}
		String hs = "";
		String stmp = "";
		for (int n = 0; n < bytes.length; n++) {
			stmp = (java.lang.Integer.toHexString(bytes[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}
}