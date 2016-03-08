package com.cenfotec.dondeEs.logic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class AES {
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static BASE64Encoder enc = new BASE64Encoder();
	private static BASE64Decoder dec = new BASE64Decoder();
	private static AES aes;

	private AES() {
	}

	public static AES getAesInstance() {
		if (aes == null)
			aes = new AES();
		return aes;
	}

	public static String base64encode(String text) {
		try {
			String rez = enc.encode(text.getBytes(DEFAULT_ENCODING));
			return rez;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static String base64decode(String text) {
		try {
			return new String(dec.decodeBuffer(text), DEFAULT_ENCODING);
		} catch (IOException e) {
			return null;
		}
	}

}