package com.amirmoulavi.watchthatpage.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-08-19
 * @since 0.0.1
 *
 */

public class MessageDigesterMixin implements MessageDigesterBehavior {

	@Override
	public String MD5(String message) {
		return digest("MD5", message);
	}
	
	private static String digest(String algorithm, String message) {
		MessageDigest md;
		StringBuilder guidBuilder=new StringBuilder();
		try {
			md = MessageDigest.getInstance(algorithm);
			byte[] hashBytes = md.digest(message.getBytes());
			for(byte bits : hashBytes)
			{
				guidBuilder.append(Integer.toHexString((bits>>4)&0xf));
				guidBuilder.append(Integer.toHexString(bits&0xf));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return guidBuilder.toString(); 
	}
}
