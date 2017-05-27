package org.zenbaei.quran.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SurahService {

	public static String extractSurahHeading(String block) {
		String regex = "\\s\\S+";//\\s"; // space then non space(one or more) then space
		regex = "سُورَةُ" + regex;
	
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(block);
		
		if (!m.find())
			return "";
		
		String surah = m.group() + " "; //added space for split by ayah to recognize the second token of surah
	//	surah = StringUtils.remove(surah, (char) 11);
		
		return surah;
				// StringUtils.removePattern(page.block, p.pattern());
	}
	
	public static List<String> extractMultipleSurahHeading(String block) {
		List<String> surahs = new ArrayList<>();
		String regex = "\\s\\S+";//\\s"; // space then non space(one or more) then space
		regex = "سُورَةُ" + regex;
	
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(block);
		
		while(m.find()){
			String surah = m.group();
			surahs.add(surah + " "); //added space for split by ayah to recognize the second token of surah
		}
		
		return surahs;
	}
		
}
