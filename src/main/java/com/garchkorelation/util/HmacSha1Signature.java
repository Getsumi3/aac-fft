package com.garchkorelation.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.garchkorelation.model.User;

public class HmacSha1Signature {

	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	private static final String HMAC_KEY = "VNBt3PM44AJCOeE3l5Ait6x0g7O1atz3";

	@SuppressWarnings("resource")
	private static String toHexString(byte[] bytes) {
		Formatter formatter = new Formatter();

		for (byte b : bytes) {
			formatter.format("%02x", b);
		}

		return formatter.toString();
	}

	public static String calculateRFC2104HMAC(String data, String key) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);
		return toHexString(mac.doFinal(data.getBytes()));
	}

	public static String calculateRFC2104HMAC(String data) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		return calculateRFC2104HMAC(data, HMAC_KEY);
	}

	/**
	 * 
	 * @param user
	 * @return email+userId as value for HMAC generation
	 */
	public static String getValueFromUser(User user) {
		return user.getEmail() + user.getId();
	}
	
}