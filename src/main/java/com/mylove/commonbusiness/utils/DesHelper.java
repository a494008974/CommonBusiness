package com.mylove.commonbusiness.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DesHelper {
	public static String encrypt(String str, String strKey) {
		try {
			IvParameterSpec zeroIv = new IvParameterSpec(strKey.getBytes());
			SecretKeySpec key = new SecretKeySpec(strKey.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			byte[] encryptedData = cipher.doFinal(str.getBytes());
			return new String(Base64.encode(encryptedData, Base64.DEFAULT));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "fail";
	}

	public static String decrypt(String str, String strKey) {
		try {
			byte[] byteMi = Base64.decode(str, Base64.DEFAULT);
			IvParameterSpec zeroIv = new IvParameterSpec(strKey.getBytes());
			SecretKeySpec key = new SecretKeySpec(strKey.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			byte decryptedData[] = cipher.doFinal(byteMi);
			return new String(decryptedData);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "fail";
	}
}
