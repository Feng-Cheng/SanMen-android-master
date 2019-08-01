package com.test.ydool.sanmen.utils;

import android.util.Base64;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Base64Util {
	private static final String key = "com.test.ydool.sanmen"; // 要求key至少长度为8个字符

	public static String getDes(String userId) {
		String result = null;
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec keySpec = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("des");
			SecretKey secretKey = keyFactory.generateSecret(keySpec);

			Cipher cipher = Cipher.getInstance("des");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
			byte[] cipherData = cipher.doFinal(userId.getBytes());
			result = Base64.encodeToString(cipherData, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
