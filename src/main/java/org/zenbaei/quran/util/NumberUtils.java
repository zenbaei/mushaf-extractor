package org.zenbaei.quran.util;

import java.util.HashMap;
import java.util.Map;

public class NumberUtils {
	
	private static final Map<String, Integer> arabicToEnglishMapper = new HashMap<>();
	
	static{
		/*
		int english = 48;
		int counter = 1;
		for (int i = 1632; i <= 1641; i++){
			arabicToEnglishMapper.put(i, english + counter);
			counter ++;
		}*/
		arabicToEnglishMapper.put("٠", 0);
		arabicToEnglishMapper.put("١", 1);
		arabicToEnglishMapper.put("٢", 2);
		arabicToEnglishMapper.put("٣", 3);
		arabicToEnglishMapper.put("٤", 4);
		arabicToEnglishMapper.put("٥", 5);
		arabicToEnglishMapper.put("٦", 6);
		arabicToEnglishMapper.put("٧", 7);
		arabicToEnglishMapper.put("٨", 8);
		arabicToEnglishMapper.put("٩", 9);
	}
	
	public static int convertFromArabicToEnglish(String number){
		int length = number.length();
		String englishNumber = "";
		
		for(int i = 0; i < length; i++){
			String token = number.substring(i, i + 1);
			englishNumber += mapArabicToEnglish(token);
		}

		return Integer.parseInt(englishNumber);
	}

	private static String mapArabicToEnglish(String token) {
		int english = arabicToEnglishMapper.get(token);
		return String.valueOf(english);
	}
	
	

}
