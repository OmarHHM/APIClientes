package com.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Util {

	public static boolean isValidDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		format.setLenient(false);

		try {
			format.parse(date.trim());
		} catch (ParseException pe) {
			pe.printStackTrace();
			return false;
		}
		return true;
	}
}
