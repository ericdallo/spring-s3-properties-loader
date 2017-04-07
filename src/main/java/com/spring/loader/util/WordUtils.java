package com.spring.loader.util;

import static java.lang.Character.toLowerCase;

public class WordUtils {

	public static String classNameloweredCaseFirstLetter(Class<?> clazz) {
		String clazzName = clazz.getSimpleName();
		return toLowerCase(clazzName.charAt(0)) + clazzName.substring(1);
	}


}
