package it.gallo.pinmanager.util;

import org.apache.commons.lang3.RandomStringUtils;

public class PinUtils {
	
	private PinUtils() {}

	public static String getPinCode(int count) {
		return RandomStringUtils.randomNumeric(count);
	}

}
